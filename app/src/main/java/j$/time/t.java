package j$.time;

import j$.util.C0184g;
import j$.util.C0194q;
import j$.util.function.BiFunction$CC;
import j$.util.function.Consumer$CC;
import j$.util.function.Function$CC;
import j$.util.function.IntPredicate$CC;
import j$.util.function.Predicate$CC;
import j$.util.stream.C0262n0;
import j$.util.stream.C0267o0;
import j$.util.stream.C0270o3;
import j$.util.stream.C0280q3;
import j$.util.stream.C0289s3;
import j$.util.stream.Collectors;
import j$.util.stream.D;
import j$.util.stream.E;
import j$.util.stream.EnumC0210c3;
import j$.util.stream.F;
import j$.util.stream.H3;
import j$.util.stream.IntStream;
import j$.util.stream.InterfaceC0279q2;
import j$.util.stream.LongStream;
import j$.util.stream.Stream;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;

public final class t implements j$.time.temporal.n, Consumer, IntPredicate, Predicate, BinaryOperator, DoubleFunction, Function, LongFunction, BooleanSupplier {
    public final /* synthetic */ int a;
    public Object b;

    public /* synthetic */ t(int i) {
        this.a = i;
    }

    public /* synthetic */ t(int i, Object obj) {
        this.a = i;
        this.b = obj;
    }

    public /* synthetic */ IntPredicate and(IntPredicate intPredicate) {
        return IntPredicate$CC.$default$and(this, intPredicate);
    }

    public /* synthetic */ Predicate and(Predicate predicate) {
        return Predicate$CC.$default$and(this, predicate);
    }

    public /* synthetic */ BiFunction andThen(Function function) {
        return BiFunction$CC.$default$andThen(this, function);
    }

    public /* synthetic */ Consumer andThen(Consumer consumer) {
        switch (this.a) {
            case 1:
                break;
            case 8:
                break;
        }
        return Consumer$CC.$default$andThen(this, consumer);
    }

    /* JADX INFO: renamed from: andThen, reason: collision with other method in class */
    public /* synthetic */ Function m2426andThen(Function function) {
        return Function$CC.$default$andThen(this, function);
    }

    public /* synthetic */ Function compose(Function function) {
        return Function$CC.$default$compose(this, function);
    }

    @Override // j$.time.temporal.n
    public boolean e(j$.time.temporal.r rVar) {
        return false;
    }

    @Override // j$.time.temporal.n
    public /* synthetic */ int i(j$.time.temporal.r rVar) {
        return j$.time.temporal.s.a(this, rVar);
    }

    @Override // j$.time.temporal.n
    public /* synthetic */ j$.time.temporal.v k(j$.time.temporal.r rVar) {
        return j$.time.temporal.s.d(this, rVar);
    }

    public /* synthetic */ IntPredicate negate() {
        return IntPredicate$CC.$default$negate(this);
    }

    /* JADX INFO: renamed from: negate, reason: collision with other method in class */
    public /* synthetic */ Predicate m2427negate() {
        return Predicate$CC.$default$negate(this);
    }

    public /* synthetic */ IntPredicate or(IntPredicate intPredicate) {
        return IntPredicate$CC.$default$or(this, intPredicate);
    }

    public /* synthetic */ Predicate or(Predicate predicate) {
        return Predicate$CC.$default$or(this, predicate);
    }

    @Override // java.util.function.Predicate
    public boolean test(Object obj) {
        return !((Predicate) this.b).test(obj);
    }

