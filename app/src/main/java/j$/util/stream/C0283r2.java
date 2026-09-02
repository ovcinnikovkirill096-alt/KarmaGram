package j$.util.stream;

/* JADX INFO: renamed from: j$.util.stream.r2, reason: case insensitive filesystem */
public final class C0283r2 extends AbstractC0259m2 {
    public long b;
    public long c;
    public final /* synthetic */ C0288s2 d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0283r2(C0288s2 c0288s2, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.d = c0288s2;
        this.b = c0288s2.s;
        long j = c0288s2.t;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a.h(B2.a(j, this.d.s, this.c));
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        long j = this.b;
        if (j == 0) {
            long j2 = this.c;
            if (j2 > 0) {
                this.c = j2 - 1;
                this.a.accept(obj);
                return;
            }
            return;
        }
        this.b = j - 1;
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        return this.c == 0 || this.a.m();
    }
}
