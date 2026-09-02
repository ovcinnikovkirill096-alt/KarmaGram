package j$.util.stream;

public final class T3 extends AbstractC0249k2 implements a4 {
    public long b;
    public boolean c;
    public final /* synthetic */ boolean d;
    public final /* synthetic */ U3 e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public T3(U3 u3, InterfaceC0279q2 interfaceC0279q2, boolean z) {
        super(interfaceC0279q2);
        this.e = u3;
        this.d = z;
    }

    /* JADX WARN: Code duplicated, block: B:8:0x0015  */
    @Override // j$.util.stream.InterfaceC0269o2, j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        boolean z;
        if (this.c) {
            z = true;
        } else {
            boolean zTest = this.e.s.test(i);
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
            this.a.accept(i);
        }
    }

    @Override // j$.util.stream.a4
    public final long n() {
        return this.b;
    }
}
