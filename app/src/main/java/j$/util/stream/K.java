package j$.util.stream;

import j$.util.Optional;

public final class K extends L {
    public static final G c;
    public static final G d;

    @Override // java.util.function.Supplier
    public final Object get() {
        if (this.a) {
            return Optional.of(this.b);
        }
        return null;
    }

    static {
        EnumC0220e3 enumC0220e3 = EnumC0220e3.REFERENCE;
        c = new G(true, enumC0220e3, Optional.empty(), new C0276q(11), new C0246k(12));
        d = new G(false, enumC0220e3, Optional.empty(), new C0276q(11), new C0246k(12));
    }
}
