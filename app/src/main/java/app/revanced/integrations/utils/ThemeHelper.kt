package app.revanced.integrations.utils

import app.revanced.integrations.utils.ResourceUtils.identifier

object ThemeHelper {
    private const val PRIMARY_DARK_THEME = "Theme.YouTube.Settings.Dark"
    private const val PRIMARY_WHITE_THEME = "Theme.YouTube.Settings"

    private var isDarkTheme = 0

    @JvmStatic
    fun setTheme(value: Any) {
        isDarkTheme = (value as Enum<*>).ordinal
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
}