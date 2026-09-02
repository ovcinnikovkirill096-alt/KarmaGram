package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.IntFunction;

public final class M2 extends AbstractC0229g2 {
    public final boolean s;
    public final Comparator t;

    public M2(AbstractC0239i2 abstractC0239i2) {
        super(abstractC0239i2, EnumC0215d3.q | EnumC0215d3.o);
        this.s = true;
        this.t = j$.util.Comparator.CC.naturalOrder();
    }

    public M2(AbstractC0239i2 abstractC0239i2, Comparator comparator) {
        super(abstractC0239i2, EnumC0215d3.q | EnumC0215d3.p);
        this.s = false;
        this.t = (Comparator) Objects.requireNonNull(comparator);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        Objects.requireNonNull(interfaceC0279q2);
        if (EnumC0215d3.SORTED.n(i) && this.s) {
            return interfaceC0279q2;
        }
        if (EnumC0215d3.SIZED.n(i)) {
            return new R2(interfaceC0279q2, this.t);
        }
        return new N2(interfaceC0279q2, this.t);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final J0 B0(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, IntFunction intFunction) {
        if (EnumC0215d3.SORTED.n(((AbstractC0201b) abstractC0322z1).m) && this.s) {
            return abstractC0322z1.d0(spliterator, false, intFunction);
        }
        Object[] objArrG = abstractC0322z1.d0(spliterator, true, intFunction).g(intFunction);
        Arrays.sort(objArrG, this.t);
        return new M0(objArrG);
    }
}
