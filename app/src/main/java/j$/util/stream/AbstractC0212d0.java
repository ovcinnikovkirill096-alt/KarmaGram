package j$.util.stream;

import j$.util.C0329x;
import j$.util.Objects;
import j$.util.OptionalInt;
import j$.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;

/* JADX INFO: renamed from: j$.util.stream.d0, reason: case insensitive filesystem */
public abstract class AbstractC0212d0 extends AbstractC0201b implements IntStream {
    @Override // j$.util.stream.IntStream
    public final OptionalInt findAny() {
        return (OptionalInt) w0(I.d);
    }

    @Override // j$.util.stream.IntStream
    public final OptionalInt findFirst() {
        return (OptionalInt) w0(I.c);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream sorted() {
        return new K2(this, EnumC0215d3.q | EnumC0215d3.o);
    }

    public void forEach(IntConsumer intConsumer) {
        Objects.requireNonNull(intConsumer);
        w0(new P(intConsumer, false));
    }

    public void forEachOrdered(IntConsumer intConsumer) {
        Objects.requireNonNull(intConsumer);
        w0(new P(intConsumer, true));
    }

    public static Spliterator.OfInt I0(Spliterator spliterator) {
        if (spliterator instanceof Spliterator.OfInt) {
            return (Spliterator.OfInt) spliterator;
        }
        if (O3.a) {
            O3.a(AbstractC0201b.class, "using IntStream.adapt(Spliterator<Integer> s)");
            throw null;
        }
        throw new UnsupportedOperationException("IntStream.adapt(Spliterator<Integer> s)");
    }

    @Override // j$.util.stream.AbstractC0201b
    public final EnumC0220e3 A0() {
        return EnumC0220e3.INT_VALUE;
    }

    @Override // j$.util.stream.AbstractC0201b
    public final J0 y0(AbstractC0201b abstractC0201b, Spliterator spliterator, boolean z, IntFunction intFunction) {
        return AbstractC0322z1.W(abstractC0201b, spliterator, z);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final Spliterator H0(AbstractC0201b abstractC0201b, Supplier supplier, boolean z) {
        return new C0280q3(abstractC0201b, supplier, z);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final boolean z0(Spliterator spliterator, InterfaceC0279q2 interfaceC0279q2) {
        IntConsumer h;
        boolean zM;
        Spliterator.OfInt ofIntI0 = I0(spliterator);
        if (interfaceC0279q2 instanceof IntConsumer) {
            h = (IntConsumer) interfaceC0279q2;
        } else {
            if (O3.a) {
                O3.a(AbstractC0201b.class, "using IntStream.adapt(Sink<Integer> s)");
                throw null;
            }
            Objects.requireNonNull(interfaceC0279q2);
            h = new j$.util.H(interfaceC0279q2, 1);
        }
        do {
            zM = interfaceC0279q2.m();
            if (zM) {
                break;
            }
        } while (ofIntI0.tryAdvance(h));
        return zM;
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final B0 q0(long j, IntFunction intFunction) {
        return AbstractC0322z1.l0(j);
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final j$.util.K iterator() {
        Spliterator.OfInt ofIntSpliterator = spliterator();
        Objects.requireNonNull(ofIntSpliterator);
        return new j$.util.f0(ofIntSpliterator);
    }

    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream
    public final Spliterator.OfInt spliterator() {
        return I0(super.spliterator());
    }

    @Override // j$.util.stream.IntStream
    public final LongStream asLongStream() {
        return new C0305w(this, 0, 1);
    }

    @Override // j$.util.stream.IntStream
    public final F asDoubleStream() {
        return new C0295u(this, 0, 3);
    }

    @Override // j$.util.stream.IntStream
    public final Stream boxed() {
        return new C0290t(this, 0, new C0276q(15), 1);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream map(IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        return new X(this, EnumC0215d3.p | EnumC0215d3.n, intUnaryOperator, 1);
    }

    @Override // j$.util.stream.IntStream
    public final Stream mapToObj(IntFunction intFunction) {
        Objects.requireNonNull(intFunction);
        return new C0290t(this, EnumC0215d3.p | EnumC0215d3.n, intFunction, 1);
    }

    @Override // j$.util.stream.IntStream
    public final LongStream k() {
        Objects.requireNonNull(null);
        return new C0305w(this, EnumC0215d3.p | EnumC0215d3.n, 2);
    }

    @Override // j$.util.stream.IntStream
    public final F g() {
        Objects.requireNonNull(null);
        return new C0295u(this, EnumC0215d3.p | EnumC0215d3.n, 4);
    }

    @Override // j$.util.stream.IntStream
    public final int reduce(int i, IntBinaryOperator intBinaryOperator) {
        Objects.requireNonNull(intBinaryOperator);
        return ((Integer) w0(new P1(EnumC0220e3.INT_VALUE, intBinaryOperator, i))).intValue();
    }

    @Override // j$.util.stream.IntStream
    public final IntStream o(N n) {
        Objects.requireNonNull(n);
        return new X(this, EnumC0215d3.p | EnumC0215d3.n | EnumC0215d3.t, n, 2);
    }

    @Override // j$.util.stream.IntStream
    public final OptionalInt reduce(IntBinaryOperator intBinaryOperator) {
        Objects.requireNonNull(intBinaryOperator);
        return (OptionalInt) w0(new C1(EnumC0220e3.INT_VALUE, intBinaryOperator, 3));
    }

    @Override // j$.util.stream.IntStream
    public final IntStream filter(IntPredicate intPredicate) {
        Objects.requireNonNull(intPredicate);
        return new X(this, EnumC0215d3.t, intPredicate, 3);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream peek(IntConsumer intConsumer) {
        Objects.requireNonNull(intConsumer);
        return new X(this, intConsumer);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream limit(long j) {
        if (j < 0) {
            throw new IllegalArgumentException(Long.toString(j));
        }
        return B2.f(this, 0L, j);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream skip(long j) {
        if (j >= 0) {
            return j == 0 ? this : B2.f(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.IntStream
    public final IntStream takeWhile(IntPredicate intPredicate) {
        int i = k4.a;
        Objects.requireNonNull(intPredicate);
        return new S3(this, k4.a, intPredicate);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream dropWhile(IntPredicate intPredicate) {
        int i = k4.a;
        Objects.requireNonNull(intPredicate);
        return new U3(this, k4.b, intPredicate);
    }

    @Override // j$.util.stream.IntStream
    public final long count() {
        return ((Long) w0(new G1(3))).longValue();
    }

    @Override // j$.util.stream.IntStream
    public final IntStream distinct() {
        return ((AbstractC0239i2) boxed()).distinct().mapToInt(new C0276q(14));
    }

    @Override // j$.util.stream.IntStream
    public final int sum() {
        return reduce(0, new C0276q(19));
    }

    @Override // j$.util.stream.IntStream
    public final OptionalInt min() {
        return reduce(new C0276q(16));
    }

    @Override // j$.util.stream.IntStream
    public final OptionalInt max() {
        return reduce(new C0276q(20));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.B average() {
        long[] jArr = (long[]) collect(new C0246k(13), new C0276q(21), new C0276q(22));
        long j = jArr[0];
        return j > 0 ? new j$.util.B(jArr[1] / j) : j$.util.B.c;
    }

    @Override // j$.util.stream.IntStream
    public final C0329x summaryStatistics() {
        return (C0329x) collect(new C0246k(2), new C0276q(17), new C0276q(18));
    }

    @Override // j$.util.stream.IntStream
    public final Object collect(Supplier supplier, ObjIntConsumer objIntConsumer, BiConsumer biConsumer) {
        Objects.requireNonNull(biConsumer);
        r rVar = new r(biConsumer, 1);
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(objIntConsumer);
        Objects.requireNonNull(rVar);
        return w0(new E1(EnumC0220e3.INT_VALUE, rVar, objIntConsumer, supplier, 4));
    }

    @Override // j$.util.stream.IntStream
    public final boolean anyMatch(IntPredicate intPredicate) {
        return ((Boolean) w0(AbstractC0322z1.o0(EnumC0306w0.ANY, intPredicate))).booleanValue();
    }

    @Override // j$.util.stream.IntStream
    public final boolean allMatch(IntPredicate intPredicate) {
        return ((Boolean) w0(AbstractC0322z1.o0(EnumC0306w0.ALL, intPredicate))).booleanValue();
    }

    @Override // j$.util.stream.IntStream
    public final boolean noneMatch(IntPredicate intPredicate) {
        return ((Boolean) w0(AbstractC0322z1.o0(EnumC0306w0.NONE, intPredicate))).booleanValue();
    }

    @Override // j$.util.stream.IntStream
    public final int[] toArray() {
        return (int[]) AbstractC0322z1.h0((F0) x0(new C0276q(13))).b();
    }
}
