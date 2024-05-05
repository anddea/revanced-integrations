package app.revanced.integrations.youtube.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.shared.NavigationBar;
import app.revanced.integrations.youtube.shared.PlayerType;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @noinspection rawtypes
 */
@SuppressWarnings("unused")
public final class LayoutComponentsFilter extends Filter {
    private static final String ENDORSEMENT_HEADER_FOOTER_PATH = "endorsement_header_footer";
    private static final ByteArrayAsStringFilterGroup grayDescriptionIdentifier =
            new ByteArrayAsStringFilterGroup(
                    SettingsEnum.HIDE_VIDEO_WITH_GRAY_DESCRIPTION,
                    ENDORSEMENT_HEADER_FOOTER_PATH
            );

    private static final ByteArrayAsStringFilterGroup lowViewsVideoIdentifier =
            new ByteArrayAsStringFilterGroup(
                    SettingsEnum.HIDE_VIDEO_WITH_LOW_VIEW_OLD,
                    "g-highZ"
            );
    private static final ByteArrayAsStringFilterGroup membershipVideoIdentifier =
            new ByteArrayAsStringFilterGroup(
                    SettingsEnum.HIDE_HOME_FEED_MEMBERSHIP_VIDEO,
                    "high-ptsZ"
            );
    private final StringFilterGroup communityPosts;
    private final StringFilterGroupList communityPostsGroupList = new StringFilterGroupList();
    private final StringFilterGroup videoWithContext;
    private final StringFilterGroup homeVideoWithContext;
    private final StringFilterGroup searchVideoWithContext;

    public LayoutComponentsFilter() {
        // Identifiers.

        final StringFilterGroup graySeparator = new StringFilterGroup(
                SettingsEnum.HIDE_GRAY_SEPARATOR,
                "cell_divider"
        );

        final StringFilterGroup chipsShelf = new StringFilterGroup(
                SettingsEnum.HIDE_CHIPS_SHELF,
                "chips_shelf"
        );

        final StringFilterGroup searchBar = new StringFilterGroup(
                SettingsEnum.HIDE_SEARCH_BAR,
                "search_bar_entry_point"
        );

        identifierFilterGroupList.addAll(
                chipsShelf,
                graySeparator,
                searchBar
        );

        // Paths.

        final StringFilterGroup albumCard = new StringFilterGroup(
                SettingsEnum.HIDE_ALBUM_CARDS,
                "browsy_bar",
                "official_card"
        );

        final StringFilterGroup audioTrackButton = new StringFilterGroup(
                SettingsEnum.HIDE_AUDIO_TRACK_BUTTON,
                "multi_feed_icon_button"
        );

        communityPosts = new StringFilterGroup(
                null,
                "post_base_wrapper",
                "image_post_root"
        );

        final StringFilterGroup expandableMetadata = new StringFilterGroup(
                SettingsEnum.HIDE_EXPANDABLE_CHIP,
                "inline_expander"
        );

        final StringFilterGroup playables = new StringFilterGroup(
                SettingsEnum.HIDE_PLAYABLES,
                "horizontal_gaming_shelf.eml"
        );

        final StringFilterGroup feedSurvey = new StringFilterGroup(
                SettingsEnum.HIDE_FEED_SURVEY,
                "feed_nudge",
                "infeed_survey",
                "in_feed_survey",
                "slimline_survey"
        );

        final StringFilterGroup grayDescription = new StringFilterGroup(
                SettingsEnum.HIDE_GRAY_DESCRIPTION,
                ENDORSEMENT_HEADER_FOOTER_PATH
        );

        videoWithContext = new StringFilterGroup(
                null,
                "video_with_context"
        );

        homeVideoWithContext = new StringFilterGroup(
                SettingsEnum.HIDE_VIDEO_WITH_LOW_VIEW_OLD,
                "home_video_with_context.eml"
        );

        final StringFilterGroup infoPanel = new StringFilterGroup(
                SettingsEnum.HIDE_INFO_PANEL,
                "compact_banner",
                "publisher_transparency_panel",
                "single_item_information_panel"
        );

        final StringFilterGroup latestPosts = new StringFilterGroup(
                SettingsEnum.HIDE_LATEST_POSTS,
                "post_shelf"
        );

        final StringFilterGroup liveChat = new StringFilterGroup(
                SettingsEnum.HIDE_LIVE_CHAT_MESSAGES,
                "live_chat_text_message",
                "viewer_engagement_message" // message about poll, not poll itself
        );

        final StringFilterGroup medicalPanel = new StringFilterGroup(
                SettingsEnum.HIDE_MEDICAL_PANEL,
                "emergency_onebox",
                "medical_panel"
        );

        final StringFilterGroup movieShelf = new StringFilterGroup(
                SettingsEnum.HIDE_MOVIE_SHELF,
                "compact_movie",
                "horizontal_movie_shelf",
                "movie_and_show_upsell_card",
                "compact_tvfilm_item",
                "offer_module",
                "offer_module_root"
        );

        final StringFilterGroup notifyMe = new StringFilterGroup(
                SettingsEnum.HIDE_NOTIFY_ME_BUTTON,
                "set_reminder_button"
        );

        searchVideoWithContext = new StringFilterGroup(
                SettingsEnum.HIDE_VIDEO_WITH_GRAY_DESCRIPTION,
                "search_video_with_context.eml"
        );

        final StringFilterGroup ticketShelf = new StringFilterGroup(
                SettingsEnum.HIDE_TICKET_SHELF,
                "ticket_horizontal_shelf",
                "ticket_shelf"
        );

        final StringFilterGroup timedReactions = new StringFilterGroup(
                SettingsEnum.HIDE_TIMED_REACTIONS,
                "emoji_control_panel",
                "timed_reaction"
        );

        pathFilterGroupList.addAll(
                albumCard,
                audioTrackButton,
                communityPosts,
                expandableMetadata,
                feedSurvey,
                grayDescription,
                homeVideoWithContext,
                infoPanel,
                latestPosts,
                liveChat,
                medicalPanel,
                movieShelf,
                notifyMe,
                playables,
                searchVideoWithContext,
                ticketShelf,
                timedReactions,
                videoWithContext
        );

        communityPostsGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_COMMUNITY_POSTS_HOME,
                        "horizontalCollectionSwipeProtector=null"
                ),
                new StringFilterGroup(
                        SettingsEnum.HIDE_COMMUNITY_POSTS_SUBSCRIPTIONS,
                        "heightConstraint=null"
                )
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {

        if (matchedGroup == videoWithContext) {
            String protobufString = new String(protobufBufferArray);
            return hideVideos(protobufString);
        }

        if (matchedGroup == homeVideoWithContext)
            return (membershipVideoIdentifier.check(protobufBufferArray).isFiltered()
                    || lowViewsVideoIdentifier.check(protobufBufferArray).isFiltered());

        if (matchedGroup == searchVideoWithContext) {
            return grayDescriptionIdentifier.check(protobufBufferArray).isFiltered();
        }
        if (matchedGroup == communityPosts) {
            if (PlayerType.getCurrent() == PlayerType.WATCH_WHILE_MAXIMIZED)
                return SettingsEnum.HIDE_COMMUNITY_POSTS_RELATED_VIDEO.getBoolean();

            // YouTube is testing new community post component in home feed
            NavigationBar.NavigationButton selectedNavButton = NavigationBar.NavigationButton.getSelectedNavigationButton();
            if (selectedNavButton == NavigationBar.NavigationButton.HOME) {
                return SettingsEnum.HIDE_COMMUNITY_POSTS_HOME.getBoolean();
            }
            return communityPostsGroupList.check(allValue).isFiltered();
        }

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }


