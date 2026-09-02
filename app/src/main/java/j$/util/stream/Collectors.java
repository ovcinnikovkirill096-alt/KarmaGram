package j$.util.stream;

import j$.util.function.BiConsumer$CC;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Collectors {
    public static final Set a;
    public static final Set b;
    public static final Set c;

    static {
        EnumC0231h enumC0231h = EnumC0231h.CONCURRENT;
        EnumC0231h enumC0231h2 = EnumC0231h.UNORDERED;
        EnumC0231h enumC0231h3 = EnumC0231h.IDENTITY_FINISH;
        Collections.unmodifiableSet(EnumSet.of(enumC0231h, enumC0231h2, enumC0231h3));
        Collections.unmodifiableSet(EnumSet.of(enumC0231h, enumC0231h2));
        a = Collections.unmodifiableSet(EnumSet.of(enumC0231h3));
        b = Collections.unmodifiableSet(EnumSet.of(enumC0231h2, enumC0231h3));
        c = Collections.EMPTY_SET;
        Collections.unmodifiableSet(EnumSet.of(enumC0231h2));
    }

    public static <T, C extends Collection<T>> Collector<T, ?, C> toCollection(Supplier<C> supplier) {
        return new C0256m(supplier, new j$.time.e(12), new j$.time.e(13), a);
    }

    public static <T> Collector<T, ?, List<T>> toList() {
        return new C0256m(new C0246k(1), new j$.time.e(14), new j$.time.e(16), a);
    }

    public static <T> Collector<T, ?, Set<T>> toSet() {
        return new C0256m(new C0246k(3), new j$.time.e(15), new j$.time.e(21), b);
    }

    public static Collector<CharSequence, ?, String> joining() {
        return new C0256m(new C0246k(5), new j$.time.e(22), new j$.time.e(23), new j$.time.e(24), c);
    }

    public static Collector<CharSequence, ?, String> joining(CharSequence charSequence) {
        return new C0256m(new C0196a(2, charSequence), new j$.time.e(18), new j$.time.e(19), new j$.time.e(20), c);
    }

    public static void a(double[] dArr, double d) {
        double d2 = d - dArr[1];
        double d3 = dArr[0];
        double d4 = d3 + d2;
        dArr[1] = (d4 - d3) - d2;
        dArr[0] = d4;
    }

    public static <T, K, U, M extends Map<K, U>> Collector<T, ?, M> toMap(final Function<? super T, ? extends K> function, final Function<? super T, ? extends U> function2, final BinaryOperator<U> binaryOperator, Supplier<M> supplier) {
        return new C0256m(supplier, new BiConsumer() { // from class: j$.util.stream.l
            public final /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer$CC.$default$andThen(this, biConsumer);
            }

            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                Set set = Collectors.a;
                j$.util.Map.EL.a((Map) obj, function.apply(obj2), function2.apply(obj2), binaryOperator);
            }
        }, new j$.time.t(4, binaryOperator), a);
    }
}
