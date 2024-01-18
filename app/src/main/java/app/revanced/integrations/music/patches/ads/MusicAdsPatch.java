package app.revanced.integrations.music.patches.ads;

import app.revanced.integrations.music.settings.SettingsEnum;

@SuppressWarnings("unused")
public class MusicAdsPatch {

    public static boolean hideMusicAds() {
        return !SettingsEnum.HIDE_MUSIC_ADS.getBoolean();
    }
}
