package app.revanced.integrations.youtube.settings.preference;

import static app.revanced.integrations.shared.utils.StringRef.str;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Arrays;

import app.revanced.integrations.shared.settings.StringSetting;
import app.revanced.integrations.shared.utils.ResourceUtils;
import app.revanced.integrations.shared.utils.Utils;
import app.revanced.integrations.youtube.settings.Settings;
import app.revanced.integrations.youtube.utils.ExtendedUtils;

/**
 * @noinspection all
 */
public class ExternalDownloaderPreference extends Preference implements Preference.OnPreferenceClickListener {

    private static final StringSetting settings = Settings.EXTERNAL_DOWNLOADER_PACKAGE_NAME;
    private static final String[] mEntries = ResourceUtils.getStringArray("revanced_external_downloader_label");
    private static final String[] mEntryValues = ResourceUtils.getStringArray("revanced_external_downloader_package_name");
    private static final String[] mWebsiteEntries = ResourceUtils.getStringArray("revanced_external_downloader_website");
    private static EditText mEditText;
    private static String packageName;
    private static int mClickedDialogEntryIndex;

    private final TextWatcher textWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            packageName = s.toString();
            mClickedDialogEntryIndex = Arrays.asList(mEntryValues).indexOf(packageName);
        }
    };

    private void init() {
        setSelectable(true);
        setOnPreferenceClickListener(this);
    }

    public ExternalDownloaderPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ExternalDownloaderPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ExternalDownloaderPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExternalDownloaderPreference(Context context) {
        super(context);
        init();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        packageName = settings.get().toString();
        mClickedDialogEntryIndex = Arrays.asList(mEntryValues).indexOf(packageName);

        final Context context = getContext();
        AlertDialog.Builder builder = Utils.getEditTextDialogBuilder(context);

        TableLayout table = new TableLayout(context);
        table.setOrientation(LinearLayout.HORIZONTAL);
        table.setPadding(15, 0, 15, 0);

        TableRow row = new TableRow(context);

        mEditText = new EditText(context);
        mEditText.setHint(settings.defaultValue);
        mEditText.setText(packageName);
        mEditText.addTextChangedListener(textWatcher);
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PT, 9);
        mEditText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(mEditText);

        table.addView(row);
        builder.setView(table);

        builder.setTitle(str("revanced_external_downloader_dialog_title"));
        builder.setSingleChoiceItems(mEntries, mClickedDialogEntryIndex, (dialog, which) -> {
            mClickedDialogEntryIndex = which;
            mEditText.setText(mEntryValues[which].toString());
        });
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            final String packageName = mEditText.getText().toString().trim();
            settings.save(packageName);
            checkPackageIsValid(context, packageName);
            dialog.dismiss();
        });
        builder.setNeutralButton(str("revanced_extended_settings_reset"), (dialog, which) -> settings.resetToDefault());
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.show();

        return true;
    }

    private static boolean checkPackageIsValid(Context context, String packageName) {
        String appName = "";
        String website = "";
        if (mClickedDialogEntryIndex >= 0) {
            appName = mEntries[mClickedDialogEntryIndex].toString();
            website = mWebsiteEntries[mClickedDialogEntryIndex].toString();
            return showToastOrOpenWebsites(context, appName, packageName, website);
        } else {
            return showToastOrOpenWebsites(context, appName, packageName, website);
        }
    }

    private static boolean showToastOrOpenWebsites(Context context, String appName, String packageName, String website) {
        if (ExtendedUtils.isPackageEnabled(packageName))
            return true;

        if (website.isEmpty()) {
            Utils.showToastShort(str("revanced_external_downloader_not_installed_warning", packageName));
            return false;
        }

        new AlertDialog.Builder(context)
                .setTitle(str("revanced_external_downloader_not_installed_dialog_title"))
                .setMessage(str("revanced_external_downloader_not_installed_dialog_message", appName, appName))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                    context.startActivity(i);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();

        return false;
    }

    public static boolean checkPackageIsEnabled() {
        final Context context = Utils.getActivity();
        packageName = settings.get().toString();
        mClickedDialogEntryIndex = Arrays.asList(mEntryValues).indexOf(packageName);
        return checkPackageIsValid(context, packageName);
    }

    public static boolean checkPackageIsEnabled(String packageName) {
        final Context context = Utils.getActivity();
        mClickedDialogEntryIndex = Arrays.asList(mEntryValues).indexOf(packageName);
        return checkPackageIsValid(context, packageName);
    }

}