package app.revanced.integrations.utils

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import app.revanced.integrations.settings.SettingsEnum
import app.revanced.integrations.utils.ResourceUtils.identifier
import app.revanced.integrations.utils.ThemeHelper.dayNightTheme

object ResourceHelper {
    private val PLAYER_CONTROL_BUTTON_LIST = listOf(
        "player_control_fast_forward_button",
        "player_control_next_button",
        "play_button",
        "player_control_play_pause_replay_button",
        "player_control_previous_button",
        "player_control_rewind_button"
    )

    private const val ARROW_BLACK_ICON = "yt_outline_arrow_left_black_24"
    private const val ARROW_WHITE_ICON = "yt_outline_arrow_left_white_24"

    private const val COLLAPSE_BUTTON = "player_collapse_button"

    @JvmStatic
    val resources: Resources get() = ReVancedUtils.getContext().resources

    @JvmStatic
    val arrow: Int
        get() {
            val themeName = if (dayNightTheme) ARROW_WHITE_ICON else ARROW_BLACK_ICON
            return identifier(themeName, ResourceType.DRAWABLE)
        }

    @JvmStatic
    fun hideCollapseButton(view: View): Boolean {
        return SettingsEnum.HIDE_COLLAPSE_BUTTON.boolean && (view.id == identifier(COLLAPSE_BUTTON, ResourceType.ID))
    }

    @JvmStatic
    @Suppress("DEPRECATION")
    fun hidePlayerButtonBackground(view: View?) {
        if (view == null || !SettingsEnum.HIDE_PLAYER_BUTTON_BACKGROUND.boolean) return
        for (blockList in PLAYER_CONTROL_BUTTON_LIST) {
            if (view.id == identifier(blockList, ResourceType.ID)) view.setBackgroundDrawable(
                ColorDrawable(
                    Color.TRANSPARENT
                )
            )
        }
    }

    @JvmStatic
    fun hidePlayerButton(view: View): Boolean {
        return hideCollapseButton(view)
    }
}