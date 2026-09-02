package j$.util.stream;

import java.util.function.DoubleConsumer;

/* JADX INFO: renamed from: j$.util.stream.y, reason: case insensitive filesystem */
public final class C0315y extends B {
    public final /* synthetic */ int s;
    public final /* synthetic */ Object t;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ C0315y(AbstractC0201b abstractC0201b, int i, Object obj, int i2) {
        super(abstractC0201b, i);
        this.s = i2;
        this.t = obj;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0315y(C c, DoubleConsumer doubleConsumer) {
        super(c, 0);
        this.s = 1;
        this.t = doubleConsumer;
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        switch (this.s) {
            case 0:
                return new C0310x(this, interfaceC0279q2);
            case 1:
                return new C0285s(this, interfaceC0279q2, 5);
            case 2:
                return new C0266o(this, interfaceC0279q2, 6);
            default:
                return new C0209c2(this, interfaceC0279q2);
        }
    }
}
