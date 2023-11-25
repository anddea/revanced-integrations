package app.revanced.music.patches.components;

import app.revanced.music.settings.SettingsEnum;

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
