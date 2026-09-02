package j$.util;

import java.util.RandomAccess;

/* JADX INFO: renamed from: j$.util.k, reason: case insensitive filesystem */
public final class C0188k extends C0186i implements RandomAccess {
    private static final long serialVersionUID = 1530674583602358482L;

    @Override // j$.util.C0186i, java.util.List
    public final java.util.List subList(int i, int i2) {
        C0188k c0188k;
        synchronized (this.b) {
            c0188k = new C0188k(this.c.subList(i, i2), this.b);
        }
        return c0188k;
    }

    private Object writeReplace() {
        return new C0186i(this.c);
    }
}
