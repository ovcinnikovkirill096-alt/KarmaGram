package okio.internal;

import kotlin.jvm.internal.Intrinsics;
import okio.C0335SegmentedByteString;

/* JADX INFO: renamed from: okio.internal.-SegmentedByteString, reason: invalid class name */
public abstract class SegmentedByteString {
    public static final int binarySearch(int[] iArr, int i, int i2, int i3) {
        Intrinsics.checkNotNullParameter(iArr, "<this>");
        int i4 = i3 - 1;
        while (i2 <= i4) {
            int i5 = (i2 + i4) >>> 1;
            int i6 = iArr[i5];
            if (i6 < i) {
                i2 = i5 + 1;
            } else {
                if (i6 <= i) {
                    return i5;
                }
                i4 = i5 - 1;
            }
        }
        return (-i2) - 1;
    }

    public static final int segment(C0335SegmentedByteString c0335SegmentedByteString, int i) {
        Intrinsics.checkNotNullParameter(c0335SegmentedByteString, "<this>");
        int iBinarySearch = binarySearch(c0335SegmentedByteString.getDirectory$okio(), i + 1, 0, c0335SegmentedByteString.getSegments$okio().length);
        return iBinarySearch >= 0 ? iBinarySearch : ~iBinarySearch;
    }
}
