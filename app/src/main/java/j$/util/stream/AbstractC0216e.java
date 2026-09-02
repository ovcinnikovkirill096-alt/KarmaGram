package j$.util.stream;

import j$.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;

/* JADX INFO: renamed from: j$.util.stream.e, reason: case insensitive filesystem */
public abstract class AbstractC0216e extends CountedCompleter {
    public static final int g = ForkJoinPool.getCommonPoolParallelism() << 2;
    public final AbstractC0322z1 a;
    public Spliterator b;
    public long c;
    public AbstractC0216e d;
    public AbstractC0216e e;
    public Object f;

    public abstract Object a();

    public abstract AbstractC0216e c(Spliterator spliterator);

    public AbstractC0216e(AbstractC0322z1 abstractC0322z1, Spliterator spliterator) {
        super(null);
        this.a = abstractC0322z1;
        this.b = spliterator;
        this.c = 0L;
    }

    public AbstractC0216e(AbstractC0216e abstractC0216e, Spliterator spliterator) {
        super(abstractC0216e);
        this.b = spliterator;
        this.a = abstractC0216e.a;
        this.c = abstractC0216e.c;
    }

    public static long e(long j) {
        long j2 = j / ((long) g);
        if (j2 > 0) {
            return j2;
        }
        return 1L;
    }

    @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public Object getRawResult() {
        return this.f;
    }

    @Override // java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public final void setRawResult(Object obj) {
        if (obj != null) {
            throw new IllegalStateException();
        }
    }

    public void d(Object obj) {
        this.f = obj;
    }

    public final boolean b() {
        return ((AbstractC0216e) getCompleter()) == null;
    }

    @Override // java.util.concurrent.CountedCompleter
    public void compute() {
        Spliterator spliteratorTrySplit;
        Spliterator spliterator = this.b;
        long jEstimateSize = spliterator.estimateSize();
        long jE = this.c;
        if (jE == 0) {
            jE = e(jEstimateSize);
            this.c = jE;
        }
        boolean z = false;
        AbstractC0216e abstractC0216e = this;
        while (jEstimateSize > jE && (spliteratorTrySplit = spliterator.trySplit()) != null) {
            AbstractC0216e abstractC0216eC = abstractC0216e.c(spliteratorTrySplit);
            abstractC0216e.d = abstractC0216eC;
            AbstractC0216e abstractC0216eC2 = abstractC0216e.c(spliterator);
            abstractC0216e.e = abstractC0216eC2;
            abstractC0216e.setPendingCount(1);
            if (z) {
                spliterator = spliteratorTrySplit;
                abstractC0216e = abstractC0216eC;
                abstractC0216eC = abstractC0216eC2;
            } else {
                abstractC0216e = abstractC0216eC2;
            }
            z = !z;
            abstractC0216eC.fork();
            jEstimateSize = spliterator.estimateSize();
        }
        abstractC0216e.d(abstractC0216e.a());
        abstractC0216e.tryComplete();
    }

    @Override // java.util.concurrent.CountedCompleter
    public void onCompletion(CountedCompleter countedCompleter) {
        this.b = null;
        this.e = null;
        this.d = null;
    }
}
