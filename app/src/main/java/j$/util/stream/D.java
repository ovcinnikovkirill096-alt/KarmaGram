package j$.util.stream;

import j$.util.C0328w;
import j$.util.Spliterator;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

public final /* synthetic */ class D implements F {
    public final /* synthetic */ DoubleStream a;

    public /* synthetic */ D(DoubleStream doubleStream) {
        this.a = doubleStream;
    }

    public static /* synthetic */ F f(DoubleStream doubleStream) {
        if (doubleStream == null) {
            return null;
        }
        return doubleStream instanceof E ? ((E) doubleStream).a : new D(doubleStream);
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.B average() {
        return j$.com.android.tools.r8.a.F(this.a.average());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F b() {
        return f(this.a.takeWhile(null));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ Stream boxed() {
        return Stream.VivifiedWrapper.convert(this.a.boxed());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F c() {
        return f(this.a.filter(null));
    }

    @Override // j$.util.stream.BaseStream, java.lang.AutoCloseable
    public final /* synthetic */ void close() {
        this.a.close();
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ Object collect(Supplier supplier, ObjDoubleConsumer objDoubleConsumer, BiConsumer biConsumer) {
        return this.a.collect(supplier, objDoubleConsumer, biConsumer);
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F d() {
        return f(this.a.dropWhile(null));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F distinct() {
        return f(this.a.distinct());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F e() {
        return f(this.a.map(null));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        DoubleStream doubleStream = this.a;
        if (obj instanceof D) {
            obj = ((D) obj).a;
        }
        return doubleStream.equals(obj);
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.B findAny() {
        return j$.com.android.tools.r8.a.F(this.a.findAny());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.B findFirst() {
        return j$.com.android.tools.r8.a.F(this.a.findFirst());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ void forEach(DoubleConsumer doubleConsumer) {
        this.a.forEach(doubleConsumer);
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ void forEachOrdered(DoubleConsumer doubleConsumer) {
        this.a.forEachOrdered(doubleConsumer);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.util.PrimitiveIterator$OfDouble] */
    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.G iterator() {
        ?? it = this.a.iterator();
        if (it == 0) {
            return null;
        }
        return it instanceof j$.util.F ? ((j$.util.F) it).a : new j$.util.E(it);
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ boolean l() {
        return this.a.anyMatch(null);
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F limit(long j) {
        return f(this.a.limit(j));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ Stream mapToObj(DoubleFunction doubleFunction) {
        return Stream.VivifiedWrapper.convert(this.a.mapToObj(doubleFunction));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.B max() {
        return j$.com.android.tools.r8.a.F(this.a.max());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.B min() {
        return j$.com.android.tools.r8.a.F(this.a.min());
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream onClose(Runnable runnable) {
        return C0221f.f(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ BaseStream parallel() {
        return C0221f.f(this.a.parallel());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F parallel() {
        return f(this.a.parallel());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F peek(DoubleConsumer doubleConsumer) {
        return f(this.a.peek(doubleConsumer));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ boolean q() {
        return this.a.allMatch(null);
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ LongStream r() {
        return C0262n0.f(this.a.mapToLong(null));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ double reduce(double d, DoubleBinaryOperator doubleBinaryOperator) {
        return this.a.reduce(d, doubleBinaryOperator);
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.B reduce(DoubleBinaryOperator doubleBinaryOperator) {
        return j$.com.android.tools.r8.a.F(this.a.reduce(doubleBinaryOperator));
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ BaseStream sequential() {
        return C0221f.f(this.a.sequential());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F sequential() {
        return f(this.a.sequential());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F skip(long j) {
        return f(this.a.skip(j));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F sorted() {
        return f(this.a.sorted());
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ Spliterator spliterator() {
        return j$.util.d0.a(this.a.spliterator());
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.util.Spliterator$OfDouble] */
    @Override // j$.util.stream.F, j$.util.stream.BaseStream
    public final /* synthetic */ j$.util.U spliterator() {
        return j$.util.S.a(this.a.spliterator());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ double sum() {
        return this.a.sum();
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ double[] toArray() {
        return this.a.toArray();
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream unordered() {
        return C0221f.f(this.a.unordered());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ IntStream w() {
        return IntStream.VivifiedWrapper.convert(this.a.mapToInt(null));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ boolean y() {
        return this.a.noneMatch(null);
    }

    @Override // j$.util.stream.F
    public final C0328w summaryStatistics() {
        this.a.summaryStatistics();
        throw new Error("Java 8+ API desugaring (library desugaring) cannot convert from java.util.DoubleSummaryStatistics");
    }

    @Override // j$.util.stream.F
    public final F a(j$.time.t tVar) {
        DoubleStream doubleStream = this.a;
        j$.time.t tVar2 = new j$.time.t(5);
        tVar2.b = tVar;
        return f(doubleStream.flatMap(tVar2));
    }
}
