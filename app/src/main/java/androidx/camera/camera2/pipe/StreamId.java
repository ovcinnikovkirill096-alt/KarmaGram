package androidx.camera.camera2.pipe;

public final class StreamId {
    private final int value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ StreamId m417boximpl(int i) {
        return new StreamId(i);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int m418constructorimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m419equalsimpl(int i, Object obj) {
        return (obj instanceof StreamId) && i == ((StreamId) obj).m423unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m420equalsimpl0(int i, int i2) {
        return i == i2;
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m421hashCodeimpl(int i) {
        return i;
    }

    public boolean equals(Object obj) {
        return m419equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m421hashCodeimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int m423unboximpl() {
        return this.value;
    }

    private /* synthetic */ StreamId(int i) {
        this.value = i;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m422toStringimpl(int i) {
        return "Stream-" + i;
    }

    public String toString() {
        return m422toStringimpl(this.value);
    }
}
