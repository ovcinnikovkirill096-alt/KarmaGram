package j$.util.stream;

import java.util.function.Supplier;

/* JADX INFO: renamed from: j$.util.stream.p0, reason: case insensitive filesystem */
public final /* synthetic */ class C0272p0 implements Supplier {
    public final /* synthetic */ int a;
    public final /* synthetic */ EnumC0306w0 b;

    public /* synthetic */ C0272p0(EnumC0306w0 enumC0306w0, int i) {
        this.a = i;
        this.b = enumC0306w0;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        switch (this.a) {
            case 0:
                return new C0291t0(this.b);
            default:
                return new C0296u0(this.b);
        }
    }
}
