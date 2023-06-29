package app.revanced.music.patches.ads;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;

abstract class FilterGroup<T> {
    protected final SettingsEnum setting;
    protected final T[] filters;
    /**
     * Initialize a new filter group.
     *
     * @param setting The associated setting.
     * @param filters The filters.
     */
    @SafeVarargs
    public FilterGroup(final SettingsEnum setting, final T... filters) {
        this.setting = setting;
        this.filters = filters;
    }

    public boolean isEnabled() {
        return setting.getBoolean();
    }

    public abstract FilterGroupResult check(final T stack);

    final static class FilterGroupResult {
        private final boolean filtered;
        private final SettingsEnum setting;

        public FilterGroupResult(final SettingsEnum setting, final boolean filtered) {
            this.setting = setting;
            this.filtered = filtered;
        }

        public SettingsEnum getSetting() {
            return setting;
        }

        public boolean isFiltered() {
            return filtered;
        }
    }
}

class StringFilterGroup extends FilterGroup<String> {

    /**
     * {@link FilterGroup#FilterGroup(SettingsEnum, Object[])}
     */
    public StringFilterGroup(final SettingsEnum setting, final String... filters) {
        super(setting, filters);
    }

    @Override
    public FilterGroupResult check(final String string) {
        return new FilterGroupResult(setting, string != null && ReVancedUtils.containsAny(string, filters));
    }
}

final class CustomFilterGroup extends StringFilterGroup {

    /**
     * {@link FilterGroup#FilterGroup(SettingsEnum, Object[])}
     */
    public CustomFilterGroup(final SettingsEnum setting, final SettingsEnum filter) {
        super(setting, filter.getString().split(","));
    }
}

abstract class FilterGroupList<V, T extends FilterGroup<V>> implements Iterable<T> {
    private final ArrayList<T> filterGroups = new ArrayList<>();

    @SafeVarargs
    protected final void addAll(final T... filterGroups) {
        this.filterGroups.addAll(Arrays.asList(filterGroups));
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return filterGroups.iterator();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void forEach(@NonNull Consumer<? super T> action) {
        filterGroups.forEach(action);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Spliterator<T> spliterator() {
        return filterGroups.spliterator();
    }

    protected boolean contains(final V stack) {
        for (T filterGroup : this) {
            if (!filterGroup.isEnabled())
                continue;

            var result = filterGroup.check(stack);
            if (result.isFiltered()) {
                return true;
            }
        }

        return false;
    }
}

final class StringFilterGroupList extends FilterGroupList<String, StringFilterGroup> {
}

abstract class Filter {
    final protected StringFilterGroupList pathFilterGroups = new StringFilterGroupList();
    final protected StringFilterGroupList identifierFilterGroups = new StringFilterGroupList();

    /**
     * Check if the given path, identifier or protobuf buffer is filtered by any
     * {@link FilterGroup}.
     *
     * @return True if filtered, false otherwise.
     */
    boolean isFiltered(final String path, final String identifier) {
        if (pathFilterGroups.contains(path)) {
            LogHelper.printDebug(LithoFilterPatch.class, String.format("Filtered path: %s", path));
            return true;
        }

        if (identifierFilterGroups.contains(identifier)) {
            LogHelper.printDebug(LithoFilterPatch.class, String.format("Filtered identifier: %s", identifier));
            return true;
        }

        return false;
    }
}

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressWarnings("unused")
public final class LithoFilterPatch {
    private static final Filter[] filters = new Filter[]{
            new DummyFilter() // Replaced by patch.
    };

    @SuppressWarnings("unused")
    public static boolean filter(final StringBuilder pathBuilder, final String identifier) {
        // TODO: Maybe this can be moved to the Filter class, to prevent unnecessary
        // string creation
        // because some filters might not need the path.
        var path = pathBuilder.toString();

        // It is assumed that protobufBuffer is empty as well in this case.
        if (path.isEmpty())
            return false;

        LogHelper.printDebug(LithoFilterPatch.class, String.format(
                "Searching (ID: %s): %s",
                identifier, path));

        for (var filter : filters) {
            var filtered = filter.isFiltered(path, identifier);

            LogHelper.printDebug(LithoFilterPatch.class, String.format("%s (ID: %s): %s", filtered ? "Filtered" : "Unfiltered", identifier, path));

            if (filtered)
                return true;
        }

        return false;
    }
}