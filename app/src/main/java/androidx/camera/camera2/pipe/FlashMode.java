package androidx.camera.camera2.pipe;

import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;

public final class FlashMode {
    public static final Companion Companion = new Companion(null);
    private static final int OFF;
    private static final int SINGLE;
    private static final int TORCH;
    private static final List values;
    private final int value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ FlashMode m261boximpl(int i) {
        return new FlashMode(i);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int m262constructorimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m263equalsimpl(int i, Object obj) {
        return (obj instanceof FlashMode) && i == ((FlashMode) obj).m266unboximpl();
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m264hashCodeimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m265toStringimpl(int i) {
        return "FlashMode(value=" + i + ')';
    }

    public boolean equals(Object obj) {
        return m263equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m264hashCodeimpl(this.value);
    }

    public String toString() {
        return m265toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int m266unboximpl() {
        return this.value;
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: renamed from: getOFF-Le5xUZU, reason: not valid java name */
        public final int m267getOFFLe5xUZU() {
            return FlashMode.OFF;
        }

        /* JADX INFO: renamed from: getTORCH-Le5xUZU, reason: not valid java name */
        public final int m268getTORCHLe5xUZU() {
            return FlashMode.TORCH;
        }
    }

    private /* synthetic */ FlashMode(int i) {
        this.value = i;
    }

    static {
        int iM262constructorimpl = m262constructorimpl(0);
        OFF = iM262constructorimpl;
        int iM262constructorimpl2 = m262constructorimpl(1);
        SINGLE = iM262constructorimpl2;
        int iM262constructorimpl3 = m262constructorimpl(2);
        TORCH = iM262constructorimpl3;
        values = CollectionsKt.listOf((Object[]) new FlashMode[]{m261boximpl(iM262constructorimpl), m261boximpl(iM262constructorimpl2), m261boximpl(iM262constructorimpl3)});
    }
}
