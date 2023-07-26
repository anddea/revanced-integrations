package app.revanced.integrations.patches.ads;

import app.revanced.integrations.patches.utils.NavBarIndexPatch;
import app.revanced.integrations.settings.SettingsEnum;

public final class NavBarIndexFilter extends Filter {
    public NavBarIndexFilter() {
        pathFilterGroups.addAll(new StringFilterGroup(
                SettingsEnum.HIDE_SUGGESTIONS_SHELF,
                "horizontal_video_shelf"
        ));
    }

    @Override
    boolean isFiltered(final String path, final String identifier, final String object, final byte[] protobufBufferArray) {
        if (path.contains("library_recent_shelf")) {
            // If the library shelf is detected, set the current navbar index to 4
            NavBarIndexPatch.setCurrentNavBarIndex(4);
        } else if (super.isFiltered(path, identifier, object, protobufBufferArray)) {
            // When the library shelf is not detected, but the suggestions shelf is detected
            // Block if the current navbar index is not 4
            return NavBarIndexPatch.isNotLibraryTab();
        }

        return false;
    }
}
