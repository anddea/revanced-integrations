package app.revanced.integrations.reddit.settingsmenu;

import static app.revanced.integrations.reddit.settings.SettingsUtils.showRestartDialog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import androidx.annotation.Nullable;

import app.revanced.integrations.reddit.settings.SettingsEnum;
import app.revanced.integrations.reddit.settings.SharedPrefCategory;
import app.revanced.integrations.reddit.utils.LogHelper;

/**
 * @noinspection ALL
 */
public class ReVancedSettingsFragment extends PreferenceFragment {
    private final SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, str) -> {
        try {
            SettingsEnum setting = SettingsEnum.settingFromPath(str);
            if (setting == null)
                return;

            if (setting.rebootApp)
                showRestartDialog(getActivity());

        } catch (Exception ex) {
            LogHelper.printException(() -> "OnSharedPreferenceChangeListener failure", ex);
        }
    };
    private PreferenceScreen mPreferenceScreen;
    private PreferenceManager mPreferenceManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferenceManager = getPreferenceManager();
        mPreferenceManager.setSharedPreferencesName(SharedPrefCategory.REDDIT.prefName);
        mPreferenceManager.getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);

        mPreferenceScreen = mPreferenceManager.createPreferenceScreen(getActivity());
        setPreferenceScreen(mPreferenceScreen);

        addPreferences(SettingsStatus.generalAds, SettingsEnum.HIDE_COMMENT_ADS);
        addPreferences(SettingsStatus.generalAds, SettingsEnum.HIDE_OLD_POST_ADS);
        addPreferences(SettingsStatus.generalAds, SettingsEnum.HIDE_NEW_POST_ADS);
        addPreferences(SettingsStatus.screenshotPopup, SettingsEnum.DISABLE_SCREENSHOT_POPUP);
        addPreferences(SettingsStatus.navigationButtons, SettingsEnum.HIDE_CHAT_BUTTON);
        addPreferences(SettingsStatus.navigationButtons, SettingsEnum.HIDE_CREATE_BUTTON);
        addPreferences(SettingsStatus.navigationButtons, SettingsEnum.HIDE_DISCOVER_BUTTON);
        addPreferences(SettingsStatus.recentlyVisitedShelf, SettingsEnum.HIDE_RECENTLY_VISITED_SHELF);
        addPreferences(SettingsStatus.toolBarButton, SettingsEnum.HIDE_TOOLBAR_BUTTON);
        addPreferences(SettingsStatus.openLinksDirectly, SettingsEnum.OPEN_LINKS_DIRECTLY);
        addPreferences(SettingsStatus.openLinksExternally, SettingsEnum.OPEN_LINKS_EXTERNALLY);
        addPreferences(SettingsStatus.sanitizeUrlQuery, SettingsEnum.SANITIZE_URL_QUERY);
    }

    private void addPreferences(boolean isAvailable, SettingsEnum setting) {
        if (!isAvailable)
            return;

        final Activity activity = getActivity();

        final SwitchPreference switchPreference = new SwitchPreference(activity);
        switchPreference.setChecked(setting.getBoolean());
        switchPreference.setTitle(setting.getTitle());
        switchPreference.setSummary(setting.getSummary());
        switchPreference.setOnPreferenceChangeListener((pref, newValue) -> {
            setting.saveValue(newValue);
            return true;
        });
        mPreferenceScreen.addPreference(switchPreference);
    }

    @Override
    public void onDestroy() {
        mPreferenceManager.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
        super.onDestroy();
    }
}
