package j$.time.chrono;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
public final class q implements l {
    public static final q AH;
    public static final /* synthetic */ q[] a;

    @Override // j$.time.temporal.n
    public final /* synthetic */ long D(j$.time.temporal.r rVar) {
        return j$.com.android.tools.r8.a.p(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final /* synthetic */ boolean e(j$.time.temporal.r rVar) {
        return j$.com.android.tools.r8.a.s(this, rVar);
    }

    @Override // j$.time.chrono.l
    public final int getValue() {
        return 1;
    }

    @Override // j$.time.temporal.n
    public final /* synthetic */ int i(j$.time.temporal.r rVar) {
        return j$.com.android.tools.r8.a.n(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final /* synthetic */ Object t(j$.time.e eVar) {
        return j$.com.android.tools.r8.a.w(this, eVar);
    }

    public static q valueOf(String str) {
        return (q) Enum.valueOf(q.class, str);
    }

    public static q[] values() {
        return (q[]) a.clone();
    }

    static {
        q qVar = new q("AH", 0);
        AH = qVar;
        a = new q[]{qVar};
    }

    @Override // j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        if (rVar == j$.time.temporal.a.ERA) {
            return j$.time.temporal.v.f(1L, 1L);
        }
        return j$.time.temporal.s.d(this, rVar);
    }

    @Override // j$.time.temporal.o
    public final j$.time.temporal.m n(j$.time.temporal.m mVar) {
        return mVar.c(1, j$.time.temporal.a.ERA);
    }
}
