package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/* JADX INFO: renamed from: j$.util.stream.s3, reason: case insensitive filesystem */
public final class C0289s3 extends AbstractC0225f3 implements j$.util.Z {
    @Override // j$.util.Spliterator
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.com.android.tools.r8.a.l(this, consumer);
    }

    @Override // j$.util.Spliterator
    public final /* synthetic */ boolean tryAdvance(Consumer consumer) {
        return j$.com.android.tools.r8.a.B(this, consumer);
    }

    @Override // j$.util.stream.AbstractC0225f3
    public final AbstractC0225f3 e(Spliterator spliterator) {
        return new C0289s3(this.b, spliterator, this.a);
    }

    @Override // j$.util.stream.AbstractC0225f3
    public final void d() {
        Y2 y2 = new Y2();
        this.h = y2;
        Objects.requireNonNull(y2);
        this.e = this.b.u0(new C0284r3(y2, 1));
        this.f = new j$.time.t(13, this);
    }

    @Override // j$.util.stream.AbstractC0225f3, j$.util.Spliterator
    public final Spliterator trySplit() {
        return (j$.util.Z) super.trySplit();
    }

    @Override // j$.util.stream.AbstractC0225f3, j$.util.Spliterator
    public final j$.util.Z trySplit() {
        return (j$.util.Z) super.trySplit();
    }

    @Override // j$.util.stream.AbstractC0225f3, j$.util.Spliterator
    public final j$.util.c0 trySplit() {
        return (j$.util.Z) super.trySplit();
    }

    @Override // j$.util.c0
    public final boolean tryAdvance(LongConsumer longConsumer) {
        long j;
        Objects.requireNonNull(longConsumer);
        boolean zA = a();
        if (zA) {
            Y2 y2 = (Y2) this.h;
            long j2 = this.g;
            int iO = y2.o(j2);
            if (y2.c == 0 && iO == 0) {
                j = ((long[]) y2.e)[(int) j2];
            } else {
                j = ((long[][]) y2.f)[iO][(int) (j2 - y2.d[iO])];
            }
            longConsumer.accept(j);
        }
        return zA;
    }

    @Override // j$.util.c0
    public final void forEachRemaining(LongConsumer longConsumer) {
        if (this.h == null && !this.i) {
            Objects.requireNonNull(longConsumer);
            c();
            Objects.requireNonNull(longConsumer);
            C0284r3 c0284r3 = new C0284r3(longConsumer, 0);
            this.b.t0(this.d, c0284r3);
            this.i = true;
            return;
        }
        while (tryAdvance(longConsumer)) {
        }
    }
}
