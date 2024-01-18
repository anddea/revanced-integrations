package app.revanced.integrations.youtube.patches.misc;

import app.revanced.integrations.youtube.shared.PlayerType;

@SuppressWarnings("unused")
public class MinimizedPlaybackPatch {

    public static boolean isPlaybackNotShort() {
        return !PlayerType.getCurrent().isNoneHiddenOrSlidingMinimized();
    }

}
