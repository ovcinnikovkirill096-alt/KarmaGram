package j$.util.stream;

public final class Z1 extends AbstractC0199a2 {
    @Override // j$.util.stream.V1, java.util.function.Supplier
    public final Object get() {
        return Long.valueOf(this.b);
    }

    @Override // j$.util.stream.U1
    public final void q(U1 u1) {
        this.b += ((AbstractC0199a2) u1).b;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.b++;
    }
}