    @Override // java.util.function.Function
    public Object apply(Object obj) {
        Object objApply = ((Function) this.b).apply(obj);
        if (objApply == null) {
            return null;
        }
        if (objApply instanceof Stream) {
            return Stream.Wrapper.convert((Stream) objApply);
        }
        if (objApply instanceof java.util.stream.Stream) {
            return Stream.VivifiedWrapper.convert((java.util.stream.Stream) objApply);
        }
        if (objApply instanceof IntStream) {
            return IntStream.Wrapper.convert((IntStream) objApply);
        }
        if (objApply instanceof java.util.stream.IntStream) {
            return IntStream.VivifiedWrapper.convert((java.util.stream.IntStream) objApply);
        }
        if (objApply instanceof F) {
            return E.f((F) objApply);
        }
        if (objApply instanceof DoubleStream) {
            return D.f((DoubleStream) objApply);
        }
        if (objApply instanceof LongStream) {
            return C0267o0.f((LongStream) objApply);
        }
        if (objApply instanceof java.util.stream.LongStream) {
            return C0262n0.f((java.util.stream.LongStream) objApply);
        }
        C0184g.a(objApply.getClass(), "java.util.stream.*Stream");
        throw null;
    }

    @Override // java.util.function.IntPredicate
    public boolean test(int i) {
        return !((IntPredicate) this.b).test(i);
    }

    @Override // java.util.function.DoubleFunction
    public Object apply(double d) {
        Object objApply = ((DoubleFunction) this.b).apply(d);
        if (objApply == null) {
            return null;
        }
        if (objApply instanceof F) {
            return E.f((F) objApply);
        }
        if (objApply instanceof DoubleStream) {
            return D.f((DoubleStream) objApply);
        }
        C0184g.a(objApply.getClass(), "java.util.stream.DoubleStream");
        throw null;
    }

    @Override // java.util.function.LongFunction
    public Object apply(long j) {
        Object objApply = ((LongFunction) this.b).apply(j);
        if (objApply == null) {
            return null;
        }
        if (objApply instanceof LongStream) {
            return C0267o0.f((LongStream) objApply);
        }
        if (objApply instanceof java.util.stream.LongStream) {
            return C0262n0.f((java.util.stream.LongStream) objApply);
        }
        C0184g.a(objApply.getClass(), "java.util.stream.LongStream");
        throw null;
    }

    @Override // java.util.function.BooleanSupplier
    public boolean getAsBoolean() {
        switch (this.a) {
            case 11:
                C0270o3 c0270o3 = (C0270o3) this.b;
                return c0270o3.d.tryAdvance(c0270o3.e);
            case 12:
                C0280q3 c0280q3 = (C0280q3) this.b;
                return c0280q3.d.tryAdvance(c0280q3.e);
            case 13:
                C0289s3 c0289s3 = (C0289s3) this.b;
                return c0289s3.d.tryAdvance(c0289s3.e);
            default:
                H3 h3 = (H3) this.b;
                return h3.d.tryAdvance(h3.e);
        }
    }

    public void s(EnumC0210c3 enumC0210c3) {
        ((EnumMap) ((Map) this.b)).put(enumC0210c3, 1);
    }

    @Override // java.util.function.BiFunction
    public Object apply(Object obj, Object obj2) {
        BinaryOperator binaryOperator = (BinaryOperator) this.b;
        Map map = (Map) obj;
        Set set = Collectors.a;
        for (Map.Entry entry : ((Map) obj2).entrySet()) {
            j$.util.Map.EL.a(map, entry.getKey(), entry.getValue(), binaryOperator);
        }
        return map;
    }

    @Override // j$.time.temporal.n
    public long D(j$.time.temporal.r rVar) {
        throw new j$.time.temporal.u(c.a("Unsupported field: ", rVar));
    }

    @Override // j$.time.temporal.n
    public Object t(e eVar) {
        if (eVar == j$.time.temporal.s.a) {
            return (ZoneId) this.b;
        }
        return j$.time.temporal.s.c(this, eVar);
    }

    @Override // java.util.function.Consumer
    public void accept(Object obj) {
        switch (this.a) {
            case 1:
                ((Consumer) this.b).accept(new C0194q((Map.Entry) obj));
                break;
            case 8:
                ((InterfaceC0279q2) this.b).accept(obj);
                break;
            default:
                ((List) this.b).add(obj);
                break;
        }
    }
}
