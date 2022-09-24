package app.revanced.integrations.patches;

import android.view.View;
import android.widget.ListView;
import android.os.Looper;
import android.os.Handler;

import app.revanced.integrations.settings.SettingsEnum;

public class OldQualityLayoutPatch {
    public static void showOldQualityMenu(ListView listView) {
        if (!SettingsEnum.OLD_STYLE_QUALITY_SETTINGS.getBoolean()) return;

        listView.setVisibility(View.GONE);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.performItemClick(null, 4, 0);
            }
        }, 1);
    }
}
