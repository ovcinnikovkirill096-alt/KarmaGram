package j$.util.stream;

import j$.util.Spliterator;

/* JADX INFO: renamed from: j$.util.stream.n, reason: case insensitive filesystem */
public final class C0261n extends AbstractC0259m2 {
    public final /* synthetic */ int b = 2;
    public boolean c;
    public Object d;

    public /* synthetic */ C0261n(InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0261n(P3 p3, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.d = p3;
        this.c = true;
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        switch (this.b) {
            case 0:
                this.c = false;
                this.d = null;
                this.a.h(-1L);
                break;
            case 1:
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
                InterfaceC0279q2 interfaceC0279q2 = this.a;
                if (obj == null) {
                    if (this.c) {
                        return;
                    }
                    this.c = true;
                    this.d = null;
                    interfaceC0279q2.v((Object) null);
                    return;
                }
                Object obj2 = this.d;
                if (obj2 == null || !obj.equals(obj2)) {
                    this.d = obj;
                    interfaceC0279q2.v(obj);
                    return;
                }
                return;
            case 1:
                Stream stream = (Stream) ((j$.time.t) ((C0290t) this.d).t).apply(obj);
                if (stream != null) {
                    try {
                        boolean z = this.c;
                        InterfaceC0279q2 interfaceC0279q3 = this.a;
                        if (!z) {
                            ((Stream) stream.sequential()).forEach(interfaceC0279q3);
                        } else {
                            Spliterator spliterator = ((Stream) stream.sequential()).spliterator();
                            while (!interfaceC0279q3.m() && spliterator.tryAdvance(interfaceC0279q3)) {
                            }
                        }
                    } catch (Throwable th) {
                        try {
                            stream.close();
                            break;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                    break;
                }
                if (stream != null) {
                    stream.close();
                    return;
                }
                return;
            default:
                if (this.c) {
                    boolean zTest = ((P3) this.d).t.test(obj);
                    this.c = zTest;
                    if (zTest) {
                        this.a.v(obj);
                        return;
                    }
                    return;
                }
                return;
        }
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public boolean m() {
        switch (this.b) {
            case 1:
                this.c = true;
                return this.a.m();
            case 2:
                return !this.c || this.a.m();
            default:
                return super.m();
        }
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public void end() {
        switch (this.b) {
            case 0:
                this.c = false;
                this.d = null;
                this.a.end();
                break;
            default:
                super.end();
                break;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0261n(C0290t c0290t, InterfaceC0279q2 interfaceC0279q2) {
        super(interfaceC0279q2);
        this.d = c0290t;
    }
}
