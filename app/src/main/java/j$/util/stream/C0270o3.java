package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

/* JADX INFO: renamed from: j$.util.stream.o3, reason: case insensitive filesystem */
public final class C0270o3 extends AbstractC0225f3 implements j$.util.U {
    @Override // j$.util.Spliterator
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.com.android.tools.r8.a.j(this, consumer);
    }

    @Override // j$.util.Spliterator
    public final /* synthetic */ boolean tryAdvance(Consumer consumer) {
        return j$.com.android.tools.r8.a.z(this, consumer);
    }

    @Override // j$.util.stream.AbstractC0225f3
    public final AbstractC0225f3 e(Spliterator spliterator) {
        return new C0270o3(this.b, spliterator, this.a);
    }

    @Override // j$.util.stream.AbstractC0225f3
    public final void d() {
        U2 u2 = new U2();
        this.h = u2;
        Objects.requireNonNull(u2);
        this.e = this.b.u0(new C0265n3(u2, 1));
        this.f = new j$.time.t(11, this);
    }

    @Override // j$.util.stream.AbstractC0225f3, j$.util.Spliterator
    public final Spliterator trySplit() {
        return (j$.util.U) super.trySplit();
    }

    @Override // j$.util.stream.AbstractC0225f3, j$.util.Spliterator
    public final j$.util.U trySplit() {
        return (j$.util.U) super.trySplit();
    }

    @Override // j$.util.stream.AbstractC0225f3, j$.util.Spliterator
    public final j$.util.c0 trySplit() {
        return (j$.util.U) super.trySplit();
    }

    @Override // j$.util.c0
    public final boolean tryAdvance(DoubleConsumer doubleConsumer) {
        double d;
        Objects.requireNonNull(doubleConsumer);
        boolean zA = a();
        if (zA) {
            U2 u2 = (U2) this.h;
            long j = this.g;
            int iO = u2.o(j);
            if (u2.c == 0 && iO == 0) {
                d = ((double[]) u2.e)[(int) j];
            } else {
                d = ((double[][]) u2.f)[iO][(int) (j - u2.d[iO])];
            }
            doubleConsumer.accept(d);
        }
        return zA;
    }

    @Override // j$.util.c0
    public final void forEachRemaining(DoubleConsumer doubleConsumer) {
        if (this.h == null && !this.i) {
            Objects.requireNonNull(doubleConsumer);
            c();
            Objects.requireNonNull(doubleConsumer);
            C0265n3 c0265n3 = new C0265n3(doubleConsumer, 0);
            this.b.t0(this.d, c0265n3);
            this.i = true;
            return;
        }
        while (tryAdvance(doubleConsumer)) {
        }
    }
}
