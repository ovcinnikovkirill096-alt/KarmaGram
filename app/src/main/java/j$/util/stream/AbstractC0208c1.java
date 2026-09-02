package j$.util.stream;

import java.util.function.IntFunction;

/* JADX INFO: renamed from: j$.util.stream.c1, reason: case insensitive filesystem */
public abstract class AbstractC0208c1 implements J0 {
    @Override // j$.util.stream.J0
    public final long count() {
        return 0L;
    }

    public final void d(Object obj) {
    }

    @Override // j$.util.stream.J0
    public /* synthetic */ J0 e(long j, long j2, IntFunction intFunction) {
        return AbstractC0322z1.S(this, j, j2, intFunction);
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ int i() {
        return 0;
    }

    @Override // j$.util.stream.J0
    public J0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.J0
    public final Object[] g(IntFunction intFunction) {
        return (Object[]) intFunction.apply(0);
    }

    public final void c(int i, Object obj) {
    }
}
