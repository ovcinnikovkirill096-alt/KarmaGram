package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public interface J0 {
    J0 a(int i);

    long count();

    J0 e(long j, long j2, IntFunction intFunction);

    void f(Object[] objArr, int i);

    void forEach(Consumer consumer);

    Object[] g(IntFunction intFunction);

    int i();

    Spliterator spliterator();
}
