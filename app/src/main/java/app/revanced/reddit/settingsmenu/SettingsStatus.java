package app.revanced.reddit.settingsmenu;

import app.revanced.reddit.settings.SettingsEnum;

public class SettingsStatus {
    public static boolean chatButtons = false;
    public static boolean createButtons = false;
    public static boolean discoverButtons = false;
    public static boolean openLinksDirectly = false;
    public static boolean openLinksExternally = false;
    public static boolean sanitizeUrlQuery = false;
    public static boolean screenshotPopup = false;

    public static void ChatButtons() {
        chatButtons = true;
        SettingsEnum.HIDE_CHAT_BUTTON.saveValue(true);
    }

    public static void CreateButtons() {
        createButtons = true;
        SettingsEnum.HIDE_CREATE_BUTTON.saveValue(true);
    }

    public static void DiscoverButtons() {
        discoverButtons = true;
        SettingsEnum.HIDE_DISCOVER_BUTTON.saveValue(true);
    }

    public static void OpenLinksDirectly() {
        openLinksDirectly = true;
    }

    public static void OpenLinksExternally() {
        openLinksExternally = true;
    }

    public static void SanitizeUrlQuery() {
        sanitizeUrlQuery = true;
    }

    public static void ScreenshotPopup() {
        screenshotPopup = true;
    }

    public static void load() {

    }
}
