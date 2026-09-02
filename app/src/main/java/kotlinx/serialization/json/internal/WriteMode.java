package kotlinx.serialization.json.internal;

import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

public enum WriteMode {
    OBJ('{', '}'),
    LIST('[', ']'),
    MAP('{', '}'),
    POLY_OBJ('[', ']');

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
    public final char begin;
    public final char end;

    public static EnumEntries getEntries() {
        return $ENTRIES;
    }

    WriteMode(char c, char c2) {
        this.begin = c;
        this.end = c2;
    }
}
