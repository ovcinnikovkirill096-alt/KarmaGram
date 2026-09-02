package j$.util;

import java.util.function.DoubleConsumer;

public interface U extends c0 {
    void forEachRemaining(DoubleConsumer doubleConsumer);

    boolean tryAdvance(DoubleConsumer doubleConsumer);

    @Override // j$.util.c0, j$.util.Spliterator
    U trySplit();
}
