package app.revanced.integrations.reddit.patches;

import android.view.View;
import android.view.ViewGroup;

import app.revanced.integrations.reddit.settings.Settings;
import app.revanced.integrations.shared.utils.Logger;

@SuppressWarnings("unused")
public final class NavigationButtonsPatch {

    public static void hideNavigationButtons(ViewGroup viewGroup) {
        try {
            for (NavigationButton button : NavigationButton.values())
                if (button.enabled && viewGroup.getChildCount() > button.index)
                    viewGroup.getChildAt(button.index).setVisibility(View.GONE);
        } catch (Exception exception) {
            Logger.printException(() -> "Failed to remove button view", exception);
        }
    }

    private enum NavigationButton {
        CHAT(Settings.HIDE_CHAT_BUTTON.get(), 3),
        CREATE(Settings.HIDE_CREATE_BUTTON.get(), 2),
        DISCOVER(Settings.HIDE_DISCOVER_BUTTON.get(), 1);
        private final boolean enabled;
        private final int index;

        NavigationButton(final boolean enabled, final int index) {
            this.enabled = enabled;
            this.index = index;
        }
    }
}
