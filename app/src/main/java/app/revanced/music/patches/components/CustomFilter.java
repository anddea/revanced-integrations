package app.revanced.music.patches.components;

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

        pathFilterGroupList.addAll(custom);
    }

    @Override
    public boolean isFiltered(String path, FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (matchedGroup != custom)
            return false;

        return super.isFiltered(path, matchedList, matchedGroup, matchedIndex);
    }
}
