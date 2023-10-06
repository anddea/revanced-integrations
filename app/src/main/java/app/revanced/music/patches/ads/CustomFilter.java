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

        this.pathFilterGroupList.addAll(
                custom
        );
    }

    @Override
    public boolean isFiltered(String path, FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (matchedGroup != custom)
            return false;

        return super.isFiltered(path, matchedList, matchedGroup, matchedIndex);
    }
}
