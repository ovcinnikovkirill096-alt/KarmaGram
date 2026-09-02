package j$.util.stream;

import j$.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.function.BinaryOperator;
import java.util.function.LongFunction;

public class P0 extends AbstractC0216e {
    public final AbstractC0322z1 h;
    public final LongFunction i;
    public final BinaryOperator j;

    @Override // j$.util.stream.AbstractC0216e, java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        AbstractC0216e abstractC0216e = this.d;
        if (abstractC0216e != null) {
            this.f = (J0) this.j.apply((J0) ((P0) abstractC0216e).f, (J0) ((P0) this.e).f);
        }
        super.onCompletion(countedCompleter);
    }

    public P0(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, LongFunction longFunction, BinaryOperator binaryOperator) {
        super(abstractC0322z1, spliterator);
        this.h = abstractC0322z1;
        this.i = longFunction;
        this.j = binaryOperator;
    }

    public P0(P0 p0, Spliterator spliterator) {
        super(p0, spliterator);
        this.h = p0.h;
        this.i = p0.i;
        this.j = p0.j;
    }

    @Override // j$.util.stream.AbstractC0216e
    public AbstractC0216e c(Spliterator spliterator) {
        return new P0(this, spliterator);
    }

    @Override // j$.util.stream.AbstractC0216e
    /* JADX INFO: renamed from: f, reason: merged with bridge method [inline-methods] */
    public final J0 a() {
        B0 b0 = (B0) this.i.apply(this.h.e0(this.b));
        this.h.t0(this.b, b0);
        return b0.build();
    }
}
