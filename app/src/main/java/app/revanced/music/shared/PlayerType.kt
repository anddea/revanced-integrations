package app.revanced.music.shared

import app.revanced.music.utils.Event
import app.revanced.music.utils.LogHelper

/**
 * WatchWhile player type
 */
enum class PlayerType {
    DISMISSED,
    MINIMIZED,
    MAXIMIZED_NOW_PLAYING,
    MAXIMIZED_PLAYER_ADDITIONAL_VIEW,
    FULLSCREEN,
    SLIDING_VERTICALLY,
    QUEUE_EXPANDING,
    SLIDING_HORIZONTALLY;

    companion object {

        private val nameToPlayerType = values().associateBy { it.name }

        @JvmStatic
        fun setFromString(enumName: String) {
            val newType = nameToPlayerType[enumName]
            if (newType != null && current != newType) {
                LogHelper.printDebug(
                    PlayerType::class.java,
                    "PlayerType changed to: $newType"
                )
                current = newType
            }
        }

        /**
         * The current player type.
         */
        @JvmStatic
        var current
            get() = currentPlayerType
            private set(value) {
                currentPlayerType = value
                onChange(currentPlayerType)
            }

        @Volatile // value is read/write from different threads
        private var currentPlayerType = MINIMIZED

        /**
         * player type change listener
         */
        @JvmStatic
        val onChange = Event<PlayerType>()
    }

    fun isDismissedOrMinimized(): Boolean {
        return this == DISMISSED || this == MINIMIZED
    }
}