package j$.time.chrono;

import java.io.Serializable;
import org.telegram.messenger.MediaDataController;

/* JADX INFO: renamed from: j$.time.chrono.d, reason: case insensitive filesystem */
public abstract class AbstractC0165d implements InterfaceC0163b, j$.time.temporal.m, j$.time.temporal.o, Serializable {
    private static final long serialVersionUID = 6282433883239719096L;

    @Override // java.lang.Comparable
    /* JADX INFO: renamed from: N */
    public final /* synthetic */ int compareTo(InterfaceC0163b interfaceC0163b) {
        return j$.com.android.tools.r8.a.e(this, interfaceC0163b);
    }

    public abstract InterfaceC0163b R(long j);

    public abstract InterfaceC0163b S(long j);

    public abstract InterfaceC0163b T(long j);

    @Override // j$.time.chrono.InterfaceC0163b, j$.time.temporal.n
    public /* synthetic */ boolean e(j$.time.temporal.r rVar) {
        return j$.com.android.tools.r8.a.r(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final /* synthetic */ int i(j$.time.temporal.r rVar) {
        return j$.time.temporal.s.a(this, rVar);
    }

    @Override // j$.time.temporal.n
    public /* synthetic */ j$.time.temporal.v k(j$.time.temporal.r rVar) {
        return j$.time.temporal.s.d(this, rVar);
    }

    @Override // j$.time.temporal.o
    public final /* synthetic */ j$.time.temporal.m n(j$.time.temporal.m mVar) {
        return j$.com.android.tools.r8.a.a(this, mVar);
    }

    @Override // j$.time.temporal.n
    public final /* synthetic */ Object t(j$.time.e eVar) {
        return j$.com.android.tools.r8.a.t(this, eVar);
    }

    public static InterfaceC0163b Q(k kVar, j$.time.temporal.m mVar) {
        InterfaceC0163b interfaceC0163b = (InterfaceC0163b) mVar;
        if (kVar.equals(interfaceC0163b.a())) {
            return interfaceC0163b;
        }
        throw new ClassCastException("Chronology mismatch, expected: " + kVar.getId() + ", actual: " + interfaceC0163b.a().getId());
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public ChronoLocalDateTime F(j$.time.i iVar) {
        return new C0167f(this, iVar);
    }

    @Override // j$.time.temporal.m
    public InterfaceC0163b d(long j, j$.time.temporal.t tVar) {
        boolean z = tVar instanceof j$.time.temporal.b;
        if (!z) {
            if (!z) {
                return Q(a(), tVar.i(this, j));
            }
            throw new j$.time.temporal.u("Unsupported unit: " + tVar);
        }
        switch (AbstractC0164c.a[((j$.time.temporal.b) tVar).ordinal()]) {
            case 1:
                return R(j);
            case 2:
                return R(j$.com.android.tools.r8.a.V(j, 7));
            case 3:
                return S(j);
            case 4:
                return T(j);
            case 5:
                return T(j$.com.android.tools.r8.a.V(j, 10));
            case 6:
                return T(j$.com.android.tools.r8.a.V(j, 100));
            case 7:
                return T(j$.com.android.tools.r8.a.V(j, MediaDataController.MAX_STYLE_RUNS_COUNT));
            case 8:
                j$.time.temporal.a aVar = j$.time.temporal.a.ERA;
                return c(j$.com.android.tools.r8.a.P(D(aVar), j), (j$.time.temporal.r) aVar);
            default:
                throw new j$.time.temporal.u("Unsupported unit: " + tVar);
        }
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public l G() {
        return a().u(j$.time.temporal.s.a(this, j$.time.temporal.a.ERA));
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public boolean p() {
        return a().O(D(j$.time.temporal.a.YEAR));
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public int M() {
        return p() ? 366 : 365;
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof InterfaceC0163b) && j$.com.android.tools.r8.a.e(this, (InterfaceC0163b) obj) == 0;
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public int hashCode() {
        long jE = E();
        return ((int) (jE ^ (jE >>> 32))) ^ a().hashCode();
    }

    @Override // j$.time.temporal.m
    public InterfaceC0163b x(j$.time.temporal.o oVar) {
        return Q(a(), oVar.n(this));
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final String toString() {
        long jD = D(j$.time.temporal.a.YEAR_OF_ERA);
        long jD2 = D(j$.time.temporal.a.MONTH_OF_YEAR);
        long jD3 = D(j$.time.temporal.a.DAY_OF_MONTH);
        StringBuilder sb = new StringBuilder(30);
        sb.append(a().toString());
        sb.append(" ");
        sb.append(G());
        sb.append(" ");
        sb.append(jD);
        sb.append(jD2 < 10 ? "-0" : "-");
        sb.append(jD2);
        sb.append(jD3 < 10 ? "-0" : "-");
        sb.append(jD3);
        return sb.toString();
    }

    @Override // j$.time.temporal.m
    public InterfaceC0163b c(long j, j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            throw new j$.time.temporal.u(j$.time.c.a("Unsupported field: ", rVar));
        }
        return Q(a(), rVar.y(this, j));
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public InterfaceC0163b J(j$.time.temporal.q qVar) {
        return Q(a(), qVar.i(this));
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: r */
    public InterfaceC0163b y(long j, j$.time.temporal.t tVar) {
        return Q(a(), j$.time.temporal.s.b(this, j, tVar));
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public long E() {
        return D(j$.time.temporal.a.EPOCH_DAY);
    }
}
