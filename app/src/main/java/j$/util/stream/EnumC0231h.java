package j$.util.stream;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: renamed from: j$.util.stream.h, reason: case insensitive filesystem */
public final class EnumC0231h {
    public static final EnumC0231h CONCURRENT;
    public static final EnumC0231h IDENTITY_FINISH;
    public static final EnumC0231h UNORDERED;
    public static final /* synthetic */ EnumC0231h[] a;

    public static EnumC0231h valueOf(String str) {
        return (EnumC0231h) Enum.valueOf(EnumC0231h.class, str);
    }

    public static EnumC0231h[] values() {
        return (EnumC0231h[]) a.clone();
    }

    static {
        EnumC0231h enumC0231h = new EnumC0231h("CONCURRENT", 0);
        CONCURRENT = enumC0231h;
        EnumC0231h enumC0231h2 = new EnumC0231h("UNORDERED", 1);
        UNORDERED = enumC0231h2;
        EnumC0231h enumC0231h3 = new EnumC0231h("IDENTITY_FINISH", 2);
        IDENTITY_FINISH = enumC0231h3;
        a = new EnumC0231h[]{enumC0231h, enumC0231h2, enumC0231h3};
    }
}
