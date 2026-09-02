package j$.util.stream;

import java.util.function.LongConsumer;

public final class Y1 extends AbstractC0199a2 implements InterfaceC0274p2 {
    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final /* bridge */ /* synthetic */ void v(Object obj) {
        u((Long) obj);
    }

    public final /* synthetic */ LongConsumer andThen(LongConsumer longConsumer) {
        return j$.com.android.tools.r8.a.d(this, longConsumer);
    }

    @Override // j$.util.stream.InterfaceC0274p2
    public final /* synthetic */ void u(Long l) {
        AbstractC0322z1.E(this, l);
    }

    @Override // j$.util.stream.V1, java.util.function.Supplier
    public final Object get() {
        return Long.valueOf(this.b);
    }

    @Override // j$.util.stream.U1
    public final void q(U1 u1) {
        this.b += ((AbstractC0199a2) u1).b;
    }

    @Override // j$.util.stream.AbstractC0199a2, j$.util.stream.InterfaceC0279q2, j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final void accept(long j) {
        this.b++;
    }
}
