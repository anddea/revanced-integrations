package app.revanced.integrations.youtube.patches.misc;

@SuppressWarnings("unused")
public class DownloadPlaylistButton {
    /*
     * Injection point
     */
    public static boolean setPlaylistDownloadButtonVisibility() {
        return true;
    }
}
