package app.revanced.integrations.youtube.patches.misc;

import app.revanced.integrations.youtube.settings.Settings;

@SuppressWarnings("unused")
public class QUICProtocolPatch {

    public static boolean disableQUICProtocol(boolean original) {
        return !Settings.DISABLE_QUIC_PROTOCOL.get() && original;
    }
}
