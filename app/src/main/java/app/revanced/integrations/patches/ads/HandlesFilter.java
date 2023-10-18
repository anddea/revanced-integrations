package app.revanced.integrations.patches.ads;

import androidx.annotation.Nullable;

import app.revanced.integrations.settings.SettingsEnum;

final class HandlesFilter extends Filter {
    private static final String ACCOUNT_HEADER_PATH = "account_header.eml";

    public HandlesFilter() {
        this.pathFilterGroupList.addAll(
                new StringFilterGroup(
                        SettingsEnum.HIDE_HANDLE,
                        "|CellType|ContainerType|ContainerType|ContainerType|TextType|"
                )
        );
    }

    @Override
    boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                       FilterGroupList matchedList, FilterGroup matchedGroup, int matchedIndex) {
        if (!path.startsWith(ACCOUNT_HEADER_PATH)) {
            return false;
        }

        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedList, matchedGroup, matchedIndex);
    }
}
