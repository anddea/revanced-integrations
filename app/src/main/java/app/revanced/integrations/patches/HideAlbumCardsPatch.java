package app.revanced.integrations.patches;

import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;

public class HideAlbumCardsPatch {

    public static void hideAlbumCards(View view) {
        if (!SettingsEnum.HIDE_ALBUM_CARDS.getBoolean()) {
            AdRemoverAPI.HideViewWithLayout1dp(view);
        }
    }
}
