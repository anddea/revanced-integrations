package app.revanced.integrations.settingsmenu;

import static app.revanced.integrations.utils.ReVancedHelper.getStringArray;
import static app.revanced.integrations.utils.ReVancedHelper.isPackageEnabled;
import static app.revanced.integrations.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;

import androidx.annotation.NonNull;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.settings.SettingsUtils;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedHelper;

public class ReVancedSettingsPreference extends ReVancedSettingsFragment {
    private static final String EXTERNAL_DOWNLOADER_PREFERENCE_KEY = "revanced_external_downloader";
    private static PreferenceManager mPreferenceManager;

    public static void setPreferenceManager(PreferenceManager mPreferenceManager) {
        ReVancedSettingsPreference.mPreferenceManager = mPreferenceManager;
    }

    public static void enableDisablePreferences() {
        for (SettingsEnum setting : SettingsEnum.values()) {
            final Preference preference = mPreferenceManager.findPreference(setting.path);
            if (preference != null) {
                preference.setEnabled(setting.isAvailable());
            }
        }
    }

    public static void enableDisablePreferences(final boolean isAvailable, final SettingsEnum... unavailableEnum) {
        if (!isAvailable) {
            return;
        }
        for (SettingsEnum setting : unavailableEnum) {
            final Preference preference = mPreferenceManager.findPreference(setting.path);
            if (preference != null) {
                preference.setEnabled(false);
            }
        }
    }

    public static void updateListPreferenceSummary(ListPreference listPreference, SettingsEnum setting) {
        updateListPreferenceSummary(listPreference, setting, true);
    }

    /**
     * Sets summary text to the currently selected list option.
     */
    public static void updateListPreferenceSummary(ListPreference listPreference, SettingsEnum setting, boolean shouldSetSummary) {
        String objectStringValue = setting.getObjectValue().toString();
        int entryIndex = listPreference.findIndexOfValue(objectStringValue);
        if (entryIndex >= 0) {
            listPreference.setValue(objectStringValue);
            objectStringValue = listPreference.getEntries()[entryIndex].toString();
        }

        if (shouldSetSummary) {
            listPreference.setSummary(objectStringValue);
        }
    }

    public static void initializeReVancedSettings(@NonNull Activity activity) {
        AmbientModePreferenceLinks();
        EnableHDRCodecPreferenceLinks();
        FullScreenPanelPreferenceLinks();
        LayoutOverrideLinks();
        NavigationPreferenceLinks();
        QuickActionsPreferenceLinks();
        TabletLayoutLinks();
        setExternalDownloaderPreference(activity);
        setOpenSettingsPreference(activity);
    }

    /**
     * Enable/Disable Preference related to Ambient Mode
     */
    private static void AmbientModePreferenceLinks() {
        enableDisablePreferences(
                SettingsEnum.DISABLE_AMBIENT_MODE.getBoolean(),
                SettingsEnum.BYPASS_AMBIENT_MODE_RESTRICTIONS,
                SettingsEnum.DISABLE_AMBIENT_MODE_IN_FULLSCREEN
        );
    }

    /**
     * Enable/Disable Layout Override Preference
     */
    private static void LayoutOverrideLinks() {
        enableDisablePreferences(
                ReVancedHelper.isTablet,
                SettingsEnum.ENABLE_TABLET_LAYOUT,
                SettingsEnum.FORCE_FULLSCREEN
        );
        enableDisablePreferences(
                !ReVancedHelper.isTablet,
                SettingsEnum.ENABLE_PHONE_LAYOUT
        );
    }

