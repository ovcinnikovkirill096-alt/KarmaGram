package j$.com.android.tools.r8;

import j$.time.ZoneId;
import j$.time.ZoneOffset;
import j$.time.c;
import j$.time.chrono.AbstractC0162a;
import j$.time.chrono.AbstractC0169h;
import j$.time.chrono.ChronoLocalDateTime;
import j$.time.chrono.ChronoZonedDateTime;
import j$.time.chrono.F;
import j$.time.chrono.InterfaceC0163b;
import j$.time.chrono.l;
import j$.time.chrono.z;
import j$.time.e;
import j$.time.temporal.m;
import j$.time.temporal.n;
import j$.time.temporal.r;
import j$.time.temporal.s;
import j$.time.temporal.u;
import j$.util.B;
import j$.util.C;
import j$.util.D;
import j$.util.H;
import j$.util.InterfaceC0330y;
import j$.util.L;
import j$.util.Objects;
import j$.util.OptionalInt;
import j$.util.Spliterator;
import j$.util.U;
import j$.util.Z;
import j$.util.concurrent.ConcurrentHashMap;
import j$.util.concurrent.k;
import j$.util.function.b;
import j$.util.function.d;
import j$.util.function.g;
import j$.util.t0;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import org.mvel2.asm.signature.SignatureVisitor;
import sun.misc.Unsafe;

