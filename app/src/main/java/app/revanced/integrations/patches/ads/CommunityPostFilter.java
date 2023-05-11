package app.revanced.integrations.patches.ads;

import app.revanced.integrations.settings.SettingsEnum;

public final class CommunityPostFilter extends Filter {

    private final StringFilterGroup communityPostGroup = new StringFilterGroup(
            null,
            "post_base_wrapper"
    );

    public CommunityPostFilter() {
        final var home = new StringFilterGroup(
                SettingsEnum.HIDE_COMMUNITY_POSTS_HOME,
                "horizontalCollectionSwipeProtector=null"
        );

        final var subscriptions = new StringFilterGroup(
                SettingsEnum.HIDE_COMMUNITY_POSTS_SUBSCRIPTIONS,
                "heightConstraint=null"
        );

        final var browserStoreButton = new ByteArrayAsStringFilterGroup(
                SettingsEnum.HIDE_BROWSE_STORE_BUTTON,
                "header_store_button"
        );

        this.pathFilterGroups.addAll(home, subscriptions);
        this.protobufBufferFilterGroups.addAll(browserStoreButton);
    }

    @Override
    boolean isFiltered(final String path, final String identifier, final String object, final byte[] protobufBufferArray) {
        if (communityPostGroup.check(object).isFiltered())
            if (this.pathFilterGroups.contains(object)) return true;

        return this.protobufBufferFilterGroups.contains(protobufBufferArray);
    }
}
