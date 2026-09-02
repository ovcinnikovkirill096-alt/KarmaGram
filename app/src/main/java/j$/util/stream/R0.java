package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public final class R0 extends T0 implements F0 {
    @Override // j$.util.stream.J0
    public final /* synthetic */ J0 e(long j, long j2, IntFunction intFunction) {
        return AbstractC0322z1.Q(this, j, j2);
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ void forEach(Consumer consumer) {
        AbstractC0322z1.N(this, consumer);
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ void f(Object[] objArr, int i) {
        AbstractC0322z1.K(this, (Integer[]) objArr, i);
    }

    @Override // j$.util.stream.I0
    public final Object newArray(int i) {
        return new int[i];
    }

    @Override // j$.util.stream.J0
    public final Spliterator spliterator() {
        return new C0238i1(this);
    }

    @Override // j$.util.stream.J0
    public final j$.util.c0 spliterator() {
        return new C0238i1(this);
    }
}
