package j$.util.stream;

public final class Y extends AbstractC0249k2 {
    public final /* synthetic */ int b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ Y(int i, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.b = i;
    }

    @Override // j$.util.stream.InterfaceC0269o2, j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        switch (this.b) {
            case 0:
                this.a.accept(i);
                break;
            default:
                this.a.accept(i);
                break;
        }
    }
}
