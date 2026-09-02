package j$.util.stream;

import j$.util.Spliterator;
import java.util.Deque;
import java.util.function.Consumer;

/* JADX INFO: renamed from: j$.util.stream.l1, reason: case insensitive filesystem */
public final class C0253l1 extends AbstractC0258m1 {
    @Override // j$.util.Spliterator
    public final boolean tryAdvance(Consumer consumer) {
        J0 j0A;
        if (!c()) {
            return false;
        }
        boolean zTryAdvance = this.d.tryAdvance(consumer);
        if (!zTryAdvance) {
            if (this.c == null && (j0A = AbstractC0258m1.a(this.e)) != null) {
                Spliterator spliterator = j0A.spliterator();
                this.d = spliterator;
                return spliterator.tryAdvance(consumer);
            }
            this.a = null;
        }
        return zTryAdvance;
    }

    @Override // j$.util.Spliterator
    public final void forEachRemaining(Consumer consumer) {
        if (this.a == null) {
            return;
        }
        if (this.d == null) {
            Spliterator spliterator = this.c;
            if (spliterator == null) {
                Deque dequeB = b();
                while (true) {
                    J0 j0A = AbstractC0258m1.a(dequeB);
                    if (j0A != null) {
                        j0A.forEach(consumer);
                    } else {
                        this.a = null;
                        return;
                    }
                }
            } else {
                spliterator.forEachRemaining(consumer);
            }
        } else {
            while (tryAdvance(consumer)) {
            }
        }
    }
}
