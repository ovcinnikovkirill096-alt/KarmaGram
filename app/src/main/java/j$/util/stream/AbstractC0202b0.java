package j$.util.stream;

import j$.util.Spliterator;

/* JADX INFO: renamed from: j$.util.stream.b0, reason: case insensitive filesystem */
public abstract class AbstractC0202b0 extends AbstractC0212d0 {
    @Override // j$.util.stream.AbstractC0201b
    public final boolean D0() {
        return true;
    }

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

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !EnumC0215d3.ORDERED.n(this.m) ? this : new C0300v(this, EnumC0215d3.r, 1);
    }

    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream
    public final /* bridge */ /* synthetic */ Spliterator spliterator() {
        return spliterator();
    }
}
