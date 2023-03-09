package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;

final class CommentsPatch extends Filter {

    public CommentsPatch() {
        var comments = new BlockRule(SettingsEnum.HIDE_COMMENTS_SECTION, "video_metadata_carousel", "_comments");
        var previewComment = new BlockRule(SettingsEnum.HIDE_PREVIEW_COMMENT, "|carousel_item", "comments_entry_point_teaser", "comments_entry_point_simplebox");
        var thanksButton = new BlockRule(SettingsEnum.HIDE_COMMENTS_THANKS_BUTTON, "super_thanks_button");

        this.pathRegister.registerAll(
                comments,
                previewComment,
                thanksButton
        );
    }

    @Override
    boolean filter(String path, String _identifier) {
        return pathRegister.contains(path);
    }
}
