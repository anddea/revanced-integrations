package app.revanced.integrations.patches.ads;

import static app.revanced.integrations.patches.ads.ByteBufferFilterPatch.filters;
import static app.revanced.integrations.patches.ads.ByteBufferFilterPatch.hideGeneralAds;
import static app.revanced.integrations.patches.utils.PatchStatus.ByteBuffer;
import static app.revanced.integrations.patches.utils.PatchStatus.GeneralAds;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

class BlockRule {
    final static class BlockResult {
        private final boolean blocked;

        public BlockResult(final boolean blocked) {
            this.blocked = blocked;
        }

        public boolean isBlocked() {
            return blocked;
        }
    }

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
}

final class CustomBlockRule extends BlockRule {
    /**
     * Initialize a new rule for components.
     *
     * @param setting The setting which controls the blocking of the components.
     * @param filter  The setting which contains the list of component names.
     */
    public CustomBlockRule(final SettingsEnum setting, final SettingsEnum filter) {
        super(setting, filter.getString().split(","));
    }
}

abstract class Filter {
    final protected LithoBlockRegister pathRegister = new LithoBlockRegister();
    final protected LithoBlockRegister identifierRegister = new LithoBlockRegister();

    abstract boolean filter(final String path, final String identifier);
}

final class LithoBlockRegister implements Iterable<BlockRule> {
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
            new GeneralAdsPatch(),
            new CommentsPatch()
    };

    public static boolean filter(StringBuilder pathBuilder, String identifier) {
        var path = pathBuilder.toString();
        if (path.isEmpty()) return false;

        for (var filter : filters) {
            if (filter.filter(path, identifier)) return true;
        }

        return false;
    }

    public static boolean filter(StringBuilder pathBuilder, String identifier, Object object, ByteBuffer buffer) {
        return (ByteBuffer() && filters(object.toString(), buffer)) || hideGeneralAds(object.toString(), buffer) || (GeneralAds() && filter(pathBuilder, identifier));
    }
}
