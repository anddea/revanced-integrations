package app.revanced.integrations.reddit.patches;

import java.util.Collections;
import java.util.List;

import app.revanced.integrations.reddit.settings.SettingsEnum;

@SuppressWarnings("unused")
public final class RecentlyVisitedShelfPatch {

    public static List<?> hideRecentlyVisitedShelf(List<?> list) {
        if (!SettingsEnum.HIDE_RECENTLY_VISITED_SHELF.getBoolean())
            return list;

        return Collections.emptyList();
    }
}
