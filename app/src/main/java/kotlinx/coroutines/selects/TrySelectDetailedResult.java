package kotlinx.coroutines.selects;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

public enum TrySelectDetailedResult {
    SUCCESSFUL,
    REREGISTER,
    CANCELLED,
    ALREADY_SELECTED;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
}
