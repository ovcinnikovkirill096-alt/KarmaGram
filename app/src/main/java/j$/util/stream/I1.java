package j$.util.stream;

import java.util.function.DoubleBinaryOperator;

public final class I1 extends AbstractC0322z1 {
    public final /* synthetic */ DoubleBinaryOperator h;
    public final /* synthetic */ double i;

    @Override // j$.util.stream.AbstractC0322z1
    public final U1 s0() {
        return new D1(this.i, this.h);
    }

    public I1(EnumC0220e3 enumC0220e3, DoubleBinaryOperator doubleBinaryOperator, double d) {
        this.h = doubleBinaryOperator;
        this.i = d;
    }
}
