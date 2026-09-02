package j$.util.stream;

import j$.util.Spliterator;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountedCompleter;

public final class U extends CountedCompleter {
    public final AbstractC0322z1 a;
    public Spliterator b;
    public final long c;
    public final ConcurrentHashMap d;
    public final T e;
    public final U f;
    public J0 g;

    public U(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, T t) {
        super(null);
        this.a = abstractC0322z1;
        this.b = spliterator;
        this.c = AbstractC0216e.e(spliterator.estimateSize());
        this.d = new ConcurrentHashMap(Math.max(16, AbstractC0216e.g << 1));
        this.e = t;
        this.f = null;
    }

    public U(U u, Spliterator spliterator, U u2) {
        super(u);
        this.a = u.a;
        this.b = spliterator;
        this.c = u.c;
        this.d = u.d;
        this.e = u.e;
        this.f = u2;
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void compute() {
        Spliterator spliteratorTrySplit;
        Spliterator spliterator = this.b;
        long j = this.c;
        boolean z = false;
        U u = this;
        while (spliterator.estimateSize() > j && (spliteratorTrySplit = spliterator.trySplit()) != null) {
            U u2 = new U(u, spliteratorTrySplit, u.f);
            U u3 = new U(u, spliterator, u2);
            u.addToPendingCount(1);
            u3.addToPendingCount(1);
            u.d.put(u2, u3);
            if (u.f != null) {
                u2.addToPendingCount(1);
                if (u.d.replace(u.f, u, u2)) {
                    u.addToPendingCount(-1);
                } else {
                    u2.addToPendingCount(-1);
                }
            }
            if (z) {
                spliterator = spliteratorTrySplit;
                u = u2;
                u2 = u3;
            } else {
                u = u3;
            }
            z = !z;
            u2.fork();
        }
        if (u.getPendingCount() > 0) {
            C0276q c0276q = new C0276q(12);
            AbstractC0322z1 abstractC0322z1 = u.a;
            B0 b0Q0 = abstractC0322z1.q0(abstractC0322z1.e0(spliterator), c0276q);
            u.a.t0(spliterator, b0Q0);
            u.g = b0Q0.build();
            u.b = null;
        }
        u.tryComplete();
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        J0 j0 = this.g;
        if (j0 != null) {
            j0.forEach(this.e);
            this.g = null;
        } else {
            Spliterator spliterator = this.b;
            if (spliterator != null) {
                this.a.t0(spliterator, this.e);
                this.b = null;
            }
        }
        U u = (U) this.d.remove(this);
        if (u != null) {
            u.tryComplete();
        }
    }
}
