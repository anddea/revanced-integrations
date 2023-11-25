package app.revanced.music.patches.components;

import app.revanced.music.settings.SettingsEnum;

public final class CarouselShelfFilter extends Filter {

    public CarouselShelfFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_CAROUSEL_SHELF,
                        "music_grid_item_carousel"
                )
        );
    }
}
