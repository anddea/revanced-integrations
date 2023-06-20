package app.revanced.integrations.patches.ads;

import android.view.View;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public final class AdsFilter extends Filter {
    private final String[] exceptions;

    private final CustomFilterGroup custom;

    public AdsFilter() {
        exceptions = new String[]{
                "comment_thread", // skip blocking anything in the comments
                "download_",
                "downloads_",
                "library_recent_shelf",
                "playlist_add",
                "related_video_with_context",
                "|comment." // skip blocking anything in the comments replies
        };

        custom = new CustomFilterGroup(
                SettingsEnum.AD_REMOVER_USER_FILTER,
                SettingsEnum.AD_REMOVER_CUSTOM_FILTER
        );

        final var albumCard = new StringFilterGroup(
                SettingsEnum.HIDE_ALBUM_CARDS,
                "browsy_bar",
                "official_card"
        );

        final var audioTrackButton = new StringFilterGroup(
                SettingsEnum.HIDE_AUDIO_TRACK_BUTTON,
                "multi_feed_icon_button"
        );

        final var carouselAd = new StringFilterGroup(
                SettingsEnum.AD_REMOVER_GENERAL_ADS,
                "carousel_ad"
        );

        final var channelGuidelines = new StringFilterGroup(
                SettingsEnum.HIDE_CHANNEL_GUIDELINES,
                "channel_guidelines_entry_banner",
                "community_guidelines",
                "sponsorships_comments_upsell"
        );

        final var channelMemberShelf = new StringFilterGroup(
                SettingsEnum.HIDE_CHANNEL_MEMBER_SHELF,
                "member_recognition_shelf"
        );

        final var graySeparator = new StringFilterGroup(
                SettingsEnum.HIDE_GRAY_SEPARATOR,
                "cell_divider"
        );

        final var imageShelf = new StringFilterGroup(
                SettingsEnum.HIDE_IMAGE_SHELF,
                "image_shelf"
        );

        final var inFeedSurvey = new StringFilterGroup(
                SettingsEnum.HIDE_FEED_SURVEY,
                "in_feed_survey",
                "slimline_survey"
        );

        final var infoPanel = new StringFilterGroup(
                SettingsEnum.HIDE_INFO_PANEL,
                "compact_banner",
                "publisher_transparency_panel",
                "single_item_information_panel"
        );

        final var joinMembership = new StringFilterGroup(
                SettingsEnum.HIDE_CHANNEL_BAR_JOIN_BUTTON,
                "compact_sponsor_button"
        );

        final var latestPosts = new StringFilterGroup(
                SettingsEnum.HIDE_LATEST_POSTS,
                "post_shelf"
        );

        final var medicalPanel = new StringFilterGroup(
                SettingsEnum.HIDE_MEDICAL_PANEL,
                "emergency_onebox",
                "medical_panel"
        );

        final var merchandise = new StringFilterGroup(
                SettingsEnum.HIDE_MERCHANDISE,
                "product_carousel"
        );

        final var paidContent = new StringFilterGroup(
                SettingsEnum.AD_REMOVER_PAID_CONTENT,
                "paid_content_overlay"
        );

        final var selfSponsor = new StringFilterGroup(
                SettingsEnum.AD_REMOVER_SELF_SPONSOR,
                "cta_shelf_card"
        );

        final var teaser = new StringFilterGroup(
                SettingsEnum.HIDE_TEASER,
                "expandable_metadata"
        );

        final var ticketShelf = new StringFilterGroup(
                SettingsEnum.HIDE_TICKET_SHELF,
                "ticket_horizontal_shelf",
                "ticket_shelf"
        );

        final var timedReactions = new StringFilterGroup(
                SettingsEnum.HIDE_TIMED_REACTIONS,
                "emoji_control_panel",
                "timed_reaction"
        );

        final var viewProducts = new StringFilterGroup(
                SettingsEnum.HIDE_VIEW_PRODUCTS,
                "products_in_video",
                "product_item"
        );

        final var webSearchPanel = new StringFilterGroup(
                SettingsEnum.HIDE_WEB_SEARCH_PANEL,
                "web_link_panel"
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

        final var movieAds = new StringFilterGroup(
                SettingsEnum.HIDE_MOVIE_SHELF,
                "compact_movie",
                "horizontal_movie_shelf",
                "movie_and_show_upsell_card",
                "compact_tvfilm_item",
                "offer_module_root"
        );

        this.pathFilterGroups.addAll(
                albumCard,
                audioTrackButton,
                channelGuidelines,
                channelMemberShelf,
                generalAds,
                imageShelf,
                inFeedSurvey,
                infoPanel,
                joinMembership,
                latestPosts,
                medicalPanel,
                merchandise,
                movieAds,
                paidContent,
                selfSponsor,
                teaser,
                ticketShelf,
                timedReactions,
                viewProducts,
                webSearchPanel
        );

        this.identifierFilterGroups.addAll(
                graySeparator,
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
        FilterResult result;

        if (custom.isEnabled() && custom.check(path).isFiltered())
            result = FilterResult.CUSTOM;
        else if (ReVancedUtils.containsAny(path, exceptions))
            result = FilterResult.EXCEPTION;
        else if (pathFilterGroups.contains(path) || identifierFilterGroups.contains(identifier))
            result = FilterResult.FILTERED;
        else
            result = FilterResult.UNFILTERED;

        return result.filter;
    }

    private enum FilterResult {
        UNFILTERED(false, "Unfiltered"),
        EXCEPTION(false, "Exception"),
        FILTERED(true, "Filtered"),
        CUSTOM(true, "Custom");

        final Boolean filter;
        final String message;

        FilterResult(boolean filter, String message) {
            this.filter = filter;
            this.message = message;
        }
    }
}
