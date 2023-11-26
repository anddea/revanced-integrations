package app.revanced.music.settingsmenu;

import static app.revanced.music.utils.ReVancedHelper.getDialogBuilder;
import static app.revanced.music.utils.ReVancedHelper.getLayoutParams;
import static app.revanced.music.utils.ReVancedHelper.getStringArray;
import static app.revanced.music.utils.ReVancedUtils.showToastShort;
import static app.revanced.music.utils.StringRef.str;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.InputType;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Objects;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedHelper;
import app.revanced.music.utils.ReVancedUtils;

public class ImportExportDialogBuilder extends PreferenceFragment {
    private static final String IMPORT_EXPORT_SETTINGS_ENTRY_KEY = "revanced_extended_settings_import_export_entry";
    private static String existingSettings;

    private static final int READ_REQUEST_CODE = 42;
    private static final int WRITE_REQUEST_CODE = 43;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listDialogBuilder();
    }

    private void listDialogBuilder() {
        try {
            final Activity activity = getActivity();
            final String[] mEntries = getStringArray(activity, IMPORT_EXPORT_SETTINGS_ENTRY_KEY);

            getDialogBuilder(activity)
                    .setTitle(str("revanced_extended_settings_import_export_title"))
                    .setItems(mEntries, (dialog, index) -> {
                        switch (index) {
                            case 0 -> exportActivity();
                            case 1 -> importActivity();
                            case 2 -> editTextDialogBuilder();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        } catch (Exception ex) {
            LogHelper.printException(() -> "listDialogBuilder failure", ex);
        }
    }

    /**
     * Invoke the SAF(Storage Access Framework) to export settings
     */
    private void exportActivity() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        var appName = ReVancedHelper.applicationLabel;
        var versionName = ReVancedHelper.appVersionName;
        var formatDate = dateFormat.format(new Date(System.currentTimeMillis()));
        var fileName = String.format("%s_v%s_%s.json", appName, versionName, formatDate);

        var intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    /**
     * Invoke the SAF(Storage Access Framework) to import settings
     */
    private void importActivity() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(Build.VERSION.SDK_INT <= 28 ? "*/*" : "application/json");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            exportJson(data.getData());
        } else if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            importJson(data.getData());
        }
    }

    private void exportJson(Uri uri) {
        Context context = this.getContext();

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
            printWriter.write(SettingsEnum.exportJSON());
            printWriter.close();
            jsonFileWriter.close();

            showToastShort(context, str("revanced_extended_settings_export_success"));
        } catch (IOException e) {
            showToastShort(context, str("revanced_extended_settings_export_failed"));
        }
    }

    private void importJson(Uri uri) {
        final Context context = this.getContext();
        StringBuilder sb = new StringBuilder();
        String line;

        try {
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

            final boolean rebootNeeded = SettingsEnum.importJSON(sb.toString());
            if (rebootNeeded) {
                SharedPreferenceChangeListener.rebootDialog();
            }
        } catch (IOException e) {
            showToastShort(context, str("revanced_extended_settings_import_failed"));
            throw new RuntimeException(e);
        }
    }

    private void editTextDialogBuilder() {
        try {
            final Activity activity = getActivity();
            final EditText textView = new EditText(activity);
            existingSettings = SettingsEnum.exportJSON();
            textView.setText(existingSettings);
            textView.setInputType(textView.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8); // Use a smaller font to reduce text wrap.

            TextInputLayout textInputLayout = new TextInputLayout(activity);
            textInputLayout.setLayoutParams(getLayoutParams(activity));
            textInputLayout.addView(textView);

            FrameLayout container = new FrameLayout(activity);
            container.addView(textInputLayout);

            getDialogBuilder(activity)
                    .setTitle(str("revanced_extended_settings_import_export_title"))
                    .setView(container)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setNeutralButton(str("revanced_extended_settings_import_copy"), (dialog, which) -> ReVancedUtils.setClipboard(textView.getText().toString(), str("revanced_share_copy_settings_success")))
                    .setPositiveButton(str("revanced_extended_settings_import"), (dialog, which) -> importSettings(textView.getText().toString()))
                    .show();
        } catch (Exception ex) {
            LogHelper.printException(() -> "editTextDialogBuilder failure", ex);
        }
    }

    private void importSettings(String replacementSettings) {
        try {
            existingSettings = SettingsEnum.exportJSON();
            if (replacementSettings.equals(existingSettings)) {
                return;
            }
            final boolean rebootNeeded = SettingsEnum.importJSON(replacementSettings);
            if (rebootNeeded) {
                SharedPreferenceChangeListener.rebootDialog();
            }
        } catch (Exception ex) {
            LogHelper.printException(() -> "importSettings failure", ex);
        }
    }
}
