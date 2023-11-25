package app.revanced.integrations.shared

/**
 * VideoState playback state.
 */
enum class VideoState {
    NEW,
    PLAYING,
    PAUSED,
    RECOVERABLE_ERROR,
    UNRECOVERABLE_ERROR,
    ENDED;

    companion object {

        private val nameToVideoState = values().associateBy { it.name }

        @JvmStatic
        fun setFromString(enumName: String) {
            val state = nameToVideoState[enumName]
            if (state != null && currentVideoState != state)
                currentVideoState = state
        }

        /**
         * Depending on which hook this is called from,
         * this value may not be up to date with the actual playback state.
         */
        @JvmStatic
        var current: VideoState?
            get() = currentVideoState
            private set(value) {
                currentVideoState = value
            }

        private var currentVideoState: VideoState? = null
    }
}