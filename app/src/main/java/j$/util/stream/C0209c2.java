package j$.util.stream;

import j$.util.Objects;
import java.util.function.DoubleConsumer;
import java.util.function.LongConsumer;

/* JADX INFO: renamed from: j$.util.stream.c2, reason: case insensitive filesystem */
public final class C0209c2 extends AbstractC0259m2 {
    public final /* synthetic */ int b = 0;
    public boolean c;
    public final Object d;
    public final /* synthetic */ AbstractC0201b e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0209c2(C0315y c0315y, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.e = c0315y;
        InterfaceC0279q2 interfaceC0279q3 = this.a;
        Objects.requireNonNull(interfaceC0279q3);
        this.d = new j$.util.D(interfaceC0279q3, 1);
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        switch (this.b) {
            case 0:
                this.a.h(-1L);
                break;
            default:
                this.a.h(-1L);
                break;
        }
    }

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final void v(Object obj) {
        switch (this.b) {
            case 0:
                j$.util.L l = (j$.util.L) this.d;
                LongStream longStream = (LongStream) ((j$.time.t) ((C0237i0) this.e).t).apply(obj);
                if (longStream != null) {
                    try {
                        if (!this.c) {
                            longStream.sequential().forEach(l);
                        } else {
                            j$.util.Z zSpliterator = longStream.sequential().spliterator();
                            while (!this.a.m() && zSpliterator.tryAdvance((LongConsumer) l)) {
                            }
                        }
                    } catch (Throwable th) {
                        try {
                            longStream.close();
                            break;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                    break;
                }
                if (longStream != null) {
                    longStream.close();
                    return;
                }
                return;
            default:
                j$.util.D d = (j$.util.D) this.d;
                F f = (F) ((j$.time.t) ((C0315y) this.e).t).apply(obj);
                if (f != null) {
                    try {
                        if (!this.c) {
                            f.sequential().forEach(d);
                        } else {
                            j$.util.U uSpliterator = f.sequential().spliterator();
                            while (!this.a.m() && uSpliterator.tryAdvance((DoubleConsumer) d)) {
                            }
                        }
                    } catch (Throwable th3) {
                        try {
                            f.close();
                            break;
                        } catch (Throwable th4) {
                            th3.addSuppressed(th4);
                        }
                        throw th3;
                    }
                    break;
                }
                if (f != null) {
                    f.close();
                    return;
                }
                return;
        }
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public final boolean m() {
        switch (this.b) {
            case 0:
                this.c = true;
                break;
            default:
                this.c = true;
                break;
        }
        return this.a.m();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0209c2(C0237i0 c0237i0, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.e = c0237i0;
        InterfaceC0279q2 interfaceC0279q3 = this.a;
        Objects.requireNonNull(interfaceC0279q3);
        this.d = new j$.util.L(interfaceC0279q3, 1);
    }
}
