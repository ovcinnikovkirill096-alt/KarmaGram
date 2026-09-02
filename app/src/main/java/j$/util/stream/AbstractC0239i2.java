package j$.util.stream;

import j$.util.Objects;
import j$.util.Optional;
import j$.util.Spliterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/* JADX INFO: renamed from: j$.util.stream.i2, reason: case insensitive filesystem */
public abstract class AbstractC0239i2 extends AbstractC0201b implements Stream {
    @Override // j$.util.stream.Stream
    public final Stream sorted() {
        return new M2(this);
    }

    @Override // j$.util.stream.Stream
    public final Stream distinct() {
        return new C0271p(this, EnumC0215d3.m | EnumC0215d3.t);
    }

    @Override // j$.util.stream.Stream
    public final Optional min(Comparator comparator) {
        Objects.requireNonNull(comparator);
        return reduce(new j$.util.function.a(comparator, 1));
    }

    @Override // j$.util.stream.Stream
    public final Optional findAny() {
        return (Optional) w0(K.d);
    }

    @Override // j$.util.stream.Stream
    public final Optional findFirst() {
        return (Optional) w0(K.c);
    }

    @Override // j$.util.stream.Stream
    public final Stream sorted(Comparator comparator) {
        return new M2(this, comparator);
    }

    @Override // j$.util.stream.Stream
    public final Object reduce(Object obj, BiFunction biFunction, BinaryOperator binaryOperator) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(binaryOperator);
        return w0(new E1(EnumC0220e3.REFERENCE, binaryOperator, biFunction, obj, 2));
    }

    @Override // j$.util.stream.Stream
    public final Object reduce(Object obj, BinaryOperator binaryOperator) {
        Objects.requireNonNull(binaryOperator);
        Objects.requireNonNull(binaryOperator);
        return w0(new E1(EnumC0220e3.REFERENCE, binaryOperator, binaryOperator, obj, 2));
    }

    public void forEach(Consumer consumer) {
        Objects.requireNonNull(consumer);
        w0(new S(consumer, false));
    }

    public void forEachOrdered(Consumer consumer) {
        Objects.requireNonNull(consumer);
        w0(new S(consumer, true));
    }

    @Override // j$.util.stream.Stream
    public final Optional max(Comparator comparator) {
        Objects.requireNonNull(comparator);
        return reduce(new j$.util.function.a(comparator, 0));
    }

    @Override // j$.util.stream.AbstractC0201b
    public final EnumC0220e3 A0() {
        return EnumC0220e3.REFERENCE;
    }

    @Override // j$.util.stream.Stream
    public final Optional reduce(BinaryOperator binaryOperator) {
        Objects.requireNonNull(binaryOperator);
        return (Optional) w0(new C1(EnumC0220e3.REFERENCE, binaryOperator, 2));
    }

    @Override // j$.util.stream.AbstractC0201b
    public final J0 y0(AbstractC0201b abstractC0201b, Spliterator spliterator, boolean z, IntFunction intFunction) {
        return AbstractC0322z1.U(abstractC0201b, spliterator, z, intFunction);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final Spliterator H0(AbstractC0201b abstractC0201b, Supplier supplier, boolean z) {
        return new H3(abstractC0201b, supplier, z);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final boolean z0(Spliterator spliterator, InterfaceC0279q2 interfaceC0279q2) {
        boolean zM;
        do {
            zM = interfaceC0279q2.m();
            if (zM) {
                break;
            }
        } while (spliterator.tryAdvance(interfaceC0279q2));
        return zM;
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final B0 q0(long j, IntFunction intFunction) {
        return AbstractC0322z1.T(j, intFunction);
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final Iterator iterator() {
        Spliterator spliterator = spliterator();
        Objects.requireNonNull(spliterator);
        return new j$.util.e0(spliterator);
    }

    @Override // j$.util.stream.Stream
    public final Stream filter(Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new C0290t(this, EnumC0215d3.t, predicate, 4);
    }

    @Override // j$.util.stream.Stream
    public final Stream map(Function function) {
        Objects.requireNonNull(function);
        return new C0290t(this, EnumC0215d3.p | EnumC0215d3.n, function, 5);
    }

    @Override // j$.util.stream.Stream
    public final IntStream mapToInt(ToIntFunction toIntFunction) {
        Objects.requireNonNull(toIntFunction);
        return new X(this, EnumC0215d3.p | EnumC0215d3.n, toIntFunction, 4);
    }

    @Override // j$.util.stream.Stream
    public final Object collect(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(biConsumer);
        Objects.requireNonNull(biConsumer2);
        return w0(new E1(EnumC0220e3.REFERENCE, biConsumer2, biConsumer, supplier, 3));
    }

    @Override // j$.util.stream.Stream
    public final LongStream mapToLong(ToLongFunction toLongFunction) {
        Objects.requireNonNull(toLongFunction);
        return new C0237i0(this, EnumC0215d3.p | EnumC0215d3.n, toLongFunction, 3);
    }

    @Override // j$.util.stream.Stream
    public final F mapToDouble(ToDoubleFunction toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        return new C0315y(this, EnumC0215d3.p | EnumC0215d3.n, toDoubleFunction, 2);
    }

    @Override // j$.util.stream.Stream
    public final long count() {
        return ((Long) w0(new G1(2))).longValue();
    }

    @Override // j$.util.stream.Stream
    public final Stream a(j$.time.t tVar) {
        Objects.requireNonNull(tVar);
        return new C0290t(this, EnumC0215d3.p | EnumC0215d3.n | EnumC0215d3.t, tVar, 6);
    }

    @Override // j$.util.stream.Stream
    public final IntStream t(j$.time.t tVar) {
        Objects.requireNonNull(tVar);
        return new X(this, EnumC0215d3.p | EnumC0215d3.n | EnumC0215d3.t, tVar, 5);
    }

    @Override // j$.util.stream.Stream
    public final F u(j$.time.t tVar) {
        Objects.requireNonNull(tVar);
        return new C0315y(this, EnumC0215d3.p | EnumC0215d3.n | EnumC0215d3.t, tVar, 3);
    }

    @Override // j$.util.stream.Stream
    public final Object collect(Collector collector) {
        Collector collector2;
        Object objW0;
        if (!this.h.r || !collector.characteristics().contains(EnumC0231h.CONCURRENT) || (EnumC0215d3.ORDERED.n(this.m) && !collector.characteristics().contains(EnumC0231h.UNORDERED))) {
            Supplier supplier = ((Collector) Objects.requireNonNull(collector)).supplier();
            collector2 = collector;
            objW0 = w0(new L1(EnumC0220e3.REFERENCE, collector.combiner(), collector.accumulator(), supplier, collector2));
        } else {
            objW0 = collector.supplier().get();
            forEach(new j$.util.concurrent.s(6, collector.accumulator(), objW0));
            collector2 = collector;
        }
        return collector2.characteristics().contains(EnumC0231h.IDENTITY_FINISH) ? objW0 : collector2.finisher().apply(objW0);
    }

    @Override // j$.util.stream.Stream
    public final LongStream n(j$.time.t tVar) {
        Objects.requireNonNull(tVar);
        return new C0237i0(this, EnumC0215d3.p | EnumC0215d3.n | EnumC0215d3.t, tVar, 2);
    }

    @Override // j$.util.stream.Stream
    public final Stream peek(Consumer consumer) {
        Objects.requireNonNull(consumer);
        return new C0290t(this, consumer);
    }

    @Override // j$.util.stream.Stream
    public final Stream limit(long j) {
        if (j < 0) {
            throw new IllegalArgumentException(Long.toString(j));
        }
        return B2.h(this, 0L, j);
    }

    @Override // j$.util.stream.Stream
    public final Stream skip(long j) {
        if (j >= 0) {
            return j == 0 ? this : B2.h(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.Stream
    public final Stream takeWhile(Predicate predicate) {
        int i = k4.a;
        Objects.requireNonNull(predicate);
        return new P3(this, k4.a, predicate, 0);
    }

    @Override // j$.util.stream.Stream
    public final Stream dropWhile(Predicate predicate) {
        int i = k4.a;
        Objects.requireNonNull(predicate);
        return new P3(this, k4.b, predicate, 1);
    }

    @Override // j$.util.stream.Stream
    public final Object[] toArray(IntFunction intFunction) {
        return AbstractC0322z1.f0(x0(intFunction), intFunction).g(intFunction);
    }

    @Override // j$.util.stream.Stream
    public final Object[] toArray() {
        return toArray(new C0217e0(12));
    }

    @Override // j$.util.stream.Stream
    public final boolean anyMatch(Predicate predicate) {
        return ((Boolean) w0(AbstractC0322z1.r0(EnumC0306w0.ANY, predicate))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final boolean allMatch(Predicate predicate) {
        return ((Boolean) w0(AbstractC0322z1.r0(EnumC0306w0.ALL, predicate))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final boolean noneMatch(Predicate predicate) {
        return ((Boolean) w0(AbstractC0322z1.r0(EnumC0306w0.NONE, predicate))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final List toList() {
        return Collections.unmodifiableList(new ArrayList(Arrays.asList(toArray())));
    }
}
