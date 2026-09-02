package j$.time.format;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
public final class p implements InterfaceC0175e {
    public static final p INSENSITIVE;
    public static final p LENIENT;
    public static final p SENSITIVE;
    public static final p STRICT;
    public static final /* synthetic */ p[] a;

    @Override // j$.time.format.InterfaceC0175e
    public final boolean i(y yVar, StringBuilder sb) {
        return true;
    }

    public static p valueOf(String str) {
        return (p) Enum.valueOf(p.class, str);
    }

    public static p[] values() {
        return (p[]) a.clone();
    }

    static {
        p pVar = new p("SENSITIVE", 0);
        SENSITIVE = pVar;
        p pVar2 = new p("INSENSITIVE", 1);
        INSENSITIVE = pVar2;
        p pVar3 = new p("STRICT", 2);
        STRICT = pVar3;
        p pVar4 = new p("LENIENT", 3);
        LENIENT = pVar4;
        a = new p[]{pVar, pVar2, pVar3, pVar4};
    }

    @Override // j$.time.format.InterfaceC0175e
    public final int j(v vVar, CharSequence charSequence, int i) {
        int iOrdinal = ordinal();
        if (iOrdinal == 0) {
            vVar.b = true;
            return i;
        }
        if (iOrdinal == 1) {
            vVar.b = false;
            return i;
        }
        if (iOrdinal == 2) {
            vVar.c = true;
            return i;
        }
        if (iOrdinal != 3) {
            return i;
        }
        vVar.c = false;
        return i;
    }

    @Override // java.lang.Enum
    public final String toString() {
        int iOrdinal = ordinal();
        if (iOrdinal == 0) {
            return "ParseCaseSensitive(true)";
        }
        if (iOrdinal == 1) {
            return "ParseCaseSensitive(false)";
        }
        if (iOrdinal == 2) {
            return "ParseStrict(true)";
        }
        if (iOrdinal == 3) {
            return "ParseStrict(false)";
        }
        throw new IllegalStateException("Unreachable");
    }
}
