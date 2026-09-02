package j$.util.stream;

import j$.util.Spliterator;
import j$.util.function.Consumer$CC;
import java.util.function.Consumer;

public abstract class T implements M3, N3 {
    public final boolean a;

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
    public final /* synthetic */ void h(long j) {
    }

    @Override // j$.util.stream.InterfaceC0279q2
    public final /* synthetic */ boolean m() {
        return false;
    }

    public T(boolean z) {
        this.a = z;
    }

    @Override // j$.util.stream.M3
    public final int s() {
        if (this.a) {
            return 0;
        }
        return EnumC0215d3.r;
    }

    public final void a(AbstractC0322z1 abstractC0322z1, Spliterator spliterator) {
        if (this.a) {
            new U(abstractC0322z1, spliterator, this).invoke();
        } else {
            new V(abstractC0322z1, spliterator, abstractC0322z1.u0(this)).invoke();
        }
    }
}
