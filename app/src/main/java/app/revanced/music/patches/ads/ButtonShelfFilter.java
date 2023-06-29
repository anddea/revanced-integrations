package app.revanced.music.patches.ads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.ReVancedUtils;


public final class ButtonShelfFilter extends Filter {
    private final String[] exceptions;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ButtonShelfFilter() {
        exceptions = new String[]{
                "menu",
                "root",
                "-count",
                "-space",
                "-button"
        };

        final var buttonShelf = new StringFilterGroup(
                SettingsEnum.HIDE_BUTTON_SHELF,
                "entry_point_button_shelf"
        );

        this.pathFilterGroups.addAll(
                buttonShelf
        );
    }

    @Override
    public boolean isFiltered(final String path, final String identifier) {
        if (ReVancedUtils.containsAny(path, exceptions))
            return false;

        return super.isFiltered(path, identifier);
    }
}
