package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.utils.ReVancedHelper.setBuildVersion;

import android.content.Context;

import androidx.annotation.NonNull;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.sponsorblock.SponsorBlockSettings;

public class FirstRun {

    public static void initializationSB(@NonNull Context context) {
        if (SettingsEnum.SB_FIRSTRUN.getBoolean()) return;

        SponsorBlockSettings.update(context);
        SettingsEnum.SB_FIRSTRUN.saveValue(true);
    }

    public static void initializationRVX(@NonNull Context context) {
        setBuildVersion(context);
    }

}