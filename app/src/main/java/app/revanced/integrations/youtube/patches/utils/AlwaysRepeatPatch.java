package app.revanced.integrations.youtube.patches.utils;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;

import app.revanced.integrations.youtube.utils.ReVancedUtils;
import app.revanced.integrations.youtube.settings.SettingsEnum;

public class AlwaysRepeatPatch {

    public static boolean enableAlwaysRepeat(boolean original) {
        return !SettingsEnum.ALWAYS_REPEAT.getBoolean() && original;
    }

    public static void shouldRepeatAndPause() {
        Context context = ReVancedUtils.getContext();
        if (context == null)
            return;

        pauseMedia(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE), context);
        pauseMedia(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PAUSE), context);

        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        if (audioManager == null)
            return;

        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    public static void pauseMedia(KeyEvent keyEvent, Context context) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        context.sendOrderedBroadcast(intent, null);
    }
}
