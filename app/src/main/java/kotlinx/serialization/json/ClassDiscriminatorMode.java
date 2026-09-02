package kotlinx.serialization.json;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

public enum ClassDiscriminatorMode {
    NONE,
    ALL_JSON_OBJECTS,
    POLYMORPHIC;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
}
