package app.revanced.music.patches.components;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;


public final class EmojiPickerFilter extends Filter {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public EmojiPickerFilter() {
        pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_EMOJI_PICKER,
                        "|CellType|ContainerType|ContainerType|ContainerType|ContainerType|ContainerType|"
                )
        );
    }
}
