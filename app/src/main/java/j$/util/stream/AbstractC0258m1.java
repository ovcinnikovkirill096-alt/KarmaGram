package j$.util.stream;

import j$.util.Spliterator;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

/* JADX INFO: renamed from: j$.util.stream.m1, reason: case insensitive filesystem */
public abstract class AbstractC0258m1 implements Spliterator {
    public J0 a;
    public int b;
    public Spliterator c;
    public Spliterator d;
    public Deque e;

    @Override // j$.util.Spliterator
    public final int characteristics() {
        return 64;
    }

    @Override // j$.util.Spliterator
    public final /* synthetic */ long getExactSizeIfKnown() {
        return j$.com.android.tools.r8.a.o(this);
    }

    @Override // j$.util.Spliterator
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.com.android.tools.r8.a.q(this, i);
    }

    @Override // j$.util.Spliterator
    public final Comparator getComparator() {
        throw new IllegalStateException();
    }

    public AbstractC0258m1(J0 j0) {
        this.a = j0;
    }

    public final Deque b() {
        ArrayDeque arrayDeque = new ArrayDeque(8);
        int i = this.a.i();
        while (true) {
            i--;
            if (i < this.b) {
                return arrayDeque;
            }
            arrayDeque.addFirst(this.a.a(i));
        }
    }

    public static J0 a(Deque deque) {
        while (true) {
            ArrayDeque arrayDeque = (ArrayDeque) deque;
            J0 j0 = (J0) arrayDeque.pollFirst();
            if (j0 == null) {
                return null;
            }
            if (j0.i() != 0) {
                for (int i = j0.i() - 1; i >= 0; i--) {
                    arrayDeque.addFirst(j0.a(i));
                }
            } else if (j0.count() > 0) {
                return j0;
            }
        }
    }

    public final boolean c() {
        if (this.a == null) {
            return false;
        }
        if (this.d != null) {
            return true;
        }
        Spliterator spliterator = this.c;
        if (spliterator == null) {
            Deque dequeB = b();
            this.e = dequeB;
            J0 j0A = a(dequeB);
            if (j0A != null) {
                this.d = j0A.spliterator();
                return true;
            }
            this.a = null;
            return false;
        }
        this.d = spliterator;
        return true;
    }

    @Override // j$.util.Spliterator
    public final Spliterator trySplit() {
        J0 j0 = this.a;
        if (j0 == null || this.d != null) {
            return null;
        }
        Spliterator spliterator = this.c;
        if (spliterator != null) {
            return spliterator.trySplit();
        }
        if (this.b < j0.i() - 1) {
            J0 j1 = this.a;
            int i = this.b;
            this.b = i + 1;
            return j1.a(i).spliterator();
        }
        J0 j0A = this.a.a(this.b);
        this.a = j0A;
        if (j0A.i() == 0) {
            Spliterator spliterator2 = this.a.spliterator();
            this.c = spliterator2;
            return spliterator2.trySplit();
        }
        J0 j2 = this.a;
        this.b = 1;
        return j2.a(0).spliterator();
    }

    @Override // j$.util.Spliterator
    public final long estimateSize() {
        long jCount = 0;
        if (this.a == null) {
            return 0L;
        }
        Spliterator spliterator = this.c;
        if (spliterator != null) {
            return spliterator.estimateSize();
        }
        for (int i = this.b; i < this.a.i(); i++) {
            jCount += this.a.a(i).count();
        }
        return jCount;
    }

    @Override // j$.util.Spliterator
    public /* bridge */ /* synthetic */ j$.util.c0 trySplit() {
        return (j$.util.c0) trySplit();
    }

    @Override // j$.util.Spliterator
    public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
        return (Spliterator.OfInt) trySplit();
    }

    @Override // j$.util.Spliterator
    public /* bridge */ /* synthetic */ j$.util.Z trySplit() {
        return (j$.util.Z) trySplit();
    }

    @Override // j$.util.Spliterator
    public /* bridge */ /* synthetic */ j$.util.U trySplit() {
        return (j$.util.U) trySplit();
    }
}
