package j$.util.stream;

import j$.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicReference;

public final class M extends AbstractC0206c {
    public final G j;
    public final boolean k;

    public M(G g, boolean z, AbstractC0201b abstractC0201b, Spliterator spliterator) {
        super(abstractC0201b, spliterator);
        this.k = z;
        this.j = g;
    }

    public M(M m, Spliterator spliterator) {
        super(m, spliterator);
        this.k = m.k;
        this.j = m.j;
    }

    @Override // j$.util.stream.AbstractC0216e
    public final AbstractC0216e c(Spliterator spliterator) {
        return new M(this, spliterator);
    }

    @Override // j$.util.stream.AbstractC0206c
    public final Object h() {
        return this.j.b;
    }

    @Override // j$.util.stream.AbstractC0216e
    public final Object a() {
        AbstractC0322z1 abstractC0322z1 = this.a;
        N3 n3 = (N3) this.j.d.get();
        abstractC0322z1.t0(this.b, n3);
        Object obj = n3.get();
        if (this.k) {
            if (obj != null) {
                AbstractC0216e abstractC0216e = this;
                while (abstractC0216e != null) {
                    AbstractC0216e abstractC0216e2 = (AbstractC0216e) abstractC0216e.getCompleter();
                    if (abstractC0216e2 != null && abstractC0216e2.d != abstractC0216e) {
                        g();
                        return obj;
                    }
                    abstractC0216e = abstractC0216e2;
                }
                AtomicReference atomicReference = this.h;
                while (!atomicReference.compareAndSet(null, obj) && atomicReference.get() == null) {
                }
                return obj;
            }
        } else if (obj != null) {
            AtomicReference atomicReference2 = this.h;
            while (!atomicReference2.compareAndSet(null, obj) && atomicReference2.get() == null) {
            }
        }
        return null;
    }

    @Override // j$.util.stream.AbstractC0216e, java.util.concurrent.CountedCompleter
    public final void onCompletion(CountedCompleter countedCompleter) {
        if (this.k) {
            M m = (M) this.d;
            M m2 = null;
            while (m != m2) {
                Object objI = m.i();
                if (objI != null && this.j.c.test(objI)) {
                    d(objI);
                    AbstractC0216e abstractC0216e = this;
                    while (abstractC0216e != null) {
                        AbstractC0216e abstractC0216e2 = (AbstractC0216e) abstractC0216e.getCompleter();
                        if (abstractC0216e2 != null && abstractC0216e2.d != abstractC0216e) {
                            g();
                            break;
                        }
                        abstractC0216e = abstractC0216e2;
                    }
                    AtomicReference atomicReference = this.h;
                    while (!atomicReference.compareAndSet(null, objI) && atomicReference.get() == null) {
                    }
                    break;
                }
                m2 = m;
                m = (M) this.e;
            }
        }
        super.onCompletion(countedCompleter);
    }
}
