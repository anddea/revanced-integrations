package app.revanced.integrations.music.patches.components;

import app.revanced.integrations.music.settings.Settings;
import app.revanced.integrations.shared.patches.components.Filter;
import app.revanced.integrations.shared.patches.components.StringFilterGroup;

@SuppressWarnings("unused")
public final class PlayerComponentsFilter extends Filter {

    public PlayerComponentsFilter() {
        addIdentifierCallbacks(
                new StringFilterGroup(
                        Settings.HIDE_COMMENT_CHANNEL_GUIDELINES,
                        "channel_guidelines_entry_banner.eml",
                        "community_guidelines.eml"
                )
        );
        addPathCallbacks(
                new StringFilterGroup(
                        Settings.HIDE_COMMENT_TIMESTAMP_AND_EMOJI_BUTTONS,
                        "|CellType|ContainerType|ContainerType|ContainerType|ContainerType|ContainerType|"
                )
        );
    }
}
