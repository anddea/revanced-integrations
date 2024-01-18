package app.revanced.integrations.music.patches.utils;

import androidx.annotation.Nullable;

import app.revanced.integrations.music.shared.PlayerType;

@SuppressWarnings("unused")
public class PlayerTypeHookPatch {
    /**
     * Injection point.
     */
    public static void setPlayerType(@Nullable Enum<?> musicPlayerType) {
        if (musicPlayerType == null)
            return;

        PlayerType.setFromString(musicPlayerType.name());
    }
}

