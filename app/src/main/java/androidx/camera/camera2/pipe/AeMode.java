package androidx.camera.camera2.pipe;

import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;

public final class AeMode {
    public static final Companion Companion = new Companion(null);
    private static final int OFF;
    private static final int ON;
    private static final int ON_ALWAYS_FLASH;
    private static final int ON_AUTO_FLASH;
    private static final int ON_AUTO_FLASH_REDEYE;
    private static final int ON_EXTERNAL_FLASH;
    private static final int ON_LOW_LIGHT_BOOST_BRIGHTNESS_PRIORITY;
    private static final List values;
    private final int value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ AeMode m115boximpl(int i) {
        return new AeMode(i);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int m116constructorimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m117equalsimpl(int i, Object obj) {
        return (obj instanceof AeMode) && i == ((AeMode) obj).m122unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m118equalsimpl0(int i, int i2) {
        return i == i2;
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m119hashCodeimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: isOn-impl, reason: not valid java name */
    public static final boolean m120isOnimpl(int i) {
        return i != 0;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m121toStringimpl(int i) {
        return "AeMode(value=" + i + ')';
    }

    public boolean equals(Object obj) {
        return m117equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m119hashCodeimpl(this.value);
    }

    public String toString() {
        return m121toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int m122unboximpl() {
        return this.value;
    }

    private /* synthetic */ AeMode(int i) {
        this.value = i;
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: renamed from: getOFF-bOjpiJc, reason: not valid java name */
        public final int m124getOFFbOjpiJc() {
            return AeMode.OFF;
        }

        /* JADX INFO: renamed from: getON-bOjpiJc, reason: not valid java name */
        public final int m125getONbOjpiJc() {
            return AeMode.ON;
        }

        public final List getValues() {
            return AeMode.values;
        }

        /* JADX INFO: renamed from: fromIntOrNull-kQd0u18, reason: not valid java name */
        public final AeMode m123fromIntOrNullkQd0u18(int i) {
            Object next;
            Iterator it = getValues().iterator();
            while (it.hasNext()) {
                next = it.next();
                if (((AeMode) next).m122unboximpl() == i) {
                    return (AeMode) next;
                }
            }
            next = null;
            return (AeMode) next;
        }
    }

    static {
        int iM116constructorimpl = m116constructorimpl(0);
        OFF = iM116constructorimpl;
        int iM116constructorimpl2 = m116constructorimpl(1);
        ON = iM116constructorimpl2;
        int iM116constructorimpl3 = m116constructorimpl(3);
        ON_ALWAYS_FLASH = iM116constructorimpl3;
        int iM116constructorimpl4 = m116constructorimpl(2);
        ON_AUTO_FLASH = iM116constructorimpl4;
        int iM116constructorimpl5 = m116constructorimpl(4);
        ON_AUTO_FLASH_REDEYE = iM116constructorimpl5;
        int iM116constructorimpl6 = m116constructorimpl(5);
        ON_EXTERNAL_FLASH = iM116constructorimpl6;
        int iM116constructorimpl7 = m116constructorimpl(6);
        ON_LOW_LIGHT_BOOST_BRIGHTNESS_PRIORITY = iM116constructorimpl7;
        values = CollectionsKt.listOf((Object[]) new AeMode[]{m115boximpl(iM116constructorimpl), m115boximpl(iM116constructorimpl2), m115boximpl(iM116constructorimpl4), m115boximpl(iM116constructorimpl3), m115boximpl(iM116constructorimpl5), m115boximpl(iM116constructorimpl6), m115boximpl(iM116constructorimpl7)});
    }
}
