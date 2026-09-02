package j$.time;

import com.android.dx.io.Opcodes;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
public final class k implements j$.time.temporal.n, j$.time.temporal.o {
    public static final k APRIL;
    public static final k AUGUST;
    public static final k DECEMBER;
    public static final k FEBRUARY;
    public static final k JANUARY;
    public static final k JULY;
    public static final k JUNE;
    public static final k MARCH;
    public static final k MAY;
    public static final k NOVEMBER;
    public static final k OCTOBER;
    public static final k SEPTEMBER;
    public static final k[] a;
    public static final /* synthetic */ k[] b;

    public static k valueOf(String str) {
        return (k) Enum.valueOf(k.class, str);
    }

    public static k[] values() {
        return (k[]) b.clone();
    }

    static {
        k kVar = new k("JANUARY", 0);
        JANUARY = kVar;
        k kVar2 = new k("FEBRUARY", 1);
        FEBRUARY = kVar2;
        k kVar3 = new k("MARCH", 2);
        MARCH = kVar3;
        k kVar4 = new k("APRIL", 3);
        APRIL = kVar4;
        k kVar5 = new k("MAY", 4);
        MAY = kVar5;
        k kVar6 = new k("JUNE", 5);
        JUNE = kVar6;
        k kVar7 = new k("JULY", 6);
        JULY = kVar7;
        k kVar8 = new k("AUGUST", 7);
        AUGUST = kVar8;
        k kVar9 = new k("SEPTEMBER", 8);
        SEPTEMBER = kVar9;
        k kVar10 = new k("OCTOBER", 9);
        OCTOBER = kVar10;
        k kVar11 = new k("NOVEMBER", 10);
        NOVEMBER = kVar11;
        k kVar12 = new k("DECEMBER", 11);
        DECEMBER = kVar12;
        b = new k[]{kVar, kVar2, kVar3, kVar4, kVar5, kVar6, kVar7, kVar8, kVar9, kVar10, kVar11, kVar12};
        a = values();
    }

    public static k T(int i) {
        if (i < 1 || i > 12) {
            throw new b("Invalid value for MonthOfYear: " + i);
        }
        return a[i - 1];
    }

    public final int getValue() {
        return ordinal() + 1;
    }

    @Override // j$.time.temporal.n
    public final boolean e(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return rVar == j$.time.temporal.a.MONTH_OF_YEAR;
        }
        return rVar != null && rVar.i(this);
    }

    @Override // j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        if (rVar == j$.time.temporal.a.MONTH_OF_YEAR) {
            return rVar.n();
        }
        return j$.time.temporal.s.d(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final int i(j$.time.temporal.r rVar) {
        if (rVar == j$.time.temporal.a.MONTH_OF_YEAR) {
            return getValue();
        }
        return j$.time.temporal.s.a(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        if (rVar == j$.time.temporal.a.MONTH_OF_YEAR) {
            return getValue();
        }
        if (rVar instanceof j$.time.temporal.a) {
            throw new j$.time.temporal.u(c.a("Unsupported field: ", rVar));
        }
        return rVar.t(this);
    }

    public final int R(boolean z) {
        int i = j.a[ordinal()];
        if (i != 1) {
            return (i == 2 || i == 3 || i == 4 || i == 5) ? 30 : 31;
        }
        return z ? 29 : 28;
    }

    public final int S() {
        int i = j.a[ordinal()];
        if (i != 1) {
            return (i == 2 || i == 3 || i == 4 || i == 5) ? 30 : 31;
        }
        return 29;
    }

    public final int Q(boolean z) {
        switch (j.a[ordinal()]) {
            case 1:
                return 32;
            case 2:
                return (z ? 1 : 0) + 91;
            case 3:
                return (z ? 1 : 0) + 152;
            case 4:
                return (z ? 1 : 0) + 244;
            case 5:
                return (z ? 1 : 0) + 305;
            case 6:
                return 1;
            case 7:
                return (z ? 1 : 0) + 60;
            case 8:
                return (z ? 1 : 0) + 121;
            case 9:
                return (z ? 1 : 0) + 182;
            case 10:
                return (z ? 1 : 0) + Opcodes.AND_INT_LIT16;
            case 11:
                return (z ? 1 : 0) + 274;
            default:
                return (z ? 1 : 0) + 335;
        }
    }

    @Override // j$.time.temporal.n
    public final Object t(e eVar) {
        if (eVar == j$.time.temporal.s.b) {
            return j$.time.chrono.r.c;
        }
        if (eVar == j$.time.temporal.s.c) {
            return j$.time.temporal.b.MONTHS;
        }
        return j$.time.temporal.s.c(this, eVar);
    }

    @Override // j$.time.temporal.o
    public final j$.time.temporal.m n(j$.time.temporal.m mVar) {
        if (!j$.com.android.tools.r8.a.N(mVar).equals(j$.time.chrono.r.c)) {
            throw new b("Adjustment only supported on ISO date-time");
        }
        return mVar.c(getValue(), j$.time.temporal.a.MONTH_OF_YEAR);
    }
}
