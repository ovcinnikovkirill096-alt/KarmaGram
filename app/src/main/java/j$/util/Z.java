package j$.util;

import java.util.function.LongConsumer;

public interface Z extends c0 {
    void forEachRemaining(LongConsumer longConsumer);

    boolean tryAdvance(LongConsumer longConsumer);

    @Override // j$.util.c0, j$.util.Spliterator
    Z trySplit();
}
