package app.revanced.integrations.returnyoutubedislike.requests;

import static app.revanced.integrations.returnyoutubedislike.requests.ReturnYouTubeDislikeRoutes.getRYDMIRRORConnectionFromRoute;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import app.revanced.integrations.requests.Requester;
import app.revanced.integrations.returnyoutubedislike.ReturnYouTubeDislikeMirror;
import app.revanced.integrations.utils.LogHelper;

public class ReturnYouTubeDislikeMirrorApi {
    private static final int API_GET_VOTES_TCP_TIMEOUT_MILLISECONDS = 2000;
    private static final int API_GET_VOTES_HTTP_TIMEOUT_MILLISECONDS = 4000;

    private ReturnYouTubeDislikeMirrorApi() {
    }

    public static void fetchDislikes(String videoId) {
        try {
            HttpURLConnection connection = getRYDMIRRORConnectionFromRoute(ReturnYouTubeDislikeRoutes.GET_DISLIKES_MIRROR, videoId);
            connection.setConnectTimeout(API_GET_VOTES_TCP_TIMEOUT_MILLISECONDS); // timeout for TCP connection to server
            connection.setReadTimeout(API_GET_VOTES_HTTP_TIMEOUT_MILLISECONDS); // timeout for server response
            if (connection.getResponseCode() == 200) {
                JSONObject json = getJSONObject(connection);
                int likeCount = json.getInt("likes");
                int dislikeCount = json.getInt("dislikes");
                float dislikePercentage = (dislikeCount == 0 ? 0 : (float) dislikeCount / (likeCount + dislikeCount));

                ReturnYouTubeDislikeMirror.setValues((long) likeCount, (long) dislikeCount, dislikePercentage);
            }
            connection.disconnect();
        } catch (Exception ex) {
            ReturnYouTubeDislikeMirror.setValues(0L, 0L, 0f);

            LogHelper.printException(ReturnYouTubeDislikeMirrorApi.class, "Failed to fetch dislikes", ex);
        }
    }

    // helpers

    private static JSONObject getJSONObject(HttpURLConnection connection) throws Exception {
        return Requester.parseJSONObjectAndDisconnect(connection);
    }
}