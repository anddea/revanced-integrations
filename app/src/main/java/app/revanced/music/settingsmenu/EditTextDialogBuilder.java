package app.revanced.music.settingsmenu;

import static app.revanced.music.settings.SettingsEnum.ReturnType;
import static app.revanced.music.utils.ReVancedHelper.getDialogBuilder;
import static app.revanced.music.utils.ReVancedHelper.getLayoutParams;
import static app.revanced.music.utils.SharedPrefHelper.saveString;
import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;

public class EditTextDialogBuilder {

    public static void editTextDialogBuilder(@NonNull SettingsEnum setting, Activity base) {
        editTextDialogBuilder(setting, base, setting.getString());
    }

    public static void editTextDialogBuilder(@NonNull SettingsEnum setting, @NonNull Activity activity, String hint) {
        try {
            if (setting.returnType != ReturnType.STRING)
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
                        saveString(setting.path, setting.defaultValue.toString());
                        SharedPreferenceChangeListener.rebootDialog();
                    })
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        saveString(setting.path, textView.getText().toString().trim());
                        SharedPreferenceChangeListener.rebootDialog();
                    })
                    .show();
        } catch (Exception ex) {
            LogHelper.printException(() -> "editTextDialogBuilder failure", ex);
        }
    }

}
