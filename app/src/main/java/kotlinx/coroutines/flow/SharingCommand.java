package kotlinx.coroutines.flow;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

public enum SharingCommand {
    START,
    STOP,
    STOP_AND_RESET_REPLAY_CACHE;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
}
