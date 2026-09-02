package j$.util.stream;

import j$.util.Spliterator;

public abstract class B2 {
    public static long c(long j, long j2) {
        long j3 = j2 >= 0 ? j + j2 : Long.MAX_VALUE;
        if (j3 >= 0) {
            return j3;
        }
        return Long.MAX_VALUE;
    }

    public static long a(long j, long j2, long j3) {
        if (j >= 0) {
            return Math.max(-1L, Math.min(j - j2, j3));
        }
        return -1L;
    }

    public static Spliterator b(EnumC0220e3 enumC0220e3, Spliterator spliterator, long j, long j2) {
        long jC = c(j, j2);
        int i = AbstractC0323z2.a[enumC0220e3.ordinal()];
        if (i == 1) {
            return new C0314x3(spliterator, j, jC);
        }
        if (i == 2) {
            return new C0299u3((Spliterator.OfInt) spliterator, j, jC);
        }
        if (i == 3) {
            return new C0304v3((j$.util.Z) spliterator, j, jC);
        }
        if (i != 4) {
            throw new IllegalStateException("Unknown shape " + enumC0220e3);
        }
        return new C0294t3((j$.util.U) spliterator, j, jC);
    }

    public static C0288s2 h(AbstractC0239i2 abstractC0239i2, long j, long j2) {
        if (j < 0) {
            throw new IllegalArgumentException("Skip must be non-negative: " + j);
        }
        return new C0288s2(abstractC0239i2, d(j2), j, j2);
    }

    public static C0298u2 f(AbstractC0212d0 abstractC0212d0, long j, long j2) {
        if (j < 0) {
            throw new IllegalArgumentException("Skip must be non-negative: " + j);
        }
        return new C0298u2(abstractC0212d0, d(j2), j, j2);
    }

    public static C0308w2 g(AbstractC0257m0 abstractC0257m0, long j, long j2) {
        if (j < 0) {
            throw new IllegalArgumentException("Skip must be non-negative: " + j);
        }
        return new C0308w2(abstractC0257m0, d(j2), j, j2);
    }

    public static C0318y2 e(C c, long j, long j2) {
        if (j < 0) {
            throw new IllegalArgumentException("Skip must be non-negative: " + j);
        }
        return new C0318y2(c, d(j2), j, j2);
    }

    public static int d(long j) {
        return (j != -1 ? EnumC0215d3.u : 0) | EnumC0215d3.t;
    }
}
