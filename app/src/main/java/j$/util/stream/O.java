package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.DoubleConsumer;

public final class O extends T implements InterfaceC0264n2 {
    public final DoubleConsumer b;

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final /* bridge */ /* synthetic */ void v(Object obj) {
        v((Double) obj);
    }

    public final /* synthetic */ DoubleConsumer andThen(DoubleConsumer doubleConsumer) {
        return j$.com.android.tools.r8.a.b(this, doubleConsumer);
    }

    @Override // java.util.function.Supplier
    public final /* bridge */ /* synthetic */ Object get() {
        return null;
    }

    @Override // j$.util.stream.InterfaceC0264n2
    public final /* synthetic */ void v(Double d) {
        AbstractC0322z1.A(this, d);
    }

    @Override // j$.util.stream.M3
    public final Object f(AbstractC0201b abstractC0201b, Spliterator spliterator) {
        abstractC0201b.t0(spliterator, this);
        return null;
    }

    @Override // j$.util.stream.M3
    public final /* bridge */ /* synthetic */ Object i(AbstractC0322z1 abstractC0322z1, Spliterator spliterator) {
        a(abstractC0322z1, spliterator);
        return null;
    }

    public O(DoubleConsumer doubleConsumer, boolean z) {
        super(z);
        this.b = doubleConsumer;
    }

    @Override // j$.util.stream.T, j$.util.stream.InterfaceC0279q2
    public final void accept(double d) {
        this.b.accept(d);
    }
}
