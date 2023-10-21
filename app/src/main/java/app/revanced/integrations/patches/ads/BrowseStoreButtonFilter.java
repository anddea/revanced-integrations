package app.revanced.integrations.patches.ads;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;

public final class BrowseStoreButtonFilter extends Filter {
    private static final String BROWSE_BUTTON_PHONE_PATH = "|ContainerType|button.eml|";
    private static final String BROWSE_BUTTON_TABLET_PATH = "|ContainerType|ContainerType|ContainerType|ContainerType|ContainerType|button.eml|";
    private static final String JOIN_BUTTON_PATH = "|ContainerType|ContainerType|ContainerType|button.eml|";
    private final StringFilterGroup browseButtonPhone;
    private final StringFilterGroup browseButtonTablet;

    public BrowseStoreButtonFilter() {
        browseButtonPhone = new StringFilterGroup(
                SettingsEnum.HIDE_BROWSE_STORE_BUTTON,
                "channel_profile_phone.eml",
                "channel_action_buttons_phone.eml"
        );

        browseButtonTablet = new StringFilterGroup(
                SettingsEnum.HIDE_BROWSE_STORE_BUTTON,
                "channel_profile_tablet.eml"
        );

        pathFilterGroupList.addAll(
                browseButtonPhone,
                browseButtonTablet
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

        return false;
    }
}
