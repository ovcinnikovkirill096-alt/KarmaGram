package j$.util.stream;

import j$.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.function.IntFunction;

public final class c4 extends AbstractC0206c {
    public final AbstractC0201b j;
    public final IntFunction k;
    public final boolean l;
    public long m;
    public boolean n;
    public volatile boolean o;

    @Override // j$.util.stream.AbstractC0206c
    public final void f() {
        this.i = true;
        if (this.l && this.o) {
            d(AbstractC0322z1.c0(this.j.A0()));
        }
    }

    /* JADX WARN: Code duplicated, block: B:15:0x0041  */
    /* JADX WARN: Code duplicated, block: B:17:0x0056  */
    /* JADX WARN: Code duplicated, block: B:18:0x005d  */
    /* JADX WARN: Code duplicated, block: B:20:0x0063  */
    /* JADX WARN: Code duplicated, block: B:21:0x006a  */
    @Override // j$.util.stream.AbstractC0216e, java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        c4 c4Var;
        c4 c4Var2;
        Object objY;
        AbstractC0216e abstractC0216e = this.d;
        if (abstractC0216e != null) {
            this.n = ((c4) abstractC0216e).n | ((c4) this.e).n;
            if (this.l && this.i) {
                this.m = 0L;
                objY = AbstractC0322z1.c0(this.j.A0());
            } else if (this.l) {
                c4 c4Var3 = (c4) this.d;
                if (c4Var3.n) {
                    this.m = c4Var3.m;
                    objY = (J0) c4Var3.i();
                } else {
                    c4Var = (c4) this.d;
                    long j = c4Var.m;
                    c4Var2 = (c4) this.e;
                    this.m = j + c4Var2.m;
                    if (c4Var.m == 0) {
                        objY = (J0) c4Var2.i();
                    } else if (c4Var2.m == 0) {
                        objY = (J0) c4Var.i();
                    } else {
                        objY = AbstractC0322z1.Y(this.j.A0(), (J0) ((c4) this.d).i(), (J0) ((c4) this.e).i());
                    }
                }
            } else {
                c4Var = (c4) this.d;
                long j2 = c4Var.m;
                c4Var2 = (c4) this.e;
                this.m = j2 + c4Var2.m;
                if (c4Var.m == 0) {
                    objY = (J0) c4Var2.i();
                } else if (c4Var2.m == 0) {
                    objY = (J0) c4Var.i();
                } else {
                    objY = AbstractC0322z1.Y(this.j.A0(), (J0) ((c4) this.d).i(), (J0) ((c4) this.e).i());
                }
            }
            d(objY);
        }
        this.o = true;
        super.onCompletion(countedCompleter);
    }

    public c4(AbstractC0201b abstractC0201b, AbstractC0322z1 abstractC0322z1, Spliterator spliterator, IntFunction intFunction) {
        super(abstractC0322z1, spliterator);
        this.j = abstractC0201b;
        this.k = intFunction;
        this.l = EnumC0215d3.ORDERED.n(((AbstractC0201b) abstractC0322z1).m);
    }

    public c4(c4 c4Var, Spliterator spliterator) {
        super(c4Var, spliterator);
        this.j = c4Var.j;
        this.k = c4Var.k;
        this.l = c4Var.l;
    }

    @Override // j$.util.stream.AbstractC0216e
    public final AbstractC0216e c(Spliterator spliterator) {
        return new c4(this, spliterator);
    }

    @Override // j$.util.stream.AbstractC0206c
    public final Object h() {
        return AbstractC0322z1.c0(this.j.A0());
    }

    @Override // j$.util.stream.AbstractC0216e
    public final Object a() {
        B0 b0Q0 = this.a.q0(-1L, this.k);
        InterfaceC0279q2 interfaceC0279q2E0 = this.j.E0(((AbstractC0201b) this.a).m, b0Q0);
        AbstractC0322z1 abstractC0322z1 = this.a;
        boolean zA0 = abstractC0322z1.a0(this.b, abstractC0322z1.u0(interfaceC0279q2E0));
        this.n = zA0;
        if (zA0) {
            g();
        }
        J0 j0Build = b0Q0.build();
        this.m = j0Build.count();
        return j0Build;
    }
}
