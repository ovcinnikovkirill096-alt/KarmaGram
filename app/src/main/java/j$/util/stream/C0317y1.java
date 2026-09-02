package j$.util.stream;

import java.util.concurrent.CountedCompleter;

/* JADX INFO: renamed from: j$.util.stream.y1, reason: case insensitive filesystem */
public class C0317y1 extends CountedCompleter {
    public final J0 a;
    public final int b;
    public final /* synthetic */ int c;
    public final Object d;

    public C0317y1(J0 j0, Object obj, int i) {
        this.c = i;
        this.a = j0;
        this.b = 0;
        this.d = obj;
    }

    public C0317y1(C0317y1 c0317y1, J0 j0, int i, byte b) {
        super(c0317y1);
        this.a = j0;
        this.b = i;
    }

    @Override // java.util.concurrent.CountedCompleter
    public final void compute() {
        C0317y1 c0317y1A = this;
        while (c0317y1A.a.i() != 0) {
            c0317y1A.setPendingCount(c0317y1A.a.i() - 1);
            int i = 0;
            int iCount = 0;
            while (i < c0317y1A.a.i() - 1) {
                C0317y1 c0317y1A2 = c0317y1A.a(i, c0317y1A.b + iCount);
                iCount = (int) (c0317y1A2.a.count() + ((long) iCount));
                c0317y1A2.fork();
                i++;
            }
            c0317y1A = c0317y1A.a(i, c0317y1A.b + iCount);
        }
        switch (c0317y1A.c) {
            case 0:
                ((I0) c0317y1A.a).c(c0317y1A.b, c0317y1A.d);
                break;
            default:
                c0317y1A.a.f((Object[]) c0317y1A.d, c0317y1A.b);
                break;
        }
        c0317y1A.propagateCompletion();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public C0317y1(C0317y1 c0317y1, J0 j0, int i) {
        this(c0317y1, j0, i, (byte) 0);
        this.c = 1;
        this.d = (Object[]) c0317y1.d;
    }

    public final C0317y1 a(int i, int i2) {
        switch (this.c) {
            case 0:
                return new C0317y1(this, ((I0) this.a).a(i), i2);
            default:
                return new C0317y1(this, this.a.a(i), i2);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public C0317y1(C0317y1 c0317y1, I0 i0, int i) {
        this(c0317y1, i0, i, (byte) 0);
        this.c = 0;
        this.d = c0317y1.d;
    }
}
