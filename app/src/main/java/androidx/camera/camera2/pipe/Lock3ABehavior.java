package androidx.camera.camera2.pipe;

import kotlin.jvm.internal.DefaultConstructorMarker;

public final class Lock3ABehavior {
    private final int value;
    public static final Companion Companion = new Companion(null);
    private static final int IMMEDIATE = m287constructorimpl(1);
    private static final int AFTER_CURRENT_SCAN = m287constructorimpl(2);
    private static final int AFTER_NEW_SCAN = m287constructorimpl(3);

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ Lock3ABehavior m286boximpl(int i) {
        return new Lock3ABehavior(i);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    private static int m287constructorimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m288equalsimpl(int i, Object obj) {
        return (obj instanceof Lock3ABehavior) && i == ((Lock3ABehavior) obj).m292unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m289equalsimpl0(int i, int i2) {
        return i == i2;
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m290hashCodeimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m291toStringimpl(int i) {
        return "Lock3ABehavior(value=" + i + ')';
    }

    public boolean equals(Object obj) {
        return m288equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m290hashCodeimpl(this.value);
    }

    public String toString() {
        return m291toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int m292unboximpl() {
        return this.value;
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: renamed from: getIMMEDIATE-hRqSH3k, reason: not valid java name */
        public final int m295getIMMEDIATEhRqSH3k() {
            return Lock3ABehavior.IMMEDIATE;
        }

        /* JADX INFO: renamed from: getAFTER_CURRENT_SCAN-hRqSH3k, reason: not valid java name */
        public final int m293getAFTER_CURRENT_SCANhRqSH3k() {
            return Lock3ABehavior.AFTER_CURRENT_SCAN;
        }

        /* JADX INFO: renamed from: getAFTER_NEW_SCAN-hRqSH3k, reason: not valid java name */
        public final int m294getAFTER_NEW_SCANhRqSH3k() {
            return Lock3ABehavior.AFTER_NEW_SCAN;
        }
    }

    private /* synthetic */ Lock3ABehavior(int i) {
        this.value = i;
    }
}
