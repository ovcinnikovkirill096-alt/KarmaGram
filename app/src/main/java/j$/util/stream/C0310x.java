package j$.util.stream;

import j$.util.Objects;
import java.util.function.DoubleConsumer;

/* JADX INFO: renamed from: j$.util.stream.x, reason: case insensitive filesystem */
public final class C0310x extends AbstractC0244j2 {
    public boolean b;
    public final j$.util.D c;
    public final /* synthetic */ C0315y d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0310x(C0315y c0315y, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.d = c0315y;
        InterfaceC0279q2 interfaceC0279q3 = this.a;
        Objects.requireNonNull(interfaceC0279q3);
        this.c = new j$.util.D(interfaceC0279q3, 1);
    }

    @Override // j$.util.stream.AbstractC0244j2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a.h(-1L);
    }

    @Override // j$.util.stream.InterfaceC0264n2, j$.util.stream.InterfaceC0279q2
    public final void accept(double d) {
        F f = (F) ((j$.time.t) this.d.t).apply(d);
        if (f != null) {
            try {
                boolean z = this.b;
                j$.util.D d2 = this.c;
                if (!z) {
                    f.sequential().forEach(d2);
                } else {
                    j$.util.U uSpliterator = f.sequential().spliterator();
                    while (!this.a.m() && uSpliterator.tryAdvance((DoubleConsumer) d2)) {
                    }
                }
            } catch (Throwable th) {
                try {
                    f.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        if (f != null) {
            f.close();
        }
    }

    @Override // j$.util.stream.AbstractC0244j2, j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        this.b = true;
        return this.a.m();
    }
}
