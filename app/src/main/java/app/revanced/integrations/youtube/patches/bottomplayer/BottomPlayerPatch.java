package app.revanced.integrations.youtube.patches.bottomplayer;

import static app.revanced.integrations.youtube.utils.ResourceUtils.identifier;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.utils.ResourceType;

@SuppressWarnings("unused")
public class BottomPlayerPatch {
    private static final int inlineExtraButtonId;

    static {
        inlineExtraButtonId = identifier("inline_extra_buttons", ResourceType.ID);
    }

    public static void changeEmojiPickerOpacity(ImageView imageView) {
        if (!SettingsEnum.HIDE_EMOJI_PICKER.getBoolean())
            return;

        imageView.setImageAlpha(0);
    }

    @Nullable
    public static Object disableEmojiPickerOnClickListener(@Nullable Object object) {
        return SettingsEnum.HIDE_EMOJI_PICKER.getBoolean() ? null : object;
    }

    public static boolean enableBottomPlayerGestures() {
        return SettingsEnum.ENABLE_BOTTOM_PLAYER_GESTURES.getBoolean();
    }

    public static int hideThanksButton(View view, int visibility) {
        if (!SettingsEnum.HIDE_COMMENTS_THANKS_BUTTON.getBoolean())
            return visibility;

        if (view.getId() != inlineExtraButtonId)
            return visibility;

        return View.GONE;
    }

}