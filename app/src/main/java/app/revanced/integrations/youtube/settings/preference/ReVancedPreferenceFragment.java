package app.revanced.integrations.youtube.settings.preference;

import static com.google.android.apps.youtube.app.settings.videoquality.VideoQualitySettingsActivity.setSearchViewVisibility;
import static com.google.android.apps.youtube.app.settings.videoquality.VideoQualitySettingsActivity.setToolbarText;
import static app.revanced.integrations.shared.settings.preference.AbstractPreferenceFragment.showRestartDialog;
import static app.revanced.integrations.shared.settings.preference.AbstractPreferenceFragment.updateListPreferenceSummary;
import static app.revanced.integrations.shared.utils.ResourceUtils.getIdIdentifier;
import static app.revanced.integrations.shared.utils.ResourceUtils.getXmlIdentifier;
import static app.revanced.integrations.shared.utils.StringRef.str;
import static app.revanced.integrations.shared.utils.Utils.getChildView;
import static app.revanced.integrations.shared.utils.Utils.isSDKAbove;
import static app.revanced.integrations.shared.utils.Utils.showToastShort;
import static app.revanced.integrations.youtube.settings.Settings.DEFAULT_PLAYBACK_SPEED;
import static app.revanced.integrations.youtube.settings.Settings.HIDE_PREVIEW_COMMENT;
import static app.revanced.integrations.youtube.settings.Settings.HIDE_PREVIEW_COMMENT_TYPE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.preference.*;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import app.revanced.integrations.shared.settings.BooleanSetting;
import app.revanced.integrations.shared.settings.Setting;
import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.shared.utils.ResourceUtils;
import app.revanced.integrations.shared.utils.Utils;
import app.revanced.integrations.youtube.patches.video.CustomPlaybackSpeedPatch;
import app.revanced.integrations.youtube.utils.ExtendedUtils;
import app.revanced.integrations.youtube.utils.ThemeUtils;

@SuppressWarnings("deprecation")
public class ReVancedPreferenceFragment extends PreferenceFragment {
    private static final int READ_REQUEST_CODE = 42;
    private static final int WRITE_REQUEST_CODE = 43;
    static boolean settingImportInProgress = false;
    static boolean showingUserDialogMessage;

