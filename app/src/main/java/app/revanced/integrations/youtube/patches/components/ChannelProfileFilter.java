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
    private static final String BROWSE_STORE_BUTTON_PHONE_PATH = "|ContainerType|button.eml|";
    private static final String BROWSE_STORE_BUTTON_TABLET_PATH = "|ContainerType|ContainerType|ContainerType|ContainerType|button.eml|";
    private static final String JOIN_BUTTON_PATH = "|ContainerType|ContainerType|ContainerType|button.eml|";
    @SuppressLint("StaticFieldLeak")
    public static View channelTabView;
    /**
     * Last time the method was used
     */
    private static long lastTimeUsed = 0;
    private final StringFilterGroup browseStoreButtonPhone;
    private final StringFilterGroup browseStoreButtonTablet;

    public ChannelProfileFilter() {
        browseStoreButtonPhone = new StringFilterGroup(
                null,
                "channel_profile_phone.eml",
                "channel_action_buttons_phone.eml"
        );

        browseStoreButtonTablet = new StringFilterGroup(
                null,
                "channel_profile_tablet.eml"
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
                browseStoreButtonPhone,
                browseStoreButtonTablet,
                channelMemberShelf,
                channelProfileLinks,
                forYouShelf
        );
    }

    private static void hideStoreTab(boolean isBrowseStoreButtonShown) {
        if (!isBrowseStoreButtonShown || !SettingsEnum.HIDE_STORE_TAB.getBoolean())
            return;

        final long currentTime = System.currentTimeMillis();

        // Ignores method reuse in less than 1 second.
        if (lastTimeUsed != 0 && currentTime - lastTimeUsed < 1000)
            return;
        lastTimeUsed = currentTime;

        // This method is called before the channeltabView is created.
        // Add a delay to hide after the channeltabView is created.
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
        boolean isBrowseStoreButtonShown;
        if (matchedGroup == browseStoreButtonPhone) {
            isBrowseStoreButtonShown = path.contains(BROWSE_STORE_BUTTON_PHONE_PATH) && !path.contains(JOIN_BUTTON_PATH);
            hideStoreTab(isBrowseStoreButtonShown);
            return isBrowseStoreButtonShown && SettingsEnum.HIDE_BROWSE_STORE_BUTTON.getBoolean();
        } else if (matchedGroup == browseStoreButtonTablet) {
            isBrowseStoreButtonShown = path.contains(BROWSE_STORE_BUTTON_TABLET_PATH);
            hideStoreTab(isBrowseStoreButtonShown);
            return isBrowseStoreButtonShown && SettingsEnum.HIDE_BROWSE_STORE_BUTTON.getBoolean();
        }

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
