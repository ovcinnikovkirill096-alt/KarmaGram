package androidx.camera.camera2.pipe;

import kotlin.jvm.internal.DefaultConstructorMarker;

public final class ConfigQueryResult {
    private final int value;
    public static final Companion Companion = new Companion(null);
    private static final int UNKNOWN = m253constructorimpl(0);
    private static final int SUPPORTED = m253constructorimpl(1);
    private static final int UNSUPPORTED = m253constructorimpl(2);

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ ConfigQueryResult m252boximpl(int i) {
        return new ConfigQueryResult(i);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int m253constructorimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m254equalsimpl(int i, Object obj) {
        return (obj instanceof ConfigQueryResult) && i == ((ConfigQueryResult) obj).m258unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m255equalsimpl0(int i, int i2) {
        return i == i2;
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m256hashCodeimpl(int i) {
        return i;
    }

    public boolean equals(Object obj) {
        return m254equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m256hashCodeimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int m258unboximpl() {
        return this.value;
    }

    private /* synthetic */ ConfigQueryResult(int i) {
        this.value = i;
    }

    public String toString() {
        return m257toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m257toStringimpl(int i) {
        if (m255equalsimpl0(i, SUPPORTED)) {
            return "SUPPORTED";
        }
        return m255equalsimpl0(i, UNSUPPORTED) ? "UNSUPPORTED" : "UNKNOWN";
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: renamed from: getUNKNOWN-Xp6DSB4, reason: not valid java name */
        public final int m260getUNKNOWNXp6DSB4() {
            return ConfigQueryResult.UNKNOWN;
        }

        /* JADX INFO: renamed from: getSUPPORTED-Xp6DSB4, reason: not valid java name */
        public final int m259getSUPPORTEDXp6DSB4() {
            return ConfigQueryResult.SUPPORTED;
        }
    }
}
