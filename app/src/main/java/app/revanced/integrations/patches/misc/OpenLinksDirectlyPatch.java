package app.revanced.integrations.patches.misc;

import android.net.Uri;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.revanced.integrations.settings.SettingsEnum;

public class OpenLinksDirectlyPatch {

    public static Uri enableBypassRedirect(String uri) {
        if (SettingsEnum.ENABLE_OPEN_LINKS_DIRECTLY.getBoolean()){
            Matcher matcher = Pattern.compile("&q=(http.+?)&html_redirect=").matcher(uri);
            if (matcher.find()) uri = URLDecoder.decode(matcher.group(1)).split("&v=")[0];
        }
        return Uri.parse(uri);
    }
}
