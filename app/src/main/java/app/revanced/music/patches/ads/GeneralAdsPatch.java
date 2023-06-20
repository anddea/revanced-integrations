package app.revanced.music.patches.ads;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

public final class GeneralAdsPatch extends Filter {
    private final String[] IGNORE = {
            "menu",
            "root",
            "-count",
            "-space",
            "-button"
    };

    public GeneralAdsPatch() {
        var buttonShelf = new BlockRule(SettingsEnum.HIDE_BUTTON_SHELF, "entry_point_button_shelf");
        var carouselShelf = new BlockRule(SettingsEnum.HIDE_CAROUSEL_SHELF, "music_grid_item_carousel");
        var generalAds = new BlockRule(SettingsEnum.HIDE_MUSIC_ADS, "statement_banner");
        var playlistCard = new BlockRule(SettingsEnum.HIDE_PLAYLIST_CARD, "music_container_card_shelf");

        this.pathRegister.registerAll(
                buttonShelf,
                carouselShelf,
                generalAds,
                playlistCard
        );
    }

    public boolean filter(final String path, final String identifier) {
        BlockResult result;

        if (ReVancedUtils.containsAny(path, IGNORE))
            result = BlockResult.IGNORED;
        else if (pathRegister.contains(path) || identifierRegister.contains(identifier))
            result = BlockResult.DEFINED;
        else
            result = BlockResult.UNBLOCKED;

        LogHelper.printDebug(GeneralAdsPatch.class, String.format("%s (ID: %s): %s", result.message, identifier, path));

        return result.filter;
    }

    private enum BlockResult {
        UNBLOCKED(false, "Unblocked"),
        IGNORED(false, "Ignored"),
        DEFINED(true, "Blocked");

        final boolean filter;
        final String message;

        BlockResult(boolean filter, String message) {
            this.filter = filter;
            this.message = message;
        }
    }
}
