package j$.util.stream;

import j$.util.Optional;
import j$.util.function.Consumer$CC;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;

public final class K1 implements U1 {
    public boolean a;
    public Object b;
    public final /* synthetic */ BinaryOperator c;

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ void accept(double d) {
        AbstractC0322z1.z();
        throw null;
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ void accept(int i) {
        AbstractC0322z1.G();
        throw null;
    }

    @Override // j$.util.stream.InterfaceC0279q2, j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final /* synthetic */ void accept(long j) {
        AbstractC0322z1.H();
        throw null;
    }

    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer$CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ void end() {
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ boolean m() {
        return false;
    }

    public K1(BinaryOperator binaryOperator) {
        this.c = binaryOperator;
    }

    @Override // j$.util.stream.U1
    public final void q(U1 u1) {
        K1 k1 = (K1) u1;
        if (k1.a) {
            return;
        }
        v(k1.b);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a = true;
        this.b = null;
    }

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final void v(Object obj) {
        if (this.a) {
            this.a = false;
            this.b = obj;
        } else {
            this.b = this.c.apply(this.b, obj);
        }
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        return this.a ? Optional.empty() : Optional.of(this.b);
    }
}
