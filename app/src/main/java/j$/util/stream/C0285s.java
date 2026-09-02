package j$.util.stream;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;

/* JADX INFO: renamed from: j$.util.stream.s, reason: case insensitive filesystem */
public final class C0285s extends AbstractC0244j2 {
    public final /* synthetic */ int b;
    public final /* synthetic */ AbstractC0201b c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ C0285s(AbstractC0201b abstractC0201b, InterfaceC0279q2 interfaceC0279q2, int i) {
        super(interfaceC0279q2);
        this.b = i;
        this.c = abstractC0201b;
    }

    @Override // j$.util.stream.AbstractC0244j2, j$.util.stream.InterfaceC0279q2
    public void h(long j) {
        switch (this.b) {
            case 4:
                this.a.h(-1L);
                break;
            default:
                super.h(j);
                break;
        }
    }

    @Override // j$.util.stream.InterfaceC0264n2, j$.util.stream.InterfaceC0279q2
    public final void accept(double d) {
        switch (this.b) {
            case 0:
                this.a.accept(((DoubleFunction) ((C0290t) this.c).t).apply(d));
                return;
            case 1:
                ((C0295u) this.c).getClass();
                DoubleUnaryOperator doubleUnaryOperator = null;
                doubleUnaryOperator.applyAsDouble(d);
                throw null;
            case 2:
                ((C0300v) this.c).getClass();
                DoubleToIntFunction doubleToIntFunction = null;
                doubleToIntFunction.applyAsInt(d);
                throw null;
            case 3:
                ((C0305w) this.c).getClass();
                DoubleToLongFunction doubleToLongFunction = null;
                doubleToLongFunction.applyAsLong(d);
                throw null;
            case 4:
                ((C0295u) this.c).getClass();
                DoublePredicate doublePredicate = null;
                doublePredicate.test(d);
                throw null;
            default:
                ((DoubleConsumer) ((C0315y) this.c).t).accept(d);
                this.a.accept(d);
                return;
        }
    }
}
