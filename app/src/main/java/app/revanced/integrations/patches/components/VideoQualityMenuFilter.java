package app.revanced.integrations.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.patches.flyoutpanel.FlyoutPanelPatch;
import app.revanced.integrations.settings.SettingsEnum;

/**
 * Abuse LithoFilter for {@link FlyoutPanelPatch}.
 */
public final class VideoQualityMenuFilter extends Filter {
    // Must be volatile or synchronized, as litho filtering runs off main thread and this field is then access from the main thread.
    public static volatile boolean isVideoQualityMenuVisible;

    public VideoQualityMenuFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT,
                        "quick_quality_sheet_content.eml-js"
                )
        );
    }

    /**
     * @noinspection rawtypes
     */
    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        isVideoQualityMenuVisible = true;

        return false;
    }
}
