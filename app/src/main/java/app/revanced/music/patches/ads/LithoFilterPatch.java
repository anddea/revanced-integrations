package app.revanced.music.patches.ads;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import app.revanced.music.settings.SettingsEnum;
import app.revanced.music.utils.LogHelper;
import app.revanced.music.utils.ReVancedUtils;
import app.revanced.music.utils.StringTrieSearch;
import app.revanced.music.utils.TrieSearch;

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
        if (filters.length == 0) {
            throw new IllegalArgumentException("Must use one or more filter patterns (zero specified)");
        }
    }

    public boolean isEnabled() {
        return setting == null || setting.getBoolean();
    }

    /**
     * @return If {@link FilterGroupList} should include this group when searching.
     * By default, all filters are included except non enabled settings that require reboot.
     */
    public boolean includeInSearch() {
        return isEnabled() || !setting.rebootApp;
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + (setting == null ? "(null setting)" : setting);
    }

    public abstract FilterGroupResult check(final T stack);

    final static class FilterGroupResult {
        SettingsEnum setting;
        boolean filtered;

        FilterGroupResult(SettingsEnum setting, boolean filtered) {
            this.setting = setting;
            this.filtered = filtered;
        }

        /**
         * A null value if the group has no setting,
         * or if no match is returned from {@link FilterGroupList#check(Object)}.
         */
        public SettingsEnum getSetting() {
            return setting;
        }

        public boolean isFiltered() {
            return filtered;
        }
    }
}

class StringFilterGroup extends FilterGroup<String> {

    public StringFilterGroup(final SettingsEnum setting, final String... filters) {
        super(setting, filters);
    }

    @Override
    public FilterGroupResult check(final String string) {
        return new FilterGroupResult(setting,
                (setting == null || setting.getBoolean()) && ReVancedUtils.containsAny(string, filters));
    }
}

final class CustomFilterGroup extends StringFilterGroup {

    public CustomFilterGroup(final SettingsEnum setting, final SettingsEnum filter) {
        super(setting, filter.getString().split("\\s+"));
    }
}

abstract class FilterGroupList<V, T extends FilterGroup<V>> implements Iterable<T> {

    private final List<T> filterGroups = new ArrayList<>();
    /**
     * Search graph. Created only if needed.
     */
    private volatile TrieSearch<V> search;

    @SafeVarargs
    protected final void addAll(final T... groups) {
        filterGroups.addAll(Arrays.asList(groups));
        search = null; // Rebuild, if already created.
    }

    protected final synchronized void buildSearch() {
        // Since litho filtering is multi-threaded, this method can be concurrently called by multiple threads.
        if (search != null)
            return; // Thread race and another thread already initialized the search.
        LogHelper.printDebug(LithoFilterPatch.class, "Creating prefix search tree for: " + this);
        TrieSearch<V> search = createSearchGraph();
        for (T group : filterGroups) {
            if (!group.includeInSearch()) {
                continue;
            }
            for (V pattern : group.filters) {
                search.addPattern(pattern, (textSearched, matchedStartIndex, callbackParameter) -> {
                    if (group.isEnabled()) {
                        FilterGroup.FilterGroupResult result = (FilterGroup.FilterGroupResult) callbackParameter;
                        result.setting = group.setting;
                        result.filtered = true;
                        return true;
                    }
                    return false;
                });
            }
        }
        this.search = search; // Must set after it's completely initialized.
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

    protected FilterGroup.FilterGroupResult check(V stack) {
        if (search == null) {
            buildSearch(); // Lazy load.
        }
        FilterGroup.FilterGroupResult result = new FilterGroup.FilterGroupResult(null, false);
        search.matches(stack, result);
        return result;
    }

    protected abstract TrieSearch<V> createSearchGraph();
}

final class StringFilterGroupList extends FilterGroupList<String, StringFilterGroup> {
    protected StringTrieSearch createSearchGraph() {
        return new StringTrieSearch();
    }
}

abstract class Filter {
    /**
     * All group filters must be set before the constructor call completes.
     * Otherwise {@link #isFiltered(String, String, FilterGroupList, FilterGroup, int)}
     * will never be called for any matches.
     */

