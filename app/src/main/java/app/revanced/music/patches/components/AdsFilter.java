package app.revanced.music.patches.components;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;


public final class AdsFilter extends Filter {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public AdsFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_MUSIC_ADS,
                        "statement_banner"
                )
        );
    }
}
