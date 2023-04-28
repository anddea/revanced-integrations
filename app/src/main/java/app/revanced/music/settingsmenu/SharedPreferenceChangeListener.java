package app.revanced.music.settingsmenu;

import static app.revanced.music.utils.ReVancedUtils.showToastShort;
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

import app.revanced.music.settings.MusicSettingsEnum;
import app.revanced.music.utils.LogHelper;

public class SharedPreferenceChangeListener {
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;

    public static void setActivity(@NonNull Object obj) {
        if (obj instanceof Activity)
            activity = (Activity) obj;
    }

    public static void onPreferenceChanged(@Nullable String key, boolean newValue) {
        for (MusicSettingsEnum setting : MusicSettingsEnum.values()) {
            if (!setting.path.equals(key) && key != null)
                continue;

            setting.saveValue(newValue);
            if (activity != null && setting.rebootApp)
                rebootDialog();
        }
    }

    public static void initializeSettings(@NonNull Activity base) {

        String dataString = Objects.requireNonNull(base.getIntent()).getDataString();
        for (MusicSettingsEnum setting : MusicSettingsEnum.values()) {
            if (!setting.path.equals(dataString) && setting.returnType != MusicSettingsEnum.ReturnType.STRING && activity == null)
                continue;

            editTextDialogBuilder(setting, base);
        }
    }

    private static void editTextDialogBuilder(@NonNull MusicSettingsEnum setting, Activity base) {
        try {
            if (setting.returnType != MusicSettingsEnum.ReturnType.STRING) return;
            final EditText textView = new EditText(base);
            textView.setHint(setting.getString());
            textView.setText(setting.getString());

            new AlertDialog.Builder(base)
                    .setTitle(str(setting.path + "_title"))
                    .setView(textView)
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> base.finish())
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        saveString(setting.path, textView.getText().toString());
                        base.finish();
                        rebootDialog();
                    })
                    .show();
        } catch (Exception ex) {
            LogHelper.printException(SharedPreferenceChangeListener.class, "editTextDialogBuilder failure", ex);
        }
    };

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
