package j$.util.stream;

/* JADX INFO: renamed from: j$.util.stream.x2, reason: case insensitive filesystem */
public final class C0313x2 extends AbstractC0244j2 {
    public long b;
    public long c;
    public final /* synthetic */ C0318y2 d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0313x2(C0318y2 c0318y2, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.d = c0318y2;
        this.b = c0318y2.s;
        long j = c0318y2.t;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.AbstractC0244j2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a.h(B2.a(j, this.d.s, this.c));
    }

    @Override // j$.util.stream.InterfaceC0264n2, j$.util.stream.InterfaceC0279q2
    public final void accept(double d) {
        long j = this.b;
        if (j == 0) {
            long j2 = this.c;
            if (j2 > 0) {
                this.c = j2 - 1;
                this.a.accept(d);
                return;
            }
            return;
        }
        this.b = j - 1;
    }

    @Override // j$.util.stream.AbstractC0244j2, j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        return this.c == 0 || this.a.m();
    }
}
