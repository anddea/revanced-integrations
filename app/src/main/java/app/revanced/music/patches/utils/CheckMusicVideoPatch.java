package app.revanced.music.patches.utils;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import app.revanced.music.patches.utils.requests.PlaylistRequester;
import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;

public class CheckMusicVideoPatch {

    @NonNull
    private static volatile String currentPlaylistId = "";

    private static volatile int currentPlaylistIndex = -1;
    @NonNull
    private static volatile String currentSongId = "";

    @NonNull
    private static volatile String currentVideoId = "";

    /**
     * Injection point.
     *
     * @param videoId    Original video id value from PlaybackStartDescriptor.
     * @param playlistId    Original playlist id value from PlaybackStartDescriptor.
     * @param playlistIndex    Original playlist index value from PlaybackStartDescriptor.
     */
    @SuppressLint("DefaultLocale")
    public static void playbackStart(@NonNull String videoId, @NonNull String playlistId, final int playlistIndex) {
        try {
            if (!SettingsEnum.REPLACE_PLAYER_CAST_BUTTON.getBoolean())
                return;

            if (currentVideoId.equals(videoId)) {
                return;
            }

            if (currentPlaylistId.equals(playlistId) && currentPlaylistIndex == playlistIndex) {
                return;
            }

            LogHelper.printDebug(CheckMusicVideoPatch.class, String.format("Playback Started\nVideo Id: %s\nPlaylist Id: %s\nPlaylist Index: %d", videoId, playlistId, playlistIndex));

            currentVideoId = videoId;
            currentPlaylistId = playlistId;
            currentPlaylistIndex = playlistIndex;

            PlaylistRequester.fetchPlaylist(videoId, playlistId, playlistIndex);
        } catch (Exception ex) {
            LogHelper.printException(CheckMusicVideoPatch.class, "playbackStart failure", ex);
        }
    }

    public static String getSongId() {
        return currentSongId;
    }

    public static void setSongId(@NonNull String videoId) {
        currentVideoId = videoId;
        currentSongId = videoId;
    }

    public static void clearInformation() {
        currentPlaylistIndex = -1;
        currentPlaylistId = "";
        currentSongId = "";
    }
}
