package j$.util.stream;

import j$.util.C0184g;
import j$.util.Objects;
import j$.util.Spliterator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.LongConsumer;
import java.util.function.Predicate;

/* JADX INFO: renamed from: j$.util.stream.z1, reason: case insensitive filesystem */
public abstract class AbstractC0322z1 implements M3 {
    public static final C0203b1 a = new C0203b1();
    public static final Z0 b = new Z0();
    public static final C0198a1 c = new C0198a1();
    public static final Y0 d = new Y0();
    public static final int[] e = new int[0];
    public static final long[] f = new long[0];
    public static final double[] g = new double[0];

    public abstract void Z(Spliterator spliterator, InterfaceC0279q2 interfaceC0279q2);

    public abstract boolean a0(Spliterator spliterator, InterfaceC0279q2 interfaceC0279q2);

    public abstract J0 d0(Spliterator spliterator, boolean z, IntFunction intFunction);

    public abstract long e0(Spliterator spliterator);

    public abstract B0 q0(long j, IntFunction intFunction);

    @Override // j$.util.stream.M3
    public /* synthetic */ int s() {
        return 0;
    }

    public abstract U1 s0();

    public abstract InterfaceC0279q2 t0(Spliterator spliterator, InterfaceC0279q2 interfaceC0279q2);

    public abstract InterfaceC0279q2 u0(InterfaceC0279q2 interfaceC0279q2);

    public abstract Spliterator v0(Spliterator spliterator);

    public static j$.time.t k0(Function function) {
        j$.time.t tVar = new j$.time.t(6);
        tVar.b = function;
        return tVar;
    }

