package j$.util.stream;

import j$.util.Spliterator;
import java.util.EnumMap;
import java.util.Map;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'DISTINCT' uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:485)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByRegister(EnumVisitor.java:422)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:351)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:284)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:153)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:102)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX INFO: renamed from: j$.util.stream.d3, reason: case insensitive filesystem */
public final class EnumC0215d3 {
    public static final EnumC0215d3 DISTINCT;
    public static final EnumC0215d3 ORDERED;
    public static final EnumC0215d3 SHORT_CIRCUIT;
    public static final EnumC0215d3 SIZED;
    public static final EnumC0215d3 SORTED;
    public static final int f;
    public static final int g;
    public static final int h;
    public static final int i;
    public static final int j;
    public static final int k;
    public static final int l;
    public static final int m;
    public static final int n;
    public static final int o;
    public static final int p;
    public static final int q;
    public static final int r;
    public static final int s;
    public static final int t;
    public static final int u;
    public static final /* synthetic */ EnumC0215d3[] v;
    public final Map a;
    public final int b;
    public final int c;
    public final int d;
    public final int e;

    public static EnumC0215d3 valueOf(String str) {
        return (EnumC0215d3) Enum.valueOf(EnumC0215d3.class, str);
    }

    public static EnumC0215d3[] values() {
        return (EnumC0215d3[]) v.clone();
    }

    static {
        EnumC0210c3 enumC0210c3 = EnumC0210c3.SPLITERATOR;
        j$.time.t tVarT = t(enumC0210c3);
        EnumC0210c3 enumC0210c4 = EnumC0210c3.STREAM;
        tVarT.s(enumC0210c4);
        EnumC0210c3 enumC0210c5 = EnumC0210c3.OP;
        ((EnumMap) ((Map) tVarT.b)).put(enumC0210c5, 3);
        EnumC0215d3 enumC0215d3 = new EnumC0215d3("DISTINCT", 0, 0, tVarT);
        DISTINCT = enumC0215d3;
        j$.time.t tVarT2 = t(enumC0210c3);
        tVarT2.s(enumC0210c4);
        ((EnumMap) ((Map) tVarT2.b)).put(enumC0210c5, 3);
        EnumC0215d3 enumC0215d4 = new EnumC0215d3("SORTED", 1, 1, tVarT2);
        SORTED = enumC0215d4;
        j$.time.t tVarT3 = t(enumC0210c3);
        tVarT3.s(enumC0210c4);
        ((EnumMap) ((Map) tVarT3.b)).put(enumC0210c5, 3);
        EnumC0210c3 enumC0210c6 = EnumC0210c3.TERMINAL_OP;
        ((EnumMap) ((Map) tVarT3.b)).put(enumC0210c6, 2);
        EnumC0210c3 enumC0210c7 = EnumC0210c3.UPSTREAM_TERMINAL_OP;
        ((EnumMap) ((Map) tVarT3.b)).put(enumC0210c7, 2);
        EnumC0215d3 enumC0215d5 = new EnumC0215d3("ORDERED", 2, 2, tVarT3);
        ORDERED = enumC0215d5;
        j$.time.t tVarT4 = t(enumC0210c3);
        tVarT4.s(enumC0210c4);
        ((EnumMap) ((Map) tVarT4.b)).put(enumC0210c5, 2);
        EnumC0215d3 enumC0215d6 = new EnumC0215d3("SIZED", 3, 3, tVarT4);
        SIZED = enumC0215d6;
        j$.time.t tVarT5 = t(enumC0210c5);
        tVarT5.s(enumC0210c6);
        int i2 = 0;
        EnumC0215d3 enumC0215d7 = new EnumC0215d3("SHORT_CIRCUIT", 4, 12, tVarT5);
        SHORT_CIRCUIT = enumC0215d7;
        v = new EnumC0215d3[]{enumC0215d3, enumC0215d4, enumC0215d5, enumC0215d6, enumC0215d7};
        f = j(enumC0210c3);
        g = j(enumC0210c4);
        h = j(enumC0210c5);
        j(enumC0210c6);
        j(enumC0210c7);
        for (EnumC0215d3 enumC0215d8 : values()) {
            i2 |= enumC0215d8.e;
        }
        i = i2;
        int i3 = g;
        j = i3;
        int i4 = i3 << 1;
        k = i4;
        l = i3 | i4;
        EnumC0215d3 enumC0215d9 = DISTINCT;
        m = enumC0215d9.c;
        n = enumC0215d9.d;
        EnumC0215d3 enumC0215d10 = SORTED;
        o = enumC0215d10.c;
        p = enumC0215d10.d;
        EnumC0215d3 enumC0215d11 = ORDERED;
        q = enumC0215d11.c;
        r = enumC0215d11.d;
        EnumC0215d3 enumC0215d12 = SIZED;
        s = enumC0215d12.c;
        t = enumC0215d12.d;
        u = SHORT_CIRCUIT.c;
    }

    public static j$.time.t t(EnumC0210c3 enumC0210c3) {
        j$.time.t tVar = new j$.time.t(10, new EnumMap(EnumC0210c3.class));
        tVar.s(enumC0210c3);
        return tVar;
    }

    public EnumC0215d3(String str, int i2, int i3, j$.time.t tVar) {
        super(str, i2);
        for (EnumC0210c3 enumC0210c3 : EnumC0210c3.values()) {
            j$.util.Map.EL.putIfAbsent((Map) tVar.b, enumC0210c3, 0);
        }
        this.a = (Map) tVar.b;
        int i4 = i3 * 2;
        this.b = i4;
        this.c = 1 << i4;
        this.d = 2 << i4;
        this.e = 3 << i4;
    }

    public final boolean n(int i2) {
        return (i2 & this.e) == this.c;
    }

    public static int j(EnumC0210c3 enumC0210c3) {
        int iIntValue = 0;
        for (EnumC0215d3 enumC0215d3 : values()) {
            iIntValue |= ((Integer) enumC0215d3.a.get(enumC0210c3)).intValue() << enumC0215d3.b;
        }
        return iIntValue;
    }

    public static int i(int i2, int i3) {
        int i4;
        if (i2 == 0) {
            i4 = i;
        } else {
            i4 = ~(((j & i2) << 1) | i2 | ((k & i2) >> 1));
        }
        return i2 | (i3 & i4);
    }

    public static int k(Spliterator spliterator) {
        int iCharacteristics = spliterator.characteristics();
        int i2 = iCharacteristics & 4;
        int i3 = f;
        return (i2 == 0 || spliterator.getComparator() == null) ? iCharacteristics & i3 : iCharacteristics & i3 & (-5);
    }
}
