package j$.util.stream;

import j$.util.Spliterator;
import j$.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntFunction;

/* JADX INFO: renamed from: j$.util.stream.a1, reason: case insensitive filesystem */
public final class C0198a1 extends AbstractC0208c1 implements H0 {
    @Override // j$.util.stream.AbstractC0208c1, j$.util.stream.J0
    public final /* synthetic */ J0 e(long j, long j2, IntFunction intFunction) {
        return AbstractC0322z1.R(this, j, j2);
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ void forEach(Consumer consumer) {
        AbstractC0322z1.O(this, consumer);
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
        AbstractC0322z1.L(this, (Long[]) objArr, i);
    }

    @Override // j$.util.stream.I0
    public final /* bridge */ /* synthetic */ Object b() {
        return AbstractC0322z1.f;
    }

    @Override // j$.util.stream.J0
    public final /* bridge */ /* synthetic */ Spliterator spliterator() {
        return Spliterators.c;
    }

    @Override // j$.util.stream.J0
    public final /* bridge */ /* synthetic */ j$.util.c0 spliterator() {
        return Spliterators.c;
    }
}
