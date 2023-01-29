package app.revanced.integrations.returnyoutubedislike.requests;

import static app.revanced.integrations.returnyoutubedislike.requests.ReturnYouTubeDislikeRoutes.getRYDConnectionFromRoute;
import static app.revanced.integrations.utils.StringRef.str;

import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

import app.revanced.integrations.requests.Requester;
import app.revanced.integrations.returnyoutubedislike.ReturnYouTubeDislike;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;

public class ReturnYouTubeDislikeApi {
    /**
     * {@link #fetchVotes(String)} TCP connection timeout
     */
    private static final int API_GET_VOTES_TCP_TIMEOUT_MILLISECONDS = 2000;

    /**
     * {@link #fetchVotes(String)} HTTP read timeout
     *  To locally debug and force timeouts, change this to a very small number (ie: 100)
     */
    private static final int API_GET_VOTES_HTTP_TIMEOUT_MILLISECONDS = 4000;

    /**
     * Default connection and response timeout for voting and registration.
     *
     * Voting and user registration runs in the background and has has no urgency
     * so this can be a larger value.
     */
    private static final int API_REGISTER_VOTE_TIMEOUT_MILLISECONDS = 90000;

    /**
     * Response code of a successful API call
     */
    private static final int SUCCESS_HTTP_STATUS_CODE = 200;

    /**
     * Indicates a client rate limit has been reached
     */
    private static final int RATE_LIMIT_HTTP_STATUS_CODE = 429;

    /**
     * How long to wait until API calls are resumed, if a rate limit is hit.
     * No clear guideline of how long to backoff.  Using 60 seconds for now.
     */
    private static final int RATE_LIMIT_BACKOFF_SECONDS = 60;

    /**
     * Last time a {@link #RATE_LIMIT_HTTP_STATUS_CODE} was reached.
     * zero if has not been reached.
     */
    private static volatile long lastTimeRateLimitWasHit; // must be volatile, since different threads read/write to this

    private ReturnYouTubeDislikeApi() {
    } // utility class

    /**
     * @return True, if api rate limit is in effect.
     */
    private static boolean checkIfRateLimitInEffect() {
        if (lastTimeRateLimitWasHit == 0) {
            return false;
        }
        final long numberOfSecondsSinceLastRateLimit = (System.currentTimeMillis() - lastTimeRateLimitWasHit) / 1000;
        return numberOfSecondsSinceLastRateLimit < RATE_LIMIT_BACKOFF_SECONDS;
    }

    /**
     * @return True, if a client rate limit was requested
     */
    private static boolean checkIfRateLimitWasHit(int httpResponseCode) {
        if (httpResponseCode == RATE_LIMIT_HTTP_STATUS_CODE) {
            lastTimeRateLimitWasHit = System.currentTimeMillis();
            ReVancedUtils.runOnMainThread(() -> { // must show toasts on main thread
                Toast.makeText(ReVancedUtils.getContext(), str("revanced_ryd_failure_client_rate_limit_requested"), Toast.LENGTH_LONG).show();
            });
            return true;
        }
        return false;
    }

    private static void updateStatistics(boolean connectionError, boolean rateLimitHit) {
        if (connectionError && rateLimitHit)
            throw new IllegalArgumentException("both connection error and rate limit parameter were true");
    }

