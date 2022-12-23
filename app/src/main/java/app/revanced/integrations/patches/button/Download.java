package app.revanced.integrations.patches.button;

import static app.revanced.integrations.utils.ResourceUtils.anim;
import static app.revanced.integrations.utils.ResourceUtils.findView;
import static app.revanced.integrations.utils.ResourceUtils.integer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import app.revanced.integrations.patches.video.VideoInformation;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;
import app.revanced.integrations.utils.StringRef;

public class Download {
    static WeakReference<ImageView> buttonview = new WeakReference<>(null);
    @SuppressLint("StaticFieldLeak")
    static ConstraintLayout constraintLayout;
    static int fadeDurationFast;
    static int fadeDurationScheduled;
    static Animation fadeIn;
    static Animation fadeOut;
    public static boolean isButtonEnabled;
    static boolean isShowing;
    public static final String[] DownloaderNameList = {"PowerTube", "NewPipe", "NewPipe x SponsorBlock", "Seal", "SnapTube"};
    public static final String[] DownloaderPackageNameList = {"ussr.razar.youtube_dl", "org.schabi.newpipe", "org.polymorphicshade.newpipe", "com.junkfood.seal", "com.snaptube.premium"};

    public static void initialize(Object obj) {
        try {
            constraintLayout = (ConstraintLayout) obj;
            isButtonEnabled = setValue();
            ImageView imageView = findView(Download.class, constraintLayout, "download_button");
            if (imageView == null) return;

            imageView.setOnClickListener(view -> {

                final var context = view.getContext();
                String downloaderPackageName = SettingsEnum.DOWNLOADER_PACKAGE_NAME.getString() == null ? "ussr.razar.youtube_dl" : SettingsEnum.DOWNLOADER_PACKAGE_NAME.getString();

                boolean packageEnabled = false;
                try {
                    assert context != null;
                    packageEnabled = context.getPackageManager().getApplicationInfo(downloaderPackageName, 0).enabled;
                } catch (PackageManager.NameNotFoundException error) {
                }

                // If the package is not installed, show the toast
                if (!packageEnabled) {
                    Toast.makeText(context, getDownloaderName(downloaderPackageName) + " " + StringRef.str("revanced_downloader_not_installed"), Toast.LENGTH_LONG).show();
                    return;
                }

                // Launch PowerTube intent
                try {
                    var content = String.format("https://youtu.be/%s", VideoInformation.getCurrentVideoId());

                    var intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.setPackage(downloaderPackageName);
                    intent.putExtra("android.intent.extra.TEXT", content);
                    context.startActivity(intent);

                } catch (Exception error) {
                    LogHelper.printException(Download.class, "Failed to launch the intent", error);
                }

            });
            buttonview = new WeakReference<>(imageView);

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

    public static String getDownloaderName(String DownloaderPackageName) {
        try {
            for (int i = 0; i < DownloaderNameList.length ; i++) {
                if (DownloaderPackageNameList[i].equals(DownloaderPackageName)){
                    return DownloaderNameList[i];
                }
            }
        } catch (Exception e) {
            LogHelper.printException(Download.class, "Unable to set DownloaderName", e);
        }
        return DownloaderPackageName;
    }

    public static void changeVisibility(boolean currentVisibility) {
        ImageView imageView = buttonview.get();
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

    public static void refreshVisibility() {
        isButtonEnabled = setValue();
    }

    private static boolean setValue() {
        return SettingsEnum.OVERLAY_BUTTON_DOWNLOADS.getBoolean();
    }
}

