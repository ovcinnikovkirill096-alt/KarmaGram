package j$.util.stream;

import j$.util.C0328w;
import j$.util.Objects;
import j$.util.Spliterator;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.IntFunction;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;

public abstract class C extends AbstractC0201b implements F {
    @Override // j$.util.stream.F
    public final j$.util.B findAny() {
        return (j$.util.B) w0(H.d);
    }

    @Override // j$.util.stream.F
    public final j$.util.B findFirst() {
        return (j$.util.B) w0(H.c);
    }

    @Override // j$.util.stream.F
    public final F sorted() {
        return new J2(this, EnumC0215d3.q | EnumC0215d3.o, 0);
    }

    public static j$.util.U I0(Spliterator spliterator) {
        if (spliterator instanceof j$.util.U) {
            return (j$.util.U) spliterator;
        }
        if (O3.a) {
            O3.a(AbstractC0201b.class, "using DoubleStream.adapt(Spliterator<Double> s)");
            throw null;
        }
        throw new UnsupportedOperationException("DoubleStream.adapt(Spliterator<Double> s)");
    }

    @Override // j$.util.stream.F
    public void forEach(DoubleConsumer doubleConsumer) {
        Objects.requireNonNull(doubleConsumer);
        w0(new O(doubleConsumer, false));
    }

    @Override // j$.util.stream.F
    public void forEachOrdered(DoubleConsumer doubleConsumer) {
        Objects.requireNonNull(doubleConsumer);
        w0(new O(doubleConsumer, true));
    }

    @Override // j$.util.stream.AbstractC0201b
    public final EnumC0220e3 A0() {
        return EnumC0220e3.DOUBLE_VALUE;
    }

