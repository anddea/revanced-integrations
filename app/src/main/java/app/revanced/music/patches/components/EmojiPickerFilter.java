package app.revanced.music.patches.components;

import app.revanced.music.settings.SettingsEnum;

public final class EmojiPickerFilter extends Filter {

    public EmojiPickerFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_EMOJI_PICKER,
                        "|CellType|ContainerType|ContainerType|ContainerType|ContainerType|ContainerType|"
                )
        );
    }
}
