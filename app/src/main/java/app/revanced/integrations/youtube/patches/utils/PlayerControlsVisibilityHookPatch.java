package app.revanced.integrations.youtube.patches.utils;

import androidx.annotation.Nullable;

import app.revanced.integrations.youtube.shared.PlayerControlsVisibility;

@SuppressWarnings("unused")
public class PlayerControlsVisibilityHookPatch {
    /**
     * Injection point.
     */
    public static void setPlayerControlsVisibility(@Nullable Enum<?> youTubePlayerControlsVisibility) {
        if (youTubePlayerControlsVisibility == null) return;

        PlayerControlsVisibility.setFromString(youTubePlayerControlsVisibility.name());
    }
}

