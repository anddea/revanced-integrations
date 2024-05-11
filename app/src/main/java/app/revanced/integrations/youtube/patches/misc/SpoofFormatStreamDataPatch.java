package app.revanced.integrations.youtube.patches.misc;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.downloader.Request;
import org.schabi.newpipe.extractor.downloader.Response;
import org.schabi.newpipe.extractor.services.youtube.YoutubeService;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.VideoStream;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import app.revanced.integrations.shared.requests.Requester;
import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.shared.utils.Utils;
import app.revanced.integrations.youtube.settings.Settings;

@SuppressWarnings("unused")
@RequiresApi(26) // Some methods of NewPipeExtractor are only available in Android 8.0+.
public final class SpoofFormatStreamDataPatch {
    private static final boolean spoofFormatStreamData = Settings.SPOOF_FORMAT_STREAM_DATA.get();

    /**
     * Response code of a successful API call
     */
    private static final int HTTP_STATUS_CODE_SUCCESS = 200;

    /**
     * Indicates a client rate limit has been reached and the client must back off.
     */
    private static final int HTTP_STATUS_CODE_RATE_LIMIT = 429;

    private static final String USER_AGENT
            = "Mozilla/5.0 (Windows NT 10.0; rv:91.0) Gecko/20100101 Firefox/91.0";

    /**
     * Last video id loaded. Used to prevent reloading the same spec multiple times.
     */
    @NonNull
    private static volatile String lastPlayerResponseVideoId = "";

    // TODO: Check that the access modifier for this variable is appropriate.
    private static Map<Integer, String> formatStreamDataMap;

    private static Downloader instance = getDownloader();

    /**
     * Injection point.
     * TODO: Check if there are any issues without wrapping this method in a try .. catch block.
     */
    public static void hookFormatStreamData() {
        if (!spoofFormatStreamData) {
            return;
        }
        String formatStreamData = getFormatStreamData();
        if (!formatStreamData.contains("googlevideo")) {
            return;
        }
        Logger.printDebug(() -> "Original FormatStreamData: " + formatStreamData);
        String itag = Uri.parse(formatStreamData).getQueryParameter("itag");
        Logger.printDebug(() -> "Hooked itag: " + itag);
        if (itag == null) {
            return;
        }

        // find nearest key to itag
        Set<Integer> availableTags = formatStreamDataMap.keySet();
        Integer nearest = availableTags.stream().min(Comparator.comparingInt(a -> Math.abs(Integer.parseInt(itag) - a))).orElse(null);
        Logger.printDebug(() -> "Hooked count: " + formatStreamDataMap.size());
        Logger.printDebug(() -> "Hooked nearest " + nearest);

        String format = formatStreamDataMap.get(nearest);
        if (format == null) {
            Logger.printDebug(() -> "Hooked format null");
            return;
        }
        Logger.printDebug(() -> "Hooked format " + format);
        setFormatStreamData(format);
    }

    /**
     * Injection point.
     * TODO: Make sure there are no issues without checking if the current video is Shorts.
     */
    public static void newPlayerResponseVideoId(@NonNull String videoId, boolean isShortAndOpeningOrPlaying) {
        if (!spoofFormatStreamData) {
            return;
        }
        if (videoId.equals(lastPlayerResponseVideoId)) {
            return;
        }
        lastPlayerResponseVideoId = videoId;

        try {
            formatStreamDataMap = Utils.submitOnBackgroundThread(() -> {
                String url = String.format("https://www.youtube.com/watch?v=%s", videoId);
                HashMap<Integer, String> formatStreamMap = new HashMap<>();
                if (instance == null) {
                    instance = getDownloader();
                }
                NewPipe.init(instance);

                // TODO: [YouTubeStreamExtractor] always fetches to the [WEB] client.
                //       (Fetch with [Android] client provided by NewPipeExtractor also has a playback buffer issue: https://github.com/TeamNewPipe/NewPipeExtractor/issues/1164)
                //       Adds new class that extends [YouTubeStreamExtractor] to integrations and fetch with [ANDROID_TESTSUITE] client: https://github.com/iv-org/invidious/pull/4650
                StreamExtractor extractor = new YoutubeService(1).getStreamExtractor(url);
                extractor.fetchPage();
                Logger.printDebug(() -> "Hooked got extractor");
                for (AudioStream audioStream : extractor.getAudioStreams()) {
                    formatStreamMap.put(audioStream.getItag(), audioStream.getContent());
                }

                Logger.printDebug(() -> "Hooked got audio");

                for (VideoStream videoOnlyStream : extractor.getVideoOnlyStreams()) {
                    formatStreamMap.put(videoOnlyStream.getItag(), videoOnlyStream.getContent());
                }

                Logger.printDebug(() -> "Hooked got video only");

                for (VideoStream videoStream : extractor.getVideoStreams()) {
                    formatStreamMap.put(videoStream.getItag(), videoStream.getContent());
                }
                Logger.printDebug(() -> "Hooked got format");

                return formatStreamMap;
            }).get();
        } catch (Exception ex) {
            Logger.printException(() -> "Hooked Error making request: " + ex.getMessage(), ex);
        }
    }

