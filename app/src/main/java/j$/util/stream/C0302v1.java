package j$.util.stream;

import java.util.function.IntFunction;

/* JADX INFO: renamed from: j$.util.stream.v1, reason: case insensitive filesystem */
public final class C0302v1 extends C0205b3 implements J0, B0 {
    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ void accept(double d) {
        AbstractC0322z1.z();
        throw null;
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ void accept(int i) {
        AbstractC0322z1.G();
        throw null;
    }

    @Override // j$.util.stream.InterfaceC0279q2, j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final /* synthetic */ void accept(long j) {
        AbstractC0322z1.H();
        throw null;
    }

    @Override // j$.util.stream.B0
    public final J0 build() {
        return this;
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ J0 e(long j, long j2, IntFunction intFunction) {
        return AbstractC0322z1.S(this, j, j2, intFunction);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final void end() {
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ int i() {
        return 0;
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ boolean m() {
        return false;
    }

    @Override // j$.util.stream.J0
    public final J0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.J0
    public final void f(Object[] objArr, int i) {
        long j = i;
        long jCount = count() + j;
        if (jCount > objArr.length || jCount < j) {
            throw new IndexOutOfBoundsException("does not fit");
        }
        if (this.c == 0) {
            System.arraycopy(this.e, 0, objArr, i, this.b);
            return;
        }
        for (int i2 = 0; i2 < this.c; i2++) {
            Object[] objArr2 = this.f[i2];
            System.arraycopy(objArr2, 0, objArr, i, objArr2.length);
            i += this.f[i2].length;
        }
        int i3 = this.b;
        if (i3 > 0) {
            System.arraycopy(this.e, 0, objArr, i, i3);
        }
    }

    @Override // j$.util.stream.J0
    public final Object[] g(IntFunction intFunction) {
        long jCount = count();
        if (jCount >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        Object[] objArr = (Object[]) intFunction.apply((int) jCount);
        f(objArr, 0);
        return objArr;
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        clear();
        j(j);
    }
}
