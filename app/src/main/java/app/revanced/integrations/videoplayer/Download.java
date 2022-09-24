package app.revanced.integrations.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


import java.lang.ref.WeakReference;

import app.revanced.integrations.videoplayer.VideoInformation;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.sponsorblock.StringRef;
import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.ReVancedUtils;
import app.revanced.integrations.utils.SharedPrefHelper;

/* loaded from: classes6.dex */
//ToDo: Refactor
public class Download {
    static WeakReference<ImageView> _button = new WeakReference<>(null);
    static ConstraintLayout _constraintLayout;
    static int fadeDurationFast;
    static int fadeDurationScheduled;
    static Animation fadeIn;
    static Animation fadeOut;
    public static boolean isDownloadButtonEnabled;
    static boolean isShowing;

    public static void initializeDownloadButton(Object obj) {
        try {
            LogHelper.debug(Download.class, "initializing");
            _constraintLayout = (ConstraintLayout) obj;
            isDownloadButtonEnabled = shouldBeShown();
            ImageView imageView = _constraintLayout.findViewById(getIdentifier("download_button", "id"));
            if (imageView == null) {
                LogHelper.debug(Download.class, "Couldn't find imageView with id \"download_button\"");
                return;
            }

            imageView.setOnClickListener(view -> {
                LogHelper.debug(Download.class, "Download button clicked");

                final var context = view.getContext();
                String downloaderPackageName = SettingsEnum.DOWNLOADS_PACKAGE_NAME.getString().isEmpty() ? StringRef.str("revanced_default_downloader") : SettingsEnum.DOWNLOADS_PACKAGE_NAME.getString();

                boolean packageEnabled = false;
                try {
                    assert context != null;
                    packageEnabled = context.getPackageManager().getApplicationInfo(downloaderPackageName, 0).enabled;
                } catch (PackageManager.NameNotFoundException error) {
                    LogHelper.debug(Download.class, "Downloader could not be found: " + error);
                }

                // If the package is not installed, show the toast
                if (!packageEnabled) {
                    Toast.makeText(context, getDownloaderName(downloaderPackageName) + " " + StringRef.str("downloader_not_installed_warning"), Toast.LENGTH_LONG).show();
                    return;
                }

                // Launch PowerTube intent
                try {
                    String content = String.format("https://youtu.be/%s", VideoInformation.currentVideoId);

                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.setPackage(downloaderPackageName);
                    intent.putExtra("android.intent.extra.TEXT", content);
                    context.startActivity(intent);

                    LogHelper.debug(Download.class, "Launched the intent with the content: " + content);
                } catch (Exception error) {
                    LogHelper.debug(Download.class, "Failed to launch the intent: " + error);
                }

                //var options = Arrays.asList("Video", "Audio").toArray(new CharSequence[0]);
                //
                //new AlertDialog.Builder(view.getContext())
                //        .setItems(options, (dialog, which) -> {
                //            LogHelper.debug(Download.class, String.valueOf(options[which]));
                //        })
                //        .show();
                // TODO: show popup and download via newpipe
            });
            _button = new WeakReference<>(imageView);
            fadeDurationFast = getInteger("fade_duration_fast");
            fadeDurationScheduled = getInteger("fade_duration_scheduled");
            Animation animation = getAnimation("fade_in");
            fadeIn = animation;
            animation.setDuration(fadeDurationFast);
            Animation animation2 = getAnimation("fade_out");
            fadeOut = animation2;
            animation2.setDuration(fadeDurationScheduled);
            isShowing = true;
            changeVisibility(false);

        } catch (Exception e) {
            LogHelper.printException(Download.class, "Unable to set FrameLayout", e);
        }
    }

    public static String getDownloaderName(String DownloaderPackageName) {
        String[] DownloaderNameList = {"PowerTube", "NewPipe", "NewPipe x SponsorBlock", "SnapTube"};
        String[] DownloaderPackageNameList = {"ussr.razar.youtube_dl", "org.schabi.newpipe", "org.polymorphicshade.newpipe", "com.snaptube.premium"};

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

    public static void changeVisibility(boolean z) {
        if (isShowing == z) return;

        isShowing = z;
        ImageView imageView = _button.get();
        if (_constraintLayout != null && imageView != null) {
            if (z && isDownloadButtonEnabled) {
                LogHelper.debug(Download.class, "Fading in");
                imageView.setVisibility(View.VISIBLE);
                imageView.startAnimation(fadeIn);
            } else if (imageView.getVisibility() == View.VISIBLE) {
                LogHelper.debug(Download.class, "Fading out");
                imageView.startAnimation(fadeOut);
                imageView.setVisibility(View.GONE);
            }
        }
    }

    public static void refreshShouldBeShown() {
        isDownloadButtonEnabled = shouldBeShown();
    }

    private static boolean shouldBeShown() {
        if (!SettingsEnum.DOWNLOADS_BUTTON_SHOWN.getBoolean()) {
            return false;
        }

        Context appContext = ReVancedUtils.getContext();
        if (appContext == null) {
            LogHelper.printException(Download.class, "shouldBeShown - context is null!");
            return false;
        }
        String string = SharedPrefHelper.getString(appContext, SharedPrefHelper.SharedPrefNames.YOUTUBE, "pref_download_button_list", "PLAYER" /* TODO: set the default to null, as this will be set by the settings page later */);
        if (string == null || string.isEmpty()) {
            return false;
        }
        return string.equalsIgnoreCase("PLAYER");
    }

    private static int getIdentifier(String str, String str2) {
        Context appContext = ReVancedUtils.getContext();
        return appContext.getResources().getIdentifier(str, str2, appContext.getPackageName());
    }

    private static int getInteger(String str) {
        return ReVancedUtils.getContext().getResources().getInteger(getIdentifier(str, "integer"));
    }
    private static Animation getAnimation(String str) {
        return AnimationUtils.loadAnimation(ReVancedUtils.getContext(), getIdentifier(str, "anim"));
    }
}

