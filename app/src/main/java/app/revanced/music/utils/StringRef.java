package app.revanced.music.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StringRef {
    private static Resources resources;
    private static String packageName;

    // must use a thread safe map, as this class is used both on and off the main thread
    private static final Map<String, StringRef> strings = Collections.synchronizedMap(new HashMap<>());

    /**
     * Returns a cached instance.
     * Should be used if the same String could be loaded more than once.
     *
     * @param id string resource name/id
     */
    @NonNull
    public static StringRef sfc(@NonNull String id) {
        StringRef ref = strings.get(id);
        if (ref == null) {
            ref = new StringRef(id);
            strings.put(id, ref);
        }
        return ref;
    }

    /**
     * Gets string value by string id, shorthand for <code>sfc(id).toString()</code>
     *
     * @param id string resource name/id
     * @return String value from string.xml
     */
    @NonNull
    public static String str(@NonNull String id) {
        return sfc(id).toString();
    }

    /**
     * Gets string value by string id, shorthand for <code>sfc(id).toString()</code> and formats the string
     * with given args.
     *
     * @param id   string resource name/id
     * @param args the args to format the string with
     * @return String value from string.xml formatted with given args
     */
    @NonNull
    public static String str(@NonNull String id, Object... args) {
        return String.format(str(id), args);
    }

    @NonNull
    private String value;
    private boolean resolved;

    public StringRef(@NonNull String resName) {
        this.value = resName;
    }

    @Override
    @NonNull
    public String toString() {
        if (!resolved) {
            if (resources == null || packageName == null) {
                Context context = ReVancedUtils.getContext();
                resources = context.getResources();
                packageName = context.getPackageName();
            }
            resolved = true;
            if (resources != null) {
                @SuppressLint("DiscouragedApi") final int identifier = resources.getIdentifier(value, "string", packageName);
                if (identifier == 0)
                    LogHelper.printException(StringRef.class, "Resource not found: " + value);
                else
                    value = resources.getString(identifier);
            } else {
                LogHelper.printException(StringRef.class, "Could not resolve resources!");
            }
        }
        return value;
    }
}