    @Override // j$.util.stream.AbstractC0201b
    public final J0 y0(AbstractC0201b abstractC0201b, Spliterator spliterator, boolean z, IntFunction intFunction) {
        return AbstractC0322z1.V(abstractC0201b, spliterator, z);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final Spliterator H0(AbstractC0201b abstractC0201b, Supplier supplier, boolean z) {
        return new C0270o3(abstractC0201b, supplier, z);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final boolean z0(Spliterator spliterator, InterfaceC0279q2 interfaceC0279q2) {
        DoubleConsumer d;
        boolean zM;
        j$.util.U uI0 = I0(spliterator);
        if (interfaceC0279q2 instanceof DoubleConsumer) {
            d = (DoubleConsumer) interfaceC0279q2;
        } else {
            if (O3.a) {
                O3.a(AbstractC0201b.class, "using DoubleStream.adapt(Sink<Double> s)");
                throw null;
            }
            Objects.requireNonNull(interfaceC0279q2);
            d = new j$.util.D(interfaceC0279q2, 1);
        }
        do {
            zM = interfaceC0279q2.m();
            if (zM) {
                break;
            }
        } while (uI0.tryAdvance(d));
        return zM;
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final B0 q0(long j, IntFunction intFunction) {
        return AbstractC0322z1.b0(j);
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final j$.util.G iterator() {
        j$.util.U uSpliterator = spliterator();
        Objects.requireNonNull(uSpliterator);
        return new j$.util.h0(uSpliterator);
    }

    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream
    public final j$.util.U spliterator() {
        return I0(super.spliterator());
    }

    @Override // j$.util.stream.F
    public final Stream boxed() {
        return new C0290t(this, 0, new C0276q(3), 0);
    }

    @Override // j$.util.stream.F
    public final F e() {
        Objects.requireNonNull(null);
        return new C0295u(this, EnumC0215d3.p | EnumC0215d3.n, 0);
    }

    @Override // j$.util.stream.F
    public final Stream mapToObj(DoubleFunction doubleFunction) {
        Objects.requireNonNull(doubleFunction);
        return new C0290t(this, EnumC0215d3.p | EnumC0215d3.n, doubleFunction, 0);
    }

    @Override // j$.util.stream.F
    public final IntStream w() {
        Objects.requireNonNull(null);
        return new C0300v(this, EnumC0215d3.p | EnumC0215d3.n, 0);
    }

    @Override // j$.util.stream.F
    public final LongStream r() {
        Objects.requireNonNull(null);
        return new C0305w(this, EnumC0215d3.p | EnumC0215d3.n, 0);
    }

    @Override // j$.util.stream.F
    public final F a(j$.time.t tVar) {
        Objects.requireNonNull(tVar);
        return new C0315y(this, EnumC0215d3.p | EnumC0215d3.n | EnumC0215d3.t, tVar, 0);
    }

    @Override // j$.util.stream.F
    public final F c() {
        Objects.requireNonNull(null);
        return new C0295u(this, EnumC0215d3.t, 2);
    }

    @Override // j$.util.stream.F
    public final F peek(DoubleConsumer doubleConsumer) {
        Objects.requireNonNull(doubleConsumer);
        return new C0315y(this, doubleConsumer);
    }

    @Override // j$.util.stream.F
    public final F limit(long j) {
        if (j < 0) {
            throw new IllegalArgumentException(Long.toString(j));
        }
        return B2.e(this, 0L, j);
    }

    @Override // j$.util.stream.F
    public final F skip(long j) {
        if (j >= 0) {
            return j == 0 ? this : B2.e(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.F
    public final F b() {
        int i = k4.a;
        Objects.requireNonNull(null);
        return new J2(this, k4.a, 1);
    }

    @Override // j$.util.stream.F
    public final F d() {
        int i = k4.a;
        Objects.requireNonNull(null);
        return new J2(this, k4.b, 2);
    }

    @Override // j$.util.stream.F
    public final F distinct() {
        return ((AbstractC0239i2) boxed()).distinct().mapToDouble(new C0276q(4));
    }

    @Override // j$.util.stream.F
    public final double sum() {
        double[] dArr = (double[]) collect(new C0246k(8), new C0276q(7), new j$.time.e(27));
        Set set = Collectors.a;
        double d = dArr[0] + dArr[1];
        double d2 = dArr[dArr.length - 1];
        return (Double.isNaN(d) && Double.isInfinite(d2)) ? d2 : d;
    }

    @Override // j$.util.stream.F
    public final j$.util.B min() {
        return reduce(new j$.time.e(28));
    }

    @Override // j$.util.stream.F
    public final j$.util.B max() {
        return reduce(new C0276q(6));
    }

    @Override // j$.util.stream.F
    public final j$.util.B average() {
        double[] dArr = (double[]) collect(new C0246k(7), new j$.time.e(29), new C0276q(0));
        if (dArr[2] <= 0.0d) {
            return j$.util.B.c;
        }
        Set set = Collectors.a;
        double d = dArr[0] + dArr[1];
        double d2 = dArr[dArr.length - 1];
        if (Double.isNaN(d) && Double.isInfinite(d2)) {
            d = d2;
        }
        return new j$.util.B(d / dArr[2]);
    }

    @Override // j$.util.stream.F
    public final C0328w summaryStatistics() {
        return (C0328w) collect(new C0246k(0), new C0276q(1), new C0276q(2));
    }

    @Override // j$.util.stream.F
    public final Object collect(Supplier supplier, ObjDoubleConsumer objDoubleConsumer, BiConsumer biConsumer) {
        Objects.requireNonNull(biConsumer);
        r rVar = new r(biConsumer, 0);
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(objDoubleConsumer);
        Objects.requireNonNull(rVar);
        return w0(new E1(EnumC0220e3.DOUBLE_VALUE, rVar, objDoubleConsumer, supplier, 1));
    }

    @Override // j$.util.stream.F
    public final boolean l() {
        return ((Boolean) w0(AbstractC0322z1.n0(EnumC0306w0.ANY))).booleanValue();
    }

    @Override // j$.util.stream.F
    public final boolean q() {
        return ((Boolean) w0(AbstractC0322z1.n0(EnumC0306w0.ALL))).booleanValue();
    }

    @Override // j$.util.stream.F
    public final boolean y() {
        return ((Boolean) w0(AbstractC0322z1.n0(EnumC0306w0.NONE))).booleanValue();
    }

    @Override // j$.util.stream.F
    public final double[] toArray() {
        return (double[]) AbstractC0322z1.g0((D0) x0(new C0276q(5))).b();
    }

    @Override // j$.util.stream.F
    public final double reduce(double d, DoubleBinaryOperator doubleBinaryOperator) {
        Objects.requireNonNull(doubleBinaryOperator);
        return ((Double) w0(new I1(EnumC0220e3.DOUBLE_VALUE, doubleBinaryOperator, d))).doubleValue();
    }

    @Override // j$.util.stream.F
    public final j$.util.B reduce(DoubleBinaryOperator doubleBinaryOperator) {
        Objects.requireNonNull(doubleBinaryOperator);
        return (j$.util.B) w0(new C1(EnumC0220e3.DOUBLE_VALUE, doubleBinaryOperator, 1));
    }

    @Override // j$.util.stream.F
    public final long count() {
        return ((Long) w0(new G1(1))).longValue();
    }
}
