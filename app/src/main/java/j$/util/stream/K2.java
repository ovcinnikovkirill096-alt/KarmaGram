package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;
import java.util.Arrays;
import java.util.function.IntFunction;

public final class K2 extends AbstractC0202b0 {
    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        Objects.requireNonNull(interfaceC0279q2);
        if (EnumC0215d3.SORTED.n(i)) {
            return interfaceC0279q2;
        }
        return EnumC0215d3.SIZED.n(i) ? new P2(interfaceC0279q2) : new H2(interfaceC0279q2);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final J0 B0(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, IntFunction intFunction) {
        if (EnumC0215d3.SORTED.n(((AbstractC0201b) abstractC0322z1).m)) {
            return abstractC0322z1.d0(spliterator, false, intFunction);
        }
        int[] iArr = (int[]) ((F0) abstractC0322z1.d0(spliterator, true, intFunction)).b();
        Arrays.sort(iArr);
        return new C0218e1(iArr);
    }
}
