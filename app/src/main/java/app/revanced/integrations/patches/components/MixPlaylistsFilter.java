package app.revanced.integrations.patches.components;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.StringTrieSearch;

@SuppressWarnings("unused")
public final class MixPlaylistsFilter extends Filter {

    private static final StringTrieSearch exceptions = new StringTrieSearch();

    private static final ByteArrayAsStringFilterGroup byteArrayExceptions =
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
        exceptions.addPatterns(
                "V.ED", // playlist browse id
                "java.lang.ref.WeakReference"
        );
    }

    /**
     * Injection point.
     * <p>
     * Called from a different place then the other filters.
     */
    public static boolean filterMixPlaylists(final Object allValue, final byte[] bytes) {
        if (bytes == null)
            return false;

        // If MixPlaylists exist in a playlist, there is an issue where all lists are hidden
        if (exceptions.matches(allValue.toString()))
            return false;

        return mixPlaylists.check(bytes).isFiltered()
                && !byteArrayExceptions.check(bytes).isFiltered();
    }
}
