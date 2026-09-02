package j$.util.stream;

import java.util.function.LongConsumer;

/* JADX INFO: renamed from: j$.util.stream.i0, reason: case insensitive filesystem */
public final class C0237i0 extends AbstractC0252l0 {
    public final /* synthetic */ int s;
    public final /* synthetic */ Object t;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ C0237i0(AbstractC0201b abstractC0201b, int i, Object obj, int i2) {
        super(abstractC0201b, i);
        this.s = i2;
        this.t = obj;
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        switch (this.s) {
            case 0:
                return new C0232h0(this, interfaceC0279q2);
            case 1:
                return new C0222f0(this, interfaceC0279q2, 5);
            case 2:
                return new C0209c2(this, interfaceC0279q2);
            default:
                return new C0266o(this, interfaceC0279q2, 5);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0237i0(AbstractC0257m0 abstractC0257m0, LongConsumer longConsumer) {
        super(abstractC0257m0, 0);
        this.s = 1;
        this.t = longConsumer;
    }
}
