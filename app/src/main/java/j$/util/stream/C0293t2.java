package j$.util.stream;

/* JADX INFO: renamed from: j$.util.stream.t2, reason: case insensitive filesystem */
public final class C0293t2 extends AbstractC0249k2 {
    public long b;
    public long c;
    public final /* synthetic */ C0298u2 d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0293t2(C0298u2 c0298u2, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.d = c0298u2;
        this.b = c0298u2.s;
        long j = c0298u2.t;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.AbstractC0249k2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a.h(B2.a(j, this.d.s, this.c));
    }

    @Override // j$.util.stream.InterfaceC0269o2, j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        long j = this.b;
        if (j == 0) {
            long j2 = this.c;
            if (j2 > 0) {
                this.c = j2 - 1;
                this.a.accept(i);
                return;
            }
            return;
        }
        this.b = j - 1;
    }

    @Override // j$.util.stream.AbstractC0249k2, j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        return this.c == 0 || this.a.m();
    }
}
