package app.revanced.integrations.returnyoutubedislike.requests;

import static app.revanced.integrations.requests.Route.Method.GET;
import static app.revanced.integrations.requests.Route.Method.POST;

import java.io.IOException;
import java.net.HttpURLConnection;

import app.revanced.integrations.requests.Requester;
import app.revanced.integrations.requests.Route;

class ReturnYouTubeDislikeRoutes {
    static final String RYD_API_URL = "https://returnyoutubedislikeapi.com/";
    static final String RYD_API_MIRROR_URL = "https://true-ryd.cane.workers.dev/";

    static final Route SEND_VOTE = new Route(POST, "interact/vote");
    static final Route CONFIRM_VOTE = new Route(POST, "interact/confirmVote");
    static final Route GET_DISLIKES = new Route(GET, "votes?videoId={video_id}");
    static final Route GET_REGISTRATION = new Route(GET, "puzzle/registration?userId={user_id}");
    static final Route CONFIRM_REGISTRATION = new Route(POST, "puzzle/registration?userId={user_id}");
    static final Route GET_DISLIKES_MIRROR = new Route(GET, "?videoId={video_id}");

    private ReturnYouTubeDislikeRoutes() {
    }

    static HttpURLConnection getRYDConnectionFromRoute(Route route, String... params) throws IOException {
        return Requester.getConnectionFromRoute(RYD_API_URL, route, params);
    }

    static HttpURLConnection getRYDMIRRORConnectionFromRoute(Route route, String... params) throws IOException {
        return Requester.getConnectionFromRoute(RYD_API_MIRROR_URL, route, params);
    }

}