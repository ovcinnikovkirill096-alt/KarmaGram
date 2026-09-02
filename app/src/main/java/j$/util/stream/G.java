package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class G implements M3 {
    public final int a;
    public final Object b;
    public final Predicate c;
    public final Supplier d;

    public G(boolean z, EnumC0220e3 enumC0220e3, Object obj, Predicate predicate, Supplier supplier) {
        this.a = (z ? 0 : EnumC0215d3.r) | EnumC0215d3.u;
        this.b = obj;
        this.c = predicate;
        this.d = supplier;
    }

    @Override // j$.util.stream.M3
    public final int s() {
        return this.a;
    }

    @Override // j$.util.stream.M3
    public final Object f(AbstractC0201b abstractC0201b, Spliterator spliterator) {
        N3 n3 = (N3) this.d.get();
        abstractC0201b.t0(spliterator, n3);
        Object obj = n3.get();
        return obj != null ? obj : this.b;
    }

    @Override // j$.util.stream.M3
    public final Object i(AbstractC0322z1 abstractC0322z1, Spliterator spliterator) {
        AbstractC0201b abstractC0201b = (AbstractC0201b) abstractC0322z1;
        return new M(this, EnumC0215d3.ORDERED.n(abstractC0201b.m), abstractC0201b, spliterator).invoke();
    }
}
