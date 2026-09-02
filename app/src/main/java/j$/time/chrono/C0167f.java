package j$.time.chrono;

import j$.time.Instant;
import j$.time.LocalDate;
import j$.time.ZoneId;
import j$.time.ZoneOffset;
import j$.util.Objects;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/* JADX INFO: renamed from: j$.time.chrono.f, reason: case insensitive filesystem */
public final class C0167f implements ChronoLocalDateTime, j$.time.temporal.m, j$.time.temporal.o, Serializable {
    private static final long serialVersionUID = 4556003607393004514L;
    public final transient InterfaceC0163b a;
    public final transient j$.time.i b;

    @Override // java.lang.Comparable
    /* JADX INFO: renamed from: H */
    public final /* synthetic */ int compareTo(ChronoLocalDateTime chronoLocalDateTime) {
        return j$.com.android.tools.r8.a.f(this, chronoLocalDateTime);
    }

    public final /* synthetic */ long T(ZoneOffset zoneOffset) {
        return j$.com.android.tools.r8.a.x(this, zoneOffset);
    }

    @Override // j$.time.temporal.n
    public final /* synthetic */ Object t(j$.time.e eVar) {
        return j$.com.android.tools.r8.a.u(this, eVar);
    }

    public static C0167f Q(k kVar, j$.time.temporal.m mVar) {
        C0167f c0167f = (C0167f) mVar;
        if (kVar.equals(c0167f.a.a())) {
            return c0167f;
        }
        throw new ClassCastException("Chronology mismatch, required: " + kVar.getId() + ", actual: " + c0167f.a.a().getId());
    }

    public C0167f(InterfaceC0163b interfaceC0163b, j$.time.i iVar) {
        Objects.requireNonNull(interfaceC0163b, "date");
        Objects.requireNonNull(iVar, "time");
        this.a = interfaceC0163b;
        this.b = iVar;
    }

    public final C0167f V(j$.time.temporal.m mVar, j$.time.i iVar) {
        InterfaceC0163b interfaceC0163b = this.a;
        return (interfaceC0163b == mVar && this.b == iVar) ? this : new C0167f(AbstractC0165d.Q(interfaceC0163b.a(), mVar), iVar);
    }

    @Override // j$.time.chrono.ChronoLocalDateTime
    public final k a() {
        return this.a.a();
    }

    @Override // j$.time.chrono.ChronoLocalDateTime
    public final InterfaceC0163b f() {
        return this.a;
    }

    public final int hashCode() {
        return this.a.hashCode() ^ this.b.hashCode();
    }

    public final String toString() {
        return this.a.toString() + "T" + this.b.toString();
    }

    @Override // j$.time.temporal.m
    public final j$.time.temporal.m y(long j, j$.time.temporal.b bVar) {
        return Q(this.a.a(), j$.time.temporal.s.b(this, j, bVar));
    }

    @Override // j$.time.chrono.ChronoLocalDateTime
    public final j$.time.i b() {
        return this.b;
    }

    @Override // j$.time.temporal.n
    public final boolean e(j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return rVar != null && rVar.i(this);
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
        return aVar.isDateBased() || aVar.Q();
    }

