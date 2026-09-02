package j$.time;

import j$.util.Objects;
import j$.util.function.BiConsumer$CC;
import j$.util.function.BiFunction$CC;
import j$.util.function.Function$CC;
import j$.util.r0;
import j$.util.stream.Collectors;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ObjDoubleConsumer;

public final /* synthetic */ class e implements j$.time.temporal.o, Function, IntFunction, BiConsumer, BinaryOperator, DoubleBinaryOperator, ObjDoubleConsumer {
    public final /* synthetic */ int a;

    public /* synthetic */ e(int i) {
        this.a = i;
    }

    public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
        switch (this.a) {
            case 12:
                break;
            case 14:
                break;
            case 15:
                break;
            case 18:
                break;
            case 22:
                break;
            case 25:
                break;
            case 26:
                break;
        }
        return BiConsumer$CC.$default$andThen(this, biConsumer);
    }

    public /* synthetic */ BiFunction andThen(Function function) {
        switch (this.a) {
            case 13:
                break;
            case 16:
                break;
            case 19:
                break;
            case 21:
                break;
        }
        return BiFunction$CC.$default$andThen(this, function);
    }

    /* JADX INFO: renamed from: andThen, reason: collision with other method in class */
    public /* synthetic */ Function m2425andThen(Function function) {
        switch (this.a) {
            case 10:
                break;
            case 17:
                break;
            case 20:
                break;
        }
        return Function$CC.$default$andThen(this, function);
    }

    @Override // java.util.function.Function
    public Object apply(Object obj) {
        switch (this.a) {
            case 17:
                Set set = Collectors.a;
            case 10:
                return obj;
            case 20:
                return ((r0) obj).toString();
            default:
                return ((StringBuilder) obj).toString();
        }
    }

    @Override // java.util.function.DoubleBinaryOperator
    public double applyAsDouble(double d, double d2) {
        return Math.min(d, d2);
    }

    public /* synthetic */ Function compose(Function function) {
        switch (this.a) {
            case 10:
                break;
            case 17:
                break;
            case 20:
                break;
        }
        return Function$CC.$default$compose(this, function);
    }

    @Override // j$.time.temporal.o
    public j$.time.temporal.m n(j$.time.temporal.m mVar) {
        j$.time.temporal.a aVar = j$.time.temporal.a.DAY_OF_MONTH;
        return mVar.c(mVar.k(aVar).d, aVar);
    }

    @Override // java.util.function.BiFunction
    public Object apply(Object obj, Object obj2) {
        switch (this.a) {
            case 13:
                Collection collection = (Collection) obj;
                Set set = Collectors.a;
                collection.addAll((Collection) obj2);
                return collection;
            case 16:
                List list = (List) obj;
                Set set2 = Collectors.a;
                list.addAll((List) obj2);
                return list;
            case 19:
                r0 r0Var = (r0) obj;
                r0 r0Var2 = (r0) obj2;
                r0Var.getClass();
                Objects.requireNonNull(r0Var2);
                if (r0Var2.d != null) {
                    r0Var2.b();
                    r0Var.a(r0Var2.d[0]);
                }
                return r0Var;
            case 21:
                Set set3 = (Set) obj;
                Set set4 = (Set) obj2;
                Set set5 = Collectors.a;
                if (set3.size() < set4.size()) {
                    set4.addAll(set3);
                    return set4;
                }
                set3.addAll(set4);
                return set3;
            default:
                StringBuilder sb = (StringBuilder) obj;
                Set set6 = Collectors.a;
                sb.append((CharSequence) obj2);
                return sb;
        }
    }

    @Override // java.util.function.BiConsumer
    public void accept(Object obj, Object obj2) {
        switch (this.a) {
            case 12:
                ((Collection) obj).add(obj2);
                break;
            case 14:
                ((List) obj).add(obj2);
                break;
            case 15:
                ((Set) obj).add(obj2);
                break;
            case 18:
                ((r0) obj).a((CharSequence) obj2);
                break;
            case 22:
                ((StringBuilder) obj).append((CharSequence) obj2);
                break;
            case 25:
                ((LinkedHashSet) obj).add(obj2);
                break;
            case 26:
                ((LinkedHashSet) obj).addAll((LinkedHashSet) obj2);
                break;
            default:
                double[] dArr = (double[]) obj;
                double[] dArr2 = (double[]) obj2;
                Collectors.a(dArr, dArr2[0]);
                Collectors.a(dArr, dArr2[1]);
                dArr[2] = dArr[2] + dArr2[2];
                break;
        }
    }

    public Object g(j$.time.temporal.n nVar) {
        switch (this.a) {
            case 0:
                return LocalDate.S(nVar);
            case 1:
                ZoneId zoneId = (ZoneId) nVar.t(j$.time.temporal.s.a);
                if (zoneId == null || (zoneId instanceof ZoneOffset)) {
                    return null;
                }
                return zoneId;
            case 2:
            default:
                j$.time.temporal.a aVar = j$.time.temporal.a.NANO_OF_DAY;
                if (nVar.e(aVar)) {
                    return i.V(nVar.D(aVar));
                }
                return null;
            case 3:
                return (ZoneId) nVar.t(j$.time.temporal.s.a);
            case 4:
                return (j$.time.chrono.k) nVar.t(j$.time.temporal.s.b);
            case 5:
                return (j$.time.temporal.t) nVar.t(j$.time.temporal.s.c);
            case 6:
                j$.time.temporal.a aVar2 = j$.time.temporal.a.OFFSET_SECONDS;
                if (nVar.e(aVar2)) {
                    return ZoneOffset.W(nVar.i(aVar2));
                }
                return null;
            case 7:
                ZoneId zoneId2 = (ZoneId) nVar.t(j$.time.temporal.s.a);
                return zoneId2 != null ? zoneId2 : (ZoneId) nVar.t(j$.time.temporal.s.d);
            case 8:
                j$.time.temporal.a aVar3 = j$.time.temporal.a.EPOCH_DAY;
                if (nVar.e(aVar3)) {
                    return LocalDate.b0(nVar.D(aVar3));
                }
                return null;
        }
    }

    public String toString() {
        switch (this.a) {
            case 3:
                return "ZoneId";
            case 4:
                return "Chronology";
            case 5:
                return "Precision";
            case 6:
                return "ZoneOffset";
            case 7:
                return "Zone";
            case 8:
                return "LocalDate";
            case 9:
                return "LocalTime";
            default:
                return super.toString();
        }
    }

    @Override // java.util.function.ObjDoubleConsumer
    public void accept(Object obj, double d) {
        double[] dArr = (double[]) obj;
        dArr[2] = dArr[2] + 1.0d;
        Collectors.a(dArr, d);
        dArr[3] = dArr[3] + d;
    }

    @Override // java.util.function.IntFunction
    public Object apply(int i) {
        return new Object[i];
    }
}
