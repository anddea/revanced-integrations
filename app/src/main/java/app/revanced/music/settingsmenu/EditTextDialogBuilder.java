package app.revanced.music.settingsmenu;

import static app.revanced.music.utils.ReVancedHelper.getDialogBuilder;
import static app.revanced.music.utils.ReVancedHelper.getLayoutParams;
import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;

public class EditTextDialogBuilder {

    public static void editTextDialogBuilder(@NonNull SettingsEnum setting) {
        editTextDialogBuilder(setting, setting.getString());
    }

    public static void editTextDialogBuilder(@NonNull SettingsEnum setting, String hint) {
        try {
            final Activity activity = ReVancedSettingActivity.getActivity();

            if (activity == null)
                return;

            final EditText textView = new EditText(activity);
            textView.setHint(hint);
            textView.setText(setting.getString());

            TextInputLayout textInputLayout = new TextInputLayout(activity);
            textInputLayout.setLayoutParams(getLayoutParams(activity));
            textInputLayout.addView(textView);

            FrameLayout container = new FrameLayout(activity);
            container.addView(textInputLayout);

            getDialogBuilder(activity)
                    .setTitle(str(setting.path + "_title"))
                    .setView(container)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setNeutralButton(str("revanced_reset"), (dialog, which) -> {
                        setting.resetToDefault();
                        ReVancedSettingsFragment.showRebootDialog();
                    })
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        setting.saveValue(textView.getText().toString().trim());
                        ReVancedSettingsFragment.showRebootDialog();
                    })
                    .show();
        } catch (Exception ex) {
            LogHelper.printException(() -> "editTextDialogBuilder failure", ex);
        }
    }

}
