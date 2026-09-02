package j$.time.chrono;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
public final class C implements l {
    public static final C BEFORE_ROC;
    public static final C ROC;
    public static final /* synthetic */ C[] a;

    @Override // j$.time.temporal.n
    public final /* synthetic */ long D(j$.time.temporal.r rVar) {
        return j$.com.android.tools.r8.a.p(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final /* synthetic */ boolean e(j$.time.temporal.r rVar) {
        return j$.com.android.tools.r8.a.s(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final /* synthetic */ int i(j$.time.temporal.r rVar) {
        return j$.com.android.tools.r8.a.n(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final /* synthetic */ Object t(j$.time.e eVar) {
        return j$.com.android.tools.r8.a.w(this, eVar);
    }

    public static C valueOf(String str) {
        return (C) Enum.valueOf(C.class, str);
    }

    public static C[] values() {
        return (C[]) a.clone();
    }

    static {
        C c = new C("BEFORE_ROC", 0);
        BEFORE_ROC = c;
        C c2 = new C("ROC", 1);
        ROC = c2;
        a = new C[]{c, c2};
    }

    @Override // j$.time.chrono.l
    public final int getValue() {
        return ordinal();
    }

    @Override // j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        return j$.time.temporal.s.d(this, rVar);
    }

    @Override // j$.time.temporal.o
    public final j$.time.temporal.m n(j$.time.temporal.m mVar) {
        return mVar.c(getValue(), j$.time.temporal.a.ERA);
    }
}
