package app.revanced.integrations.patches.misc;

import app.revanced.integrations.shared.PlayerType;

@SuppressWarnings("unused")
public class MinimizedPlaybackPatch {

    public static boolean isPlaybackNotShort() {
        return !PlayerType.getCurrent().isNoneHiddenOrSlidingMinimized();
    }

}
