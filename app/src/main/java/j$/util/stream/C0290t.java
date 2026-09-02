package j$.util.stream;

import java.util.function.Consumer;

/* JADX INFO: renamed from: j$.util.stream.t, reason: case insensitive filesystem */
public final class C0290t extends AbstractC0234h2 {
    public final /* synthetic */ int s;
    public final /* synthetic */ Object t;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ C0290t(AbstractC0201b abstractC0201b, int i, Object obj, int i2) {
        super(abstractC0201b, i);
        this.s = i2;
        this.t = obj;
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        switch (this.s) {
            case 0:
                return new C0285s(this, interfaceC0279q2, 0);
            case 1:
                return new W(this, interfaceC0279q2, 0);
            case 2:
                return new C0222f0(this, interfaceC0279q2, 0);
            case 3:
                return new C0266o(this, interfaceC0279q2, 1);
            case 4:
                return new C0266o(this, interfaceC0279q2, 2);
            case 5:
                return new C0266o(this, interfaceC0279q2, 3);
            default:
                return new C0261n(this, interfaceC0279q2);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0290t(AbstractC0239i2 abstractC0239i2, Consumer consumer) {
        super(abstractC0239i2, 0);
        this.s = 3;
        this.t = consumer;
    }
}
