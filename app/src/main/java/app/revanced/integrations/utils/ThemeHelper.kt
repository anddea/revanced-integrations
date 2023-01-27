package app.revanced.integrations.utils

import android.content.res.Resources
import android.view.View
import app.revanced.integrations.utils.ResourceUtils.identifier

object ThemeHelper {
    private const val PRIMARY_DARK_THEME = "Theme.YouTube.Settings.Dark"
    private const val PRIMARY_WHITE_THEME = "Theme.YouTube.Settings"
    private const val SECONDARY_DARK_THEME = "Theme.YouTube.Dark.DarkerPalette"
    private const val SECONDARY_WHITE_THEME = "Theme.YouTube.Light.DarkerPalette"
    private const val ARROW_BLACK_ICON = "yt_outline_arrow_left_black_24"
    private const val ARROW_WHITE_ICON = "yt_outline_arrow_left_white_24"
    private const val TRASH_BLACK_ICON = "yt_outline_trash_can_black_24"
    private const val TRASH_WHITE_ICON = "yt_outline_trash_can_white_24"

    private const val LIVE_CHAT_BUTTON = "live_chat_overlay_button"

    private const val NEXT_BUTTON = "player_control_next_button"
    private const val NEXT_BUTTON_AREA = "player_control_next_button_touch_area"

    private const val PREVIOUS_BUTTON = "player_control_previous_button"
    private const val PREVIOUS_BUTTON_AREA = "player_control_previous_button_touch_area"

    private var isDarkTheme = 0

    @JvmStatic
    fun setTheme(value: Any) {
        isDarkTheme = (value as Enum<*>).ordinal
    }

    @JvmStatic
    val resources: Resources get() = ReVancedUtils.getContext().resources

    @JvmStatic
    fun setPrimaryTheme(theme: Int) {
        isDarkTheme = if (matchTheme(theme, PRIMARY_DARK_THEME)) 1 else if (matchTheme(
                theme,
                PRIMARY_WHITE_THEME
            )
        ) 0 else isDarkTheme
    }

    @JvmStatic
    fun setSecondaryTheme(theme: Int) {
        isDarkTheme = if (matchTheme(theme, SECONDARY_DARK_THEME)) 1 else if (matchTheme(
                theme,
                SECONDARY_WHITE_THEME
            )
        ) 0 else isDarkTheme
    }

    @JvmStatic
    val dayNightTheme: Boolean
        get() = isDarkTheme == 1

    @JvmStatic
    val settingTheme: Int
        get() {
            val themeName = if (dayNightTheme) PRIMARY_DARK_THEME else PRIMARY_WHITE_THEME
            return identifier(themeName, ResourceType.STYLE);
        }

    @JvmStatic
    val arrow: Int
        get() {
            val themeName = if (dayNightTheme) ARROW_WHITE_ICON else ARROW_BLACK_ICON
            return identifier(themeName, ResourceType.DRAWABLE);
        }

    @JvmStatic
    val trash: Int
        get() {
            val themeName = if (dayNightTheme) TRASH_WHITE_ICON else TRASH_BLACK_ICON
            return identifier(themeName, ResourceType.DRAWABLE);
        }

    @JvmStatic
    fun isLiveChat(view: View): Boolean {
        return view.id == identifier(LIVE_CHAT_BUTTON, ResourceType.ID)
    }

    @JvmStatic
    fun isNextButton(view: View): Boolean {
        return view.id == identifier(NEXT_BUTTON, ResourceType.ID)
                || view.id == identifier(NEXT_BUTTON_AREA, ResourceType.ID)
    }

    @JvmStatic
    fun isPrevButton(view: View): Boolean {
        return view.id == identifier(PREVIOUS_BUTTON, ResourceType.ID)
                || view.id == identifier(PREVIOUS_BUTTON_AREA, ResourceType.ID)
    }

    @JvmStatic
    fun matchTheme(themeValue: Int, themeName: String?): Boolean {
        return themeValue == themeName?.let { identifier(it, ResourceType.STYLE) };
    }

}