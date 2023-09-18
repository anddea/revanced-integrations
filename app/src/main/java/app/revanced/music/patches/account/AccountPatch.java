package app.revanced.music.patches.account;

import android.view.View;
import android.widget.TextView;

import app.revanced.music.settings.SettingsEnum;

public class AccountPatch {

    public static boolean hideHandle(boolean original) {
        return SettingsEnum.HIDE_HANDLE.getBoolean() || original;
    }

    public static void hideHandle(TextView textView, int visibility) {
        if (SettingsEnum.HIDE_HANDLE.getBoolean()) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(visibility);
        }
    }
}
