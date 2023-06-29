package app.revanced.music.patches.ads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;


public final class CustomFilter extends Filter {

    private final CustomFilterGroup custom;

    // endregion

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CustomFilter() {
        custom = new CustomFilterGroup(
                SettingsEnum.CUSTOM_FILTER,
                SettingsEnum.CUSTOM_FILTER_STRINGS
        );
    }

    @Override
    public boolean isFiltered(final String path, final String identifier) {
        if (custom.isEnabled() && custom.check(path).isFiltered())
            return true;

        return super.isFiltered(path, identifier);
    }
}
