package j$.util.stream;

import java.util.function.DoubleConsumer;

public final class H extends L implements InterfaceC0264n2 {
    public static final G c;
    public static final G d;

    public final /* synthetic */ DoubleConsumer andThen(DoubleConsumer doubleConsumer) {
        return j$.com.android.tools.r8.a.b(this, doubleConsumer);
    }

    @Override // j$.util.stream.L, j$.util.stream.InterfaceC0279q2
    public final void accept(double d2) {
        v(Double.valueOf(d2));
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        if (this.a) {
            return new j$.util.B(((Double) this.b).doubleValue());
        }
        return null;
    }

    static {
        EnumC0220e3 enumC0220e3 = EnumC0220e3.DOUBLE_VALUE;
        C0276q c0276q = new C0276q(8);
        C0246k c0246k = new C0246k(9);
        j$.util.B b = j$.util.B.c;
        c = new G(true, enumC0220e3, b, c0276q, c0246k);
        d = new G(false, enumC0220e3, b, new C0276q(8), new C0246k(9));
    }
}
