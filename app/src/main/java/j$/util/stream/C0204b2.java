package j$.util.stream;

import j$.util.Spliterator;
import java.util.concurrent.CountedCompleter;

/* JADX INFO: renamed from: j$.util.stream.b2, reason: case insensitive filesystem */
public final class C0204b2 extends AbstractC0216e {
    public final AbstractC0322z1 h;

    @Override // j$.util.stream.AbstractC0216e, java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        AbstractC0216e abstractC0216e = this.d;
        if (abstractC0216e != null) {
            U1 u1 = (U1) ((C0204b2) abstractC0216e).f;
            u1.q((U1) ((C0204b2) this.e).f);
            this.f = u1;
        }
        super.onCompletion(countedCompleter);
    }

    public C0204b2(AbstractC0322z1 abstractC0322z1, AbstractC0322z1 abstractC0322z2, Spliterator spliterator) {
        super(abstractC0322z2, spliterator);
        this.h = abstractC0322z1;
    }

    public C0204b2(C0204b2 c0204b2, Spliterator spliterator) {
        super(c0204b2, spliterator);
        this.h = c0204b2.h;
    }

    @Override // j$.util.stream.AbstractC0216e
    public final AbstractC0216e c(Spliterator spliterator) {
        return new C0204b2(this, spliterator);
    }

    @Override // j$.util.stream.AbstractC0216e
    public final Object a() {
        AbstractC0322z1 abstractC0322z1 = this.a;
        U1 u1S0 = this.h.s0();
        abstractC0322z1.t0(this.b, u1S0);
        return u1S0;
    }
}
