package j$.util.stream;

import j$.util.Spliterator;
import java.util.Set;
import java.util.function.Supplier;

/* JADX INFO: renamed from: j$.util.stream.a, reason: case insensitive filesystem */
public final /* synthetic */ class C0196a implements Supplier {
    public final /* synthetic */ int a;
    public final /* synthetic */ Object b;

    public /* synthetic */ C0196a(int i, Object obj) {
        this.a = i;
        this.b = obj;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        int i = this.a;
        Object obj = this.b;
        switch (i) {
            case 0:
                return ((AbstractC0201b) obj).F0(0);
            case 1:
                return (Spliterator) obj;
            default:
                Set set = Collectors.a;
                return new j$.util.r0((CharSequence) obj);
        }
    }
}
