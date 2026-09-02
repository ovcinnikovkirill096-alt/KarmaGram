package j$.util.stream;

import j$.util.OptionalInt;
import java.util.function.IntConsumer;

public final class I extends L implements InterfaceC0269o2 {
    public static final G c;
    public static final G d;

    public final /* synthetic */ IntConsumer andThen(IntConsumer intConsumer) {
        return j$.com.android.tools.r8.a.c(this, intConsumer);
    }

    @Override // j$.util.stream.L, j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        v(Integer.valueOf(i));
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        if (this.a) {
            return new OptionalInt(((Integer) this.b).intValue());
        }
        return null;
    }

    static {
        EnumC0220e3 enumC0220e3 = EnumC0220e3.INT_VALUE;
        C0276q c0276q = new C0276q(9);
        C0246k c0246k = new C0246k(10);
        OptionalInt optionalInt = OptionalInt.c;
        c = new G(true, enumC0220e3, optionalInt, c0276q, c0246k);
        d = new G(false, enumC0220e3, optionalInt, new C0276q(9), new C0246k(10));
    }
}
