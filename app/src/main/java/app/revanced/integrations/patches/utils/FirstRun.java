package app.revanced.integrations.patches.utils;

import app.revanced.integrations.sponsorblock.PlayerController;
import app.revanced.integrations.settings.SettingsEnum;

public class FirstRun {

    public static void initializationSB() {
        if (SettingsEnum.SB_FIRSTRUN.getBoolean()) return;

        // set dummy video-id to initialize Sponsorblock: TeamVanced's Sponsorblock tutorial (https://www.youtube.com/watch?v=sE2IzSn-hHU)
        PlayerController.setCurrentVideoId("sE2IzSn-hHU");
        SettingsEnum.SB_FIRSTRUN.saveValue(true);
    }

}