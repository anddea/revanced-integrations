package app.revanced.music.settingsmenu;

import static app.revanced.music.settings.SettingsEnum.CUSTOM_FILTER_STRINGS;
import static app.revanced.music.settings.SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME;
import static app.revanced.music.settings.SettingsEnum.ReturnType;
import static app.revanced.music.settings.SettingsEnum.values;
import static app.revanced.music.utils.SharedPrefHelper.saveString;
import static app.revanced.music.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;

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
        base.finish();

        if (dataString == null || dataString.isEmpty())
            return false;

        if (dataString.equals(EXTERNAL_DOWNLOADER_PACKAGE_NAME.path)) {
            editTextDialogBuilder(EXTERNAL_DOWNLOADER_PACKAGE_NAME, activity);
            return true;
        } else if (dataString.equals(CUSTOM_FILTER_STRINGS.path)) {
            editTextDialogBuilder(CUSTOM_FILTER_STRINGS, activity);
            return true;
        }

        return false;
    }

    private static void editTextDialogBuilder(@NonNull SettingsEnum setting, Activity base) {
        try {
            if (setting.returnType != ReturnType.STRING) return;
            final EditText textView = new EditText(base);
            textView.setHint(setting.getString());
            textView.setText(setting.getString());

            new AlertDialog.Builder(base)
                    .setTitle(str(setting.path + "_title"))
                    .setView(textView)
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> base.finish())
                    .setNeutralButton(str("revanced_reset"), (dialog, which) -> {
                        saveString(setting.path, setting.defaultValue.toString());
                        rebootDialog();
                    })
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        saveString(setting.path, textView.getText().toString());
                        rebootDialog();
                    })
                    .show();
        } catch (Exception ex) {
            LogHelper.printException(SharedPreferenceChangeListener.class, "editTextDialogBuilder failure", ex);
        }
    }

    private static void reboot(Activity activity) {
        Intent restartIntent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());

        activity.finishAffinity();
        activity.startActivity(restartIntent);
        Runtime.getRuntime().exit(0);
    }

    private static void rebootDialog() {
        new AlertDialog.Builder(activity).
                setMessage(str("revanced_reboot_message")).
                setPositiveButton(android.R.string.ok, (dialog, i) -> reboot(activity))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
