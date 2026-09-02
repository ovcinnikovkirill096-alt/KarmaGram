package androidx.camera.camera2.pipe.core;

import androidx.camera.camera2.pipe.CameraTimestamp$$ExternalSyntheticBackport0;
import kotlin.jvm.internal.DefaultConstructorMarker;

public final class DurationNs {
    public static final Companion Companion = new Companion(null);
    private final long value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ DurationNs m511boximpl(long j) {
        return new DurationNs(j);
    }

    /* JADX INFO: renamed from: compareTo-zYRVrok, reason: not valid java name */
    public static final int m512compareTozYRVrok(long j, long j2) {
        if (j == j2) {
            return 0;
        }
        return j < j2 ? -1 : 1;
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static long m513constructorimpl(long j) {
        return j;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m514equalsimpl(long j, Object obj) {
        return (obj instanceof DurationNs) && j == ((DurationNs) obj).m517unboximpl();
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m515hashCodeimpl(long j) {
        return CameraTimestamp$$ExternalSyntheticBackport0.m(j);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m516toStringimpl(long j) {
        return "DurationNs(value=" + j + ')';
    }

    public boolean equals(Object obj) {
        return m514equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m515hashCodeimpl(this.value);
    }

    public String toString() {
        return m516toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ long m517unboximpl() {
        return this.value;
    }

    private /* synthetic */ DurationNs(long j) {
        this.value = j;
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
