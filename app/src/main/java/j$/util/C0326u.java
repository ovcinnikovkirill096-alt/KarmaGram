package j$.util;

import java.util.RandomAccess;

/* JADX INFO: renamed from: j$.util.u, reason: case insensitive filesystem */
public final class C0326u extends C0193p implements RandomAccess {
    private static final long serialVersionUID = -2542308836966382001L;

    @Override // j$.util.C0193p, java.util.List
    public final java.util.List subList(int i, int i2) {
        return new C0326u(this.b.subList(i, i2));
    }

    private Object writeReplace() {
        return new C0193p(this.b);
    }
}
