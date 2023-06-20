package app.revanced.integrations.patches.layout;

import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public class FlyoutPanelPatch {

    public static void enableOldQualityMenu(ListView listView) {
        ReVancedUtils.hideViewUnderCondition(SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean(), listView);
        new Handler(Looper.getMainLooper()).postDelayed(() ->
                        listView.performItemClick(null, 4, 0),
                1
        );
    }
}
