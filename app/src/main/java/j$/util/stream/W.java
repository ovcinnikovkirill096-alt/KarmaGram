package j$.util.stream;

import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

public final class W extends AbstractC0249k2 {
    public final /* synthetic */ int b;
    public final /* synthetic */ AbstractC0201b c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ W(AbstractC0201b abstractC0201b, InterfaceC0279q2 interfaceC0279q2, int i) {
        super(interfaceC0279q2);
        this.b = i;
        this.c = abstractC0201b;
    }

    @Override // j$.util.stream.AbstractC0249k2, j$.util.stream.InterfaceC0279q2
    public void h(long j) {
        switch (this.b) {
            case 5:
                this.a.h(-1L);
                break;
            default:
                super.h(j);
                break;
        }
    }

    @Override // j$.util.stream.InterfaceC0269o2, j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        switch (this.b) {
            case 0:
                this.a.accept(((IntFunction) ((C0290t) this.c).t).apply(i));
                return;
            case 1:
                ((IntConsumer) ((X) this.c).t).accept(i);
                this.a.accept(i);
                return;
            case 2:
                this.a.accept(((IntUnaryOperator) ((X) this.c).t).applyAsInt(i));
                return;
            case 3:
                ((C0305w) this.c).getClass();
                IntToLongFunction intToLongFunction = null;
                intToLongFunction.applyAsLong(i);
                throw null;
            case 4:
                ((C0295u) this.c).getClass();
                IntToDoubleFunction intToDoubleFunction = null;
                intToDoubleFunction.applyAsDouble(i);
                throw null;
            default:
                if (((IntPredicate) ((X) this.c).t).test(i)) {
                    this.a.accept(i);
                    return;
                }
                return;
        }
    }
}
