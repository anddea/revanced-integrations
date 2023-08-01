package app.revanced.integrations.patches.ads;

import androidx.annotation.Nullable;

import app.revanced.integrations.patches.utils.NavBarIndexPatch;
import app.revanced.integrations.settings.SettingsEnum;

public final class ShortsHeaderFilter extends Filter {

    public ShortsHeaderFilter() {
        final var shortsHeader = new StringFilterGroup(
                SettingsEnum.HIDE_SHORTS_SHELF,
                "shelf_header" // shorts shelf header is not blocked in the 'shorts_video_cell'
        );

        this.identifierFilterGroups.addAll(shortsHeader);
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        // 'shelf_header' is also used in the library tab, so use [NavBarIndexPatch] to identify it
        if (!NavBarIndexPatch.isNotLibraryTab())
            return false;

        return identifierFilterGroups == matchedList;
    }
}
