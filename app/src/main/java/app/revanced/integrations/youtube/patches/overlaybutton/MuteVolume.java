package app.revanced.integrations.youtube.patches.overlaybutton;

import android.view.View;
import android.view.ViewGroup;
import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.youtube.settings.Settings;
import app.revanced.integrations.youtube.utils.VideoUtils;

import static app.revanced.integrations.youtube.utils.VideoUtils.isAudioMuted;

@SuppressWarnings("unused")
public class MuteVolume extends BottomControlButton {
    private static MuteVolume instance;

    public MuteVolume(ViewGroup bottomControlsViewGroup) {
        super(bottomControlsViewGroup,
                "revanced_overlay_button_mute_volume",
                Settings.OVERLAY_BUTTON_MUTE_VOLUME,
                view -> {
                    VideoUtils.toggleMuteVolume();
                    if (instance != null) {
                        Logger.printInfo(() -> "isAudioMuted: " + isAudioMuted());
                        // TODO: why is this is not changing the icon?
                        instance.changeSelected(isAudioMuted());
                    }
                },
                null
        );
    }

    public static void initialize(View ViewGroup) {
        try {
            if (ViewGroup instanceof ViewGroup bottomControlsViewGroup) {
                instance = new MuteVolume(bottomControlsViewGroup);
            }
        } catch (Exception e) {
            Logger.printException(() -> "initialize failure", e);
        }
    }

    public static void changeVisibility(boolean visible, boolean animation) {
        MuteVolume muteVolume = instance;
        if (muteVolume != null)
            muteVolume.setVisibility(visible, animation);
    }

    public static void changeVisibilityNegatedImmediate() {
        MuteVolume muteVolume = instance;
        if (muteVolume != null)
            muteVolume.setVisibilityNegatedImmediate();
    }
}