    protected final StringFilterGroupList pathFilterGroups = new StringFilterGroupList();
    protected final StringFilterGroupList identifierFilterGroups = new StringFilterGroupList();


    /**
     * Called after an enabled filter has been matched.
     * Default implementation is to always filter the matched item.
     * Subclasses can perform additional or different checks if needed.
     * <p>
     * Method is called off the main thread.
     *
     * @param matchedList  The list the group filter belongs to.
     * @param matchedGroup The actual filter that matched.
     * @param matchedIndex Matched index of string/array.
     * @return True if the litho item should be filtered out.
     */
    @SuppressWarnings("rawtypes")
    boolean isFiltered(String path, @Nullable String identifier,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (SettingsEnum.ENABLE_DEBUG_LOGGING.getBoolean()) {
            if (pathFilterGroups == matchedList) {
                LogHelper.printDebug(LithoFilterPatch.class, getClass().getSimpleName() + " Filtered path: " + path);
            } else if (identifierFilterGroups == matchedList) {
                LogHelper.printDebug(LithoFilterPatch.class, getClass().getSimpleName() + " Filtered identifier: " + identifier);
            }
        }
        return true;
    }
}

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressWarnings("unused")
public final class LithoFilterPatch {
    private static final Filter[] filters = new Filter[]{
            new DummyFilter() // Replaced by patch.
    };
    private static final StringTrieSearch pathSearchTree = new StringTrieSearch();
    private static final StringTrieSearch identifierSearchTree = new StringTrieSearch();

    static {
        for (Filter filter : filters) {
            filterGroupLists(pathSearchTree, filter, filter.pathFilterGroups);
            filterGroupLists(identifierSearchTree, filter, filter.identifierFilterGroups);
        }

        LogHelper.printDebug(LithoFilterPatch.class, "Using: "
                + pathSearchTree.numberOfPatterns() + " path filters"
                + " (" + pathSearchTree.getEstimatedMemorySize() + " KB), "
                + identifierSearchTree.numberOfPatterns() + " identifier filters"
                + " (" + identifierSearchTree.getEstimatedMemorySize() + " KB), ");
    }

    private static <T> void filterGroupLists(TrieSearch<T> pathSearchTree,
                                             Filter filter, FilterGroupList<T, ? extends FilterGroup<T>> list) {
        for (FilterGroup<T> group : list) {
            if (!group.includeInSearch()) {
                continue;
            }
            for (T pattern : group.filters) {
                pathSearchTree.addPattern(pattern, (textSearched, matchedStartIndex, callbackParameter) -> {
                            if (!group.isEnabled()) return false;
                            LithoFilterParameters parameters = (LithoFilterParameters) callbackParameter;
                            return filter.isFiltered(parameters.path, parameters.identifier,
                                    list, group, matchedStartIndex);
                        }
                );
            }
        }
    }

    /**
     * Injection point.  Called off the main thread.
     */
    @SuppressWarnings("unused")
    public static boolean filter(@NonNull StringBuilder pathBuilder, @Nullable String lithoIdentifier) {
        try {
            // It is assumed that protobufBuffer is empty as well in this case.
            if (pathBuilder.length() == 0)
                return false;

            LithoFilterParameters parameter = new LithoFilterParameters(pathBuilder, lithoIdentifier);
            LogHelper.printDebug(LithoFilterPatch.class, "Searching " + parameter);

            if (pathSearchTree.matches(parameter.path, parameter)) return true;
            if (parameter.identifier != null) {
                if (identifierSearchTree.matches(parameter.identifier, parameter)) return true;
            }
        } catch (Exception ex) {
            LogHelper.printException(LithoFilterPatch.class, "Litho filter failure", ex);
        }

        return false;
    }

    /**
     * Simple wrapper to pass the litho parameters through the prefix search.
     */
    private static final class LithoFilterParameters {
        final String path;
        final String identifier;

        LithoFilterParameters(StringBuilder lithoPath, String lithoIdentifier) {
            this.path = lithoPath.toString();
            this.identifier = lithoIdentifier;
        }

        @NonNull
        @Override
        public String toString() {
            // Estimated percentage of the buffer that are Strings.

            return "\nID: " + identifier + "\nPath: " + path;
        }
    }
}