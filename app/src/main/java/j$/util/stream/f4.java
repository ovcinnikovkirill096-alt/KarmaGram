package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.IntConsumer;

public final class f4 extends g4 {
    @Override // j$.util.stream.j4
    public final Spliterator b(Spliterator spliterator) {
        return new f4((Spliterator.OfInt) spliterator, this);
    }

    @Override // j$.util.Spliterator.OfInt
    public final boolean tryAdvance(IntConsumer intConsumer) {
        boolean zTest;
        if (this.c && a() && ((Spliterator.OfInt) this.a).tryAdvance((IntConsumer) this)) {
            zTest = this.e.test(this.f);
            if (zTest) {
                intConsumer.accept(this.f);
                return true;
            }
        } else {
            zTest = true;
        }
        this.c = false;
        if (!zTest) {
            this.b.set(true);
        }
        return false;
    }

    @Override // j$.util.stream.j4, j$.util.Spliterator
    public final Spliterator.OfInt trySplit() {
        if (this.b.get()) {
            return null;
        }
        return (Spliterator.OfInt) super.trySplit();
    }
}
