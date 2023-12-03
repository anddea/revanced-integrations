package app.revanced.integrations.settingsmenu;

import static app.revanced.integrations.settings.SharedPrefCategory.RETURN_YOUTUBE_DISLIKE;
import static app.revanced.integrations.utils.StringRef.str;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import app.revanced.integrations.patches.utils.ReturnYouTubeDislikePatch;
import app.revanced.integrations.returnyoutubedislike.ReturnYouTubeDislike;
import app.revanced.integrations.settings.SettingsEnum;

public class ReturnYouTubeDislikeSettingsFragment extends PreferenceFragment {

    /**
     * If dislikes are shown on Shorts.
     */
    private SwitchPreference shortsPreference;

    /**
     * If dislikes are shown as percentage.
     */
    private SwitchPreference percentagePreference;

    /**
     * If segmented like/dislike button uses smaller compact layout.
     */
    private SwitchPreference compactLayoutPreference;

    /**
     * If segmented like/dislike button uses smaller compact layout.
     */
    private SwitchPreference toastOnRYDNotAvailable;

    private void updateUIState() {
        shortsPreference.setEnabled(SettingsEnum.RYD_SHORTS.isAvailable());
        percentagePreference.setEnabled(SettingsEnum.RYD_DISLIKE_PERCENTAGE.isAvailable());
        compactLayoutPreference.setEnabled(SettingsEnum.RYD_COMPACT_LAYOUT.isAvailable());
        toastOnRYDNotAvailable.setEnabled(SettingsEnum.RYD_TOAST_ON_CONNECTION_ERROR.isAvailable());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(RETURN_YOUTUBE_DISLIKE.prefName);

        final Activity activity = this.getActivity();
        final PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(activity);
        setPreferenceScreen(preferenceScreen);

        SwitchPreference enabledPreference = new SwitchPreference(activity);
        enabledPreference.setChecked(SettingsEnum.RYD_ENABLED.getBoolean());
        enabledPreference.setTitle(str("revanced_ryd_enable_title"));
        enabledPreference.setSummaryOn(str("revanced_ryd_enable_summary_on"));
        enabledPreference.setSummaryOff(str("revanced_ryd_enable_summary_off"));
        enabledPreference.setOnPreferenceChangeListener((pref, newValue) -> {
            final boolean rydIsEnabled = (Boolean) newValue;
            SettingsEnum.RYD_ENABLED.saveValue(rydIsEnabled);
            ReturnYouTubeDislikePatch.onRYDStatusChange(rydIsEnabled);

            updateUIState();
            return true;
        });
        preferenceScreen.addPreference(enabledPreference);

        shortsPreference = new SwitchPreference(activity);
        shortsPreference.setChecked(SettingsEnum.RYD_SHORTS.getBoolean());
        shortsPreference.setTitle(str("revanced_ryd_shorts_title"));
        String shortsSummary = str("revanced_ryd_shorts_summary_on",
                ReturnYouTubeDislikePatch.IS_SPOOFING_TO_NON_LITHO_SHORTS_PLAYER
                        ? ""
                        : "\n\n" + str("revanced_ryd_shorts_summary_disclaimer"));
        shortsPreference.setSummaryOn(shortsSummary);
        shortsPreference.setSummaryOff(str("revanced_ryd_shorts_summary_off"));
        shortsPreference.setOnPreferenceChangeListener((pref, newValue) -> {
            SettingsEnum.RYD_SHORTS.saveValue(newValue);
            updateUIState();
            return true;
        });
        preferenceScreen.addPreference(shortsPreference);

        percentagePreference = new SwitchPreference(activity);
        percentagePreference.setChecked(SettingsEnum.RYD_DISLIKE_PERCENTAGE.getBoolean());
        percentagePreference.setTitle(str("revanced_ryd_dislike_percentage_title"));
        percentagePreference.setSummaryOn(str("revanced_ryd_dislike_percentage_summary_on"));
        percentagePreference.setSummaryOff(str("revanced_ryd_dislike_percentage_summary_off"));
        percentagePreference.setOnPreferenceChangeListener((pref, newValue) -> {
            SettingsEnum.RYD_DISLIKE_PERCENTAGE.saveValue(newValue);
            ReturnYouTubeDislike.clearAllUICaches();
            updateUIState();
            return true;
        });
        preferenceScreen.addPreference(percentagePreference);

        compactLayoutPreference = new SwitchPreference(activity);
        compactLayoutPreference.setChecked(SettingsEnum.RYD_COMPACT_LAYOUT.getBoolean());
        compactLayoutPreference.setTitle(str("revanced_ryd_compact_layout_title"));
        compactLayoutPreference.setSummaryOn(str("revanced_ryd_compact_layout_summary_on"));
        compactLayoutPreference.setSummaryOff(str("revanced_ryd_compact_layout_summary_off"));
        compactLayoutPreference.setOnPreferenceChangeListener((pref, newValue) -> {
            SettingsEnum.RYD_COMPACT_LAYOUT.saveValue(newValue);
            ReturnYouTubeDislike.clearAllUICaches();
            updateUIState();
            return true;
        });
        preferenceScreen.addPreference(compactLayoutPreference);

        toastOnRYDNotAvailable = new SwitchPreference(activity);
        toastOnRYDNotAvailable.setChecked(SettingsEnum.RYD_TOAST_ON_CONNECTION_ERROR.getBoolean());
        toastOnRYDNotAvailable.setTitle(str("revanced_ryd_toast_on_connection_error_title"));
        toastOnRYDNotAvailable.setSummaryOn(str("revanced_ryd_toast_on_connection_error_summary_on"));
        toastOnRYDNotAvailable.setSummaryOff(str("revanced_ryd_toast_on_connection_error_summary_off"));
        toastOnRYDNotAvailable.setOnPreferenceChangeListener((pref, newValue) -> {
            SettingsEnum.RYD_TOAST_ON_CONNECTION_ERROR.saveValue(newValue);
            updateUIState();
            return true;
        });
        preferenceScreen.addPreference(toastOnRYDNotAvailable);

        updateUIState();


        // About category

        PreferenceCategory aboutCategory = new PreferenceCategory(activity);
        aboutCategory.setTitle(str("revanced_ryd_about"));
        preferenceScreen.addPreference(aboutCategory);

        // ReturnYouTubeDislike Website

        Preference aboutWebsitePreference = new Preference(activity);
        aboutWebsitePreference.setTitle(str("revanced_ryd_attribution_title"));
        aboutWebsitePreference.setSummary(str("revanced_ryd_attribution_summary"));
        aboutWebsitePreference.setOnPreferenceClickListener(pref -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://returnyoutubedislike.com"));
            pref.getContext().startActivity(intent);
            return false;
        });
        preferenceScreen.addPreference(aboutWebsitePreference);
    }
}
