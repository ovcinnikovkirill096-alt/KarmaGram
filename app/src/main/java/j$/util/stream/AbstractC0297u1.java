package j$.util.stream;

import j$.util.Spliterator;
import j$.util.function.Consumer$CC;
import java.util.concurrent.CountedCompleter;
import java.util.function.Consumer;

/* JADX INFO: renamed from: j$.util.stream.u1, reason: case insensitive filesystem */
public abstract class AbstractC0297u1 extends CountedCompleter implements InterfaceC0279q2 {
    public final Spliterator a;
    public final AbstractC0322z1 b;
    public final long c;
    public final long d;
    public final long e;
    public int f;
    public int g;

    public abstract AbstractC0297u1 a(Spliterator spliterator, long j, long j2);

    public /* synthetic */ void accept(double d) {
        AbstractC0322z1.z();
        throw null;
    }

    public /* synthetic */ void accept(int i) {
        AbstractC0322z1.G();
        throw null;
    }

    public /* synthetic */ void accept(long j) {
        AbstractC0322z1.H();
        throw null;
    }

    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer$CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ void end() {
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ boolean m() {
        return false;
    }

    public AbstractC0297u1(Spliterator spliterator, AbstractC0322z1 abstractC0322z1, int i) {
        this.a = spliterator;
        this.b = abstractC0322z1;
        this.c = AbstractC0216e.e(spliterator.estimateSize());
        this.d = 0L;
        this.e = i;
    }

    public AbstractC0297u1(AbstractC0297u1 abstractC0297u1, Spliterator spliterator, long j, long j2, int i) {
        super(abstractC0297u1);
        this.a = spliterator;
        this.b = abstractC0297u1.b;
        this.c = abstractC0297u1.c;
        this.d = j;
        this.e = j2;
        if (j < 0 || j2 < 0 || (j + j2) - 1 >= i) {
            throw new IllegalArgumentException(String.format("offset and length interval [%d, %d + %d) is not within array size interval [0, %d)", Long.valueOf(j), Long.valueOf(j), Long.valueOf(j2), Integer.valueOf(i)));
        }
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void compute() {
        Spliterator spliteratorTrySplit;
        Spliterator spliterator = this.a;
        AbstractC0297u1 abstractC0297u1A = this;
        while (spliterator.estimateSize() > abstractC0297u1A.c && (spliteratorTrySplit = spliterator.trySplit()) != null) {
            abstractC0297u1A.setPendingCount(1);
            long jEstimateSize = spliteratorTrySplit.estimateSize();
            AbstractC0297u1 abstractC0297u1 = abstractC0297u1A;
            abstractC0297u1.a(spliteratorTrySplit, abstractC0297u1A.d, jEstimateSize).fork();
            abstractC0297u1A = abstractC0297u1.a(spliterator, abstractC0297u1.d + jEstimateSize, abstractC0297u1.e - jEstimateSize);
        }
        AbstractC0297u1 abstractC0297u2 = abstractC0297u1A;
        abstractC0297u2.b.t0(spliterator, abstractC0297u2);
        abstractC0297u2.propagateCompletion();
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        long j2 = this.e;
        if (j > j2) {
            throw new IllegalStateException("size passed to Sink.begin exceeds array length");
        }
        int i = (int) this.d;
        this.f = i;
        this.g = i + ((int) j2);
    }
}
