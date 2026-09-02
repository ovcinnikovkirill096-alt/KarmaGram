package j$.util.stream;

import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;

/* JADX INFO: renamed from: j$.util.stream.f0, reason: case insensitive filesystem */
public final class C0222f0 extends AbstractC0254l2 {
    public final /* synthetic */ int b;
    public final /* synthetic */ AbstractC0201b c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ C0222f0(AbstractC0201b abstractC0201b, InterfaceC0279q2 interfaceC0279q2, int i) {
        super(interfaceC0279q2);
        this.b = i;
        this.c = abstractC0201b;
    }

    @Override // j$.util.stream.AbstractC0254l2, j$.util.stream.InterfaceC0279q2
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

    @Override // j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final void accept(long j) {
        switch (this.b) {
            case 0:
                this.a.accept(((LongFunction) ((C0290t) this.c).t).apply(j));
                return;
            case 1:
                ((C0305w) this.c).getClass();
                LongUnaryOperator longUnaryOperator = null;
                longUnaryOperator.applyAsLong(j);
                throw null;
            case 2:
                ((C0300v) this.c).getClass();
                LongToIntFunction longToIntFunction = null;
                longToIntFunction.applyAsInt(j);
                throw null;
            case 3:
                ((C0295u) this.c).getClass();
                LongToDoubleFunction longToDoubleFunction = null;
                longToDoubleFunction.applyAsDouble(j);
                throw null;
            case 4:
                ((C0305w) this.c).getClass();
                LongPredicate longPredicate = null;
                longPredicate.test(j);
                throw null;
            default:
                ((LongConsumer) ((C0237i0) this.c).t).accept(j);
                this.a.accept(j);
                return;
        }
    }
}
