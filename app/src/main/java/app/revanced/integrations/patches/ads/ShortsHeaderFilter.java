package app.revanced.integrations.patches.ads;

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
    boolean isFiltered(final String path, final String identifier, final String object, final byte[] protobufBufferArray) {
        // 'shelf_header' is also used in the library tab, so use [NavBarIndexPatch] to identify it
        if (!NavBarIndexPatch.isNotLibraryTab())
            return false;

        return this.identifierFilterGroups.contains(identifier);
    }
}
