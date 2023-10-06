package app.revanced.music.patches.ads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;


public final class ButtonShelfFilter extends Filter {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ButtonShelfFilter() {
        this.pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_BUTTON_SHELF,
                        "entry_point_button_shelf"
                )
        );
    }
}
