package app.revanced.music.patches.components;

import static app.revanced.music.utils.StringRef.str;

import androidx.annotation.NonNull;

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
     * @return If {@link FilterGroupList} should exclude this group when searching.
     * By default, all filters are included except non enabled settings that require reboot.
     */
    public boolean excludeInSearch() {
        return !isEnabled() && setting.rebootApp;
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + (setting == null ? "(null setting)" : setting);
    }

    public abstract FilterGroupResult check(final T stack);

    final static class FilterGroupResult {
        private SettingsEnum setting;
        private int matchedIndex;
        private int matchedLength;
        // In the future it might be useful to include which pattern matched,
        // but for now that is not needed.

        FilterGroupResult() {
            this(null, -1, 0);
        }

        FilterGroupResult(SettingsEnum setting, int matchedIndex, int matchedLength) {
            setValues(setting, matchedIndex, matchedLength);
        }

        public void setValues(SettingsEnum setting, int matchedIndex, int matchedLength) {
            this.setting = setting;
            this.matchedIndex = matchedIndex;
            this.matchedLength = matchedLength;
        }

        /**
         * A null value if the group has no setting,
         * or if no match is returned from {@link FilterGroupList#check(Object)}.
         */
        public SettingsEnum getSetting() {
            return setting;
        }

        @SuppressWarnings("unused")
        public boolean isFiltered() {
            return matchedIndex >= 0;
        }

        /**
         * Matched index of first pattern that matched, or -1 if nothing matched.
         */
        public int getMatchedIndex() {
            return matchedIndex;
        }

        /**
         * Length of the matched filter pattern.
         */
        @SuppressWarnings("unused")
        public int getMatchedLength() {
            return matchedLength;
        }
    }
}

class StringFilterGroup extends FilterGroup<String> {

    public StringFilterGroup(final SettingsEnum setting, final String... filters) {
        super(setting, filters);
    }

    @Override
    public FilterGroupResult check(final String string) {
        int matchedIndex = -1;
        int matchedLength = 0;
        if (isEnabled()) {
            for (String pattern : filters) {
                if (!string.isEmpty()) {
                    final int indexOf = pattern.indexOf(string);
                    if (indexOf >= 0) {
                        matchedIndex = indexOf;
                        matchedLength = pattern.length();
                        break;
                    }
                }
            }
        }
        return new FilterGroupResult(setting, matchedIndex, matchedLength);
    }
}

final class CustomFilterGroup extends StringFilterGroup {

    public CustomFilterGroup(SettingsEnum setting, SettingsEnum filter) {
        super(setting, getFilterPatterns(filter));
    }

    private static String[] getFilterPatterns(SettingsEnum setting) {
        String[] patterns = setting.getString().split("\\s+");
        for (String pattern : patterns) {
            if (!StringTrieSearch.isValidPattern(pattern)) {
                ReVancedUtils.showToastShort(str("revanced_custom_filter_strings_warning"));
                setting.resetToDefault();
                return getFilterPatterns(setting);
            }
        }
        return patterns;
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
        LogHelper.printDebug(() -> "Creating prefix search tree for: " + this);
        TrieSearch<V> search = createSearchGraph();
        for (T group : filterGroups) {
            if (group.excludeInSearch()) {
                continue;
            }
            for (V pattern : group.filters) {
                search.addPattern(pattern, (textSearched, matchedStartIndex, matchedLength, callbackParameter) -> {
                    if (group.isEnabled()) {
                        FilterGroup.FilterGroupResult result = (FilterGroup.FilterGroupResult) callbackParameter;
                        result.setValues(group.setting, matchedStartIndex, matchedLength);
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

    @Override
    public void forEach(@NonNull Consumer<? super T> action) {
        filterGroups.forEach(action);
    }

    @NonNull
    @Override
    public Spliterator<T> spliterator() {
        return filterGroups.spliterator();
    }

    protected FilterGroup.FilterGroupResult check(V stack) {
        if (search == null) {
            buildSearch(); // Lazy load.
        }
        FilterGroup.FilterGroupResult result = new FilterGroup.FilterGroupResult();
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
     * Otherwise {@link #isFiltered(String, FilterGroupList, FilterGroup, int)}
     * will never be called for any matches.
     */

    protected final StringFilterGroupList pathFilterGroupList = new StringFilterGroupList();

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
    boolean isFiltered(String path, FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (SettingsEnum.ENABLE_DEBUG_LOGGING.getBoolean()) {
            if (matchedList == pathFilterGroupList) {
                LogHelper.printDebug(() -> getClass().getSimpleName() + " Filtered path: " + path);
            }
        }
        return true;
    }
}

@SuppressWarnings("unused")
public final class LithoFilterPatch {
    private static final Filter[] filters = new Filter[]{
            new DummyFilter() // Replaced by patch.
    };
    private static final StringTrieSearch pathSearchTree = new StringTrieSearch();

    static {
        for (Filter filter : filters) {
            filterGroupLists(pathSearchTree, filter, filter.pathFilterGroupList);
        }

        LogHelper.printDebug(() -> "Using: "
                + pathSearchTree.numberOfPatterns() + " path filters"
                + " (" + pathSearchTree.getEstimatedMemorySize() + " KB)");
    }

    private static <T> void filterGroupLists(TrieSearch<T> pathSearchTree,
                                             Filter filter, FilterGroupList<T, ? extends FilterGroup<T>> list) {
        for (FilterGroup<T> group : list) {
            if (group.excludeInSearch()) {
                continue;
            }
            for (T pattern : group.filters) {
                pathSearchTree.addPattern(pattern, (textSearched, matchedStartIndex, matchedLength, callbackParameter) -> {
                            if (!group.isEnabled()) return false;
                            LithoFilterParameters parameters = (LithoFilterParameters) callbackParameter;
                            return filter.isFiltered(parameters.path, list, group, matchedStartIndex);
                        }
                );
            }
        }
    }

    /**
     * Injection point.  Called off the main thread, and commonly called by multiple threads at the same time.
     */
    @SuppressWarnings("unused")
    public static boolean filter(@NonNull StringBuilder pathBuilder) {
        try {
            // It is assumed that protobufBuffer is empty as well in this case.
            if (pathBuilder.length() == 0)
                return false;

            LithoFilterParameters parameter = new LithoFilterParameters(pathBuilder);
            LogHelper.printDebug(() -> "Searching " + parameter);

            if (pathSearchTree.matches(parameter.path, parameter))
                return true;
        } catch (Exception ex) {
            LogHelper.printException(() -> "Litho filter failure", ex);
        }

        return false;
    }

    /**
     * Simple wrapper to pass the litho parameters through the prefix search.
     */
    private static final class LithoFilterParameters {
        final String path;

        LithoFilterParameters(StringBuilder lithoPath) {
            this.path = lithoPath.toString();
        }

        @NonNull
        @Override
        public String toString() {
            // Estimate the percentage of the buffer that are Strings.
            return "\nPath: " + path;
        }
    }
}