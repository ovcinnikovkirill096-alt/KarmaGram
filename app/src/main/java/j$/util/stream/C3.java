package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;
import java.util.Comparator;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public abstract class C3 extends F3 implements j$.util.c0 {
    public abstract void d(Object obj);

    public abstract AbstractC0245j3 e(int i);

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

    @Override // j$.util.c0
    public final boolean tryAdvance(Object obj) {
        Objects.requireNonNull(obj);
        while (c() != E3.NO_MORE && ((j$.util.c0) this.a).tryAdvance(this)) {
            if (a(1L) == 1) {
                d(obj);
                return true;
            }
        }
        return false;
    }

    @Override // j$.util.c0
    public final void forEachRemaining(Object obj) {
        Objects.requireNonNull(obj);
        AbstractC0245j3 abstractC0245j3E = null;
        while (true) {
            E3 e3C = c();
            if (e3C == E3.NO_MORE) {
                return;
            }
            E3 e3 = E3.MAYBE_MORE;
            Spliterator spliterator = this.a;
            if (e3C == e3) {
                int i = this.c;
                if (abstractC0245j3E == null) {
                    abstractC0245j3E = e(i);
                } else {
                    abstractC0245j3E.b = 0;
                }
                long j = 0;
                while (((j$.util.c0) spliterator).tryAdvance(abstractC0245j3E)) {
                    j++;
                    if (j >= i) {
                        break;
                    }
                }
                if (j == 0) {
                    return;
                } else {
                    abstractC0245j3E.a(obj, a(j));
                }
            } else {
                ((j$.util.c0) spliterator).forEachRemaining(obj);
                return;
            }
        }
    }

    public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
        forEachRemaining((Object) intConsumer);
    }

    public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
        return tryAdvance((Object) intConsumer);
    }

    public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
        forEachRemaining((Object) longConsumer);
    }

    public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
        return tryAdvance((Object) longConsumer);
    }

    public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
        forEachRemaining((Object) doubleConsumer);
    }

    public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
        return tryAdvance((Object) doubleConsumer);
    }
}
