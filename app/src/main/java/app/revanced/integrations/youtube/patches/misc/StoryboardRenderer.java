package app.revanced.integrations.youtube.patches.misc;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

/**
 * @noinspection ALL
 */
public final class StoryboardRenderer {
    @Nullable
    public final String spec;
    public final String videoId;
    public final boolean isLiveStream;
    @Nullable
    public final Integer recommendedLevel;

    public StoryboardRenderer(String videoId, @Nullable String spec, boolean isLiveStream, @Nullable Integer recommendedLevel) {
        this.videoId = videoId;
        this.spec = spec;
        this.isLiveStream = isLiveStream;
        this.recommendedLevel = recommendedLevel;
    }

    @NotNull
    @Override
    public String toString() {
        return "StoryboardRenderer{" +
                "isLiveStream=" + isLiveStream +
                "videoId=" + videoId +
                ", isLiveStream=" + isLiveStream +
                ", spec='" + spec + '\'' +
                ", recommendedLevel=" + recommendedLevel +
                '}';
    }
}
