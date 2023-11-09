package app.revanced.music.settingsmenu;

import static app.revanced.music.utils.ReVancedHelper.getDialogBuilder;
import static app.revanced.music.utils.ReVancedHelper.getLayoutParams;
import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;
import android.text.InputType;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

public class ImportExportDialogBuilder {
    private static String existingSettings;

    public static void editTextDialogBuilder(@NonNull Activity activity) {
        try {
            final EditText textView = new EditText(activity);
            existingSettings = SettingsEnum.exportJSON();
            textView.setText(existingSettings);
            textView.setInputType(textView.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8); // Use a smaller font to reduce text wrap.

            TextInputLayout textInputLayout = new TextInputLayout(activity);
            textInputLayout.setLayoutParams(getLayoutParams(activity));
            textInputLayout.addView(textView);

            FrameLayout container = new FrameLayout(activity);
            container.addView(textInputLayout);

            getDialogBuilder(activity)
                    .setTitle(str("revanced_extended_settings_import_export_title"))
                    .setView(container)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setNeutralButton(str("revanced_extended_settings_import_copy"), (dialog, which) -> {
                        ReVancedUtils.setClipboard(textView.getText().toString(), str("revanced_share_copy_settings_success"));
                    })
                    .setPositiveButton(str("revanced_extended_settings_import"), (dialog, which) -> {
                        importSettings(textView.getText().toString());
                    })
                    .show();
        } catch (Exception ex) {
            LogHelper.printException(ImportExportDialogBuilder.class, "editTextDialogBuilder failure", ex);
        }
    }

    private static void importSettings(String replacementSettings) {
        try {
            if (replacementSettings.equals(existingSettings)) {
                return;
            }
            SettingsEnum.importJSON(replacementSettings);
            SharedPreferenceChangeListener.rebootDialog();
        } catch (Exception ex) {
            LogHelper.printException(ImportExportDialogBuilder.class, "importSettings failure", ex);
        }
    }
}
