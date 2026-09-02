package androidx.camera.camera2.pipe;

public final class FrameNumber {
    private final long value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ FrameNumber m273boximpl(long j) {
        return new FrameNumber(j);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static long m274constructorimpl(long j) {
        return j;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m275equalsimpl(long j, Object obj) {
        return (obj instanceof FrameNumber) && j == ((FrameNumber) obj).m279unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m276equalsimpl0(long j, long j2) {
        return j == j2;
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m277hashCodeimpl(long j) {
        return CameraTimestamp$$ExternalSyntheticBackport0.m(j);
    }

    public boolean equals(Object obj) {
        return m275equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m277hashCodeimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ long m279unboximpl() {
        return this.value;
    }

    private /* synthetic */ FrameNumber(long j) {
        this.value = j;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m278toStringimpl(long j) {
        return "Frame-" + j;
    }

    public String toString() {
        return m278toStringimpl(this.value);
    }
}
