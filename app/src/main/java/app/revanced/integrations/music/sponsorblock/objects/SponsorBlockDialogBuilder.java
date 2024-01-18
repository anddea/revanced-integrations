package app.revanced.integrations.music.sponsorblock.objects;

import static app.revanced.integrations.music.settings.SharedPrefCategory.YOUTUBE;
import static app.revanced.integrations.music.utils.ReVancedHelper.getDialogBuilder;
import static app.revanced.integrations.music.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Objects;

import app.revanced.integrations.music.utils.LogHelper;
import app.revanced.integrations.music.utils.ReVancedUtils;

public class SponsorBlockDialogBuilder {
    private static final String[] CategoryBehaviourEntries = {str("sb_skip_automatically"), str("sb_skip_ignore")};
    private static final CategoryBehaviour[] CategoryBehaviourEntryValues = {CategoryBehaviour.SKIP_AUTOMATICALLY, CategoryBehaviour.IGNORE};
    private static int mClickedDialogEntryIndex;

    public static void dialogBuilder(String categoryString, Activity base) {
        try {
            SegmentCategory category = Objects.requireNonNull(SegmentCategory.byCategoryKey(categoryString));
            final AlertDialog.Builder builder = getDialogBuilder(base);
            TableLayout table = new TableLayout(base);
            table.setOrientation(LinearLayout.HORIZONTAL);
            table.setPadding(70, 0, 150, 0);

            TableRow row = new TableRow(base);

            TextView colorTextLabel = new TextView(base);
            colorTextLabel.setText(str("sb_color_dot_label"));
            row.addView(colorTextLabel);

            TextView colorDotView = new TextView(base);
            colorDotView.setText(category.getCategoryColorDot());
            colorDotView.setPadding(30, 0, 30, 0);
            row.addView(colorDotView);

            final EditText mEditText = new EditText(base);
            mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            mEditText.setText(category.colorString());
            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        String colorString = s.toString();
                        if (!colorString.startsWith("#")) {
                            s.insert(0, "#"); // recursively calls back into this method
                            return;
                        }
                        if (colorString.length() > 7) {
                            s.delete(7, colorString.length());
                            return;
                        }
                        final int color = Color.parseColor(colorString);
                        colorDotView.setText(SegmentCategory.getCategoryColorDot(color));
                    } catch (IllegalArgumentException ex) {
                        // ignore
                    }
                }
            });
            mEditText.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            row.addView(mEditText);

            table.addView(row);
            builder.setView(table);
            builder.setTitle(category.title.toString());

            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                category.behaviour = CategoryBehaviourEntryValues[mClickedDialogEntryIndex];
                SegmentCategory.updateEnabledCategories();

                String colorString = mEditText.getText().toString();
                try {
                    final int color = Color.parseColor(colorString) & 0xFFFFFF;
                    if (color != category.color) {
                        category.setColor(color);
                        ReVancedUtils.showToastShort(str("sb_color_changed"));
                    }
                } catch (IllegalArgumentException ex) {
                    ReVancedUtils.showToastShort(str("sb_color_invalid"));
                }

                String colorValue = mEditText.getText().toString().trim();
                SharedPreferences.Editor editor = YOUTUBE.preferences.edit();
                category.setColor(colorValue);
                category.save(editor);
                editor.apply();
            });
            builder.setNeutralButton(str("sb_reset_color"), (dialog, which) -> {
                try {
                    SharedPreferences.Editor editor = YOUTUBE.preferences.edit();
                    category.setColor(category.defaultColor);
                    category.save(editor);
                    editor.apply();
                    ReVancedUtils.showToastShort(str("sb_color_reset"));
                } catch (Exception ex) {
                    LogHelper.printException(() -> "setNeutralButton failure", ex);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);

            final int index = Arrays.asList(CategoryBehaviourEntryValues).indexOf(category.behaviour);
            mClickedDialogEntryIndex = Math.max(index, 0);

            builder.setSingleChoiceItems(CategoryBehaviourEntries, mClickedDialogEntryIndex,
                    (dialog, id) -> mClickedDialogEntryIndex = id);
            builder.show();
        } catch (Exception ex) {
            LogHelper.printException(() -> "dialogBuilder failure", ex);
        }
    }
}
