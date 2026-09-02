package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/* JADX INFO: renamed from: j$.util.stream.q3, reason: case insensitive filesystem */
public final class C0280q3 extends AbstractC0225f3 implements Spliterator.OfInt {
    @Override // j$.util.Spliterator
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.com.android.tools.r8.a.k(this, consumer);
    }

    @Override // j$.util.Spliterator
    public final /* synthetic */ boolean tryAdvance(Consumer consumer) {
        return j$.com.android.tools.r8.a.A(this, consumer);
    }

    @Override // j$.util.stream.AbstractC0225f3
    public final AbstractC0225f3 e(Spliterator spliterator) {
        return new C0280q3(this.b, spliterator, this.a);
    }

    @Override // j$.util.stream.AbstractC0225f3
    public final void d() {
        W2 w2 = new W2();
        this.h = w2;
        Objects.requireNonNull(w2);
        this.e = this.b.u0(new C0275p3(w2, 1));
        this.f = new j$.time.t(12, this);
    }

    @Override // j$.util.stream.AbstractC0225f3, j$.util.Spliterator
    public final Spliterator.OfInt trySplit() {
        return (Spliterator.OfInt) super.trySplit();
    }

    @Override // j$.util.stream.AbstractC0225f3, j$.util.Spliterator
    public final Spliterator trySplit() {
        return (Spliterator.OfInt) super.trySplit();
    }

    @Override // j$.util.stream.AbstractC0225f3, j$.util.Spliterator
    public final j$.util.c0 trySplit() {
        return (Spliterator.OfInt) super.trySplit();
    }

    @Override // j$.util.c0
    public final boolean tryAdvance(IntConsumer intConsumer) {
        int i;
        Objects.requireNonNull(intConsumer);
        boolean zA = a();
        if (zA) {
            W2 w2 = (W2) this.h;
            long j = this.g;
            int iO = w2.o(j);
            if (w2.c == 0 && iO == 0) {
                i = ((int[]) w2.e)[(int) j];
            } else {
                i = ((int[][]) w2.f)[iO][(int) (j - w2.d[iO])];
            }
            intConsumer.accept(i);
        }
        return zA;
    }

    @Override // j$.util.c0
    public final void forEachRemaining(IntConsumer intConsumer) {
        if (this.h == null && !this.i) {
            Objects.requireNonNull(intConsumer);
            c();
            Objects.requireNonNull(intConsumer);
            C0275p3 c0275p3 = new C0275p3(intConsumer, 0);
            this.b.t0(this.d, c0275p3);
            this.i = true;
            return;
        }
        while (tryAdvance(intConsumer)) {
        }
    }
}
