package app.revanced.music.patches.misc;

import app.revanced.music.settings.SettingsEnum;

public class OpusCodecPatch {

    public static boolean enableOpusCodec() {
        return SettingsEnum.ENABLE_OPUS_CODEC.getBoolean();
    }
}
