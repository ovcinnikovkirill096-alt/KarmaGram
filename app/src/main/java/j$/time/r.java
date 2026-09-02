package j$.time;

import j$.time.format.E;
import j$.time.format.F;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Locale;
import org.telegram.messenger.MediaDataController;

public final class r implements j$.time.temporal.m, j$.time.temporal.o, Comparable, Serializable {
    public static final /* synthetic */ int b = 0;
    private static final long serialVersionUID = -23038383694477807L;
    public final int a;

    @Override // java.lang.Comparable
    public final int compareTo(Object obj) {
        return this.a - ((r) obj).a;
    }

    static {
        j$.time.format.u uVar = new j$.time.format.u();
        uVar.m(j$.time.temporal.a.YEAR, 4, 10, F.EXCEEDS_PAD);
        uVar.q(Locale.getDefault(), E.SMART, null);
    }

    public static r Q(int i) {
        j$.time.temporal.a.YEAR.D(i);
        return new r(i);
    }

    public r(int i) {
        this.a = i;
    }

    @Override // j$.time.temporal.n
    public final boolean e(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return rVar == j$.time.temporal.a.YEAR || rVar == j$.time.temporal.a.YEAR_OF_ERA || rVar == j$.time.temporal.a.ERA;
        }
        return rVar != null && rVar.i(this);
    }

    @Override // j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        if (rVar == j$.time.temporal.a.YEAR_OF_ERA) {
            return j$.time.temporal.v.f(1L, this.a <= 0 ? 1000000000L : 999999999L);
        }
        return j$.time.temporal.s.d(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final int i(j$.time.temporal.r rVar) {
        return k(rVar).a(D(rVar), rVar);
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return rVar.t(this);
        }
        int i = q.a[((j$.time.temporal.a) rVar).ordinal()];
        if (i == 1) {
            int i2 = this.a;
            if (i2 < 1) {
                i2 = 1 - i2;
            }
            return i2;
        }
        if (i == 2) {
            return this.a;
        }
        if (i == 3) {
            return this.a < 1 ? 0 : 1;
        }
        throw new j$.time.temporal.u(c.a("Unsupported field: ", rVar));
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: j */
    public final j$.time.temporal.m x(LocalDate localDate) {
        localDate.getClass();
        return (r) j$.com.android.tools.r8.a.a(localDate, this);
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: T, reason: merged with bridge method [inline-methods] */
    public final r c(long j, j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return (r) rVar.y(this, j);
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
        aVar.D(j);
        int i = q.a[aVar.ordinal()];
        if (i == 1) {
            if (this.a < 1) {
                j = 1 - j;
            }
            return Q((int) j);
        }
        if (i == 2) {
            return Q((int) j);
        }
        if (i == 3) {
            return D(j$.time.temporal.a.ERA) == j ? this : Q(1 - this.a);
        }
        throw new j$.time.temporal.u(c.a("Unsupported field: ", rVar));
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: R, reason: merged with bridge method [inline-methods] */
    public final r d(long j, j$.time.temporal.t tVar) {
        if (!(tVar instanceof j$.time.temporal.b)) {
            return (r) tVar.i(this, j);
        }
        int i = q.b[((j$.time.temporal.b) tVar).ordinal()];
        if (i == 1) {
            return S(j);
        }
        if (i == 2) {
            return S(j$.com.android.tools.r8.a.V(j, 10));
        }
        if (i == 3) {
            return S(j$.com.android.tools.r8.a.V(j, 100));
        }
        if (i == 4) {
            return S(j$.com.android.tools.r8.a.V(j, MediaDataController.MAX_STYLE_RUNS_COUNT));
        }
        if (i == 5) {
            j$.time.temporal.a aVar = j$.time.temporal.a.ERA;
            return c(j$.com.android.tools.r8.a.P(D(aVar), j), aVar);
        }
        throw new j$.time.temporal.u("Unsupported unit: " + tVar);
    }

    public final r S(long j) {
        if (j == 0) {
            return this;
        }
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR;
        return Q(aVar.b.a(((long) this.a) + j, aVar));
    }

    @Override // j$.time.temporal.m
    public final j$.time.temporal.m y(long j, j$.time.temporal.b bVar) {
        return j == Long.MIN_VALUE ? d(Long.MAX_VALUE, bVar).d(1L, bVar) : d(-j, bVar);
    }

    @Override // j$.time.temporal.n
    public final Object t(e eVar) {
        if (eVar == j$.time.temporal.s.b) {
            return j$.time.chrono.r.c;
        }
        if (eVar == j$.time.temporal.s.c) {
            return j$.time.temporal.b.YEARS;
        }
        return j$.time.temporal.s.c(this, eVar);
    }

    @Override // j$.time.temporal.o
    public final j$.time.temporal.m n(j$.time.temporal.m mVar) {
        if (!j$.com.android.tools.r8.a.N(mVar).equals(j$.time.chrono.r.c)) {
            throw new b("Adjustment only supported on ISO date-time");
        }
        return mVar.c(this.a, j$.time.temporal.a.YEAR);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof r) && this.a == ((r) obj).a;
    }

    public final int hashCode() {
        return this.a;
    }

    public final String toString() {
        return Integer.toString(this.a);
    }

    private Object writeReplace() {
        return new p((byte) 11, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
