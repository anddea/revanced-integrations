package app.revanced.integrations.patches.utils;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.revanced.integrations.patches.layout.SeekBarPatch;
import app.revanced.integrations.settings.SettingsEnum;

public class ProgressBarDrawable extends Drawable {
    private static final int ORIGINAL_SEEKBAR_CLICKED_COLOR = 0xFFFF0000;

    private final Paint paint = new Paint();

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (SettingsEnum.HIDE_SEEKBAR.getBoolean()) {
            return;
        }
        paint.setColor(SeekBarPatch.resumedProgressBarColor(ORIGINAL_SEEKBAR_CLICKED_COLOR));
        canvas.drawRect(getBounds(), paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}
