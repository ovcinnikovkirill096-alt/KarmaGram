package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

public final class U3 extends AbstractC0202b0 implements Z3 {
    public final /* synthetic */ IntPredicate s;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public U3(AbstractC0212d0 abstractC0212d0, int i, IntPredicate intPredicate) {
        super(abstractC0212d0, i);
        this.s = intPredicate;
    }

    @Override // j$.util.stream.AbstractC0201b
    public final Spliterator C0(AbstractC0201b abstractC0201b, Spliterator spliterator) {
        return EnumC0215d3.ORDERED.n(abstractC0201b.m) ? B0(abstractC0201b, spliterator, new C0217e0(19)).spliterator() : new e4((Spliterator.OfInt) abstractC0201b.v0(spliterator), this.s);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final J0 B0(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, IntFunction intFunction) {
        return (J0) new b4(this, abstractC0322z1, spliterator, intFunction).invoke();
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        return new T3(this, interfaceC0279q2, false);
    }

    @Override // j$.util.stream.Z3
    public final a4 h(B0 b0, boolean z) {
        return new T3(this, b0, z);
    }
}
