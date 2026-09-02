package j$.util.stream;

import j$.util.C0331z;
import j$.util.Objects;
import j$.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;

/* JADX INFO: renamed from: j$.util.stream.m0, reason: case insensitive filesystem */
public abstract class AbstractC0257m0 extends AbstractC0201b implements LongStream {
    @Override // j$.util.stream.LongStream
    public final j$.util.C findAny() {
        return (j$.util.C) w0(J.d);
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.C findFirst() {
        return (j$.util.C) w0(J.c);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream sorted() {
        return new L2(this, EnumC0215d3.q | EnumC0215d3.o, 0);
    }

    public void forEach(LongConsumer longConsumer) {
        Objects.requireNonNull(longConsumer);
        w0(new Q(longConsumer, false));
    }

    public void forEachOrdered(LongConsumer longConsumer) {
        Objects.requireNonNull(longConsumer);
        w0(new Q(longConsumer, true));
    }

    public static j$.util.Z I0(Spliterator spliterator) {
        if (spliterator instanceof j$.util.Z) {
            return (j$.util.Z) spliterator;
        }
        if (O3.a) {
            O3.a(AbstractC0201b.class, "using LongStream.adapt(Spliterator<Long> s)");
            throw null;
        }
        throw new UnsupportedOperationException("LongStream.adapt(Spliterator<Long> s)");
    }

    @Override // j$.util.stream.AbstractC0201b
    public final EnumC0220e3 A0() {
        return EnumC0220e3.LONG_VALUE;
    }

    @Override // j$.util.stream.AbstractC0201b
    public final J0 y0(AbstractC0201b abstractC0201b, Spliterator spliterator, boolean z, IntFunction intFunction) {
        return AbstractC0322z1.X(abstractC0201b, spliterator, z);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final Spliterator H0(AbstractC0201b abstractC0201b, Supplier supplier, boolean z) {
        return new C0289s3(abstractC0201b, supplier, z);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final boolean z0(Spliterator spliterator, InterfaceC0279q2 interfaceC0279q2) {
        LongConsumer l;
        boolean zM;
        j$.util.Z zI0 = I0(spliterator);
        if (interfaceC0279q2 instanceof LongConsumer) {
            l = (LongConsumer) interfaceC0279q2;
        } else {
            if (O3.a) {
                O3.a(AbstractC0201b.class, "using LongStream.adapt(Sink<Long> s)");
                throw null;
            }
            Objects.requireNonNull(interfaceC0279q2);
            l = new j$.util.L(interfaceC0279q2, 1);
        }
        do {
            zM = interfaceC0279q2.m();
            if (zM) {
                break;
            }
        } while (zI0.tryAdvance(l));
        return zM;
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final B0 q0(long j, IntFunction intFunction) {
        return AbstractC0322z1.m0(j);
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final j$.util.O iterator() {
        j$.util.Z zSpliterator = spliterator();
        Objects.requireNonNull(zSpliterator);
        return new j$.util.g0(zSpliterator);
    }

    @Override // j$.util.stream.AbstractC0201b, j$.util.stream.BaseStream
    public final j$.util.Z spliterator() {
        return I0(super.spliterator());
    }

    @Override // j$.util.stream.LongStream
    public final F asDoubleStream() {
        return new C0295u(this, EnumC0215d3.n, 5);
    }

    @Override // j$.util.stream.LongStream
    public final Stream boxed() {
        return new C0290t(this, 0, new C0276q(28), 2);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream e() {
        Objects.requireNonNull(null);
        return new C0305w(this, EnumC0215d3.p | EnumC0215d3.n, 3);
    }

    @Override // j$.util.stream.LongStream
    public final Stream mapToObj(LongFunction longFunction) {
        Objects.requireNonNull(longFunction);
        return new C0290t(this, EnumC0215d3.p | EnumC0215d3.n, longFunction, 2);
    }

    @Override // j$.util.stream.LongStream
    public final IntStream x() {
        Objects.requireNonNull(null);
        return new C0300v(this, EnumC0215d3.p | EnumC0215d3.n, 2);
    }

    @Override // j$.util.stream.LongStream
    public final F j() {
        Objects.requireNonNull(null);
        return new C0295u(this, EnumC0215d3.p | EnumC0215d3.n, 6);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream a(j$.time.t tVar) {
        Objects.requireNonNull(tVar);
        return new C0237i0(this, EnumC0215d3.p | EnumC0215d3.n | EnumC0215d3.t, tVar, 0);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream c() {
        Objects.requireNonNull(null);
        return new C0305w(this, EnumC0215d3.t, 5);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream peek(LongConsumer longConsumer) {
        Objects.requireNonNull(longConsumer);
        return new C0237i0(this, longConsumer);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream limit(long j) {
        if (j < 0) {
            throw new IllegalArgumentException(Long.toString(j));
        }
        return B2.g(this, 0L, j);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream skip(long j) {
        if (j >= 0) {
            return j == 0 ? this : B2.g(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.LongStream
    public final LongStream b() {
        int i = k4.a;
        Objects.requireNonNull(null);
        return new L2(this, k4.a, 1);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream d() {
        int i = k4.a;
        Objects.requireNonNull(null);
        return new L2(this, k4.b, 2);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream distinct() {
        return ((AbstractC0239i2) boxed()).distinct().mapToLong(new C0276q(25));
    }

    @Override // j$.util.stream.LongStream
    public final long sum() {
        return reduce(0L, new C0217e0(2));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.C min() {
        return reduce(new C0276q(24));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.C max() {
        return reduce(new C0217e0(1));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.B average() {
        long[] jArr = (long[]) collect(new C0246k(14), new C0276q(29), new C0217e0(0));
        long j = jArr[0];
        return j > 0 ? new j$.util.B(jArr[1] / j) : j$.util.B.c;
    }

    @Override // j$.util.stream.LongStream
    public final long reduce(long j, LongBinaryOperator longBinaryOperator) {
        Objects.requireNonNull(longBinaryOperator);
        return ((Long) w0(new A1(EnumC0220e3.LONG_VALUE, longBinaryOperator, j))).longValue();
    }

    @Override // j$.util.stream.LongStream
    public final C0331z summaryStatistics() {
        return (C0331z) collect(new C0246k(4), new C0276q(23), new C0276q(26));
    }

    @Override // j$.util.stream.LongStream
    public final Object collect(Supplier supplier, ObjLongConsumer objLongConsumer, BiConsumer biConsumer) {
        Objects.requireNonNull(biConsumer);
        r rVar = new r(biConsumer, 2);
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(objLongConsumer);
        Objects.requireNonNull(rVar);
        return w0(new E1(EnumC0220e3.LONG_VALUE, rVar, objLongConsumer, supplier, 0));
    }

    @Override // j$.util.stream.LongStream
    public final boolean v() {
        return ((Boolean) w0(AbstractC0322z1.p0(EnumC0306w0.ANY))).booleanValue();
    }

    @Override // j$.util.stream.LongStream
    public final boolean m() {
        return ((Boolean) w0(AbstractC0322z1.p0(EnumC0306w0.ALL))).booleanValue();
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.C reduce(LongBinaryOperator longBinaryOperator) {
        Objects.requireNonNull(longBinaryOperator);
        return (j$.util.C) w0(new C1(EnumC0220e3.LONG_VALUE, longBinaryOperator, 0));
    }

    @Override // j$.util.stream.LongStream
    public final boolean p() {
        return ((Boolean) w0(AbstractC0322z1.p0(EnumC0306w0.NONE))).booleanValue();
    }

    @Override // j$.util.stream.LongStream
    public final long[] toArray() {
        return (long[]) AbstractC0322z1.i0((H0) x0(new C0276q(27))).b();
    }

    @Override // j$.util.stream.LongStream
    public final long count() {
        return ((Long) w0(new G1(0))).longValue();
    }
}
