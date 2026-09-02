package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.IntConsumer;

public final class P extends T implements InterfaceC0269o2 {
    public final IntConsumer b;

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final /* bridge */ /* synthetic */ void v(Object obj) {
        l((Integer) obj);
    }

    public final /* synthetic */ IntConsumer andThen(IntConsumer intConsumer) {
        return j$.com.android.tools.r8.a.c(this, intConsumer);
    }

    @Override // java.util.function.Supplier
    public final /* bridge */ /* synthetic */ Object get() {
        return null;
    }

    @Override // j$.util.stream.InterfaceC0269o2
    public final /* synthetic */ void l(Integer num) {
        AbstractC0322z1.C(this, num);
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

    public P(IntConsumer intConsumer, boolean z) {
        super(z);
        this.b = intConsumer;
    }

    @Override // j$.util.stream.T, j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        this.b.accept(i);
    }
}
