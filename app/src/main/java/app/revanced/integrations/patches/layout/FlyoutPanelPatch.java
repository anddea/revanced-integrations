package app.revanced.integrations.patches.layout;

import android.widget.ListView;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public class FlyoutPanelPatch {

    public static void enableOldQualityMenu(ListView listView) {
        if (!SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean())
            return;

        listView.setVisibility(View.GONE);

        ReVancedUtils.runOnMainThreadDelayed(() ->
                        listView.performItemClick(null, 4, 0),
                1
        );
        SettingsEnum.NEW_PLAYER_FLYOUT_PANEL_DETECTED.saveValue(false);
    }

    public static CharSequence hideFeedFlyoutPanel(CharSequence charSequence) {
        if (charSequence == null || !SettingsEnum.HIDE_FEED_FLYOUT_PANEL.getBoolean())
            return charSequence;

        String[] blockList = SettingsEnum.HIDE_FEED_FLYOUT_PANEL_FILTER_STRINGS.getString().split("\\n");

        for (String filter : blockList) {
            if (charSequence.toString().contains(filter) && !filter.isEmpty())
                return null;
        }

        return charSequence;
    }
}
