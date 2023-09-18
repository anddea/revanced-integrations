package app.revanced.music.settingsmenu;

import static app.revanced.music.utils.ReVancedHelper.getDialogBuilder;
import static app.revanced.music.utils.ReVancedHelper.getStringArray;
import static app.revanced.music.utils.SharedPrefHelper.saveString;
import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.util.Arrays;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;

public class ListDialogBuilder {
    private static int mClickedDialogEntryIndex;

    public static void listDialogBuilder(@NonNull SettingsEnum setting, @NonNull Activity activity, int defaultIndex, String entryKey, String entryValueKey) {
        try {
            final String[] mEntries = getStringArray(activity, entryKey);
            final String[] mEntryValues = getStringArray(activity, entryValueKey);

            final int index = Arrays.asList(mEntryValues).indexOf(setting.getString());
            mClickedDialogEntryIndex = Math.max(index, defaultIndex);

            getDialogBuilder(activity)
                    .setTitle(str(setting.path + "_title"))
                    .setSingleChoiceItems(mEntries, mClickedDialogEntryIndex,
                            (dialog, id) -> mClickedDialogEntryIndex = id)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setNeutralButton(str("revanced_reset"), (dialog, which) -> {
                        saveString(setting.path, setting.defaultValue.toString());
                        SharedPreferenceChangeListener.rebootDialog();
                    })
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        saveString(setting.path, mEntryValues[mClickedDialogEntryIndex]);
                        SharedPreferenceChangeListener.rebootDialog();
                    })
                    .show();
        } catch (Exception ex) {
            LogHelper.printException(ListDialogBuilder.class, "listDialogBuilder failure", ex);
        }
    }
}
