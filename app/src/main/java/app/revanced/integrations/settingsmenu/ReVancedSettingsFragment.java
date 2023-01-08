package app.revanced.integrations.settingsmenu;

import static app.revanced.integrations.utils.ResourceUtils.identifier;
import static app.revanced.integrations.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import java.util.List;
import java.util.Objects;

import app.revanced.integrations.BuildConfig;
import app.revanced.integrations.patches.button.AutoRepeat;
import app.revanced.integrations.patches.button.Copy;
import app.revanced.integrations.patches.button.CopyWithTimeStamp;
import app.revanced.integrations.patches.button.Download;
import app.revanced.integrations.patches.button.Whitelists;
import app.revanced.integrations.patches.utils.PatchStatus;
import app.revanced.integrations.patches.video.VideoQualityPatch;
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

    private PreferenceScreen overlayPreferenceScreen;
    private PreferenceScreen extendedPreferenceScreen;
    private PreferenceScreen whitelistingPreferenceScreen;

    private final CharSequence[] videoSpeedEntries = {str("quality_auto"), "0.25x", "0.5x", "0.75x", str("shorts_speed_control_normal_label"), "1.25x", "1.5x", "1.75x", "2x"};
    private final CharSequence[] videoSpeedentryValues = {"-2.0", "0.25", "0.5", "0.75", "1.0", "1.25", "1.5", "1.75", "2.0"};
    public static final String[] DownloaderNameList = {"PowerTube", "NewPipe", "NewPipe_SponsorBlock", "Seal"};
    public static final String[] DownloaderPackageNameList = {"ussr.razar.youtube_dl", "org.schabi.newpipe", "org.polymorphicshade.newpipe", "com.junkfood.seal"};
    public static final String[] DownloaderURLList = {"https://github.com/razar-dev/PowerTube/releases/latest", "https://github.com/TeamNewPipe/NewPipe/releases/latest", "https://github.com/polymorphicshade/NewPipe/releases/latest", "https://github.com/JunkFood02/Seal/releases/latest"};

    @SuppressLint("SuspiciousIndentation")
    SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, str) -> {
        for (SettingsEnum setting : SettingsEnum.values()) {
            if (!setting.getPath().equals(str)) continue;
            Preference pref = this.findPreferenceOnScreen(str);

            if (pref instanceof SwitchPreference) {
                SwitchPreference switchPref = (SwitchPreference) pref;
                setting.setValue(switchPref.isChecked());

                if (setting.equals(SettingsEnum.OVERLAY_BUTTON_WHITELIST)) {
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
                    if (SettingsEnum.DOWNLOADER_PACKAGE_NAME.getString() != null)
                    editPref.setSummary(SettingsEnum.DOWNLOADER_PACKAGE_NAME.getString());
                }

            } else if (pref instanceof ListPreference) {
                ListPreference listPref = (ListPreference) pref;
                String defaultValue = sharedPreferences.getString(setting.getPath(), setting.getDefaultValue() + "");
                CharSequence[] entries = listPref.getEntries();
                int entryIndex = listPref.findIndexOfValue(defaultValue);
                listPref.setSummary(entryIndex < 0 ? null : entries[entryIndex]);
                Object value = null;
                switch (setting.getReturnType()) {
                    case FLOAT:
                        value = Float.parseFloat(defaultValue);
                        break;
                    case INTEGER:
                        value = Integer.parseInt(defaultValue);
                        break;
                    default:
                        LogHelper.printException(ReVancedSettingsFragment.class, "Setting has no valid return type! " + setting.getReturnType());
                        break;
                }
                setting.setValue(value);

                if (setting.equals(SettingsEnum.DEFAULT_VIDEO_SPEED)) {
                    setVideoSpeed();
                } else if (setting.equals(SettingsEnum.DEFAULT_VIDEO_QUALITY_WIFI)) {
                    setVideoQuality(true);
                } else if (setting.equals(SettingsEnum.DEFAULT_VIDEO_QUALITY_MOBILE)) {
                    setVideoQuality(false);
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
        getPreferenceManager().setSharedPreferencesName(SharedPrefHelper.SharedPrefNames.REVANCED.getName());
        try {
            int identifier = identifier("revanced_prefs", ResourceType.XML);
            addPreferencesFromResource(identifier);
            SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
            sharedPreferences.registerOnSharedPreferenceChangeListener(this.listener);
            this.Registered = true;

            this.overlayPreferenceScreen = (PreferenceScreen) getPreferenceScreen().findPreference("overlaybutton");
            this.extendedPreferenceScreen = (PreferenceScreen) getPreferenceScreen().findPreference("extended");
            this.whitelistingPreferenceScreen = (PreferenceScreen) getPreferenceScreen().findPreference("whitelisting");

            AutoRepeatLinks();
            AddWhitelistSettings();
            LayoutOverrideLinks();

            setVideoSpeed();
            setVideoQuality(true);
            setVideoQuality(false);

            setPatchesInfomation();

            for (int i = 0; i < DownloaderNameList.length ; i++) {
                int index = i;
                Preference downloader = findPreferenceOnScreen(DownloaderNameList[index]);
                downloader.setOnPreferenceClickListener(preference -> {
                    setDownloaderPreferenceDialog(index);
                    return false;
                });
            }
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

    public void AutoRepeatLinks() {
        try {
            boolean isAutoRepeatEnabled = SettingsEnum.OVERLAY_BUTTON_AUTO_REPEAT.getBoolean();
            SwitchPreference alwaysAutoRepeat = (SwitchPreference) findPreferenceOnScreen(SettingsEnum.ENABLE_ALWAYS_AUTO_REPEAT.getPath());

            alwaysAutoRepeat.setEnabled(!isAutoRepeatEnabled);
            AutoRepeat.isButtonEnabled = isAutoRepeatEnabled;
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting AutoRepeatLinks" + th);
        }
    }

    public void AddWhitelistSettings() {
        try {
            Context context = getContext();
            boolean isIncludedSB = PatchStatus.Sponsorblock();
            boolean isIncludedSPEED = PatchStatus.VideoSpeed();
            boolean isIncludedADS = PatchStatus.VideoAds();

            if (isIncludedSB || isIncludedSPEED || isIncludedADS) {
                // Sponsorblock
                if (isIncludedSB) {
                    Whitelist.setEnabled(WhitelistType.SPONSORBLOCK, SettingsEnum.SB_WHITELIST.getBoolean());

                    WhitelistedChannelsPreference WhitelistSB = new WhitelistedChannelsPreference(context);
                    WhitelistSB.setTitle(str("revanced_whitelisting_sponsorblock"));
                    WhitelistSB.setWhitelistType(WhitelistType.SPONSORBLOCK);
                    this.whitelistingPreferenceScreen.addPreference(WhitelistSB);
                }

                // Video Speed
                if (isIncludedSPEED) {
                    Whitelist.setEnabled(WhitelistType.SPEED, SettingsEnum.SPEED_WHITELIST.getBoolean());

                    WhitelistedChannelsPreference WhitelistSPEED = new WhitelistedChannelsPreference(context);
                    WhitelistSPEED.setTitle(str("revanced_whitelisting_speed"));
                    WhitelistSPEED.setWhitelistType(WhitelistType.SPEED);
                    this.whitelistingPreferenceScreen.addPreference(WhitelistSPEED);
                }

                // Video Ads
                if (isIncludedADS) {
                    Whitelist.setEnabled(WhitelistType.ADS, SettingsEnum.ADS_WHITELIST.getBoolean());

                    WhitelistedChannelsPreference WhitelistADS = new WhitelistedChannelsPreference(context);
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

    public void LayoutOverrideLinks() {
        try {
            SwitchPreference tabletLayoutSwitch = (SwitchPreference) findPreferenceOnScreen(SettingsEnum.ENABLE_TABLET_LAYOUT.getPath());
            SwitchPreference phoneLayoutSwitch = (SwitchPreference) findPreferenceOnScreen(SettingsEnum.ENABLE_PHONE_LAYOUT.getPath());

            if (ReVancedHelper.isTablet()) {
                tabletLayoutSwitch.setEnabled(false);
                return;
            }
            phoneLayoutSwitch.setEnabled(false);
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting LayoutOverrideLinks" + th);
        }
    }
    private void setVideoSpeed() {
        SettingsEnum speedSetting = SettingsEnum.DEFAULT_VIDEO_SPEED;
        SettingsEnum customSpeedSetting = SettingsEnum.ENABLE_CUSTOM_VIDEO_SPEED;

        ListPreference speedListPreference = (ListPreference) findPreferenceOnScreen(speedSetting.getPath());
        var value = Float.toString(speedSetting.getFloat());

        if (customSpeedSetting.getBoolean()) {
            CharSequence[] entries = speedListPreference.getEntries();
            int entryIndex = speedListPreference.findIndexOfValue(value);
            speedListPreference.setSummary(entryIndex < 0 ? null : entries[entryIndex]);
        } else {
            speedListPreference.setEntries(this.videoSpeedEntries);
            speedListPreference.setEntryValues(this.videoSpeedentryValues);
            speedListPreference.setSummary(this.videoSpeedEntries[speedListPreference.findIndexOfValue(value)]);
        }
    }

    private void setVideoQuality(boolean isQualityWiFi) {
        VideoQualityPatch.refreshQuality();
        SettingsEnum qualitySetting = isQualityWiFi ? SettingsEnum.DEFAULT_VIDEO_QUALITY_WIFI : SettingsEnum.DEFAULT_VIDEO_QUALITY_MOBILE;

        ListPreference qualityListPreference = (ListPreference) findPreferenceOnScreen(qualitySetting.getPath());
        var value = Integer.toString(qualitySetting.getInt());
        CharSequence[] entries = qualityListPreference.getEntries();
        int entryIndex = qualityListPreference.findIndexOfValue(value);
        qualityListPreference.setSummary(entryIndex < 0 ? null : entries[entryIndex]);
    }

    private void setPatchesInfomation() {
        Preference reportPreference = new Preference(ReVancedSettingsFragment.this.getActivity());
        reportPreference.setTitle(str("revanced_extended_issue_center_title"));
        reportPreference.setSummary(str("revanced_extended_issue_center_summary"));
        reportPreference.setOnPreferenceClickListener(pref -> {
            var intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://github.com/inotia00/ReVanced_Extended"));
            pref.getContext().startActivity(intent);
            return false;
        });
        this.extendedPreferenceScreen.addPreference(reportPreference);

        Preference integration = findPreferenceOnScreen("revanced-integrations");
        integration.setSummary(BuildConfig.VERSION_NAME);
    }

    private void setDownloaderPreferenceDialog(int index) {
        SettingsEnum downloaderPackageName = SettingsEnum.DOWNLOADER_PACKAGE_NAME;

        EditTextPreference downloaderPreference = (EditTextPreference) findPreferenceOnScreen(downloaderPackageName.getPath());

        Activity activity = ReVancedSettingsFragment.this.getActivity();
        String downloader = DownloaderNameList[index].replaceAll("_", " x ");
        String msg = "\n" + str("accessibility_share_target") + "\n==" + "\n" + downloader +  "\n\n" + str("revanced_downloader_package_name_title") + "\n==" + "\n" + DownloaderPackageNameList[index] + "\n             ";
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);

        builder.setTitle(downloader);
        builder.setMessage(msg);
        builder.setNegativeButton(str("playback_control_close"), null);
        builder.setPositiveButton(str("save_metadata_menu"),
                (dialog, id) -> {
                    downloaderPackageName.saveValue(DownloaderPackageNameList[index]);
                    downloaderPreference.setSummary(DownloaderPackageNameList[index]);
                    dialog.dismiss();
                });
        builder.setNeutralButton(str("common_google_play_services_install_button"), null);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(view -> {
            Uri uri = Uri.parse(DownloaderURLList[index]);
            var intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        });
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
