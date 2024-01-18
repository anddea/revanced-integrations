package app.revanced.integrations.music.patches.components;

import app.revanced.integrations.music.settings.SettingsEnum;

@SuppressWarnings("unused")
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
