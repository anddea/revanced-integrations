package app.revanced.integrations.patches.utils;

import android.content.Context;
import android.media.AudioManager;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public class AlwaysRepeatPatch {

    public static boolean enableAlwaysRepeat(boolean original) {
        return !SettingsEnum.ALWAYS_REPEAT.getBoolean() && original;
    }

    public static void shouldRepeatAndPause() {
        Context context = ReVancedUtils.getContext();
        if (context == null)
            return;

        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        if (audioManager == null)
            return;

        //noinspection deprecation
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }
}
