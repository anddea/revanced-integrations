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
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

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

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    private static void editTextDialogBuilder(@NonNull SettingsEnum setting, Activity base) {
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

            final AlertDialog.Builder builder = new AlertDialog.Builder(base,android.R.style.Theme_DeviceDefault_Dialog_Alert);

            builder.setTitle(str(setting.path + "_title"))
                    .setView(container)
                    .setNegativeButton(android.R.string.cancel, null)
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
        new AlertDialog.Builder(activity, android.R.style.Theme_DeviceDefault_Dialog_Alert).
                setMessage(str("revanced_reboot_message")).
                setPositiveButton(android.R.string.ok, (dialog, i) -> reboot(activity))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
