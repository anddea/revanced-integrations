package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.patches.seekbar.SeekBarPatch.ORIGINAL_SEEKBAR_COLOR;
import static app.revanced.integrations.patches.seekbar.SeekBarPatch.resumedProgressBarColor;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;

@SuppressWarnings("unused")
public class ProgressBarDrawable extends Drawable {

    private final Paint paint = new Paint();

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (SettingsEnum.HIDE_SEEKBAR_THUMBNAIL.getBoolean()) {
            return;
        }
        paint.setColor(resumedProgressBarColor(ORIGINAL_SEEKBAR_COLOR));
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
