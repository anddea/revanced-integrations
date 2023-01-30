package app.revanced.integrations.settingsmenu;

import static app.revanced.integrations.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import app.revanced.integrations.returnyoutubedislike.ReturnYouTubeDislike;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.SharedPrefHelper;

public class ReturnYouTubeDislikeSettingsFragment extends PreferenceFragment {

    /**
     * If ReturnYouTubeDislike is enabled
     */
    private SwitchPreference enabledPreference;

    /**
     * If dislikes are shown as percentage
     */
    private SwitchPreference percentagePreference;

    /**
     * If segmented like/dislike button uses smaller compact layout
     */
    private SwitchPreference compactLayoutPreference;

    private void updateUIState() {
        final boolean rydIsEnabled = SettingsEnum.RYD_ENABLED.getBoolean();

        enabledPreference.setSummary(rydIsEnabled
                ? str("revanced_ryd_enable_summary_on")
                : str("revanced_ryd_enable_summary_off"));

        percentagePreference.setSummary(SettingsEnum.RYD_SHOW_DISLIKE_PERCENTAGE.getBoolean()
                ? str("revanced_ryd_dislike_percentage_summary_on")
                : str("revanced_ryd_dislike_percentage_summary_off"));
        percentagePreference.setEnabled(rydIsEnabled);

        compactLayoutPreference.setSummary(SettingsEnum.RYD_USE_COMPACT_LAYOUT.getBoolean()
                ? str("revanced_ryd_compact_layout_summary_on")
                : str("revanced_ryd_compact_layout_summary_off"));
        compactLayoutPreference.setEnabled(rydIsEnabled);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(SharedPrefHelper.SharedPrefNames.RYD.getName());

        Activity context = this.getActivity();
        PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(context);
        setPreferenceScreen(preferenceScreen);

        enabledPreference = new SwitchPreference(context);
        enabledPreference.setChecked(SettingsEnum.RYD_ENABLED.getBoolean());
        enabledPreference.setTitle(str("revanced_ryd_enable_title"));
        enabledPreference.setOnPreferenceChangeListener((pref, newValue) -> {
            final boolean rydIsEnabled = (Boolean) newValue;
            SettingsEnum.RYD_ENABLED.saveValue(rydIsEnabled);
            ReturnYouTubeDislike.onEnabledChange(rydIsEnabled);

            updateUIState();
            return true;
        });
        preferenceScreen.addPreference(enabledPreference);

        percentagePreference = new SwitchPreference(context);
        percentagePreference.setChecked(SettingsEnum.RYD_SHOW_DISLIKE_PERCENTAGE.getBoolean());
        percentagePreference.setTitle(str("revanced_ryd_dislike_percentage_title"));
        percentagePreference.setOnPreferenceChangeListener((pref, newValue) -> {
            SettingsEnum.RYD_SHOW_DISLIKE_PERCENTAGE.saveValue(newValue);

            updateUIState();
            return true;
        });
        preferenceScreen.addPreference(percentagePreference);

        compactLayoutPreference = new SwitchPreference(context);
        compactLayoutPreference.setChecked(SettingsEnum.RYD_USE_COMPACT_LAYOUT.getBoolean());
        compactLayoutPreference.setTitle(str("revanced_ryd_compact_layout_title"));
        compactLayoutPreference.setOnPreferenceChangeListener((pref, newValue) -> {
            SettingsEnum.RYD_USE_COMPACT_LAYOUT.saveValue(newValue);

            updateUIState();
            return true;
        });
        preferenceScreen.addPreference(compactLayoutPreference);

        updateUIState();


        // About category

        PreferenceCategory aboutCategory = new PreferenceCategory(context);
        aboutCategory.setTitle(str("about"));
        preferenceScreen.addPreference(aboutCategory);

        // ReturnYouTubeDislike Website

        Preference aboutWebsitePreference = new Preference(context);
        aboutWebsitePreference.setTitle(str("revanced_ryd_attribution_title"));
        aboutWebsitePreference.setSummary(str("revanced_ryd_attribution_summary"));
        aboutWebsitePreference.setOnPreferenceClickListener(pref -> {
            var intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://returnyoutubedislike.com"));
            pref.getContext().startActivity(intent);
            return false;
        });
        preferenceScreen.addPreference(aboutWebsitePreference);
    }
}
