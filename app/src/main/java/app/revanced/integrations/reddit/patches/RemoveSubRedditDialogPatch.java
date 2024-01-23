package app.revanced.integrations.reddit.patches;

import static app.revanced.integrations.reddit.utils.StringRef.str;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import app.revanced.integrations.reddit.settings.SettingsEnum;
import app.revanced.integrations.reddit.utils.ReVancedUtils;

@SuppressWarnings("unused")
public class RemoveSubRedditDialogPatch {

    private static void clickButton(View button) {
        if (button != null) {
            ReVancedUtils.runOnMainThreadDelayed(() -> {
                button.setSoundEffectsEnabled(false);
                button.performClick();
            }, 0);
        }
    }

    public static void confirmDialog(@NonNull TextView textView) {
        if (!SettingsEnum.REMOVE_NSFW_DIALOG.getBoolean())
            return;

        if (!textView.getText().toString().equals(str("nsfw_continue_non_anonymously")))
            return;

        clickButton(textView);
    }

    public static void dismissDialog(View cancelButtonView) {
        if (!SettingsEnum.REMOVE_NOTIFICATION_DIALOG.getBoolean())
            return;

        clickButton(cancelButtonView);
    }
}
