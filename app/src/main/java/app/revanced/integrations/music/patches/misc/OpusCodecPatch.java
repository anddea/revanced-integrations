package app.revanced.integrations.music.patches.misc;

import app.revanced.integrations.music.settings.SettingsEnum;

@SuppressWarnings("unused")
public class OpusCodecPatch {

    public static boolean enableOpusCodec() {
        return SettingsEnum.ENABLE_OPUS_CODEC.getBoolean();
    }
}
