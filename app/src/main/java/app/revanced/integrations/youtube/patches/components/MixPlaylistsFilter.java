package app.revanced.integrations.youtube.patches.components;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.utils.StringTrieSearch;

@SuppressWarnings("unused")
public final class MixPlaylistsFilter extends Filter {

    private static final StringTrieSearch exceptions = new StringTrieSearch();
    private static final StringTrieSearch mixPlaylistsExceptions = new StringTrieSearch();
    private static final ByteArrayAsStringFilterGroup mixPlaylistsExceptions2 =
            new ByteArrayAsStringFilterGroup(
                    null,
                    "cell_description_body"
            );

    private static final ByteArrayAsStringFilterGroup mixPlaylists =
            new ByteArrayAsStringFilterGroup(
                    SettingsEnum.HIDE_MIX_PLAYLISTS,
                    "&list="
            );

    public MixPlaylistsFilter() {
        mixPlaylistsExceptions.addPatterns(
                "V.ED", // playlist browse id
                "java.lang.ref.WeakReference"
        );
    }

    /**
     * Injection point.
     * <p>
     * Called from a different place then the other filters.
     */
    public static boolean filterMixPlaylists(final Object conversionContext, final byte[] bytes) {
        if (bytes == null)
            return false;

        return mixPlaylists.check(bytes).isFiltered()
                && !mixPlaylistsExceptions.matches(conversionContext.toString())
                && !mixPlaylistsExceptions2.check(bytes).isFiltered();
    }
}
