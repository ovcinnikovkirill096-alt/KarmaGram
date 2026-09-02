package j$.util.stream;

import j$.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: renamed from: j$.util.stream.c, reason: case insensitive filesystem */
public abstract class AbstractC0206c extends AbstractC0216e {
    public final AtomicReference h;
    public volatile boolean i;

    public abstract Object h();

    public AbstractC0206c(AbstractC0322z1 abstractC0322z1, Spliterator spliterator) {
        super(abstractC0322z1, spliterator);
        this.h = new AtomicReference(null);
    }

    public AbstractC0206c(AbstractC0206c abstractC0206c, Spliterator spliterator) {
        super(abstractC0206c, spliterator);
        this.h = abstractC0206c.h;
    }

    @Override // j$.util.stream.AbstractC0216e, java.util.concurrent.CountedCompleter
    public final void compute() {
        Object objH;
        Spliterator spliteratorTrySplit;
        Spliterator spliterator = this.b;
        long jEstimateSize = spliterator.estimateSize();
        long jE = this.c;
        if (jE == 0) {
            jE = AbstractC0216e.e(jEstimateSize);
            this.c = jE;
        }
        AtomicReference atomicReference = this.h;
        boolean z = false;
        AbstractC0206c abstractC0206c = this;
        while (true) {
            objH = atomicReference.get();
            if (objH != null) {
                break;
            }
            boolean z2 = abstractC0206c.i;
            if (!z2) {
                CountedCompleter<?> completer = abstractC0206c.getCompleter();
                while (true) {
                    AbstractC0206c abstractC0206c2 = (AbstractC0206c) ((AbstractC0216e) completer);
                    if (z2 || abstractC0206c2 == null) {
                        break;
                    }
                    z2 = abstractC0206c2.i;
                    completer = abstractC0206c2.getCompleter();
                }
            }
            if (z2) {
                objH = abstractC0206c.h();
                break;
            }
            if (jEstimateSize <= jE || (spliteratorTrySplit = spliterator.trySplit()) == null) {
                objH = abstractC0206c.a();
                break;
            }
            AbstractC0206c abstractC0206c3 = (AbstractC0206c) abstractC0206c.c(spliteratorTrySplit);
            abstractC0206c.d = abstractC0206c3;
            AbstractC0206c abstractC0206c4 = (AbstractC0206c) abstractC0206c.c(spliterator);
            abstractC0206c.e = abstractC0206c4;
            abstractC0206c.setPendingCount(1);
            if (z) {
                spliterator = spliteratorTrySplit;
                abstractC0206c = abstractC0206c3;
                abstractC0206c3 = abstractC0206c4;
            } else {
                abstractC0206c = abstractC0206c4;
            }
            z = !z;
            abstractC0206c3.fork();
            jEstimateSize = spliterator.estimateSize();
        }
        abstractC0206c.d(objH);
        abstractC0206c.tryComplete();
    }

    @Override // j$.util.stream.AbstractC0216e
    public final void d(Object obj) {
        if (!b()) {
            this.f = obj;
        } else if (obj != null) {
            AtomicReference atomicReference = this.h;
            while (!atomicReference.compareAndSet(null, obj) && atomicReference.get() == null) {
            }
        }
    }

    @Override // j$.util.stream.AbstractC0216e, java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public final Object getRawResult() {
        return i();
    }

    public final Object i() {
        if (b()) {
            Object obj = this.h.get();
            return obj == null ? h() : obj;
        }
        return this.f;
    }

    public void f() {
        this.i = true;
    }

    public final void g() {
        AbstractC0206c abstractC0206c = this;
        for (AbstractC0206c abstractC0206c2 = (AbstractC0206c) ((AbstractC0216e) getCompleter()); abstractC0206c2 != null; abstractC0206c2 = (AbstractC0206c) ((AbstractC0216e) abstractC0206c2.getCompleter())) {
            if (abstractC0206c2.d == abstractC0206c) {
                AbstractC0206c abstractC0206c3 = (AbstractC0206c) abstractC0206c2.e;
                if (!abstractC0206c3.i) {
                    abstractC0206c3.f();
                }
            }
            abstractC0206c = abstractC0206c2;
        }
    }
}
