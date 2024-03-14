package app.revanced.integrations.youtube.patches.utils;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Objects;

import app.revanced.integrations.youtube.utils.LogHelper;

public class BrowseIdPatch {
    private static final String DEFAULT_BROWSE_ID = "FEwhat_to_watch";

    /**
     * Current browse id.
     */
    private static volatile String browseId = DEFAULT_BROWSE_ID;
    /**
     * Field where the BrowseId is saved. (type: String)
     */
    private static volatile Field browseIdField;
    /**
     * Class to handle BrowseId.
     */
    private static WeakReference<Object> browseIdRef;

    /**
     * Injection point.
     * <p>
     * Access BrowseId field using Java Reflection.
     *
     * @param browseIdClass     class to handle BrowseId.
     * @param browseIdFieldName field where the BrowseId is saved. (type: String)
     */
    public static void initialize(@NonNull Object browseIdClass, @NonNull String browseIdFieldName) {
        try {
            browseIdRef = new WeakReference<>(Objects.requireNonNull(browseIdClass));

            Field field = browseIdClass.getClass().getDeclaredField(browseIdFieldName);
            field.setAccessible(true);
            browseIdField = field;
        } catch (Exception ex) {
            LogHelper.printException(() -> "Failed to initialize", ex);
        }
    }

    /**
     * Injection point.
     */
    public static void setBrowseIdFromField() {
        try {
            if (browseIdField == null || browseIdRef == null) {
                return;
            }
            final Object browseIdFieldReference = browseIdRef.get();
            if (browseIdFieldReference == null) {
                return;
            }
            final Object browseIdFieldObject = browseIdField.get(browseIdFieldReference);
            if (browseIdFieldObject == null) {
                return;
            }
            final String newlyLoadedBrowseId = browseIdFieldObject.toString();
            if (newlyLoadedBrowseId.isEmpty() || Objects.equals(browseId, newlyLoadedBrowseId)) {
                return;
            }
            browseId = newlyLoadedBrowseId;
            LogHelper.printDebug(() -> "setBrowseIdFromField: " + browseId);
        } catch (Exception ex) {
            LogHelper.printException(() -> "Failed to setBrowseIdFromField", ex);
        }
    }

    /**
     * Save a new value to BrowseId field.
     */
    public static void setBrowseIdToField(@NonNull String newlyLoadedBrowseId) {
        try {
            if (browseIdField == null || browseIdRef == null) {
                return;
            }
            final Object browseIdFieldReference = browseIdRef.get();
            if (browseIdFieldReference == null) {
                return;
            }
            if (Objects.equals(browseId, newlyLoadedBrowseId)) {
                return;
            }
            browseId = newlyLoadedBrowseId;
            browseIdField.set(browseIdFieldReference, newlyLoadedBrowseId);
        } catch (Exception ex) {
            LogHelper.printException(() -> "Failed to setBrowseIdToField", ex);
        }
    }

    public static void setDefaultBrowseIdToField() {
        setBrowseIdToField(DEFAULT_BROWSE_ID);
    }

    public static boolean isHomeFeed() {
        return browseId.equals(DEFAULT_BROWSE_ID);
    }
}
