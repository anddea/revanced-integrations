package app.revanced.integrations.shared.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

@SuppressWarnings("unused")
public class StringSetting extends Setting<String> {

    public StringSetting(String key, String defaultValue) {
        super(key, defaultValue);
    }
    public StringSetting(String key, String defaultValue, boolean rebootApp) {
        super(key, defaultValue, rebootApp);
    }
    public StringSetting(String key, String defaultValue, boolean rebootApp, boolean includeWithImportExport) {
        super(key, defaultValue, rebootApp, includeWithImportExport);
    }
    public StringSetting(String key, String defaultValue, Availability availability) {
        super(key, defaultValue, availability);
    }
    public StringSetting(String key, String defaultValue, boolean rebootApp, Availability availability) {
        super(key, defaultValue, rebootApp, availability);
    }
    public StringSetting(@NonNull String key, @NonNull String defaultValue, boolean rebootApp, boolean includeWithImportExport, @Nullable Availability availability) {
        super(key, defaultValue, rebootApp, includeWithImportExport, availability);
    }

    @Override
    protected void load() {
        value = preferences.getString(key, defaultValue);
    }

    @Override
    protected String readFromJSON(JSONObject json, String importExportKey) throws JSONException {
        return json.getString(importExportKey);
    }

    @Override
    protected void setValueFromString(@NonNull String newValue) {
        value = Objects.requireNonNull(newValue);
    }

    @Override
    public void save(@NonNull String newValue) {
        // Must set before saving to preferences (otherwise importing fails to update UI correctly).
        value = Objects.requireNonNull(newValue);
        preferences.saveString(key, newValue);
    }

    @NonNull
    @Override
    public String get() {
        return value;
    }
}
