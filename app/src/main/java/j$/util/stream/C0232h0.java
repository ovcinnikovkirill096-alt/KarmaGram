package j$.util.stream;

import j$.util.Objects;
import java.util.function.LongConsumer;

/* JADX INFO: renamed from: j$.util.stream.h0, reason: case insensitive filesystem */
public final class C0232h0 extends AbstractC0254l2 {
    public boolean b;
    public final j$.util.L c;
    public final /* synthetic */ C0237i0 d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0232h0(C0237i0 c0237i0, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.d = c0237i0;
        InterfaceC0279q2 interfaceC0279q3 = this.a;
        Objects.requireNonNull(interfaceC0279q3);
        this.c = new j$.util.L(interfaceC0279q3, 1);
    }

    @Override // j$.util.stream.AbstractC0254l2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        this.a.h(-1L);
    }

    @Override // j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final void accept(long j) {
        LongStream longStream = (LongStream) ((j$.time.t) this.d.t).apply(j);
        if (longStream != null) {
            try {
                boolean z = this.b;
                j$.util.L l = this.c;
                if (!z) {
                    longStream.sequential().forEach(l);
                } else {
                    j$.util.Z zSpliterator = longStream.sequential().spliterator();
                    while (!this.a.m() && zSpliterator.tryAdvance((LongConsumer) l)) {
                    }
                }
            } catch (Throwable th) {
                try {
                    longStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        if (longStream != null) {
            longStream.close();
        }
    }

    @Override // j$.util.stream.AbstractC0254l2, j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        this.b = true;
        return this.a.m();
    }
}