public abstract /* synthetic */ class a {
    public static /* synthetic */ int O(long j) {
        int i = (int) j;
        if (j == i) {
            return i;
        }
        throw new ArithmeticException();
    }

    public static /* synthetic */ long P(long j, long j2) {
        long j3 = j + j2;
        if (((j2 ^ j) < 0) || ((j ^ j3) >= 0)) {
            return j3;
        }
        throw new ArithmeticException();
    }

    public static /* synthetic */ List Q(Object[] objArr) {
        ArrayList arrayList = new ArrayList(objArr.length);
        for (Object obj : objArr) {
            arrayList.add(Objects.requireNonNull(obj));
        }
        return Collections.unmodifiableList(arrayList);
    }

    public static /* synthetic */ Map.Entry R(Object obj, Object obj2) {
        return new AbstractMap.SimpleImmutableEntry(Objects.requireNonNull(obj), Objects.requireNonNull(obj2));
    }

    public static /* synthetic */ boolean S(Unsafe unsafe, Object obj, long j, k kVar) {
        while (true) {
            Unsafe unsafe2 = unsafe;
            Object obj2 = obj;
            long j2 = j;
            k kVar2 = kVar;
            if (unsafe2.compareAndSwapObject(obj2, j2, (Object) null, kVar2)) {
                return true;
            }
            if (unsafe2.getObject(obj2, j2) != null) {
                return false;
            }
            unsafe = unsafe2;
            obj = obj2;
            j = j2;
            kVar = kVar2;
        }
    }

    public static /* synthetic */ long T(long j, long j2) {
        long j3 = j % j2;
        if (j3 == 0) {
            return 0L;
        }
        return (((j ^ j2) >> 63) | 1) > 0 ? j3 : j3 + j2;
    }

    public static /* synthetic */ long U(long j, long j2) {
        long j3 = j / j2;
        return (j - (j2 * j3) != 0 && (((j ^ j2) >> 63) | 1) < 0) ? j3 - 1 : j3;
    }

    public static /* synthetic */ long V(long j, long j2) {
        int iNumberOfLeadingZeros = Long.numberOfLeadingZeros(~j2) + Long.numberOfLeadingZeros(j2) + Long.numberOfLeadingZeros(~j) + Long.numberOfLeadingZeros(j);
        if (iNumberOfLeadingZeros > 65) {
            return j * j2;
        }
        if (iNumberOfLeadingZeros >= 64) {
            if ((j2 != Long.MIN_VALUE) | (j >= 0)) {
                long j3 = j * j2;
                if (j == 0 || j3 / j == j2) {
                    return j3;
                }
            }
        }
        throw new ArithmeticException();
    }

    public static /* synthetic */ long W(long j, long j2) {
        long j3 = j - j2;
        if (((j2 ^ j) >= 0) || ((j ^ j3) >= 0)) {
            return j3;
        }
        throw new ArithmeticException();
    }

    public static Optional I(j$.util.Optional optional) {
        if (optional == null) {
            return null;
        }
        if (optional.isPresent()) {
            return Optional.of(optional.get());
        }
        return Optional.empty();
    }

    public static j$.util.Optional E(Optional optional) {
        if (optional == null) {
            return null;
        }
        if (optional.isPresent()) {
            return j$.util.Optional.of(optional.get());
        }
        return j$.util.Optional.empty();
    }

    public static B F(OptionalDouble optionalDouble) {
        if (optionalDouble == null) {
            return null;
        }
        if (!optionalDouble.isPresent()) {
            return B.c;
        }
        return new B(optionalDouble.getAsDouble());
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.util.function.b] */
    public static b b(final DoubleConsumer doubleConsumer, final DoubleConsumer doubleConsumer2) {
        Objects.requireNonNull(doubleConsumer2);
        return new DoubleConsumer() { // from class: j$.util.function.b
            public final /* synthetic */ DoubleConsumer andThen(DoubleConsumer doubleConsumer3) {
                return j$.com.android.tools.r8.a.b(this, doubleConsumer3);
            }

            @Override // java.util.function.DoubleConsumer
            public final void accept(double d) {
                doubleConsumer.accept(d);
                doubleConsumer2.accept(d);
            }
        };
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.util.function.d] */
    public static d c(final IntConsumer intConsumer, final IntConsumer intConsumer2) {
        Objects.requireNonNull(intConsumer2);
        return new IntConsumer() { // from class: j$.util.function.d
            public final /* synthetic */ IntConsumer andThen(IntConsumer intConsumer3) {
                return j$.com.android.tools.r8.a.c(this, intConsumer3);
            }

            @Override // java.util.function.IntConsumer
            public final void accept(int i) {
                intConsumer.accept(i);
                intConsumer2.accept(i);
            }
        };
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.util.function.g] */
    public static g d(final LongConsumer longConsumer, final LongConsumer longConsumer2) {
        Objects.requireNonNull(longConsumer2);
        return new LongConsumer() { // from class: j$.util.function.g
            public final /* synthetic */ LongConsumer andThen(LongConsumer longConsumer3) {
                return j$.com.android.tools.r8.a.d(this, longConsumer3);
            }

            @Override // java.util.function.LongConsumer
            public final void accept(long j) {
                longConsumer.accept(j);
                longConsumer2.accept(j);
            }
        };
    }

    public static C H(OptionalLong optionalLong) {
        if (optionalLong == null) {
            return null;
        }
        if (!optionalLong.isPresent()) {
            return C.c;
        }
        return new C(optionalLong.getAsLong());
    }

    public static String D(long j, String str, Locale locale) {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str, locale);
        simpleDateFormat.setTimeZone(timeZone);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        calendar.set(0, (int) j, 0, 0, 0, 0);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static OptionalInt G(java.util.OptionalInt optionalInt) {
        if (optionalInt == null) {
            return null;
        }
        if (!optionalInt.isPresent()) {
            return OptionalInt.c;
        }
        return new OptionalInt(optionalInt.getAsInt());
    }

    public static void i(ConcurrentMap concurrentMap, BiConsumer biConsumer) {
        Objects.requireNonNull(biConsumer);
        for (Map.Entry entry : concurrentMap.entrySet()) {
            try {
                biConsumer.accept(entry.getKey(), entry.getValue());
            } catch (IllegalStateException unused) {
            }
        }
    }

    public static String X(Object obj, Object obj2) {
        String string;
        String string2;
        String str = "null";
        if (obj == null || (string = obj.toString()) == null) {
            string = "null";
        }
        int length = string.length();
        if (obj2 != null && (string2 = obj2.toString()) != null) {
            str = string2;
        }
        int length2 = str.length();
        char[] cArr = new char[length + length2 + 1];
        string.getChars(0, length, cArr, 0);
        cArr[length] = SignatureVisitor.INSTANCEOF;
        str.getChars(0, length2, cArr, length + 1);
        return new String(cArr);
    }

    public static String C(long j, String str, Locale locale) {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str, locale);
        simpleDateFormat.setTimeZone(timeZone);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        calendar.set(2016, 1, (int) j, 0, 0, 0);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static void M(Iterator it, Consumer consumer) {
        if (it instanceof InterfaceC0330y) {
            ((InterfaceC0330y) it).forEachRemaining(consumer);
            return;
        }
        Objects.requireNonNull(consumer);
        while (it.hasNext()) {
            consumer.accept(it.next());
        }
    }

    public static OptionalDouble J(B b) {
        if (b == null) {
            return null;
        }
        boolean z = b.a;
        if (!z) {
            return OptionalDouble.empty();
        }
        if (z) {
            return OptionalDouble.of(b.b);
        }
        throw new NoSuchElementException("No value present");
    }

    public static java.util.OptionalInt K(OptionalInt optionalInt) {
        if (optionalInt == null) {
            return null;
        }
        boolean z = optionalInt.a;
        if (!z) {
            return java.util.OptionalInt.empty();
        }
        if (z) {
            return java.util.OptionalInt.of(optionalInt.b);
        }
        throw new NoSuchElementException("No value present");
    }

    public static OptionalLong L(C c) {
        if (c == null) {
            return null;
        }
        boolean z = c.a;
        if (!z) {
            return OptionalLong.empty();
        }
        if (z) {
            return OptionalLong.of(c.b);
        }
        throw new NoSuchElementException("No value present");
    }

    public static boolean s(l lVar, r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return rVar == j$.time.temporal.a.ERA;
        }
        return rVar != null && rVar.i(lVar);
    }

    public static j$.time.chrono.k N(n nVar) {
        Objects.requireNonNull(nVar, "temporal");
        return (j$.time.chrono.k) Objects.requireNonNullElse((j$.time.chrono.k) nVar.t(s.b), j$.time.chrono.r.c);
    }

    public static j$.time.a Z() {
        return new j$.time.a(ZoneId.systemDefault());
    }

    public static int m(ChronoZonedDateTime chronoZonedDateTime, r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            int i = AbstractC0169h.a[((j$.time.temporal.a) rVar).ordinal()];
            if (i == 1) {
                throw new u("Invalid field 'InstantSeconds' for get() method, use getLong() instead");
            }
            if (i == 2) {
                return chronoZonedDateTime.g().getTotalSeconds();
            }
            return chronoZonedDateTime.o().i(rVar);
        }
        return s.a(chronoZonedDateTime, rVar);
    }

    public static int n(l lVar, r rVar) {
        if (rVar == j$.time.temporal.a.ERA) {
            return lVar.getValue();
        }
        return s.a(lVar, rVar);
    }

    public static long p(l lVar, r rVar) {
        if (rVar == j$.time.temporal.a.ERA) {
            return lVar.getValue();
        }
        if (rVar instanceof j$.time.temporal.a) {
            throw new u(c.a("Unsupported field: ", rVar));
        }
        return rVar.t(lVar);
    }

    public static j$.time.chrono.k Y(String str) {
        ConcurrentHashMap concurrentHashMap = AbstractC0162a.a;
        Objects.requireNonNull(str, "id");
        while (true) {
            ConcurrentHashMap concurrentHashMap2 = AbstractC0162a.a;
            j$.time.chrono.k kVar = (j$.time.chrono.k) concurrentHashMap2.get(str);
            if (kVar == null) {
                kVar = (j$.time.chrono.k) AbstractC0162a.b.get(str);
            }
            if (kVar != null) {
                return kVar;
            }
            if (concurrentHashMap2.get("ISO") != null) {
                for (j$.time.chrono.k kVar2 : ServiceLoader.load(j$.time.chrono.k.class)) {
                    if (str.equals(kVar2.getId()) || str.equals(kVar2.l())) {
                        return kVar2;
                    }
                }
                throw new j$.time.b("Unknown chronology: " + str);
            }
            j$.time.chrono.n nVar = j$.time.chrono.n.l;
            nVar.getClass();
            AbstractC0162a.k(nVar, "Hijrah-umalqura");
            j$.time.chrono.u uVar = j$.time.chrono.u.c;
            uVar.getClass();
            AbstractC0162a.k(uVar, "Japanese");
            z zVar = z.c;
            zVar.getClass();
            AbstractC0162a.k(zVar, "Minguo");
            F f = F.c;
            f.getClass();
            AbstractC0162a.k(f, "ThaiBuddhist");
            try {
                for (AbstractC0162a abstractC0162a : Arrays.asList(new AbstractC0162a[0])) {
                    if (!abstractC0162a.getId().equals("ISO")) {
                        AbstractC0162a.k(abstractC0162a, abstractC0162a.getId());
                    }
                }
                j$.time.chrono.r rVar = j$.time.chrono.r.c;
                rVar.getClass();
                AbstractC0162a.k(rVar, "ISO");
            } catch (Throwable th) {
                throw new ServiceConfigurationError(th.getMessage(), th);
            }
        }
    }

    public static Object w(l lVar, e eVar) {
        if (eVar == s.c) {
            return j$.time.temporal.b.ERAS;
        }
        return s.c(lVar, eVar);
    }

    public static Object u(ChronoLocalDateTime chronoLocalDateTime, e eVar) {
        if (eVar == s.a || eVar == s.e || eVar == s.d) {
            return null;
        }
        if (eVar == s.g) {
            return chronoLocalDateTime.b();
        }
        if (eVar == s.b) {
            return chronoLocalDateTime.a();
        }
        if (eVar == s.c) {
            return j$.time.temporal.b.NANOS;
        }
        return eVar.g(chronoLocalDateTime);
    }

    public static boolean r(InterfaceC0163b interfaceC0163b, r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return ((j$.time.temporal.a) rVar).isDateBased();
        }
        return rVar != null && rVar.i(interfaceC0163b);
    }

    public static long o(Spliterator spliterator) {
        if ((spliterator.characteristics() & 64) == 0) {
            return -1L;
        }
        return spliterator.estimateSize();
    }

    public static Object h(ConcurrentMap concurrentMap, Object obj, BiFunction biFunction) {
        Object objApply;
        loop0: while (true) {
            Object objPutIfAbsent = concurrentMap.get(obj);
            do {
                objApply = biFunction.apply(obj, objPutIfAbsent);
                if (objApply != null) {
                    if (objPutIfAbsent != null) {
                        if (concurrentMap.replace(obj, objPutIfAbsent, objApply)) {
                            break;
                        }
                    } else {
                        objPutIfAbsent = concurrentMap.putIfAbsent(obj, objApply);
                    }
                } else if (objPutIfAbsent == null || concurrentMap.remove(obj, objPutIfAbsent)) {
                    return null;
                }
            } while (objPutIfAbsent != null);
        }
        return objApply;
    }

    public static boolean q(Spliterator spliterator, int i) {
        return (spliterator.characteristics() & i) == i;
    }

    public static long x(ChronoLocalDateTime chronoLocalDateTime, ZoneOffset zoneOffset) {
        Objects.requireNonNull(zoneOffset, "offset");
        return ((chronoLocalDateTime.f().E() * 86400) + ((long) chronoLocalDateTime.b().d0())) - ((long) zoneOffset.getTotalSeconds());
    }

    public static Object v(ChronoZonedDateTime chronoZonedDateTime, e eVar) {
        if (eVar == s.e || eVar == s.a) {
            return chronoZonedDateTime.C();
        }
        if (eVar == s.d) {
            return chronoZonedDateTime.g();
        }
        if (eVar == s.g) {
            return chronoZonedDateTime.b();
        }
        if (eVar == s.b) {
            return chronoZonedDateTime.a();
        }
        if (eVar == s.c) {
            return j$.time.temporal.b.NANOS;
        }
        return eVar.g(chronoZonedDateTime);
    }

    public static int f(ChronoLocalDateTime chronoLocalDateTime, ChronoLocalDateTime chronoLocalDateTime2) {
        int iN = chronoLocalDateTime.f().compareTo(chronoLocalDateTime2.f());
        return (iN == 0 && (iN = chronoLocalDateTime.b().compareTo(chronoLocalDateTime2.b())) == 0) ? ((AbstractC0162a) chronoLocalDateTime.a()).getId().compareTo(chronoLocalDateTime2.a().getId()) : iN;
    }

    public static Object t(InterfaceC0163b interfaceC0163b, e eVar) {
        if (eVar == s.a || eVar == s.e || eVar == s.d || eVar == s.g) {
            return null;
        }
        if (eVar == s.b) {
            return interfaceC0163b.a();
        }
        if (eVar == s.c) {
            return j$.time.temporal.b.DAYS;
        }
        return eVar.g(interfaceC0163b);
    }

    public static m a(InterfaceC0163b interfaceC0163b, m mVar) {
        return mVar.c(interfaceC0163b.E(), j$.time.temporal.a.EPOCH_DAY);
    }

    public static long y(ChronoZonedDateTime chronoZonedDateTime) {
        return ((chronoZonedDateTime.f().E() * 86400) + ((long) chronoZonedDateTime.b().d0())) - ((long) chronoZonedDateTime.g().getTotalSeconds());
    }

    public static int g(ChronoZonedDateTime chronoZonedDateTime, ChronoZonedDateTime chronoZonedDateTime2) {
        int iCompare = Long.compare(chronoZonedDateTime.P(), chronoZonedDateTime2.P());
        return (iCompare == 0 && (iCompare = chronoZonedDateTime.b().d - chronoZonedDateTime2.b().d) == 0 && (iCompare = chronoZonedDateTime.o().H(chronoZonedDateTime2.o())) == 0 && (iCompare = chronoZonedDateTime.C().getId().compareTo(chronoZonedDateTime2.C().getId())) == 0) ? ((AbstractC0162a) chronoZonedDateTime.a()).getId().compareTo(chronoZonedDateTime2.a().getId()) : iCompare;
    }

    public static boolean A(Spliterator.OfInt ofInt, Consumer consumer) {
        if (consumer instanceof IntConsumer) {
            return ofInt.tryAdvance((IntConsumer) consumer);
        }
        if (t0.a) {
            t0.a(ofInt.getClass(), "{0} calling Spliterator.OfInt.tryAdvance((IntConsumer) action::accept)");
            throw null;
        }
        Objects.requireNonNull(consumer);
        return ofInt.tryAdvance((IntConsumer) new H(consumer, 0));
    }

    public static void k(Spliterator.OfInt ofInt, Consumer consumer) {
        if (consumer instanceof IntConsumer) {
            ofInt.forEachRemaining((IntConsumer) consumer);
        } else {
            if (t0.a) {
                t0.a(ofInt.getClass(), "{0} calling Spliterator.OfInt.forEachRemaining((IntConsumer) action::accept)");
                throw null;
            }
            Objects.requireNonNull(consumer);
            ofInt.forEachRemaining((IntConsumer) new H(consumer, 0));
        }
    }

    public static int e(InterfaceC0163b interfaceC0163b, InterfaceC0163b interfaceC0163b2) {
        int iCompare = Long.compare(interfaceC0163b.E(), interfaceC0163b2.E());
        if (iCompare != 0) {
            return iCompare;
        }
        return ((AbstractC0162a) interfaceC0163b.a()).getId().compareTo(interfaceC0163b2.a().getId());
    }

    public static boolean B(Z z, Consumer consumer) {
        if (consumer instanceof LongConsumer) {
            return z.tryAdvance((LongConsumer) consumer);
        }
        if (t0.a) {
            t0.a(z.getClass(), "{0} calling Spliterator.OfLong.tryAdvance((LongConsumer) action::accept)");
            throw null;
        }
        Objects.requireNonNull(consumer);
        return z.tryAdvance((LongConsumer) new L(consumer, 0));
    }

    public static void l(Z z, Consumer consumer) {
        if (consumer instanceof LongConsumer) {
            z.forEachRemaining((LongConsumer) consumer);
        } else {
            if (t0.a) {
                t0.a(z.getClass(), "{0} calling Spliterator.OfLong.forEachRemaining((LongConsumer) action::accept)");
                throw null;
            }
            Objects.requireNonNull(consumer);
            z.forEachRemaining((LongConsumer) new L(consumer, 0));
        }
    }

    public static boolean z(U u, Consumer consumer) {
        if (consumer instanceof DoubleConsumer) {
            return u.tryAdvance((DoubleConsumer) consumer);
        }
        if (t0.a) {
            t0.a(u.getClass(), "{0} calling Spliterator.OfDouble.tryAdvance((DoubleConsumer) action::accept)");
            throw null;
        }
        Objects.requireNonNull(consumer);
        return u.tryAdvance((DoubleConsumer) new D(consumer, 0));
    }

    public static void j(U u, Consumer consumer) {
        if (consumer instanceof DoubleConsumer) {
            u.forEachRemaining((DoubleConsumer) consumer);
        } else {
            if (t0.a) {
                t0.a(u.getClass(), "{0} calling Spliterator.OfDouble.forEachRemaining((DoubleConsumer) action::accept)");
                throw null;
            }
            Objects.requireNonNull(consumer);
            u.forEachRemaining((DoubleConsumer) new D(consumer, 0));
        }
    }

    public Spliterator trySplit() {
        return null;
    }

    public boolean tryAdvance(Object obj) {
        Objects.requireNonNull(obj);
        return false;
    }

    public void forEachRemaining(Object obj) {
        Objects.requireNonNull(obj);
    }

    public long estimateSize() {
        return 0L;
    }

    public int characteristics() {
        return 16448;
    }
}
