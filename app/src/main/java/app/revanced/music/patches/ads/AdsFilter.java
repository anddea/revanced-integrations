package app.revanced.music.patches.ads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.ReVancedUtils;


public final class AdsFilter extends Filter {
    private final String[] exceptions;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public AdsFilter() {
        exceptions = new String[]{
                "menu",
                "root",
                "-count",
                "-space",
                "-button"
        };

        final var generalAds = new StringFilterGroup(
                SettingsEnum.HIDE_MUSIC_ADS,
                "statement_banner"
        );

        this.pathFilterGroups.addAll(
                generalAds
        );
    }

    @Override
    public boolean isFiltered(final String path, final String identifier) {
        if (ReVancedUtils.containsAny(path, exceptions))
           return false;

        return super.isFiltered(path, identifier);
    }
}
