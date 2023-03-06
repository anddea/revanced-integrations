package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;

final class ButtonsPatch extends Filter {
    private final BlockRule actionBarRule;

    private final BlockRule[] rules;

    public ButtonsPatch() {
        var likeButton = new BlockRule(SettingsEnum.HIDE_LIKE_BUTTON, "|like_button");
        var dislikeButton = new BlockRule(SettingsEnum.HIDE_DISLIKE_BUTTON, "dislike_button");
        var downloadButton = new BlockRule(SettingsEnum.HIDE_DOWNLOAD_BUTTON, "download_button");
        var playlistButton = new BlockRule(SettingsEnum.HIDE_PLAYLIST_BUTTON, "save_to_playlist_button");
        rules = new BlockRule[]{likeButton, dislikeButton, downloadButton, playlistButton};

        actionBarRule = new BlockRule(null, "video_action_bar");

        this.pathRegister.registerAll(
                likeButton,
                dislikeButton,
                downloadButton,
                playlistButton
        );
    }

    private boolean hideActionBar() {
        for (BlockRule rule : rules) if (!rule.isEnabled()) return false;
        return true;
    }

    @Override
    boolean filter(String path, String identifier) {
        if (hideActionBar() && actionBarRule.check(identifier).isBlocked()) return true;
        return pathRegister.contains(path);
    }
}
