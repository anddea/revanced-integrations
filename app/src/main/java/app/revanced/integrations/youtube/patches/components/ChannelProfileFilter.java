package app.revanced.integrations.youtube.patches.components;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.Nullable;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.utils.ReVancedUtils;

/**
 * @noinspection rawtypes
 */
@SuppressWarnings("unused")
public final class ChannelProfileFilter extends Filter {
    private static final String BROWSE_STORE_BUTTON_PATH = "|ContainerType|button.eml|";
    @SuppressLint("StaticFieldLeak")
    public static View channelTabView;
    /**
     * Last time the method was used
     */
    private static long lastTimeUsed = 0;
    private final StringFilterGroup channelProfileButtonRule;
    private static final ByteArrayAsStringFilterGroup browseStoreButton =
            new ByteArrayAsStringFilterGroup(
                    null,
                    "header_store_button"
            );

    public ChannelProfileFilter() {
        channelProfileButtonRule = new StringFilterGroup(
                null,
                "|channel_profile_"
        );

        final StringFilterGroup channelMemberShelf = new StringFilterGroup(
                SettingsEnum.HIDE_CHANNEL_MEMBER_SHELF,
                "member_recognition_shelf"
        );

        final StringFilterGroup channelProfileLinks = new StringFilterGroup(
                SettingsEnum.HIDE_CHANNEL_PROFILE_LINKS,
                "channel_header_links"
        );

        final StringFilterGroup forYouShelf = new StringFilterGroup(
                SettingsEnum.HIDE_FOR_YOU_SHELF,
                "mixed_content_shelf"
        );

        pathFilterGroupList.addAll(
                channelProfileButtonRule,
                channelMemberShelf,
                channelProfileLinks,
                forYouShelf
        );
    }

    private static void hideStoreTab(boolean isBrowseStoreButtonShown) {
        if (!isBrowseStoreButtonShown || !SettingsEnum.HIDE_STORE_TAB.getBoolean())
            return;

        final long currentTime = System.currentTimeMillis();

        // Ignores method reuse in less than 3 second.
        if (lastTimeUsed != 0 && currentTime - lastTimeUsed < 3000)
            return;
        lastTimeUsed = currentTime;

        // This method is called before the channel tab is created.
        // Add a delay to hide after the channel tab is created.
        ReVancedUtils.runOnMainThreadDelayed(() -> {
                    if (channelTabView != null) {
                        channelTabView.setVisibility(View.GONE);
                    }
                },
                0
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (matchedGroup == channelProfileButtonRule) {
            final boolean isBrowseStoreButtonShown = path.contains(BROWSE_STORE_BUTTON_PATH) && browseStoreButton.check(protobufBufferArray).isFiltered();
            hideStoreTab(isBrowseStoreButtonShown);
            if (!isBrowseStoreButtonShown || !SettingsEnum.HIDE_BROWSE_STORE_BUTTON.getBoolean()) {
                return false;
            }
        }

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
