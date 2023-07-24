package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;

public final class VideoQualityMenuFilter extends Filter {
    // Must be volatile or synchronized, as litho filtering runs off main thread and this field is then access from the main thread.
    public static volatile boolean isVideoQualityMenuVisible;

    public VideoQualityMenuFilter() {
        pathFilterGroups.addAll(new StringFilterGroup(
                SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT,
                "quick_quality_sheet_content.eml-js"
        ));
    }

    @Override
    boolean isFiltered(final String path, final String identifier, final String object, final byte[] protobufBufferArray) {
        isVideoQualityMenuVisible = super.isFiltered(path, identifier, object, protobufBufferArray);

        return false;
    }
}
