package j$.util.stream;

import j$.util.Spliterator;
import java.util.concurrent.CountedCompleter;

public final class V extends CountedCompleter {
    public Spliterator a;
    public final InterfaceC0279q2 b;
    public final AbstractC0322z1 c;
    public long d;

    public V(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, InterfaceC0279q2 interfaceC0279q2) {
        super(null);
        this.b = interfaceC0279q2;
        this.c = abstractC0322z1;
        this.a = spliterator;
        this.d = 0L;
    }

    public V(V v, Spliterator spliterator) {
        super(v);
        this.a = spliterator;
        this.b = v.b;
        this.d = v.d;
        this.c = v.c;
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void compute() {
        Spliterator spliteratorTrySplit;
        Spliterator spliterator = this.a;
        long jEstimateSize = spliterator.estimateSize();
        long jE = this.d;
        if (jE == 0) {
            jE = AbstractC0216e.e(jEstimateSize);
            this.d = jE;
        }
        boolean zN = EnumC0215d3.SHORT_CIRCUIT.n(((AbstractC0201b) this.c).m);
        InterfaceC0279q2 interfaceC0279q2 = this.b;
        boolean z = false;
        V v = this;
        while (true) {
            if (zN && interfaceC0279q2.m()) {
                break;
            }
            if (jEstimateSize <= jE || (spliteratorTrySplit = spliterator.trySplit()) == null) {
                v.c.Z(spliterator, interfaceC0279q2);
                break;
            }
            V v2 = new V(v, spliteratorTrySplit);
            v.addToPendingCount(1);
            if (z) {
                spliterator = spliteratorTrySplit;
            } else {
                V v3 = v;
                v = v2;
                v2 = v3;
            }
            z = !z;
            v.fork();
            v = v2;
            jEstimateSize = spliterator.estimateSize();
        }
        v.a = null;
        v.propagateCompletion();
    }
}
