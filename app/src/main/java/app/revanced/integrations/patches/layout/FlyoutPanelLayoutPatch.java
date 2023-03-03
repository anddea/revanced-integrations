package app.revanced.integrations.patches.layout;

import static app.revanced.integrations.utils.ReVancedUtils.runOnMainThread;

import android.view.View;
import android.widget.ListView;

import app.revanced.integrations.settings.SettingsEnum;

public class FlyoutPanelLayoutPatch {

    public static void enableOldQualityMenu(ListView listView) {
        if (!SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean()) return;

        listView.setVisibility(View.GONE);
        runOnMainThread(() -> listView.performItemClick(null, 4, 0));
    }

    public static int enableOldQualityLayout(int original) {
        return SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean() ? 3 : original;
    }
}
