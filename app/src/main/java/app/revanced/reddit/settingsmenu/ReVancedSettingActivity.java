package app.revanced.reddit.settingsmenu;

import android.app.Activity;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/**
 * @noinspection ALL
 */
public class ReVancedSettingActivity {
    public static void initializeSettings(Activity activity) {
        SettingsStatus.load();

        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setFitsSystemWindows(true);
        linearLayout.setTransitionGroup(true);
        FrameLayout fragment = new FrameLayout(activity);
        fragment.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        int fragmentId = View.generateViewId();
        fragment.setId(fragmentId);
        linearLayout.addView(fragment);
        activity.setContentView(linearLayout);
        PreferenceFragment preferenceFragment = new ReVancedSettingsFragment();
        activity.getFragmentManager().beginTransaction().replace(fragmentId, preferenceFragment).commit();
    }
}
