package app.revanced.integrations.youtube.utils;

import androidx.annotation.NonNull;

import app.revanced.integrations.shared.settings.BooleanSetting;
import app.revanced.integrations.shared.settings.Setting;
import app.revanced.integrations.shared.utils.PackageUtils;
import app.revanced.integrations.youtube.patches.components.FeedComponentsFilter;
import app.revanced.integrations.youtube.settings.Settings;

public class ExtendedUtils extends PackageUtils {
    /**
     * Versions of YouTube after this version use the You tab instead of the Library tab.
     */
    private static final String CLIENT_VERSIONS_YOU_TAB_USED = "18.36.32";
    /**
     * Spoofing this version restores the old library shelves.
     */
    private static final String RESTORE_OLD_LIBRARY_SHELF = "18.09.39";
    /**
     * This value is used in {@link FeedComponentsFilter}.
     * In old YouTube client, hiding carousel shelves via navigation tab buttons does not work as intended.
     */
    private static boolean youTabUsed = false;

    public static boolean isFullscreenHidden() {
        boolean isFullscreenHidden = isTablet() &&
                !Settings.ENABLE_PHONE_LAYOUT.get();
        final BooleanSetting[] hideFullscreenSettings = {
                Settings.ENABLE_TABLET_LAYOUT,
                Settings.DISABLE_ENGAGEMENT_PANEL,
                Settings.HIDE_QUICK_ACTIONS
        };
        for (BooleanSetting s : hideFullscreenSettings) {
            isFullscreenHidden |= s.get();
        }
        return isFullscreenHidden;
    }

    public static boolean isSpoofingToLessThan(@NonNull String versionName) {
        if (!Settings.SPOOF_APP_VERSION.get())
            return false;

        return isVersionToLessThan(Settings.SPOOF_APP_VERSION_TARGET.get(), versionName);
    }

    public static boolean isYouTabUsed() {
        return youTabUsed;
    }

    public static void setYouTabUsed() {
        if (isVersionToLessThan(getVersionName(), CLIENT_VERSIONS_YOU_TAB_USED)) {
            return;
        }
        if (isSpoofingToLessThan(RESTORE_OLD_LIBRARY_SHELF)) {
            return;
        }
        youTabUsed = true;
    }

    public static void setCommentPreviewSettings() {
        final boolean enabled = Settings.HIDE_PREVIEW_COMMENT.get();
        final boolean newMethod = Settings.HIDE_PREVIEW_COMMENT_TYPE.get();

        Settings.HIDE_PREVIEW_COMMENT_OLD_METHOD.save(enabled && !newMethod);
        Settings.HIDE_PREVIEW_COMMENT_NEW_METHOD.save(enabled && newMethod);
    }

    private static final Setting<?>[] additionalSettings = {
            Settings.HIDE_PLAYER_FLYOUT_MENU_AMBIENT,
            Settings.HIDE_PLAYER_FLYOUT_MENU_HELP,
            Settings.HIDE_PLAYER_FLYOUT_MENU_LOOP,
            Settings.HIDE_PLAYER_FLYOUT_MENU_PIP,
            Settings.HIDE_PLAYER_FLYOUT_MENU_PREMIUM_CONTROLS,
            Settings.HIDE_PLAYER_FLYOUT_MENU_STABLE_VOLUME,
            Settings.HIDE_PLAYER_FLYOUT_MENU_STATS_FOR_NERDS,
            Settings.HIDE_PLAYER_FLYOUT_MENU_WATCH_IN_VR,
            Settings.HIDE_PLAYER_FLYOUT_MENU_YT_MUSIC,
            Settings.SPOOF_APP_VERSION,
            Settings.SPOOF_APP_VERSION_TARGET
    };

    public static boolean anyMatchSetting(Setting<?> setting) {
        for (Setting<?> s : additionalSettings) {
            if (setting == s) return true;
        }
        return false;
    }

    public static void setPlayerFlyoutMenuAdditionalSettings() {
        Settings.HIDE_PLAYER_FLYOUT_MENU_ADDITIONAL_SETTINGS.save(isAdditionalSettingsEnabled());
    }

    private static boolean isAdditionalSettingsEnabled() {
        // In the old player flyout panels, the video quality icon and additional quality icon are the same
        // Therefore, additional Settings should not be blocked in old player flyout panels
        if (isSpoofingToLessThan("18.22.00"))
            return false;

        boolean additionalSettingsEnabled = true;
        final BooleanSetting[] additionalSettings = {
                Settings.HIDE_PLAYER_FLYOUT_MENU_AMBIENT,
                Settings.HIDE_PLAYER_FLYOUT_MENU_HELP,
                Settings.HIDE_PLAYER_FLYOUT_MENU_LOOP,
                Settings.HIDE_PLAYER_FLYOUT_MENU_PIP,
                Settings.HIDE_PLAYER_FLYOUT_MENU_PREMIUM_CONTROLS,
                Settings.HIDE_PLAYER_FLYOUT_MENU_STABLE_VOLUME,
                Settings.HIDE_PLAYER_FLYOUT_MENU_STATS_FOR_NERDS,
                Settings.HIDE_PLAYER_FLYOUT_MENU_WATCH_IN_VR,
                Settings.HIDE_PLAYER_FLYOUT_MENU_YT_MUSIC,
        };
        for (BooleanSetting s : additionalSettings) {
            additionalSettingsEnabled &= s.get();
        }
        return additionalSettingsEnabled;
    }
}