    @Override // j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            if (!((j$.time.temporal.a) rVar).Q()) {
                return this.a.k(rVar);
            }
            j$.time.i iVar = this.b;
            iVar.getClass();
            return j$.time.temporal.s.d(iVar, rVar);
        }
        return rVar.j(this);
    }

    @Override // j$.time.temporal.n
    public final int i(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return ((j$.time.temporal.a) rVar).Q() ? this.b.i(rVar) : this.a.i(rVar);
        }
        return k(rVar).a(D(rVar), rVar);
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return ((j$.time.temporal.a) rVar).Q() ? this.b.D(rVar) : this.a.D(rVar);
        }
        return rVar.t(this);
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: j */
    public final j$.time.temporal.m x(LocalDate localDate) {
        if (j$.time.c.b(localDate)) {
            return V(localDate, this.b);
        }
        k kVarA = this.a.a();
        localDate.getClass();
        return Q(kVarA, (C0167f) j$.com.android.tools.r8.a.a(localDate, this));
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: U, reason: merged with bridge method [inline-methods] */
    public final C0167f c(long j, j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            if (((j$.time.temporal.a) rVar).Q()) {
                return V(this.a, this.b.c(j, rVar));
            }
            return V(this.a.c(j, rVar), this.b);
        }
        return Q(this.a.a(), rVar.y(this, j));
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: R, reason: merged with bridge method [inline-methods] */
    public final C0167f d(long j, j$.time.temporal.t tVar) {
        if (!(tVar instanceof j$.time.temporal.b)) {
            return Q(this.a.a(), tVar.i(this, j));
        }
        switch (AbstractC0166e.a[((j$.time.temporal.b) tVar).ordinal()]) {
            case 1:
                return S(this.a, 0L, 0L, 0L, j);
            case 2:
                C0167f c0167fV = V(this.a.d(j / 86400000000L, (j$.time.temporal.t) j$.time.temporal.b.DAYS), this.b);
                return c0167fV.S(c0167fV.a, 0L, 0L, 0L, (j % 86400000000L) * 1000);
            case 3:
                C0167f c0167fV2 = V(this.a.d(j / 86400000, (j$.time.temporal.t) j$.time.temporal.b.DAYS), this.b);
                return c0167fV2.S(c0167fV2.a, 0L, 0L, 0L, (j % 86400000) * 1000000);
            case 4:
                return S(this.a, 0L, 0L, j, 0L);
            case 5:
                return S(this.a, 0L, j, 0L, 0L);
            case 6:
                return S(this.a, j, 0L, 0L, 0L);
            case 7:
                C0167f c0167fV3 = V(this.a.d(j / 256, (j$.time.temporal.t) j$.time.temporal.b.DAYS), this.b);
                return c0167fV3.S(c0167fV3.a, (j % 256) * 12, 0L, 0L, 0L);
            default:
                return V(this.a.d(j, tVar), this.b);
        }
    }

    public final C0167f S(InterfaceC0163b interfaceC0163b, long j, long j2, long j3, long j4) {
        if ((j | j2 | j3 | j4) == 0) {
            return V(interfaceC0163b, this.b);
        }
        long j5 = j / 24;
        long j6 = ((j % 24) * 3600000000000L) + ((j2 % 1440) * 60000000000L) + ((j3 % 86400) * 1000000000) + (j4 % 86400000000000L);
        long jC0 = this.b.c0();
        long j7 = j6 + jC0;
        long jU = j$.com.android.tools.r8.a.U(j7, 86400000000000L) + j5 + (j2 / 1440) + (j3 / 86400) + (j4 / 86400000000000L);
        long jT = j$.com.android.tools.r8.a.T(j7, 86400000000000L);
        return V(interfaceC0163b.d(jU, (j$.time.temporal.t) j$.time.temporal.b.DAYS), jT == jC0 ? this.b : j$.time.i.V(jT));
    }

    @Override // j$.time.chrono.ChronoLocalDateTime
    public final ChronoZonedDateTime z(ZoneId zoneId) {
        return j.Q(zoneId, null, this);
    }

    @Override // j$.time.temporal.o
    public final j$.time.temporal.m n(j$.time.temporal.m mVar) {
        return mVar.c(f().E(), j$.time.temporal.a.EPOCH_DAY).c(b().c0(), j$.time.temporal.a.NANO_OF_DAY);
    }

    private Object writeReplace() {
        return new D((byte) 2, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ChronoLocalDateTime) && j$.com.android.tools.r8.a.f(this, (ChronoLocalDateTime) obj) == 0;
    }

    @Override // j$.time.chrono.ChronoLocalDateTime
    public final Instant toInstant(ZoneOffset zoneOffset) {
        return Instant.S(T(zoneOffset), b().d);
    }
}
