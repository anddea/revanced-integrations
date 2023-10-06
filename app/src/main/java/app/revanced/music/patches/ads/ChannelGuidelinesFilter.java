package app.revanced.music.patches.ads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import app.revanced.music.settings.SettingsEnum;


public final class ChannelGuidelinesFilter extends Filter {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ChannelGuidelinesFilter() {
        this.pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_CHANNEL_GUIDELINES,
                        "community_guidelines"
                )
        );
    }
}
