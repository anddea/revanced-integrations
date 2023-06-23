package app.revanced.reddit.patches;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.reddit.frontpage.RedditDeepLinkActivity;

import app.revanced.reddit.settings.SettingsEnum;

public class OpenLinksExternallyPatch {

    /**
     * Override 'CustomTabsIntent', in order to open links in the default browser.
     * Instead of doing CustomTabsActivity,
     * this is done by building send data Intent from Android support library.
     * <p>
     * Modified implementation from developer.android.com/training/sharing/send
     *
     * @param activity The activity, to start an Intent.
     * @param uri      The URL to be opened in the default browser.
     */
    public static void openLinksExternally(@NonNull Activity activity, @NonNull Uri uri) {
        Intent createChooser = Intent.createChooser(new Intent("android.intent.action.VIEW", uri), null);
        createChooser.putExtra("android.intent.extra.EXCLUDE_COMPONENTS", new Parcelable[]{new Intent(activity, RedditDeepLinkActivity.class)});
        activity.startActivity(createChooser);
    }

    public static boolean openLinksExternally() {
        return SettingsEnum.ENABLE_OPEN_LINKS_EXTERNALLY.getBoolean();
    }
}
