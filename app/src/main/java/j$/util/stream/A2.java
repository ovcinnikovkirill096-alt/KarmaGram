package j$.util.stream;

import j$.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.function.IntFunction;

public final class A2 extends AbstractC0206c {
    public final AbstractC0201b j;
    public final IntFunction k;
    public final long l;
    public final long m;
    public long n;
    public volatile boolean o;

    @Override // j$.util.stream.AbstractC0206c
    public final void f() {
        this.i = true;
        if (this.o) {
            d(AbstractC0322z1.c0(this.j.A0()));
        }
    }

    @Override // j$.util.stream.AbstractC0216e, java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        A2 a2;
        J0 j0Y;
        AbstractC0216e abstractC0216e = this.d;
        if (abstractC0216e != null) {
            this.n = ((A2) abstractC0216e).n + ((A2) this.e).n;
            if (this.i) {
                this.n = 0L;
                j0Y = AbstractC0322z1.c0(this.j.A0());
            } else if (this.n == 0) {
                j0Y = AbstractC0322z1.c0(this.j.A0());
            } else {
                j0Y = ((A2) this.d).n == 0 ? (J0) ((A2) this.e).i() : AbstractC0322z1.Y(this.j.A0(), (J0) ((A2) this.d).i(), (J0) ((A2) this.e).i());
            }
            J0 j0E = j0Y;
            if (b()) {
                j0E = j0E.e(this.l, this.m >= 0 ? Math.min(j0E.count(), this.l + this.m) : this.n, this.k);
            }
            d(j0E);
            this.o = true;
        }
        if (this.m >= 0 && !b()) {
            long j = this.l + this.m;
            long j2 = this.o ? this.n : j(j);
            if (j2 >= j) {
                g();
            } else {
                A2 a3 = (A2) ((AbstractC0216e) getCompleter());
                Object obj = this;
                while (true) {
                    if (a3 == null) {
                        if (j2 >= j) {
                            break;
                        }
                    } else {
                        if (obj == a3.e && (a2 = (A2) a3.d) != null) {
                            long j3 = a2.j(j) + j2;
                            if (j3 >= j) {
                                break;
                            } else {
                                j2 = j3;
                            }
                        }
                        obj = a3;
                        a3 = (A2) ((AbstractC0216e) a3.getCompleter());
                    }
                }
                g();
            }
        }
        super.onCompletion(countedCompleter);
    }

    public A2(AbstractC0201b abstractC0201b, AbstractC0322z1 abstractC0322z1, Spliterator spliterator, IntFunction intFunction, long j, long j2) {
        super(abstractC0322z1, spliterator);
        this.j = abstractC0201b;
        this.k = intFunction;
        this.l = j;
        this.m = j2;
    }

    public A2(A2 a2, Spliterator spliterator) {
        super(a2, spliterator);
        this.j = a2.j;
        this.k = a2.k;
        this.l = a2.l;
        this.m = a2.m;
    }

    @Override // j$.util.stream.AbstractC0216e
    public final AbstractC0216e c(Spliterator spliterator) {
        return new A2(this, spliterator);
    }

    @Override // j$.util.stream.AbstractC0206c
    public final Object h() {
        return AbstractC0322z1.c0(this.j.A0());
    }

    @Override // j$.util.stream.AbstractC0216e
    public final Object a() {
        if (b()) {
            EnumC0215d3 enumC0215d3 = EnumC0215d3.SIZED;
            AbstractC0201b abstractC0201b = this.j;
            int i = abstractC0201b.j;
            int i2 = enumC0215d3.e;
            B0 b0Q0 = this.j.q0((i & i2) == i2 ? abstractC0201b.e0(this.b) : -1L, this.k);
            InterfaceC0279q2 interfaceC0279q2E0 = this.j.E0(((AbstractC0201b) this.a).m, b0Q0);
            AbstractC0322z1 abstractC0322z1 = this.a;
            abstractC0322z1.a0(this.b, abstractC0322z1.u0(interfaceC0279q2E0));
            return b0Q0.build();
        }
        B0 b0Q1 = this.j.q0(-1L, this.k);
        if (this.l == 0) {
            InterfaceC0279q2 interfaceC0279q2E1 = this.j.E0(((AbstractC0201b) this.a).m, b0Q1);
            AbstractC0322z1 abstractC0322z2 = this.a;
            abstractC0322z2.a0(this.b, abstractC0322z2.u0(interfaceC0279q2E1));
        } else {
            this.a.t0(this.b, b0Q1);
        }
        J0 j0Build = b0Q1.build();
        this.n = j0Build.count();
        this.o = true;
        this.b = null;
        return j0Build;
    }

    public final long j(long j) {
        if (this.o) {
            return this.n;
        }
        A2 a2 = (A2) this.d;
        A2 a3 = (A2) this.e;
        if (a2 == null || a3 == null) {
            return this.n;
        }
        long j2 = a2.j(j);
        return j2 >= j ? j2 : a3.j(j) + j2;
    }
}
