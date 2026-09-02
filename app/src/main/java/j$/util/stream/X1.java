package j$.util.stream;

import java.util.function.IntConsumer;

public final class X1 extends AbstractC0199a2 implements InterfaceC0269o2 {
    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final /* bridge */ /* synthetic */ void v(Object obj) {
        l((Integer) obj);
    }

    public final /* synthetic */ IntConsumer andThen(IntConsumer intConsumer) {
        return j$.com.android.tools.r8.a.c(this, intConsumer);
    }

    @Override // j$.util.stream.InterfaceC0269o2
    public final /* synthetic */ void l(Integer num) {
        AbstractC0322z1.C(this, num);
    }

    @Override // j$.util.stream.V1, java.util.function.Supplier
    public final Object get() {
        return Long.valueOf(this.b);
    }

    @Override // j$.util.stream.U1
    public final void q(U1 u1) {
        this.b += ((AbstractC0199a2) u1).b;
    }

    @Override // j$.util.stream.AbstractC0199a2, j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        this.b++;
    }
}
