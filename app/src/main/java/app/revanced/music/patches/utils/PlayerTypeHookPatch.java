package app.revanced.music.patches.utils;

import androidx.annotation.Nullable;

import app.revanced.music.shared.VideoType;

@SuppressWarnings("unused")
public class PlayerTypeHookPatch {
    /**
     * Injection point.
     */
    public static void setPlayerType(@Nullable Enum<?> musicPlayerType) {
        if (musicPlayerType == null)
            return;

        VideoType.setFromString(musicPlayerType.name());
    }
}

