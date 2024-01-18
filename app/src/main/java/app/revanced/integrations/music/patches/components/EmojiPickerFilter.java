package app.revanced.integrations.music.patches.components;

import app.revanced.integrations.music.settings.SettingsEnum;

@SuppressWarnings("unused")
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
