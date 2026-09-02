package com.exteragram.messenger.api.model;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

public enum ProfileStatus {
    DEFAULT,
    DEVELOPER,
    SUPPORTER;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

    public static EnumEntries getEntries() {
        return $ENTRIES;
    }
}
