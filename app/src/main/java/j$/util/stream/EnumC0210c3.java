package j$.util.stream;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: renamed from: j$.util.stream.c3, reason: case insensitive filesystem */
public final class EnumC0210c3 {
    public static final EnumC0210c3 OP;
    public static final EnumC0210c3 SPLITERATOR;
    public static final EnumC0210c3 STREAM;
    public static final EnumC0210c3 TERMINAL_OP;
    public static final EnumC0210c3 UPSTREAM_TERMINAL_OP;
    public static final /* synthetic */ EnumC0210c3[] a;

    public static EnumC0210c3 valueOf(String str) {
        return (EnumC0210c3) Enum.valueOf(EnumC0210c3.class, str);
    }

    public static EnumC0210c3[] values() {
        return (EnumC0210c3[]) a.clone();
    }

    static {
        EnumC0210c3 enumC0210c3 = new EnumC0210c3("SPLITERATOR", 0);
        SPLITERATOR = enumC0210c3;
        EnumC0210c3 enumC0210c4 = new EnumC0210c3("STREAM", 1);
        STREAM = enumC0210c4;
        EnumC0210c3 enumC0210c5 = new EnumC0210c3("OP", 2);
        OP = enumC0210c5;
        EnumC0210c3 enumC0210c6 = new EnumC0210c3("TERMINAL_OP", 3);
        TERMINAL_OP = enumC0210c6;
        EnumC0210c3 enumC0210c7 = new EnumC0210c3("UPSTREAM_TERMINAL_OP", 4);
        UPSTREAM_TERMINAL_OP = enumC0210c7;
        a = new EnumC0210c3[]{enumC0210c3, enumC0210c4, enumC0210c5, enumC0210c6, enumC0210c7};
    }
}
