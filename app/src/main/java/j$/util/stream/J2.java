package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;
import java.util.Arrays;
import java.util.function.IntFunction;

public final class J2 extends A implements Z3 {
    public final /* synthetic */ int s;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ J2(AbstractC0201b abstractC0201b, int i, int i2) {
        super(abstractC0201b, i);
        this.s = i2;
    }

    @Override // j$.util.stream.AbstractC0201b
    public Spliterator C0(AbstractC0201b abstractC0201b, Spliterator spliterator) {
        switch (this.s) {
            case 1:
                return EnumC0215d3.ORDERED.n(abstractC0201b.m) ? B0(abstractC0201b, spliterator, new C0217e0(22)).spliterator() : new d4((j$.util.U) abstractC0201b.v0(spliterator), 1);
            case 2:
                return EnumC0215d3.ORDERED.n(abstractC0201b.m) ? B0(abstractC0201b, spliterator, new C0217e0(23)).spliterator() : new d4((j$.util.U) abstractC0201b.v0(spliterator), 0);
            default:
                return super.C0(abstractC0201b, spliterator);
        }
    }

    @Override // j$.util.stream.AbstractC0201b
    public final J0 B0(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, IntFunction intFunction) {
        switch (this.s) {
            case 0:
                if (EnumC0215d3.SORTED.n(((AbstractC0201b) abstractC0322z1).m)) {
                    return abstractC0322z1.d0(spliterator, false, intFunction);
                }
                double[] dArr = (double[]) ((D0) abstractC0322z1.d0(spliterator, true, intFunction)).b();
                Arrays.sort(dArr);
                return new V0(dArr);
            case 1:
                return (J0) new c4(this, abstractC0322z1, spliterator, intFunction).invoke();
            default:
                return (J0) new b4(this, abstractC0322z1, spliterator, intFunction).invoke();
        }
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        switch (this.s) {
            case 0:
                Objects.requireNonNull(interfaceC0279q2);
                if (EnumC0215d3.SORTED.n(i)) {
                    return interfaceC0279q2;
                }
                return EnumC0215d3.SIZED.n(i) ? new O2(interfaceC0279q2) : new G2(interfaceC0279q2);
            case 1:
                return new X3(this, interfaceC0279q2);
            default:
                return new Y3(this, interfaceC0279q2, false);
        }
    }

    @Override // j$.util.stream.Z3
    public a4 h(B0 b0, boolean z) {
        return new Y3(this, b0, z);
    }
}
