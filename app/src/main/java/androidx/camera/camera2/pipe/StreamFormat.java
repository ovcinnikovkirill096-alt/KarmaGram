package androidx.camera.camera2.pipe;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;

public final class StreamFormat {
    private final int value;
    public static final Companion Companion = new Companion(null);
    private static final int UNKNOWN = m404constructorimpl(0);
    private static final int PRIVATE = m404constructorimpl(34);
    private static final int DEPTH16 = m404constructorimpl(1144402265);
    private static final int DEPTH_JPEG = m404constructorimpl(1768253795);
    private static final int DEPTH_POINT_CLOUD = m404constructorimpl(257);
    private static final int FLEX_RGB_888 = m404constructorimpl(41);
    private static final int FLEX_RGBA_8888 = m404constructorimpl(42);
    private static final int HEIC = m404constructorimpl(1212500294);
    private static final int JPEG = m404constructorimpl(256);
    private static final int JPEG_R = m404constructorimpl(4101);
    private static final int NV16 = m404constructorimpl(16);
    private static final int NV21 = m404constructorimpl(17);
    private static final int RAW10 = m404constructorimpl(37);
    private static final int RAW12 = m404constructorimpl(38);
    private static final int RAW_DEPTH = m404constructorimpl(4098);
    private static final int RAW_PRIVATE = m404constructorimpl(36);
    private static final int RAW_SENSOR = m404constructorimpl(32);
    private static final int RGB_565 = m404constructorimpl(4);
    private static final int Y12 = m404constructorimpl(842094169);
    private static final int Y16 = m404constructorimpl(540422489);
    private static final int Y8 = m404constructorimpl(538982489);
    private static final int YCBCR_P010 = m404constructorimpl(54);
    private static final int YUV_420_888 = m404constructorimpl(35);
    private static final int YUV_422_888 = m404constructorimpl(39);
    private static final int YUV_444_888 = m404constructorimpl(40);
    private static final int YUY2 = m404constructorimpl(20);
    private static final int YV12 = m404constructorimpl(842094169);

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ StreamFormat m403boximpl(int i) {
        return new StreamFormat(i);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int m404constructorimpl(int i) {
        return i;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m405equalsimpl(int i, Object obj) {
        return (obj instanceof StreamFormat) && i == ((StreamFormat) obj).m410unboximpl();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m406equalsimpl0(int i, int i2) {
        return i == i2;
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m408hashCodeimpl(int i) {
        return i;
    }

    public boolean equals(Object obj) {
        return m405equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m408hashCodeimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int m410unboximpl() {
        return this.value;
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: renamed from: getUNKNOWN-8FPWQzE, reason: not valid java name */
        public final int m412getUNKNOWN8FPWQzE() {
            return StreamFormat.UNKNOWN;
        }

        /* JADX INFO: renamed from: getPRIVATE-8FPWQzE, reason: not valid java name */
        public final int m411getPRIVATE8FPWQzE() {
            return StreamFormat.PRIVATE;
        }
    }

    private /* synthetic */ StreamFormat(int i) {
        this.value = i;
    }

    public String toString() {
        return m409toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m409toStringimpl(int i) {
        return "StreamFormat(" + m407getNameimpl(i) + ')';
    }

    /* JADX INFO: renamed from: getName-impl, reason: not valid java name */
    public static final String m407getNameimpl(int i) {
        if (m406equalsimpl0(i, UNKNOWN)) {
            return "UNKNOWN";
        }
        if (m406equalsimpl0(i, PRIVATE)) {
            return "PRIVATE";
        }
        if (m406equalsimpl0(i, DEPTH16)) {
            return "DEPTH16";
        }
        if (m406equalsimpl0(i, DEPTH_JPEG)) {
            return "DEPTH_JPEG";
        }
        if (m406equalsimpl0(i, DEPTH_POINT_CLOUD)) {
            return "DEPTH_POINT_CLOUD";
        }
        if (m406equalsimpl0(i, FLEX_RGB_888)) {
            return "FLEX_RGB_888";
        }
        if (m406equalsimpl0(i, FLEX_RGBA_8888)) {
            return "FLEX_RGBA_8888";
        }
        if (m406equalsimpl0(i, HEIC)) {
            return "HEIC";
        }
        if (m406equalsimpl0(i, JPEG)) {
            return "JPEG";
        }
        if (m406equalsimpl0(i, JPEG_R)) {
            return "JPEG_R";
        }
        if (m406equalsimpl0(i, NV16)) {
            return "NV16";
        }
        if (m406equalsimpl0(i, NV21)) {
            return "NV21";
        }
        if (m406equalsimpl0(i, RAW10)) {
            return "RAW10";
        }
        if (m406equalsimpl0(i, RAW12)) {
            return "RAW12";
        }
        if (m406equalsimpl0(i, RAW_DEPTH)) {
            return "RAW_DEPTH";
        }
        if (m406equalsimpl0(i, RAW_PRIVATE)) {
            return "RAW_PRIVATE";
        }
        if (m406equalsimpl0(i, RAW_SENSOR)) {
            return "RAW_SENSOR";
        }
        if (m406equalsimpl0(i, RGB_565)) {
            return "RGB_565";
        }
        if (m406equalsimpl0(i, Y12)) {
            return "Y12";
        }
        if (m406equalsimpl0(i, Y16)) {
            return "Y16";
        }
        if (m406equalsimpl0(i, Y8)) {
            return "Y8";
        }
        if (m406equalsimpl0(i, YCBCR_P010)) {
            return "YCBCR_P010";
        }
        if (m406equalsimpl0(i, YUV_420_888)) {
            return "YUV_420_888";
        }
        if (m406equalsimpl0(i, YUV_422_888)) {
            return "YUV_422_888";
        }
        if (m406equalsimpl0(i, YUV_444_888)) {
            return "YUV_444_888";
        }
        if (m406equalsimpl0(i, YUY2)) {
            return "YUY2";
        }
        if (m406equalsimpl0(i, YV12)) {
            return "YV12";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("UNKNOWN(");
        String string = Integer.toString(i, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        sb.append(string);
        sb.append(')');
        return sb.toString();
    }
}
