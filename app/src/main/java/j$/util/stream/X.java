package j$.util.stream;

import java.util.function.IntConsumer;

public final class X extends AbstractC0207c0 {
    public final /* synthetic */ int s;
    public final /* synthetic */ Object t;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ X(AbstractC0201b abstractC0201b, int i, Object obj, int i2) {
        super(abstractC0201b, i);
        this.s = i2;
        this.t = obj;
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        switch (this.s) {
            case 0:
                return new W(this, interfaceC0279q2, 1);
            case 1:
                return new W(this, interfaceC0279q2, 2);
            case 2:
                return new Z(this, interfaceC0279q2);
            case 3:
                return new W(this, interfaceC0279q2, 5);
            case 4:
                return new C0266o(this, interfaceC0279q2, 4);
            default:
                return new C0219e2(this, interfaceC0279q2);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public X(AbstractC0212d0 abstractC0212d0, IntConsumer intConsumer) {
        super(abstractC0212d0, 0);
        this.s = 0;
        this.t = intConsumer;
    }
}
