package j$.util.stream;

public final class R3 extends AbstractC0249k2 {
    public boolean b;
    public final /* synthetic */ S3 c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public R3(S3 s3, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.c = s3;
        this.b = true;
    }

    @Override // j$.util.stream.AbstractC0249k2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a.h(-1L);
    }

    @Override // j$.util.stream.InterfaceC0269o2, j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        if (this.b) {
            boolean zTest = this.c.s.test(i);
            this.b = zTest;
            if (zTest) {
                this.a.accept(i);
            }
        }
    }

    @Override // j$.util.stream.AbstractC0249k2, j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        return !this.b || this.a.m();
    }
}
