package app.revanced.music.patches.ads;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

class BlockRule {
    protected final SettingsEnum setting;
    private final String[] blocks;

    /**
     * Initialize a new rule for components.
     *
     * @param setting The setting which controls the blocking of this component.
     * @param blocks  The rules to block the component on.
     */
    public BlockRule(final SettingsEnum setting, final String... blocks) {
        this.setting = setting;
        this.blocks = blocks;
    }

    public boolean isEnabled() {
        return setting.getBoolean();
    }

    public BlockResult check(final String string) {
        return new BlockResult(string != null && ReVancedUtils.containsAny(string, blocks));
    }

    final static class BlockResult {
        private final boolean blocked;

        public BlockResult(final boolean blocked) {
            this.blocked = blocked;
        }

        public boolean isBlocked() {
            return blocked;
        }
    }
}

abstract class Filter {
    final protected LithoBlockRegisters pathRegister = new LithoBlockRegisters();
    final protected LithoBlockRegisters identifierRegister = new LithoBlockRegisters();

    abstract boolean filter(final String path, final String identifier);
}

final class LithoBlockRegisters implements Iterable<BlockRule> {
    private final ArrayList<BlockRule> blocks = new ArrayList<>();

    public void registerAll(BlockRule... blocks) {
        this.blocks.addAll(Arrays.asList(blocks));
    }

    @NonNull
    @Override
    public Iterator<BlockRule> iterator() {
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

public final class LithoFilterPatch {

    private static final Filter[] filters = new Filter[]{
            new GeneralAdsPatch()
    };

    public static boolean filter(StringBuilder pathBuilder, String identifier) {
        var path = pathBuilder.toString();
        if (path.isEmpty()) return false;

        LogHelper.printDebug(LithoFilterPatch.class, String.format("Searching (ID: %s): %s", identifier, path));

        for (var filter : filters) {
            if (filter.filter(path, identifier)) return true;
        }
        return false;
    }
}
