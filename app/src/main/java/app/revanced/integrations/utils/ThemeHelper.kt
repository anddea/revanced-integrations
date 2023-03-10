package app.revanced.integrations.utils

import android.content.res.Resources
import app.revanced.integrations.utils.ResourceUtils.identifier

object ThemeHelper {
    private const val PRIMARY_DARK_THEME = "Theme.YouTube.Settings.Dark"
    private const val PRIMARY_WHITE_THEME = "Theme.YouTube.Settings"
    private const val SECONDARY_DARK_THEME = "Theme.YouTube.Dark.DarkerPalette"
    private const val SECONDARY_WHITE_THEME = "Theme.YouTube.Light.DarkerPalette"

    private var isDarkTheme = 0

    @JvmStatic
    fun setTheme(value: Any) {
        isDarkTheme = (value as Enum<*>).ordinal
    }

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
            return identifier(themeName, ResourceType.STYLE)
        }

    @JvmStatic
    fun matchTheme(themeValue: Int, themeName: String?): Boolean {
        return themeValue == themeName?.let { identifier(it, ResourceType.STYLE) }
    }

}