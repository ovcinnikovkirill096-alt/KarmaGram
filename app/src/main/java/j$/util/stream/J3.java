package j$.util.stream;

public final class J3 implements Runnable {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;
    public final /* synthetic */ Object c;

    public /* synthetic */ J3(int i, Object obj, Object obj2) {
        this.a = i;
        this.b = obj;
        this.c = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.a) {
            case 0:
                try {
                    ((Runnable) this.b).run();
                    ((Runnable) this.c).run();
                    return;
                } catch (Throwable th) {
                    try {
                        ((Runnable) this.c).run();
                        break;
                    } catch (Throwable th2) {
                        try {
                            th.addSuppressed(th2);
                            break;
                        } catch (Throwable unused) {
                        }
                    }
                    throw th;
                }
            default:
                try {
                    ((BaseStream) this.b).close();
                    ((BaseStream) this.c).close();
                    return;
                } catch (Throwable th3) {
                    try {
                        ((BaseStream) this.c).close();
                        break;
                    } catch (Throwable th4) {
                        try {
                            th3.addSuppressed(th4);
                            break;
                        } catch (Throwable unused2) {
                        }
                    }
                    throw th3;
                }
        }
    }
}
