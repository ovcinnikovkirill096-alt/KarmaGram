package j$.time.format;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
public final class E {
    public static final E LENIENT;
    public static final E SMART;
    public static final E STRICT;
    public static final /* synthetic */ E[] a;

    public static E valueOf(String str) {
        return (E) Enum.valueOf(E.class, str);
    }

    public static E[] values() {
        return (E[]) a.clone();
    }

    static {
        E e = new E("STRICT", 0);
        STRICT = e;
        E e2 = new E("SMART", 1);
        SMART = e2;
        E e3 = new E("LENIENT", 2);
        LENIENT = e3;
        a = new E[]{e, e2, e3};
    }
}
