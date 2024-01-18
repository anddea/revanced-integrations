package app.revanced.integrations.youtube.patches.components;

import app.revanced.integrations.youtube.settings.SettingsEnum;

@SuppressWarnings("unused")
final class CaptionsFilter extends Filter {

    public CaptionsFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_CAPTIONS_BUTTON,
                        "captions_button.eml"
                )
        );
    }
}
