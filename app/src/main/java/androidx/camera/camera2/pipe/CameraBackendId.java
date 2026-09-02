package androidx.camera.camera2.pipe;

import kotlin.jvm.internal.Intrinsics;

public final class CameraBackendId {
    private final String value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ CameraBackendId m157boximpl(String str) {
        return new CameraBackendId(str);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static String m158constructorimpl(String value) {
        Intrinsics.checkNotNullParameter(value, "value");
        return value;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m159equalsimpl(String str, Object obj) {
        return (obj instanceof CameraBackendId) && Intrinsics.areEqual(str, ((CameraBackendId) obj).m163unboximpl());
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m160equalsimpl0(String str, String str2) {
        return Intrinsics.areEqual(str, str2);
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m161hashCodeimpl(String str) {
        return str.hashCode();
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m162toStringimpl(String str) {
        return "CameraBackendId(value=" + str + ')';
    }

    public boolean equals(Object obj) {
        return m159equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m161hashCodeimpl(this.value);
    }

    public String toString() {
        return m162toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ String m163unboximpl() {
        return this.value;
    }

    private /* synthetic */ CameraBackendId(String str) {
        this.value = str;
    }
}
