package app.revanced.music.settingsmenu;

import static app.revanced.music.settings.SettingsEnum.CUSTOM_FILTER_STRINGS;
import static app.revanced.music.settings.SettingsEnum.CUSTOM_PLAYBACK_SPEEDS;
import static app.revanced.music.settings.SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME;
import static app.revanced.music.settings.SettingsEnum.SB_API_URL;
import static app.revanced.music.settings.SettingsEnum.values;
import static app.revanced.music.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import app.revanced.music.patches.video.CustomPlaybackSpeedPatch;
import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.sponsorblock.objects.SponsorBlockDialogBuilder;
import app.revanced.music.sponsorblock.objects.SponsorBlockEditTextDialogBuilder;

public class SharedPreferenceChangeListener {
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;

    public static void setActivity(@NonNull Object obj) {
        if (obj instanceof Activity)
            activity = (Activity) obj;
    }

    public static void onPreferenceChanged(@Nullable String key, boolean newValue) {
        for (SettingsEnum setting : values()) {
            if (!setting.path.equals(key) && key != null)
                continue;

            setting.saveValue(newValue);
            if (activity != null && setting.rebootApp)
                rebootDialog();
        }
    }

    public static boolean initializeSettings(@NonNull Activity base) {
        final String dataString = Objects.requireNonNull(base.getIntent()).getDataString();

        if (dataString == null || dataString.isEmpty())
            return false;

        base.finish();

        if (dataString.startsWith("sb_segments_")) {
            SponsorBlockDialogBuilder.dialogBuilder(dataString.replaceAll("sb_segments_", ""), activity);
            return true;
        } else if (dataString.equals(SB_API_URL.path)) {
            SponsorBlockEditTextDialogBuilder.editTextDialogBuilder(activity);
            return true;
        } else if (dataString.equals(CUSTOM_FILTER_STRINGS.path)) {
            EditTextDialogBuilder.editTextDialogBuilder(CUSTOM_FILTER_STRINGS, activity, str("revanced_custom_filter_strings_summary"));
            return true;
        } else if (dataString.equals(CUSTOM_PLAYBACK_SPEEDS.path)) {
            EditTextDialogBuilder.editTextDialogBuilder(CUSTOM_PLAYBACK_SPEEDS, activity, CustomPlaybackSpeedPatch.getWarningMessage());
            return true;
        } else if (dataString.equals(EXTERNAL_DOWNLOADER_PACKAGE_NAME.path)) {
            EditTextDialogBuilder.editTextDialogBuilder(EXTERNAL_DOWNLOADER_PACKAGE_NAME, activity);
            return true;
        } else if (dataString.equals("revanced_extended_settings_import_export")) {
            ImportExportDialogBuilder.editTextDialogBuilder(activity);
            return true;
        }

        return false;
    }

    public static void reboot(Activity activity) {
        Intent restartIntent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());

        activity.finishAffinity();
        activity.startActivity(restartIntent);
        Runtime.getRuntime().exit(0);
    }

    public static void rebootDialog() {
        new AlertDialog.Builder(activity, android.R.style.Theme_DeviceDefault_Dialog_Alert).
                setMessage(str("revanced_reboot_message")).
                setPositiveButton(android.R.string.ok, (dialog, i) -> reboot(activity))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
