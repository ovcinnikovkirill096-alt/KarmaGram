package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public final class U0 extends L0 {
    @Override // j$.util.stream.J0
    public final J0 e(long j, long j2, IntFunction intFunction) {
        if (j == 0 && j2 == this.c) {
            return this;
        }
        long jCount = this.a.count();
        if (j >= jCount) {
            return this.b.e(j - jCount, j2 - jCount, intFunction);
        }
        if (j2 > jCount) {
            return AbstractC0322z1.Y(EnumC0220e3.REFERENCE, this.a.e(j, jCount, intFunction), this.b.e(0L, j2 - jCount, intFunction));
        }
        return this.a.e(j, j2, intFunction);
    }

    @Override // j$.util.stream.J0
    public final Spliterator spliterator() {
        return new C0253l1(this);
    }

    @Override // j$.util.stream.J0
    public final void f(Object[] objArr, int i) {
        Objects.requireNonNull(objArr);
        J0 j0 = this.a;
        j0.f(objArr, i);
        this.b.f(objArr, i + ((int) j0.count()));
    }

    @Override // j$.util.stream.J0
    public final Object[] g(IntFunction intFunction) {
        long j = this.c;
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        Object[] objArr = (Object[]) intFunction.apply((int) j);
        f(objArr, 0);
        return objArr;
    }

    @Override // j$.util.stream.J0
    public final void forEach(Consumer consumer) {
        this.a.forEach(consumer);
        this.b.forEach(consumer);
    }

    public final String toString() {
        long j = this.c;
        return j < 32 ? String.format("ConcNode[%s.%s]", this.a, this.b) : String.format("ConcNode[size=%d]", Long.valueOf(j));
    }
}
