package app.revanced.integrations.youtube.settingsmenu;

import static app.revanced.integrations.youtube.settings.SharedPrefCategory.REVANCED;
import static app.revanced.integrations.youtube.settingsmenu.ReVancedSettingsPreference.enableDisablePreferences;
import static app.revanced.integrations.youtube.settingsmenu.ReVancedSettingsPreference.setPreferenceManager;
import static app.revanced.integrations.youtube.settingsmenu.ReVancedSettingsPreference.updateListPreferenceSummary;
import static app.revanced.integrations.youtube.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.youtube.utils.ResourceUtils.identifier;
import static app.revanced.integrations.youtube.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Objects;

import app.revanced.integrations.youtube.patches.overlaybutton.AlwaysRepeat;
import app.revanced.integrations.youtube.patches.overlaybutton.CopyVideoUrl;
import app.revanced.integrations.youtube.patches.overlaybutton.CopyVideoUrlTimestamp;
import app.revanced.integrations.youtube.patches.overlaybutton.ExternalDownload;
import app.revanced.integrations.youtube.patches.overlaybutton.SpeedDialog;
import app.revanced.integrations.youtube.patches.video.CustomPlaybackSpeedPatch;
import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.settings.SettingsUtils;
import app.revanced.integrations.youtube.utils.LogHelper;
import app.revanced.integrations.youtube.utils.ReVancedHelper;
import app.revanced.integrations.youtube.utils.ResourceType;

/**
 * @noinspection ALL
 */
