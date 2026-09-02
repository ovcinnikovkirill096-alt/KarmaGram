package j$.util.stream;

import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

/* JADX INFO: renamed from: j$.util.stream.q0, reason: case insensitive filesystem */
public final /* synthetic */ class C0277q0 implements Supplier {
    public final /* synthetic */ int a;
    public final /* synthetic */ EnumC0306w0 b;
    public final /* synthetic */ Object c;

    public /* synthetic */ C0277q0(EnumC0306w0 enumC0306w0, Object obj, int i) {
        this.a = i;
        this.b = enumC0306w0;
        this.c = obj;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        switch (this.a) {
            case 0:
                return new C0286s0(this.b, (IntPredicate) this.c);
            default:
                return new C0281r0(this.b, (Predicate) this.c);
        }
    }
}
