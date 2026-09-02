package j$.util.stream;

import java.util.function.DoubleConsumer;

public final class W1 extends AbstractC0199a2 implements InterfaceC0264n2 {
    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final /* bridge */ /* synthetic */ void v(Object obj) {
        v((Double) obj);
    }

    public final /* synthetic */ DoubleConsumer andThen(DoubleConsumer doubleConsumer) {
        return j$.com.android.tools.r8.a.b(this, doubleConsumer);
    }

    @Override // j$.util.stream.InterfaceC0264n2
    public final /* synthetic */ void v(Double d) {
        AbstractC0322z1.A(this, d);
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
    public final void accept(double d) {
        this.b++;
    }
}
