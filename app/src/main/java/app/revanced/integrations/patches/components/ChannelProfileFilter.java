package app.revanced.integrations.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;

public final class ChannelProfileFilter extends Filter {
    private static final String BROWSE_BUTTON_PHONE_PATH = "|ContainerType|button.eml|";
    private static final String BROWSE_BUTTON_TABLET_PATH = "|ContainerType|ContainerType|ContainerType|ContainerType|ContainerType|button.eml|";
    private static final String JOIN_BUTTON_PATH = "|ContainerType|ContainerType|ContainerType|button.eml|";
    private final StringFilterGroup browseButtonPhone;
    private final StringFilterGroup browseButtonTablet;

    public ChannelProfileFilter() {
        browseButtonPhone = new StringFilterGroup(
                SettingsEnum.HIDE_BROWSE_STORE_BUTTON,
                "channel_profile_phone.eml",
                "channel_action_buttons_phone.eml"
        );

        browseButtonTablet = new StringFilterGroup(
                SettingsEnum.HIDE_BROWSE_STORE_BUTTON,
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
                browseButtonPhone,
                browseButtonTablet,
                channelMemberShelf,
                channelProfileLinks,
                forYouShelf
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (matchedGroup == browseButtonPhone) {
            return path.contains(BROWSE_BUTTON_PHONE_PATH) && !path.contains(JOIN_BUTTON_PATH);
        } else if (matchedGroup == browseButtonTablet) {
            return path.contains(BROWSE_BUTTON_TABLET_PATH);
        }

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
