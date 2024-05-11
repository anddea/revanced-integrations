package com.google.android.apps.youtube.app.settings.videoquality;

import static app.revanced.integrations.shared.utils.ResourceUtils.getIdIdentifier;
import static app.revanced.integrations.shared.utils.ResourceUtils.getLayoutIdentifier;
import static app.revanced.integrations.shared.utils.Utils.getChildView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.Objects;

import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.shared.utils.ResourceUtils;
import app.revanced.integrations.shared.utils.Utils;
import app.revanced.integrations.youtube.settings.preference.ReVancedPreferenceFragment;
import app.revanced.integrations.youtube.utils.ThemeUtils;

/**
 * @noinspection ALL
 */
public class VideoQualitySettingsActivity extends Activity {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Utils.getLocalizedContextAndSetResources(base));
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            setTheme(ThemeUtils.getThemeId());
            setContentView(getLayoutIdentifier("revanced_settings_with_toolbar"));

            PreferenceFragment fragment;
            String toolbarTitleResourceName;
            String dataString = Objects.requireNonNull(getIntent().getDataString());
            switch (dataString) {
                case "revanced_extended_settings_intent" -> {
                    fragment = new ReVancedPreferenceFragment();
                    toolbarTitleResourceName = "revanced_extended_settings_title";
                }
                default -> {
                    Logger.printException(() -> "Unknown setting: " + dataString);
                    return;
                }
            }

            setToolbar(toolbarTitleResourceName);

            final int fragmentId = getIdIdentifier("revanced_settings_fragments");
            getFragmentManager()
                    .beginTransaction()
                    .replace(fragmentId, fragment)
                    .commit();
        } catch (Exception ex) {
            Logger.printException(() -> "onCreate failure", ex);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setToolbar(String toolbarTitleResourceName) {
        final ViewGroup toolBarParent = Objects.requireNonNull(findViewById(getIdIdentifier("revanced_toolbar_parent")));
        Toolbar toolbar = new Toolbar(toolBarParent.getContext());
        toolbar.setBackgroundColor(ThemeUtils.getToolbarBackgroundColor());
        toolbar.setNavigationIcon(ThemeUtils.getBackButtonDrawable());
        toolbar.setNavigationOnClickListener(view -> VideoQualitySettingsActivity.this.onBackPressed());
        toolbar.setTitle(ResourceUtils.getString(toolbarTitleResourceName));
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        toolbar.setTitleMargin(margin, 0, margin, 0);
        TextView toolbarTextView = getChildView(toolbar, view -> view instanceof TextView);
        toolbarTextView.setTextColor(ThemeUtils.getTextColor());
        toolBarParent.addView(toolbar, 0);
    }
}