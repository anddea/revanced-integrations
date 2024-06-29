package app.revanced.integrations.youtube.utils;

import static app.revanced.integrations.shared.utils.ResourceUtils.getColor;
import static app.revanced.integrations.shared.utils.ResourceUtils.getDrawable;
import static app.revanced.integrations.shared.utils.ResourceUtils.getStyleIdentifier;
import static app.revanced.integrations.shared.utils.Utils.getResources;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.shared.utils.ResourceUtils;

@SuppressWarnings({"unused", "SameParameterValue"})
public class ThemeUtils {
    private static int themeValue;

    /**
     * Injection point.
     */
    public static void setTheme(Enum<?> themeEnum) {
        themeValue = themeEnum.ordinal();
    }

    public static boolean isDarkTheme() {
        return themeValue == 1;
    }

    public static int getThemeId() {
        final String themeName = isDarkTheme()
                ? "Theme.YouTube.Settings.Dark"
                : "Theme.YouTube.Settings";

        return getStyleIdentifier(themeName);
    }

    public static Drawable getBackButtonDrawable() {
        final String drawableName = isDarkTheme()
                ? "yt_outline_arrow_left_white_24"
                : "yt_outline_arrow_left_black_24";

        return getDrawable(drawableName);
    }

    public static Drawable getTrashButtonDrawable() {
        final String drawableName = isDarkTheme()
                ? "yt_outline_trash_can_white_24"
                : "yt_outline_trash_can_black_24";

        return getDrawable(drawableName);
    }

    public static int getTextColor() {
        final String colorName = isDarkTheme()
                ? "yt_white1"
                : "yt_black1";

        return getColor(colorName);
    }

    /**
     * Since {@link android.widget.Toolbar} is used instead of {@link android.support.v7.widget.Toolbar},
     * We have to manually specify the toolbar background.
     *
     * @return toolbar background color.
     */
    public static int getToolbarBackgroundColor() {
        final String colorName = isDarkTheme()
                ? "yt_black3"   // Color names used in the light theme
                : "yt_white1";  // Color names used in the dark theme

        return getColor(colorName);
    }

    public static GradientDrawable getSearchViewShape() {
        GradientDrawable shape = new GradientDrawable();

        String currentHex = getThemeHexValue();
        String defaultHex = isDarkTheme() ? "#1A1A1A" : "#E5E5E5";

        String finalHex;
        if (currentThemeColorIsBlackOrWhite()) {
            shape.setColor(Color.parseColor(defaultHex)); // stock black/white color
            finalHex = defaultHex;
        } else {
            // custom color theme
            String adjustedColor = isDarkTheme()
                    ? lightenColor(currentHex, 15)
                    : darkenColor(currentHex, 15);
            shape.setColor(Color.parseColor(adjustedColor));
            finalHex = adjustedColor;
        }
        Logger.printInfo(() -> "searchbar color: " + finalHex);

        shape.setCornerRadius(30 * getResources().getDisplayMetrics().density);

        return shape;
    }

    private static String getThemeHexValue() {
        return String.format("#%06X", (0xFFFFFF & getThemeColor()));
    }

    private static int getThemeColor() {
        final String colorName = isDarkTheme()
                ? "yt_black1"
                : "yt_white1";

        return ResourceUtils.getColor(colorName);
    }

    private static boolean currentThemeColorIsBlackOrWhite() {
        final int color = isDarkTheme()
                ? Color.BLACK
                : Color.WHITE;

        return getThemeColor() == color;
    }

    // Convert HEX to RGB
    private static int[] hexToRgb(String hex) {
        int r = Integer.valueOf(hex.substring(1, 3), 16);
        int g = Integer.valueOf(hex.substring(3, 5), 16);
        int b = Integer.valueOf(hex.substring(5, 7), 16);
        return new int[]{r, g, b};
    }

    // Convert RGB to HEX
    private static String rgbToHex(int r, int g, int b) {
        return String.format("#%02x%02x%02x", r, g, b);
    }

    // Darken color by percentage
    private static String darkenColor(String hex, double percentage) {
        int[] rgb = hexToRgb(hex);
        int r = (int) (rgb[0] * (1 - percentage / 100));
        int g = (int) (rgb[1] * (1 - percentage / 100));
        int b = (int) (rgb[2] * (1 - percentage / 100));
        return rgbToHex(r, g, b);
    }

    // Lighten color by percentage
    private static String lightenColor(String hex, double percentage) {
        int[] rgb = hexToRgb(hex);
        int r = (int) (rgb[0] + (255 - rgb[0]) * (percentage / 100));
        int g = (int) (rgb[1] + (255 - rgb[1]) * (percentage / 100));
        int b = (int) (rgb[2] + (255 - rgb[2]) * (percentage / 100));
        return rgbToHex(r, g, b);
    }
}