    @SuppressLint("SuspiciousIndentation")
    private final SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, str) -> {
        try {
            if (str == null) return;
            Setting<?> setting = Setting.getSettingFromPath(str);

            if (setting == null) return;

            Preference mPreference = findPreference(str);

            if (mPreference == null) return;

            if (mPreference instanceof SwitchPreference switchPreference) {
                BooleanSetting boolSetting = (BooleanSetting) setting;
                if (settingImportInProgress) {
                    switchPreference.setChecked(boolSetting.get());
                } else {
                    BooleanSetting.privateSetValue(boolSetting, switchPreference.isChecked());
                }

                if (ExtendedUtils.anyMatchSetting(setting)) {
                    ExtendedUtils.setPlayerFlyoutMenuAdditionalSettings();
                } else if (setting.equals(HIDE_PREVIEW_COMMENT) || setting.equals(HIDE_PREVIEW_COMMENT_TYPE)) {
                    ExtendedUtils.setCommentPreviewSettings();
                }
            } else if (mPreference instanceof EditTextPreference editTextPreference) {
                if (settingImportInProgress) {
                    editTextPreference.setText(setting.get().toString());
                } else {
                    Setting.privateSetValueFromString(setting, editTextPreference.getText());
                }
            } else if (mPreference instanceof ListPreference listPreference) {
                if (settingImportInProgress) {
                    listPreference.setValue(setting.get().toString());
                } else {
                    Setting.privateSetValueFromString(setting, listPreference.getValue());
                }

                if (setting.equals(DEFAULT_PLAYBACK_SPEED)) {
                    listPreference.setEntries(CustomPlaybackSpeedPatch.getListEntries());
                    listPreference.setEntryValues(CustomPlaybackSpeedPatch.getListEntryValues());
                }
                updateListPreferenceSummary(listPreference, setting);
            } else {
                Logger.printException(() -> "Setting cannot be handled: " + mPreference.getClass() + " " + mPreference);
                return;
            }

            ReVancedSettingsPreference.initializeReVancedSettings();

            if (settingImportInProgress) {
                return;
            }

            if (!showingUserDialogMessage) {
                final Context context = getActivity();

                if (setting.userDialogMessage != null
                        && mPreference instanceof SwitchPreference switchPreference
                        && setting.defaultValue instanceof Boolean defaultValue
                        && switchPreference.isChecked() != defaultValue) {
                    showSettingUserDialogConfirmation(context, switchPreference, (BooleanSetting) setting);
                } else if (setting.rebootApp) {
                    showRestartDialog(context);
                }
            }
        } catch (Exception ex) {
            Logger.printException(() -> "OnSharedPreferenceChangeListener failure", ex);
        }
    };

    private void showSettingUserDialogConfirmation(Context context, SwitchPreference switchPreference, BooleanSetting setting) {
        Utils.verifyOnMainThread();

        showingUserDialogMessage = true;
        assert setting.userDialogMessage != null;
        new AlertDialog.Builder(context)
                .setTitle(str("revanced_extended_confirm_user_dialog_title"))
                .setMessage(setting.userDialogMessage.toString())
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    if (setting.rebootApp) {
                        showRestartDialog(context);
                    }
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                    switchPreference.setChecked(setting.defaultValue); // Recursive call that resets the Setting value.
                })
                .setOnDismissListener(dialog -> showingUserDialogMessage = false)
                .setCancelable(false)
                .show();
    }

    static PreferenceManager mPreferenceManager;
    private SharedPreferences mSharedPreferences;

    private PreferenceScreen originalPreferenceScreen;

    public ReVancedPreferenceFragment() {
        // Required empty public constructor
    }

    @TargetApi(26)
    public void setPreferenceFragmentToolbar(final String key) {
        PreferenceFragment fragment;
        switch (key) {
            case "revanced_preference_screen_ryd" ->
                    fragment = new ReturnYouTubeDislikePreferenceFragment();
            case "revanced_preference_screen_sb" -> fragment = new SponsorBlockPreferenceFragment();
            default -> {
                Logger.printException(() -> "Unknown key: " + key);
                return;
            }
        }

        final Preference mPreference = mPreferenceManager.findPreference(key);
        if (mPreference == null) {
            return;
        }
        mPreference.setOnPreferenceClickListener(pref -> {
            // Set toolbar text
            setToolbarText(pref.getTitle());

            // Hide the search bar
            setSearchViewVisibility(false);

            getFragmentManager()
                    .beginTransaction()
                    .replace(getIdIdentifier("revanced_settings_fragments"), fragment)
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .commitAllowingStateLoss();
            return false;
        });
    }

    private void putPreferenceScreenMap(SortedMap<String, PreferenceScreen> preferenceScreenMap, PreferenceGroup preferenceGroup) {
        if (preferenceGroup instanceof PreferenceScreen mPreferenceScreen) {
            preferenceScreenMap.put(mPreferenceScreen.getKey(), mPreferenceScreen);
        }
    }

    private void setPreferenceScreenToolbar() {
        SortedMap<String, PreferenceScreen> preferenceScreenMap = new TreeMap<>();

        PreferenceScreen rootPreferenceScreen = getPreferenceScreen();
        for (Preference preference : getAllPreferencesBy(rootPreferenceScreen)) {
            if (!(preference instanceof PreferenceGroup preferenceGroup)) continue;
            putPreferenceScreenMap(preferenceScreenMap, preferenceGroup);
            for (Preference childPreference : getAllPreferencesBy(preferenceGroup)) {
                if (!(childPreference instanceof PreferenceGroup nestedPreferenceGroup)) continue;
                putPreferenceScreenMap(preferenceScreenMap, nestedPreferenceGroup);
                for (Preference nestedPreference : getAllPreferencesBy(nestedPreferenceGroup)) {
                    if (!(nestedPreference instanceof PreferenceGroup childPreferenceGroup))
                        continue;
                    putPreferenceScreenMap(preferenceScreenMap, childPreferenceGroup);
                }
            }
        }

        for (PreferenceScreen mPreferenceScreen : preferenceScreenMap.values()) {
            mPreferenceScreen.setOnPreferenceClickListener(
                    preferenceScreen -> {
                        Dialog preferenceScreenDialog = mPreferenceScreen.getDialog();
                        ViewGroup rootView = (ViewGroup) preferenceScreenDialog
                                .findViewById(android.R.id.content)
                                .getParent();

                        Toolbar toolbar = new Toolbar(preferenceScreen.getContext());

                        toolbar.setTitle(preferenceScreen.getTitle());
                        toolbar.setNavigationIcon(ThemeUtils.getBackButtonDrawable());
                        toolbar.setNavigationOnClickListener(view -> preferenceScreenDialog.dismiss());

                        int margin = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()
                        );

                        if (isSDKAbove(24)) {
                            toolbar.setTitleMargin(margin, 0, margin, 0);
                        } else {
                            // Untested
                            toolbar.setContentInsetsAbsolute(margin, margin);
                        }

                        TextView toolbarTextView = getChildView(toolbar, TextView.class::isInstance);
                        if (toolbarTextView != null) {
                            toolbarTextView.setTextColor(ThemeUtils.getTextColor());
                        }

                        rootView.addView(toolbar, 0);
                        return false;
                    }
            );
        }
    }

    // TODO: SEARCH BAR
    //  - Add ability to search for SB and RYD settings

    // Map to store dependencies: key is the preference key, value is a list of dependent preferences
    private final Map<String, List<Preference>> dependencyMap = new HashMap<>();
    // Set to track already added preferences to avoid duplicates
    private final Set<String> addedPreferences = new HashSet<>();

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            mPreferenceManager = getPreferenceManager();
            mPreferenceManager.setSharedPreferencesName(Setting.preferences.name);
            mSharedPreferences = mPreferenceManager.getSharedPreferences();
            addPreferencesFromResource(getXmlIdentifier("revanced_prefs"));

            // Initialize toolbars and other UI elements
            setPreferenceFragmentToolbar("revanced_preference_screen_ryd");
            setPreferenceFragmentToolbar("revanced_preference_screen_sb");
            setPreferenceScreenToolbar();

            // Initialize ReVanced settings
            ReVancedSettingsPreference.initializeReVancedSettings();

            // Import/export
            setBackupRestorePreference();

            // Store all preferences and their dependencies
            storeAllPreferences(getPreferenceScreen());

            // Load and set initial preferences states
            for (Setting<?> setting : Setting.allLoadedSettings()) {
                final Preference preference = mPreferenceManager.findPreference(setting.key);

                if (preference instanceof SwitchPreference switchPreference) {
                    BooleanSetting boolSetting = (BooleanSetting) setting;
                    switchPreference.setChecked(boolSetting.get());
                } else if (preference instanceof EditTextPreference editTextPreference) {
                    editTextPreference.setText(setting.get().toString());
                } else if (preference instanceof ListPreference listPreference) {
                    if (setting.equals(DEFAULT_PLAYBACK_SPEED)) {
                        listPreference.setEntries(CustomPlaybackSpeedPatch.getListEntries());
                        listPreference.setEntryValues(CustomPlaybackSpeedPatch.getListEntryValues());
                    }
                    updateListPreferenceSummary(listPreference, setting);
                }
            }

            // Register preference change listener
            mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);

            originalPreferenceScreen = getPreferenceManager().createPreferenceScreen(getActivity());
            copyPreferences(getPreferenceScreen(), originalPreferenceScreen);
        } catch (Exception th) {
            Logger.printException(() -> "Error during onCreate()", th);
        }
    }

    private void copyPreferences(PreferenceScreen source, PreferenceScreen destination) {
        for (Preference preference : getAllPreferencesBy(source)) {
            destination.addPreference(preference);
        }
    }

    @Override
    public void onDestroy() {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
        super.onDestroy();
    }

    // Map to store preferences grouped by their parent PreferenceGroup
    private final Map<PreferenceGroup, List<Preference>> groupedPreferences = new LinkedHashMap<>();

    /**
     * Recursively stores all preferences and their dependencies grouped by their parent PreferenceGroup.
     *
     * @param preferenceGroup The preference group to scan.
     */
    private void storeAllPreferences(PreferenceGroup preferenceGroup) {
        Logger.printDebug(() -> "SearchFragmentPrefGroup: " + preferenceGroup);

        // Check if this is the root PreferenceScreen
        boolean isRootScreen = preferenceGroup == getPreferenceScreen();

        // Use the special top-level group only for the root PreferenceScreen
        PreferenceGroup groupKey = isRootScreen
                ? new PreferenceCategory(preferenceGroup.getContext())
                : preferenceGroup;

        if (isRootScreen) {
            groupKey.setTitle(ResourceUtils.getString("revanced_extended_settings_title"));
        }

        // Initialize a list to hold preferences of the current group
        List<Preference> currentGroupPreferences = groupedPreferences.computeIfAbsent(groupKey, k -> new ArrayList<>());

        for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
            Preference preference = preferenceGroup.getPreference(i);

            // Add preference to the current group if not already added
            if (!currentGroupPreferences.contains(preference)) {
                currentGroupPreferences.add(preference);
                Logger.printDebug(() -> "SearchFragment: Stored preference with key: " + preference.getKey() +
                        " in group: " + (isRootScreen ? "Top Level" : preferenceGroup.getTitle()));
            }

            // Store dependencies
            if (preference.getDependency() != null) {
                String dependencyKey = preference.getDependency();
                if (isSDKAbove(24)) {
                    dependencyMap.computeIfAbsent(dependencyKey, k -> new ArrayList<>()).add(preference);
                } else {
                    // Untested
                    if (!dependencyMap.containsKey(dependencyKey)) {
                        dependencyMap.put(dependencyKey, new ArrayList<>() {{
                            add(preference);
                        }});
                    }
                }
                Logger.printDebug(() -> "SearchFragment: Added dependency for key: " + dependencyKey + " on preference: " + preference.getKey());
            }

            // Recursively handle nested PreferenceGroups
            if (preference instanceof PreferenceGroup nestedGroup) {
                storeAllPreferences(nestedGroup);
            }
        }

        Logger.printDebug(() -> "SearchFragmentAllPrefs: " + groupedPreferences);
        Logger.printDebug(() -> "SearchFragmentCurrentGroup: " + currentGroupPreferences);
    }

    /**
     * Filters preferences based on the search query, displaying grouped results with group titles.
     *
     * @param query The search query.
     */
    public void filterPreferences(String query) {
        // If the query is null or empty, reset preferences to their default state
        if (query == null || query.isEmpty()) {
            Logger.printDebug(() -> "SearchFragment: Query is null or empty. Resetting preferences.");
            resetPreferences();
            return;
        }

        // Convert the query to lowercase for case-insensitive search
        query = query.toLowerCase();

        // Get the preference screen to modify
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        // Remove all current preferences from the screen
        preferenceScreen.removeAll();
        // Clear the list of added preferences to start fresh
        addedPreferences.clear();

        // Create a map to store matched preferences for each group
        Map<PreferenceGroup, List<Preference>> matchedGroupPreferences = new LinkedHashMap<>();

        // Create a set to store all keys that should be included
        Set<String> keysToInclude = new HashSet<>();

        // First pass: identify all preferences that match the query and their dependencies
        for (Map.Entry<PreferenceGroup, List<Preference>> entry : groupedPreferences.entrySet()) {
            List<Preference> preferences = entry.getValue();
            for (Preference preference : preferences) {
                if (preferenceMatches(preference, query)) {
                    addPreferenceAndDependencies(preference, keysToInclude);
                }
            }
        }

        // Second pass: add all identified preferences to matchedGroupPreferences
        for (Map.Entry<PreferenceGroup, List<Preference>> entry : groupedPreferences.entrySet()) {
            PreferenceGroup group = entry.getKey();
            List<Preference> preferences = entry.getValue();
            List<Preference> matchedPreferences = new ArrayList<>();

            for (Preference preference : preferences) {
                if (keysToInclude.contains(preference.getKey())) {
                    matchedPreferences.add(preference);
                }
            }

            Logger.printDebug(() -> "SearchFragment: Keys to include: " + keysToInclude);

            if (!matchedPreferences.isEmpty()) {
                matchedGroupPreferences.put(group, matchedPreferences);
            }
        }

        Logger.printDebug(() -> "SearchFragmentMatchedGroupPreferences: " + matchedGroupPreferences);

        // Add matched preferences to the screen, maintaining the original order
        for (Map.Entry<PreferenceGroup, List<Preference>> entry : matchedGroupPreferences.entrySet()) {
            PreferenceGroup group = entry.getKey();
            List<Preference> matchedPreferences = entry.getValue();

            // Add the category for this group
            PreferenceCategory category = new PreferenceCategory(preferenceScreen.getContext());
            category.setTitle(group.getTitle());
            preferenceScreen.addPreference(category);

            Logger.printDebug(() -> "SearchFragment: Adding category: " + group.getTitle());

            // Add matched preferences for this group
            for (Preference preference : matchedPreferences) {
                if (preference.isSelectable()) {
                    addPreferenceWithDependencies(category, preference);
                } else {
                    // For non-selectable preferences, just add them directly
                    category.addPreference(preference);
                    Logger.printDebug(() -> "SearchFragment: Added non-selectable preference: " + preference.getTitle());
                }
            }
        }

        Logger.printDebug(() -> "SearchFragment: Filtered preferences added. Group count: " + matchedGroupPreferences.size());
    }

    /**
     * Checks if a preference matches the given query.
     *
     * @param preference The preference to check.
     * @param query The search query.
     * @return True if the preference matches the query, false otherwise.
     */
    private boolean preferenceMatches(Preference preference, String query) {
        // Check if the title contains the query string
        if (preference.getTitle().toString().toLowerCase().contains(query)) {
            return true;
        }

        // Check if the summary contains the query string
        if (preference.getSummary() != null && preference.getSummary().toString().toLowerCase().contains(query)) {
            return true;
        }

        // Additional checks for SwitchPreference
        if (preference instanceof SwitchPreference switchPreference) {
            CharSequence summaryOn = switchPreference.getSummaryOn();
            CharSequence summaryOff = switchPreference.getSummaryOff();

            if ((summaryOn != null && summaryOn.toString().toLowerCase().contains(query)) ||
                    (summaryOff != null && summaryOff.toString().toLowerCase().contains(query))) {
                return true;
            }
        }

        // Additional checks for ListPreference
        if (preference instanceof ListPreference listPreference) {
            CharSequence[] entries = listPreference.getEntries();
            if (entries != null) {
                for (CharSequence entry : entries) {
                    if (entry.toString().toLowerCase().contains(query)) {
                        return true;
                    }
                }
            }

            CharSequence[] entryValues = listPreference.getEntryValues();
            if (entryValues != null) {
                for (CharSequence entryValue : entryValues) {
                    if (entryValue.toString().toLowerCase().contains(query)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Recursively adds a preference and its dependencies to the set of keys to include.
     *
     * @param preference The preference to add.
     * @param keysToInclude The set of keys to include.
     */
    private void addPreferenceAndDependencies(Preference preference, Set<String> keysToInclude) {
        String key = preference.getKey();
        if (key != null && !keysToInclude.contains(key)) {
            keysToInclude.add(key);

            // Add the preference this one depends on
            String dependencyKey = preference.getDependency();
            if (dependencyKey != null) {
                Preference dependency = findPreferenceInAllGroups(dependencyKey);
                if (dependency != null) {
                    addPreferenceAndDependencies(dependency, keysToInclude);
                }
            }

            // Add preferences that depend on this one
            if (dependencyMap.containsKey(key)) {
                for (Preference dependentPreference : Objects.requireNonNull(dependencyMap.get(key))) {
                    addPreferenceAndDependencies(dependentPreference, keysToInclude);
                }
            }
        }
    }

    /**
     * Recursively adds a preference along with its dependencies
     * (android:dependency attribute in XML).
     *
     * @param preferenceGroup The preference group to add to.
     * @param preference      The preference to add.
     */
    private void addPreferenceWithDependencies(PreferenceGroup preferenceGroup, Preference preference) {
        String key = preference.getKey();

        // Instead of just using preference keys, we combine the category and key to ensure uniqueness
        if (key != null && !addedPreferences.contains(preferenceGroup.getTitle() + ":" + key)) {
            // Add dependencies first
            if (preference.getDependency() != null) {
                String dependencyKey = preference.getDependency();
                Logger.printDebug(() -> "SearchFragment: Adding preference dependency for key: " + dependencyKey);
                Preference dependency = findPreferenceInAllGroups(dependencyKey);
                if (dependency != null) {
                    addPreferenceWithDependencies(preferenceGroup, dependency);
                } else {
                    Logger.printDebug(() -> "SearchFragment: Dependency not found for key: " + dependencyKey);
                    return;
                }
            }

            // Add the preference using a combination of the category and the key
            preferenceGroup.addPreference(preference);
            addedPreferences.add(preferenceGroup.getTitle() + ":" + key); // Track based on both category and key
            Logger.printDebug(() -> "SearchFragment: Added preference with key: " + key);

            // Handle dependent preferences
            if (dependencyMap.containsKey(key)) {
                Logger.printDebug(() -> "SearchFragment: Adding dependent preferences for key: " + key);
                for (Preference dependentPreference : Objects.requireNonNull(dependencyMap.get(key))) {
                    addPreferenceWithDependencies(preferenceGroup, dependentPreference);
                }
            }
        }
    }

    /**
     * Finds a preference in all groups based on its key.
     *
     * @param key The key of the preference to find.
     * @return The found preference, or null if not found.
     */
    private Preference findPreferenceInAllGroups(String key) {
        for (List<Preference> preferences : groupedPreferences.values()) {
            for (Preference preference : preferences) {
                if (preference.getKey() != null && preference.getKey().equals(key)) {
                    return preference;
                }
            }
        }
        return null;
    }

    /**
     * Resets the preference screen to its original state.
     */
    private void resetPreferences() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removeAll();
        for (Preference preference : getAllPreferencesBy(originalPreferenceScreen))
            preferenceScreen.addPreference(preference);

        Logger.printDebug(() -> "SearchFragment: Reset preferences completed.");
    }

    private List<Preference> getAllPreferencesBy(PreferenceGroup preferenceGroup) {
        List<Preference> preferences = new ArrayList<>();
        for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++)
            preferences.add(preferenceGroup.getPreference(i));
        return preferences;
    }

    /**
     * Add Preference to Import/Export settings submenu
     */
    private void setBackupRestorePreference() {
        findPreference("revanced_extended_settings_import").setOnPreferenceClickListener(pref -> {
            importActivity();
            return false;
        });

        findPreference("revanced_extended_settings_export").setOnPreferenceClickListener(pref -> {
            exportActivity();
            return false;
        });
    }

    /**
     * Invoke the SAF(Storage Access Framework) to export settings
     */
    private void exportActivity() {
        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        final String appName = ExtendedUtils.getApplicationLabel();
        final String versionName = ExtendedUtils.getVersionName();
        final String formatDate = dateFormat.format(new Date(System.currentTimeMillis()));
        final String fileName = String.format("%s_v%s_%s.txt", appName, versionName, formatDate);

        final Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    /**
     * Invoke the SAF(Storage Access Framework) to import settings
     */
    private void importActivity() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(isSDKAbove(29) ? "text/plain" : "*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    /**
     * Activity should be done within the lifecycle of PreferenceFragment
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            exportText(data.getData());
        } else if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            importText(data.getData());
        }
    }

    private void exportText(Uri uri) {
        final Context context = this.getActivity();

        try {
            @SuppressLint("Recycle")
            FileWriter jsonFileWriter =
                    new FileWriter(
                            Objects.requireNonNull(context.getApplicationContext()
                                            .getContentResolver()
                                            .openFileDescriptor(uri, "w"))
                                    .getFileDescriptor()
                    );
            PrintWriter printWriter = new PrintWriter(jsonFileWriter);
            printWriter.write(Setting.exportToJson(context));
            printWriter.close();
            jsonFileWriter.close();

            showToastShort(str("revanced_extended_settings_export_success"));
        } catch (IOException e) {
            showToastShort(str("revanced_extended_settings_export_failed"));
        }
    }

    private void importText(Uri uri) {
        final Context context = this.getActivity();
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            settingImportInProgress = true;

            @SuppressLint("Recycle")
            FileReader fileReader =
                    new FileReader(
                            Objects.requireNonNull(context.getApplicationContext()
                                            .getContentResolver()
                                            .openFileDescriptor(uri, "r"))
                                    .getFileDescriptor()
                    );
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            bufferedReader.close();
            fileReader.close();

            final boolean restartNeeded = Setting.importFromJSON(sb.toString(), true);
            if (restartNeeded) {
                showRestartDialog(getActivity());
            }
        } catch (IOException e) {
            showToastShort(str("revanced_extended_settings_import_failed"));
            throw new RuntimeException(e);
        } finally {
            settingImportInProgress = false;
        }
    }
}
