package app.revanced.music.settingsmenu;

import static app.revanced.music.settings.SettingsEnum.ReturnType;
import static app.revanced.music.utils.SharedPrefHelper.saveString;
import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;

public class ResettableEditTextPreference {

    private static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static void editTextDialogBuilder(@NonNull SettingsEnum setting, Activity base) {
        try {
            if (setting.returnType != ReturnType.STRING) return;

            TextInputLayout textInputLayout = new TextInputLayout(base);

            final EditText textView = new EditText(base);
            textView.setHint(setting.getString());
            textView.setText(setting.getString());

            FrameLayout container = new FrameLayout(base);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            int left_margin = dpToPx(20, base.getResources());
            int top_margin = dpToPx(10, base.getResources());
            int right_margin = dpToPx(20, base.getResources());
            int bottom_margin = dpToPx(4, base.getResources());
            params.setMargins(left_margin, top_margin, right_margin, bottom_margin);

            textInputLayout.setLayoutParams(params);

            textInputLayout.addView(textView);
            container.addView(textInputLayout);

            final AlertDialog.Builder builder = new AlertDialog.Builder(base, android.R.style.Theme_DeviceDefault_Dialog_Alert);

            builder.setTitle(str(setting.path + "_title"))
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
            LogHelper.printException(ResettableEditTextPreference.class, "editTextDialogBuilder failure", ex);
        }
    }

}
