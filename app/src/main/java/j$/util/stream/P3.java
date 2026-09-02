package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public final class P3 extends AbstractC0229g2 implements Z3 {
    public final /* synthetic */ int s;
    public final /* synthetic */ Predicate t;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ P3(AbstractC0239i2 abstractC0239i2, int i, Predicate predicate, int i2) {
        super(abstractC0239i2, i);
        this.s = i2;
        this.t = predicate;
    }

    @Override // j$.util.stream.AbstractC0201b
    public final Spliterator C0(AbstractC0201b abstractC0201b, Spliterator spliterator) {
        switch (this.s) {
            case 0:
                return EnumC0215d3.ORDERED.n(abstractC0201b.m) ? B0(abstractC0201b, spliterator, new C0217e0(4)).spliterator() : new i4(abstractC0201b.v0(spliterator), this.t, 1);
            default:
                return EnumC0215d3.ORDERED.n(abstractC0201b.m) ? B0(abstractC0201b, spliterator, new C0217e0(4)).spliterator() : new i4(abstractC0201b.v0(spliterator), this.t, 0);
        }
    }

    @Override // j$.util.stream.AbstractC0201b
    public final J0 B0(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, IntFunction intFunction) {
        switch (this.s) {
            case 0:
                return (J0) new c4(this, abstractC0322z1, spliterator, intFunction).invoke();
            default:
                return (J0) new b4(this, abstractC0322z1, spliterator, intFunction).invoke();
        }
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        switch (this.s) {
            case 0:
                return new C0261n(this, interfaceC0279q2);
            default:
                return new Q3(this, interfaceC0279q2, false);
        }
    }

    @Override // j$.util.stream.Z3
    public a4 h(B0 b0, boolean z) {
        return new Q3(this, b0, z);
    }
}
