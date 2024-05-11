package app.revanced.integrations.reddit.patches;

import static app.revanced.integrations.shared.utils.StringRef.str;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import app.revanced.integrations.reddit.settings.Settings;
import app.revanced.integrations.shared.utils.Utils;

@SuppressWarnings("unused")
public class RemoveSubRedditDialogPatch {

    private static void clickButton(View button) {
        if (button != null) {
            Utils.runOnMainThreadDelayed(() -> {
                button.setSoundEffectsEnabled(false);
                button.performClick();
            }, 0);
        }
    }

    public static void confirmDialog(@NonNull TextView textView) {
        if (!Settings.REMOVE_NSFW_DIALOG.get())
            return;

        if (!textView.getText().toString().equals(str("nsfw_continue_non_anonymously")))
            return;

        clickButton(textView);
    }

    public static void dismissDialog(View cancelButtonView) {
        if (!Settings.REMOVE_NOTIFICATION_DIALOG.get())
            return;

        clickButton(cancelButtonView);
    }
}
