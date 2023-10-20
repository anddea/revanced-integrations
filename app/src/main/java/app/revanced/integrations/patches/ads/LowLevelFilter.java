package app.revanced.integrations.patches.ads;

import java.util.Arrays;
import java.util.List;

import app.revanced.integrations.patches.utils.PatchStatus;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;


public class LowLevelFilter {

    private static final List<String> ignoredList = Arrays.asList(
            "_menu",
            "-button",
            "-count",
            "-space"
    );
    private static final List<String> browseButtonPhone = Arrays.asList(
            "channel_profile_phone.eml",
            "channel_action_buttons_phone.eml",
            "|ContainerType|button.eml|"
    );
    private static final List<String> browseButtonTablet = Arrays.asList(
            "channel_profile_tablet.eml",
            "|ContainerType|ContainerType|ContainerType|ContainerType|ContainerType|button.eml|"
    );
    private static final List<String> joinButtonPhone = List.of(
            "|ContainerType|ContainerType|ContainerType|button.eml|"
    );

    public static boolean filters(String path) {
        try {
            if (ignoredList.stream().anyMatch(path::contains))
                return false;

            return filter(path);
        } catch (Exception ex) {
            LogHelper.printException(LowLevelFilter.class, "Litho filter failure", ex);
        }
        return false;
    }

    private static boolean filter(String path) {
        int count = 0;

        if (PatchStatus.LayoutComponent()) {
            // Browse store button needs a bit of a tricky filter
            if (SettingsEnum.HIDE_BROWSE_STORE_BUTTON.getBoolean() &&
                    ((browseButtonPhone.stream().allMatch(path::contains) && joinButtonPhone.stream().noneMatch(path::contains)) ||
                            browseButtonTablet.stream().allMatch(path::contains)))
                count++;
        }

        return count > 0;
    }
}
