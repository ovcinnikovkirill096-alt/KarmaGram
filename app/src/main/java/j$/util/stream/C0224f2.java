package j$.util.stream;

import java.util.function.Consumer;

/* JADX INFO: renamed from: j$.util.stream.f2, reason: case insensitive filesystem */
public final class C0224f2 extends AbstractC0239i2 {
    @Override // j$.util.stream.AbstractC0239i2, j$.util.stream.Stream
    public final void forEach(Consumer consumer) {
        if (!this.h.r) {
            G0().forEachRemaining(consumer);
        } else {
            super.forEach(consumer);
        }
    }

    @Override // j$.util.stream.AbstractC0239i2, j$.util.stream.Stream
    public final void forEachOrdered(Consumer consumer) {
        if (!this.h.r) {
            G0().forEachRemaining(consumer);
        } else {
            super.forEachOrdered(consumer);
        }
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !EnumC0215d3.ORDERED.n(this.m) ? this : new C0214d2(this, EnumC0215d3.r);
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
