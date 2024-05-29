package app.revanced.integrations.shared.settings;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

@SuppressWarnings("unused")
public class IntegerSetting extends Setting<Integer> {

    public IntegerSetting(String key, Integer defaultValue) {
        super(key, defaultValue);
    }
    public IntegerSetting(String key, Integer defaultValue, boolean rebootApp) {
        super(key, defaultValue, rebootApp);
    }
    public IntegerSetting(String key, Integer defaultValue, boolean rebootApp, boolean includeWithImportExport) {
        super(key, defaultValue, rebootApp, includeWithImportExport);
    }
    public IntegerSetting(String key, Integer defaultValue, Availability availability) {
        super(key, defaultValue, availability);
    }
    public IntegerSetting(String key, Integer defaultValue, boolean rebootApp, Availability availability) {
        super(key, defaultValue, rebootApp, availability);
    }

    @Override
    protected void load() {
        value = preferences.getIntegerString(key, defaultValue);
    }

    @Override
    protected Integer readFromJSON(JSONObject json, String importExportKey) throws JSONException {
        return json.getInt(importExportKey);
    }

    @Override
    protected void setValueFromString(@NonNull String newValue) {
        value = Integer.valueOf(Objects.requireNonNull(newValue));
    }

    @Override
    public void save(@NonNull Integer newValue) {
        // Must set before saving to preferences (otherwise importing fails to update UI correctly).
        value = Objects.requireNonNull(newValue);
        preferences.saveIntegerString(key, newValue);
    }

    @NonNull
    @Override
    public Integer get() {
        return value;
    }
}
