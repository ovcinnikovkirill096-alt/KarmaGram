package androidx.camera.camera2.pipe.core;

import androidx.camera.camera2.pipe.CameraTimestamp$$ExternalSyntheticBackport0;

public final class TimestampNs {
    private final long value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ TimestampNs m523boximpl(long j) {
        return new TimestampNs(j);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static long m524constructorimpl(long j) {
        return j;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m525equalsimpl(long j, Object obj) {
        return (obj instanceof TimestampNs) && j == ((TimestampNs) obj).m529unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m526equalsimpl0(long j, long j2) {
        return j == j2;
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m527hashCodeimpl(long j) {
        return CameraTimestamp$$ExternalSyntheticBackport0.m(j);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m528toStringimpl(long j) {
        return "TimestampNs(value=" + j + ')';
    }

    public boolean equals(Object obj) {
        return m525equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m527hashCodeimpl(this.value);
    }

    public String toString() {
        return m528toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ long m529unboximpl() {
        return this.value;
    }

    private /* synthetic */ TimestampNs(long j) {
        this.value = j;
    }
}
