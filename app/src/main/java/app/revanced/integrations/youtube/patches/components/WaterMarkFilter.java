package app.revanced.integrations.youtube.patches.components;

import app.revanced.integrations.youtube.settings.SettingsEnum;

@SuppressWarnings("unused")
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
