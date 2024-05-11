package app.revanced.integrations.youtube.patches.misc;

import app.revanced.integrations.youtube.settings.Settings;

/**
 * @noinspection ALL
 * Spoof the client name as 'ANDROID_TESTSUITE'.
 * This is currently the only client name available on Android without DroidGuard results.
 * <a href="https://github.com/iv-org/invidious/pull/4650">invidious#4650</a>
 */
public class SpoofTestClientPatch {
    private static final String ANDROID_TESTSUITE_VERSION_NAME = "1.9";
    private static final boolean spoofTestClientEnabled =
            Settings.SPOOF_TEST_CLIENT.get();

    public static boolean spoofTestClient() {
        return spoofTestClientEnabled;
    }

    public static String spoofTestClient(final String original) {
        return spoofTestClientEnabled ? ANDROID_TESTSUITE_VERSION_NAME : original;
    }
}
