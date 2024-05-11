package app.revanced.integrations.shared.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

/** @noinspection ALL*/
public class ResourceUtils extends Utils {

    private ResourceUtils() {
    } // utility class

    public static int getIdentifier(@NonNull String str, @NonNull ResourceType resourceType) {
        return getIdentifier(str, resourceType, getContext());
    }

    public static int getIdentifier(@NonNull String str, @NonNull ResourceType resourceType,
                                    @NonNull Context context) {
        return getResources().getIdentifier(str, resourceType.getType(), context.getPackageName());
    }

    public static int getAnimIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.ANIM);
    }

    public static int getArrayIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.ARRAY);
    }

    public static int getAttrIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.ATTR);
    }

    public static int getColorIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.COLOR);
    }

    public static int getDimenIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.DIMEN);
    }

    public static int getDrawableIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.DRAWABLE);
    }

    public static int getFontIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.FONT);
    }

    public static int getIdIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.ID);
    }

    public static int getIntegerIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.INTEGER);
    }

    public static int getLayoutIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.LAYOUT);
    }

    public static int getMenuIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.MENU);
    }

    public static int getMipmapIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.MIPMAP);
    }

    public static int getRawIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.RAW);
    }

    public static int getStringIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.STRING);
    }

    public static int getStyleIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.STYLE);
    }

    public static int getXmlIdentifier(@NonNull String str) {
        return getIdentifier(str, ResourceType.XML);
    }

    public static Animation getAnimation(@NonNull String str) {
        return AnimationUtils.loadAnimation(getContext(), getAnimIdentifier(str));
    }

    public static int getColor(@NonNull String str) {
        final int identifier = getColorIdentifier(str);
        return identifier == 0 ? 0 : getResources().getColor(identifier);
    }

    public static int getDimension(@NonNull String str) {
        return getResources().getDimensionPixelSize(getDimenIdentifier(str));
    }

    public static Drawable getDrawable(@NonNull String str) {
        return getResources().getDrawable(getDrawableIdentifier(str));
    }


    public static String getString(@NonNull String str) {
        final int identifier = getStringIdentifier(str);
        return identifier == 0 ? str : getResources().getString(identifier);
    }

    public static String[] getStringArray(@NonNull String str) {
        return getResources().getStringArray(getArrayIdentifier(str));
    }

    public static int getInteger(@NonNull String str) {
        return getResources().getInteger(getIntegerIdentifier(str));
    }

    public enum ResourceType {
        ANIM("anim"),
        ARRAY("array"),
        ATTR("attr"),
        COLOR("color"),
        DIMEN("dimen"),
        DRAWABLE("drawable"),
        FONT("font"),
        ID("id"),
        INTEGER("integer"),
        LAYOUT("layout"),
        MENU("menu"),
        MIPMAP("mipmap"),
        RAW("raw"),
        STRING("string"),
        STYLE("style"),
        XML("xml");

        private final String type;

        ResourceType(String type) {
            this.type = type;
        }

        public final String getType() {
            return type;
        }
    }
}
