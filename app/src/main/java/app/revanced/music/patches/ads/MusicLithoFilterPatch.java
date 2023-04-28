package app.revanced.music.patches.ads;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import app.revanced.music.settings.MusicSettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

class MusicBlockRule {
    final static class BlockResult {
        private final boolean blocked;

        public BlockResult(final boolean blocked) {
            this.blocked = blocked;
        }

        public boolean isBlocked() {
            return blocked;
        }
    }

    protected final MusicSettingsEnum setting;
    private final String[] blocks;

    /**
     * Initialize a new rule for components.
     *
     * @param setting The setting which controls the blocking of this component.
     * @param blocks  The rules to block the component on.
     */
    public MusicBlockRule(final MusicSettingsEnum setting, final String... blocks) {
        this.setting = setting;
        this.blocks = blocks;
    }

    public boolean isEnabled() {
        return setting.getBoolean();
    }

    public BlockResult check(final String string) {
        return new BlockResult(string != null && ReVancedUtils.containsAny(string, blocks));
    }
}

abstract class MusicFilter {
    final protected LithoBlockRegisters pathRegister = new LithoBlockRegisters();
    final protected LithoBlockRegisters identifierRegister = new LithoBlockRegisters();

    abstract boolean filter(final String path, final String identifier);
}

final class LithoBlockRegisters implements Iterable<MusicBlockRule> {
    private final ArrayList<MusicBlockRule> blocks = new ArrayList<>();

    public void registerAll(MusicBlockRule... blocks) {
        this.blocks.addAll(Arrays.asList(blocks));
    }

    @NonNull
    @Override
    public Iterator<MusicBlockRule> iterator() {
        return blocks.iterator();
    }

    public boolean contains(String path) {
        for (var rule : this) {
            if (!rule.isEnabled()) continue;

            var result = rule.check(path);
            if (result.isBlocked()) {
                return true;
            }
        }

        return false;
    }
}

public final class MusicLithoFilterPatch {

    private static final MusicFilter[] filters = new MusicFilter[]{
            new GeneralMusicAdsPatch()
    };

    public static boolean filter(StringBuilder pathBuilder, String identifier) {
        var path = pathBuilder.toString();
        if (path.isEmpty()) return false;

        LogHelper.printDebug(MusicLithoFilterPatch.class, String.format("Searching (ID: %s): %s", identifier, path));

        for (var filter : filters) {
            if (filter.filter(path, identifier)) return true;
        }
        return false;
    }
}