    /**
     * @return NULL if fetch failed, or if a rate limit is in effect.
     */
    @Nullable
    public static RYDVoteData fetchVotes(String videoId) {
        ReVancedUtils.verifyOffMainThread();
        Objects.requireNonNull(videoId);

        if (checkIfRateLimitInEffect()) return null;

        try {
            HttpURLConnection connection = getRYDConnectionFromRoute(ReturnYouTubeDislikeRoutes.GET_DISLIKES, videoId);
            // request headers, as per https://returnyoutubedislike.com/docs/fetching
            // the documentation says to use 'Accept:text/html', but the RYD browser plugin uses 'Accept:application/json'
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Connection", "keep-alive"); // keep-alive is on by default with http 1.1, but specify anyways
            connection.setRequestProperty("Pragma", "no-cache");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setUseCaches(false);
            connection.setConnectTimeout(API_GET_VOTES_TCP_TIMEOUT_MILLISECONDS); // timeout for TCP connection to server
            connection.setReadTimeout(API_GET_VOTES_HTTP_TIMEOUT_MILLISECONDS); // timeout for server response

            final int responseCode = connection.getResponseCode();
            if (checkIfRateLimitWasHit(responseCode)) {
                connection.disconnect(); // rate limit hit, should disconnect
                updateStatistics(false, true);
                return null;
            }

            if (responseCode == SUCCESS_HTTP_STATUS_CODE) {
                // do not disconnect, the same server connection will likely be used again soon
                JSONObject json = Requester.parseJSONObject(connection);
                try {
                    return new RYDVoteData(json);
                } catch (JSONException ex) {
                    LogHelper.printException(ReturnYouTubeDislikeApi.class, "Failed to parse video: " + videoId + " json: " + json, ex);
                    // fall thru to update statistics
                }
            } else {
                connection.disconnect(); // something went wrong, might as well disconnect
            }
        } catch (Exception ex) { // connection timed out, response timeout, or some other network error
            LogHelper.printException(ReturnYouTubeDislikeApi.class, "Failed to fetch votes", ex);
        }

        updateStatistics(true, false);
        return null;
    }

