package app.revanced.integrations.patches.layout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import app.revanced.integrations.settings.SettingsEnum;

public class FlyoutPanelLayoutPatch {

    public static void enableOldQualityMenu(ListView listView) {
        if (!SettingsEnum.ENABLE_OLD_QUALITY_LAYOUT.getBoolean()) return;

        listView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                parent.setVisibility(View.GONE);

                final var indexOfAdvancedQualityMenuItem = 4;
                if (listView.indexOfChild(child) != indexOfAdvancedQualityMenuItem) return;

                final var qualityItemMenuPosition = 4;
                listView.performItemClick(null, qualityItemMenuPosition, 0);
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {}
        });
    }
}
