package androidx.graphics.shapes;

import androidx.collection.FloatFloatPair;
import java.util.Arrays;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public class Cubic {
    public static final Companion Companion = new Companion(null);
    private final float[] points;

    public /* synthetic */ Cubic(long j, long j2, long j3, long j4, DefaultConstructorMarker defaultConstructorMarker) {
        this(j, j2, j3, j4);
    }

    public Cubic(float[] points) {
        Intrinsics.checkNotNullParameter(points, "points");
        this.points = points;
        if (points.length != 8) {
            throw new IllegalArgumentException("Points array size should be 8");
        }
    }

    public /* synthetic */ Cubic(float[] fArr, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? new float[8] : fArr);
    }

    public final float[] getPoints$graphics_shapes_release() {
        return this.points;
    }

    public final float getAnchor0X() {
        return this.points[0];
    }

    public final float getAnchor0Y() {
        return this.points[1];
    }

    public final float getControl0X() {
        return this.points[2];
    }

    public final float getControl0Y() {
        return this.points[3];
    }

    public final float getControl1X() {
        return this.points[4];
    }

    public final float getControl1Y() {
        return this.points[5];
    }

    public final float getAnchor1X() {
        return this.points[6];
    }

    public final float getAnchor1Y() {
        return this.points[7];
    }

    private Cubic(long j, long j2, long j3, long j4) {
        this(new float[]{PointKt.m751getXDnnuFBc(j), PointKt.m752getYDnnuFBc(j), PointKt.m751getXDnnuFBc(j2), PointKt.m752getYDnnuFBc(j2), PointKt.m751getXDnnuFBc(j3), PointKt.m752getYDnnuFBc(j3), PointKt.m751getXDnnuFBc(j4), PointKt.m752getYDnnuFBc(j4)});
    }

    /* JADX INFO: renamed from: pointOnCurve-OOQOV4g$graphics_shapes_release, reason: not valid java name */
    public final long m744pointOnCurveOOQOV4g$graphics_shapes_release(float f) {
        float f2 = 1 - f;
        float f3 = f2 * f2 * f2;
        float f4 = 3 * f;
        float f5 = f4 * f2 * f2;
        float f6 = f4 * f * f2;
        float f7 = f * f * f;
        return FloatFloatPair.m675constructorimpl((getAnchor0X() * f3) + (getControl0X() * f5) + (getControl1X() * f6) + (getAnchor1X() * f7), (getAnchor0Y() * f3) + (getControl0Y() * f5) + (getControl1Y() * f6) + (getAnchor1Y() * f7));
    }

    public final boolean zeroLength$graphics_shapes_release() {
        return Math.abs(getAnchor0X() - getAnchor1X()) < 1.0E-4f && Math.abs(getAnchor0Y() - getAnchor1Y()) < 1.0E-4f;
    }

    private final boolean zeroIsh(float f) {
        return Math.abs(f) < 1.0E-4f;
    }

    /* JADX WARN: Code duplicated, block: B:50:0x01a4  */
    /* JADX WARN: Code duplicated, block: B:53:0x01aa  */
    /* JADX WARN: Code duplicated, block: B:63:0x01c9 A[PHI: r3 r7
  0x01c9: PHI (r3v15 float) = (r3v12 float), (r3v21 float) binds: [B:85:0x0218, B:62:0x01c7] A[DONT_GENERATE, DONT_INLINE]
  0x01c9: PHI (r7v14 float) = (r7v10 float), (r7v16 float) binds: [B:85:0x0218, B:62:0x01c7] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:64:0x01cb  */
    /* JADX WARN: Code duplicated, block: B:66:0x01d4  */
    /* JADX WARN: Code duplicated, block: B:72:0x01f2  */
    /* JADX WARN: Code duplicated, block: B:75:0x01f7  */
    /* JADX WARN: Code duplicated, block: B:87:0x021b A[PHI: r9 r15
  0x021b: PHI (r9v5 float) = (r9v2 float), (r9v3 float), (r9v3 float), (r9v2 float), (r9v2 float), (r9v2 float) binds: [B:65:0x01d2, B:77:0x0201, B:79:0x0205, B:51:0x01a6, B:54:0x01b0, B:56:0x01b4] A[DONT_GENERATE, DONT_INLINE]
  0x021b: PHI (r15v13 float) = (r15v9 float), (r15v10 float), (r15v10 float), (r15v9 float), (r15v9 float), (r15v9 float) binds: [B:65:0x01d2, B:77:0x0201, B:79:0x0205, B:51:0x01a6, B:54:0x01b0, B:56:0x01b4] A[DONT_GENERATE, DONT_INLINE]] */
    public final void calculateBounds$graphics_shapes_release(float[] bounds, boolean z) {
        char c;
        char c2;
        char c3;
        float f;
        float control0Y;
        float anchor0Y;
        float control0Y2;
        float f2;
        float fSqrt;
        float fSqrt2;
        float fM752getYDnnuFBc;
        float f3;
        float fM752getYDnnuFBc2;
        float f4;
        Intrinsics.checkNotNullParameter(bounds, "bounds");
        if (zeroLength$graphics_shapes_release()) {
            bounds[0] = getAnchor0X();
            bounds[1] = getAnchor0Y();
            bounds[2] = getAnchor0X();
            bounds[3] = getAnchor0Y();
            return;
        }
        float fMin = Math.min(getAnchor0X(), getAnchor1X());
        float fMin2 = Math.min(getAnchor0Y(), getAnchor1Y());
        float fMax = Math.max(getAnchor0X(), getAnchor1X());
        float fMax2 = Math.max(getAnchor0Y(), getAnchor1Y());
        if (z) {
            bounds[0] = Math.min(fMin, Math.min(getControl0X(), getControl1X()));
            bounds[1] = Math.min(fMin2, Math.min(getControl0Y(), getControl1Y()));
            bounds[2] = Math.max(fMax, Math.max(getControl0X(), getControl1X()));
            bounds[3] = Math.max(fMax2, Math.max(getControl0Y(), getControl1Y()));
            return;
        }
        float f5 = 3;
        float control0X = (((-getAnchor0X()) + (getControl0X() * f5)) - (getControl1X() * f5)) + getAnchor1X();
        float f6 = 2;
        float f7 = 4;
        float anchor0X = ((getAnchor0X() * f6) - (getControl0X() * f7)) + (getControl1X() * f6);
        float control0X2 = (-getAnchor0X()) + getControl0X();
        if (!zeroIsh(control0X)) {
            float f8 = (anchor0X * anchor0X) - ((f7 * control0X) * control0X2);
            if (f8 >= 0.0f) {
                float f9 = -anchor0X;
                c = 0;
                c2 = 3;
                double d = f8;
                c3 = 2;
                f = fMin2;
                float f10 = control0X * f6;
                float fSqrt3 = (((float) Math.sqrt(d)) + f9) / f10;
                if (0.0f <= fSqrt3 && fSqrt3 <= 1.0f) {
                    float fM751getXDnnuFBc = PointKt.m751getXDnnuFBc(m744pointOnCurveOOQOV4g$graphics_shapes_release(fSqrt3));
                    if (fM751getXDnnuFBc < fMin) {
                        fMin = fM751getXDnnuFBc;
                    }
                    if (fM751getXDnnuFBc > fMax) {
                        fMax = fM751getXDnnuFBc;
                    }
                }
                float fSqrt4 = (f9 - ((float) Math.sqrt(d))) / f10;
                if (0.0f <= fSqrt4 && fSqrt4 <= 1.0f) {
                    float fM751getXDnnuFBc2 = PointKt.m751getXDnnuFBc(m744pointOnCurveOOQOV4g$graphics_shapes_release(fSqrt4));
                    if (fM751getXDnnuFBc2 < fMin) {
                        fMin = fM751getXDnnuFBc2;
                    }
                    if (fM751getXDnnuFBc2 > fMax) {
                        fMax = fM751getXDnnuFBc2;
                    }
                }
            }
            control0Y = (((-getAnchor0Y()) + (getControl0Y() * f5)) - (f5 * getControl1Y())) + getAnchor1Y();
            anchor0Y = ((getAnchor0Y() * f6) - (getControl0Y() * f7)) + (getControl1Y() * f6);
            control0Y2 = (-getAnchor0Y()) + getControl0Y();
            if (zeroIsh(control0Y)) {
                f2 = (anchor0Y * anchor0Y) - ((f7 * control0Y) * control0Y2);
                if (f2 >= 0.0f) {
                    float f11 = -anchor0Y;
                    double d2 = f2;
                    float f12 = f6 * control0Y;
                    fSqrt = (((float) Math.sqrt(d2)) + f11) / f12;
                    if (0.0f <= fSqrt && fSqrt <= 1.0f) {
                        fM752getYDnnuFBc2 = PointKt.m752getYDnnuFBc(m744pointOnCurveOOQOV4g$graphics_shapes_release(fSqrt));
                        if (fM752getYDnnuFBc2 < f) {
                            f = fM752getYDnnuFBc2;
                        }
                        if (fM752getYDnnuFBc2 > fMax2) {
                            fMax2 = fM752getYDnnuFBc2;
                        }
                    }
                    fSqrt2 = (f11 - ((float) Math.sqrt(d2))) / f12;
                    if (0.0f <= fSqrt2 || fSqrt2 > 1.0f) {
                        f3 = f;
                    } else {
                        fM752getYDnnuFBc = PointKt.m752getYDnnuFBc(m744pointOnCurveOOQOV4g$graphics_shapes_release(fSqrt2));
                        f3 = fM752getYDnnuFBc < f ? fM752getYDnnuFBc : f;
                        if (fM752getYDnnuFBc > fMax2) {
                            fMax2 = fM752getYDnnuFBc;
                        }
                    }
                } else {
                    f3 = f;
                }
            } else if (anchor0Y == 0.0f) {
                f3 = f;
            } else {
                f4 = (f6 * control0Y2) / ((-2) * anchor0Y);
                if (0.0f <= f4 || f4 > 1.0f) {
                    f3 = f;
                } else {
                    fM752getYDnnuFBc = PointKt.m752getYDnnuFBc(m744pointOnCurveOOQOV4g$graphics_shapes_release(f4));
                    f3 = fM752getYDnnuFBc < f ? fM752getYDnnuFBc : f;
                    if (fM752getYDnnuFBc > fMax2) {
                        fMax2 = fM752getYDnnuFBc;
                    }
                }
            }
            bounds[c] = fMin;
            bounds[1] = f3;
            bounds[c3] = fMax;
            bounds[c2] = fMax2;
        }
        if (anchor0X != 0.0f) {
            float f13 = (control0X2 * f6) / ((-2) * anchor0X);
            if (0.0f <= f13 && f13 <= 1.0f) {
                float fM751getXDnnuFBc3 = PointKt.m751getXDnnuFBc(m744pointOnCurveOOQOV4g$graphics_shapes_release(f13));
                if (fM751getXDnnuFBc3 < fMin) {
                    fMin = fM751getXDnnuFBc3;
                }
                if (fM751getXDnnuFBc3 > fMax) {
                    fMax = fM751getXDnnuFBc3;
                }
            }
        }
        c = 0;
        c2 = 3;
        c3 = 2;
        f = fMin2;
        control0Y = (((-getAnchor0Y()) + (getControl0Y() * f5)) - (f5 * getControl1Y())) + getAnchor1Y();
        anchor0Y = ((getAnchor0Y() * f6) - (getControl0Y() * f7)) + (getControl1Y() * f6);
        control0Y2 = (-getAnchor0Y()) + getControl0Y();
        if (zeroIsh(control0Y)) {
            f2 = (anchor0Y * anchor0Y) - ((f7 * control0Y) * control0Y2);
            if (f2 >= 0.0f) {
                float f14 = -anchor0Y;
                double d3 = f2;
                float f15 = f6 * control0Y;
                fSqrt = (((float) Math.sqrt(d3)) + f14) / f15;
                if (0.0f <= fSqrt) {
                    fM752getYDnnuFBc2 = PointKt.m752getYDnnuFBc(m744pointOnCurveOOQOV4g$graphics_shapes_release(fSqrt));
                    if (fM752getYDnnuFBc2 < f) {
                        f = fM752getYDnnuFBc2;
                    }
                    if (fM752getYDnnuFBc2 > fMax2) {
                        fMax2 = fM752getYDnnuFBc2;
                    }
                }
                fSqrt2 = (f14 - ((float) Math.sqrt(d3))) / f15;
                if (0.0f <= fSqrt2) {
                    f3 = f;
                } else {
                    f3 = f;
                }
            } else {
                f3 = f;
            }
        } else if (anchor0Y == 0.0f) {
            f3 = f;
        } else {
            f4 = (f6 * control0Y2) / ((-2) * anchor0Y);
            if (0.0f <= f4) {
                f3 = f;
            } else {
                f3 = f;
            }
        }
        bounds[c] = fMin;
        bounds[1] = f3;
        bounds[c3] = fMax;
        bounds[c2] = fMax2;
    }

    public final Pair split(float f) {
        float f2 = 1 - f;
        long jM744pointOnCurveOOQOV4g$graphics_shapes_release = m744pointOnCurveOOQOV4g$graphics_shapes_release(f);
        float f3 = f2 * f2;
        float f4 = 2 * f2 * f;
        float f5 = f * f;
        return TuplesKt.to(CubicKt.Cubic(getAnchor0X(), getAnchor0Y(), (getAnchor0X() * f2) + (getControl0X() * f), (getAnchor0Y() * f2) + (getControl0Y() * f), (getAnchor0X() * f3) + (getControl0X() * f4) + (getControl1X() * f5), (getAnchor0Y() * f3) + (getControl0Y() * f4) + (getControl1Y() * f5), PointKt.m751getXDnnuFBc(jM744pointOnCurveOOQOV4g$graphics_shapes_release), PointKt.m752getYDnnuFBc(jM744pointOnCurveOOQOV4g$graphics_shapes_release)), CubicKt.Cubic(PointKt.m751getXDnnuFBc(jM744pointOnCurveOOQOV4g$graphics_shapes_release), PointKt.m752getYDnnuFBc(jM744pointOnCurveOOQOV4g$graphics_shapes_release), (getControl0X() * f3) + (getControl1X() * f4) + (getAnchor1X() * f5), (getControl0Y() * f3) + (getControl1Y() * f4) + (getAnchor1Y() * f5), (getControl1X() * f2) + (getAnchor1X() * f), (getControl1Y() * f2) + (getAnchor1Y() * f), getAnchor1X(), getAnchor1Y()));
    }

    public final Cubic reverse() {
        return CubicKt.Cubic(getAnchor1X(), getAnchor1Y(), getControl1X(), getControl1Y(), getControl0X(), getControl0Y(), getAnchor0X(), getAnchor0Y());
    }

    public String toString() {
        return "anchor0: (" + getAnchor0X() + ", " + getAnchor0Y() + ") control0: (" + getControl0X() + ", " + getControl0Y() + "), control1: (" + getControl1X() + ", " + getControl1Y() + "), anchor1: (" + getAnchor1X() + ", " + getAnchor1Y() + ')';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Cubic) {
            return Arrays.equals(this.points, ((Cubic) obj).points);
        }
        return false;
    }

    public final Cubic transformed(PointTransformer f) {
        Intrinsics.checkNotNullParameter(f, "f");
        MutableCubic mutableCubic = new MutableCubic();
        ArraysKt.copyInto$default(this.points, mutableCubic.getPoints$graphics_shapes_release(), 0, 0, 0, 14, (Object) null);
        mutableCubic.transform(f);
        return mutableCubic;
    }

    public int hashCode() {
        return Arrays.hashCode(this.points);
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Cubic straightLine(float f, float f2, float f3, float f4) {
            return CubicKt.Cubic(f, f2, Utils.interpolate(f, f3, 0.33333334f), Utils.interpolate(f2, f4, 0.33333334f), Utils.interpolate(f, f3, 0.6666667f), Utils.interpolate(f2, f4, 0.6666667f), f3, f4);
        }

        public final Cubic circularArc(float f, float f2, float f3, float f4, float f5, float f6) {
            float f7 = f3 - f;
            float f8 = f4 - f2;
            long jDirectionVector = Utils.directionVector(f7, f8);
            float f9 = f5 - f;
            float f10 = f6 - f2;
            long jDirectionVector2 = Utils.directionVector(f9, f10);
            long jM764rotate90DnnuFBc = Utils.m764rotate90DnnuFBc(jDirectionVector);
            long jM764rotate90DnnuFBc2 = Utils.m764rotate90DnnuFBc(jDirectionVector2);
            boolean z = PointKt.m747dotProduct5P9i7ZU(jM764rotate90DnnuFBc, f9, f10) >= 0.0f;
            float fM748dotProductybeJwSQ = PointKt.m748dotProductybeJwSQ(jDirectionVector, jDirectionVector2);
            if (fM748dotProductybeJwSQ > 0.999f) {
                return straightLine(f3, f4, f5, f6);
            }
            float f11 = 1;
            float f12 = f11 - fM748dotProductybeJwSQ;
            float fDistance = ((((Utils.distance(f7, f8) * 4.0f) / 3.0f) * (((float) Math.sqrt(2 * f12)) - ((float) Math.sqrt(f11 - (fM748dotProductybeJwSQ * fM748dotProductybeJwSQ))))) / f12) * (z ? 1.0f : -1.0f);
            return CubicKt.Cubic(f3, f4, f3 + (PointKt.m751getXDnnuFBc(jM764rotate90DnnuFBc) * fDistance), f4 + (PointKt.m752getYDnnuFBc(jM764rotate90DnnuFBc) * fDistance), f5 - (PointKt.m751getXDnnuFBc(jM764rotate90DnnuFBc2) * fDistance), f6 - (PointKt.m752getYDnnuFBc(jM764rotate90DnnuFBc2) * fDistance), f5, f6);
        }
    }
}
