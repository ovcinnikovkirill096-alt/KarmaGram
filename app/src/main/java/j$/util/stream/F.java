package j$.util.stream;

import j$.util.C0328w;
import java.util.function.BiConsumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;

public interface F extends BaseStream {
    F a(j$.time.t tVar);

    j$.util.B average();

    F b();

    Stream boxed();

    F c();

    Object collect(Supplier supplier, ObjDoubleConsumer objDoubleConsumer, BiConsumer biConsumer);

    long count();

    F d();

    F distinct();

    F e();

    j$.util.B findAny();

    j$.util.B findFirst();

    void forEach(DoubleConsumer doubleConsumer);

    void forEachOrdered(DoubleConsumer doubleConsumer);

    j$.util.G iterator();

    boolean l();

    F limit(long j);

    Stream mapToObj(DoubleFunction doubleFunction);

    j$.util.B max();

    j$.util.B min();

    F parallel();

    F peek(DoubleConsumer doubleConsumer);

    boolean q();

    LongStream r();

    double reduce(double d, DoubleBinaryOperator doubleBinaryOperator);

    j$.util.B reduce(DoubleBinaryOperator doubleBinaryOperator);

    F sequential();

    F skip(long j);

    F sorted();

    @Override // j$.util.stream.BaseStream
    j$.util.U spliterator();

    double sum();

    C0328w summaryStatistics();

    double[] toArray();

    IntStream w();

    boolean y();
}
