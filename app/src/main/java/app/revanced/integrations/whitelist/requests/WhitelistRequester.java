package app.revanced.integrations.whitelist.requests;

import static app.revanced.integrations.utils.ReVancedUtils.runOnMainThread;
import static app.revanced.integrations.utils.StringRef.str;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import app.revanced.integrations.patches.video.VideoInformation;
import app.revanced.integrations.requests.Requester;
import app.revanced.integrations.sponsorblock.player.ChannelModel;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedHelper;
import app.revanced.integrations.utils.ReVancedUtils;
import app.revanced.integrations.whitelist.Whitelist;
import app.revanced.integrations.whitelist.WhitelistType;

public class WhitelistRequester {
    private static final String YT_API_URL = "https://www.youtube.com/youtubei/v1/";
    private static final String YT_API_KEY = "AIzaSyA8eiZmM1FaDVjRy-df2KTyQ_vz_yYM39w";

    private WhitelistRequester() {
    }

    public static void addChannelToWhitelist(WhitelistType whitelistType) {
        try {
            Context context = ReVancedUtils.getContext();

            HttpURLConnection connection = getConnectionFromRoute(YT_API_KEY);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(2 * 1000);

            String versionName = ReVancedHelper.getVersionName();
            String jsonInputString = "{\"context\": {\"client\": { \"clientName\": \"Android\", \"clientVersion\": \"" + versionName + "\" } }, \"videoId\": \"" + VideoInformation.getCurrentVideoId() + "\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                JSONObject json = getJSONObject(connection);
                JSONObject videoInfo = json.getJSONObject("videoDetails");
                ChannelModel channelModel = new ChannelModel(videoInfo.getString("author"), videoInfo.getString("channelId"));
                String author = channelModel.getAuthor();

                boolean success = Whitelist.addToWhitelist(whitelistType, context, channelModel);
                String whitelistTypeName = whitelistType.getFriendlyName();
                runOnMainThread(() -> {
                    if (success) {
                        Toast.makeText(context, str("revanced_whitelisting_added", author, whitelistTypeName), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, str("revanced_whitelisting_add_failed", author, whitelistTypeName), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                runOnMainThread(() -> {
                    Toast.makeText(context, str("revanced_whitelisting_fetch_failed", responseCode), Toast.LENGTH_SHORT).show();
                });
            }
            connection.disconnect();
        } catch (Exception ex) {
            LogHelper.printException(WhitelistRequester.class, "Failed to fetch channelId", ex);
            runOnMainThread(() -> {
                Toast.makeText(ReVancedUtils.getContext(), str("revanced_whitelisting_fetch_failed"), Toast.LENGTH_SHORT).show();
            });
        }
    }

    // helpers

    private static HttpURLConnection getConnectionFromRoute(String... params) throws IOException {
        return Requester.getConnectionFromRoute(YT_API_URL, WhitelistRoutes.GET_CHANNEL_DETAILS, params);
    }

    private static JSONObject getJSONObject(HttpURLConnection connection) throws Exception {
        return Requester.parseJSONObjectAndDisconnect(connection);
    }
}