package j$.util.stream;

import java.util.function.LongBinaryOperator;

public final class A1 extends AbstractC0322z1 {
    public final /* synthetic */ LongBinaryOperator h;
    public final /* synthetic */ long i;

    @Override // j$.util.stream.AbstractC0322z1
    public final U1 s0() {
        return new S1(this.i, this.h);
    }

    public A1(EnumC0220e3 enumC0220e3, LongBinaryOperator longBinaryOperator, long j) {
        this.h = longBinaryOperator;
        this.i = j;
    }
}
