package j$.util.stream;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

public final class L1 extends AbstractC0322z1 {
    public final /* synthetic */ BinaryOperator h;
    public final /* synthetic */ BiConsumer i;
    public final /* synthetic */ Supplier j;
    public final /* synthetic */ Collector k;

    @Override // j$.util.stream.AbstractC0322z1
    public final U1 s0() {
        return new M1(this.j, this.i, this.h);
    }

    @Override // j$.util.stream.AbstractC0322z1, j$.util.stream.M3
    public final int s() {
        if (this.k.characteristics().contains(EnumC0231h.UNORDERED)) {
            return EnumC0215d3.r;
        }
        return 0;
    }

    public L1(EnumC0220e3 enumC0220e3, BinaryOperator binaryOperator, BiConsumer biConsumer, Supplier supplier, Collector collector) {
        this.h = binaryOperator;
        this.i = biConsumer;
        this.j = supplier;
        this.k = collector;
    }
}
