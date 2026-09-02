package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.LongConsumer;

public final class Q extends T implements InterfaceC0274p2 {
    public final LongConsumer b;

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final /* bridge */ /* synthetic */ void v(Object obj) {
        u((Long) obj);
    }

    public final /* synthetic */ LongConsumer andThen(LongConsumer longConsumer) {
        return j$.com.android.tools.r8.a.d(this, longConsumer);
    }

    @Override // java.util.function.Supplier
    public final /* bridge */ /* synthetic */ Object get() {
        return null;
    }

    @Override // j$.util.stream.InterfaceC0274p2
    public final /* synthetic */ void u(Long l) {
        AbstractC0322z1.E(this, l);
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

    public Q(LongConsumer longConsumer, boolean z) {
        super(z);
        this.b = longConsumer;
    }

    @Override // j$.util.stream.T, j$.util.stream.InterfaceC0279q2, j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final void accept(long j) {
        this.b.accept(j);
    }
}
