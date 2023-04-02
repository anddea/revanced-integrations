package app.revanced.integrations.patches.button;

import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.ResourceUtils.anim;
import static app.revanced.integrations.utils.ResourceUtils.findView;
import static app.revanced.integrations.utils.ResourceUtils.identifier;
import static app.revanced.integrations.utils.ResourceUtils.integer;
import static app.revanced.integrations.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import app.revanced.integrations.patches.video.VideoInformation;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ResourceType;

public class Download {
    static WeakReference<ImageView> buttonView = new WeakReference<>(null);
    @SuppressLint("StaticFieldLeak")
    static ConstraintLayout constraintLayout;
    static int fadeDurationFast;
    static int fadeDurationScheduled;
    static Animation fadeIn;
    static Animation fadeOut;
    public static boolean isButtonEnabled;
    static boolean isShowing;

    public static void initialize(Object obj) {
        try {
            constraintLayout = (ConstraintLayout) obj;
            isButtonEnabled = setValue();
            ImageView imageView = findView(Download.class, constraintLayout, "download_button");

            imageView.setOnClickListener(view -> {

                final var context = view.getContext();
                var downloaderPackageName = SettingsEnum.DOWNLOADER_PACKAGE_NAME.getString();
                if (downloaderPackageName == null) downloaderPackageName = "ussr.razar.youtube_dl";

                boolean packageEnabled = false;
                try {
                    assert context != null;
                    packageEnabled = context.getPackageManager().getApplicationInfo(downloaderPackageName, 0).enabled;
                } catch (PackageManager.NameNotFoundException ignored) {
                }

                // If the package is not installed, show the toast
                if (!packageEnabled) {
                    showToastShort(context, getDownloaderName(context, downloaderPackageName) + " " + str("revanced_downloader_not_installed"));
                    return;
                }

                // Launch PowerTube intent
                try {
                    var content = String.format("https://youtu.be/%s", VideoInformation.getVideoId());

                    var intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.setPackage(downloaderPackageName);
                    intent.putExtra("android.intent.extra.TEXT", content);
                    context.startActivity(intent);

                } catch (Exception error) {
                    LogHelper.printException(Download.class, "Failed to launch the intent", error);
                }

            });
            buttonView = new WeakReference<>(imageView);

            fadeDurationFast = integer("fade_duration_fast");
            fadeDurationScheduled = integer("fade_duration_scheduled");

            fadeIn = anim("fade_in");
            fadeIn.setDuration(fadeDurationFast);

            fadeOut = anim("fade_out");
            fadeOut.setDuration(fadeDurationScheduled);

            isShowing = true;
            changeVisibility(false);

        } catch (Exception e) {
            LogHelper.printException(Download.class, "Unable to set FrameLayout", e);
        }
    }

    private static String getDownloaderName(Context context, String DownloaderPackageName) {
        try {
            final var DOWNLOADER_LABEL_PREFERENCE_KEY = "revanced_downloader_label";
            final var DOWNLOADER_PACKAGE_NAME_PREFERENCE_KEY = "revanced_downloader_package_name";

            String[] labelArray = context.getResources().getStringArray(identifier(DOWNLOADER_LABEL_PREFERENCE_KEY, ResourceType.ARRAY));
            String[] packageNameArray = context.getResources().getStringArray(identifier(DOWNLOADER_PACKAGE_NAME_PREFERENCE_KEY, ResourceType.ARRAY));

            int findIndex = Arrays.binarySearch(packageNameArray, DownloaderPackageName);

            return findIndex >= 0 ? labelArray[findIndex] : DownloaderPackageName;
        } catch (Exception e) {
            LogHelper.printException(Download.class, "Unable to set DownloaderName", e);
        }
        return DownloaderPackageName;
    }

    public static void changeVisibility(boolean currentVisibility) {
        ImageView imageView = buttonView.get();
        if (isShowing == currentVisibility || constraintLayout == null || imageView == null) return;

        isShowing = currentVisibility;
        if (currentVisibility && isButtonEnabled) {
            imageView.setVisibility(View.VISIBLE);
            imageView.startAnimation(fadeIn);
        } else if (imageView.getVisibility() == View.VISIBLE) {
            imageView.startAnimation(fadeOut);
            imageView.setVisibility(View.GONE);
        }
    }

    public static void changeVisibilityNegatedImmediate() {
        changeVisibility(false);
    }

    public static void refreshVisibility() {
        isButtonEnabled = setValue();
    }

    private static boolean setValue() {
        return SettingsEnum.OVERLAY_BUTTON_DOWNLOADS.getBoolean();
    }
}

