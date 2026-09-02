package j$.util.stream;

import j$.util.DesugarArrays;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public final class X2 extends Z2 implements j$.util.Z {
    public final /* synthetic */ Y2 g;

    @Override // j$.util.Spliterator
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.com.android.tools.r8.a.l(this, consumer);
    }

    @Override // j$.util.Spliterator
    public final /* synthetic */ boolean tryAdvance(Consumer consumer) {
        return j$.com.android.tools.r8.a.B(this, consumer);
    }

    @Override // j$.util.stream.Z2
    public final void a(int i, Object obj, Object obj2) {
        ((LongConsumer) obj2).accept(((long[]) obj)[i]);
    }

    @Override // j$.util.stream.Z2
    public final j$.util.c0 b(Object obj, int i, int i2) {
        return DesugarArrays.b((long[]) obj, i, i2 + i);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public X2(Y2 y2, int i, int i2, int i3, int i4) {
        super(y2, i, i2, i3, i4);
        this.g = y2;
    }

    @Override // j$.util.stream.Z2
    public final j$.util.c0 c(int i, int i2, int i3, int i4) {
        return new X2(this.g, i, i2, i3, i4);
    }
}
