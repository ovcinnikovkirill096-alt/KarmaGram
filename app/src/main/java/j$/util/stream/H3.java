package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;
import java.util.function.Consumer;

public final class H3 extends AbstractC0225f3 {
    @Override // j$.util.stream.AbstractC0225f3
    public final AbstractC0225f3 e(Spliterator spliterator) {
        return new H3(this.b, spliterator, this.a);
    }

    @Override // j$.util.stream.AbstractC0225f3
    public final void d() {
        C0205b3 c0205b3 = new C0205b3();
        this.h = c0205b3;
        Objects.requireNonNull(c0205b3);
        this.e = this.b.u0(new G3(c0205b3, 0));
        this.f = new j$.time.t(14, this);
    }

    @Override // j$.util.Spliterator
    public final boolean tryAdvance(Consumer consumer) {
        Object obj;
        Objects.requireNonNull(consumer);
        boolean zA = a();
        if (!zA) {
            return zA;
        }
        C0205b3 c0205b3 = (C0205b3) this.h;
        long j = this.g;
        if (c0205b3.c != 0) {
            if (j >= c0205b3.count()) {
                throw new IndexOutOfBoundsException(Long.toString(j));
            }
            for (int i = 0; i <= c0205b3.c; i++) {
                long j2 = c0205b3.d[i];
                Object[] objArr = c0205b3.f[i];
                if (j < ((long) objArr.length) + j2) {
                    obj = objArr[(int) (j - j2)];
                }
            }
            throw new IndexOutOfBoundsException(Long.toString(j));
        }
        if (j < c0205b3.b) {
            obj = c0205b3.e[(int) j];
        } else {
            throw new IndexOutOfBoundsException(Long.toString(j));
        }
        consumer.v(obj);
        return zA;
    }

    @Override // j$.util.Spliterator
    public final void forEachRemaining(Consumer consumer) {
        if (this.h == null && !this.i) {
            Objects.requireNonNull(consumer);
            c();
            Objects.requireNonNull(consumer);
            G3 g3 = new G3(consumer, 1);
            this.b.t0(this.d, g3);
            this.i = true;
            return;
        }
        while (tryAdvance(consumer)) {
        }
    }
}
