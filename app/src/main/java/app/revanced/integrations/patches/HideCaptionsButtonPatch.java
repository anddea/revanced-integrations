package app.revanced.integrations.patches;

import android.widget.ImageView;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;

public class HideCaptionsButtonPatch {

    public static void hideCaptionsButton(ImageView imageView) {
        imageView.setVisibility(SettingsEnum.CAPTIONS_BUTTON_SHOWN.getBoolean() ? ImageView.VISIBLE : ImageView.GONE);
    }
}