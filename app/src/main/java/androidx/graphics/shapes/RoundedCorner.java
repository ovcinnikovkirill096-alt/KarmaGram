package androidx.graphics.shapes;

import androidx.collection.FloatFloatPair;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;

final class RoundedCorner {
    private long center;
    private final float cornerRadius;
    private final float cosAngle;
    private final long d1;
    private final long d2;
    private final float expectedRoundCut;
    private final long p0;
    private final long p1;
    private final long p2;
    private final CornerRounding rounding;
    private final float sinAngle;
    private final float smoothing;

    public /* synthetic */ RoundedCorner(long j, long j2, long j3, CornerRounding cornerRounding, DefaultConstructorMarker defaultConstructorMarker) {
        this(j, j2, j3, cornerRounding);
    }

    private RoundedCorner(long j, long j2, long j3, CornerRounding cornerRounding) {
        this.p0 = j;
        this.p1 = j2;
        this.p2 = j3;
        this.rounding = cornerRounding;
        long jM749getDirectionDnnuFBc = PointKt.m749getDirectionDnnuFBc(PointKt.m754minusybeJwSQ(j, j2));
        this.d1 = jM749getDirectionDnnuFBc;
        long jM749getDirectionDnnuFBc2 = PointKt.m749getDirectionDnnuFBc(PointKt.m754minusybeJwSQ(j3, j2));
        this.d2 = jM749getDirectionDnnuFBc2;
        float radius = cornerRounding != null ? cornerRounding.getRadius() : 0.0f;
        this.cornerRadius = radius;
        this.smoothing = cornerRounding != null ? cornerRounding.getSmoothing() : 0.0f;
        float fM748dotProductybeJwSQ = PointKt.m748dotProductybeJwSQ(jM749getDirectionDnnuFBc, jM749getDirectionDnnuFBc2);
        this.cosAngle = fM748dotProductybeJwSQ;
        float f = 1;
        float fSqrt = (float) Math.sqrt(f - Utils.square(fM748dotProductybeJwSQ));
        this.sinAngle = fSqrt;
        this.expectedRoundCut = ((double) fSqrt) > 0.001d ? (radius * (fM748dotProductybeJwSQ + f)) / fSqrt : 0.0f;
        this.center = FloatFloatPair.m675constructorimpl(0.0f, 0.0f);
    }

    public final float getExpectedRoundCut() {
        return this.expectedRoundCut;
    }

    public final float getExpectedCut() {
        return (1 + this.smoothing) * this.expectedRoundCut;
    }

    /* JADX INFO: renamed from: getCenter-1ufDz9w, reason: not valid java name */
    public final long m761getCenter1ufDz9w() {
        return this.center;
    }

    public final List getCubics(float f, float f2) {
        float fMin = Math.min(f, f2);
        float f3 = this.expectedRoundCut;
        if (f3 < 1.0E-4f || fMin < 1.0E-4f || this.cornerRadius < 1.0E-4f) {
            long j = this.p1;
            this.center = j;
            return CollectionsKt.listOf(Cubic.Companion.straightLine(PointKt.m751getXDnnuFBc(j), PointKt.m752getYDnnuFBc(this.p1), PointKt.m751getXDnnuFBc(this.p1), PointKt.m752getYDnnuFBc(this.p1)));
        }
        float fMin2 = Math.min(fMin, f3);
        float fCalculateActualSmoothingValue = calculateActualSmoothingValue(f);
        float fCalculateActualSmoothingValue2 = calculateActualSmoothingValue(f2);
        float f4 = (this.cornerRadius * fMin2) / this.expectedRoundCut;
        this.center = PointKt.m755plusybeJwSQ(this.p1, PointKt.m756timesso9K2fw(PointKt.m749getDirectionDnnuFBc(PointKt.m746divso9K2fw(PointKt.m755plusybeJwSQ(this.d1, this.d2), 2.0f)), (float) Math.sqrt(Utils.square(f4) + Utils.square(fMin2))));
        long jM755plusybeJwSQ = PointKt.m755plusybeJwSQ(this.p1, PointKt.m756timesso9K2fw(this.d1, fMin2));
        long jM755plusybeJwSQ2 = PointKt.m755plusybeJwSQ(this.p1, PointKt.m756timesso9K2fw(this.d2, fMin2));
        Cubic cubicM759computeFlankingCurveoAJzIJU = m759computeFlankingCurveoAJzIJU(fMin2, fCalculateActualSmoothingValue, this.p1, this.p0, jM755plusybeJwSQ, jM755plusybeJwSQ2, this.center, f4);
        Cubic cubicReverse = m759computeFlankingCurveoAJzIJU(fMin2, fCalculateActualSmoothingValue2, this.p1, this.p2, jM755plusybeJwSQ2, jM755plusybeJwSQ, this.center, f4).reverse();
        return CollectionsKt.listOf((Object[]) new Cubic[]{cubicM759computeFlankingCurveoAJzIJU, Cubic.Companion.circularArc(PointKt.m751getXDnnuFBc(this.center), PointKt.m752getYDnnuFBc(this.center), cubicM759computeFlankingCurveoAJzIJU.getAnchor1X(), cubicM759computeFlankingCurveoAJzIJU.getAnchor1Y(), cubicReverse.getAnchor0X(), cubicReverse.getAnchor0Y()), cubicReverse});
    }

