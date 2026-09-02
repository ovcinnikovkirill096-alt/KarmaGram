package j$.util.stream;

/* JADX INFO: renamed from: j$.util.stream.z2, reason: case insensitive filesystem */
public abstract /* synthetic */ class AbstractC0323z2 {
    public static final /* synthetic */ int[] a;

    static {
        int[] iArr = new int[EnumC0220e3.values().length];
        a = iArr;
        try {
            iArr[EnumC0220e3.REFERENCE.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            a[EnumC0220e3.INT_VALUE.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            a[EnumC0220e3.LONG_VALUE.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            a[EnumC0220e3.DOUBLE_VALUE.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
    }
}