    /**
     * Enable/Disable Preferences not working in tablet layout
     */
    private static void TabletLayoutLinks() {
        final boolean isTabletDevice = ReVancedHelper.isTablet &&
                !SettingsEnum.ENABLE_PHONE_LAYOUT.getBoolean();
        final boolean isEnabledTabletLayout = SettingsEnum.ENABLE_TABLET_LAYOUT.getBoolean();

        final boolean isTablet = isTabletDevice || isEnabledTabletLayout;

        enableDisablePreferences(
                isTablet,
                SettingsEnum.HIDE_CHANNEL_LIST_SUBMENU,
                SettingsEnum.HIDE_COMMUNITY_POSTS_HOME,
                SettingsEnum.HIDE_COMMUNITY_POSTS_SUBSCRIPTIONS,
                SettingsEnum.HIDE_END_SCREEN_OVERLAY,
                SettingsEnum.HIDE_FULLSCREEN_PANELS,
                SettingsEnum.HIDE_LATEST_VIDEOS_BUTTON,
                SettingsEnum.HIDE_MIX_PLAYLISTS,
                SettingsEnum.HIDE_QUICK_ACTIONS,
                SettingsEnum.HIDE_QUICK_ACTIONS_COMMENT_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_DISLIKE_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_LIKE_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_LIVE_CHAT_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_MORE_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_OPEN_MIX_PLAYLIST_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_OPEN_PLAYLIST_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_RELATED_VIDEO,
                SettingsEnum.HIDE_QUICK_ACTIONS_SAVE_TO_PLAYLIST_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_SHARE_BUTTON,
                SettingsEnum.QUICK_ACTIONS_MARGIN_TOP,
                SettingsEnum.SHOW_FULLSCREEN_TITLE
        );
    }

    /**
     * Enable/Disable Preference related to Enable HDR Codec
     */
    private static void EnableHDRCodecPreferenceLinks() {
        enableDisablePreferences(
                SettingsEnum.ENABLE_VIDEO_CODEC.getBoolean() && SettingsEnum.ENABLE_VIDEO_CODEC_TYPE.getBoolean(),
                SettingsEnum.DISABLE_HDR_VIDEO
        );
    }

    /**
     * Enable/Disable Preference related to Fullscreen Panel
     */
    private static void FullScreenPanelPreferenceLinks() {
        enableDisablePreferences(
                SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean(),
                SettingsEnum.HIDE_END_SCREEN_OVERLAY,
                SettingsEnum.HIDE_QUICK_ACTIONS,
                SettingsEnum.HIDE_QUICK_ACTIONS_COMMENT_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_DISLIKE_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_LIKE_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_LIVE_CHAT_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_MORE_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_OPEN_MIX_PLAYLIST_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_OPEN_PLAYLIST_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_RELATED_VIDEO,
                SettingsEnum.HIDE_QUICK_ACTIONS_SAVE_TO_PLAYLIST_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_SHARE_BUTTON
        );

        enableDisablePreferences(
                SettingsEnum.DISABLE_LANDSCAPE_MODE.getBoolean(),
                SettingsEnum.FORCE_FULLSCREEN
        );

        enableDisablePreferences(
                SettingsEnum.FORCE_FULLSCREEN.getBoolean(),
                SettingsEnum.DISABLE_LANDSCAPE_MODE
        );

    }

    /**
     * Enable/Disable Preference related to Hide Quick Actions
     */
    private static void QuickActionsPreferenceLinks() {
        final boolean isEnabled =
                SettingsEnum.HIDE_FULLSCREEN_PANELS.getBoolean() || SettingsEnum.HIDE_QUICK_ACTIONS.getBoolean();

        enableDisablePreferences(
                isEnabled,
                SettingsEnum.HIDE_QUICK_ACTIONS_COMMENT_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_DISLIKE_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_LIKE_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_LIVE_CHAT_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_MORE_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_OPEN_MIX_PLAYLIST_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_OPEN_PLAYLIST_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_RELATED_VIDEO,
                SettingsEnum.HIDE_QUICK_ACTIONS_SAVE_TO_PLAYLIST_BUTTON,
                SettingsEnum.HIDE_QUICK_ACTIONS_SHARE_BUTTON
        );
    }

