package j$.util.stream;

import j$.util.Objects;
import j$.util.function.Consumer$CC;
import java.util.function.Consumer;

/* JADX INFO: renamed from: j$.util.stream.m2, reason: case insensitive filesystem */
public abstract class AbstractC0259m2 implements InterfaceC0279q2 {
    public final InterfaceC0279q2 a;

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

    public AbstractC0259m2(InterfaceC0279q2 interfaceC0279q2) {
        this.a = (InterfaceC0279q2) Objects.requireNonNull(interfaceC0279q2);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public void h(long j) {
        this.a.h(j);
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public void end() {
        this.a.end();
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public boolean m() {
        return this.a.m();
    }
}
