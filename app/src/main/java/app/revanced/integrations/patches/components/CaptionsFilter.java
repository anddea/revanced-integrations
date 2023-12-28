package app.revanced.integrations.patches.components;

import app.revanced.integrations.settings.SettingsEnum;

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
