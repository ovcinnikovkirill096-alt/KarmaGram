package j$.util;

import java.util.Iterator;
import java.util.function.Consumer;

/* JADX INFO: renamed from: j$.util.m, reason: case insensitive filesystem */
public final class C0190m implements Iterator, InterfaceC0330y {
    public final /* synthetic */ int a = 0;
    public final Iterator b;

    public C0190m(C0191n c0191n) {
        this.b = c0191n.a.iterator();
    }

    public C0190m(C0195s c0195s) {
        this.b = c0195s.a.iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        switch (this.a) {
            case 0:
                break;
        }
        return this.b.hasNext();
    }

    @Override // java.util.Iterator
    public final Object next() {
        switch (this.a) {
            case 0:
                return this.b.next();
            default:
                return new C0194q((java.util.Map.Entry) this.b.next());
        }
    }

    @Override // java.util.Iterator
    public final void remove() {
        switch (this.a) {
            case 0:
                throw new UnsupportedOperationException();
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override // java.util.Iterator, j$.util.InterfaceC0330y
    public final void forEachRemaining(Consumer consumer) {
        switch (this.a) {
            case 0:
                j$.com.android.tools.r8.a.M(this.b, consumer);
                break;
            default:
                j$.com.android.tools.r8.a.M(this.b, new j$.time.t(1, consumer));
                break;
        }
    }
}
