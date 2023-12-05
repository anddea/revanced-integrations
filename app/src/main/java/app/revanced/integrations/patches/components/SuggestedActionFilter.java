package app.revanced.integrations.patches.components;

import static app.revanced.integrations.utils.ReVancedUtils.hideViewUnderCondition;

import android.view.View;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.shared.PlayerType;
import app.revanced.integrations.utils.StringTrieSearch;

/** @noinspection rawtypes*/
@SuppressWarnings("unused")
public final class SuggestedActionFilter extends Filter {
    private final StringTrieSearch exceptions = new StringTrieSearch();

    public SuggestedActionFilter() {
        exceptions.addPatterns(
                "channel_bar",
                "lock_mode_suggested_action",
                "shorts"
        );

        allValueFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_SUGGESTED_ACTION,
                        "suggested_action"
                )
        );
    }

    public static void hideSuggestedActions(View view) {
        hideViewUnderCondition(SettingsEnum.HIDE_SUGGESTED_ACTION.getBoolean(), view);
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (exceptions.matches(path) || PlayerType.getCurrent().isNoneOrHidden())
            return false;

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
