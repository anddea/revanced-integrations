package app.revanced.music.settingsmenu;

import static app.revanced.music.utils.ReVancedHelper.getDialogBuilder;
import static app.revanced.music.utils.ReVancedHelper.getStringArray;
import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.util.Arrays;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;

public class ListDialogBuilder {
    private static int mClickedDialogEntryIndex;

    public static void listDialogBuilder(@NonNull SettingsEnum setting, int defaultIndex) {
        try {
            final Activity activity = ReVancedSettingActivity.getActivity();

            if (activity == null)
                return;

            final String entryKey = setting.path + "_entry";
            final String entryValueKey = setting.path + "_entry_value";
            final String[] mEntries = getStringArray(activity, entryKey);
            final String[] mEntryValues = getStringArray(activity, entryValueKey);

            final int findIndex = Arrays.binarySearch(mEntryValues, setting.getString());
            mClickedDialogEntryIndex = findIndex >= 0 ? findIndex : defaultIndex;

            getDialogBuilder(activity)
                    .setTitle(str(setting.path + "_title"))
                    .setSingleChoiceItems(mEntries, mClickedDialogEntryIndex,
                            (dialog, id) -> mClickedDialogEntryIndex = id)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setNeutralButton(str("revanced_reset"), (dialog, which) -> {
                        setting.resetToDefault();
                        ReVancedSettingsFragment.showRebootDialog();
                    })
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        setting.saveValue(mEntryValues[mClickedDialogEntryIndex]);
                        ReVancedSettingsFragment.showRebootDialog();
                    })
                    .show();
        } catch (Exception ex) {
            LogHelper.printException(() -> "listDialogBuilder failure", ex);
        }
    }
}
