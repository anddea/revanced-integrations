package app.revanced.music.patches.misc;

import app.revanced.music.settings.MusicSettingsEnum;

public class OpusCodecPatch {

    public static boolean enableOpusCodec() {
        return MusicSettingsEnum.ENABLE_OPUS_CODEC.getBoolean();
    }
}
