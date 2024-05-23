package app.revanced.integrations.youtube.patches.misc.requests;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import app.revanced.integrations.shared.requests.Requester;
import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.shared.utils.Utils;
import app.revanced.integrations.youtube.patches.misc.StoryboardRenderer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static app.revanced.integrations.youtube.patches.misc.requests.PlayerRoutes.*;

public class StoryboardRendererRequester {

    private StoryboardRendererRequester() {
    }

    private static void handleConnectionError(@NonNull String toastMessage, @Nullable Exception ex) {
        Logger.printInfo(() -> toastMessage, ex);
    }

    @Nullable
    private static JSONObject fetchPlayerResponse(@NonNull String requestBody) {
        final long startTime = System.currentTimeMillis();
        try {
            Utils.verifyOffMainThread();
            Objects.requireNonNull(requestBody);

            final byte[] innerTubeBody = requestBody.getBytes(StandardCharsets.UTF_8);

            HttpURLConnection connection = PlayerRoutes.getPlayerResponseConnectionFromRoute(GET_STORYBOARD_SPEC_RENDERER);
            connection.getOutputStream().write(innerTubeBody, 0, innerTubeBody.length);

            final int responseCode = connection.getResponseCode();
            if (responseCode == 200) return Requester.parseJSONObject(connection);

            // Always show a toast for this, as a non 200 response means something is broken.
            handleConnectionError("Spoof storyboard not available: " + responseCode, null);
            connection.disconnect();
        } catch (SocketTimeoutException ex) {
            handleConnectionError("Spoof storyboard temporarily not available (API timed out)", ex);
        } catch (IOException ex) {
            handleConnectionError("Spoof storyboard temporarily not available: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            Logger.printException(() -> "Spoof storyboard fetch failed", ex); // Should never happen.
        } finally {
            Logger.printDebug(() -> "Request took: " + (System.currentTimeMillis() - startTime) + "ms");
        }

        return null;
    }

    private static String getPlayabilityStatus(@NonNull JSONObject playerResponse) {
        try {
            return playerResponse.getJSONObject("playabilityStatus").getString("status");
        } catch (JSONException e) {
            Logger.printDebug(() -> "Failed to get playabilityStatus for response: " + playerResponse);
        }

        // Prevent NullPointerException
        return "";
    }

    /**
     * Fetches the storyboardRenderer from the innerTubeBody.
     *
     * @param innerTubeBody The innerTubeBody to use to fetch the storyboardRenderer.
     * @return StoryboardRenderer or null if playabilityStatus is not OK.
     */
    @Nullable
    private static StoryboardRenderer getStoryboardRendererUsingBody(@NonNull String videoId, @NonNull String innerTubeBody) {
        final JSONObject playerResponse = fetchPlayerResponse(innerTubeBody);
        if (playerResponse == null) return null;

        final String playabilityStatus = getPlayabilityStatus(playerResponse);

        if (playabilityStatus.equals("OK")) return getStoryboardRendererUsingResponse(videoId, playerResponse);

        // Get the StoryboardRenderer from Premieres Video.
        // In Android client, YouTube used weird base64-like encoding for PlayerResponse.
        // So additional fetching with WEB client is required for getting unSerialized ones.
        if (playabilityStatus.equals("LIVE_STREAM_OFFLINE")) return getTrailerStoryboardRenderer(videoId);
        return null;
    }

    @Nullable
    private static StoryboardRenderer getTrailerStoryboardRenderer(@NonNull String videoId) {
        try {
            final JSONObject playerResponse = fetchPlayerResponse(String.format(WEB_INNER_TUBE_BODY, videoId));

            if (playerResponse == null) return null;

            JSONObject unSerializedPlayerResponse = playerResponse.getJSONObject("playabilityStatus").getJSONObject("errorScreen").getJSONObject("ypcTrailerRenderer").getJSONObject("unserializedPlayerResponse");

            if (getPlayabilityStatus(unSerializedPlayerResponse).equals("OK"))
                return getStoryboardRendererUsingResponse(videoId, unSerializedPlayerResponse);
            return null;
        } catch (JSONException e) {
            Logger.printException(() -> "Failed to get unserializedPlayerResponse", e);
        }

        return null;
    }

    @Nullable
    private static StoryboardRenderer getStoryboardRendererUsingResponse(@NonNull String videoId, @NonNull JSONObject playerResponse) {
        try {
            Logger.printDebug(() -> "Parsing storyboardRenderer from response: " + playerResponse);
            if (!playerResponse.has("storyboards")) {
                Logger.printDebug(() -> "Using empty storyboard");
                return new StoryboardRenderer(videoId, null, false, null);
            }
            final JSONObject storyboards = playerResponse.getJSONObject("storyboards");
            final boolean isLiveStream = storyboards.has("playerLiveStoryboardSpecRenderer");
            final String storyboardsRendererTag = isLiveStream ? "playerLiveStoryboardSpecRenderer" : "playerStoryboardSpecRenderer";

            final var rendererElement = storyboards.getJSONObject(storyboardsRendererTag);
            StoryboardRenderer renderer = new StoryboardRenderer(videoId, rendererElement.getString("spec"), isLiveStream, rendererElement.has("recommendedLevel") ? rendererElement.getInt("recommendedLevel") : null);

            Logger.printDebug(() -> "Fetched: " + renderer);

            return renderer;
        } catch (JSONException e) {
            Logger.printException(() -> "Failed to get storyboardRenderer", e);
        }

        return null;
    }

    @Nullable
    public static StoryboardRenderer getStoryboardRenderer(@NonNull String videoId) {
        Objects.requireNonNull(videoId);

        StoryboardRenderer renderer = getStoryboardRendererUsingBody(
                videoId, String.format(ANDROID_INNER_TUBE_BODY, videoId)
        );
        if (renderer == null) {
            Logger.printDebug(() -> videoId + " not available using Android client");
            renderer = getStoryboardRendererUsingBody(
                    videoId, String.format(TV_EMBED_INNER_TUBE_BODY, videoId, videoId)
            );
            if (renderer == null) {
                Logger.printDebug(() -> videoId + " not available using TV embedded client");
            }
        }

        return renderer;
    }
}