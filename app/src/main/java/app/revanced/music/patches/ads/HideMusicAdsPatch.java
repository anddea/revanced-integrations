package app.revanced.music.patches.ads;

import app.revanced.music.settings.MusicSettingsEnum;

public class HideMusicAdsPatch {

    public static boolean hideMusicAds() {
        return !MusicSettingsEnum.HIDE_MUSIC_ADS.getBoolean();
    }
}
