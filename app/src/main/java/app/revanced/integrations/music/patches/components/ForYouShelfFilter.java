package app.revanced.integrations.music.patches.components;

import app.revanced.integrations.music.settings.SettingsEnum;

@SuppressWarnings("unused")
public final class ForYouShelfFilter extends Filter {

    public ForYouShelfFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_FOR_YOU_SHELF,
                        "immersive_card_shelf"
                )
        );
    }
}
