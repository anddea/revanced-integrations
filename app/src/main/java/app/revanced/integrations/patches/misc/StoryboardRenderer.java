package app.revanced.integrations.patches.misc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public final class StoryboardRenderer {
    private final String spec;
    private final boolean isLiveStream;
    @Nullable
    private final Integer recommendedLevel;

    public StoryboardRenderer(String spec, boolean isLiveStream, @Nullable Integer recommendedLevel) {
        this.spec = spec;
        this.isLiveStream = isLiveStream;
        this.recommendedLevel = recommendedLevel;
    }

    @NonNull
    public String spec() {
        return spec;
    }

    public boolean isLiveStream() {
        return isLiveStream;
    }

    /**
     * @return Recommended image quality level, or NULL if no recommendation exists.
     */
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
