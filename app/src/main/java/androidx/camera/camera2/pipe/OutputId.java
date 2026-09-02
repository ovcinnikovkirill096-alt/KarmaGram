package androidx.camera.camera2.pipe;

public final class OutputId {
    private final int value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ OutputId m296boximpl(int i) {
        return new OutputId(i);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int m297constructorimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m298equalsimpl(int i, Object obj) {
        return (obj instanceof OutputId) && i == ((OutputId) obj).m302unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m299equalsimpl0(int i, int i2) {
        return i == i2;
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m300hashCodeimpl(int i) {
        return i;
    }

    public boolean equals(Object obj) {
        return m298equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m300hashCodeimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int m302unboximpl() {
        return this.value;
    }

    private /* synthetic */ OutputId(int i) {
        this.value = i;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m301toStringimpl(int i) {
        return "Output-" + i;
    }

    public String toString() {
        return m301toStringimpl(this.value);
    }
}
