package app.revanced.integrations.patches.ads;

import app.revanced.integrations.utils.ReVancedUtils;

public final class MusicAdsPatch extends MusicFilter {
    private final String[] IGNORE = {
            "menu",
            "root",
            "-count",
            "-space",
            "-button"
    };

    public MusicAdsPatch() {
        var generalAds = new MusicBlockRule("revanced_hide_music_ads","statement_banner");
        var playlistCard = new MusicBlockRule("revanced_hide_playlist_card", "music_container_card_shelf");

        this.pathRegister.registerAll(
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
