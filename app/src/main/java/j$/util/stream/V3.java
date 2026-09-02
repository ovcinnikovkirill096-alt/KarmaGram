package j$.util.stream;

import java.util.function.LongPredicate;

public final class V3 extends AbstractC0254l2 {
    public final boolean b;

    public V3(L2 l2, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.b = true;
    }

    @Override // j$.util.stream.AbstractC0254l2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a.h(-1L);
    }

    @Override // j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final void accept(long j) {
        if (this.b) {
            LongPredicate longPredicate = null;
            longPredicate.test(j);
            throw null;
        }
    }

    @Override // j$.util.stream.AbstractC0254l2, j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        return !this.b || this.a.m();
    }
}
