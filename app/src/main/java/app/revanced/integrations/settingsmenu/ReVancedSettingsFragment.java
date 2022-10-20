package app.revanced.integrations.settingsmenu;

import static app.revanced.integrations.sponsorblock.StringRef.str;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.os.Build;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.google.android.apps.youtube.app.YouTubeTikTokRoot_Application;
import com.google.android.apps.youtube.app.application.Shell_HomeActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.revanced.integrations.sponsorblock.SponsorBlockSettings;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;
import app.revanced.integrations.utils.SharedPrefHelper;
import app.revanced.integrations.videoplayer.AutoRepeat;
import app.revanced.integrations.videoplayer.Copy;
import app.revanced.integrations.videoplayer.CopyWithTimeStamp;
import app.revanced.integrations.videoplayer.Download;

public class ReVancedSettingsFragment extends PreferenceFragment {

    private List<PreferenceScreen> screens;

    private boolean Registered = false;
    private boolean settingsInitialized = false;
    private PreferenceScreen extendedPreferenceScreen;
    private Preference ExperimentalFlag;
    private SwitchPreference Rotation;
    private SwitchPreference OldLayout;
    private SwitchPreference RydNewLayout;

    private final CharSequence[] videoQualityEntries = {"Auto", "144p", "240p", "360p", "480p", "720p", "1080p", "1440p", "2160p"};
    private final CharSequence[] videoQualityentryValues = {"-2", "144", "240", "360", "480", "720", "1080", "1440", "2160"};
    private final CharSequence[] videoSpeedEntries = {"Auto", "0.25x", "0.5x", "0.75x", "Normal", "1.25x", "1.5x", "1.75x", "2x", "2.25x", "2.5x", "3.0x", "5.0x"};
    private final CharSequence[] videoSpeedentryValues = {"-2", "0.25", "0.5", "0.75", "1.0", "1.25", "1.5", "1.75", "2.0", "2.25", "2.5", "3.0", "5.0"};
    private final CharSequence[] videoSpeedEntries2 = {"Auto", "0.25x", "0.5x", "0.75x", "Normal", "1.25x", "1.5x", "1.75x", "2x"};
    private final CharSequence[] videoSpeedentryValues2 = {"-2", "0.25", "0.5", "0.75", "1.0", "1.25", "1.5", "1.75", "2.0"};
    private final String[] DownloaderNameList = {"PowerTube", "NewPipe", "NewPipe_SponsorBlock"};
    private final String[] DownloaderPackageNameList = {"ussr.razar.youtube_dl", "org.schabi.newpipe", "org.polymorphicshade.newpipe"};
    private final String[] DownloaderURLList = {"https://github.com/razar-dev/PowerTube/releases/latest", "https://github.com/TeamNewPipe/NewPipe/releases/latest", "https://github.com/polymorphicshade/NewPipe/releases/latest"};
    //private final CharSequence[] buttonLocationEntries = {"None", "In player", "Under player", "Both"};
    //private final CharSequence[] buttonLocationentryValues = {"NONE", "PLAYER", "BUTTON_BAR", "BOTH"};

    SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, str) -> {
        for (SettingsEnum setting : SettingsEnum.values()) {
            if (!setting.getPath().equals(str)) continue;
            Preference pref = this.findPreferenceOnScreen(str);

            LogHelper.debug(ReVancedSettingsFragment.class, "Setting " + setting.name() + " was changed. Preference " + str + ": " + pref.toString());

            if (pref instanceof SwitchPreference) {
                SwitchPreference switchPref = (SwitchPreference) pref;
                setting.setValue(switchPref.isChecked());

                if (setting == SettingsEnum.PREFERRED_COPY_BUTTON) {
                    Copy.refreshShouldBeShown();
                } else if (setting == SettingsEnum.PREFERRED_COPY_WITH_TIMESTAMP_BUTTON) {
                    CopyWithTimeStamp.refreshShouldBeShown();
                } else if (setting == SettingsEnum.PREFERRED_AUTO_REPEAT_BUTTON) {
                    ReVancedSettingsFragment.this.AutoRepeatLinks();
                } else if (setting == SettingsEnum.PREFERRED_AUTO_REPEAT) {
                    AutoRepeat.changeSelected(SettingsEnum.PREFERRED_AUTO_REPEAT.getBoolean(), true);
                }

            } else if (pref instanceof EditTextPreference) {
                EditTextPreference editPref = (EditTextPreference) pref;
                Object value = null;
                switch (setting.getReturnType()) {
                    case FLOAT:
                        value = Float.parseFloat(editPref.getText());
                        break;
                    case LONG:
                        value = Long.parseLong(editPref.getText());
                        break;
                    case STRING:
                        value = editPref.getText();
                        break;
                    case INTEGER:
                        value = Integer.parseInt(editPref.getText());
                        break;
                    default:
                        LogHelper.printException(ReVancedSettingsFragment.class, "Setting has no valid return type! " + setting.getReturnType());
                        break;
                }
                setting.setValue(value);
                
                if (setting == SettingsEnum.DOWNLOADS_PACKAGE_NAME) {
                    try {
                        editPref.setSummary(SettingsEnum.DOWNLOADS_PACKAGE_NAME.getString().isEmpty() ? str("revanced_downloads_package_name_summary") : SettingsEnum.DOWNLOADS_PACKAGE_NAME.getString());
                    } catch (Throwable th) {
                        LogHelper.printException(ReVancedSettingsFragment.class, "Error setting value of downloader package name" + th);
                    }
                } else {
                    LogHelper.printException(ReVancedSettingsFragment.class, "No valid setting found: " + setting.toString());
                }
            } else if (pref instanceof ListPreference) {
                ListPreference listPref = (ListPreference) pref;
                Context context = ReVancedUtils.getContext();
                if (setting == SettingsEnum.PREFERRED_VIDEO_SPEED) {
                    try {
                        String value = sharedPreferences.getString(setting.getPath(), setting.getDefaultValue() + "");
                        listPref.setDefaultValue(value);
                        if (SettingsEnum.CUSTOM_PLAYBACK_SPEED_ENABLED.getBoolean()) {
							listPref.setSummary(videoSpeedEntries[listPref.findIndexOfValue(String.valueOf(value))]);
                        } else {
							listPref.setSummary(videoSpeedEntries2[listPref.findIndexOfValue(String.valueOf(value))]);
						}
                        SettingsEnum.PREFERRED_VIDEO_SPEED.saveValue(Float.parseFloat(value));
                        SharedPrefHelper.saveString(context, SharedPrefHelper.SharedPrefNames.REVANCED_PREFS, "revanced_pref_video_speed", value + "");
                        //rebootDialog_Warning2(getActivity());
                    } catch (Throwable th) {
                        LogHelper.printException(ReVancedSettingsFragment.class, "Error setting value of speed" + th);
                    }
                } else {
                    LogHelper.printException(ReVancedSettingsFragment.class, "No valid setting found: " + setting.toString());
                }

                if (setting == SettingsEnum.PREFERRED_VIDEO_QUALITY_WIFI) {
                    try {
                        String value = sharedPreferences.getString(setting.getPath(), setting.getDefaultValue() + "");
                        listPref.setDefaultValue(value);
                        listPref.setSummary(videoQualityEntries[listPref.findIndexOfValue(String.valueOf(value))]);
                        SettingsEnum.PREFERRED_VIDEO_QUALITY_WIFI.saveValue(Integer.parseInt(value));
                        SharedPrefHelper.saveString(context, SharedPrefHelper.SharedPrefNames.REVANCED_PREFS, "revanced_pref_video_quality_wifi", value + "");
                    } catch (Throwable th) {
                        LogHelper.printException(ReVancedSettingsFragment.class, "Error setting value of wifi quality" + th);
                    }
                } else {
                    LogHelper.printException(ReVancedSettingsFragment.class, "No valid setting found: " + setting.toString());
                }

                if (setting == SettingsEnum.PREFERRED_VIDEO_QUALITY_MOBILE) {
                    try {
                        String value = sharedPreferences.getString(setting.getPath(), setting.getDefaultValue() + "");
                        listPref.setDefaultValue(value);
                        listPref.setSummary(videoQualityEntries[listPref.findIndexOfValue(String.valueOf(value))]);
                        SettingsEnum.PREFERRED_VIDEO_QUALITY_MOBILE.saveValue(Integer.parseInt(value));
                        SharedPrefHelper.saveString(context, SharedPrefHelper.SharedPrefNames.REVANCED_PREFS, "revanced_pref_video_quality_mobile", value + "");
                    } catch (Throwable th) {
                        LogHelper.printException(ReVancedSettingsFragment.class, "Error setting value of mobile quality" + th);
                    }
                } else {
                    LogHelper.printException(ReVancedSettingsFragment.class, "No valid setting found: " + setting.toString());
                }

                if ("pref_download_button_list".equals(str)) {
                    Download.refreshShouldBeShown();
                }
            } else {
                LogHelper.printException(ReVancedSettingsFragment.class, "Setting cannot be handled! " + pref.toString());
            }

            if (ReVancedUtils.getContext() != null && settingsInitialized && setting.shouldRebootOnChange() && (setting.shouldWarningOnChange() == "")) {
                rebootDialog(getActivity());
            } else if (ReVancedUtils.getContext() != null && settingsInitialized && !(setting.shouldWarningOnChange() == "")) {
                rebootDialog_Warning(getActivity(), setting.getPath(), setting.shouldWarningOnChange());
            }
        }
    };

    @SuppressLint("ResourceType")
    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getPreferenceManager().setSharedPreferencesName(SharedPrefHelper.SharedPrefNames.YOUTUBE.getName());
        try {
            int identifier = getResources().getIdentifier("revanced_prefs", "xml", getPackageName());
            addPreferencesFromResource(identifier);
            SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
            this.settingsInitialized = sharedPreferences.getBoolean("revanced_initialized", false);
            sharedPreferences.registerOnSharedPreferenceChangeListener(this.listener);
            this.Registered = true;
            this.screens = new ArrayList<>();
            this.screens.add((PreferenceScreen) getPreferenceScreen().findPreference("ads"));
            this.screens.add((PreferenceScreen) getPreferenceScreen().findPreference("interactions"));
            this.screens.add((PreferenceScreen) getPreferenceScreen().findPreference("layout"));
            this.screens.add((PreferenceScreen) getPreferenceScreen().findPreference("misc"));
            this.screens.add((PreferenceScreen) getPreferenceScreen().findPreference("video_settings"));
            this.screens.add((PreferenceScreen) getPreferenceScreen().findPreference("overlaybutton"));
            this.screens.add((PreferenceScreen) getPreferenceScreen().findPreference("extended"));
            this.extendedPreferenceScreen = (PreferenceScreen) getPreferenceScreen().findPreference("extended");
            this.ExperimentalFlag = (Preference) this.extendedPreferenceScreen.findPreference("revanced_experimental_flag");
            this.Rotation = (SwitchPreference) this.extendedPreferenceScreen.findPreference("revanced_fullscreen_rotation");
            this.OldLayout = (SwitchPreference) this.extendedPreferenceScreen.findPreference("revanced_disable_new_layout");
            this.RydNewLayout = (SwitchPreference) this.extendedPreferenceScreen.findPreference("revanced_ryd_new_layout");
			AutoRepeatLinks();
            VersionOverrideLinks();
            VersionOverrideLinks2();

            String AUTO = str("quality_auto");
			this.videoSpeedEntries[0] = AUTO;
			this.videoSpeedEntries2[0] = AUTO;
			this.videoQualityEntries[0] = AUTO;
			String NORMAL = str("pref_subtitles_scale_normal");
            this.videoSpeedEntries[4] = NORMAL;
            this.videoSpeedEntries2[4] = NORMAL;

            final ListPreference listPreference3 = (ListPreference) screens.get(4).findPreference("revanced_pref_video_speed");
            setSpeedListPreferenceData(listPreference3);

            listPreference3.setOnPreferenceClickListener(preference -> {
                setSpeedListPreferenceData(listPreference3);
                return false;
            });

            final ListPreference listPreference4 = (ListPreference) screens.get(4).findPreference("revanced_pref_video_quality_wifi");
            setListPreferenceData(listPreference4, true);

            listPreference4.setOnPreferenceClickListener(preference -> {
                setListPreferenceData(listPreference4, true);
                return false;
            });

            final ListPreference listPreference5 = (ListPreference) screens.get(4).findPreference("revanced_pref_video_quality_mobile");
            setListPreferenceData(listPreference5, false);

            listPreference5.setOnPreferenceClickListener(preference -> {
                setListPreferenceData(listPreference5, false);
                return false;
            });

            final EditTextPreference editTextPreference = (EditTextPreference) screens.get(5).findPreference("revanced_downloads_package_name");
            editTextPreference.setSummary(editTextPreference.getText());

            for (int i = 0; i < DownloaderNameList.length ; i++) {
                int tempint = i;
                Preference tmp = (Preference) screens.get(5).findPreference(DownloaderNameList[tempint]);
                tmp.setOnPreferenceClickListener(preference -> {
                    setDownloaderPreferenceDialog(editTextPreference, tempint);
                    return false;
                });
            }

            sharedPreferences.edit().putBoolean("revanced_initialized", true);
            this.settingsInitialized = true;
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
        if (key == null) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Key cannot be null!");
            return null;
        }
        Preference pref = null;
        if (this.findPreference(key) != null) {
            pref = this.findPreference(key);
        } else {
            for (PreferenceScreen screen : this.screens) {
                Preference toCheck = screen.findPreference(key);
                if (toCheck == null) continue;
                pref = toCheck;
                LogHelper.debug(ReVancedSettingsFragment.class, "Found preference " + key + " on screen: " + screen.getTitle());
            }
        }

        return pref;
    }

    public void AutoRepeatLinks() {
        boolean z = SettingsEnum.PREFERRED_AUTO_REPEAT_BUTTON.getBoolean();
        SwitchPreference switchPreference = (SwitchPreference) findPreferenceOnScreen("revanced_pref_auto_repeat");
        if (switchPreference == null) {
            return;
        }
        if (z) {
            switchPreference.setEnabled(false);
            AutoRepeat.isAutoRepeatBtnEnabled = true;
            return;
        }
        switchPreference.setEnabled(true);
        AutoRepeat.isAutoRepeatBtnEnabled = false;
    }

    public void VersionOverrideLinks() {
        try {
            // if the version is newer than v17.32.39 (1531051456) & older than v17.38.32 (1531823552) -> true
            boolean hasrotationissue = (ReVancedSettingsFragment.getVersionCode() > 1531051456) && (ReVancedSettingsFragment.getVersionCode() < 1531823552);
            SwitchPreference switchPreference = (SwitchPreference) findPreferenceOnScreen("revanced_fullscreen_rotation");
            this.extendedPreferenceScreen.removePreference(this.Rotation);
            if (hasrotationissue) {
                this.extendedPreferenceScreen.addPreference(this.Rotation);
                switchPreference.setEnabled(hasrotationissue);
            } else if (!hasrotationissue) {
                SettingsEnum.FULLSCREEN_ROTATION.saveValue(false);
                SettingsEnum.EXPERIMENTAL_FLAG.saveValue(true);
                return;
            } else if (!SettingsEnum.FULLSCREEN_ROTATION.getBoolean()) {
                switchPreference.setEnabled(false);
                SettingsEnum.EXPERIMENTAL_FLAG.saveValue(true);
                return;
            }
            if (SettingsEnum.EXPERIMENTAL_FLAG.getBoolean()) return;
            SettingsEnum.EXPERIMENTAL_FLAG.saveValue(true);
            SettingsEnum.FULLSCREEN_ROTATION.saveValue(hasrotationissue);
            return;
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting VersionOverrideLinks" + th);
        }
    }

    public void VersionOverrideLinks2() {
        try {
            // 17.28.35 | 1530518976
            boolean after29 = getVersionCode() > 1530518976;
            boolean oldlayout = SettingsEnum.DISABLE_NEWLAYOUT.getBoolean();
            SwitchPreference switchPreference = (SwitchPreference) findPreferenceOnScreen("revanced_fullscreen_rotation");
            if (!after29) {
                SettingsEnum.DISABLE_NEWLAYOUT.saveValue(false);
                SettingsEnum.RYD_NEWLAYOUT.saveValue(false);
                this.extendedPreferenceScreen.removePreference(this.OldLayout);
                this.extendedPreferenceScreen.removePreference(this.RydNewLayout);
                this.extendedPreferenceScreen.removePreference(this.ExperimentalFlag);
                this.extendedPreferenceScreen.removePreference(this.Rotation);
                return;
            }
            if (oldlayout) {
                switchPreference.setEnabled(false);
                SettingsEnum.FULLSCREEN_ROTATION.saveValue(true);
                return;
            }
            switchPreference.setEnabled(true);
            return;
        } catch (Throwable th) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Error setting VersionOverrideLinks2" + th);
        }
    }

    private void setSpeedListPreferenceData(ListPreference listPreference) {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        String value = sharedPreferences.getString("revanced_pref_video_speed", "-2");
        if (listPreference.getValue() == null) {
            listPreference.setValue(value);
        }
        if (SettingsEnum.CUSTOM_PLAYBACK_SPEED_ENABLED.getBoolean()) {
			listPreference.setEntries(this.videoSpeedEntries);
			listPreference.setEntryValues(this.videoSpeedentryValues);
            listPreference.setSummary(this.videoSpeedEntries[listPreference.findIndexOfValue(value)]);
        } else {
			listPreference.setEntries(this.videoSpeedEntries2);
			listPreference.setEntryValues(this.videoSpeedentryValues2);
            listPreference.setSummary(this.videoSpeedEntries2[listPreference.findIndexOfValue(value)]);
        }
    }

    private void setListPreferenceData(ListPreference listPreference, boolean z) {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        String value = sharedPreferences.getString(z ? "revanced_pref_video_quality_wifi" : "revanced_pref_video_quality_mobile", "-2");
        if (listPreference.getValue() == null) {
            listPreference.setValue(value);
        }
        listPreference.setEntries(this.videoQualityEntries);
        listPreference.setEntryValues(this.videoQualityentryValues);
        listPreference.setSummary(this.videoQualityEntries[listPreference.findIndexOfValue(value)]);
    }

    private void setDownloaderPreferenceDialog(EditTextPreference editextpreference, int i) {
        Activity activity = getActivity();
        String downloader = DownloaderNameList[i].replaceAll("_", " x ");
        String msg = "\n" + str("accessibility_share_target") + "\n==" + "\n" + downloader +  "\n\n" + str("revanced_downloads_package_name_title") + "\n==" + "\n" + DownloaderPackageNameList[i] + "\n             ";
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);

        builder.setTitle(downloader);
        builder.setMessage(msg);
        builder.setNegativeButton(str("playback_control_close"), null);
        builder.setPositiveButton(str("save_metadata_menu"),
                (dialog, id) -> {
                    SettingsEnum.DOWNLOADS_PACKAGE_NAME.saveValue(DownloaderPackageNameList[i]);
                    editextpreference.setSummary(DownloaderPackageNameList[i]);
                    dialog.dismiss();
                });
        builder.setNeutralButton(str("common_google_play_services_install_button"), null);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Set learn more action (set here so clicking it doesn't dismiss the dialog)
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v -> {
            Uri uri = Uri.parse(DownloaderURLList[i]);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        });
    }

    /*
    private void setCopyLinkListPreferenceData(ListPreference listPreference, String str) {
        listPreference.setEntries(this.buttonLocationEntries);
        listPreference.setEntryValues(this.buttonLocationentryValues);
        String string = this.sharedPreferences.getString(str, "NONE");
        if (listPreference.getValue() == null) {
            listPreference.setValue(string);
        }
        listPreference.setSummary(this.buttonLocationEntries[listPreference.findIndexOfValue(string)]);
    }
    */

    private String getPackageName() {
        Context context = YouTubeTikTokRoot_Application.getAppContext();
        if (context == null) {
            LogHelper.printException(ReVancedSettingsFragment.class, "Context is null, returning com.google.android.youtube!");
            return "com.google.android.youtube";
        }
        String PACKAGE_NAME = context.getPackageName();
        LogHelper.debug(ReVancedSettingsFragment.class, "getPackageName: " + PACKAGE_NAME);

        return PACKAGE_NAME;
    }

    public static void checkMicroG() {
        try {
            PackageManager pm = ReVancedUtils.getContext().getPackageManager();
            pm.getPackageInfo("com.mgoogle.android.gms", PackageManager.GET_ACTIVITIES);
            LogHelper.debug(ReVancedSettingsFragment.class, "MicroG is installed on the device");
        } catch (PackageManager.NameNotFoundException e) {
            LogHelper.printException(ReVancedSettingsFragment.class, "MicroG was not found", e);
            Toast.makeText(ReVancedUtils.getContext(), str("microg_not_installed_warning"), Toast.LENGTH_LONG).show();
            Toast.makeText(ReVancedUtils.getContext(), str("microg_not_installed_notice"), Toast.LENGTH_LONG).show();
        }
    }

    public boolean deleteCache(File dir) {
        try {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (String child : children) {
                    boolean isSuccess = deleteCache(new File(dir, child));
                    if (!isSuccess) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(ReVancedUtils.getContext(), "deleteCache Error!", Toast.LENGTH_LONG).show();
            LogHelper.printException(ReVancedSettingsFragment.class, "deleteCache Error!", e);
        }
        return dir.delete();
    }

    private void reboot(Activity activity, Class homeActivityClass) {
        int intent;
        intent = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        ((AlarmManager) activity.getSystemService(Context.ALARM_SERVICE)).setExact(AlarmManager.ELAPSED_REALTIME, 1500L, PendingIntent.getActivity(activity, 0, new Intent(activity, Shell_HomeActivity.class), intent));
        Process.killProcess(Process.myPid());
    }

    private void reboot_and_clear_cache(Activity activity, Class homeActivityClass) {
        File dir = ReVancedUtils.getContext().getCacheDir();
        deleteCache(dir);
        int intent;
        intent = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        ((AlarmManager) activity.getSystemService(Context.ALARM_SERVICE)).setExact(AlarmManager.ELAPSED_REALTIME, 1500L, PendingIntent.getActivity(activity, 0, new Intent(activity, Shell_HomeActivity.class), intent));
        Process.killProcess(Process.myPid());
    }

    public void restore_value(String path) {
        Context context = ReVancedUtils.getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("youtube", 0);
        boolean old = sharedPreferences.getBoolean(path, false);
        sharedPreferences.edit().putBoolean(path, !old);
        SwitchPreference switchpref = (SwitchPreference) this.extendedPreferenceScreen.findPreference(path);
        switchpref.setChecked(!old);
    }

    public void rebootDialog(final Activity activity) {
        new AlertDialog.Builder(activity).setMessage(getStringByName(activity, "pref_refresh_config")).setPositiveButton(getStringByName(activity, "in_app_update_restart_button"), (dialog, id) -> {reboot(activity, Shell_HomeActivity.class);activity.finish();}).setNegativeButton(getStringByName(activity, "sign_in_cancel"), null).show();
    }

    public void rebootDialog_Warning(final Activity activity, String path, String msg) {
        new AlertDialog.Builder(activity).setMessage(getStringByName(activity, msg)).setPositiveButton(getStringByName(activity, "in_app_update_restart_button"), (dialog, id) -> {reboot_and_clear_cache(activity, Shell_HomeActivity.class);activity.finish();}).setNegativeButton(getStringByName(activity, "offline_undo_snackbar_button_text"), (dialog, id) -> {restore_value(path);dialog.dismiss();activity.finish();}).show();
    }

    private String getStringByName(Context context, String name) {
        try {
            Resources res = context.getResources();
            return res.getString(res.getIdentifier(name, "string", context.getPackageName()));
        } catch (Throwable exception) {
            LogHelper.printException(ReVancedUtils.class, "Resource not found.", exception);
            return "";
        }
    }

    public static int getVersionCode() {
        // 17.32.39 | 1531051456
        int versionCode = 1531051456;
        Context context = YouTubeTikTokRoot_Application.getAppContext();
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
