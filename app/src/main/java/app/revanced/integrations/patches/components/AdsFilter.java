package app.revanced.integrations.patches.components;

import android.view.View;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

@SuppressWarnings("unused")
public final class AdsFilter extends Filter {

    public AdsFilter() {

        final StringFilterGroup carouselAd = new StringFilterGroup(
                SettingsEnum.HIDE_GENERAL_ADS,
                "carousel_ad"
        );

        final StringFilterGroup imageShelf = new StringFilterGroup(
                SettingsEnum.HIDE_IMAGE_SHELF,
                "image_shelf"
        );

        final StringFilterGroup merchandise = new StringFilterGroup(
                SettingsEnum.HIDE_MERCHANDISE_SHELF,
                "product_carousel"
        );

        final StringFilterGroup paidContent = new StringFilterGroup(
                SettingsEnum.HIDE_PAID_PROMOTION,
                "paid_content_overlay"
        );

        final StringFilterGroup selfSponsor = new StringFilterGroup(
                SettingsEnum.HIDE_SELF_SPONSOR_CARDS,
                "cta_shelf_card"
        );

        final StringFilterGroup generalAds = new StringFilterGroup(
                SettingsEnum.HIDE_GENERAL_ADS,
                "ads_video_with_context",
                "banner_text_icon",
                "brand_video",
                "button_banner",
                "carousel_footered_layout",
                "carousel_headered_layout",
                "full_width_portrait_image_layout",
                "full_width_square_image_layout",
                "hero_promo_image",
                "landscape_image_wide_button_layout",
                "legal_disclosure",
                "lumiere_promo_carousel",
                "primetime_promo",
                "product_details",
                "square_image_layout",
                "statement_banner",
                "text_image_button_group_layout",
                "text_image_button_layout",
                "video_display_button_group_layout",
                "video_display_carousel_button",
                "video_display_full_layout",
                "watch_metadata_app_promo",
                "_ad_with",
                "_buttoned_layout",
                "_image_layout"
        );

        final StringFilterGroup viewProducts = new StringFilterGroup(
                SettingsEnum.HIDE_VIEW_PRODUCTS,
                "product_item",
                "products_in_video"
        );

        final StringFilterGroup webSearchPanel = new StringFilterGroup(
                SettingsEnum.HIDE_WEB_SEARCH_RESULTS,
                "web_link_panel",
                "web_result_panel"
        );

        pathFilterGroupList.addAll(
                generalAds,
                imageShelf,
                merchandise,
                paidContent,
                selfSponsor,
                viewProducts,
                webSearchPanel
        );

        identifierFilterGroupList.addAll(carouselAd);
    }

    /**
     * Hide the view, which shows ads in the homepage.
     *
     * @param view The view, which shows ads.
     */
    public static void hideAdAttributionView(View view) {
        ReVancedUtils.hideViewBy0dpUnderCondition(SettingsEnum.HIDE_GENERAL_ADS.getBoolean(), view);
    }

    public static boolean hideGetPremium() {
        return SettingsEnum.HIDE_GET_PREMIUM.getBoolean();
    }
}
