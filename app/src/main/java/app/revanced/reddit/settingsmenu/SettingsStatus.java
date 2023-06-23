package app.revanced.reddit.settingsmenu;

public class SettingsStatus {
    public static boolean sanitizeUrlQuery = false;
    public static boolean screenshotPopup = false;

    public static void SanitizeUrlQuery() {
        sanitizeUrlQuery = true;
    }
    public static void ScreenshotPopup() {
        screenshotPopup = true;
    }

    public static void load() {

    }
}
