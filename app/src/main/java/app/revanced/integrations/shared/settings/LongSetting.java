package app.revanced.integrations.shared.settings;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

@SuppressWarnings("unused")
public class LongSetting extends Setting<Long> {

    public LongSetting(String key, Long defaultValue) {
        super(key, defaultValue);
    }
    public LongSetting(String key, Long defaultValue, boolean rebootApp) {
        super(key, defaultValue, rebootApp);
    }
    public LongSetting(String key, Long defaultValue, boolean rebootApp, boolean includeWithImportExport) {
        super(key, defaultValue, rebootApp, includeWithImportExport);
    }
    public LongSetting(String key, Long defaultValue, Availability availability) {
        super(key, defaultValue, availability);
    }
    public LongSetting(String key, Long defaultValue, boolean rebootApp, Availability availability) {
        super(key, defaultValue, rebootApp, availability);
    }

    @Override
    protected void load() {
        value = preferences.getLongString(key, defaultValue);
    }

    @Override
    protected Long readFromJSON(JSONObject json, String importExportKey) throws JSONException {
        return json.getLong(importExportKey);
    }

    @Override
    protected void setValueFromString(@NonNull String newValue) {
        value = Long.valueOf(Objects.requireNonNull(newValue));
    }

    @Override
    public void save(@NonNull Long newValue) {
        // Must set before saving to preferences (otherwise importing fails to update UI correctly).
        value = Objects.requireNonNull(newValue);
        preferences.saveLongString(key, newValue);
    }

    @NonNull
    @Override
    public Long get() {
        return value;
    }
}
