package app.revanced.integrations.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;

/**
 * @noinspection rawtypes
 */
@SuppressWarnings("unused")
public final class LowViewsFilter extends Filter {
    private static final ByteArrayAsStringFilterGroup lowViewsVideoIdentifier =
            new ByteArrayAsStringFilterGroup(
                    SettingsEnum.HIDE_VIDEO_WITH_LOW_VIEW,
                    "g-highZ"
            );

    public LowViewsFilter() {
        // [home_video_with_context.eml] is always used in home feed.
        // Add [SettingsEnum] to prevent the filter from being used even when the setting value is off.
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_VIDEO_WITH_LOW_VIEW,
                        "home_video_with_context.eml"
                )
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {

        return lowViewsVideoIdentifier.check(protobufBufferArray).isFiltered();
    }
}
