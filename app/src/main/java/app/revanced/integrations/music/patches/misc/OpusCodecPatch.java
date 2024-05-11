package app.revanced.integrations.music.patches.misc;

import app.revanced.integrations.music.settings.Settings;

@SuppressWarnings("unused")
public class OpusCodecPatch {

    public static boolean enableOpusCodec() {
        return Settings.ENABLE_OPUS_CODEC.get();
    }
}
