package kotlin.comparisons;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import kotlin.jvm.internal.Intrinsics;

final class NaturalOrderComparator implements Comparator, j$.util.Comparator {
    public static final NaturalOrderComparator INSTANCE = new NaturalOrderComparator();

    @Override // java.util.Comparator, j$.util.Comparator
    public /* synthetic */ Comparator thenComparing(Comparator comparator) {
        return j$.util.Comparator.CC.$default$thenComparing(this, comparator);
    }

    @Override // java.util.Comparator, j$.util.Comparator
    public /* synthetic */ Comparator thenComparing(Function function) {
        return j$.util.Comparator.EL.a(this, j$.util.Comparator.CC.comparing(function));
    }

    @Override // java.util.Comparator, j$.util.Comparator
    public /* synthetic */ Comparator thenComparing(Function function, Comparator comparator) {
        return j$.util.Comparator.EL.a(this, j$.util.Comparator.CC.comparing(function, comparator));
    }

    @Override // java.util.Comparator, j$.util.Comparator
    public /* synthetic */ Comparator thenComparingDouble(ToDoubleFunction toDoubleFunction) {
        return j$.util.Comparator.EL.a(this, j$.util.Comparator.CC.comparingDouble(toDoubleFunction));
    }

    @Override // java.util.Comparator, j$.util.Comparator
    public /* synthetic */ Comparator thenComparingInt(ToIntFunction toIntFunction) {
        return j$.util.Comparator.CC.$default$thenComparingInt(this, toIntFunction);
    }

    @Override // java.util.Comparator, j$.util.Comparator
    public /* synthetic */ Comparator thenComparingLong(ToLongFunction toLongFunction) {
        return j$.util.Comparator.EL.a(this, j$.util.Comparator.CC.comparingLong(toLongFunction));
    }

    private NaturalOrderComparator() {
    }

    @Override // java.util.Comparator
    public int compare(Comparable a, Comparable b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        return a.compareTo(b);
    }

    @Override // java.util.Comparator, j$.util.Comparator
    public final Comparator reversed() {
        return ReverseOrderComparator.INSTANCE;
    }
}
