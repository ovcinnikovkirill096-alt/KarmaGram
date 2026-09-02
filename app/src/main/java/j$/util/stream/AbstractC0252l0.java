package j$.util.stream;

import j$.util.Spliterator;

/* JADX INFO: renamed from: j$.util.stream.l0, reason: case insensitive filesystem */
public abstract class AbstractC0252l0 extends AbstractC0257m0 {
    @Override // j$.util.stream.AbstractC0201b
    public final boolean D0() {
        return false;
    }

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

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !EnumC0215d3.ORDERED.n(this.m) ? this : new C0305w(this, EnumC0215d3.r, 4);
    }

    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream
    public final /* bridge */ /* synthetic */ Spliterator spliterator() {
        return spliterator();
    }
}
