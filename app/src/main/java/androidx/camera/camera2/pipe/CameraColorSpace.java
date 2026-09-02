package androidx.camera.camera2.pipe;

import android.graphics.ColorSpace;
import android.os.Build;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public abstract class CameraColorSpace {
    public static final Companion Companion = new Companion(null);
    private static final String UNKNOWN = m165constructorimpl("UNKNOWN");
    private static final String SRGB = m165constructorimpl("SRGB");
    private static final String LINEAR_SRGB = m165constructorimpl("LINEAR_SRGB");
    private static final String EXTENDED_SRGB = m165constructorimpl("EXTENDED_SRGB");
    private static final String LINEAR_EXTENDED_SRGB = m165constructorimpl("LINEAR_EXTENDED_SRGB");
    private static final String BT709 = m165constructorimpl("BT709");
    private static final String BT2020 = m165constructorimpl("BT2020");
    private static final String DCI_P3 = m165constructorimpl("DCI_P3");
    private static final String DISPLAY_P3 = m165constructorimpl("DISPLAY_P3");
    private static final String NTSC_1953 = m165constructorimpl("NTSC_1953");
    private static final String SMPTE_C = m165constructorimpl("SMPTE_C");
    private static final String ADOBE_RGB = m165constructorimpl("ADOBE_RGB");
    private static final String PRO_PHOTO_RGB = m165constructorimpl("PRO_PHOTO_RGB");
    private static final String ACES = m165constructorimpl("ACES");
    private static final String ACESCG = m165constructorimpl("ACESCG");
    private static final String CIE_XYZ = m165constructorimpl("CIE_XYZ");
    private static final String CIE_LAB = m165constructorimpl("CIE_LAB");
    private static final String BT2020_HLG = m165constructorimpl("BT2020_HLG");
    private static final String BT2020_PQ = m165constructorimpl("BT2020_PQ");

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    private static String m165constructorimpl(String str) {
        return str;
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m166equalsimpl0(String str, String str2) {
        return Intrinsics.areEqual(str, str2);
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m167hashCodeimpl(String str) {
        return str.hashCode();
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m169toStringimpl(String str) {
        return "CameraColorSpace(colorSpaceName=" + str + ')';
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: renamed from: toColorSpaceNamed-impl, reason: not valid java name */
    public static final ColorSpace.Named m168toColorSpaceNamedimpl(String str) {
        if (m166equalsimpl0(str, UNKNOWN)) {
            return null;
        }
        if (m166equalsimpl0(str, SRGB)) {
            return ColorSpace.Named.SRGB;
        }
        if (m166equalsimpl0(str, LINEAR_SRGB)) {
            return ColorSpace.Named.LINEAR_SRGB;
        }
        if (m166equalsimpl0(str, EXTENDED_SRGB)) {
            return ColorSpace.Named.EXTENDED_SRGB;
        }
        if (m166equalsimpl0(str, LINEAR_EXTENDED_SRGB)) {
            return ColorSpace.Named.LINEAR_EXTENDED_SRGB;
        }
        if (m166equalsimpl0(str, BT709)) {
            return ColorSpace.Named.BT709;
        }
        if (m166equalsimpl0(str, BT2020)) {
            return ColorSpace.Named.BT2020;
        }
        if (m166equalsimpl0(str, DCI_P3)) {
            return ColorSpace.Named.DCI_P3;
        }
        if (m166equalsimpl0(str, DISPLAY_P3)) {
            return ColorSpace.Named.DISPLAY_P3;
        }
        if (m166equalsimpl0(str, NTSC_1953)) {
            return ColorSpace.Named.NTSC_1953;
        }
        if (m166equalsimpl0(str, SMPTE_C)) {
            return ColorSpace.Named.SMPTE_C;
        }
        if (m166equalsimpl0(str, ADOBE_RGB)) {
            return ColorSpace.Named.ADOBE_RGB;
        }
        if (m166equalsimpl0(str, PRO_PHOTO_RGB)) {
            return ColorSpace.Named.PRO_PHOTO_RGB;
        }
        if (m166equalsimpl0(str, ACES)) {
            return ColorSpace.Named.ACES;
        }
        if (m166equalsimpl0(str, ACESCG)) {
            return ColorSpace.Named.ACESCG;
        }
        if (m166equalsimpl0(str, CIE_XYZ)) {
            return ColorSpace.Named.CIE_XYZ;
        }
        if (m166equalsimpl0(str, CIE_LAB)) {
            return ColorSpace.Named.CIE_LAB;
        }
        if (Build.VERSION.SDK_INT < 34) {
            return null;
        }
        if (m166equalsimpl0(str, BT2020_HLG)) {
            return ColorSpace.Named.BT2020_HLG;
        }
        if (m166equalsimpl0(str, BT2020_PQ)) {
            return ColorSpace.Named.BT2020_PQ;
        }
        return null;
    }
}
