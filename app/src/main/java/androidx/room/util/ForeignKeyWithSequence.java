package androidx.room.util;

import kotlin.jvm.internal.Intrinsics;

final class ForeignKeyWithSequence implements Comparable {
    private final String from;
    private final int id;
    private final int sequence;
    private final String to;

    public ForeignKeyWithSequence(int i, int i2, String from, String to) {
        Intrinsics.checkNotNullParameter(from, "from");
        Intrinsics.checkNotNullParameter(to, "to");
        this.id = i;
        this.sequence = i2;
        this.from = from;
        this.to = to;
    }

    public final int getId() {
        return this.id;
    }

    public final String getFrom() {
        return this.from;
    }

    public final String getTo() {
        return this.to;
    }

    @Override // java.lang.Comparable
    public int compareTo(ForeignKeyWithSequence other) {
        Intrinsics.checkNotNullParameter(other, "other");
        int i = this.id - other.id;
        return i == 0 ? this.sequence - other.sequence : i;
    }
}
