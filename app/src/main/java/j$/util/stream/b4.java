package j$.util.stream;

import j$.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.function.IntFunction;

public final class b4 extends AbstractC0216e {
    public final AbstractC0201b h;
    public final IntFunction i;
    public final boolean j;
    public long k;
    public long l;

    @Override // j$.util.stream.AbstractC0216e, java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        J0 j0Y;
        AbstractC0216e abstractC0216e = this.d;
        if (abstractC0216e != null) {
            if (this.j) {
                b4 b4Var = (b4) abstractC0216e;
                long j = b4Var.l;
                this.l = j;
                if (j == b4Var.k) {
                    this.l = j + ((b4) this.e).l;
                }
            }
            b4 b4Var2 = (b4) abstractC0216e;
            long j2 = b4Var2.k;
            b4 b4Var3 = (b4) this.e;
            this.k = j2 + b4Var3.k;
            if (b4Var2.k == 0) {
                j0Y = (J0) b4Var3.f;
            } else {
                j0Y = b4Var3.k == 0 ? (J0) b4Var2.f : AbstractC0322z1.Y(this.h.A0(), (J0) ((b4) this.d).f, (J0) ((b4) this.e).f);
            }
            J0 j0E = j0Y;
            if (b() && this.j) {
                j0E = j0E.e(this.l, j0E.count(), this.i);
            }
            this.f = j0E;
        }
        super.onCompletion(countedCompleter);
    }

    public b4(AbstractC0201b abstractC0201b, AbstractC0322z1 abstractC0322z1, Spliterator spliterator, IntFunction intFunction) {
        super(abstractC0322z1, spliterator);
        this.h = abstractC0201b;
        this.i = intFunction;
        this.j = EnumC0215d3.ORDERED.n(((AbstractC0201b) abstractC0322z1).m);
    }

    public b4(b4 b4Var, Spliterator spliterator) {
        super(b4Var, spliterator);
        this.h = b4Var.h;
        this.i = b4Var.i;
        this.j = b4Var.j;
    }

    @Override // j$.util.stream.AbstractC0216e
    public final AbstractC0216e c(Spliterator spliterator) {
        return new b4(this, spliterator);
    }

    /* JADX WARN: Code duplicated, block: B:9:0x001c  */
    @Override // j$.util.stream.AbstractC0216e
    public final Object a() {
        long jE0;
        boolean zB = b();
        if (zB || !this.j) {
            jE0 = -1;
        } else {
            EnumC0215d3 enumC0215d3 = EnumC0215d3.SIZED;
            AbstractC0201b abstractC0201b = this.h;
            int i = abstractC0201b.j;
            int i2 = enumC0215d3.e;
            if ((i & i2) == i2) {
                jE0 = abstractC0201b.e0(this.b);
            } else {
                jE0 = -1;
            }
        }
        B0 b0Q0 = this.a.q0(jE0, this.i);
        a4 a4VarH = ((Z3) this.h).h(b0Q0, this.j && !zB);
        this.a.t0(this.b, a4VarH);
        J0 j0Build = b0Q0.build();
        this.k = j0Build.count();
        this.l = a4VarH.n();
        return j0Build;
    }
}
