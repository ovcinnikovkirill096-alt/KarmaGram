package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.IntConsumer;

/* JADX INFO: renamed from: j$.util.stream.a0, reason: case insensitive filesystem */
public final class C0197a0 extends AbstractC0212d0 {
    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream, j$.util.stream.F
    public final IntStream sequential() {
        this.h.r = false;
        return this;
    }

    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream, j$.util.stream.F
    public final IntStream parallel() {
        this.h.r = true;
        return this;
    }

    @Override // j$.util.stream.AbstractC0212d0, j$.util.stream.IntStream
    public final void forEach(IntConsumer intConsumer) {
        if (this.h.r) {
            super.forEach(intConsumer);
        } else {
            AbstractC0212d0.I0(G0()).forEachRemaining(intConsumer);
        }
    }

    @Override // j$.util.stream.AbstractC0212d0, j$.util.stream.IntStream
    public final void forEachOrdered(IntConsumer intConsumer) {
        if (this.h.r) {
            super.forEachOrdered(intConsumer);
        } else {
            AbstractC0212d0.I0(G0()).forEachRemaining(intConsumer);
        }
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !EnumC0215d3.ORDERED.n(this.m) ? this : new C0300v(this, EnumC0215d3.r, 1);
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
