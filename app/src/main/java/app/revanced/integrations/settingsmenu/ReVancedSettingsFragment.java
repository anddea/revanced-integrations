package app.revanced.integrations.settingsmenu;

import static app.revanced.integrations.utils.ReVancedUtils.runOnMainThreadDelayed;
import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.ResourceUtils.identifier;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.REVANCED;
import static app.revanced.integrations.utils.SharedPrefHelper.saveString;
import static app.revanced.integrations.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app.revanced.integrations.BuildConfig;
import app.revanced.integrations.patches.button.AutoRepeat;
import app.revanced.integrations.patches.button.Copy;
import app.revanced.integrations.patches.button.CopyWithTimeStamp;
import app.revanced.integrations.patches.button.Download;
import app.revanced.integrations.patches.button.Speed;
import app.revanced.integrations.patches.button.Whitelists;
import app.revanced.integrations.patches.utils.PatchStatus;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedHelper;
import app.revanced.integrations.utils.ReVancedUtils;
import app.revanced.integrations.utils.ResourceType;
import app.revanced.integrations.utils.SharedPrefHelper;
import app.revanced.integrations.whitelist.Whitelist;
import app.revanced.integrations.whitelist.WhitelistType;

public class ReVancedSettingsFragment extends PreferenceFragment {
    private List<PreferenceScreen> screens;

    private boolean Registered = false;

    private PreferenceScreen backupPreferenceScreen;
    private PreferenceScreen downloaderPreferenceScreen;
    private PreferenceScreen miscPreferenceScreen;
    private PreferenceScreen overlayPreferenceScreen;
    private PreferenceScreen whitelistingPreferenceScreen;

