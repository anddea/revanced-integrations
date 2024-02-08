package app.revanced.integrations.youtube.patches.components;

import app.revanced.integrations.youtube.settings.SettingsEnum;

@SuppressWarnings("unused")
final class ChannelListSubMenuFilter extends Filter {

    public ChannelListSubMenuFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_CHANNEL_LIST_SUBMENU,
                        "subscriptions_channel_bar.eml"
                )
        );
    }
}
