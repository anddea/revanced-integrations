package app.revanced.integrations.youtube.patches.components;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.revanced.integrations.shared.patches.components.Filter;
import app.revanced.integrations.shared.patches.components.StringFilterGroup;
import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.youtube.settings.Settings;
import app.revanced.integrations.youtube.shared.NavigationBar;
import app.revanced.integrations.youtube.shared.RootView;

import static app.revanced.integrations.shared.patches.components.LithoFilterPatch.LithoFilterParameters.findAsciiStrings;

@SuppressWarnings("all")
public final class FeedVideoViewsFilter extends Filter {

    private final StringFilterGroup feedVideoFilter = new StringFilterGroup(
            null,
            "video_with_context.eml",
            "video_lockup_with_attachment.eml"
    );

    public FeedVideoViewsFilter() {
        addPathCallbacks(feedVideoFilter);
    }

    private final Set<String> savedTitles = new HashSet<>();

    private boolean hideFeedVideoViewsSettingIsActive() {
        final boolean hideHome = Settings.HIDE_VIDEO_BY_VIEW_COUNTS_HOME.get();
        final boolean hideSearch = Settings.HIDE_VIDEO_BY_VIEW_COUNTS_SEARCH.get();
        final boolean hideSubscriptions = Settings.HIDE_VIDEO_BY_VIEW_COUNTS_SUBSCRIPTIONS.get();

        if (!hideHome && !hideSearch && !hideSubscriptions) {
            return false;
        } else if (hideHome && hideSearch && hideSubscriptions) {
            return true;
        }

        // Must check player type first, as search bar can be active behind the player.
        if (RootView.isPlayerActive()) {
            // For now, consider the under video results the same as the home feed.
            return hideHome;
        }

        // Must check second, as search can be from any tab.
        if (RootView.isSearchBarActive()) {
            return hideSearch;
        }

        NavigationBar.NavigationButton selectedNavButton = NavigationBar.NavigationButton.getSelectedNavigationButton();
        if (selectedNavButton == null) {
            return hideHome; // Unknown tab, treat the same as home.
        } else if (selectedNavButton == NavigationBar.NavigationButton.HOME) {
            return hideHome;
        } else if (selectedNavButton == NavigationBar.NavigationButton.SUBSCRIPTIONS) {
            return hideSubscriptions;
        }
        // User is in the Library or Notifications tab.
        return false;
    }

