package j$.util;

import j$.util.stream.C0242j0;
import j$.util.stream.EnumC0215d3;
import j$.util.stream.I3;
import j$.util.stream.IntStream;
import j$.util.stream.LongStream;
import j$.util.stream.Stream;

public final /* synthetic */ class DesugarArrays {
    public static i0 a(Object[] objArr, int i, int i2) {
        Spliterators.a(((Object[]) Objects.requireNonNull(objArr)).length, i, i2);
        return new i0(objArr, i, i2, 1040);
    }

    public static q0 b(long[] jArr, int i, int i2) {
        Spliterators.a(((long[]) Objects.requireNonNull(jArr)).length, i, i2);
        return new q0(jArr, i, i2, 1040);
    }

    public static <T> Stream<T> stream(T[] tArr) {
        return I3.b(a(tArr, 0, tArr.length), false);
    }

    public static IntStream stream(int[] iArr) {
        return I3.a(Spliterators.spliterator(iArr, 0, iArr.length, 1040));
    }

    public static LongStream stream(long[] jArr) {
        q0 q0VarB = b(jArr, 0, jArr.length);
        return new C0242j0(q0VarB, EnumC0215d3.k(q0VarB), false);
    }
}
