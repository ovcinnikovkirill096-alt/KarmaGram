package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;

public abstract class I3 {
    public static C0224f2 b(Spliterator spliterator, boolean z) {
        Objects.requireNonNull(spliterator);
        return new C0224f2(spliterator, EnumC0215d3.k(spliterator), z);
    }

    public static C0197a0 a(Spliterator.OfInt ofInt) {
        return new C0197a0(ofInt, EnumC0215d3.k(ofInt), false);
    }
}