    /**
     * @return The newly created and registered user id.  Returns NULL if registration failed.
     */
    @Nullable
    public static String registerAsNewUser() {
        ReVancedUtils.verifyOffMainThread();
        try {
            if (checkIfRateLimitInEffect()) {
                return null;
            }
            String userId = randomString();

            HttpURLConnection connection = getRYDConnectionFromRoute(ReturnYouTubeDislikeRoutes.GET_REGISTRATION, userId);
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(API_REGISTER_VOTE_TIMEOUT_MILLISECONDS);
            connection.setReadTimeout(API_REGISTER_VOTE_TIMEOUT_MILLISECONDS);

            final int responseCode = connection.getResponseCode();
            if (checkIfRateLimitWasHit(responseCode)) {
                connection.disconnect(); // disconnect, as no more connections will be made for a little while
                return null;
            }
            if (responseCode == SUCCESS_HTTP_STATUS_CODE) {
                JSONObject json = Requester.parseJSONObject(connection);
                String challenge = json.getString("challenge");
                int difficulty = json.getInt("difficulty");

                String solution = solvePuzzle(challenge, difficulty);
                return confirmRegistration(userId, solution);
            }
            connection.disconnect();
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikeApi.class, "Failed to register user", ex);
        }
        return null;
    }

    @Nullable
    private static String confirmRegistration(String userId, String solution) {
        ReVancedUtils.verifyOffMainThread();
        Objects.requireNonNull(userId);
        Objects.requireNonNull(solution);
        try {
            if (checkIfRateLimitInEffect()) return null;

            HttpURLConnection connection = getRYDConnectionFromRoute(ReturnYouTubeDislikeRoutes.CONFIRM_REGISTRATION, userId);
            applyCommonPostRequestSettings(connection);

            String jsonInputString = "{\"solution\": \"" + solution + "\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            final int responseCode = connection.getResponseCode();
            if (checkIfRateLimitWasHit(responseCode)) {
                connection.disconnect(); // disconnect, as no more connections will be made for a little while
                return null;
            }
            if (responseCode == SUCCESS_HTTP_STATUS_CODE) {
                String result = Requester.parseJson(connection);
                if (result.equalsIgnoreCase("true")) return userId;
            }
            connection.disconnect(); // something went wrong, might as well disconnect
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikeApi.class, "Failed to confirm registration for user: " + userId
                    + "solution: " + solution, ex);
        }
        return null;
    }

    public static void sendVote(String videoId, String userId, ReturnYouTubeDislike.Vote vote) {
        ReVancedUtils.verifyOffMainThread();
        Objects.requireNonNull(videoId);
        Objects.requireNonNull(userId);
        Objects.requireNonNull(vote);

        try {
            if (checkIfRateLimitInEffect()) {
                return;
            }

            HttpURLConnection connection = getRYDConnectionFromRoute(ReturnYouTubeDislikeRoutes.SEND_VOTE);
            applyCommonPostRequestSettings(connection);

            String voteJsonString = "{\"userId\": \"" + userId + "\", \"videoId\": \"" + videoId + "\", \"value\": \"" + vote.value + "\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = voteJsonString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            final int responseCode = connection.getResponseCode();
            if (checkIfRateLimitWasHit(responseCode)) {
                connection.disconnect(); // disconnect, as no more connections will be made for a little while
                return;
            }
            if (responseCode == SUCCESS_HTTP_STATUS_CODE) {
                JSONObject json = Requester.parseJSONObject(connection);
                String challenge = json.getString("challenge");
                int difficulty = json.getInt("difficulty");

                String solution = solvePuzzle(challenge, difficulty);
                confirmVote(videoId, userId, solution);
                return;
            }
            connection.disconnect(); // something went wrong, might as well disconnect
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikeApi.class, "Failed to send vote for video: " + videoId
                    + " user: " + userId + " vote: " + vote, ex);
        }
    }

    private static void confirmVote(String videoId, String userId, String solution) {
        ReVancedUtils.verifyOffMainThread();
        Objects.requireNonNull(videoId);
        Objects.requireNonNull(userId);
        Objects.requireNonNull(solution);

        try {
            if (checkIfRateLimitInEffect()) {
                return;
            }
            HttpURLConnection connection = getRYDConnectionFromRoute(ReturnYouTubeDislikeRoutes.CONFIRM_VOTE);
            applyCommonPostRequestSettings(connection);

            String jsonInputString = "{\"userId\": \"" + userId + "\", \"videoId\": \"" + videoId + "\", \"solution\": \"" + solution + "\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            final int responseCode = connection.getResponseCode();
            if (checkIfRateLimitWasHit(responseCode)) {
                connection.disconnect(); // disconnect, as no more connections will be made for a little while
                return;
            }

            if (responseCode == SUCCESS_HTTP_STATUS_CODE) {
                String result = Requester.parseJson(connection);
                if (result.equalsIgnoreCase("true")) return;
            }
            connection.disconnect(); // something went wrong, might as well disconnect
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikeApi.class, "Failed to confirm vote for video: " + videoId
                    + " user: " + userId + " solution: " + solution, ex);
        }
    }

    private static void applyCommonPostRequestSettings(HttpURLConnection connection) throws ProtocolException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Pragma", "no-cache");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setConnectTimeout(API_REGISTER_VOTE_TIMEOUT_MILLISECONDS); // timeout for TCP connection to server
        connection.setReadTimeout(API_REGISTER_VOTE_TIMEOUT_MILLISECONDS); // timeout for server response
    }


    private static String solvePuzzle(String challenge, int difficulty) {
        byte[] decodedChallenge = Base64.decode(challenge, Base64.NO_WRAP);

        byte[] buffer = new byte[20];
        System.arraycopy(decodedChallenge, 0, buffer, 4, 16);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex); // should never happen
        }

        final int maxCount = (int) (Math.pow(2, difficulty + 1) * 5);
        for (int i = 0; i < maxCount; i++) {
            buffer[0] = (byte) i;
            buffer[1] = (byte) (i >> 8);
            buffer[2] = (byte) (i >> 16);
            buffer[3] = (byte) (i >> 24);
            byte[] messageDigest = md.digest(buffer);

            if (countLeadingZeroes(messageDigest) >= difficulty) {
                return Base64.encodeToString(new byte[]{buffer[0], buffer[1], buffer[2], buffer[3]}, Base64.NO_WRAP);
            }
        }

        // should never be reached
        throw new IllegalStateException("Failed to solve puzzle challenge: " + challenge + " of difficulty: " + difficulty);
    }

    // https://stackoverflow.com/a/157202
    private static String randomString() {
        String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(36);
        for (int i = 0; i < 36; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    private static int countLeadingZeroes(byte[] uInt8View) {
        int zeroes = 0;
        int value = 0;
        for (byte b : uInt8View) {
            value = b & 0xFF;
            if (value == 0) {
                zeroes += 8;
            } else {
                int count = 1;
                if (value >>> 4 == 0) {
                    count += 4;
                    value <<= 4;
                }
                if (value >>> 6 == 0) {
                    count += 2;
                    value <<= 2;
                }
                zeroes += count - (value >>> 7);
                break;
            }
        }
        return zeroes;
    }
}
