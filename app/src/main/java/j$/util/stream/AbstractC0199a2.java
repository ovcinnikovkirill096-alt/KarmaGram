package j$.util.stream;

import j$.util.function.Consumer$CC;
import java.util.function.Consumer;

/* JADX INFO: renamed from: j$.util.stream.a2, reason: case insensitive filesystem */
public abstract class AbstractC0199a2 extends V1 implements U1 {
    public long b;

    public /* synthetic */ void accept(double d) {
        AbstractC0322z1.z();
        throw null;
    }

    public /* synthetic */ void accept(int i) {
        AbstractC0322z1.G();
        throw null;
    }

    public /* synthetic */ void accept(long j) {
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

    @Override // j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.b = 0L;
    }
}
