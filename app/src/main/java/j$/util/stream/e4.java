package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.IntConsumer;

public final class e4 extends g4 {
    @Override // j$.util.stream.j4
    public final Spliterator b(Spliterator spliterator) {
        return new e4((Spliterator.OfInt) spliterator, this);
    }

    @Override // j$.util.Spliterator.OfInt
    public final boolean tryAdvance(IntConsumer intConsumer) {
        boolean zTryAdvance;
        boolean z = this.c;
        Spliterator spliterator = this.a;
        if (z) {
            boolean z2 = false;
            this.c = false;
            while (true) {
                zTryAdvance = ((Spliterator.OfInt) spliterator).tryAdvance((IntConsumer) this);
                if (!zTryAdvance || !a() || !this.e.test(this.f)) {
                    break;
                }
                z2 = true;
            }
            if (zTryAdvance) {
                if (z2) {
                    this.b.set(true);
                }
                intConsumer.accept(this.f);
            }
            return zTryAdvance;
        }
        return ((Spliterator.OfInt) spliterator).tryAdvance(intConsumer);
    }
}
