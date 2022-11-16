package app.revanced.integrations.patches;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;

/**
 * Helper functions.
 */
final class Extensions {
    static boolean containsAny(final String value, final String... targets) {
        for (String string : targets)
            if (value.contains(string)) return true;
        return false;
    }

    static boolean any(LithoBlockRegister register, String path) {
        for (var rule : register) {
            if (!rule.isEnabled()) continue;

            var result = rule.check(path);
            if (result.isBlocked()) {
                return true;
            }
        }

        return false;
    }
}

final class BlockRule {
    final static class BlockResult {
        private final boolean blocked;
        private final SettingsEnum setting;

        public BlockResult(final SettingsEnum setting, final boolean blocked) {
            this.setting = setting;
            this.blocked = blocked;
        }

        public SettingsEnum getSetting() {
            return setting;
        }

        public boolean isBlocked() {
            return blocked;
        }
    }

    private final SettingsEnum setting;
    private final String[] blocks;
    private final boolean invert;

    /**
     * Initialize a new rule for components.
     *
     * @param setting The setting which controls the blocking of this component.
     * @param blocks  The rules to block the component on.
     */
    public BlockRule(final SettingsEnum setting, final String... blocks) {
        this.setting = setting;
        this.blocks = blocks;
        this.invert = false;
    }

    public BlockRule(final SettingsEnum setting, final boolean invert, final String... blocks) {
        this.setting = setting;
        this.blocks = blocks;
        this.invert = invert;
    }

    public boolean isEnabled() {
        return invert ? !setting.getBoolean() : setting.getBoolean();
    }

    public BlockResult check(final String string) {
        return new BlockResult(setting, string != null && Extensions.containsAny(string, blocks));
    }
}

abstract class Filter {
    final LithoBlockRegister pathRegister = new LithoBlockRegister();
    final LithoBlockRegister identifierRegister = new LithoBlockRegister();

    abstract boolean filter(final String path, final String identifier);
}

final class LithoBlockRegister implements Iterable<BlockRule> {
    private final ArrayList<BlockRule> blocks = new ArrayList<>();

    public void registerAll(BlockRule... blocks) {
        this.blocks.addAll(Arrays.asList(blocks));
    }

