package app.revanced.integrations.patches.layout;

import android.view.View;
import android.widget.ListView;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public class FlyoutPanelLayoutPatch {

    public static void enableOldQualityMenu(ListView listView) {
        if (!SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean()) return;

        listView.setVisibility(View.GONE);
        ReVancedUtils.runDelayed(() -> listView.performItemClick(null, 4, 0), 1L);
    }
}
