package app.revanced.integrations.music.patches.components;

import app.revanced.integrations.music.settings.SettingsEnum;

@SuppressWarnings("unused")
public final class ChannelGuidelinesFilter extends Filter {

    public ChannelGuidelinesFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_CHANNEL_GUIDELINES,
                        "community_guidelines"
                )
        );
    }
}