    @NonNull
    @Override
    public Iterator<BlockRule> iterator() {
        return blocks.iterator();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void forEach(@NonNull Consumer<? super BlockRule> action) {
        blocks.forEach(action);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Spliterator<BlockRule> spliterator() {
        return blocks.spliterator();
    }
}

public final class LithoFilterPatch {
    private static final Filter[] filters = new Filter[]{
            new GeneralBytecodeAdsPatch(),
            new CommentsPatch()
    };

    public static boolean filter(final StringBuilder pathBuilder, final String identifier) {
        var path = pathBuilder.toString();
        if (path.isEmpty()) return false;

        LogHelper.debug(LithoFilterPatch.class, String.format("Searching (ID: %s): %s", identifier, path));

        for (var filter : filters) {
            if (filter.filter(path, identifier)) return true;
        }

        return false;
    }
}

final class CommentsPatch extends Filter {

    public CommentsPatch() {
        var comments = new BlockRule(SettingsEnum.HIDE_COMMENTS_SECTION, "video_metadata_carousel", "comments_");
        var previewComment = new BlockRule(
                SettingsEnum.HIDE_PREVIEW_COMMENT,
                "carousel_item",
                "comments_entry_point_teaser",
                "comments_entry_point_simplebox"
        );

        this.pathRegister.registerAll(
                comments,
                previewComment
        );
    }

    @Override
    boolean filter(String path, String _identifier) {
        if (!Extensions.any(pathRegister, path)) return false;

        LogHelper.debug(CommentsPatch.class, "Blocked: " + path);

        return true;
    }
}

final class GeneralBytecodeAdsPatch extends Filter {

    public GeneralBytecodeAdsPatch() {
        var communityPosts = new BlockRule(SettingsEnum.ADREMOVER_COMMUNITY_POSTS_REMOVAL, "post_base_wrapper");
        var communityGuidelines = new BlockRule(SettingsEnum.ADREMOVER_COMMUNITY_GUIDELINES_REMOVAL, "community_guidelines");
        var compactBanner = new BlockRule(SettingsEnum.ADREMOVER_COMPACT_BANNER_REMOVAL, "compact_banner");
        var inFeedSurvey = new BlockRule(SettingsEnum.ADREMOVER_FEED_SURVEY_REMOVAL, "in_feed_survey");
        var medicalPanel = new BlockRule(SettingsEnum.ADREMOVER_MEDICAL_PANEL_REMOVAL, "medical_panel");
        var paidContent = new BlockRule(SettingsEnum.ADREMOVER_PAID_CONTECT_REMOVAL, "paid_content_overlay");
        var merchandise = new BlockRule(SettingsEnum.ADREMOVER_MERCHANDISE_REMOVAL, "product_carousel");
        var image = new BlockRule(SettingsEnum.ADREMOVER_IMAGE_SHELF_REMOVAL, "image_shelf");
        var infoPanel = new BlockRule(SettingsEnum.ADREMOVER_INFO_PANEL_REMOVAL, "publisher_transparency_panel", "single_item_information_panel");
        var suggestions = new BlockRule(SettingsEnum.ADREMOVER_SUGGESTIONS_REMOVAL, "horizontal_video_shelf");
        var latestPosts = new BlockRule(SettingsEnum.ADREMOVER_HIDE_LATEST_POSTS, "post_shelf");
        var channelGuidelines = new BlockRule(SettingsEnum.ADREMOVER_HIDE_CHANNEL_GUIDELINES, "channel_guidelines_entry_banner");
        var selfSponsor = new BlockRule(SettingsEnum.ADREMOVER_SELF_SPONSOR_REMOVAL, "cta_shelf_card");
        var chapterTeaser = new BlockRule(SettingsEnum.ADREMOVER_CHAPTER_TEASER_REMOVAL, "expandable_metadata");
        var officialCard = new BlockRule(SettingsEnum.HIDE_OFFICIAL_CARDS, true, "official_card");
        var generalAds = new BlockRule(
            SettingsEnum.ADREMOVER_GENERAL_ADS_REMOVAL,
            // could be required
            //"full_width_square_image_layout",
            "video_display_full",
            "_ad",
            "active_view_display_container",
            "|ad_",
            "|ads_",
            "ads_video_with_context",
            "cell_divider",
            "legal_disclosure_cell",
            "reels_player_overlay",
            "primetime_promo",
            "watch_metadata_app_promo"
        );
        var movieAds = new BlockRule(
            SettingsEnum.ADREMOVER_MOVIE_REMOVAL,
            "browsy_bar",
            "compact_movie",
            "horizontal_movie_shelf",
            "movie_and_show_upsell_card"
        );

        this.pathRegister.registerAll(
            generalAds,
            communityPosts,
            paidContent,
            image,
            suggestions,
            latestPosts,
            movieAds,
            chapterTeaser,
            communityGuidelines,
            compactBanner,
            inFeedSurvey,
            medicalPanel,
            merchandise,
            infoPanel,
            channelGuidelines,
            officialCard,
            selfSponsor
        );

        // Block for the ComponentContext.identifier field
        var carouselAd = new BlockRule(SettingsEnum.ADREMOVER_GENERAL_ADS_REMOVAL, "carousel_ad");
        var shorts = new BlockRule(SettingsEnum.SHORTS_SHELF, true, "shelf_header", "shorts_shelf", "inline_shorts");

        this.identifierRegister.registerAll(
                shorts,
                carouselAd
        );
    }

    public boolean filter(final String path, final String identifier) {
        // Do not block on these
        if (Extensions.containsAny(path,
            "home_video_with_context",
            "related_video_with_context",
            "comment_thread",
            "horizontal_shelf",
            "playlist_add_to_option_wrapper"
        )) return false;

        if (!(Extensions.any(pathRegister, path) || Extensions.any(identifierRegister, identifier)))
            return false;

        LogHelper.debug(GeneralBytecodeAdsPatch.class, String.format("Blocked (ID: %s): %s", identifier, path));

        return true;
    }
}
