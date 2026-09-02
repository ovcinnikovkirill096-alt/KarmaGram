package j$.util.stream;

import j$.util.Spliterator;

public abstract class B extends C {
    @Override // j$.util.stream.AbstractC0201b
    public final boolean D0() {
        return false;
    }

    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream, j$.util.stream.F
    public final F sequential() {
        this.h.r = false;
        return this;
    }

    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream, j$.util.stream.F
    public final F parallel() {
        this.h.r = true;
        return this;
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !EnumC0215d3.ORDERED.n(this.m) ? this : new C0295u(this, EnumC0215d3.r, 1);
    }

    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream
    public final /* bridge */ /* synthetic */ Spliterator spliterator() {
        return spliterator();
    }
}
