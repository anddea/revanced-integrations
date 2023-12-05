package app.revanced.music.patches.components;

import app.revanced.music.settings.SettingsEnum;

@SuppressWarnings("unused")
public final class PlaylistCardFilter extends Filter {

    public PlaylistCardFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_PLAYLIST_CARD,
                        "music_container_card_shelf"
                )
        );
    }
}
