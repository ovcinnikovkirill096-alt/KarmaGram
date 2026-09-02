package j$.util.stream;

public final class Q3 extends AbstractC0259m2 implements a4 {
    public long b;
    public boolean c;
    public final /* synthetic */ boolean d;
    public final /* synthetic */ P3 e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Q3(P3 p3, InterfaceC0279q2 interfaceC0279q2, boolean z) {
        super(interfaceC0279q2);
        this.e = p3;
        this.d = z;
    }

    /* JADX WARN: Code duplicated, block: B:8:0x0015  */
    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        boolean z;
        if (this.c) {
            z = true;
        } else {
            boolean zTest = this.e.t.test(obj);
            this.c = !zTest;
            if (zTest) {
                z = false;
            } else {
                z = true;
            }
        }
        boolean z2 = this.d;
        if (z2 && !z) {
            this.b++;
        }
        if (z2 || z) {
            this.a.accept(obj);
        }
    }

    @Override // j$.util.stream.a4
    public final long n() {
        return this.b;
    }
}
