package androidx.graphics.shapes;

import androidx.collection.FloatFloatPair;
import kotlin.jvm.internal.Intrinsics;

public abstract class PointKt {
    /* JADX INFO: renamed from: getX-DnnuFBc, reason: not valid java name */
    public static final float m751getXDnnuFBc(long j) {
        return Float.intBitsToFloat((int) (j >> 32));
    }

    /* JADX INFO: renamed from: getY-DnnuFBc, reason: not valid java name */
    public static final float m752getYDnnuFBc(long j) {
        return Float.intBitsToFloat((int) (j & 4294967295L));
    }

    /* JADX INFO: renamed from: getDistance-DnnuFBc, reason: not valid java name */
    public static final float m750getDistanceDnnuFBc(long j) {
        return (float) Math.sqrt((m751getXDnnuFBc(j) * m751getXDnnuFBc(j)) + (m752getYDnnuFBc(j) * m752getYDnnuFBc(j)));
    }

    /* JADX INFO: renamed from: dotProduct-ybeJwSQ, reason: not valid java name */
    public static final float m748dotProductybeJwSQ(long j, long j2) {
        return (m751getXDnnuFBc(j) * m751getXDnnuFBc(j2)) + (m752getYDnnuFBc(j) * m752getYDnnuFBc(j2));
    }

    /* JADX INFO: renamed from: dotProduct-5P9i7ZU, reason: not valid java name */
    public static final float m747dotProduct5P9i7ZU(long j, float f, float f2) {
        return (m751getXDnnuFBc(j) * f) + (m752getYDnnuFBc(j) * f2);
    }

    /* JADX INFO: renamed from: clockwise-ybeJwSQ, reason: not valid java name */
    public static final boolean m745clockwiseybeJwSQ(long j, long j2) {
        return (m751getXDnnuFBc(j) * m752getYDnnuFBc(j2)) - (m752getYDnnuFBc(j) * m751getXDnnuFBc(j2)) > 0.0f;
    }

    /* JADX INFO: renamed from: getDirection-DnnuFBc, reason: not valid java name */
    public static final long m749getDirectionDnnuFBc(long j) {
        float fM750getDistanceDnnuFBc = m750getDistanceDnnuFBc(j);
        if (fM750getDistanceDnnuFBc <= 0.0f) {
            throw new IllegalArgumentException("Can't get the direction of a 0-length vector");
        }
        return m746divso9K2fw(j, fM750getDistanceDnnuFBc);
    }

    /* JADX INFO: renamed from: minus-ybeJwSQ, reason: not valid java name */
    public static final long m754minusybeJwSQ(long j, long j2) {
        return FloatFloatPair.m675constructorimpl(m751getXDnnuFBc(j) - m751getXDnnuFBc(j2), m752getYDnnuFBc(j) - m752getYDnnuFBc(j2));
    }

    /* JADX INFO: renamed from: plus-ybeJwSQ, reason: not valid java name */
    public static final long m755plusybeJwSQ(long j, long j2) {
        return FloatFloatPair.m675constructorimpl(m751getXDnnuFBc(j) + m751getXDnnuFBc(j2), m752getYDnnuFBc(j) + m752getYDnnuFBc(j2));
    }

    /* JADX INFO: renamed from: times-so9K2fw, reason: not valid java name */
    public static final long m756timesso9K2fw(long j, float f) {
        return FloatFloatPair.m675constructorimpl(m751getXDnnuFBc(j) * f, m752getYDnnuFBc(j) * f);
    }

    /* JADX INFO: renamed from: div-so9K2fw, reason: not valid java name */
    public static final long m746divso9K2fw(long j, float f) {
        return FloatFloatPair.m675constructorimpl(m751getXDnnuFBc(j) / f, m752getYDnnuFBc(j) / f);
    }

    /* JADX INFO: renamed from: interpolate-dLqxh1s, reason: not valid java name */
    public static final long m753interpolatedLqxh1s(long j, long j2, float f) {
        return FloatFloatPair.m675constructorimpl(Utils.interpolate(m751getXDnnuFBc(j), m751getXDnnuFBc(j2), f), Utils.interpolate(m752getYDnnuFBc(j), m752getYDnnuFBc(j2), f));
    }

    /* JADX INFO: renamed from: transformed-so9K2fw, reason: not valid java name */
    public static final long m757transformedso9K2fw(long j, PointTransformer f) {
        Intrinsics.checkNotNullParameter(f, "f");
        long jMo758transformXgqJiTY = f.mo758transformXgqJiTY(m751getXDnnuFBc(j), m752getYDnnuFBc(j));
        return FloatFloatPair.m675constructorimpl(Float.intBitsToFloat((int) (jMo758transformXgqJiTY >> 32)), Float.intBitsToFloat((int) (jMo758transformXgqJiTY & 4294967295L)));
    }
}
