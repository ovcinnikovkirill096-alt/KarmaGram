package j$.util.stream;

/* JADX INFO: renamed from: j$.util.stream.v2, reason: case insensitive filesystem */
public final class C0303v2 extends AbstractC0254l2 {
    public long b;
    public long c;
    public final /* synthetic */ C0308w2 d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0303v2(C0308w2 c0308w2, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.d = c0308w2;
        this.b = c0308w2.s;
        long j = c0308w2.t;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.AbstractC0254l2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a.h(B2.a(j, this.d.s, this.c));
    }

    @Override // j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final void accept(long j) {
        long j2 = this.b;
        if (j2 == 0) {
            long j3 = this.c;
            if (j3 > 0) {
                this.c = j3 - 1;
                this.a.accept(j);
                return;
            }
            return;
        }
        this.b = j2 - 1;
    }

    @Override // j$.util.stream.AbstractC0254l2, j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        return this.c == 0 || this.a.m();
    }
}
