package app.revanced.integrations.youtube.patches.misc;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import org.chromium.net.UrlRequest;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import app.revanced.integrations.shared.settings.Setting;
import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.shared.utils.Utils;
import app.revanced.integrations.youtube.patches.misc.client.AppClient.ClientType;
import app.revanced.integrations.youtube.patches.misc.requests.StreamingDataRequester;
import app.revanced.integrations.youtube.settings.Settings;

@SuppressWarnings("unused")
public class SpoofStreamingDataPatch {
    private static final boolean SPOOF_STREAMING_DATA = Settings.SPOOF_STREAMING_DATA.get();

    /**
     * Any unreachable ip address.  Used to intentionally fail requests.
     */
    private static final String UNREACHABLE_HOST_URI_STRING = "https://127.0.0.0";
    private static final Uri UNREACHABLE_HOST_URI = Uri.parse(UNREACHABLE_HOST_URI_STRING);

    private static volatile Future<ByteBuffer> currentVideoStream;

    private static String url;
    private static Map<String, String> playerHeaders;

    /**
     * Injection point.
     * Blocks /get_watch requests by returning an unreachable URI.
     *
     * @param playerRequestUri The URI of the player request.
     * @return An unreachable URI if the request is a /get_watch request, otherwise the original URI.
     */
    public static Uri blockGetWatchRequest(Uri playerRequestUri) {
        if (SPOOF_STREAMING_DATA) {
            try {
                String path = playerRequestUri.getPath();

                if (path != null && path.contains("get_watch")) {
                    Logger.printDebug(() -> "Blocking 'get_watch' by returning unreachable uri");

                    return UNREACHABLE_HOST_URI;
                }
            } catch (Exception ex) {
                Logger.printException(() -> "blockGetWatchRequest failure", ex);
            }
        }

        return playerRequestUri;
    }

    /**
     * Injection point.
     * <p>
     * Blocks /initplayback requests.
     */
    public static String blockInitPlaybackRequest(String originalUrlString) {
        if (SPOOF_STREAMING_DATA) {
            try {
                var originalUri = Uri.parse(originalUrlString);
                String path = originalUri.getPath();

                if (path != null && path.contains("initplayback")) {
                    Logger.printDebug(() -> "Blocking 'initplayback' by returning unreachable url");

                    return UNREACHABLE_HOST_URI_STRING;
                }
            } catch (Exception ex) {
                Logger.printException(() -> "blockInitPlaybackRequest failure", ex);
            }
        }

        return originalUrlString;
    }

    /**
     * Injection point.
     */
    public static boolean isSpoofingEnabled() {
        return SPOOF_STREAMING_DATA;
    }

    /**
     * Injection point.
     */
    public static void setHeader(String url, Map<String, String> playerHeaders) {
        if (SPOOF_STREAMING_DATA) {
            SpoofStreamingDataPatch.url = url;
            SpoofStreamingDataPatch.playerHeaders = playerHeaders;
        }
    }

    /**
     * Injection point.
     */
    public static UrlRequest buildRequest(UrlRequest.Builder builder) {
        if (SPOOF_STREAMING_DATA) {
            try {
                Uri uri = Uri.parse(url);
                String path = uri.getPath();
                if (path != null && path.contains("player") && !path.contains("heartbeat")) {
                    String videoId = Objects.requireNonNull(uri.getQueryParameter("id"));
                    currentVideoStream = StreamingDataRequester.fetch(videoId, playerHeaders);
                }
            } catch (Exception ex) {
                Logger.printException(() -> "buildRequest failure", ex);
            }
        }

        return builder.build();
    }

    /**
     * Injection point.
     * Fix playback by replace the streaming data.
     * Called after {@link #buildRequest(UrlRequest.Builder)}.
     */
    @Nullable
    public static ByteBuffer getStreamingData(String videoId) {
        if (SPOOF_STREAMING_DATA) {
            try {
                Utils.verifyOffMainThread();

                var future = currentVideoStream;
                if (future != null) {
                    final long maxSecondsToWait = 20;
                    var stream = future.get(maxSecondsToWait, TimeUnit.SECONDS);
                    if (stream != null) {
                        Logger.printDebug(() -> "Overriding video stream");
                        return stream;
                    }

                    Logger.printDebug(() -> "Not overriding streaming data (video stream is null)");
                }
            } catch (TimeoutException ex) {
                Logger.printInfo(() -> "getStreamingData timed out", ex);
            } catch (InterruptedException ex) {
                Logger.printException(() -> "getStreamingData interrupted", ex);
                Thread.currentThread().interrupt(); // Restore interrupt status flag.
            } catch (ExecutionException ex) {
                Logger.printException(() -> "getStreamingData failure", ex);
            }
        }

        return null;
    }

    /**
     * Injection point.
     * Called after {@link #getStreamingData(String)}.
     */
    @Nullable
    public static byte[] removeVideoPlaybackPostBody(Uri uri, int method, byte[] postData) {
        if (SPOOF_STREAMING_DATA) {
            try {
                final int methodPost = 2;
                if (method == methodPost) {
                    String path = uri.getPath();
                    String clientName = "c";
                    final boolean iosClient = ClientType.IOS.name().equals(uri.getQueryParameter(clientName));
                    if (iosClient && path != null && path.contains("videoplayback")) {
                        return null;
                    }
                }
            }  catch (Exception ex) {
                Logger.printException(() -> "removeVideoPlaybackPostBody failure", ex);
            }
        }

        return postData;
    }

    /**
     * Injection point.
     */
    public static String appendSpoofedClient(String videoFormat) {
        try {
            if (SPOOF_STREAMING_DATA && Settings.SPOOF_STREAMING_DATA_STATS_FOR_NERDS.get()
                    && !TextUtils.isEmpty(videoFormat)) {
                // Force LTR layout, to match the same LTR video time/length layout YouTube uses for all languages
                return "\u202D" + videoFormat + String.format("\u2009(%s)", StreamingDataRequester.getLastSpoofedClientName()); // u202D = left to right override
            }
        } catch (Exception ex) {
            Logger.printException(() -> "appendSpoofedClient failure", ex);
        }

        return videoFormat;
    }

    public static final class ForceiOSAVCAvailability implements Setting.Availability {
        @Override
        public boolean isAvailable() {
            return Settings.SPOOF_STREAMING_DATA.get() && Settings.SPOOF_STREAMING_DATA_TYPE.get() == ClientType.IOS;
        }
    }
}