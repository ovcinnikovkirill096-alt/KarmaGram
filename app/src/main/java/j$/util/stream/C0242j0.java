package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.LongConsumer;

/* JADX INFO: renamed from: j$.util.stream.j0, reason: case insensitive filesystem */
public final class C0242j0 extends AbstractC0257m0 {
    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream, j$.util.stream.F
    public final LongStream sequential() {
        this.h.r = false;
        return this;
    }

    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream, j$.util.stream.F
    public final LongStream parallel() {
        this.h.r = true;
        return this;
    }

    @Override // j$.util.stream.AbstractC0257m0, j$.util.stream.LongStream
    public final void forEach(LongConsumer longConsumer) {
        if (this.h.r) {
            super.forEach(longConsumer);
        } else {
            AbstractC0257m0.I0(G0()).forEachRemaining(longConsumer);
        }
    }

    @Override // j$.util.stream.AbstractC0257m0, j$.util.stream.LongStream
    public final void forEachOrdered(LongConsumer longConsumer) {
        if (this.h.r) {
            super.forEachOrdered(longConsumer);
        } else {
            AbstractC0257m0.I0(G0()).forEachRemaining(longConsumer);
        }
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !EnumC0215d3.ORDERED.n(this.m) ? this : new C0305w(this, EnumC0215d3.r, 4);
    }

    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream
    public final /* bridge */ /* synthetic */ Spliterator spliterator() {
        return spliterator();
    }

    @Override // j$.util.stream.AbstractC0201b
    public final boolean D0() {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        throw new UnsupportedOperationException();
    }
}
