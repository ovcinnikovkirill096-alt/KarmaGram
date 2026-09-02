package androidx.camera.camera2.pipe;

import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;

public final class AfMode {
    private static final int AUTO;
    private static final int CONTINUOUS_PICTURE;
    private static final int CONTINUOUS_VIDEO;
    public static final Companion Companion = new Companion(null);
    private static final int EDOF;
    private static final int MACRO;
    private static final int OFF;
    private static final List values;
    private final int value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ AfMode m126boximpl(int i) {
        return new AfMode(i);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int m127constructorimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m128equalsimpl(int i, Object obj) {
        return (obj instanceof AfMode) && i == ((AfMode) obj).m133unboximpl();
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m129hashCodeimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: isContinuous-impl, reason: not valid java name */
    public static final boolean m130isContinuousimpl(int i) {
        return i == 3 || i == 4;
    }

    /* JADX INFO: renamed from: isOn-impl, reason: not valid java name */
    public static final boolean m131isOnimpl(int i) {
        return i != 0;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m132toStringimpl(int i) {
        return "AfMode(value=" + i + ')';
    }

    public boolean equals(Object obj) {
        return m128equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m129hashCodeimpl(this.value);
    }

    public String toString() {
        return m132toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int m133unboximpl() {
        return this.value;
    }

    private /* synthetic */ AfMode(int i) {
        this.value = i;
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: renamed from: getAUTO-vHZNRtE, reason: not valid java name */
        public final int m135getAUTOvHZNRtE() {
            return AfMode.AUTO;
        }

        public final List getValues() {
            return AfMode.values;
        }

        /* JADX INFO: renamed from: fromIntOrNull-MKXwA8g, reason: not valid java name */
        public final AfMode m134fromIntOrNullMKXwA8g(int i) {
            Object next;
            Iterator it = getValues().iterator();
            while (it.hasNext()) {
                next = it.next();
                if (((AfMode) next).m133unboximpl() == i) {
                    return (AfMode) next;
                }
            }
            next = null;
            return (AfMode) next;
        }
    }

    static {
        int iM127constructorimpl = m127constructorimpl(0);
        OFF = iM127constructorimpl;
        int iM127constructorimpl2 = m127constructorimpl(1);
        AUTO = iM127constructorimpl2;
        int iM127constructorimpl3 = m127constructorimpl(2);
        MACRO = iM127constructorimpl3;
        int iM127constructorimpl4 = m127constructorimpl(3);
        CONTINUOUS_VIDEO = iM127constructorimpl4;
        int iM127constructorimpl5 = m127constructorimpl(4);
        CONTINUOUS_PICTURE = iM127constructorimpl5;
        int iM127constructorimpl6 = m127constructorimpl(5);
        EDOF = iM127constructorimpl6;
        values = CollectionsKt.listOf((Object[]) new AfMode[]{m126boximpl(iM127constructorimpl), m126boximpl(iM127constructorimpl2), m126boximpl(iM127constructorimpl3), m126boximpl(iM127constructorimpl4), m126boximpl(iM127constructorimpl5), m126boximpl(iM127constructorimpl6)});
    }
}
