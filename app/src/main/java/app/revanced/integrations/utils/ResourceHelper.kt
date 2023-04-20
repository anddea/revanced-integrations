package app.revanced.integrations.utils

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import app.revanced.integrations.settings.SettingsEnum
import app.revanced.integrations.utils.ResourceUtils.identifier
import app.revanced.integrations.utils.ThemeHelper.dayNightTheme

object ResourceHelper {
    private const val ARROW_BLACK_ICON = "yt_outline_arrow_left_black_24"
    private const val ARROW_WHITE_ICON = "yt_outline_arrow_left_white_24"

    private const val TRASH_BLACK_ICON = "yt_outline_trash_can_black_24"
    private const val TRASH_WHITE_ICON = "yt_outline_trash_can_white_24"

    private const val COLLAPSE_BUTTON = "player_collapse_button"

    private const val FAST_FORWARD_BUTTON = "player_control_fast_forward_button"

    private const val NEXT_BUTTON = "player_control_next_button"

    private const val PLAY_BUTTON = "play_button"
    private const val PLAY_PAUSE_BUTTON = "player_control_play_pause_replay_button"

    private const val PREVIOUS_BUTTON = "player_control_previous_button"

    private const val REWIND_BUTTON = "player_control_rewind_button"

    @JvmStatic
    val resources: Resources get() = ReVancedUtils.getContext().resources

    @JvmStatic
    val arrow: Int
        get() {
            val themeName = if (dayNightTheme) ARROW_WHITE_ICON else ARROW_BLACK_ICON
            return identifier(themeName, ResourceType.DRAWABLE)
        }

    @JvmStatic
    val trash: Int
        get() {
            val themeName = if (dayNightTheme) TRASH_WHITE_ICON else TRASH_BLACK_ICON
            return identifier(themeName, ResourceType.DRAWABLE)
        }

    @JvmStatic
    fun hideCollapseButton(view: View): Boolean {
        return SettingsEnum.HIDE_COLLAPSE_BUTTON.boolean && (view.id == identifier(COLLAPSE_BUTTON, ResourceType.ID))
    }

    @JvmStatic
    fun hidePlayerButtonBackground(view: View) {
        if (SettingsEnum.HIDE_PLAYER_BUTTON_BACKGROUND.boolean
            && (view.id == identifier(FAST_FORWARD_BUTTON, ResourceType.ID)
                    || view.id == identifier(NEXT_BUTTON, ResourceType.ID)
                    || view.id == identifier(PLAY_BUTTON, ResourceType.ID)
                    || view.id == identifier(PLAY_PAUSE_BUTTON, ResourceType.ID)
                    || view.id == identifier(PREVIOUS_BUTTON, ResourceType.ID)
                    || view.id == identifier(REWIND_BUTTON, ResourceType.ID))) {
            view.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    @JvmStatic
    fun hidePlayerButton(view: View): Boolean {
        return hideCollapseButton(view)
    }
}