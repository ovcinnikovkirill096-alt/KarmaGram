package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/* JADX INFO: renamed from: j$.util.stream.b, reason: case insensitive filesystem */
public abstract class AbstractC0201b extends AbstractC0322z1 implements BaseStream {
    public final AbstractC0201b h;
    public final AbstractC0201b i;
    public final int j;
    public final AbstractC0201b k;
    public int l;
    public int m;
    public Spliterator n;
    public boolean o;
    public final boolean p;
    public Runnable q;
    public boolean r;

    public abstract EnumC0220e3 A0();

    public abstract boolean D0();

    public abstract InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2);

    public abstract Spliterator H0(AbstractC0201b abstractC0201b, Supplier supplier, boolean z);

    public abstract J0 y0(AbstractC0201b abstractC0201b, Spliterator spliterator, boolean z, IntFunction intFunction);

    public abstract boolean z0(Spliterator spliterator, InterfaceC0279q2 interfaceC0279q2);

    public AbstractC0201b(Spliterator spliterator, int i, boolean z) {
        this.i = null;
        this.n = spliterator;
        this.h = this;
        int i2 = EnumC0215d3.g & i;
        this.j = i2;
        this.m = (~(i2 << 1)) & EnumC0215d3.l;
        this.l = 0;
        this.r = z;
    }

    public AbstractC0201b(AbstractC0201b abstractC0201b, int i) {
        if (abstractC0201b.o) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        abstractC0201b.o = true;
        abstractC0201b.k = this;
        this.i = abstractC0201b;
        this.j = EnumC0215d3.h & i;
        this.m = EnumC0215d3.i(i, abstractC0201b.m);
        AbstractC0201b abstractC0201b2 = abstractC0201b.h;
        this.h = abstractC0201b2;
        if (D0()) {
            abstractC0201b2.p = true;
        }
        this.l = abstractC0201b.l + 1;
    }

    public final Object w0(M3 m3) {
        if (this.o) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.o = true;
        if (this.h.r) {
            return m3.i(this, F0(m3.s()));
        }
        return m3.f(this, F0(m3.s()));
    }

    public final J0 x0(IntFunction intFunction) {
        if (this.o) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.o = true;
        if (this.h.r && this.i != null && D0()) {
            this.l = 0;
            AbstractC0201b abstractC0201b = this.i;
            return B0(abstractC0201b, abstractC0201b.F0(0), intFunction);
        }
        return d0(F0(0), true, intFunction);
    }

    public final Spliterator G0() {
        AbstractC0201b abstractC0201b = this.h;
        if (this != abstractC0201b) {
            throw new IllegalStateException();
        }
        if (this.o) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.o = true;
        Spliterator spliterator = abstractC0201b.n;
        if (spliterator != null) {
            abstractC0201b.n = null;
            return spliterator;
        }
        throw new IllegalStateException("source already consumed or closed");
    }

    public final BaseStream sequential() {
        this.h.r = false;
        return this;
    }

    public final BaseStream parallel() {
        this.h.r = true;
        return this;
    }

    @Override // j$.util.stream.BaseStream, java.lang.AutoCloseable
    public final void close() {
        this.o = true;
        this.n = null;
        AbstractC0201b abstractC0201b = this.h;
        Runnable runnable = abstractC0201b.q;
        if (runnable != null) {
            abstractC0201b.q = null;
            runnable.run();
        }
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream onClose(Runnable runnable) {
        if (this.o) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        Objects.requireNonNull(runnable);
        AbstractC0201b abstractC0201b = this.h;
        Runnable runnable2 = abstractC0201b.q;
        if (runnable2 != null) {
            runnable = new J3(0, runnable2, runnable);
        }
        abstractC0201b.q = runnable;
        return this;
    }

    public Spliterator spliterator() {
        if (this.o) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.o = true;
        AbstractC0201b abstractC0201b = this.h;
        if (this == abstractC0201b) {
            Spliterator spliterator = abstractC0201b.n;
            if (spliterator != null) {
                abstractC0201b.n = null;
                return spliterator;
            }
            throw new IllegalStateException("source already consumed or closed");
        }
        return H0(this, new C0196a(0, this), abstractC0201b.r);
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final J0 d0(Spliterator spliterator, boolean z, IntFunction intFunction) {
        if (this.h.r) {
            return y0(this, spliterator, z, intFunction);
        }
        B0 b0Q0 = q0(e0(spliterator), intFunction);
        t0(spliterator, b0Q0);
        return b0Q0.build();
    }

    @Override // j$.util.stream.BaseStream
    public final boolean isParallel() {
        return this.h.r;
    }

    public final Spliterator F0(int i) {
        int i2;
        int i3;
        AbstractC0201b abstractC0201b = this.h;
        Spliterator spliteratorC0 = abstractC0201b.n;
        if (spliteratorC0 != null) {
            abstractC0201b.n = null;
            if (abstractC0201b.r && abstractC0201b.p) {
                AbstractC0201b abstractC0201b2 = abstractC0201b.k;
                int i4 = 1;
                while (abstractC0201b != this) {
                    int i5 = abstractC0201b2.j;
                    if (abstractC0201b2.D0()) {
                        if (EnumC0215d3.SHORT_CIRCUIT.n(i5)) {
                            i5 &= ~EnumC0215d3.u;
                        }
                        spliteratorC0 = abstractC0201b2.C0(abstractC0201b, spliteratorC0);
                        if (spliteratorC0.hasCharacteristics(64)) {
                            i2 = (~EnumC0215d3.t) & i5;
                            i3 = EnumC0215d3.s;
                        } else {
                            i2 = (~EnumC0215d3.s) & i5;
                            i3 = EnumC0215d3.t;
                        }
                        i5 = i2 | i3;
                        i4 = 0;
                    }
                    int i6 = i4 + 1;
                    abstractC0201b2.l = i4;
                    abstractC0201b2.m = EnumC0215d3.i(i5, abstractC0201b.m);
                    AbstractC0201b abstractC0201b3 = abstractC0201b2;
                    abstractC0201b2 = abstractC0201b2.k;
                    abstractC0201b = abstractC0201b3;
                    i4 = i6;
                }
            }
            if (i != 0) {
                this.m = EnumC0215d3.i(i, this.m);
            }
            return spliteratorC0;
        }
        throw new IllegalStateException("source already consumed or closed");
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final long e0(Spliterator spliterator) {
        if (EnumC0215d3.SIZED.n(this.m)) {
            return spliterator.getExactSizeIfKnown();
        }
        return -1L;
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final InterfaceC0279q2 t0(Spliterator spliterator, InterfaceC0279q2 interfaceC0279q2) {
        Z(spliterator, u0((InterfaceC0279q2) Objects.requireNonNull(interfaceC0279q2)));
        return interfaceC0279q2;
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final void Z(Spliterator spliterator, InterfaceC0279q2 interfaceC0279q2) {
        Objects.requireNonNull(interfaceC0279q2);
        if (!EnumC0215d3.SHORT_CIRCUIT.n(this.m)) {
            interfaceC0279q2.h(spliterator.getExactSizeIfKnown());
            spliterator.forEachRemaining(interfaceC0279q2);
            interfaceC0279q2.end();
            return;
        }
        a0(spliterator, interfaceC0279q2);
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final boolean a0(Spliterator spliterator, InterfaceC0279q2 interfaceC0279q2) {
        AbstractC0201b abstractC0201b = this;
        while (abstractC0201b.l > 0) {
            abstractC0201b = abstractC0201b.i;
        }
        interfaceC0279q2.h(spliterator.getExactSizeIfKnown());
        boolean zZ0 = abstractC0201b.z0(spliterator, interfaceC0279q2);
        interfaceC0279q2.end();
        return zZ0;
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final InterfaceC0279q2 u0(InterfaceC0279q2 interfaceC0279q2) {
        Objects.requireNonNull(interfaceC0279q2);
        for (AbstractC0201b abstractC0201b = this; abstractC0201b.l > 0; abstractC0201b = abstractC0201b.i) {
            interfaceC0279q2 = abstractC0201b.E0(abstractC0201b.i.m, interfaceC0279q2);
        }
        return interfaceC0279q2;
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final Spliterator v0(Spliterator spliterator) {
        return this.l == 0 ? spliterator : H0(this, new C0196a(1, spliterator), this.h.r);
    }

    public J0 B0(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, IntFunction intFunction) {
        throw new UnsupportedOperationException("Parallel evaluation is not supported");
    }

    public Spliterator C0(AbstractC0201b abstractC0201b, Spliterator spliterator) {
        return B0(abstractC0201b, spliterator, new j$.time.e(11)).spliterator();
    }
}
