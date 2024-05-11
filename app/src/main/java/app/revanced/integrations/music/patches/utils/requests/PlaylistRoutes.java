package app.revanced.integrations.music.patches.utils.requests;

import static app.revanced.integrations.shared.requests.Route.Method.GET;

import java.io.IOException;
import java.net.HttpURLConnection;

import app.revanced.integrations.shared.requests.Requester;
import app.revanced.integrations.shared.requests.Route;

class PlaylistRoutes {
    static final String PIPED_URL = "https://pipedapi.kavin.rocks/";
    static final Route GET_PLAYLIST = new Route(GET, "playlists/{playlist_id}");

    private PlaylistRoutes() {
    }

    static HttpURLConnection getPlaylistConnectionFromRoute(Route route, String... params) throws IOException {
        return Requester.getConnectionFromRoute(PIPED_URL, route, params);
    }

}