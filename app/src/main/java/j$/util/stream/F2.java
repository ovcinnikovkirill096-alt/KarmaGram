package j$.util.stream;

import java.util.Comparator;

public abstract class F2 extends AbstractC0259m2 {
    public final Comparator b;
    public boolean c;

    public F2(InterfaceC0279q2 interfaceC0279q2, Comparator comparator) {
        super(interfaceC0279q2);
        this.b = comparator;
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        this.c = true;
        return false;
    }
}
