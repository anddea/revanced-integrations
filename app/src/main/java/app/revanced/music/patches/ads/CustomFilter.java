package app.revanced.music.patches.ads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.StringTrieSearch;


public final class CustomFilter extends Filter {

    private final CustomFilterGroup custom;
    private final StringTrieSearch exceptions = new StringTrieSearch();

    // endregion

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CustomFilter() {
        exceptions.addPatterns(
                "menu",
                "root",
                "-count",
                "-space",
                "-button"
        );

        custom = new CustomFilterGroup(
                SettingsEnum.CUSTOM_FILTER,
                SettingsEnum.CUSTOM_FILTER_STRINGS
        );

        this.pathFilterGroups.addAll(
                custom
        );
    }

    @Override
    public boolean isFiltered(String path, FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (matchedGroup != custom && exceptions.matches(path))
            return false;

        return super.isFiltered(path, matchedList, matchedGroup, matchedIndex);
    }
}
