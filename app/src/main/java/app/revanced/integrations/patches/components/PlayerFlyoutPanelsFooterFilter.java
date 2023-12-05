package app.revanced.integrations.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.StringTrieSearch;

/**
 * @noinspection rawtypes
 */
@SuppressWarnings("unused")
final class PlayerFlyoutPanelsFooterFilter extends Filter {

    private final StringTrieSearch exceptions = new StringTrieSearch();
    private final StringTrieSearch targetPath = new StringTrieSearch();

    public PlayerFlyoutPanelsFooterFilter() {
        exceptions.addPattern(
                "bottom_sheet_list_option"
        );

        targetPath.addPatterns(
                "captions_sheet_content.eml",
                "quality_sheet_content.eml"
        );

        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_QUALITY_FOOTER,
                        "quality_sheet_footer.eml",
                        "|divider.eml|"
                ),
                new StringFilterGroup(
                        SettingsEnum.HIDE_PLAYER_FLYOUT_PANEL_CAPTIONS_FOOTER,
                        "|ContainerType|ContainerType|ContainerType|TextType|",
                        "|divider.eml|"
                )
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (exceptions.matches(path) || !targetPath.matches(path))
            return false;

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
