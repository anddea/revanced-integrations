package app.revanced.music.patches.ads;


import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.ReVancedUtils;


public final class PlaylistCardFilter extends Filter {
    private final String[] exceptions;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PlaylistCardFilter() {
        exceptions = new String[]{
                "menu",
                "root",
                "-count",
                "-space",
                "-button"
        };

        final var playlistCard = new StringFilterGroup(
                SettingsEnum.HIDE_PLAYLIST_CARD,
                "music_container_card_shelf"
        );

        this.pathFilterGroups.addAll(
                playlistCard
        );
    }

    @Override
    public boolean isFiltered(final String path, final String identifier) {
        if (ReVancedUtils.containsAny(path, exceptions))
           return false;

        return super.isFiltered(path, identifier);
    }
}