    private final float calculateActualSmoothingValue(float f) {
        if (f > getExpectedCut()) {
            return this.smoothing;
        }
        float f2 = this.expectedRoundCut;
        if (f > f2) {
            return (this.smoothing * (f - f2)) / (getExpectedCut() - this.expectedRoundCut);
        }
        return 0.0f;
    }

    /* JADX INFO: renamed from: computeFlankingCurve-oAJzIJU, reason: not valid java name */
    private final Cubic m759computeFlankingCurveoAJzIJU(float f, float f2, long j, long j2, long j3, long j4, long j5, float f3) {
        long jM749getDirectionDnnuFBc = PointKt.m749getDirectionDnnuFBc(PointKt.m754minusybeJwSQ(j2, j));
        long jM755plusybeJwSQ = PointKt.m755plusybeJwSQ(j, PointKt.m756timesso9K2fw(PointKt.m756timesso9K2fw(jM749getDirectionDnnuFBc, f), 1 + f2));
        long jM680unboximpl = j3;
        long jM753interpolatedLqxh1s = PointKt.m753interpolatedLqxh1s(jM680unboximpl, PointKt.m746divso9K2fw(PointKt.m755plusybeJwSQ(j3, j4), 2.0f), f2);
        long jM755plusybeJwSQ2 = PointKt.m755plusybeJwSQ(j5, PointKt.m756timesso9K2fw(Utils.directionVector(PointKt.m751getXDnnuFBc(jM753interpolatedLqxh1s) - PointKt.m751getXDnnuFBc(j5), PointKt.m752getYDnnuFBc(jM753interpolatedLqxh1s) - PointKt.m752getYDnnuFBc(j5)), f3));
        FloatFloatPair floatFloatPairM760lineIntersectionCBFvKDc = m760lineIntersectionCBFvKDc(j2, jM749getDirectionDnnuFBc, jM755plusybeJwSQ2, Utils.m764rotate90DnnuFBc(PointKt.m754minusybeJwSQ(jM755plusybeJwSQ2, j5)));
        if (floatFloatPairM760lineIntersectionCBFvKDc != null) {
            jM680unboximpl = floatFloatPairM760lineIntersectionCBFvKDc.m680unboximpl();
        }
        return new Cubic(jM755plusybeJwSQ, PointKt.m746divso9K2fw(PointKt.m755plusybeJwSQ(jM755plusybeJwSQ, PointKt.m756timesso9K2fw(jM680unboximpl, 2.0f)), 3.0f), jM680unboximpl, jM755plusybeJwSQ2, null);
    }

    /* JADX INFO: renamed from: lineIntersection-CBFvKDc, reason: not valid java name */
    private final FloatFloatPair m760lineIntersectionCBFvKDc(long j, long j2, long j3, long j4) {
        long jM764rotate90DnnuFBc = Utils.m764rotate90DnnuFBc(j4);
        float fM748dotProductybeJwSQ = PointKt.m748dotProductybeJwSQ(j2, jM764rotate90DnnuFBc);
        if (Math.abs(fM748dotProductybeJwSQ) < 1.0E-4f) {
            return null;
        }
        float fM748dotProductybeJwSQ2 = PointKt.m748dotProductybeJwSQ(PointKt.m754minusybeJwSQ(j3, j), jM764rotate90DnnuFBc);
        if (Math.abs(fM748dotProductybeJwSQ) < Math.abs(fM748dotProductybeJwSQ2) * 1.0E-4f) {
            return null;
        }
        return FloatFloatPair.m674boximpl(PointKt.m755plusybeJwSQ(j, PointKt.m756timesso9K2fw(j2, fM748dotProductybeJwSQ2 / fM748dotProductybeJwSQ)));
    }
}
