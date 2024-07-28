package app.revanced.integrations.youtube.patches.misc;

import android.view.View;
import app.revanced.integrations.shared.utils.Logger;

@SuppressWarnings("unused")
public class DownloadPlaylistButton {

    /*
     * Injection point
     */
    public static int enablePlaylistDownloadButton(int i) {
        Logger.printInfo(() ->
                "[DownloadPlaylistButton] Enabling playlist download button, " +
                        "before: " + i
        );
        return 2;
    }

    /*
     * Injection point
     */
    public static void setPlaylistDownloadButtonVisibility(View offlineArrowView) {
        Logger.printInfo(() ->
                "[DownloadPlaylistButton] Setting playlist download button visibility, " +
                        "before: " + (offlineArrowView == null ? "null" : offlineArrowView.toString())
        );
        offlineArrowView.setVisibility(View.VISIBLE);
    }
}
