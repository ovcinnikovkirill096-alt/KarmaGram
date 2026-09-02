package androidx.camera.camera2.pipe;

public final class RequestNumber {
    private final long value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ RequestNumber m378boximpl(long j) {
        return new RequestNumber(j);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static long m379constructorimpl(long j) {
        return j;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m380equalsimpl(long j, Object obj) {
        return (obj instanceof RequestNumber) && j == ((RequestNumber) obj).m383unboximpl();
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m381hashCodeimpl(long j) {
        return CameraTimestamp$$ExternalSyntheticBackport0.m(j);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m382toStringimpl(long j) {
        return "RequestNumber(value=" + j + ')';
    }

    public boolean equals(Object obj) {
        return m380equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m381hashCodeimpl(this.value);
    }

    public String toString() {
        return m382toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ long m383unboximpl() {
        return this.value;
    }

    private /* synthetic */ RequestNumber(long j) {
        this.value = j;
    }
}
