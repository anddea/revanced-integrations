package app.revanced.music.patches.buttoncontainer;


import app.revanced.music.settings.SettingsEnum;

public class ButtonContainerPatch {

    public static boolean hideButtonContainerLabel(boolean original) {
        return !SettingsEnum.HIDE_BUTTON_CONTAINER_LABEL.getBoolean() && original;
    }

}
