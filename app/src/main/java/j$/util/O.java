package j$.util;

import java.util.function.Consumer;
import java.util.function.LongConsumer;

public interface O extends P {
    @Override // java.util.Iterator, j$.util.InterfaceC0330y
    void forEachRemaining(Consumer consumer);

    void forEachRemaining(LongConsumer longConsumer);

    @Override // java.util.Iterator
    Long next();

    long nextLong();
}
