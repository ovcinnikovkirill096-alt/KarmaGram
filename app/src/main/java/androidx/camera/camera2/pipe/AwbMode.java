package androidx.camera.camera2.pipe;

import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;

public final class AwbMode {
    private static final int AUTO;
    private static final int CLOUDY_DAYLIGHT;
    public static final Companion Companion = new Companion(null);
    private static final int DAYLIGHT;
    private static final int FLUORESCENT;
    private static final int INCANDESCENT;
    private static final int OFF;
    private static final int SHADE;
    private static final int TWILIGHT;
    private static final List values;
    private final int value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ AwbMode m146boximpl(int i) {
        return new AwbMode(i);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int m147constructorimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m148equalsimpl(int i, Object obj) {
        return (obj instanceof AwbMode) && i == ((AwbMode) obj).m152unboximpl();
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m149hashCodeimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: isOn-impl, reason: not valid java name */
    public static final boolean m150isOnimpl(int i) {
        return i != 0;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m151toStringimpl(int i) {
        return "AwbMode(value=" + i + ')';
    }

    public boolean equals(Object obj) {
        return m148equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m149hashCodeimpl(this.value);
    }

    public String toString() {
        return m151toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int m152unboximpl() {
        return this.value;
    }

    private /* synthetic */ AwbMode(int i) {
        this.value = i;
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final List getValues() {
            return AwbMode.values;
        }

        /* JADX INFO: renamed from: fromIntOrNull--SaEiwI, reason: not valid java name */
        public final AwbMode m153fromIntOrNullSaEiwI(int i) {
            Object next;
            Iterator it = getValues().iterator();
            while (it.hasNext()) {
                next = it.next();
                if (((AwbMode) next).m152unboximpl() == i) {
                    return (AwbMode) next;
                }
            }
            next = null;
            return (AwbMode) next;
        }
    }

    static {
        int iM147constructorimpl = m147constructorimpl(0);
        OFF = iM147constructorimpl;
        int iM147constructorimpl2 = m147constructorimpl(1);
        AUTO = iM147constructorimpl2;
        int iM147constructorimpl3 = m147constructorimpl(6);
        CLOUDY_DAYLIGHT = iM147constructorimpl3;
        int iM147constructorimpl4 = m147constructorimpl(5);
        DAYLIGHT = iM147constructorimpl4;
        int iM147constructorimpl5 = m147constructorimpl(2);
        INCANDESCENT = iM147constructorimpl5;
        int iM147constructorimpl6 = m147constructorimpl(3);
        FLUORESCENT = iM147constructorimpl6;
        int iM147constructorimpl7 = m147constructorimpl(8);
        SHADE = iM147constructorimpl7;
        int iM147constructorimpl8 = m147constructorimpl(7);
        TWILIGHT = iM147constructorimpl8;
        values = CollectionsKt.listOf((Object[]) new AwbMode[]{m146boximpl(iM147constructorimpl), m146boximpl(iM147constructorimpl2), m146boximpl(iM147constructorimpl3), m146boximpl(iM147constructorimpl4), m146boximpl(iM147constructorimpl5), m146boximpl(iM147constructorimpl6), m146boximpl(iM147constructorimpl7), m146boximpl(iM147constructorimpl8)});
    }
}
