package j$.util.stream;

/* JADX INFO: renamed from: j$.util.stream.u, reason: case insensitive filesystem */
public final class C0295u extends B {
    public final /* synthetic */ int s;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ C0295u(AbstractC0201b abstractC0201b, int i, int i2) {
        super(abstractC0201b, i);
        this.s = i2;
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        switch (this.s) {
            case 0:
                return new C0285s(this, interfaceC0279q2, 1);
            case 1:
                return interfaceC0279q2;
            case 2:
                return new C0285s(this, interfaceC0279q2, 4);
            case 3:
                return new Y(1, interfaceC0279q2);
            case 4:
                return new W(this, interfaceC0279q2, 4);
            case 5:
                return new C0227g0(interfaceC0279q2);
            default:
                return new C0222f0(this, interfaceC0279q2, 3);
        }
    }
}
