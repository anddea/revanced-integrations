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

    public static CharSequence hideFeedFlyoutPanel(CharSequence charSequence) {
        if (charSequence == null || !SettingsEnum.HIDE_FEED_FLYOUT_PANEL.getBoolean()) return charSequence;

        String[] blockList = SettingsEnum.HIDE_FEED_FLYOUT_PANEL_CUSTOM_FILTER.getString().split("\\n");

        for (String filter : blockList) {
            if (charSequence.toString().contains(filter) && !filter.isEmpty())
                return null;
        }

        return charSequence;
    }
}
