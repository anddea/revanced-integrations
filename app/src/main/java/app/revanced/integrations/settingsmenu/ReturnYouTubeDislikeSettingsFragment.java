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
import app.revanced.integrations.returnyoutubedislike.ReturnYouTubeDislikeMirror;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.SharedPrefHelper;

public class ReturnYouTubeDislikeSettingsFragment extends PreferenceFragment {

    /**
     * If ReturnYouTubeDislike is enabled
     */
    private SwitchPreference enabledPreference;

    /**
     * If Mirror API is enabled
     */
    private SwitchPreference mirrorPreference;

    /**
     * If dislikes are shown as percentage
     */
    private SwitchPreference percentagePreference;

    /**
     * If left separator is shown
     */
    private SwitchPreference separatorPreference;

    private void updateUIState() {
        final boolean rydIsEnabled = SettingsEnum.RYD_ENABLED.getBoolean();
        final boolean rydMirrorIsEnabled = SettingsEnum.RYD_MIRROR_ENABLED.getBoolean();
        final boolean dislikePercentageEnabled = SettingsEnum.RYD_SHOW_DISLIKE_PERCENTAGE.getBoolean();
        final boolean separatorIsShown = SettingsEnum.RYD_SHOW_DISLIKE_SEPARATOR.getBoolean();

        enabledPreference.setSummary(rydIsEnabled
                ? str("revanced_ryd_enable_summary_on")
                : str("revanced_ryd_enable_summary_off"));

        mirrorPreference.setSummary(rydMirrorIsEnabled
                ? str("revanced_ryd_mirror_enable_summary_on")
                : str("revanced_ryd_mirror_enable_summary_off"));
        mirrorPreference.setEnabled(rydIsEnabled);

        percentagePreference.setSummary(dislikePercentageEnabled
                ? str("revanced_ryd_dislike_percentage_summary_on")
                : str("revanced_ryd_dislike_percentage_summary_off"));
        percentagePreference.setEnabled(rydIsEnabled);

        separatorPreference.setSummary(separatorIsShown
                ? str("revanced_ryd_dislike_separator_summary_on")
                : str("revanced_ryd_dislike_separator_summary_off"));
        separatorPreference.setEnabled(rydIsEnabled);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(SharedPrefHelper.SharedPrefNames.RYD.getName());

        Activity context = this.getActivity();
        PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(context);
        setPreferenceScreen(preferenceScreen);

        enabledPreference = new SwitchPreference(context);
        enabledPreference.setKey(SettingsEnum.RYD_ENABLED.getPath());
        enabledPreference.setDefaultValue(SettingsEnum.RYD_ENABLED.getDefaultValue());
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

        mirrorPreference = new SwitchPreference(context);
        mirrorPreference.setKey(SettingsEnum.RYD_MIRROR_ENABLED.getPath());
        mirrorPreference.setDefaultValue(SettingsEnum.RYD_MIRROR_ENABLED.getDefaultValue());
        mirrorPreference.setChecked(SettingsEnum.RYD_MIRROR_ENABLED.getBoolean());
        mirrorPreference.setTitle(str("revanced_ryd_mirror_enable_title"));
        mirrorPreference.setOnPreferenceChangeListener((pref, newValue) -> {
            final boolean rydMirrorIsEnabled = (Boolean) newValue;
            if (rydMirrorIsEnabled) {
                new AlertDialog.Builder(pref.getContext())
                        .setTitle(str("revanced_ryd_mirror_guidelines_popup_title"))
                        .setMessage(str("revanced_ryd_mirror_guidelines_popup_summary"))
                        .setNegativeButton(str("cancel"),
                                (dialog, id) -> {
                                    mirrorPreference.setChecked(false);
                                    SettingsEnum.RYD_MIRROR_ENABLED.saveValue(false);
                                    updateUIState();
                                    dialog.dismiss();
                                })
                        .setPositiveButton(str("apply"),
                                (dialog, id) -> {
                                    SettingsEnum.RYD_MIRROR_ENABLED.saveValue(true);
                                    updateUIState();
                                    dialog.dismiss();
                                })
                        .setCancelable(false)
                        .show();
            } else {
                SettingsEnum.RYD_MIRROR_ENABLED.saveValue(false);
                updateUIState();
            }
            return true;
        });
        preferenceScreen.addPreference(mirrorPreference);

        percentagePreference = new SwitchPreference(context);
        percentagePreference.setKey(SettingsEnum.RYD_SHOW_DISLIKE_PERCENTAGE.getPath());
        percentagePreference.setDefaultValue(SettingsEnum.RYD_SHOW_DISLIKE_PERCENTAGE.getDefaultValue());
        percentagePreference.setChecked(SettingsEnum.RYD_SHOW_DISLIKE_PERCENTAGE.getBoolean());
        percentagePreference.setTitle(str("revanced_ryd_dislike_percentage_title"));
        percentagePreference.setOnPreferenceChangeListener((pref, newValue) -> {
            SettingsEnum.RYD_SHOW_DISLIKE_PERCENTAGE.saveValue(newValue);

            updateUIState();
            return true;
        });
        preferenceScreen.addPreference(percentagePreference);

        separatorPreference = new SwitchPreference(context);
        separatorPreference.setKey(SettingsEnum.RYD_SHOW_DISLIKE_SEPARATOR.getPath());
        separatorPreference.setDefaultValue(SettingsEnum.RYD_SHOW_DISLIKE_SEPARATOR.getDefaultValue());
        separatorPreference.setChecked(SettingsEnum.RYD_SHOW_DISLIKE_SEPARATOR.getBoolean());
        separatorPreference.setTitle(str("revanced_ryd_dislike_separator_title"));
        separatorPreference.setOnPreferenceChangeListener((pref, newValue) -> {
            final boolean separatorIsShown = (Boolean) newValue;
            SettingsEnum.RYD_SHOW_DISLIKE_SEPARATOR.saveValue(separatorIsShown);
            ReturnYouTubeDislike.onSeparatorChange(separatorIsShown);
            ReturnYouTubeDislikeMirror.onSeparatorChange(separatorIsShown);

            updateUIState();
            return true;
        });
        preferenceScreen.addPreference(separatorPreference);

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

        Preference aboutMirrorPreference = new Preference(context);
        aboutMirrorPreference.setTitle(str("revanced_ryd_mirror_attribution_title"));
        aboutMirrorPreference.setSummary(str("revanced_ryd_mirror_attribution_summary"));
        aboutMirrorPreference.setOnPreferenceClickListener(pref -> {
            var intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://github.com/caneleex/true-ryd-worker"));
            pref.getContext().startActivity(intent);
            return false;
        });
        preferenceScreen.addPreference(aboutMirrorPreference);
    }
}
