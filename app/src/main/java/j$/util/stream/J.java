package j$.util.stream;

import java.util.function.LongConsumer;

public final class J extends L implements InterfaceC0274p2 {
    public static final G c;
    public static final G d;

    public final /* synthetic */ LongConsumer andThen(LongConsumer longConsumer) {
        return j$.com.android.tools.r8.a.d(this, longConsumer);
    }

    @Override // j$.util.stream.L, j$.util.stream.InterfaceC0279q2, j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final void accept(long j) {
        v(Long.valueOf(j));
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        if (this.a) {
            return new j$.util.C(((Long) this.b).longValue());
        }
        return null;
    }

    static {
        EnumC0220e3 enumC0220e3 = EnumC0220e3.LONG_VALUE;
        C0276q c0276q = new C0276q(10);
        C0246k c0246k = new C0246k(11);
        j$.util.C c2 = j$.util.C.c;
        c = new G(true, enumC0220e3, c2, c0276q, c0246k);
        d = new G(false, enumC0220e3, c2, new C0276q(10), new C0246k(11));
    }
}
