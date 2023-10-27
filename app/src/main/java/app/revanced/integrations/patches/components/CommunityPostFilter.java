package app.revanced.integrations.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;

public final class CommunityPostFilter extends Filter {

    public CommunityPostFilter() {
        allValueFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_COMMUNITY_POSTS_HOME,
                        "horizontalCollectionSwipeProtector=null"
                ),
                new StringFilterGroup(
                        SettingsEnum.HIDE_COMMUNITY_POSTS_SUBSCRIPTIONS,
                        "heightConstraint=null"
                )
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (!path.startsWith("post_base_wrapper")) {
            return false;
        }
        return allValueFilterGroupList == matchedList;
    }
}
