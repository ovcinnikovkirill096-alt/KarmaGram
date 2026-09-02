package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.Consumer;

public final class S extends T {
    public final Consumer b;

    @Override // java.util.function.Supplier
    public final /* bridge */ /* synthetic */ Object get() {
        return null;
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

    public S(Consumer consumer, boolean z) {
        super(z);
        this.b = consumer;
    }

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final void v(Object obj) {
        this.b.v(obj);
    }
}
