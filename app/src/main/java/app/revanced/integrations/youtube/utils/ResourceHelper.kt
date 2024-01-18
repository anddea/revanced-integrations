package app.revanced.integrations.youtube.utils

import android.content.res.Resources
import android.view.View
import app.revanced.integrations.youtube.settings.SettingsEnum
import app.revanced.integrations.youtube.utils.ResourceUtils.identifier
import app.revanced.integrations.youtube.utils.ThemeHelper.dayNightTheme

object ResourceHelper {
    private const val ARROW_BLACK_ICON = "yt_outline_arrow_left_black_24"
    private const val ARROW_WHITE_ICON = "yt_outline_arrow_left_white_24"

    @JvmStatic
    val resources: Resources get() = ReVancedUtils.getContext().resources

    @JvmStatic
    val arrow: Int
        get() {
            val themeName = if (dayNightTheme) ARROW_WHITE_ICON else ARROW_BLACK_ICON
            return identifier(themeName, ResourceType.DRAWABLE)
        }

    @JvmStatic
    fun hidePlayerButton(view: View, original: Int): Int {
        PlayerButton.COLLAPSE.apply {
            if (settings.boolean) {
                for (id in filter) {
                    if (view.id == identifier(id, ResourceType.ID))
                        return 8
                }
            }
        }
        return original
    }
}

private enum class PlayerButton(
    val settings: SettingsEnum,
    val filter: List<String>
) {
    COLLAPSE(
        SettingsEnum.HIDE_COLLAPSE_BUTTON,
        listOf("player_collapse_button")
    );
}