public class ReVancedSettingsFragment extends PreferenceFragment {
    public static boolean settingImportInProgress = false;
    private final int READ_REQUEST_CODE = 42;
    private final int WRITE_REQUEST_CODE = 43;
    @SuppressLint("SuspiciousIndentation")
    private final SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, str) -> {
        try {
            SettingsEnum setting = SettingsEnum.settingFromPath(str);
            if (setting == null) {
                return;
            }
            Preference mPreference = findPreference(str);
            if (mPreference == null) {
                return;
            }

            if (mPreference instanceof SwitchPreference switchPreference) {
                if (settingImportInProgress) {
                    switchPreference.setChecked(setting.getBoolean());
                } else {
                    SettingsEnum.setValue(setting, switchPreference.isChecked());
                }

                switch (setting) {
                    case HIDE_PLAYER_FLYOUT_PANEL_AMBIENT,
                            HIDE_PLAYER_FLYOUT_PANEL_HELP,
                            HIDE_PLAYER_FLYOUT_PANEL_LOOP,
                            HIDE_PLAYER_FLYOUT_PANEL_PREMIUM_CONTROLS,
                            HIDE_PLAYER_FLYOUT_PANEL_STABLE_VOLUME,
                            HIDE_PLAYER_FLYOUT_PANEL_STATS_FOR_NERDS,
                            HIDE_PLAYER_FLYOUT_PANEL_WATCH_IN_VR,
                            HIDE_PLAYER_FLYOUT_PANEL_YT_MUSIC,
                            SPOOF_APP_VERSION,
                            SPOOF_APP_VERSION_TARGET ->
                            ReVancedHelper.setPlayerFlyoutPanelAdditionalSettings();
                    case HIDE_PREVIEW_COMMENT,
                            HIDE_PREVIEW_COMMENT_TYPE ->
                            ReVancedHelper.setCommentPreviewSettings();
                    case OVERLAY_BUTTON_ALWAYS_REPEAT -> AlwaysRepeat.refreshVisibility();
                    case OVERLAY_BUTTON_COPY_VIDEO_URL -> CopyVideoUrl.refreshVisibility();
                    case OVERLAY_BUTTON_COPY_VIDEO_URL_TIMESTAMP ->
                            CopyVideoUrlTimestamp.refreshVisibility();
                    case OVERLAY_BUTTON_EXTERNAL_DOWNLOADER -> ExternalDownload.refreshVisibility();
                    case OVERLAY_BUTTON_SPEED_DIALOG -> SpeedDialog.refreshVisibility();
                }
            } else if (mPreference instanceof EditTextPreference editTextPreference) {
                if (settingImportInProgress) {
                    editTextPreference.setText(setting.getObjectValue().toString());
                } else {
                    SettingsEnum.setValue(setting, editTextPreference.getText());
                }
            } else if (mPreference instanceof ListPreference listPreference) {
                if (settingImportInProgress) {
                    listPreference.setValue(setting.getObjectValue().toString());
                } else {
                    SettingsEnum.setValue(setting, listPreference.getValue());
                }

                switch (setting) {
                    case DEFAULT_PLAYBACK_SPEED -> {
                        listPreference.setEntries(CustomPlaybackSpeedPatch.getListEntries());
                        listPreference.setEntryValues(CustomPlaybackSpeedPatch.getListEntryValues());
                        updateListPreferenceSummary(listPreference, setting);
                    }
                    case DOUBLE_BACK_TIMEOUT ->
                            updateListPreferenceSummary(listPreference, setting, false);
                    default -> updateListPreferenceSummary(listPreference, setting);
                }
            } else {
                LogHelper.printException(() -> "Setting cannot be handled: " + mPreference.getClass() + " " + mPreference);
                return;
            }

            enableDisablePreferences();

            if (settingImportInProgress) {
                return;
            }

            if (setting.rebootApp)
                SettingsUtils.showRestartDialog(getActivity());
        } catch (Exception ex) {
            LogHelper.printException(() -> "OnSharedPreferenceChangeListener failure", ex);
        }
    };
    private SharedPreferences mSharedPreferences;

    public ReVancedSettingsFragment() {
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            final PreferenceManager mPreferenceManager = getPreferenceManager();
            mPreferenceManager.setSharedPreferencesName(REVANCED.prefName);
            mSharedPreferences = mPreferenceManager.getSharedPreferences();
            addPreferencesFromResource(identifier("revanced_prefs", ResourceType.XML));

            setPreferenceManager(mPreferenceManager);
            enableDisablePreferences();

            setBackupRestorePreference();
            ReVancedSettingsPreference.initializeReVancedSettings(getActivity());

            for (SettingsEnum setting : SettingsEnum.values()) {
                Preference preference = findPreference(setting.path);

                if (preference instanceof SwitchPreference switchPreference) {
                    switchPreference.setChecked(setting.getBoolean());
                } else if (preference instanceof EditTextPreference editTextPreference) {
                    editTextPreference.setText(setting.getObjectValue().toString());
                } else if (preference instanceof ListPreference listPreference) {
                    switch (setting) {
                        case DEFAULT_PLAYBACK_SPEED -> {
                            listPreference.setEntries(CustomPlaybackSpeedPatch.getListEntries());
                            listPreference.setEntryValues(CustomPlaybackSpeedPatch.getListEntryValues());
                            updateListPreferenceSummary(listPreference, setting);
                        }
                        case DOUBLE_BACK_TIMEOUT ->
                                updateListPreferenceSummary(listPreference, setting, false);
                        default -> updateListPreferenceSummary(listPreference, setting);
                    }
                }
            }

            mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        } catch (Throwable th) {
            LogHelper.printException(() -> "Error during onCreate()", th);
        }
    }

    @Override
    public void onDestroy() {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
        super.onDestroy();
    }

    /**
     * Add Preference to Import/Export settings submenu
     */
    private void setBackupRestorePreference() {
        findPreference("revanced_extended_settings_import").setOnPreferenceClickListener(pref -> {
            importActivity();
            return false;
        });

        findPreference("revanced_extended_settings_export").setOnPreferenceClickListener(pref -> {
            exportActivity();
            return false;
        });
    }

    /**
     * Invoke the SAF(Storage Access Framework) to export settings
     */
    private void exportActivity() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        var appName = ReVancedHelper.applicationLabel;
        var versionName = ReVancedHelper.appVersionName;
        var formatDate = dateFormat.format(new Date(System.currentTimeMillis()));
        var fileName = String.format("%s_v%s_%s.txt", appName, versionName, formatDate);

        var intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    /**
     * Invoke the SAF(Storage Access Framework) to import settings
     */
    private void importActivity() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(Build.VERSION.SDK_INT <= 28 ? "*/*" : "text/plain");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    /**
     * Activity should be done within the lifecycle of PreferenceFragment
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            exportText(data.getData());
        } else if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            importText(data.getData());
        }
    }

    private void exportText(Uri uri) {
        final Context context = this.getContext();

        try {
            @SuppressLint("Recycle")
            FileWriter jsonFileWriter =
                    new FileWriter(
                            Objects.requireNonNull(context.getApplicationContext()
                                            .getContentResolver()
                                            .openFileDescriptor(uri, "w"))
                                    .getFileDescriptor()
                    );
            PrintWriter printWriter = new PrintWriter(jsonFileWriter);
            printWriter.write(SettingsEnum.exportJSON(context));
            printWriter.close();
            jsonFileWriter.close();

            showToastShort(context, str("revanced_extended_settings_export_success"));
        } catch (IOException e) {
            showToastShort(context, str("revanced_extended_settings_export_failed"));
        }
    }

    private void importText(Uri uri) {
        final Activity activity = this.getActivity();
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            settingImportInProgress = true;

            @SuppressLint("Recycle")
            FileReader fileReader =
                    new FileReader(
                            Objects.requireNonNull(activity.getApplicationContext()
                                            .getContentResolver()
                                            .openFileDescriptor(uri, "r"))
                                    .getFileDescriptor()
                    );
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            bufferedReader.close();
            fileReader.close();

            final boolean rebootNeeded = SettingsEnum.importJSON(sb.toString());
            if (rebootNeeded) {
                SettingsUtils.showRestartDialog(activity);
            }
        } catch (IOException e) {
            showToastShort(activity, str("revanced_extended_settings_import_failed"));
            throw new RuntimeException(e);
        } finally {
            settingImportInProgress = false;
        }
    }
}
