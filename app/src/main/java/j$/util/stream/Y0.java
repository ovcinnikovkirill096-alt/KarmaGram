package j$.util.stream;

import j$.util.Spliterator;
import j$.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public final class Y0 extends AbstractC0208c1 implements D0 {
    @Override // j$.util.stream.AbstractC0208c1, j$.util.stream.J0
    public final /* synthetic */ J0 e(long j, long j2, IntFunction intFunction) {
        return AbstractC0322z1.P(this, j, j2);
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ void forEach(Consumer consumer) {
        AbstractC0322z1.M(this, consumer);
    }

    @Override // j$.util.stream.AbstractC0208c1, j$.util.stream.J0
    public final /* bridge */ /* synthetic */ J0 a(int i) {
        a(i);
        throw null;
    }

    @Override // j$.util.stream.AbstractC0208c1, j$.util.stream.J0
    public final I0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ void f(Object[] objArr, int i) {
        AbstractC0322z1.J(this, (Double[]) objArr, i);
    }

    @Override // j$.util.stream.I0
    public final /* bridge */ /* synthetic */ Object b() {
        return AbstractC0322z1.g;
    }

    @Override // j$.util.stream.J0
    public final /* bridge */ /* synthetic */ Spliterator spliterator() {
        return Spliterators.d;
    }

    @Override // j$.util.stream.J0
    public final /* bridge */ /* synthetic */ j$.util.c0 spliterator() {
        return Spliterators.d;
    }
}
