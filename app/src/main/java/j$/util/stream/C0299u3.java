package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.Consumer;

/* JADX INFO: renamed from: j$.util.stream.u3, reason: case insensitive filesystem */
public final class C0299u3 extends AbstractC0309w3 implements Spliterator.OfInt {
    @Override // j$.util.Spliterator
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.com.android.tools.r8.a.k(this, consumer);
    }

    @Override // j$.util.Spliterator
    public final /* synthetic */ boolean tryAdvance(Consumer consumer) {
        return j$.com.android.tools.r8.a.A(this, consumer);
    }

    @Override // j$.util.stream.AbstractC0319y3
    public final Spliterator a(Spliterator spliterator, long j, long j2, long j3, long j4) {
        return new C0299u3((Spliterator.OfInt) spliterator, j, j2, j3, j4);
    }

    @Override // j$.util.stream.AbstractC0309w3
    public final Object b() {
        return new E0(1);
    }
}
