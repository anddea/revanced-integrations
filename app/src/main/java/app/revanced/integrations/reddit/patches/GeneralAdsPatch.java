package app.revanced.integrations.reddit.patches;

import com.reddit.domain.model.ILink;

import java.util.ArrayList;
import java.util.List;

import app.revanced.integrations.reddit.settings.SettingsEnum;

@SuppressWarnings("unused")
public final class GeneralAdsPatch {

    private static List<?> filterChildren(final Iterable<?> links) {
        final List<Object> filteredList = new ArrayList<>();

        for (Object item : links) {
            if (item instanceof ILink && ((ILink) item).getPromoted()) continue;

            filteredList.add(item);
        }

        return filteredList;
    }

    public static boolean hideCommentAds() {
        return SettingsEnum.HIDE_COMMENT_ADS.getBoolean();
    }

    public static List<?> hideOldPostAds(List<?> list) {
        if (!SettingsEnum.HIDE_OLD_POST_ADS.getBoolean())
            return list;

        return filterChildren(list);
    }

    public static boolean hideNewPostAds() {
        return SettingsEnum.HIDE_NEW_POST_ADS.getBoolean();
    }
}
