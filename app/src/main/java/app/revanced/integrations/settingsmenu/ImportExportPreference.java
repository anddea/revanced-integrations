package app.revanced.integrations.settingsmenu;

import static app.revanced.integrations.utils.StringRef.str;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.settings.SettingsUtils;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;

public class ImportExportPreference extends EditTextPreference implements Preference.OnPreferenceClickListener {

    private String existingSettings;

    @TargetApi(26)
    private void init() {
        setSelectable(true);

        EditText editText = getEditText();
        editText.setTextIsSelectable(true);
        editText.setAutofillHints((String) null);
        editText.setInputType(editText.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8); // Use a smaller font to reduce text wrap.

        setOnPreferenceClickListener(this);
    }

    public ImportExportPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ImportExportPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ImportExportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImportExportPreference(Context context) {
        super(context);
        init();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        try {
            // Must set text before preparing dialog, otherwise text is non selectable if this preference is later reopened.
            existingSettings = SettingsEnum.exportJSON(getContext());
            getEditText().setText(existingSettings);
        } catch (Exception ex) {
            LogHelper.printException(() -> "showDialog failure", ex);
        }
        return true;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        try {
            // Show the user the settings in JSON format.
            builder.setNeutralButton(str("revanced_extended_settings_import_copy"), (dialog, which) ->
                            ReVancedUtils.setClipboard(getEditText().getText().toString(), str("revanced_share_copy_settings_success")))
                    .setPositiveButton(str("revanced_extended_settings_import"), (dialog, which) ->
                            importSettings(getEditText().getText().toString())
                    );
        } catch (Exception ex) {
            LogHelper.printException(() -> "onPrepareDialogBuilder failure", ex);
        }
    }

    private void importSettings(String replacementSettings) {
        try {
            if (replacementSettings.equals(existingSettings)) {
                return;
            }
            ReVancedSettingsFragment.settingImportInProgress = true;
            final boolean rebootNeeded = SettingsEnum.importJSON(replacementSettings);
            if (rebootNeeded && this.getContext() instanceof Activity activity) {
                SettingsUtils.showRestartDialog(activity);
            }
        } catch (Exception ex) {
            ReVancedUtils.showToastShort(str("revanced_extended_settings_import_failed"));
            LogHelper.printException(() -> "importSettings failure", ex);
        } finally {
            ReVancedSettingsFragment.settingImportInProgress = false;
        }
    }

}