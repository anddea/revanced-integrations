package app.revanced.integrations.youtube.patches.components;

import androidx.annotation.Nullable;

import java.util.regex.Pattern;

import app.revanced.integrations.youtube.settings.SettingsEnum;

/**
 * There are too many exceptions to integrate into one {@link LayoutComponentsFilter}, and the filter is too complicated.
 * That's why I separated it with a new filter.
 */
@SuppressWarnings("unused")
final class ChannelBarFilter extends Filter {
    private static final String CHANNEL_BAR_JOIN_BUTTON_NEW_LAYOUT_PATH = "|button.eml|";
    private static final Pattern CHANNEL_BAR_JOIN_BUTTON_NEW_LAYOUT_PATTERN = Pattern.compile("ContainerType.+ContainerType");
    private final StringFilterGroupList channelBarGroupList = new StringFilterGroupList();

    public ChannelBarFilter() {
        final StringFilterGroup channelBar = new StringFilterGroup(
                null,
                "channel_bar_inner"
        );

        final StringFilterGroup joinMembership = new StringFilterGroup(
                SettingsEnum.HIDE_JOIN_BUTTON,
                "compact_sponsor_button"
        );

        final StringFilterGroup joinMembershipNewLayout = new StringFilterGroup(
                SettingsEnum.HIDE_JOIN_BUTTON,
                CHANNEL_BAR_JOIN_BUTTON_NEW_LAYOUT_PATH
        );

        final StringFilterGroup startTrial = new StringFilterGroup(
                SettingsEnum.HIDE_START_TRIAL_BUTTON,
                "channel_purchase_button"
        );

        pathFilterGroupList.addAll(channelBar);
        channelBarGroupList.addAll(
                joinMembership,
                joinMembershipNewLayout,
                startTrial
        );
    }

    /** @noinspection rawtypes*/
    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (!channelBarGroupList.check(path).isFiltered()) {
            return false;
        }
        if (path.contains(CHANNEL_BAR_JOIN_BUTTON_NEW_LAYOUT_PATH)) {
            return CHANNEL_BAR_JOIN_BUTTON_NEW_LAYOUT_PATTERN.matcher(path).find();
        }

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
