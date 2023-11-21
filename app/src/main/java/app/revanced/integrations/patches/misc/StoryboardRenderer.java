package app.revanced.integrations.patches.misc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public record StoryboardRenderer(String spec, boolean isLiveStream, @Nullable Integer recommendedLevel) {

    @Override
    @NonNull
    public String spec() {
        return spec;
    }

    /**
     * @return Recommended image quality level, or NULL if no recommendation exists.
     */
    @Override
    @Nullable
    public Integer recommendedLevel() {
        return recommendedLevel;
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StoryboardRenderer{" +
                "spec='" + spec);
        if (!isLiveStream) {
            sb.append('\'' + ", recommendedLevel=").append(recommendedLevel);
        }
        return sb.append('}').toString();
    }
}
