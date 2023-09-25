package app.revanced.integrations.patches.misc.requests;

import static app.revanced.integrations.requests.Route.Method.POST;

import java.io.IOException;
import java.net.HttpURLConnection;

import app.revanced.integrations.requests.Requester;
import app.revanced.integrations.requests.Route;

class StoryBoardRendererRoutes {
    static final String YT_API_URL = "https://www.youtube.com/youtubei/v1/";
    static final String YT_API_KEY = "AIzaSyA8eiZmM1FaDVjRy-df2KTyQ_vz_yYM39w";

    static final Route GET_PLAYER_RESPONSE_BODY = new Route(POST, "player?key={api_key}");

    private StoryBoardRendererRoutes() {
    }

    static HttpURLConnection getPlayerResponseConnectionFromRoute() throws IOException {
        return Requester.getConnectionFromRoute(YT_API_URL, GET_PLAYER_RESPONSE_BODY, YT_API_KEY);
    }

}