    @Override
    public boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                              StringFilterGroup matchedGroup, FilterContentType contentType, int contentIndex) {
        if (hideFeedVideoViewsSettingIsActive() &&
                filterByViews(protobufBufferArray)) {
            return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
        }

        String buf = protoBufferToString(protobufBufferArray);

        if (Settings.HIDE_VIDEO_BY_DURATION.get()) {
            filterByDurationAndSaveTitle(buf);
            if (!savedTitles.isEmpty()) {
                for (String savedTitle : savedTitles) {
                    if (filterByTitle(savedTitle, buf)) {
                        Logger.printDebug(() -> "Filtered by saved title: " + savedTitle);
                        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
                    }
                }
            }
        }

        return false;
    }

    private final String ARROW = " -> ";
    private final String VIEWS = "views";
    private final String[] parts = Settings.HIDE_VIDEO_VIEW_COUNTS_MULTIPLIER.get().split("\\n");
    private Pattern[] viewCountPatterns = null;

    /**
     * Hide videos based on views count
     */
    private synchronized boolean filterByViews(byte[] protobufBufferArray) {
        final String protobufString = new String(protobufBufferArray);
        final long lessThan = Settings.HIDE_VIDEO_VIEW_COUNTS_LESS_THAN.get();
        final long greaterThan = Settings.HIDE_VIDEO_VIEW_COUNTS_GREATER_THAN.get();

        if (viewCountPatterns == null) {
            viewCountPatterns = getViewCountPatterns(parts);
        }

        for (Pattern pattern : viewCountPatterns) {
            final Matcher matcher = pattern.matcher(protobufString);
            if (matcher.find()) {
                String numString = Objects.requireNonNull(matcher.group(1));
                double num = parseNumber(numString);
                String multiplierKey = matcher.group(2);
                long multiplierValue = getMultiplierValue(parts, multiplierKey);
                return num * multiplierValue < lessThan || num * multiplierValue > greaterThan;
            }
        }

        return false;
    }

    private synchronized double parseNumber(String numString) {
        /**
         * Some languages have comma (,) as a decimal separator.
         * In order to detect those numbers as doubles in Java
         * we convert commas (,) to dots (.).
         * Unless we find a language that has commas used in
         * a different manner, it should work.
         */
        numString = numString.replace(",", ".");

        /**
         * Some languages have dot (.) as a kilo separator.
         * So we check with regex if there is a number with 3+
         * digits after dot (.), we replace it with nothing
         * to make Java understand the number as a whole.
         */
        if (numString.matches("\\d+\\.\\d{3,}")) {
            numString = numString.replace(".", "");
        }

        return Double.parseDouble(numString);
    }

    private synchronized Pattern[] getViewCountPatterns(String[] parts) {
        StringBuilder prefixPatternBuilder = new StringBuilder("(\\d+(?:[.,]\\d+)?)\\s?("); // LTR layout
        StringBuilder secondPatternBuilder = new StringBuilder(); // RTL layout
        StringBuilder suffixBuilder = getSuffixBuilder(parts, prefixPatternBuilder, secondPatternBuilder);

        prefixPatternBuilder.deleteCharAt(prefixPatternBuilder.length() - 1); // Remove the trailing |
        prefixPatternBuilder.append(")?\\s*");
        prefixPatternBuilder.append(suffixBuilder.length() > 0 ? suffixBuilder.toString() : VIEWS);

        secondPatternBuilder.deleteCharAt(secondPatternBuilder.length() - 1); // Remove the trailing |
        secondPatternBuilder.append(")?");

        final Pattern[] patterns = new Pattern[2];
        patterns[0] = Pattern.compile(prefixPatternBuilder.toString());
        patterns[1] = Pattern.compile(secondPatternBuilder.toString());

        return patterns;
    }

    @NonNull
    private synchronized StringBuilder getSuffixBuilder(String[] parts, StringBuilder prefixPatternBuilder, StringBuilder secondPatternBuilder) {
        StringBuilder suffixBuilder = new StringBuilder();

        for (String part : parts) {
            final String[] pair = part.split(ARROW);
            final String pair0 = pair[0].trim();
            final String pair1 = pair[1].trim();

            if (pair.length == 2 && !pair1.equals(VIEWS)) {
                prefixPatternBuilder.append(pair0).append("|");
            }

            if (pair.length == 2 && pair1.equals(VIEWS)) {
                suffixBuilder.append(pair0);
                secondPatternBuilder.append(pair0).append("\\s*").append(prefixPatternBuilder);
            }
        }
        return suffixBuilder;
    }

    private synchronized long getMultiplierValue(String[] parts, String multiplier) {
        for (String part : parts) {
            final String[] pair = part.split(ARROW);
            final String pair0 = pair[0].trim();
            final String pair1 = pair[1].trim();

            if (pair.length == 2 && pair0.equals(multiplier) && !pair1.equals(VIEWS)) {
                return Long.parseLong(pair[1].replaceAll("[^\\d]", ""));
            }
        }

        return 1L; // Default value if not found
    }

    /**
     * Hide videos based on duration
     */
    private String filterByDurationAndSaveTitle(String protobufString) {
        Pattern pattern = getDurationAndTitlePattern();

        String shorterThanStr = Settings.HIDE_VIDEO_BY_DURATION_SHORTER_THAN.get();
        String longerThanStr = Settings.HIDE_VIDEO_BY_DURATION_LONGER_THAN.get();

        long shorterThan = parseDuration(shorterThanStr);
        long longerThan = parseDuration(longerThanStr);

        Matcher matcher = pattern.matcher(protobufString);

        // Check if pattern matches
        if (matcher.find()) {
            Logger.printDebug(() -> "DURATIONtest: " + matcher.group(2) + ":" + matcher.group(3) + " - " + matcher.group(4));

            // Group 1 to 3 are the duration components (hours?, minutes, seconds)
            String durationString = matcher.group(1) != null ? matcher.group(1) + ":" + matcher.group(2) + ":" + matcher.group(3)
                    : matcher.group(2) + ":" + matcher.group(3);
            long durationInSeconds = convertToSeconds(durationString);

            // Check the duration conditions
            if (checkDuration(durationInSeconds, shorterThan, longerThan)) {
                // Group 4 contains the title
                String title = matcher.group(4);
                savedTitles.add(title);
                return title;  // Return the title if the duration matches
            }
        }

        return null;
    }

    private boolean filterByTitle(String title, String protobufString) {
        Pattern titlePattern = Pattern.compile(Pattern.quote(title));
        Matcher titleMatcher = titlePattern.matcher(protobufString);
        return titleMatcher.find();
    }

    private Pattern getDurationAndTitlePattern() {
        // Extract both duration and title: hours? : minutes : seconds ... title
        return Pattern.compile("(?:(\\d+):)?(\\d+):(\\d+).+decoder❙([^❙]+)");
    }

    private long parseDuration(String durationString) {
        if (durationString.contains(":")) {
            return convertToSeconds(durationString);
        } else {
            return Long.parseLong(durationString);
        }
    }

    private long convertToSeconds(String durationString) {
        String[] parts = durationString.split(":");
        int length = parts.length;
        long seconds = 0;
        for (int i = length - 1; i >= 0; i--) {
            long value = Long.parseLong(parts[i]);
            seconds += (long) (value * Math.pow(60, length - 1 - i));
        }
        return seconds;
    }

    private boolean checkDuration(long durationInSeconds, long shorterThan, long longerThan) {
        if (shorterThan < 0 || longerThan < 0)
            throw new IllegalArgumentException("Duration cannot be negative.");

        return durationInSeconds < shorterThan || durationInSeconds > longerThan;
    }

    public String protoBufferToString(byte[] protoBuffer) {
        // Estimate the percentage of the buffer that are Strings.
        StringBuilder builder = new StringBuilder(Math.max(100, protoBuffer.length / 2));
        builder.append("\nBufferStrings: ");
        findAsciiStrings(builder, protoBuffer);

        return builder.toString();
    }
}
