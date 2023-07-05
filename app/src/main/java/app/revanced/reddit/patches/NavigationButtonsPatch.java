package app.revanced.reddit.patches;

import java.util.List;

import app.revanced.reddit.settings.SettingsEnum;
import app.revanced.reddit.utils.LogHelper;

public final class NavigationButtonsPatch {

    public static List hideNavigationButtons(List list) {
        try {
            for (NavigationButton button : NavigationButton.values())
                if (button.enabled && list.size() > button.index)
                    list.remove(button.index);
        } catch (Exception exception) {
            LogHelper.printException(NavigationButtonsPatch.class, "Failed to remove button view", exception);
        }
        return list;
    }

    private enum NavigationButton {
        CHAT(SettingsEnum.HIDE_CHAT_BUTTON.getBoolean(), 3),
        CREATE(SettingsEnum.HIDE_CREATE_BUTTON.getBoolean(), 2),
        DISCOVER(SettingsEnum.HIDE_DISCOVER_BUTTON.getBoolean(), 1);
        private final boolean enabled;
        private final int index;

        NavigationButton(final boolean enabled, final int index) {
            this.enabled = enabled;
            this.index = index;
        }
    }
}
