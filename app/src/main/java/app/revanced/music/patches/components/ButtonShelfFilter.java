package app.revanced.music.patches.components;

import app.revanced.music.settings.SettingsEnum;

@SuppressWarnings("unused")
public final class ButtonShelfFilter extends Filter {

    public ButtonShelfFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_BUTTON_SHELF,
                        "entry_point_button_shelf"
                )
        );
    }
}
