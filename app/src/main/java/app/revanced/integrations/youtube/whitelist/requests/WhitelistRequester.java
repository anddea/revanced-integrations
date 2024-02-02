package app.revanced.integrations.youtube.whitelist.requests;

import static app.revanced.integrations.youtube.patches.misc.requests.PlayerRoutes.GET_CHANNEL_INFORMATION;
import static app.revanced.integrations.youtube.patches.misc.requests.PlayerRoutes.TV_EMBED_INNER_TUBE_BODY;
import static app.revanced.integrations.youtube.utils.ReVancedUtils.runOnMainThread;
import static app.revanced.integrations.youtube.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.youtube.utils.StringRef.str;

import org.json.JSONObject;
import org.json.JSONArray;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import app.revanced.integrations.youtube.patches.video.VideoChannel;
import app.revanced.integrations.youtube.patches.video.VideoInformation;
import app.revanced.integrations.youtube.patches.misc.requests.PlayerRoutes;
import app.revanced.integrations.youtube.requests.Requester;
import app.revanced.integrations.youtube.utils.LogHelper;
import app.revanced.integrations.youtube.utils.ReVancedHelper;
import app.revanced.integrations.youtube.utils.ReVancedUtils;
import app.revanced.integrations.youtube.whitelist.Whitelist;
import app.revanced.integrations.youtube.whitelist.WhitelistType;

public class WhitelistRequester {

    private WhitelistRequester() {
    }

    public static void addChannelToWhitelist(WhitelistType whitelistType) {
        try {
            var context = Objects.requireNonNull(ReVancedUtils.getContext());
            String videoId = VideoInformation.getVideoId();
            // Use TVHTML5_SIMPLY_EMBEDDED_PLAYER by default to bypass age restriction video
            final byte[] innerTubeBody = String.format(TV_EMBED_INNER_TUBE_BODY, videoId, videoId).getBytes(StandardCharsets.UTF_8);

            HttpURLConnection connection = PlayerRoutes.getPlayerResponseConnectionFromRoute(GET_CHANNEL_INFORMATION);
            connection.getOutputStream().write(innerTubeBody, 0, innerTubeBody.length);

            final int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                final JSONObject json = getJSONObject(connection);
                JSONObject videoDetails = json.getJSONObject("videoDetails");
                VideoChannel channelModel = new VideoChannel(videoDetails.getString("author"), videoDetails.getString("channelId"));

                boolean success = Whitelist.addToWhitelist(whitelistType, context, channelModel);
                String whitelistTypeName = whitelistType.getFriendlyName();
                runOnMainThread(() -> {
                    if (success) {
                        showToastShort(str("revanced_whitelisting_added", channelModel.getAuthor(), whitelistTypeName));
                    } else {
                        showToastShort(str("revanced_whitelisting_add_failed", channelModel.getAuthor(), whitelistTypeName));
                    }
                });
            } else {
                runOnMainThread(() -> showToastShort(str("revanced_whitelisting_fetch_failed", responseCode)));
            }
            connection.disconnect();
        } catch (Exception ex) {
            LogHelper.printException(() -> "Failed to fetch channelId", ex);
            runOnMainThread(() -> showToastShort(str("revanced_whitelisting_fetch_failed")));
        }
    }

    // helpers

    private static JSONObject getJSONObject(HttpURLConnection connection) throws Exception {
        return Requester.parseJSONObjectAndDisconnect(connection);
    }
}
