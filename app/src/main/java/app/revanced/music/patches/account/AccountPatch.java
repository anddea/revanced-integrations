package app.revanced.music.patches.account;

import android.view.View;
import android.widget.TextView;

import app.revanced.music.settings.SettingsEnum;

public class AccountPatch {

    public static void hideAccountMenu(CharSequence charSequence, View view) {
        if (!SettingsEnum.HIDE_ACCOUNT_MENU.getBoolean())
            return;

        if (charSequence == null) {
            if (SettingsEnum.HIDE_ACCOUNT_MENU_EMPTY_COMPONENT.getBoolean())
                view.setVisibility(View.GONE);

            return;
        }

        final String[] blockList = SettingsEnum.HIDE_ACCOUNT_MENU_FILTER_STRINGS.getString().split("\\n");

        for (String filter : blockList) {
            if (charSequence.toString().equals(filter) && !filter.isEmpty())
                view.setVisibility(View.GONE);
        }
    }

    public static boolean hideHandle(boolean original) {
        return SettingsEnum.HIDE_HANDLE.getBoolean() || original;
    }

    public static void hideHandle(TextView textView, int visibility) {
        if (SettingsEnum.HIDE_HANDLE.getBoolean()) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(visibility);
        }
    }

    public static int hideTermsContainer() {
        return SettingsEnum.HIDE_TERMS_CONTAINER.getBoolean() ? View.GONE : View.VISIBLE;
    }
}
