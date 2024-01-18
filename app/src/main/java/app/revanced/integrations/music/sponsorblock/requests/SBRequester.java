package app.revanced.integrations.music.sponsorblock.requests;

import static app.revanced.integrations.music.utils.StringRef.str;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.revanced.integrations.music.requests.Requester;
import app.revanced.integrations.music.settings.SettingsEnum;
import app.revanced.integrations.music.sponsorblock.SponsorBlockSettings;
import app.revanced.integrations.music.sponsorblock.objects.SegmentCategory;
import app.revanced.integrations.music.sponsorblock.objects.SponsorSegment;
import app.revanced.integrations.music.utils.LogHelper;
import app.revanced.integrations.music.utils.ReVancedUtils;

public class SBRequester {
    /**
     * TCP timeout
     */
    private static final int TIMEOUT_TCP_DEFAULT_MILLISECONDS = 7000;

    /**
     * HTTP response timeout
     */
    private static final int TIMEOUT_HTTP_DEFAULT_MILLISECONDS = 10000;

    /**
     * Response code of a successful API call
     */
    private static final int HTTP_STATUS_CODE_SUCCESS = 200;

    private SBRequester() {
    }

    private static void handleConnectionError(@NonNull String toastMessage, @Nullable Exception ex) {
        if (SettingsEnum.SB_TOAST_ON_CONNECTION_ERROR.getBoolean()) {
            ReVancedUtils.showToastShort(toastMessage);
        }
        if (ex != null) {
            LogHelper.printInfo(() -> toastMessage, ex);
        }
    }

    @NonNull
    public static SponsorSegment[] getSegments(@NonNull String videoId) {
        ReVancedUtils.verifyOffMainThread();
        List<SponsorSegment> segments = new ArrayList<>();
        try {
            HttpURLConnection connection = getConnectionFromRoute(videoId, SegmentCategory.sponsorBlockAPIFetchCategories);
            final int responseCode = connection.getResponseCode();

            if (responseCode == HTTP_STATUS_CODE_SUCCESS) {
                JSONArray responseArray = Requester.parseJSONArray(connection);
                final long minSegmentDuration = 0;
                for (int i = 0, length = responseArray.length(); i < length; i++) {
                    JSONObject obj = (JSONObject) responseArray.get(i);
                    JSONArray segment = obj.getJSONArray("segment");
                    final long start = (long) (segment.getDouble(0) * 1000);
                    final long end = (long) (segment.getDouble(1) * 1000);

                    String uuid = obj.getString("UUID");
                    final boolean locked = obj.getInt("locked") == 1;
                    String categoryKey = obj.getString("category");
                    SegmentCategory category = SegmentCategory.byCategoryKey(categoryKey);
                    if (category == null) {
                        LogHelper.printException(() -> "Received unknown category: " + categoryKey); // should never happen
                    } else if ((end - start) >= minSegmentDuration) {
                        segments.add(new SponsorSegment(category, uuid, start, end, locked));
                    }
                }
                runVipCheckInBackgroundIfNeeded();
            } else if (responseCode == 404) {
                // no segments are found.  a normal response
                LogHelper.printDebug(() -> "No segments found for video: " + videoId);
            } else {
                handleConnectionError(str("sb_sponsorblock_connection_failure_status", responseCode), null);
                connection.disconnect(); // something went wrong, might as well disconnect
            }
        } catch (SocketTimeoutException ex) {
            handleConnectionError(str("sb_sponsorblock_connection_failure_timeout"), ex);
        } catch (IOException ex) {
            handleConnectionError(str("sb_sponsorblock_connection_failure_generic"), ex);
        } catch (Exception ex) {
            // Should never happen
            LogHelper.printException(() -> "getSegments failure", ex);
        }

        return segments.toArray(new SponsorSegment[0]);
    }

    public static void runVipCheckInBackgroundIfNeeded() {
        if (!SponsorBlockSettings.userHasSBPrivateId()) {
            return; // User cannot be a VIP. User has never voted, created any segments, or has imported a SB user id.
        }
        long now = System.currentTimeMillis();
        if (now < (SettingsEnum.SB_LAST_VIP_CHECK.getLong() + TimeUnit.DAYS.toMillis(3))) {
            return;
        }
        ReVancedUtils.runOnBackgroundThread(() -> {
            try {
                SettingsEnum.SB_LAST_VIP_CHECK.saveValue(now);
            } catch (Exception ex) {
                LogHelper.printException(() -> "Failed to check VIP", ex); // should never happen
            }
        });
    }

    // helpers

    private static HttpURLConnection getConnectionFromRoute(String... params) throws IOException {
        HttpURLConnection connection = Requester.getConnectionFromRoute(SettingsEnum.SB_API_URL.getString(), SBRoutes.GET_SEGMENTS, params);
        connection.setConnectTimeout(TIMEOUT_TCP_DEFAULT_MILLISECONDS);
        connection.setReadTimeout(TIMEOUT_HTTP_DEFAULT_MILLISECONDS);
        return connection;
    }
}
