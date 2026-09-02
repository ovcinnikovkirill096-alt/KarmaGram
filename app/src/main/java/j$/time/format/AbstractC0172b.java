package j$.time.format;

/* JADX INFO: renamed from: j$.time.format.b, reason: case insensitive filesystem */
public abstract /* synthetic */ class AbstractC0172b {
    public static final /* synthetic */ int[] a;

    static {
        int[] iArr = new int[F.values().length];
        a = iArr;
        try {
            iArr[F.EXCEEDS_PAD.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            a[F.ALWAYS.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            a[F.NORMAL.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            a[F.NOT_NEGATIVE.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
    }
}
