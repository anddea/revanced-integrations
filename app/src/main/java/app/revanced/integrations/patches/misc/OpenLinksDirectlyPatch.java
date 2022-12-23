package app.revanced.integrations.patches.misc;

import app.revanced.integrations.settings.SettingsEnum;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenLinksDirectlyPatch {

    public static String enableBypassRedirect(String uri) {
        if (SettingsEnum.ENABLE_OPEN_LINKS_DIRECTLY.getBoolean()){
            Matcher matcher = Pattern.compile("&q=(http.+?)&v=").matcher(uri);
            return matcher.find() ? URLDecoder.decode(matcher.group(1)) : uri;
        }
        return uri;
    }
}
