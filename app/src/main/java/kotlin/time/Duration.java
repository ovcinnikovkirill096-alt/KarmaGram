package kotlin.time;

import androidx.camera.camera2.pipe.CameraTimestamp$$ExternalSyntheticBackport0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import okhttp3.internal.http2.Http2Connection;
import org.mvel2.asm.signature.SignatureVisitor;
import org.telegram.messenger.MediaDataController;

public final class Duration implements Comparable {
    private final long rawValue;
    public static final Companion Companion = new Companion(null);
    private static final long ZERO = m2462constructorimpl(0);
    private static final long INFINITE = DurationKt.durationOfMillis(4611686018427387903L);
    private static final long NEG_INFINITE = DurationKt.durationOfMillis(-4611686018427387903L);

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ Duration m2460boximpl(long j) {
        return new Duration(j);
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m2463equalsimpl(long j, Object obj) {
        return (obj instanceof Duration) && j == ((Duration) obj).m2488unboximpl();
    }

    /* JADX INFO: renamed from: getValue-impl, reason: not valid java name */
    private static final long m2475getValueimpl(long j) {
        return j >> 1;
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m2476hashCodeimpl(long j) {
        return CameraTimestamp$$ExternalSyntheticBackport0.m(j);
    }

    /* JADX INFO: renamed from: isInMillis-impl, reason: not valid java name */
    private static final boolean m2478isInMillisimpl(long j) {
        return (((int) j) & 1) == 1;
    }

    /* JADX INFO: renamed from: isInNanos-impl, reason: not valid java name */
    private static final boolean m2479isInNanosimpl(long j) {
        return (((int) j) & 1) == 0;
    }

    /* JADX INFO: renamed from: isNegative-impl, reason: not valid java name */
    public static final boolean m2481isNegativeimpl(long j) {
        return j < 0;
    }

    /* JADX INFO: renamed from: isPositive-impl, reason: not valid java name */
    public static final boolean m2482isPositiveimpl(long j) {
        return j > 0;
    }

    public boolean equals(Object obj) {
        return m2463equalsimpl(this.rawValue, obj);
    }

    public int hashCode() {
        return m2476hashCodeimpl(this.rawValue);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ long m2488unboximpl() {
        return this.rawValue;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(Object obj) {
        return m2487compareToLRDsOJo(((Duration) obj).m2488unboximpl());
    }

    private /* synthetic */ Duration(long j) {
        this.rawValue = j;
    }

    /* JADX INFO: renamed from: getStorageUnit-impl, reason: not valid java name */
    private static final DurationUnit m2474getStorageUnitimpl(long j) {
        return m2479isInNanosimpl(j) ? DurationUnit.NANOSECONDS : DurationUnit.MILLISECONDS;
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static long m2462constructorimpl(long j) {
        if (!DurationJvmKt.getDurationAssertionsEnabled()) {
            return j;
        }
        if (m2479isInNanosimpl(j)) {
            long jM2475getValueimpl = m2475getValueimpl(j);
            if (-4611686018426999999L <= jM2475getValueimpl && jM2475getValueimpl < 4611686018427000000L) {
                return j;
            }
            throw new AssertionError(m2475getValueimpl(j) + " ns is out of nanoseconds range");
        }
        long jM2475getValueimpl2 = m2475getValueimpl(j);
        if (-4611686018427387903L > jM2475getValueimpl2 || jM2475getValueimpl2 >= 4611686018427387904L) {
            throw new AssertionError(m2475getValueimpl(j) + " ms is out of milliseconds range");
        }
        long jM2475getValueimpl3 = m2475getValueimpl(j);
        if (-4611686018426L > jM2475getValueimpl3 || jM2475getValueimpl3 >= 4611686018427L) {
            return j;
        }
        throw new AssertionError(m2475getValueimpl(j) + " ms is denormalized");
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: renamed from: unaryMinus-UwyO8pc, reason: not valid java name */
    public static final long m2486unaryMinusUwyO8pc(long j) {
        return DurationKt.durationOf(-m2475getValueimpl(j), ((int) j) & 1);
    }

    /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
    public static final long m2483plusLRDsOJo(long j, long j2) {
        if (m2480isInfiniteimpl(j)) {
            if (m2477isFiniteimpl(j2) || (j2 ^ j) >= 0) {
                return j;
            }
            throw new IllegalArgumentException("Summing infinite durations of different signs yields an undefined result.");
        }
        if (m2480isInfiniteimpl(j2)) {
            return j2;
        }
        if ((((int) j) & 1) == (((int) j2) & 1)) {
            long jM2475getValueimpl = m2475getValueimpl(j) + m2475getValueimpl(j2);
            return m2479isInNanosimpl(j) ? DurationKt.durationOfNanosNormalized(jM2475getValueimpl) : DurationKt.durationOfMillisNormalized(jM2475getValueimpl);
        }
        if (m2478isInMillisimpl(j)) {
            return m2458addValuesMixedRangesUwyO8pc(j, m2475getValueimpl(j), m2475getValueimpl(j2));
        }
        return m2458addValuesMixedRangesUwyO8pc(j, m2475getValueimpl(j2), m2475getValueimpl(j));
    }

    /* JADX INFO: renamed from: addValuesMixedRanges-UwyO8pc, reason: not valid java name */
    private static final long m2458addValuesMixedRangesUwyO8pc(long j, long j2, long j3) {
        long jNanosToMillis = DurationKt.nanosToMillis(j3);
        long j4 = j2 + jNanosToMillis;
        if (-4611686018426L > j4 || j4 >= 4611686018427L) {
            return DurationKt.durationOfMillis(RangesKt.coerceIn(j4, -4611686018427387903L, 4611686018427387903L));
        }
        return DurationKt.durationOfNanos(DurationKt.millisToNanos(j4) + (j3 - DurationKt.millisToNanos(jNanosToMillis)));
    }

    /* JADX INFO: renamed from: isInfinite-impl, reason: not valid java name */
    public static final boolean m2480isInfiniteimpl(long j) {
        return j == INFINITE || j == NEG_INFINITE;
    }

    /* JADX INFO: renamed from: isFinite-impl, reason: not valid java name */
    public static final boolean m2477isFiniteimpl(long j) {
        return !m2480isInfiniteimpl(j);
    }

    /* JADX INFO: renamed from: getAbsoluteValue-UwyO8pc, reason: not valid java name */
    public static final long m2464getAbsoluteValueUwyO8pc(long j) {
        return m2481isNegativeimpl(j) ? m2486unaryMinusUwyO8pc(j) : j;
    }

    /* JADX INFO: renamed from: compareTo-LRDsOJo, reason: not valid java name */
    public int m2487compareToLRDsOJo(long j) {
        return m2461compareToLRDsOJo(this.rawValue, j);
    }

    /* JADX INFO: renamed from: compareTo-LRDsOJo, reason: not valid java name */
    public static int m2461compareToLRDsOJo(long j, long j2) {
        long j3 = j ^ j2;
        if (j3 < 0 || (((int) j3) & 1) == 0) {
            return Intrinsics.compare(j, j2);
        }
        int i = (((int) j) & 1) - (((int) j2) & 1);
        return m2481isNegativeimpl(j) ? -i : i;
    }

    /* JADX INFO: renamed from: getHoursComponent-impl, reason: not valid java name */
    public static final int m2465getHoursComponentimpl(long j) {
        if (m2480isInfiniteimpl(j)) {
            return 0;
        }
        return (int) (m2467getInWholeHoursimpl(j) % ((long) 24));
    }

    /* JADX INFO: renamed from: getMinutesComponent-impl, reason: not valid java name */
    public static final int m2471getMinutesComponentimpl(long j) {
        if (m2480isInfiniteimpl(j)) {
            return 0;
        }
        return (int) (m2469getInWholeMinutesimpl(j) % ((long) 60));
    }

    /* JADX INFO: renamed from: getSecondsComponent-impl, reason: not valid java name */
    public static final int m2473getSecondsComponentimpl(long j) {
        if (m2480isInfiniteimpl(j)) {
            return 0;
        }
        return (int) (m2470getInWholeSecondsimpl(j) % ((long) 60));
    }

    /* JADX INFO: renamed from: getNanosecondsComponent-impl, reason: not valid java name */
    public static final int m2472getNanosecondsComponentimpl(long j) {
        long jM2475getValueimpl;
        if (m2480isInfiniteimpl(j)) {
            return 0;
        }
        if (m2478isInMillisimpl(j)) {
            jM2475getValueimpl = DurationKt.millisToNanos(m2475getValueimpl(j) % ((long) MediaDataController.MAX_STYLE_RUNS_COUNT));
        } else {
            jM2475getValueimpl = m2475getValueimpl(j) % ((long) Http2Connection.DEGRADED_PONG_TIMEOUT_NS);
        }
        return (int) jM2475getValueimpl;
    }

    /* JADX INFO: renamed from: toLong-impl, reason: not valid java name */
    public static final long m2484toLongimpl(long j, DurationUnit unit) {
        Intrinsics.checkNotNullParameter(unit, "unit");
        if (j == INFINITE) {
            return Long.MAX_VALUE;
        }
        if (j == NEG_INFINITE) {
            return Long.MIN_VALUE;
        }
        return DurationUnitKt__DurationUnitJvmKt.convertDurationUnit(m2475getValueimpl(j), m2474getStorageUnitimpl(j), unit);
    }

    /* JADX INFO: renamed from: getInWholeDays-impl, reason: not valid java name */
    public static final long m2466getInWholeDaysimpl(long j) {
        return m2484toLongimpl(j, DurationUnit.DAYS);
    }

    /* JADX INFO: renamed from: getInWholeHours-impl, reason: not valid java name */
    public static final long m2467getInWholeHoursimpl(long j) {
        return m2484toLongimpl(j, DurationUnit.HOURS);
    }

    /* JADX INFO: renamed from: getInWholeMinutes-impl, reason: not valid java name */
    public static final long m2469getInWholeMinutesimpl(long j) {
        return m2484toLongimpl(j, DurationUnit.MINUTES);
    }

    /* JADX INFO: renamed from: getInWholeSeconds-impl, reason: not valid java name */
    public static final long m2470getInWholeSecondsimpl(long j) {
        return m2484toLongimpl(j, DurationUnit.SECONDS);
    }

    /* JADX INFO: renamed from: getInWholeMilliseconds-impl, reason: not valid java name */
    public static final long m2468getInWholeMillisecondsimpl(long j) {
        return (m2478isInMillisimpl(j) && m2477isFiniteimpl(j)) ? m2475getValueimpl(j) : m2484toLongimpl(j, DurationUnit.MILLISECONDS);
    }

    public String toString() {
        return m2485toStringimpl(this.rawValue);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m2485toStringimpl(long j) {
        if (j == 0) {
            return "0s";
        }
        if (j == INFINITE) {
            return "Infinity";
        }
        if (j == NEG_INFINITE) {
            return "-Infinity";
        }
        boolean zM2481isNegativeimpl = m2481isNegativeimpl(j);
        StringBuilder sb = new StringBuilder();
        if (zM2481isNegativeimpl) {
            sb.append(SignatureVisitor.SUPER);
        }
        long jM2464getAbsoluteValueUwyO8pc = m2464getAbsoluteValueUwyO8pc(j);
        long jM2466getInWholeDaysimpl = m2466getInWholeDaysimpl(jM2464getAbsoluteValueUwyO8pc);
        int iM2465getHoursComponentimpl = m2465getHoursComponentimpl(jM2464getAbsoluteValueUwyO8pc);
        int iM2471getMinutesComponentimpl = m2471getMinutesComponentimpl(jM2464getAbsoluteValueUwyO8pc);
        int iM2473getSecondsComponentimpl = m2473getSecondsComponentimpl(jM2464getAbsoluteValueUwyO8pc);
        int iM2472getNanosecondsComponentimpl = m2472getNanosecondsComponentimpl(jM2464getAbsoluteValueUwyO8pc);
        int i = 0;
        boolean z = jM2466getInWholeDaysimpl != 0;
        boolean z2 = iM2465getHoursComponentimpl != 0;
        boolean z3 = iM2471getMinutesComponentimpl != 0;
        boolean z4 = (iM2473getSecondsComponentimpl == 0 && iM2472getNanosecondsComponentimpl == 0) ? false : true;
        if (z) {
            sb.append(jM2466getInWholeDaysimpl);
            sb.append('d');
            i = 1;
        }
        if (z2 || (z && (z3 || z4))) {
            int i2 = i + 1;
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(iM2465getHoursComponentimpl);
            sb.append('h');
            i = i2;
        }
        if (z3 || (z4 && (z2 || z))) {
            int i3 = i + 1;
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(iM2471getMinutesComponentimpl);
            sb.append('m');
            i = i3;
        }
        if (z4) {
            int i4 = i + 1;
            if (i > 0) {
                sb.append(' ');
            }
            if (iM2473getSecondsComponentimpl != 0 || z || z2 || z3) {
                m2459appendFractionalimpl(j, sb, iM2473getSecondsComponentimpl, iM2472getNanosecondsComponentimpl, 9, "s", false);
            } else if (iM2472getNanosecondsComponentimpl >= 1000000) {
                m2459appendFractionalimpl(j, sb, iM2472getNanosecondsComponentimpl / 1000000, iM2472getNanosecondsComponentimpl % 1000000, 6, "ms", false);
            } else if (iM2472getNanosecondsComponentimpl >= 1000) {
                m2459appendFractionalimpl(j, sb, iM2472getNanosecondsComponentimpl / MediaDataController.MAX_STYLE_RUNS_COUNT, iM2472getNanosecondsComponentimpl % MediaDataController.MAX_STYLE_RUNS_COUNT, 3, "us", false);
            } else {
                sb.append(iM2472getNanosecondsComponentimpl);
                sb.append("ns");
            }
            i = i4;
        }
        if (zM2481isNegativeimpl && i > 1) {
            sb.insert(1, '(').append(')');
        }
        return sb.toString();
    }

    /* JADX INFO: renamed from: appendFractional-impl, reason: not valid java name */
    private static final void m2459appendFractionalimpl(long j, StringBuilder sb, int i, int i2, int i3, String str, boolean z) {
        sb.append(i);
        if (i2 != 0) {
            sb.append('.');
            String strPadStart = StringsKt.padStart(String.valueOf(i2), i3, '0');
            int i4 = -1;
            int length = strPadStart.length() - 1;
            if (length >= 0) {
                while (true) {
                    int i5 = length - 1;
                    if (strPadStart.charAt(length) != '0') {
                        i4 = length;
                        break;
                    } else if (i5 < 0) {
                        break;
                    } else {
                        length = i5;
                    }
                }
            }
            int i6 = i4 + 1;
            if (!z && i6 < 3) {
                sb.append((CharSequence) strPadStart, 0, i6);
                Intrinsics.checkNotNullExpressionValue(sb, "append(...)");
            } else {
                sb.append((CharSequence) strPadStart, 0, ((i4 + 3) / 3) * 3);
                Intrinsics.checkNotNullExpressionValue(sb, "append(...)");
            }
        }
        sb.append(str);
    }
}
