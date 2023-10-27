package app.revanced.music.patches.components;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;


public final class CarouselShelfFilter extends Filter {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CarouselShelfFilter() {
        this.pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_CAROUSEL_SHELF,
                        "music_grid_item_carousel"
                )
        );
    }
}