    /**
     * Enable/Disable Preference related to Navigation settings
     */
    private static void NavigationPreferenceLinks() {
        enableDisablePreferences(
                SettingsEnum.SWITCH_CREATE_NOTIFICATION.getBoolean(),
                SettingsEnum.HIDE_CREATE_BUTTON
        );
        enableDisablePreferences(
                !SettingsEnum.SWITCH_CREATE_NOTIFICATION.getBoolean(),
                SettingsEnum.HIDE_NOTIFICATIONS_BUTTON
        );
    }

    /**
     * Add Preference to External downloader settings submenu
     */
    private static void setExternalDownloaderPreference(@NonNull Activity activity) {
        final String[] labelArray = getStringArray(activity, EXTERNAL_DOWNLOADER_PREFERENCE_KEY + "_label");
        final String[] packageNameArray = getStringArray(activity, EXTERNAL_DOWNLOADER_PREFERENCE_KEY + "_package_name");
        final String[] websiteArray = getStringArray(activity, EXTERNAL_DOWNLOADER_PREFERENCE_KEY + "_website");

        final String[] mEntries = {str("revanced_external_downloader_download"), str("revanced_external_downloader_save"), activity.getString(android.R.string.cancel)};

        try {
            final PreferenceScreen externalDownloaderPreferenceScreen = (PreferenceScreen) mPreferenceManager.findPreference("external_downloader");
            if (externalDownloaderPreferenceScreen == null)
                return;

            for (int index = 0; index < labelArray.length; index++) {
                final String label = labelArray[index];
                final String packageName = packageNameArray[index];
                final Uri uri = Uri.parse(websiteArray[index]);

                final String installedMessage = isPackageEnabled(activity, packageName)
                        ? str("revanced_external_downloader_installed")
                        : str("revanced_external_downloader_not_installed");

                Preference externalDownloaderPreference = new Preference(activity);

                externalDownloaderPreference.setTitle(label);
                externalDownloaderPreference.setSummary(packageName);
                externalDownloaderPreference.setOnPreferenceClickListener(preference -> {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    builder.setTitle(String.format("%s (%s)", label, installedMessage));
                    builder.setItems(mEntries, (mDialog, mIndex) -> {
                        switch (mIndex) {
                            case 0 -> {
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                activity.startActivity(intent);
                            }
                            case 1 -> {
                                SettingsEnum.EXTERNAL_DOWNLOADER_PACKAGE_NAME.saveValue(packageName);
                                SettingsUtils.showRestartDialog(activity);
                            }
                            case 2 -> mDialog.dismiss();
                        }
                    });
                    builder.show();

                    return false;
                });
                externalDownloaderPreferenceScreen.addPreference(externalDownloaderPreference);
            }

            Preference experimentalPreference = new Preference(activity);
            experimentalPreference.setTitle(" ");
            experimentalPreference.setSummary(str("revanced_experimental_flag"));

            SwitchPreference hookDownloadButtonPreference = new SwitchPreference(activity);
            hookDownloadButtonPreference.setTitle(str("revanced_hook_download_button_title"));
            hookDownloadButtonPreference.setSummary(str("revanced_hook_download_button_summary"));
            hookDownloadButtonPreference.setKey("revanced_hook_download_button");
            hookDownloadButtonPreference.setDefaultValue(false);

            externalDownloaderPreferenceScreen.addPreference(experimentalPreference);
            externalDownloaderPreferenceScreen.addPreference(hookDownloadButtonPreference);
        } catch (Throwable th) {
            LogHelper.printException(() -> "Error setting setExternalDownloaderPreference" + th);
        }
    }

    /**
     * Set Open External Link Preference onClickListener
     */
    private static void setOpenSettingsPreference(@NonNull Activity activity) {
        try {
            final Uri uri = Uri.parse("package:" + activity.getPackageName());

            final Intent intent = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                    ? new Intent(Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS, uri)
                    : new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);

            Objects.requireNonNull(mPreferenceManager.findPreference("revanced_default_app_settings"))
                    .setOnPreferenceClickListener(pref -> {
                        activity.startActivity(intent);
                        return false;
                    });
        } catch (Throwable th) {
            LogHelper.printException(() -> "Error setting setOpenSettingsPreference" + th);
        }
    }
}