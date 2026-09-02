package j$.util.stream;

import j$.util.function.Consumer$CC;
import java.util.function.Consumer;

/* JADX INFO: renamed from: j$.util.stream.v0, reason: case insensitive filesystem */
public abstract class AbstractC0301v0 implements InterfaceC0279q2 {
    public boolean a;
    public boolean b;

    @Override // j$.util.stream.InterfaceC0279q2
    public /* synthetic */ void accept(double d) {
        AbstractC0322z1.z();
        throw null;
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public /* synthetic */ void accept(int i) {
        AbstractC0322z1.G();
        throw null;
    }

    @Override // j$.util.stream.InterfaceC0279q2, j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
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
    public final /* synthetic */ void h(long j) {
    }

    public AbstractC0301v0(EnumC0306w0 enumC0306w0) {
        this.b = !enumC0306w0.b;
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        return this.a;
    }
}
