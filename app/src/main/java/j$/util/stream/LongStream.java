package j$.util.stream;

import j$.util.C0331z;
import java.util.function.BiConsumer;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;

public interface LongStream extends BaseStream<Long, LongStream> {
    LongStream a(j$.time.t tVar);

    F asDoubleStream();

    j$.util.B average();

    LongStream b();

    Stream boxed();

    LongStream c();

    Object collect(Supplier supplier, ObjLongConsumer objLongConsumer, BiConsumer biConsumer);

    long count();

    LongStream d();

    LongStream distinct();

    LongStream e();

    j$.util.C findAny();

    j$.util.C findFirst();

    void forEach(LongConsumer longConsumer);

    void forEachOrdered(LongConsumer longConsumer);

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    j$.util.O iterator();

    F j();

    LongStream limit(long j);

    boolean m();

    <U> Stream<U> mapToObj(LongFunction<? extends U> longFunction);

    j$.util.C max();

    j$.util.C min();

    boolean p();

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    LongStream parallel();

    LongStream peek(LongConsumer longConsumer);

    long reduce(long j, LongBinaryOperator longBinaryOperator);

    j$.util.C reduce(LongBinaryOperator longBinaryOperator);

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    LongStream sequential();

    LongStream skip(long j);

    LongStream sorted();

    @Override // j$.util.stream.BaseStream
    j$.util.Z spliterator();

    long sum();

    C0331z summaryStatistics();

    long[] toArray();

    boolean v();

    IntStream x();
}
