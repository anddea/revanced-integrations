package app.revanced.music.sponsorblock.objects;

import static app.revanced.music.utils.ReVancedHelper.getDialogBuilder;
import static app.revanced.music.utils.ReVancedHelper.getLayoutParams;
import static app.revanced.music.utils.StringRef.str;

import android.app.Activity;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

public class SponsorBlockEditTextDialogBuilder {

    public static void editTextDialogBuilder(@NonNull Activity activity) {
        try {
            SettingsEnum api = SettingsEnum.SB_API_URL;

            final EditText textView = new EditText(activity);
            textView.setHint(api.getString());
            textView.setText(api.getString());

            TextInputLayout textInputLayout = new TextInputLayout(activity);
            textInputLayout.setLayoutParams(getLayoutParams(activity));
            textInputLayout.addView(textView);

            FrameLayout container = new FrameLayout(activity);
            container.addView(textInputLayout);

            getDialogBuilder(activity)
                    .setTitle(str("sb_api_url"))
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
            LogHelper.printException(() -> "editTextDialogBuilder failure", ex);
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
