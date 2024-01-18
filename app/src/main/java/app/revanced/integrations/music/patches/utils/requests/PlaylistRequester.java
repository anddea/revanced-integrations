package app.revanced.integrations.music.patches.utils.requests;

import static app.revanced.integrations.music.patches.utils.CheckMusicVideoPatch.clearInformation;
import static app.revanced.integrations.music.patches.utils.CheckMusicVideoPatch.setSongId;
import static app.revanced.integrations.music.patches.utils.requests.PlaylistRoutes.GET_PLAYLIST;
import static app.revanced.integrations.music.patches.utils.requests.PlaylistRoutes.getPlaylistConnectionFromRoute;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

import app.revanced.integrations.music.requests.Requester;
import app.revanced.integrations.music.utils.LogHelper;
import app.revanced.integrations.music.utils.ReVancedUtils;

public class PlaylistRequester {
    /**
     * TCP timeout
     */
    private static final int TIMEOUT_TCP_DEFAULT_MILLISECONDS = 2000;

    /**
     * HTTP response timeout
     */
    private static final int TIMEOUT_HTTP_DEFAULT_MILLISECONDS = 4000;

    /**
     * Response code of a successful API call
     */
    private static final int HTTP_STATUS_CODE_SUCCESS = 200;

    private PlaylistRequester() {
    }

    public static void fetchPlaylist(@NonNull String videoId, @NonNull String playlistId, final int playlistIndex) {
        try {
            ReVancedUtils.verifyOffMainThread();
            HttpURLConnection connection = getPlaylistConnectionFromRoute(GET_PLAYLIST, playlistId);
            connection.setConnectTimeout(TIMEOUT_TCP_DEFAULT_MILLISECONDS);
            connection.setReadTimeout(TIMEOUT_HTTP_DEFAULT_MILLISECONDS);

            final int responseCode = connection.getResponseCode();

            if (responseCode != HTTP_STATUS_CODE_SUCCESS) {
                handleConnectionError("API not available: " + responseCode);
                connection.disconnect();
                return;
            }

            final JSONObject playerResponse = Requester.parseJSONObject(connection);
            final JSONArray playlistArray = playerResponse.getJSONArray("relatedStreams");
            final JSONObject songInfo = playlistArray.getJSONObject(playlistIndex);
            final String songId = songInfo.getString("url").replaceAll("/.+=", "");

            if (songId.isEmpty()) {
                handleConnectionError("Url is empty!");
            } else if (!songId.equals(videoId)) {
                LogHelper.printDebug(() -> String.format("Fetched successfully\nVideoId: %s\nPlaylistId:%s\nSongId: %s", videoId, playlistId, songId));
                setSongId(songId);
            }
            connection.disconnect();
        } catch (SocketTimeoutException ex) {
            handleConnectionError("API timed out", ex);
        } catch (IOException ex) {
            handleConnectionError(String.format("Failed to fetch Playlist (%s)", ex.getMessage()), ex);
        } catch (Exception ex) {
            handleConnectionError("Failed to fetch Playlist", ex);
        }
    }

    private static void handleConnectionError(@NonNull String errorMessage) {
        LogHelper.printInfo(() -> errorMessage);
        clearInformation();
    }

    private static void handleConnectionError(@NonNull String errorMessage, @Nullable Exception ex) {
        if (ex != null) {
            LogHelper.printInfo(() -> errorMessage, ex);
        }
        clearInformation();
    }
}
