package app.revanced.integrations.music.patches.actionbar;

import static app.revanced.integrations.music.utils.ReVancedUtils.hideViewUnderCondition;

import android.view.View;

import androidx.annotation.NonNull;

import app.revanced.integrations.music.settings.SettingsEnum;
import app.revanced.integrations.music.utils.VideoHelpers;

@SuppressWarnings("unused")
public class ActionBarPatch {

    @NonNull
    private static String buttonType = "";

    public static boolean hideActionBarLabel() {
        return SettingsEnum.HIDE_ACTION_BUTTON_LABEL.getBoolean();
    }

    public static boolean hideActionButton() {
        for (ActionButton actionButton : ActionButton.values())
            if (actionButton.enabled && actionButton.name.equals(buttonType))
                return true;

        return false;
    }

    public static void hideLikeDislikeButton(View view) {
        hideViewUnderCondition(
                SettingsEnum.HIDE_ACTION_BUTTON_LIKE_DISLIKE.getBoolean(),
                view
        );
    }

    public static boolean hideLikeDislikeButton(boolean original) {
        return SettingsEnum.HIDE_ACTION_BUTTON_LIKE_DISLIKE.getBoolean() || original;
    }

    public static void hookDownloadButton(View view) {
        if (!SettingsEnum.HOOK_ACTION_BUTTON_DOWNLOAD.getBoolean()) {
            return;
        }

        if (buttonType.equals(ActionButton.DOWNLOAD.name))
            view.setOnClickListener(imageView -> VideoHelpers.downloadMusic(imageView.getContext()));
    }

    public static void setButtonType(@NonNull Object obj) {
        final String buttonType = obj.toString();

        for (ActionButton actionButton : ActionButton.values())
            if (buttonType.contains(actionButton.identifier))
                setButtonType(actionButton.name);
    }

    public static void setButtonType(@NonNull String newButtonType) {
        buttonType = newButtonType;
    }

    public static void setButtonTypeDownload(int type) {
        if (type != 0)
            return;

        setButtonType(ActionButton.DOWNLOAD.name);
    }

    private enum ActionButton {
        ADD_TO_PLAYLIST("ACTION_BUTTON_ADD_TO_PLAYLIST", "69487224", SettingsEnum.HIDE_ACTION_BUTTON_ADD_TO_PLAYLIST.getBoolean()),
        COMMENT_DISABLED("ACTION_BUTTON_COMMENT", "76623563", SettingsEnum.HIDE_ACTION_BUTTON_COMMENT.getBoolean()),
        COMMENT_ENABLED("ACTION_BUTTON_COMMENT", "138681778", SettingsEnum.HIDE_ACTION_BUTTON_COMMENT.getBoolean()),
        DOWNLOAD("ACTION_BUTTON_DOWNLOAD", "73080600", SettingsEnum.HIDE_ACTION_BUTTON_DOWNLOAD.getBoolean()),
        RADIO("ACTION_BUTTON_RADIO", "48687757", SettingsEnum.HIDE_ACTION_BUTTON_RADIO.getBoolean()),
        SHARE("ACTION_BUTTON_SHARE", "90650344", SettingsEnum.HIDE_ACTION_BUTTON_SHARE.getBoolean());

        private final String name;
        private final String identifier;
        private final boolean enabled;

        ActionButton(String name, String identifier, boolean enabled) {
            this.name = name;
            this.identifier = identifier;
            this.enabled = enabled;
        }
    }
}
