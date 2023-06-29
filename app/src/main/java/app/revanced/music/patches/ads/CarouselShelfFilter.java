package app.revanced.music.patches.ads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.ReVancedUtils;


public final class CarouselShelfFilter extends Filter {
    private final String[] exceptions;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CarouselShelfFilter() {
        exceptions = new String[]{
                "menu",
                "root",
                "-count",
                "-space",
                "-button"
        };

        final var carouselShelf = new StringFilterGroup(
                SettingsEnum.HIDE_CAROUSEL_SHELF,
                "music_grid_item_carousel"
        );

        this.pathFilterGroups.addAll(
                carouselShelf
        );
    }

    @Override
    public boolean isFiltered(final String path, final String identifier) {
        if (ReVancedUtils.containsAny(path, exceptions))
            return false;

        return super.isFiltered(path, identifier);
    }
}
