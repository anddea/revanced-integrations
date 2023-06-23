package app.revanced.reddit.settings;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static app.revanced.reddit.settings.SettingsEnum.ReturnType.BOOLEAN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import app.revanced.reddit.utils.StringRef;

public enum SettingsEnum {
    ENABLE_DEBUG_LOGGING("revanced_enable_debug_logging", BOOLEAN, FALSE), // must be first value, otherwise logging during loading will not work

    ENABLE_SANITIZE_URL_QUERY("revanced_enable_sanitize_url_query", BOOLEAN, TRUE);

    @NonNull
    public final String path;

    @NonNull
    public final Object defaultValue;
    @NonNull
    public final SharedPrefCategory sharedPref;
    @NonNull
    public final ReturnType returnType;
    /**
     * If the app should be rebooted, if this setting is changed
     */
    public final boolean rebootApp;

    @Nullable
    public final StringRef title;

    @Nullable
    public final StringRef summary;

    // Must be volatile, as some settings are read/write from different threads.
    // Of note, the object value is persistently stored using SharedPreferences (which is thread safe).
    @NonNull
    private Object value;

    SettingsEnum(String path, ReturnType returnType, Object defaultValue) {
        this(path, returnType, defaultValue, SharedPrefCategory.REDDIT, false);
    }

    SettingsEnum(String path, ReturnType returnType, Object defaultValue, boolean rebootApp) {
        this(path, returnType, defaultValue, SharedPrefCategory.REDDIT, rebootApp);
    }

    SettingsEnum(String path, ReturnType returnType, Object defaultValue, SharedPrefCategory prefName, boolean rebootApp) {
        this.path = Objects.requireNonNull(path);
        this.returnType = Objects.requireNonNull(returnType);
        this.value = this.defaultValue = Objects.requireNonNull(defaultValue);
        this.sharedPref = Objects.requireNonNull(prefName);
        this.rebootApp = rebootApp;
        this.title = new StringRef(path + "_title");
        this.summary = new StringRef(path + "_summary");
    }

    private static final Map<String, SettingsEnum> pathToSetting = new HashMap<>(2* values().length);

    static {
        loadAllSettings();

        for (SettingsEnum setting : values()) {
            pathToSetting.put(setting.path, setting);
        }
    }

    @Nullable
    public static SettingsEnum settingFromPath(@NonNull String str) {
        return pathToSetting.get(str);
    }

    private static void loadAllSettings() {
        for (SettingsEnum setting : values()) {
            setting.load();
        }
    }

    private void load() {
        switch (returnType) {
            case BOOLEAN -> value = sharedPref.getBoolean(path, (boolean) defaultValue);
            case INTEGER -> value = sharedPref.getIntegerString(path, (Integer) defaultValue);
            case LONG -> value = sharedPref.getLongString(path, (Long) defaultValue);
            case FLOAT -> value = sharedPref.getFloatString(path, (Float) defaultValue);
            case STRING -> value = sharedPref.getString(path, (String) defaultValue);
            default -> throw new IllegalStateException(name());
        }
    }

    /**
     * Sets the value, and persistently saves it.
     */
    public void saveValue(@NonNull Object newValue) {
        returnType.validate(newValue);
        value = newValue; // Must set before saving to preferences (otherwise importing fails to update UI correctly).
        switch (returnType) {
            case BOOLEAN -> sharedPref.saveBoolean(path, (boolean) newValue);
            case INTEGER -> sharedPref.saveIntegerString(path, (Integer) newValue);
            case LONG -> sharedPref.saveLongString(path, (Long) newValue);
            case FLOAT -> sharedPref.saveFloatString(path, (Float) newValue);
            case STRING -> sharedPref.saveString(path, (String) newValue);
            default -> throw new IllegalStateException(name());
        }
    }

    public boolean getBoolean() {
        return (Boolean) value;
    }

    public int getInt() {
        return (Integer) value;
    }

    public long getLong() {
        return (Long) value;
    }

    public float getFloat() {
        return (Float) value;
    }

    @NonNull
    public String getString() {
        return (String) value;
    }

    @NonNull
    public String getSummary() {
        if (summary == null)
            return "";
        return summary.toString();
    }

    @NonNull
    public String getTitle() {
        if (title == null)
            return "";
        return title.toString();
    }

    /**
     * @return the value of this setting as as generic object type.
     */
    @NonNull
    public Object getObjectValue() {
        return value;
    }

    public enum ReturnType {
        BOOLEAN,
        INTEGER,
        LONG,
        FLOAT,
        STRING;

        public void validate(@Nullable Object obj) throws IllegalArgumentException {
            if (!matches(obj)) {
                throw new IllegalArgumentException("'" + obj + "' does not match:" + this);
            }
        }

        public boolean matches(@Nullable Object obj) {
            return switch (this) {
                case BOOLEAN -> obj instanceof Boolean;
                case INTEGER -> obj instanceof Integer;
                case LONG -> obj instanceof Long;
                case FLOAT -> obj instanceof Float;
                case STRING -> obj instanceof String;
            };
        }
    }
}
