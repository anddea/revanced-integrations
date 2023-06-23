package app.revanced.integrations.patches.ads;

import android.view.View;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public final class AdsFilter extends Filter {
    private final String[] exceptions;

    public AdsFilter() {
        exceptions = new String[]{
                "comment_thread", // skip blocking anything in the comments
                "download_",
                "downloads_",
                "home_video_with_context", // Don't filter anything in the home page video component.
                "library_recent_shelf",
                "playlist_add",
                "related_video_with_context", // Don't filter anything in the related video component.
                "|comment." // skip blocking anything in the comments replies
        };

        final var carouselAd = new StringFilterGroup(
                SettingsEnum.AD_REMOVER_GENERAL_ADS,
                "carousel_ad"
        );

        final var paidContent = new StringFilterGroup(
                SettingsEnum.AD_REMOVER_PAID_CONTENT,
                "paid_content_overlay"
        );

        final var selfSponsor = new StringFilterGroup(
                SettingsEnum.AD_REMOVER_SELF_SPONSOR,
                "cta_shelf_card"
        );

        final var generalAds = new StringFilterGroup(
                SettingsEnum.AD_REMOVER_GENERAL_ADS,
                "active_view_display_container",
                "ads_",
                "ads_video_with_context",
                "ad_",
                "banner_text_icon",
                "brand_video_shelf",
                "brand_video_singleton",
                "carousel_footered_layout",
                "carousel_headered_layout",
                "full_width_square_image_layout",
                "hero_promo_image",
                "landscape_image_wide_button_layout",
                "legal_disclosure_cell",
                "primetime_promo",
                "product_details",
                "square_image_layout",
                "statement_banner",
                "text_image_button_layout",
                "video_display_button_group_layout",
                "video_display_carousel_buttoned_layout",
                "video_display_full_layout",
                "watch_metadata_app_promo",
                "_ad",
                "_ads",
                "_ad_with",
                "_buttoned_layout",
                "|ads_",
                "|ad_"
        );

        this.pathFilterGroups.addAll(
                generalAds,
                paidContent,
                selfSponsor
        );

        this.identifierFilterGroups.addAll(
                carouselAd
        );
    }

    /**
     * Hide the view, which shows ads in the homepage.
     *
     * @param view The view, which shows ads.
     */
    public static void hideAdAttributionView(View view) {
        ReVancedUtils.hideViewBy0dpUnderCondition(SettingsEnum.AD_REMOVER_GENERAL_ADS.getBoolean(), view);
    }

    public static boolean hideGetPremium() {
        return SettingsEnum.AD_REMOVER_GET_PREMIUM.getBoolean();
    }

    @Override
    public boolean isFiltered(final String path, final String identifier, final String object, final byte[] _protobufBufferArray) {
        if (ReVancedUtils.containsAny(path, exceptions))
            return false;

        return super.isFiltered(path, identifier, object, _protobufBufferArray);
    }
}
