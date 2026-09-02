package j$.util.stream;

import j$.util.Spliterator;

/* JADX INFO: renamed from: j$.util.stream.y3, reason: case insensitive filesystem */
public abstract class AbstractC0319y3 {
    public final long a;
    public final long b;
    public Spliterator c;
    public long d;
    public long e;

    public abstract Spliterator a(Spliterator spliterator, long j, long j2, long j3, long j4);

    public AbstractC0319y3(Spliterator spliterator, long j, long j2, long j3, long j4) {
        this.c = spliterator;
        this.a = j;
        this.b = j2;
        this.d = j3;
        this.e = j4;
    }

    /* JADX INFO: renamed from: trySplit, reason: collision with other method in class */
    public final Spliterator m2432trySplit() {
        long j = this.e;
        if (this.a >= j || this.d >= j) {
            return null;
        }
        while (true) {
            Spliterator spliteratorTrySplit = this.c.trySplit();
            if (spliteratorTrySplit == null) {
                return null;
            }
            long jEstimateSize = spliteratorTrySplit.estimateSize() + this.d;
            long jMin = Math.min(jEstimateSize, this.b);
            long j2 = this.a;
            if (j2 >= jMin) {
                this.d = jMin;
            } else {
                long j3 = this.b;
                if (jMin >= j3) {
                    this.c = spliteratorTrySplit;
                    this.e = jMin;
                } else {
                    long j4 = this.d;
                    if (j4 >= j2 && jEstimateSize <= j3) {
                        this.d = jMin;
                        return spliteratorTrySplit;
                    }
                    this.d = jMin;
                    return a(spliteratorTrySplit, j2, j3, j4, jMin);
                }
            }
        }
    }

    public final long estimateSize() {
        long j = this.e;
        long j2 = this.a;
        if (j2 < j) {
            return j - Math.max(j2, this.d);
        }
        return 0L;
    }

    public final int characteristics() {
        return this.c.characteristics();
    }

    /* JADX INFO: renamed from: trySplit, reason: collision with other method in class */
    public /* bridge */ /* synthetic */ j$.util.c0 m2435trySplit() {
        return (j$.util.c0) m2432trySplit();
    }

    public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
        return (Spliterator.OfInt) m2432trySplit();
    }

    /* JADX INFO: renamed from: trySplit, reason: collision with other method in class */
    public /* bridge */ /* synthetic */ j$.util.Z m2434trySplit() {
        return (j$.util.Z) m2432trySplit();
    }

    /* JADX INFO: renamed from: trySplit, reason: collision with other method in class */
    public /* bridge */ /* synthetic */ j$.util.U m2433trySplit() {
        return (j$.util.U) m2432trySplit();
    }
}
