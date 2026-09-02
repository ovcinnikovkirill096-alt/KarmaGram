package j$.util.stream;

import j$.util.Spliterator;
import j$.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public final class V2 extends Z2 implements Spliterator.OfInt {
    public final /* synthetic */ W2 g;

    @Override // j$.util.Spliterator
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.com.android.tools.r8.a.k(this, consumer);
    }

    @Override // j$.util.Spliterator
    public final /* synthetic */ boolean tryAdvance(Consumer consumer) {
        return j$.com.android.tools.r8.a.A(this, consumer);
    }

    @Override // j$.util.stream.Z2
    public final void a(int i, Object obj, Object obj2) {
        ((IntConsumer) obj2).accept(((int[]) obj)[i]);
    }

    @Override // j$.util.stream.Z2
    public final j$.util.c0 b(Object obj, int i, int i2) {
        return Spliterators.spliterator((int[]) obj, i, i2 + i, 1040);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public V2(W2 w2, int i, int i2, int i3, int i4) {
        super(w2, i, i2, i3, i4);
        this.g = w2;
    }

    @Override // j$.util.stream.Z2
    public final j$.util.c0 c(int i, int i2, int i3, int i4) {
        return new V2(this.g, i, i2, i3, i4);
    }
}
