package app.revanced.integrations.youtube.patches.misc;

import app.revanced.integrations.shared.utils.Logger;

@SuppressWarnings("unused")
public class DownloadPlaylistButton {
    /*
     * Injection point
     */
    public static boolean isPlaylistDownloadButtonHooked() {
        return true;
    }

    /*
     * Injection point
     */
    public static String startPlaylistDownloadActivity(String playlistId) {
        // Hook the playlist download button
        Logger.printInfo(() -> "[DownloadPlaylistButton] String: " + playlistId);

        if (!isPlaylistDownloadButtonHooked()) return playlistId;


        // return an empty string to prevent
        // the original method from being called
        return "";
    }
}
