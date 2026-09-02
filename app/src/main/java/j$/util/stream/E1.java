package j$.util.stream;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;

public final class E1 extends AbstractC0322z1 {
    public final /* synthetic */ int h;
    public final /* synthetic */ Object i;
    public final /* synthetic */ Object j;
    public final /* synthetic */ Object k;

    public /* synthetic */ E1(EnumC0220e3 enumC0220e3, Object obj, Object obj2, Object obj3, int i) {
        this.h = i;
        this.j = obj;
        this.k = obj2;
        this.i = obj3;
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final U1 s0() {
        switch (this.h) {
            case 0:
                return new B1((Supplier) this.i, (ObjLongConsumer) this.k, (r) this.j);
            case 1:
                return new H1((Supplier) this.i, (ObjDoubleConsumer) this.k, (r) this.j);
            case 2:
                return new J1(this.i, (BiFunction) this.k, (BinaryOperator) this.j);
            case 3:
                return new N1((Supplier) this.i, (BiConsumer) this.k, (BiConsumer) this.j);
            default:
                return new R1((Supplier) this.i, (ObjIntConsumer) this.k, (r) this.j);
        }
    }
}