    private boolean hideVideos(String protobufString) {
        if (SettingsEnum.HIDE_VIDEO_WITH_VIEW.getBoolean())
            return hideVideoBasedOnViews(protobufString);

        return false;
    }

    private boolean hideVideoBasedOnViews(String protobufString) {
        Pattern[] viewCountPatterns = getViewCountPatterns();

        long lessThan = SettingsEnum.HIDE_VIDEO_WITH_LESS_THAN_VIEW_NUM.getLong();
        long greaterThan = SettingsEnum.HIDE_VIDEO_WITH_GREATER_THAN_VIEW_NUM.getLong();

        for (Pattern pattern : viewCountPatterns) {
            Matcher matcher = pattern.matcher(protobufString);
            if (matcher.find()) {
                String numString = Objects.requireNonNull(matcher.group(1));
                double num = parseNumber(numString);
                String multiplierKey = matcher.group(2);
                long multiplierValue = getMultiplierValue(multiplierKey);
                return num * multiplierValue < lessThan || num * multiplierValue > greaterThan;
            }
        }

        return false;
    }

    private double parseNumber(String numString) {
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

    private static Pattern[] getViewCountPatterns() {
        String[] parts = SettingsEnum.HIDE_VIDEO_WITH_VIEW_NUM_KEYS.getString().split("\\n");
        StringBuilder prefixPatternBuilder = new StringBuilder("(\\d+(?:[.,]\\d+)?)\\s?(");
        StringBuilder secondPatternBuilder = new StringBuilder();
        StringBuilder suffixBuilder = new StringBuilder();

        for (String part : parts) {
            String[] pair = part.split(" -> ");

            if (pair.length == 2 && !pair[1].trim().equals("views")) {
                prefixPatternBuilder.append(pair[0].trim()).append("|");
            }

            if (pair.length == 2 && pair[1].trim().equals("views")) {
                suffixBuilder.append(pair[0].trim());
                secondPatternBuilder.append(pair[0].trim()).append("\\s*").append(prefixPatternBuilder);
            }
        }

        prefixPatternBuilder.deleteCharAt(prefixPatternBuilder.length() - 1); // Remove the trailing |
        prefixPatternBuilder.append(")?\\s*");
        prefixPatternBuilder.append(suffixBuilder.length() > 0 ? suffixBuilder.toString() : "views");

        secondPatternBuilder.deleteCharAt(secondPatternBuilder.length() - 1); // Remove the trailing |
        secondPatternBuilder.append(")?");

        Pattern[] patterns = new Pattern[2];
        patterns[0] = Pattern.compile(prefixPatternBuilder.toString());
        patterns[1] = Pattern.compile(secondPatternBuilder.toString());

        return patterns;
    }

    private long getMultiplierValue(String multiplier) {
        String[] parts = SettingsEnum.HIDE_VIDEO_WITH_VIEW_NUM_KEYS.getString().split("\\n");

        for (String part : parts) {
            String[] pair = part.split(" -> ");

            if (pair.length == 2 && pair[0].trim().equals(multiplier) && !pair[1].trim().equals("views")) {
                return Long.parseLong(pair[1].replaceAll("[^\\d]", ""));
            }
        }

        return 1L; // Default value if not found
    }
}
