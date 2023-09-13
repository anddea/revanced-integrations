package app.revanced.music.sponsorblock;

import androidx.annotation.NonNull;

import java.util.UUID;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.sponsorblock.objects.SegmentCategory;

public class SponsorBlockSettings {
    private static boolean initialized;

    /**
     * @return if the user has ever voted, created a segment, or imported existing SB settings.
     */
    public static boolean userHasSBPrivateId() {
        return !SettingsEnum.SB_PRIVATE_USER_ID.getString().isEmpty();
    }

    /**
     * Use this only if a user id is required (creating segments, voting).
     */
    @NonNull
    public static String getSBPrivateUserID() {
        String uuid = SettingsEnum.SB_PRIVATE_USER_ID.getString();
        if (uuid.isEmpty()) {
            uuid = (UUID.randomUUID().toString() +
                    UUID.randomUUID().toString() +
                    UUID.randomUUID().toString())
                    .replace("-", "");
            SettingsEnum.SB_PRIVATE_USER_ID.saveValue(uuid);
        }
        return uuid;
    }

    public static void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;

        SegmentCategory.loadFromPreferences();
    }
}