    private static Downloader getDownloader() {
        return new Downloader() {
            @Override
            public Response execute(@NonNull Request request) throws IOException {
                HttpURLConnection connection = makeRequest(request);
                Logger.printDebug(() -> "Hooked got response");
                boolean body = false;
                Response response;
                try {
                    assert connection != null;
                    body = connection.getInputStream() != null;
                } catch (Exception ex) {
                    Logger.printException(() -> "Hooked Error making request: " + ex.getMessage(), ex);
                }
                try {
                    response = new Response(
                            connection.getResponseCode(),
                            connection.getResponseMessage(),
                            connection.getHeaderFields(),
                            body ? Requester.parseString(connection) : null,
                            connection.getURL().toString()
                    );
                } catch (IOException ex) {
                    Logger.printException(() -> "Hooked Error making request: " + ex.getMessage(), ex);
                    throw ex;
                }
                connection.disconnect();
                return response;
            }
        };
    }

    // TODO: Adds a setting that allows users to choose whether or not to show toast messages.
    private static void handleConnectionError(@NonNull String toastMessage, @Nullable Exception ex) {
        Logger.printInfo(() -> toastMessage, ex);
    }

    @Nullable
    private static HttpURLConnection makeRequest(final Request request) {
        try {
            Logger.printDebug(() -> "Hooked request");

            HttpURLConnection connection = (HttpURLConnection) new URL(request.url()).openConnection();
            connection.setRequestMethod(request.httpMethod());
            connection.setUseCaches(false);
            connection.setDoOutput(Objects.equals(request.httpMethod(), "POST"));
            connection.setRequestProperty("User-Agent", USER_AGENT);

            for (final Map.Entry<String, List<String>> pair : request.headers().entrySet()) {
                final String headerName = pair.getKey();
                final List<String> headerValueList = pair.getValue();

                if (headerValueList.size() > 1) {
                    for (final String headerValue : headerValueList) {
                        connection.addRequestProperty(headerName, headerValue);
                    }
                } else if (headerValueList.size() == 1) {
                    connection.addRequestProperty(headerName, headerValueList.get(0));
                }
            }
            Logger.printDebug(() -> "Hooked headers");

            final byte[] innerTubeBody = request.dataToSend();
            if (innerTubeBody != null) {
                connection.getOutputStream().write(innerTubeBody, 0, innerTubeBody.length);
            }
            Logger.printDebug(() -> "Hooked body");

            final int responseCode = connection.getResponseCode();
            if (responseCode == HTTP_STATUS_CODE_SUCCESS) {
                return connection;
            } else if (responseCode == HTTP_STATUS_CODE_RATE_LIMIT) {
                handleConnectionError("Hooked reCaptcha Challenge requested", null);
            } else {
                handleConnectionError("Hooked Error making request: " + responseCode, null);
            }
        } catch (Exception exception) {
            handleConnectionError("Hooked Error making request: " + exception.getMessage(), exception);
        }

        return null;
    }

    /**
     * Get current FormatStreamData.
     * Rest of the implementation added by patch.
     */
    private static String getFormatStreamData() {
        return "";
    }

    /**
     * Set current FormatStreamData.
     * Rest of the implementation added by patch.
     */
    private static void setFormatStreamData(String formatStreamData) {
        // These instructions are ignored by patch.
        Logger.printDebug(() -> "Original FormatStreamData: " + formatStreamData);
    }
}
