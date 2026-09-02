package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;
import java.util.function.IntConsumer;

/* JADX INFO: renamed from: j$.util.stream.e2, reason: case insensitive filesystem */
public final class C0219e2 extends AbstractC0259m2 {
    public boolean b;
    public final j$.util.H c;
    public final /* synthetic */ X d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0219e2(X x, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.d = x;
        InterfaceC0279q2 interfaceC0279q3 = this.a;
        Objects.requireNonNull(interfaceC0279q3);
        this.c = new j$.util.H(interfaceC0279q3, 1);
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a.h(-1L);
    }

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final void v(Object obj) {
        IntStream intStream = (IntStream) ((j$.time.t) this.d.t).apply(obj);
        if (intStream != null) {
            try {
                boolean z = this.b;
                j$.util.H h = this.c;
                if (!z) {
                    intStream.sequential().forEach(h);
                } else {
                    Spliterator.OfInt ofIntSpliterator = intStream.sequential().spliterator();
                    while (!this.a.m() && ofIntSpliterator.tryAdvance((IntConsumer) h)) {
                    }
                }
            } catch (Throwable th) {
                try {
                    intStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        if (intStream != null) {
            intStream.close();
        }
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        this.b = true;
        return this.a.m();
    }
}
