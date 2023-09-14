package app.revanced.music.sponsorblock.objects;

import static app.revanced.music.utils.StringRef.sf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import app.revanced.music.utils.ReVancedUtils;
import app.revanced.music.utils.StringRef;

public enum CategoryBehaviour {
    SKIP_AUTOMATICALLY("skip", 2, true, sf("sb_skip_automatically")),
    // ignored categories are not exported to json, and ignore is the default behavior when importing
    IGNORE("ignore", -1, false, sf("sb_skip_ignore"));

    private static String[] behaviorKeys;
    private static String[] behaviorDescriptions;
    @NonNull
    public final String key;
    public final int desktopKey;
    /**
     * If the segment should skip automatically
     */
    public final boolean skipAutomatically;
    @NonNull
    public final StringRef description;

    CategoryBehaviour(String key, int desktopKey, boolean skipAutomatically, StringRef description) {
        this.key = Objects.requireNonNull(key);
        this.desktopKey = desktopKey;
        this.skipAutomatically = skipAutomatically;
        this.description = Objects.requireNonNull(description);
    }

    @Nullable
    public static CategoryBehaviour byStringKey(@NonNull String key) {
        for (CategoryBehaviour behaviour : values()) {
            if (behaviour.key.equals(key)) {
                return behaviour;
            }
        }
        return null;
    }

    private static void createNameAndKeyArrays() {
        ReVancedUtils.verifyOnMainThread();

        CategoryBehaviour[] behaviours = values();
        final int behaviorLength = behaviours.length;
        behaviorKeys = new String[behaviorLength];
        behaviorDescriptions = new String[behaviorLength];

        int behaviorIndex = 0;
        while (behaviorIndex < behaviorLength) {
            CategoryBehaviour behaviour = behaviours[behaviorIndex];
            String key = behaviour.key;
            String description = behaviour.description.toString();
            behaviorKeys[behaviorIndex] = key;
            behaviorDescriptions[behaviorIndex] = description;
            behaviorIndex++;
        }
    }

    static String[] getBehaviorKeys() {
        if (behaviorKeys == null) {
            createNameAndKeyArrays();
        }
        return behaviorKeys;
    }

    static String[] getBehaviorDescriptions() {
        if (behaviorDescriptions == null) {
            createNameAndKeyArrays();
        }
        return behaviorDescriptions;
    }
}
