package app.revanced.integrations.youtube.patches.components;

import static app.revanced.integrations.youtube.utils.ReVancedHelper.isSpoofingToLessThan;
import static app.revanced.integrations.youtube.utils.ReVancedUtils.hideViewBy0dpUnderCondition;

import android.view.View;

import androidx.annotation.Nullable;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.shared.NavigationBar;
import app.revanced.integrations.youtube.shared.NavigationBar.NavigationButton;
import app.revanced.integrations.youtube.shared.PlayerType;

/**
 * @noinspection rawtypes
 */
@SuppressWarnings("unused")
public final class SuggestionsShelfFilter extends Filter {
    private final StringFilterGroup horizontalShelf;

    public SuggestionsShelfFilter() {
        horizontalShelf = new StringFilterGroup(
                SettingsEnum.HIDE_SUGGESTIONS_SHELF,
                "horizontal_video_shelf.eml",
                "horizontal_shelf.eml"
        );

        pathFilterGroupList.addAll(horizontalShelf);
    }

    /**
     * Injection point.
     * <p>
     * Only used to tablet layout and the old UI components.
     */
    public static void hideBreakingNewsShelf(View view) {
        hideViewBy0dpUnderCondition(
                SettingsEnum.HIDE_SUGGESTIONS_SHELF.getBoolean()
                        && !isSpoofingToLessThan("17.31.00"),
                view
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (matchedGroup == horizontalShelf) {
            if (matchedIndex == 0 && hideShelves()) {
                return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
            }

            return false;
        }

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }

    private static boolean hideShelves() {
        // If the player is opened while library is selected,
        // then filter any recommendations below the player.
        if (PlayerType.getCurrent().isMaximizedOrFullscreen()
                // Or if the search is active while library is selected, then also filter.
                || NavigationBar.isSearchBarActive()) {
            return true;
        }

        // Check navigation button last.
        // Only filter if the library tab is not selected.
        // This check is important as the shelf layout is used for the library tab playlists.
        NavigationButton selectedNavButton = NavigationButton.getSelectedNavigationButton();
        return selectedNavButton != null && !selectedNavButton.isLibraryOrYouTab();
    }
}
