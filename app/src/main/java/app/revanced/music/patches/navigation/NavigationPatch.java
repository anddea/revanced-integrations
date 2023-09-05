package app.revanced.music.patches.navigation;

import static app.revanced.music.utils.ReVancedUtils.hideViewUnderCondition;

import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import app.revanced.music.settings.SettingsEnum;

public class NavigationPatch {

    public static int enableBlackNavigationBar() {
        return SettingsEnum.ENABLE_BLACK_NAVIGATION_BAR.getBoolean() ? -16777216 : -14869219;
    }

    public static void hideNavigationLabel(TextView textview) {
        hideViewUnderCondition(SettingsEnum.HIDE_NAVIGATION_LABEL.getBoolean(), textview);
    }

    public static void hideSampleButton(List list) {
        if (!SettingsEnum.HIDE_SAMPLE_BUTTON.getBoolean() || !SettingsEnum.IS_SAMPLE_BUTTON_SHOWN.getBoolean())
            return;

        final int size = list.size();
        final int sampleButtonIndex = 1;

        if (size > sampleButtonIndex) {
            list.remove(sampleButtonIndex);
        }
    }

    public static boolean hideUpgradeButton(@NonNull Enum button) {
        final String buttonName = button.name();

        if (!SettingsEnum.IS_SAMPLE_BUTTON_SHOWN.getBoolean() && buttonName.equals("TAB_SAMPLES")) {
            SettingsEnum.IS_SAMPLE_BUTTON_SHOWN.saveValue(true);
        }

        return buttonName.equals("TAB_MUSIC_PREMIUM");
    }
}
