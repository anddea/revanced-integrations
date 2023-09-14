package app.revanced.music.sponsorblock.objects;

import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

public class SponsorBlockEditTextPreference {

    private static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static void editTextDialogBuilder(Activity base) {
        try {
            SettingsEnum api = SettingsEnum.SB_API_URL;
            TextInputLayout textInputLayout = new TextInputLayout(base);

            final EditText textView = new EditText(base);
            textView.setHint(api.getString());
            textView.setText(api.getString());

            FrameLayout container = new FrameLayout(base);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            int left_margin = dpToPx(20, base.getResources());
            int top_margin = dpToPx(10, base.getResources());
            int right_margin = dpToPx(20, base.getResources());
            int bottom_margin = dpToPx(4, base.getResources());
            params.setMargins(left_margin, top_margin, right_margin, bottom_margin);

            textInputLayout.setLayoutParams(params);

            textInputLayout.addView(textView);
            container.addView(textInputLayout);

            final AlertDialog.Builder builder = new AlertDialog.Builder(base, android.R.style.Theme_DeviceDefault_Dialog_Alert);

            builder.setTitle(str("sb_api_url"))
                    .setView(container)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setNeutralButton(str("revanced_reset"), (dialog, which) -> {
                        api.saveValue(api.defaultValue);
                        ReVancedUtils.showToastShort(str("sb_api_url_reset"));
                    })
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        String serverAddress = textView.getText().toString().trim();
                        if (!isValidSBServerAddress(serverAddress)) {
                            ReVancedUtils.showToastShort(str("sb_api_url_invalid"));
                        } else if (!serverAddress.equals(SettingsEnum.SB_API_URL.getString())) {
                            api.saveValue(serverAddress);
                            ReVancedUtils.showToastShort(str("sb_api_url_changed"));
                        }
                    })
                    .show();
        } catch (Exception ex) {
            LogHelper.printException(SponsorBlockEditTextPreference.class, "editTextDialogBuilder failure", ex);
        }
    }

    private static boolean isValidSBServerAddress(@NonNull String serverAddress) {
        if (!Patterns.WEB_URL.matcher(serverAddress).matches()) {
            return false;
        }
        // Verify url is only the server address and does not contain a path such as: "https://sponsor.ajay.app/api/"
        // Could use Patterns.compile, but this is simpler
        final int lastDotIndex = serverAddress.lastIndexOf('.');
        return lastDotIndex == -1 || !serverAddress.substring(lastDotIndex).contains("/");
        // Optionally, could also verify the domain exists using "InetAddress.getByName(serverAddress)"
        // but that should not be done on the main thread.
        // Instead, assume the domain exists and the user knows what they're doing.
    }

}
