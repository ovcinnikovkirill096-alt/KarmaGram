package androidx.camera.camera2.pipe;

public final class RequestTemplate {
    private final int value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ RequestTemplate m384boximpl(int i) {
        return new RequestTemplate(i);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int m385constructorimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m386equalsimpl(int i, Object obj) {
        return (obj instanceof RequestTemplate) && i == ((RequestTemplate) obj).m391unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m387equalsimpl0(int i, int i2) {
        return i == i2;
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m389hashCodeimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m390toStringimpl(int i) {
        return "RequestTemplate(value=" + i + ')';
    }

    public boolean equals(Object obj) {
        return m386equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m389hashCodeimpl(this.value);
    }

    public String toString() {
        return m390toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int m391unboximpl() {
        return this.value;
    }

    private /* synthetic */ RequestTemplate(int i) {
        this.value = i;
    }

    /* JADX INFO: renamed from: getName-impl, reason: not valid java name */
    public static final String m388getNameimpl(int i) {
        switch (i) {
            case 1:
                return "TEMPLATE_PREVIEW";
            case 2:
                return "TEMPLATE_STILL_CAPTURE";
            case 3:
                return "TEMPLATE_RECORD";
            case 4:
                return "TEMPLATE_VIDEO_SNAPSHOT";
            case 5:
                return "TEMPLATE_ZERO_SHUTTER_LAG";
            case 6:
                return "TEMPLATE_MANUAL";
            default:
                return "UNKNOWN-" + i;
        }
    }
}
