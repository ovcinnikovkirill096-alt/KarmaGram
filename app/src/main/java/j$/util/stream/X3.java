package j$.util.stream;

import java.util.function.DoublePredicate;

public final class X3 extends AbstractC0244j2 {
    public final boolean b;

    public X3(J2 j2, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.b = true;
    }

    @Override // j$.util.stream.AbstractC0244j2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a.h(-1L);
    }

    @Override // j$.util.stream.InterfaceC0264n2, j$.util.stream.InterfaceC0279q2
    public final void accept(double d) {
        if (this.b) {
            DoublePredicate doublePredicate = null;
            doublePredicate.test(d);
            throw null;
        }
    }

    @Override // j$.util.stream.AbstractC0244j2, j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        return !this.b || this.a.m();
    }
}
