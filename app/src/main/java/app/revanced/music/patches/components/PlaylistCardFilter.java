package app.revanced.music.patches.components;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;


public final class PlaylistCardFilter extends Filter {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PlaylistCardFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_PLAYLIST_CARD,
                        "music_container_card_shelf"
                )
        );
    }
}
