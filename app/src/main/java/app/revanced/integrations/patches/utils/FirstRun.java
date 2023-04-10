package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.sponsorblock.SegmentPlaybackController.initialize;
import static app.revanced.integrations.utils.ReVancedHelper.setBuildVersion;

import android.content.Context;

import androidx.annotation.NonNull;

import app.revanced.integrations.settings.SettingsEnum;

public class FirstRun {

    public static void initializationSB(@NonNull Context context) {
        if (SettingsEnum.SB_FIRST_RUN.getBoolean()) return;
        initialize(null);
        SettingsEnum.SB_FIRST_RUN.saveValue(true);
    }

    public static void initializationRVX(@NonNull Context context) {
        setBuildVersion();
    }

}