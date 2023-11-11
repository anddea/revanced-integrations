package app.revanced.integrations.patches.components;

import app.revanced.integrations.settings.SettingsEnum;

final class WaterMarkFilter extends Filter {

    public WaterMarkFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_CHANNEL_WATERMARK,
                        "featured_channel_watermark_overlay.eml"
                )
        );
    }
}
