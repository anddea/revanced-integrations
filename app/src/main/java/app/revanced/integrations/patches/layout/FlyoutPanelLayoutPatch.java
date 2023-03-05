package app.revanced.integrations.patches.layout;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ListView;

import app.revanced.integrations.settings.SettingsEnum;

public class FlyoutPanelLayoutPatch {

    public static void enableOldQualityMenu(ListView listView) {
        if (!SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean()) return;
        listView.setVisibility(View.GONE);

        new Handler(Looper.getMainLooper()).postDelayed(() ->
                        listView.performItemClick(null, 4, 0),
                1
        );
    }

    public static int enableOldQualityLayout(int original) {
        return SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean() ? 3 : original;
    }
}
