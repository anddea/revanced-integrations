package app.revanced.music.patches.ads;

import app.revanced.music.settings.SettingsEnum;

@SuppressWarnings("unused")
public class MusicAdsPatch {

    public static boolean hideMusicAds() {
        return !SettingsEnum.HIDE_MUSIC_ADS.getBoolean();
    }
}
