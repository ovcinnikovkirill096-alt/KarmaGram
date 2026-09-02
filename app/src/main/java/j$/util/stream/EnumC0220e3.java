package j$.util.stream;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: renamed from: j$.util.stream.e3, reason: case insensitive filesystem */
public final class EnumC0220e3 {
    public static final EnumC0220e3 DOUBLE_VALUE;
    public static final EnumC0220e3 INT_VALUE;
    public static final EnumC0220e3 LONG_VALUE;
    public static final EnumC0220e3 REFERENCE;
    public static final /* synthetic */ EnumC0220e3[] a;

    public static EnumC0220e3 valueOf(String str) {
        return (EnumC0220e3) Enum.valueOf(EnumC0220e3.class, str);
    }

    public static EnumC0220e3[] values() {
        return (EnumC0220e3[]) a.clone();
    }

    static {
        EnumC0220e3 enumC0220e3 = new EnumC0220e3("REFERENCE", 0);
        REFERENCE = enumC0220e3;
        EnumC0220e3 enumC0220e4 = new EnumC0220e3("INT_VALUE", 1);
        INT_VALUE = enumC0220e4;
        EnumC0220e3 enumC0220e5 = new EnumC0220e3("LONG_VALUE", 2);
        LONG_VALUE = enumC0220e5;
        EnumC0220e3 enumC0220e6 = new EnumC0220e3("DOUBLE_VALUE", 3);
        DOUBLE_VALUE = enumC0220e6;
        a = new EnumC0220e3[]{enumC0220e3, enumC0220e4, enumC0220e5, enumC0220e6};
    }
}
