package app.revanced.integrations.music.settingsmenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Objects;

import app.revanced.integrations.music.settings.SettingsEnum;
import app.revanced.integrations.music.utils.LogHelper;

/**
 * @noinspection ALL
 */
public class ReVancedSettingActivity {
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;

    public static Activity getActivity() {
        return activity;
    }

    /**
     * Injection point.
     *
     * @param object object is usually Activity, but sometimes object cannot be cast to Activity.
     *               Check whether object can be cast as Activity for a safe hook.
     */
    public static void setActivity(@NonNull Object object) {
        if (object instanceof Activity mActivity)
            activity = mActivity;
    }

    /**
     * Injection point.
     *
     * @param baseActivity Activity containing intent data.
     *                     It should be finished immediately after obtaining the dataString.
     * @return Whether or not dataString is included.
     */
    public static boolean initializeSettings(@NonNull Activity baseActivity) {
        try {
            final String dataString = Objects.requireNonNull(baseActivity.getIntent()).getDataString();

            // If we do not finish the activity immediately, the YT Music logo will remain on the screen.
            baseActivity.finish();

            if (dataString == null || dataString.isEmpty())
                return false;

            // Checks whether dataString contains settings that use Intent.
            if (!SettingsEnum.includeWithIntent(dataString))
                return false;

            // Save intent data in settings activity.
            Intent intent = activity.getIntent();
            intent.setData(Uri.parse(dataString));
            activity.setIntent(intent);

            // Starts a new PreferenceFragment to handle activities freely.
            activity.getFragmentManager()
                    .beginTransaction()
                    .add(new ReVancedSettingsFragment(), "")
                    .commit();

            return true;
        } catch (Exception ex) {
            LogHelper.printException(() -> "initializeSettings failure", ex);
        }
        return false;
    }

}
