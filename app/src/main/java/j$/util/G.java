package j$.util;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public interface G extends P {
    @Override // java.util.Iterator, j$.util.InterfaceC0330y
    void forEachRemaining(Consumer consumer);

    void forEachRemaining(DoubleConsumer doubleConsumer);

    @Override // java.util.Iterator
    Double next();

    double nextDouble();
}