    public static Set j0(Set set) {
        EnumC0231h enumC0231h;
        java.util.stream.Collector.Characteristics characteristics;
        if (set == null || set.isEmpty()) {
            return set;
        }
        HashSet hashSet = new HashSet();
        Object next = set.iterator().next();
        if (next instanceof EnumC0231h) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                try {
                    EnumC0231h enumC0231h2 = (EnumC0231h) it.next();
                    if (enumC0231h2 == null) {
                        characteristics = null;
                    } else if (enumC0231h2 == EnumC0231h.CONCURRENT) {
                        characteristics = java.util.stream.Collector.Characteristics.CONCURRENT;
                    } else {
                        characteristics = enumC0231h2 == EnumC0231h.UNORDERED ? java.util.stream.Collector.Characteristics.UNORDERED : java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
                    }
                    hashSet.add(characteristics);
                } catch (ClassCastException e2) {
                    C0184g.a(e2, "java.util.stream.Collector.Characteristics");
                    throw null;
                }
            }
        } else {
            if (!(next instanceof java.util.stream.Collector.Characteristics)) {
                C0184g.a(next.getClass(), "java.util.stream.Collector.Characteristics");
                throw null;
            }
            Iterator it2 = set.iterator();
            while (it2.hasNext()) {
                try {
                    java.util.stream.Collector.Characteristics characteristics2 = (java.util.stream.Collector.Characteristics) it2.next();
                    if (characteristics2 == null) {
                        enumC0231h = null;
                    } else if (characteristics2 == java.util.stream.Collector.Characteristics.CONCURRENT) {
                        enumC0231h = EnumC0231h.CONCURRENT;
                    } else {
                        enumC0231h = characteristics2 == java.util.stream.Collector.Characteristics.UNORDERED ? EnumC0231h.UNORDERED : EnumC0231h.IDENTITY_FINISH;
                    }
                    hashSet.add(enumC0231h);
                } catch (ClassCastException e3) {
                    C0184g.a(e3, "java.util.stream.Collector.Characteristics");
                    throw null;
                }
            }
        }
        return hashSet;
    }

    public static j$.util.concurrent.s r0(EnumC0306w0 enumC0306w0, Predicate predicate) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(enumC0306w0);
        return new j$.util.concurrent.s(EnumC0220e3.REFERENCE, enumC0306w0, new C0277q0(enumC0306w0, predicate, 1));
    }

    public static AbstractC0208c1 c0(EnumC0220e3 enumC0220e3) {
        int i = K0.a[enumC0220e3.ordinal()];
        if (i == 1) {
            return a;
        }
        if (i == 2) {
            return b;
        }
        if (i == 3) {
            return c;
        }
        if (i == 4) {
            return d;
        }
        throw new IllegalStateException("Unknown shape " + enumC0220e3);
    }

    public static j$.util.concurrent.s o0(EnumC0306w0 enumC0306w0, IntPredicate intPredicate) {
        Objects.requireNonNull(intPredicate);
        Objects.requireNonNull(enumC0306w0);
        return new j$.util.concurrent.s(EnumC0220e3.INT_VALUE, enumC0306w0, new C0277q0(enumC0306w0, intPredicate, 0));
    }

    public static J0 S(J0 j0, long j, long j2, IntFunction intFunction) {
        if (j == 0 && j2 == j0.count()) {
            return j0;
        }
        Spliterator spliterator = j0.spliterator();
        long j3 = j2 - j;
        B0 b0T = T(j3, intFunction);
        b0T.h(j3);
        for (int i = 0; i < j && spliterator.tryAdvance(new C0217e0(3)); i++) {
        }
        if (j2 == j0.count()) {
            spliterator.forEachRemaining(b0T);
        } else {
            for (int i2 = 0; i2 < j3 && spliterator.tryAdvance(b0T); i2++) {
            }
        }
        b0T.end();
        return b0T.build();
    }

    public static L0 Y(EnumC0220e3 enumC0220e3, J0 j0, J0 j1) {
        int i = K0.a[enumC0220e3.ordinal()];
        if (i == 1) {
            return new U0(j0, j1);
        }
        if (i == 2) {
            return new R0((F0) j0, (F0) j1);
        }
        if (i == 3) {
            return new S0((H0) j0, (H0) j1);
        }
        if (i != 4) {
            throw new IllegalStateException("Unknown shape " + enumC0220e3);
        }
        return new Q0((D0) j0, (D0) j1);
    }

    public static j$.util.concurrent.s p0(EnumC0306w0 enumC0306w0) {
        Objects.requireNonNull(null);
        Objects.requireNonNull(enumC0306w0);
        return new j$.util.concurrent.s(EnumC0220e3.LONG_VALUE, enumC0306w0, new C0272p0(enumC0306w0, 0));
    }

    public static void G() {
        throw new IllegalStateException("called wrong accept method");
    }

    public static j$.util.concurrent.s n0(EnumC0306w0 enumC0306w0) {
        Objects.requireNonNull(null);
        Objects.requireNonNull(enumC0306w0);
        return new j$.util.concurrent.s(EnumC0220e3.DOUBLE_VALUE, enumC0306w0, new C0272p0(enumC0306w0, 1));
    }

    public static void H() {
        throw new IllegalStateException("called wrong accept method");
    }

    public static B0 T(long j, IntFunction intFunction) {
        if (j >= 0 && j < 2147483639) {
            return new C0213d1(j, intFunction);
        }
        return new C0302v1();
    }

    public static void z() {
        throw new IllegalStateException("called wrong accept method");
    }

    public static void C(InterfaceC0269o2 interfaceC0269o2, Integer num) {
        if (O3.a) {
            O3.a(interfaceC0269o2.getClass(), "{0} calling Sink.OfInt.accept(Integer)");
            throw null;
        }
        interfaceC0269o2.accept(num.intValue());
    }

    public static void E(InterfaceC0274p2 interfaceC0274p2, Long l) {
        if (O3.a) {
            O3.a(interfaceC0274p2.getClass(), "{0} calling Sink.OfLong.accept(Long)");
            throw null;
        }
        interfaceC0274p2.accept(l.longValue());
    }

    public static InterfaceC0321z0 l0(long j) {
        if (j < 0 || j >= 2147483639) {
            return new C0228g1();
        }
        return new C0223f1(j);
    }

    public static void A(InterfaceC0264n2 interfaceC0264n2, Double d2) {
        if (O3.a) {
            O3.a(interfaceC0264n2.getClass(), "{0} calling Sink.OfDouble.accept(Double)");
            throw null;
        }
        interfaceC0264n2.accept(d2.doubleValue());
    }

    public static A0 m0(long j) {
        if (j < 0 || j >= 2147483639) {
            return new C0273p1();
        }
        return new C0268o1(j);
    }

    public static Object[] I(I0 i0, IntFunction intFunction) {
        if (O3.a) {
            O3.a(i0.getClass(), "{0} calling Node.OfPrimitive.asArray");
            throw null;
        }
        if (i0.count() >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        Object[] objArr = (Object[]) intFunction.apply((int) i0.count());
        i0.f(objArr, 0);
        return objArr;
    }

    public static InterfaceC0316y0 b0(long j) {
        if (j < 0 || j >= 2147483639) {
            return new X0();
        }
        return new W0(j);
    }

    public static J0 U(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, boolean z, IntFunction intFunction) {
        long jE0 = abstractC0322z1.e0(spliterator);
        if (jE0 < 0 || !spliterator.hasCharacteristics(16384)) {
            N n = new N();
            n.a = intFunction;
            J0 j0 = (J0) new O0(abstractC0322z1, spliterator, n, new C0217e0(11), 3).invoke();
            return z ? f0(j0, intFunction) : j0;
        }
        if (jE0 >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        Object[] objArr = (Object[]) intFunction.apply((int) jE0);
        new C0292t1(spliterator, abstractC0322z1, objArr).invoke();
        return new M0(objArr);
    }

    public static void N(F0 f0, Consumer consumer) {
        if (consumer instanceof IntConsumer) {
            f0.d((IntConsumer) consumer);
        } else {
            if (O3.a) {
                O3.a(f0.getClass(), "{0} calling Node.OfInt.forEachRemaining(Consumer)");
                throw null;
            }
            ((Spliterator.OfInt) f0.spliterator()).forEachRemaining(consumer);
        }
    }

    public static void K(F0 f0, Integer[] numArr, int i) {
        if (O3.a) {
            O3.a(f0.getClass(), "{0} calling Node.OfInt.copyInto(Integer[], int)");
            throw null;
        }
        int[] iArr = (int[]) f0.b();
        for (int i2 = 0; i2 < iArr.length; i2++) {
            numArr[i + i2] = Integer.valueOf(iArr[i2]);
        }
    }

    public static F0 Q(F0 f0, long j, long j2) {
        if (j == 0 && j2 == f0.count()) {
            return f0;
        }
        long j3 = j2 - j;
        Spliterator.OfInt ofInt = (Spliterator.OfInt) f0.spliterator();
        InterfaceC0321z0 interfaceC0321z0L0 = l0(j3);
        interfaceC0321z0L0.h(j3);
        for (int i = 0; i < j && ofInt.tryAdvance((IntConsumer) new E0(0)); i++) {
        }
        if (j2 == f0.count()) {
            ofInt.forEachRemaining((IntConsumer) interfaceC0321z0L0);
        } else {
            for (int i2 = 0; i2 < j3 && ofInt.tryAdvance((IntConsumer) interfaceC0321z0L0); i2++) {
            }
        }
        interfaceC0321z0L0.end();
        return interfaceC0321z0L0.build();
    }

    public static F0 W(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, boolean z) {
        long jE0 = abstractC0322z1.e0(spliterator);
        if (jE0 < 0 || !spliterator.hasCharacteristics(16384)) {
            F0 f0 = (F0) new O0(abstractC0322z1, spliterator, new C0217e0(7), new C0217e0(8), 1).invoke();
            return z ? h0(f0) : f0;
        }
        if (jE0 >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        int[] iArr = new int[(int) jE0];
        new C0282r1(spliterator, abstractC0322z1, iArr).invoke();
        return new C0218e1(iArr);
    }

    public static H0 X(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, boolean z) {
        long jE0 = abstractC0322z1.e0(spliterator);
        if (jE0 < 0 || !spliterator.hasCharacteristics(16384)) {
            H0 h0 = (H0) new O0(abstractC0322z1, spliterator, new C0217e0(9), new C0217e0(10), 2).invoke();
            return z ? i0(h0) : h0;
        }
        if (jE0 >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        long[] jArr = new long[(int) jE0];
        new C0287s1(spliterator, abstractC0322z1, jArr).invoke();
        return new C0263n1(jArr);
    }

    public static void O(H0 h0, Consumer consumer) {
        if (consumer instanceof LongConsumer) {
            h0.d((LongConsumer) consumer);
        } else {
            if (O3.a) {
                O3.a(h0.getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
                throw null;
            }
            ((j$.util.Z) h0.spliterator()).forEachRemaining(consumer);
        }
    }

    public static void L(H0 h0, Long[] lArr, int i) {
        if (O3.a) {
            O3.a(h0.getClass(), "{0} calling Node.OfInt.copyInto(Long[], int)");
            throw null;
        }
        long[] jArr = (long[]) h0.b();
        for (int i2 = 0; i2 < jArr.length; i2++) {
            lArr[i + i2] = Long.valueOf(jArr[i2]);
        }
    }

    public static H0 R(H0 h0, long j, long j2) {
        if (j == 0 && j2 == h0.count()) {
            return h0;
        }
        long j3 = j2 - j;
        j$.util.Z z = (j$.util.Z) h0.spliterator();
        A0 a0M0 = m0(j3);
        a0M0.h(j3);
        for (int i = 0; i < j && z.tryAdvance((LongConsumer) new G0(0)); i++) {
        }
        if (j2 == h0.count()) {
            z.forEachRemaining((LongConsumer) a0M0);
        } else {
            for (int i2 = 0; i2 < j3 && z.tryAdvance((LongConsumer) a0M0); i2++) {
            }
        }
        a0M0.end();
        return a0M0.build();
    }

    public static D0 V(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, boolean z) {
        long jE0 = abstractC0322z1.e0(spliterator);
        if (jE0 < 0 || !spliterator.hasCharacteristics(16384)) {
            D0 d0 = (D0) new O0(abstractC0322z1, spliterator, new C0217e0(5), new C0217e0(6), 0).invoke();
            return z ? g0(d0) : d0;
        }
        if (jE0 >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        double[] dArr = new double[(int) jE0];
        new C0278q1(spliterator, abstractC0322z1, dArr).invoke();
        return new V0(dArr);
    }

    public static J0 f0(J0 j0, IntFunction intFunction) {
        if (j0.i() <= 0) {
            return j0;
        }
        long jCount = j0.count();
        if (jCount >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        Object[] objArr = (Object[]) intFunction.apply((int) jCount);
        new C0317y1(j0, objArr, 1).invoke();
        return new M0(objArr);
    }

    public static void M(D0 d0, Consumer consumer) {
        if (consumer instanceof DoubleConsumer) {
            d0.d((DoubleConsumer) consumer);
        } else {
            if (O3.a) {
                O3.a(d0.getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
                throw null;
            }
            ((j$.util.U) d0.spliterator()).forEachRemaining(consumer);
        }
    }

    public static F0 h0(F0 f0) {
        if (f0.i() <= 0) {
            return f0;
        }
        long jCount = f0.count();
        if (jCount >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        int[] iArr = new int[(int) jCount];
        new C0312x1(f0, iArr, 0).invoke();
        return new C0218e1(iArr);
    }

    public static void J(D0 d0, Double[] dArr, int i) {
        if (O3.a) {
            O3.a(d0.getClass(), "{0} calling Node.OfDouble.copyInto(Double[], int)");
            throw null;
        }
        double[] dArr2 = (double[]) d0.b();
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            dArr[i + i2] = Double.valueOf(dArr2[i2]);
        }
    }

    public static D0 P(D0 d0, long j, long j2) {
        if (j == 0 && j2 == d0.count()) {
            return d0;
        }
        long j3 = j2 - j;
        j$.util.U u = (j$.util.U) d0.spliterator();
        InterfaceC0316y0 interfaceC0316y0B0 = b0(j3);
        interfaceC0316y0B0.h(j3);
        for (int i = 0; i < j && u.tryAdvance((DoubleConsumer) new C0(0)); i++) {
        }
        if (j2 == d0.count()) {
            u.forEachRemaining((DoubleConsumer) interfaceC0316y0B0);
        } else {
            for (int i2 = 0; i2 < j3 && u.tryAdvance((DoubleConsumer) interfaceC0316y0B0); i2++) {
            }
        }
        interfaceC0316y0B0.end();
        return interfaceC0316y0B0.build();
    }

    public static H0 i0(H0 h0) {
        if (h0.i() <= 0) {
            return h0;
        }
        long jCount = h0.count();
        if (jCount >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        long[] jArr = new long[(int) jCount];
        new C0307w1(h0, jArr, 0).invoke();
        return new C0263n1(jArr);
    }

    public static D0 g0(D0 d0) {
        if (d0.i() <= 0) {
            return d0;
        }
        long jCount = d0.count();
        if (jCount >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        double[] dArr = new double[(int) jCount];
        new C0307w1(d0, dArr, 0).invoke();
        return new V0(dArr);
    }

    @Override // j$.util.stream.M3
    public Object f(AbstractC0201b abstractC0201b, Spliterator spliterator) {
        U1 u1S0 = s0();
        abstractC0201b.t0(spliterator, u1S0);
        return u1S0.get();
    }

    @Override // j$.util.stream.M3
    public Object i(AbstractC0322z1 abstractC0322z1, Spliterator spliterator) {
        return ((U1) new C0204b2(this, abstractC0322z1, spliterator).invoke()).get();
    }
}
