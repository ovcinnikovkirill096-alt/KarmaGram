package j$.util.stream;

import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;

public final class C1 extends AbstractC0322z1 {
    public final /* synthetic */ int h;
    public final /* synthetic */ Object i;

    public /* synthetic */ C1(EnumC0220e3 enumC0220e3, Object obj, int i) {
        this.h = i;
        this.i = obj;
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final U1 s0() {
        switch (this.h) {
            case 0:
                return new T1((LongBinaryOperator) this.i);
            case 1:
                return new F1((DoubleBinaryOperator) this.i);
            case 2:
                return new K1((BinaryOperator) this.i);
            default:
                return new Q1((IntBinaryOperator) this.i);
        }
    }
}
