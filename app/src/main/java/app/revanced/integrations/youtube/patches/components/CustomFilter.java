package app.revanced.integrations.youtube.patches.components;

import static app.revanced.integrations.youtube.utils.StringRef.str;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.utils.ByteTrieSearch;
import app.revanced.integrations.youtube.utils.LogHelper;
import app.revanced.integrations.youtube.utils.ReVancedUtils;

/**
 * Allows custom filtering using a path and optionally a proto buffer string.
 */
@SuppressWarnings("unused")
final class CustomFilter extends Filter {

    private static void showInvalidSyntaxToast(@NonNull String expression) {
        ReVancedUtils.showToastLong(str("revanced_custom_filter_toast_invalid_syntax", expression));
    }

    private static void showInvalidCharactersToast(@NonNull String expression) {
        ReVancedUtils.showToastLong(str("revanced_custom_filter_toast_invalid_characters", expression));
    }

    private static class CustomFilterGroup extends StringFilterGroup {
        /**
         * Optional character for the path that indicates the custom filter path must match the start.
         * Must be the first character of the expression.
         */
        public static final String SYNTAX_STARTS_WITH = "^";

        /**
         * Optional character that separates the path from a proto buffer string pattern.
         */
        public static final String SYNTAX_BUFFER_SYMBOL = "$";

        /**
         * @return the parsed objects, or NULL if there was a parse error.
         */
        @Nullable
        @SuppressWarnings("ConstantConditions")
        static Collection<CustomFilterGroup> parseCustomFilterGroups() {
            String rawCustomFilterText = SettingsEnum.CUSTOM_FILTER_STRINGS.getString();
            if (rawCustomFilterText.isBlank()) {
                return Collections.emptyList();
            }

            // Map key is the path including optional special characters (^ and/or $)
            Map<String, CustomFilterGroup> result = new HashMap<>();
            Pattern pattern = Pattern.compile(
                    "(" // map key group
                            + "(\\Q" + SYNTAX_STARTS_WITH + "\\E?)" // optional starts with
                            + "([^\\Q" + SYNTAX_BUFFER_SYMBOL + "\\E]*)" // path
                            + "(\\Q" + SYNTAX_BUFFER_SYMBOL + "\\E?)" // optional buffer symbol
                            + ")" // end map key group
                            + "(.*)"); // optional buffer string

            for (String expression : rawCustomFilterText.split("\n")) {
                if (expression.isBlank()) continue;

                Matcher matcher = pattern.matcher(expression);
                if (!matcher.find()) {
                    showInvalidSyntaxToast(expression);
                    continue;
                }

                final String mapKey = matcher.group(1);
                final boolean pathStartsWith = !matcher.group(2).isEmpty();
                final String path = matcher.group(3);
                final boolean hasBufferSymbol = !matcher.group(4).isEmpty();
                final String bufferString = matcher.group(5);

                if (path.isBlank() || (hasBufferSymbol && bufferString.isBlank())) {
                    showInvalidSyntaxToast(expression);
                    continue;
                }

                // Use one group object for all expressions with the same path.
                // This ensures the buffer is searched exactly once
                // when multiple paths are used with different buffer strings.
                CustomFilterGroup group = result.get(mapKey);
                if (group == null) {
                    group = new CustomFilterGroup(pathStartsWith, path);
                    result.put(mapKey, group);
                }
                if (hasBufferSymbol) {
                    group.addBufferString(bufferString);
                }
            }

            return result.values();
        }

        final boolean startsWith;
        ByteTrieSearch bufferSearch;

        CustomFilterGroup(boolean startsWith, @NonNull String path) {
            super(SettingsEnum.CUSTOM_FILTER, path);
            this.startsWith = startsWith;
        }

        void addBufferString(@NonNull String bufferString) {
            if (bufferSearch == null) {
                bufferSearch = new ByteTrieSearch();
            }
            bufferSearch.addPattern(bufferString.getBytes());
        }

        @NonNull
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("CustomFilterGroup{");
            builder.append("path=");
            if (startsWith) builder.append(SYNTAX_STARTS_WITH);
            builder.append(filters[0]);

            if (bufferSearch != null) {
                String delimitingCharacter = "❙";
                builder.append(", bufferStrings=");
                builder.append(delimitingCharacter);
                for (byte[] bufferString : bufferSearch.getPatterns()) {
                    builder.append(new String(bufferString));
                    builder.append(delimitingCharacter);
                }
            }
            builder.append("}");
            return builder.toString();
        }
    }

    public CustomFilter() {
        Collection<CustomFilterGroup> groups = CustomFilterGroup.parseCustomFilterGroups();

        assert groups != null;
        if (!groups.isEmpty()) {
            CustomFilterGroup[] groupsArray = groups.toArray(new CustomFilterGroup[0]);
            LogHelper.printDebug(()-> "Using Custom filters: " + Arrays.toString(groupsArray));
            this.pathFilterGroupList.addAll(groupsArray);
        }
    }

    @Override
    public boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        // All callbacks are custom filter groups.
        CustomFilterGroup custom = (CustomFilterGroup) matchedGroup;
        if (custom.startsWith && matchedIndex != 0) {
            return false;
        }
        if (custom.bufferSearch != null && !custom.bufferSearch.matches(protobufBufferArray)) {
            return false;
        }
        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}