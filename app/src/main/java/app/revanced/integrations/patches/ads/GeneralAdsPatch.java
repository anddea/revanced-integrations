package app.revanced.integrations.patches.ads;

import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public final class GeneralAdsPatch extends Filter {
    private final String[] IGNORE = {
            "comment_thread", // skip blocking anything in the comments
            "|comment.", // skip blocking anything in the comments replies
            "related_video_with_context",
            "library_recent_shelf"
    };

    private final BlockRule custom = new CustomBlockRule(
            SettingsEnum.ADREMOVER_USER_FILTER,
            SettingsEnum.ADREMOVER_CUSTOM_FILTER
    );

    public GeneralAdsPatch() {
        var carouselAd = new BlockRule(SettingsEnum.ADREMOVER_GENERAL_ADS, "carousel_ad");
        var channelGuidelines = new BlockRule(SettingsEnum.ADREMOVER_CHANNEL_GUIDELINES, "channel_guidelines_entry_banner", "community_guidelines", "sponsorships_comments_upsell");
        var channelMemberShelf = new BlockRule(SettingsEnum.ADREMOVER_CHANNEL_MEMBER_SHELF, "member_recognition_shelf");
        var graySeparator = new BlockRule(SettingsEnum.ADREMOVER_GRAY_SEPARATOR, "cell_divider");
        var imageShelf = new BlockRule(SettingsEnum.ADREMOVER_IMAGE_SHELF, "image_shelf");
        var inFeedSurvey = new BlockRule(SettingsEnum.ADREMOVER_FEED_SURVEY, "in_feed_survey", "slimline_survey");
        var infoPanel = new BlockRule(SettingsEnum.ADREMOVER_INFO_PANEL, "compact_banner", "publisher_transparency_panel", "single_item_information_panel");
        var joinMembership = new BlockRule(SettingsEnum.ADREMOVER_CHANNEL_BAR_JOIN_BUTTON, "compact_sponsor_button");
        var latestPosts = new BlockRule(SettingsEnum.ADREMOVER_LATEST_POSTS, "post_shelf");
        var medicalPanel = new BlockRule(SettingsEnum.ADREMOVER_MEDICAL_PANEL, "medical_panel", "emergency_onebox");
        var merchandise = new BlockRule(SettingsEnum.ADREMOVER_MERCHANDISE, "product_carousel");
        var officialCard = new BlockRule(SettingsEnum.ADREMOVER_OFFICIAL_CARDS, "official_card");
        var paidContent = new BlockRule(SettingsEnum.ADREMOVER_PAID_CONTENT, "paid_content_overlay");
        var selfSponsor = new BlockRule(SettingsEnum.ADREMOVER_SELF_SPONSOR, "cta_shelf_card");
        var teaser = new BlockRule(SettingsEnum.ADREMOVER_TEASER, "expandable_metadata");
        var timedReactions = new BlockRule(SettingsEnum.ADREMOVER_TIMED_REACTIONS, "emoji_control_panel", "timed_reaction_player_animation", "timed_reaction_live_player_overlay");
        var viewProducts = new BlockRule(SettingsEnum.ADREMOVER_VIEW_PRODUCTS, "product_item", "products_in_video");
        var webSearchPanel = new BlockRule(SettingsEnum.ADREMOVER_WEB_SEARCH_PANEL, "web_link_panel");

        var buttonedAd = new BlockRule(SettingsEnum.ADREMOVER_BUTTON_ADS,
                "video_display_full_buttoned_layout",
                "_ad_with",
                "landscape_image_wide_button_layout"
        );
        var generalAds = new BlockRule(
            SettingsEnum.ADREMOVER_GENERAL_ADS,
                "video_display_full_layout",
                "active_view_display_container",
                "|ad_",
                "|ads_",
                "ads_video_with_context",
                "legal_disclosure_cell",
                "primetime_promo",
                "brand_video_shelf",
                "hero_promo_image",
                "statement_banner",
                "square_image_layout",
                "watch_metadata_app_promo"
        );
        var movieAds = new BlockRule(
                SettingsEnum.ADREMOVER_MOVIE_SHELF,
                "browsy_bar",
                "compact_movie",
                "horizontal_movie_shelf",
                "movie_and_show_upsell_card",
                "compact_tvfilm_item",
                "offer_module_root"
        );

        this.pathRegister.registerAll(
                buttonedAd,
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
                officialCard,
                paidContent,
                selfSponsor,
                teaser,
                timedReactions,
                viewProducts,
                webSearchPanel
        );

        this.identifierRegister.registerAll(
                carouselAd,
                graySeparator
        );
    }

    public boolean filter(final String path, final String identifier) {
        BlockResult result;

        if (custom.isEnabled() && custom.check(path).isBlocked())
            result = BlockResult.CUSTOM;
        else if (ReVancedUtils.containsAny(path, IGNORE))
            result = BlockResult.IGNORED;
        else if (pathRegister.contains(path) || identifierRegister.contains(identifier))
            result = BlockResult.DEFINED;
        else
            result = BlockResult.UNBLOCKED;

        return result.filter;
    }

    private enum BlockResult {
        UNBLOCKED(false, "Unblocked"),
        IGNORED(false, "Ignored"),
        DEFINED(true, "Blocked"),
        CUSTOM(true, "Custom");

        final boolean filter;
        final String message;

        BlockResult(boolean filter, String message) {
            this.filter = filter;
            this.message = message;
        }
    }

    /**
     * Hide the specific view, which shows ads in the homepage.
     *
     * @param view The view, which shows ads.
     */
    public static void hideAdAttributionView(View view) {
        if (!SettingsEnum.ADREMOVER_GENERAL_ADS.getBoolean()) return;
        AdRemoverAPI.HideViewWithLayout1dp(view);
    }

    public static void hideBreakingNewsShelf(View view) {
        if (!SettingsEnum.ADREMOVER_BREAKING_NEWS_SHELF.getBoolean()) return;
        AdRemoverAPI.HideViewWithLayout1dp(view);
    }

    public static void hideAlbumCards(View view) {
        if (!SettingsEnum.ADREMOVER_ALBUM_CARDS.getBoolean()) return;
        AdRemoverAPI.HideViewWithLayout1dp(view);
    }

    public static boolean hideInfoPanel() {
        return SettingsEnum.ADREMOVER_INFO_PANEL.getBoolean();
    }

    public static boolean hidePaidContentBanner() {
        return SettingsEnum.ADREMOVER_PAID_CONTENT.getBoolean();
    }
}
