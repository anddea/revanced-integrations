package app.revanced.music.patches.ads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.StringTrieSearch;


public final class CarouselShelfFilter extends Filter {
    private final StringTrieSearch exceptions = new StringTrieSearch();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CarouselShelfFilter() {
        exceptions.addPatterns(
                "comment_thread",
                "menu",
                "root",
                "-count",
                "-space",
                "-button"
        );

        final var carouselShelf = new StringFilterGroup(
                SettingsEnum.HIDE_CAROUSEL_SHELF,
                "music_grid_item_carousel"
        );

        this.pathFilterGroups.addAll(
                carouselShelf
        );
    }

    @Override
    public boolean isFiltered(String path, FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (exceptions.matches(path))
            return false;

        return super.isFiltered(path, matchedList, matchedGroup, matchedIndex);
    }
}
