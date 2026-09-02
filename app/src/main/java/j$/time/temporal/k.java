package j$.time.temporal;

import j$.time.format.D;
import j$.time.format.E;
import java.util.Map;

public enum k implements r {
    JULIAN_DAY("JulianDay", 2440588),
    MODIFIED_JULIAN_DAY("ModifiedJulianDay", 40587),
    RATA_DIE("RataDie", 719163);

    private static final long serialVersionUID = -7501623920830201812L;
    public final transient String a;
    public final transient v b;
    public final transient long c;

    @Override // j$.time.temporal.r
    public final boolean isDateBased() {
        return true;
    }

    static {
        b bVar = b.NANOS;
    }

    k(String str, long j) {
        this.a = str;
        this.b = v.f((-365243219162L) + j, 365241780471L + j);
        this.c = j;
    }

    @Override // j$.time.temporal.r
    public final v n() {
        return this.b;
    }

    @Override // j$.time.temporal.r
    public final m y(m mVar, long j) {
        if (!this.b.e(j)) {
            throw new j$.time.b("Invalid value: " + this.a + " " + j);
        }
        return mVar.c(j$.com.android.tools.r8.a.W(j, this.c), a.EPOCH_DAY);
    }

    @Override // j$.time.temporal.r
    public final boolean i(n nVar) {
        return nVar.e(a.EPOCH_DAY);
    }

    @Override // j$.time.temporal.r
    public final v j(n nVar) {
        if (nVar.e(a.EPOCH_DAY)) {
            return this.b;
        }
        throw new j$.time.b("Unsupported field: " + this);
    }

    @Override // j$.time.temporal.r
    public final long t(n nVar) {
        return nVar.D(a.EPOCH_DAY) + this.c;
    }

    @Override // j$.time.temporal.r
    public final n k(Map map, D d2, E e) {
        long jLongValue = ((Long) map.remove(this)).longValue();
        j$.time.chrono.k kVarN = j$.com.android.tools.r8.a.N(d2);
        E e2 = E.LENIENT;
        long j = this.c;
        if (e == e2) {
            return kVarN.h(j$.com.android.tools.r8.a.W(jLongValue, j));
        }
        this.b.b(jLongValue, this);
        return kVarN.h(jLongValue - j);
    }

    @Override // java.lang.Enum
    public final String toString() {
        return this.a;
    }
}
