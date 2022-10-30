package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;

public class TabletLayoutOverridePatch {

    public static int getTabletLayoutOverride(int original) {
        if (SettingsEnum.TABLET_LAYOUT.getBoolean()) return 720;
        return original;
    }

    public static void hideShelfHeader(View view) {
        if (SettingsEnum.TABLET_LAYOUT.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
        }
    }

}