    @SuppressLint("SuspiciousIndentation")
    SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, str) -> {
        for (SettingsEnum setting : SettingsEnum.values()) {
            if (!setting.getPath().equals(str)) continue;
            Preference pref = this.findPreferenceOnScreen(str);

            if (pref instanceof SwitchPreference) {
                SwitchPreference switchPref = (SwitchPreference) pref;
                setting.setValue(switchPref.isChecked());

                if (setting.equals(SettingsEnum.OVERLAY_BUTTON_SPEED)) {
                    Speed.refreshVisibility();
                } else if (setting.equals(SettingsEnum.OVERLAY_BUTTON_WHITELIST)) {
                    Whitelists.refreshVisibility();
                } else if (setting.equals(SettingsEnum.OVERLAY_BUTTON_COPY)) {
                    Copy.refreshVisibility();
                } else if (setting.equals(SettingsEnum.OVERLAY_BUTTON_COPY_WITH_TIMESTAMP)) {
                    CopyWithTimeStamp.refreshVisibility();
                } else if (setting.equals(SettingsEnum.OVERLAY_BUTTON_AUTO_REPEAT)) {
                    ReVancedSettingsFragment.this.AutoRepeatLinks();
                } else if (setting.equals(SettingsEnum.ENABLE_ALWAYS_AUTO_REPEAT)) {
                    AutoRepeat.changeSelected(SettingsEnum.ENABLE_ALWAYS_AUTO_REPEAT.getBoolean(), true);
                } else if (setting.equals(SettingsEnum.OVERLAY_BUTTON_DOWNLOADS)) {
                    Download.refreshVisibility();
                }

            } else if (pref instanceof EditTextPreference) {
                EditTextPreference editPref = (EditTextPreference) pref;
                String defaultValue = sharedPreferences.getString(setting.getPath(), setting.getDefaultValue() + "");
                editPref.setSummary(defaultValue);
                Object value = null;
                switch (setting.getReturnType()) {
                    case FLOAT:
                        value = Float.parseFloat(defaultValue);
                        break;
                    case LONG:
                        value = Long.parseLong(defaultValue);
                        break;
                    case STRING:
                        value = editPref.getText();
                        break;
                    case INTEGER:
                        value = Integer.parseInt(defaultValue);
                        break;
                    default:
                        LogHelper.printException(ReVancedSettingsFragment.class, "Setting has no valid return type! " + setting.getReturnType());
                        break;
                }
                setting.setValue(value);

                if (setting.equals(SettingsEnum.DOWNLOADER_PACKAGE_NAME)) {
                    if (SettingsEnum.DOWNLOADER_PACKAGE_NAME.getString() != null) editPref.setSummary(SettingsEnum.DOWNLOADER_PACKAGE_NAME.getString());
                }

            } else if (pref instanceof ListPreference) {
                if (setting.equals(SettingsEnum.DEFAULT_VIDEO_SPEED)) {
                    setVideoSpeed();
                } else if (setting.equals(SettingsEnum.DEFAULT_VIDEO_QUALITY_WIFI)) {
                    setVideoQuality(true);
                } else if (setting.equals(SettingsEnum.DEFAULT_VIDEO_QUALITY_MOBILE)) {
                    setVideoQuality(false);
                } else if (setting.equals(SettingsEnum.DOUBLE_BACK_TIMEOUT)) {
                    setDoubleBackTimeout();
                    rebootDialog();
                }
            }

            if (ReVancedUtils.getContext() == null) return;

            if (setting.shouldRebootOnChange() && (Objects.equals(setting.shouldWarningOnChange(), ""))) {
                rebootDialog();
            } else if (!(Objects.equals(setting.shouldWarningOnChange(), ""))) {
                rebootDialogWarning(setting, setting.shouldWarningOnChange());
            }
        }
    };

    public ReVancedSettingsFragment() {
    }

    @SuppressLint({"ResourceType", "CommitPrefEdits"})
    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getPreferenceManager().setSharedPreferencesName(REVANCED.getName());
        try {
            int identifier = identifier("revanced_prefs", ResourceType.XML);
            addPreferencesFromResource(identifier);
            SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
            sharedPreferences.registerOnSharedPreferenceChangeListener(this.listener);
            this.Registered = true;

            this.backupPreferenceScreen = (PreferenceScreen) getPreferenceScreen().findPreference("backup");
            this.downloaderPreferenceScreen = (PreferenceScreen) getPreferenceScreen().findPreference("downloader");
            this.miscPreferenceScreen = (PreferenceScreen) getPreferenceScreen().findPreference("misc");
            this.overlayPreferenceScreen = (PreferenceScreen) getPreferenceScreen().findPreference("overlay_button");
            this.whitelistingPreferenceScreen = (PreferenceScreen) getPreferenceScreen().findPreference("whitelisting");

            initializeReVancedSettings();
            initializeOverlayButton();

            LayoutOverrideLinks();
            TabletLayoutLinks();
            FullScreenPanelPreferenceLinks();

            setVideoSpeed();
            setVideoQuality(true);
            setVideoQuality(false);
            setDoubleBackTimeout();
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error during onCreate()", th);
        }
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onDestroy() {
        if (this.Registered) {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this.listener);
            this.Registered = false;
        }
        super.onDestroy();
    }

    private Preference findPreferenceOnScreen(CharSequence key) {
        if (key == null) return null;
        Preference pref = null;
        if (this.findPreference(key) != null) {
            pref = this.findPreference(key);
        } else {
            for (PreferenceScreen screen : this.screens) {
                Preference toCheck = screen.findPreference(key);
                if (toCheck == null) continue;
                pref = toCheck;
            }
        }

        return pref;
    }

    private void initializeReVancedSettings() {
        setPatchesInformation();
        setBackupRestorePreference();
    }

    private void initializeOverlayButton() {
        AutoRepeatLinks();
        AddWhitelistSettings();
        setDownloaderPreference();
    }

    /**
     * Set interaction for AutoRepeat Preference
     */
    public void AutoRepeatLinks() {
        try {
            boolean isAutoRepeatEnabled = SettingsEnum.OVERLAY_BUTTON_AUTO_REPEAT.getBoolean();
            SwitchPreference autoRepeatPreference = Objects.requireNonNull((SwitchPreference) findPreferenceOnScreen(SettingsEnum.ENABLE_ALWAYS_AUTO_REPEAT.getPath()));

            autoRepeatPreference.setEnabled(!isAutoRepeatEnabled);
            AutoRepeat.isButtonEnabled = isAutoRepeatEnabled;
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting AutoRepeatLinks" + th);
        }
    }

    /**
     * Enable/Disable Layout Override Preference
     */
    public void LayoutOverrideLinks() {
        try {
            boolean isTablet = ReVancedHelper.isTablet();
            SwitchPreference tabletPreference = Objects.requireNonNull((SwitchPreference) findPreferenceOnScreen(SettingsEnum.ENABLE_TABLET_LAYOUT.getPath()));
            SwitchPreference phonePreference = Objects.requireNonNull((SwitchPreference) findPreferenceOnScreen(SettingsEnum.ENABLE_PHONE_LAYOUT.getPath()));

            tabletPreference.setEnabled(!isTablet);
            phonePreference.setEnabled(isTablet);
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting LayoutOverrideLinks" + th);
        }
    }

    /**
     * Enable/Disable Preferences not working in tablet layout
     */
    public void TabletLayoutLinks() {
        final String[] unavailablePreference = {
                SettingsEnum.ADREMOVER_COMMUNITY_POSTS_HOME.getPath(),
                SettingsEnum.ADREMOVER_COMMUNITY_POSTS_SUBSCRIPTIONS.getPath(),
                SettingsEnum.HIDE_ENDSCREEN_OVERLAY.getPath(),
                SettingsEnum.HIDE_FULLSCREEN_BUTTON_CONTAINER.getPath(),
                SettingsEnum.HIDE_FULLSCREEN_PANELS.getPath(),
                SettingsEnum.SHOW_FULLSCREEN_TITLE.getPath(),
                SettingsEnum.HIDE_MIX_PLAYLISTS.getPath()
        };

        try {
            boolean isTabletDevice = ReVancedHelper.isTablet() &&
                    !SettingsEnum.ENABLE_PHONE_LAYOUT.getBoolean();
            boolean isEnabledTabletLayout = SettingsEnum.ENABLE_TABLET_LAYOUT.getBoolean();

            boolean isTablet = isTabletDevice || isEnabledTabletLayout;

            for (String s : unavailablePreference) {
                SwitchPreference switchPreference = Objects.requireNonNull((SwitchPreference) findPreferenceOnScreen(s));
                switchPreference.setEnabled(!isTablet);
            }
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting TabletLayoutLinks" + th);
        }
    }

    /**
     * Enable/Disable Preference related to Hide Fullscreen Panel
     */
    public void FullScreenPanelPreferenceLinks() {
        final String[] unavailablePreference = {
                SettingsEnum.HIDE_ENDSCREEN_OVERLAY.getPath(),
                SettingsEnum.HIDE_FULLSCREEN_BUTTON_CONTAINER.getPath()
        };

        try {
            boolean isEnabled = SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean();

            for (String s : unavailablePreference) {
                SwitchPreference switchPreference = Objects.requireNonNull((SwitchPreference) findPreferenceOnScreen(s));
                switchPreference.setEnabled(!isEnabled);
            }
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting FullScreenPanelPreferenceLinks" + th);
        }
    }

    /**
     * Set interaction for default video speed ListPreference
     */
    private void setVideoSpeed() {
        try {
            final var CUSTOM_SPEED_ENTRY_ARRAY_KEY = "revanced_custom_video_speed_entry";
            final var CUSTOM_SPEED_ENTRY_VALUE_ARRAY_KEY = "revanced_custom_video_speed_entry_value";
            final var DEFAULT_SPEED_ENTRY_ARRAY_KEY = "revanced_default_video_speed_entry";
            final var DEFAULT_SPEED_ENTRY_VALUE_ARRAY_KEY = "revanced_default_video_speed_entry_value";

            SettingsEnum speedSetting = SettingsEnum.DEFAULT_VIDEO_SPEED;

            boolean isCustomSpeedEnabled = SettingsEnum.ENABLE_CUSTOM_VIDEO_SPEED.getBoolean();
            var entriesKey = isCustomSpeedEnabled ? CUSTOM_SPEED_ENTRY_ARRAY_KEY : DEFAULT_SPEED_ENTRY_ARRAY_KEY;
            var entriesValueKey = isCustomSpeedEnabled ? CUSTOM_SPEED_ENTRY_VALUE_ARRAY_KEY : DEFAULT_SPEED_ENTRY_VALUE_ARRAY_KEY;

            var context = Objects.requireNonNull(ReVancedUtils.getContext());
            var value = SharedPrefHelper.getString(REVANCED, speedSetting.getPath(), "-2.0");
            saveString(REVANCED, speedSetting.getPath(), value);

            String[] speedEntries = context.getResources().getStringArray(identifier(entriesKey, ResourceType.ARRAY));
            String[] speedEntriesValues = context.getResources().getStringArray(identifier(entriesValueKey, ResourceType.ARRAY));

            ListPreference speedListPreference = (ListPreference) findPreferenceOnScreen(speedSetting.getPath());
            speedListPreference.setEntries(speedEntries);
            speedListPreference.setEntryValues(speedEntriesValues);
            int entryIndex = speedListPreference.findIndexOfValue(value);
            speedListPreference.setSummary(entryIndex < 0 ? null : speedEntries[entryIndex]);
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting setVideoSpeed" + th);
        }
    }

    /**
     * Set interaction for default video quality ListPreference
     */
    private void setVideoQuality(boolean isQualityWiFi) {
        try {
            SettingsEnum qualitySetting = isQualityWiFi ? SettingsEnum.DEFAULT_VIDEO_QUALITY_WIFI : SettingsEnum.DEFAULT_VIDEO_QUALITY_MOBILE;

            var value = SharedPrefHelper.getString(REVANCED, qualitySetting.getPath(), "-2");
            qualitySetting.saveValue(Integer.parseInt(value));

            ListPreference qualityListPreference = (ListPreference) findPreferenceOnScreen(qualitySetting.getPath());
            CharSequence[] entries = qualityListPreference.getEntries();
            int entryIndex = qualityListPreference.findIndexOfValue(value);
            qualityListPreference.setSummary(entryIndex < 0 ? null : entries[entryIndex]);
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting setVideoQuality" + th);
        }
    }

    /**
     * Set interaction for double back timeout ListPreference
     */
    private void setDoubleBackTimeout() {
        try {
            CharSequence[] entryValues = {"0", "1", "2", "3"};
            CharSequence[] Entries = new CharSequence[entryValues.length];

            for (int i = 0; i < entryValues.length; i++) {
                Entries[i] = entryValues[i] + "\u2009" + str("seconds");
            }

            SettingsEnum doubleBackSetting = SettingsEnum.DOUBLE_BACK_TIMEOUT;

            var value = SharedPrefHelper.getString(REVANCED, doubleBackSetting.getPath(), "2");
            doubleBackSetting.saveValue(Integer.parseInt(value));

            ListPreference timeoutListPreference = (ListPreference) findPreferenceOnScreen(doubleBackSetting.getPath());
            timeoutListPreference.setEntries(Entries);
            timeoutListPreference.setEntryValues(entryValues);
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting setDoubleBackTimeout" + th);
        }
    }

    /**
     * Add Preference for github support
     * Also setSummary integrations version (BuildConfig.VERSION_NAME)
     */
    private void setPatchesInformation() {
        try {
            Preference reportPreference = new Preference(ReVancedSettingsFragment.this.getActivity());
            reportPreference.setTitle(str("revanced_extended_support_center_title"));
            reportPreference.setSummary(str("revanced_extended_support_center_summary"));
            reportPreference.setOnPreferenceClickListener(pref -> {
                var intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/inotia00/ReVanced_Extended"));
                pref.getContext().startActivity(intent);
                return false;
            });
            this.miscPreferenceScreen.addPreference(reportPreference);

            Preference integration = findPreferenceOnScreen("revanced-integrations");
            integration.setSummary(BuildConfig.VERSION_NAME);
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting setPatchesInformation" + th);
        }
    }

    /**
     * Add Preference to Whitelist settings submenu
     */
    public void AddWhitelistSettings() {
        try {
            Activity activity = ReVancedSettingsFragment.this.getActivity();
            boolean isIncludedSB = PatchStatus.SponsorBlock();
            boolean isIncludedSPEED = PatchStatus.VideoSpeed();
            boolean isIncludedADS = PatchStatus.VideoAds();

            if (isIncludedSB || isIncludedSPEED || isIncludedADS) {
                // Sponsorblock
                if (isIncludedSB) {
                    Whitelist.setEnabled(WhitelistType.SPONSORBLOCK, SettingsEnum.SB_WHITELIST.getBoolean());

                    WhitelistedChannelsPreference WhitelistSB = new WhitelistedChannelsPreference(activity);
                    WhitelistSB.setTitle(str("revanced_whitelisting_sponsorblock"));
                    WhitelistSB.setWhitelistType(WhitelistType.SPONSORBLOCK);
                    this.whitelistingPreferenceScreen.addPreference(WhitelistSB);
                }

                // Video Speed
                if (isIncludedSPEED) {
                    Whitelist.setEnabled(WhitelistType.SPEED, SettingsEnum.SPEED_WHITELIST.getBoolean());

                    WhitelistedChannelsPreference WhitelistSPEED = new WhitelistedChannelsPreference(activity);
                    WhitelistSPEED.setTitle(str("revanced_whitelisting_speed"));
                    WhitelistSPEED.setWhitelistType(WhitelistType.SPEED);
                    this.whitelistingPreferenceScreen.addPreference(WhitelistSPEED);
                }

                // Video Ads
                if (isIncludedADS) {
                    Whitelist.setEnabled(WhitelistType.ADS, SettingsEnum.ADS_WHITELIST.getBoolean());

                    WhitelistedChannelsPreference WhitelistADS = new WhitelistedChannelsPreference(activity);
                    WhitelistADS.setTitle(str("revanced_whitelisting_ads"));
                    WhitelistADS.setWhitelistType(WhitelistType.ADS);
                    this.whitelistingPreferenceScreen.addPreference(WhitelistADS);
                }
            } else {
                SwitchPreference setWhitelist = (SwitchPreference) findPreferenceOnScreen(SettingsEnum.OVERLAY_BUTTON_WHITELIST.getPath());
                this.overlayPreferenceScreen.removePreference(setWhitelist);
                this.overlayPreferenceScreen.removePreference(whitelistingPreferenceScreen);
            }
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting AddWhitelistSettings" + th);
        }
    }

    /**
     * Add Preference to Downloader settings submenu
     */
    private void setDownloaderPreference() {
        try {
            final var DOWNLOADER_LABEL_PREFERENCE_KEY = "revanced_downloader_label";
            final var DOWNLOADER_PACKAGE_NAME_PREFERENCE_KEY = "revanced_downloader_package_name";
            final var DOWNLOADER_WEBSITE_PREFERENCE_KEY = "revanced_downloader_website";

            Activity activity = ReVancedSettingsFragment.this.getActivity();

            String[] labelArray = activity.getResources().getStringArray(identifier(DOWNLOADER_LABEL_PREFERENCE_KEY, ResourceType.ARRAY));
            String[] packageNameArray = activity.getResources().getStringArray(identifier(DOWNLOADER_PACKAGE_NAME_PREFERENCE_KEY, ResourceType.ARRAY));
            String[] websiteArray = activity.getResources().getStringArray(identifier(DOWNLOADER_WEBSITE_PREFERENCE_KEY, ResourceType.ARRAY));

            SettingsEnum downloaderPackageName = SettingsEnum.DOWNLOADER_PACKAGE_NAME;

            for (int index = 0; index < labelArray.length ; index++) {
                int finalIndex = index;
                Preference downloaderPreference = new Preference(activity);
                downloaderPreference.setTitle(labelArray[index]);
                downloaderPreference.setSummary(packageNameArray[index]);
                downloaderPreference.setOnPreferenceClickListener(preference -> {
                    String msg = "\n" +
                            str("accessibility_share_target") +"\n==" + "\n" +
                            packageNameArray[finalIndex] +  "\n\n" +
                            str("revanced_downloader_package_name_title") + "\n==" + "\n" +
                            packageNameArray[finalIndex] + "\n             ";

                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(activity);

                    builder.setTitle(labelArray[finalIndex]);
                    builder.setMessage(msg);
                    builder.setNegativeButton(str("playback_control_close"), null);
                    builder.setPositiveButton(str("save_metadata_menu"),
                            (dialog, id) -> {
                                downloaderPackageName.saveValue(packageNameArray[finalIndex]);
                                rebootDialog();
                                dialog.dismiss();
                            });
                    builder.setNeutralButton(str("common_google_play_services_install_button"), null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(view -> {
                        Uri uri = Uri.parse(websiteArray[finalIndex]);
                        var intent = new Intent(Intent.ACTION_VIEW, uri);
                        activity.startActivity(intent);
                    });
                    return false;
                });
                this.downloaderPreferenceScreen.addPreference(downloaderPreference);
            }
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting setDownloaderPreference" + th);
        }
    }

    /**
     * Add Preference to Import/Export settings submenu
     */
    private void setBackupRestorePreference() {
        try {
            Preference importPreference = new Preference(ReVancedSettingsFragment.this.getActivity());
            importPreference.setTitle(str("revanced_import_settings_title"));
            importPreference.setSummary(str("revanced_import_settings_summary"));
            importPreference.setOnPreferenceClickListener(pref -> {
                importActivity();
                return false;
            });
            this.backupPreferenceScreen.addPreference(importPreference);

            Preference exportPreference = new Preference(ReVancedSettingsFragment.this.getActivity());
            exportPreference.setTitle(str("revanced_export_settings_title"));
            exportPreference.setSummary(str("revanced_export_settings_summary"));
            exportPreference.setOnPreferenceClickListener(pref -> {
                exportActivity();
                return false;
            });
            this.backupPreferenceScreen.addPreference(exportPreference);
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting setBackupRestorePreference" + th);
        }
    }

    private final int READ_REQUEST_CODE = 42;
    private final int WRITE_REQUEST_CODE = 43;

    /**
     * Invoke the SAF(Storage Access Framework) to export settings
     */
    private void exportActivity(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        var appName = ReVancedHelper.getAppName();
        var versionName = ReVancedHelper.getVersionName();
        var formatDate = dateFormat.format(new Date(System.currentTimeMillis()));
        var fileName = String.format("%s_v%s_%s.json", appName, versionName, formatDate);

        var intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE,fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    /**
     * Invoke the SAF(Storage Access Framework) to import settings
     */
    private void importActivity(){
        var intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    /**
     * Activity should be done within the lifecycle of PreferenceFragment
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            exportJson(data.getData());
        } else if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            importJson(data.getData());
        }
    }

    /**
     * TODO: Implemented as a more ideal serialize method
     */
    private void exportJson(Uri uri) {
        Context context = this.getContext();
        SharedPreferences prefs = context.getSharedPreferences(REVANCED.getName(), Context.MODE_PRIVATE);

        try {
            @SuppressLint("Recycle")
            FileWriter fileWriter = new FileWriter(
                    context.getApplicationContext()
                            .getContentResolver()
                            .openFileDescriptor(uri, "w")
                            .getFileDescriptor()
            );
            PrintWriter printWriter = new PrintWriter(fileWriter);
            JSONObject settingsJson = new JSONObject();

            var prefsMap = prefs.getAll();
            for (Map.Entry entry : prefsMap.entrySet()) {
                settingsJson.put(entry.getKey().toString(), entry.getValue());
            }
            printWriter.write(settingsJson.toString());
            printWriter.close();
            fileWriter.close();

            showToastShort(context, str("settings_export_successful"));
        } catch (IOException | JSONException e) {
            showToastShort(context, str("settings_export_failed"));
            throw new RuntimeException(e);
        }
    }

    /**
     * TODO: Implemented as a more ideal serialize method
     */
    private void importJson(Uri uri) {
        Context context = this.getContext();
        SharedPreferences prefs = context.getSharedPreferences(REVANCED.getName(), Context.MODE_PRIVATE);
        String json;

        try {
            @SuppressLint("Recycle")
            FileReader fileReader = new FileReader(
                    context.getApplicationContext()
                            .getContentResolver()
                            .openFileDescriptor(uri, "r")
                            .getFileDescriptor()
            );
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            SharedPreferences.Editor editor = SharedPrefHelper.getPreferences(context, SharedPrefHelper.SharedPrefNames.REVANCED).edit();

            while ((json = bufferedReader.readLine()) != null) {
                JSONObject settingsJson = new JSONObject(json);

                var prefsMap = prefs.getAll();
                for (Map.Entry entry : prefsMap.entrySet()) {
                    String key = entry.getKey().toString();
                    Object value = entry.getValue();

                    if (value instanceof Boolean)
                        editor.putBoolean(key, settingsJson.optBoolean(key, (Boolean) entry.getValue()));
                    else
                        editor.putString(key, settingsJson.optString(key, (String) entry.getValue()));
                }
            }
            editor.apply();
            bufferedReader.close();
            fileReader.close();

            showToastShort(context, str("settings_import_successful"));
            runOnMainThreadDelayed(() -> reboot(ReVancedSettingsFragment.this.getActivity()), 1000L);
        } catch (IOException | JSONException e) {
            showToastShort(context, str("settings_import_failed"));
            throw new RuntimeException(e);
        }
    }

    public static void reboot(Activity activity) {
       Intent restartIntent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());

       activity.finishAffinity();
       activity.startActivity(restartIntent);
       Runtime.getRuntime().exit(0);
    }

    void rebootDialog() {
        Activity activity = ReVancedSettingsFragment.this.getActivity();

        new AlertDialog.Builder(activity)
                .setMessage(str("pref_refresh_config"))
                .setPositiveButton(str("in_app_update_restart_button"), (dialog, id) -> reboot(activity))
                .setNegativeButton(str("sign_in_cancel"), null)
                .show();
    }

    void rebootDialogWarning(SettingsEnum setting, String msg) {
        if (setting.getBoolean()) {
            Activity activity = ReVancedSettingsFragment.this.getActivity();

            new AlertDialog.Builder(activity)
                    .setMessage(str(msg) + "\n\n" + str("revanced_reboot_warning_general"))
                    .setPositiveButton(str("in_app_update_restart_button"), (dialog, id) -> reboot(activity))
                    .setNegativeButton(str("offline_undo_snackbar_button_text"), (dialog, id) -> {
                        SwitchPreference switchPref = (SwitchPreference) findPreferenceOnScreen(setting.getPath());
                        switchPref.setChecked(false);
                        setting.setValue(false);
                        dialog.dismiss();
                    })
                    .setCancelable(false)
                    .show();
        } else {
            rebootDialog();
        }
    }

    public static void rebootDialogStatic(Context context, String msg) {
        Activity activity = (Activity) context;
        new AlertDialog.Builder(activity)
                .setMessage(str(msg))
                .setPositiveButton(str("in_app_update_restart_button"), (dialog, id) -> reboot(activity))
                .setNegativeButton(str("sign_in_cancel"), null)
                .show();
    }
}
