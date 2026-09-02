package j$.time.chrono;

import j$.time.Duration;
import j$.time.Instant;
import j$.time.LocalDate;
import j$.time.LocalDateTime;
import j$.time.ZoneId;
import j$.time.ZoneOffset;
import j$.time.zone.ZoneRules;
import j$.util.Objects;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

public final class j implements ChronoZonedDateTime, Serializable {
    private static final long serialVersionUID = -5261813987200935591L;
    public final transient C0167f a;
    public final transient ZoneOffset b;
    public final transient ZoneId c;

    @Override // j$.time.chrono.ChronoZonedDateTime
    public final /* synthetic */ long P() {
        return j$.com.android.tools.r8.a.y(this);
    }

    @Override // j$.time.temporal.n
    public final /* synthetic */ int i(j$.time.temporal.r rVar) {
        return j$.com.android.tools.r8.a.m(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final /* synthetic */ Object t(j$.time.e eVar) {
        return j$.com.android.tools.r8.a.v(this, eVar);
    }

    @Override // java.lang.Comparable
    public final /* synthetic */ int compareTo(ChronoZonedDateTime<?> chronoZonedDateTime) {
        return j$.com.android.tools.r8.a.g(this, chronoZonedDateTime);
    }

    public static j Q(ZoneId zoneId, ZoneOffset zoneOffset, C0167f c0167f) {
        Objects.requireNonNull(c0167f, "localDateTime");
        Objects.requireNonNull(zoneId, "zone");
        if (zoneId instanceof ZoneOffset) {
            return new j(zoneId, (ZoneOffset) zoneId, c0167f);
        }
        ZoneRules rules = zoneId.getRules();
        LocalDateTime localDateTimeR = LocalDateTime.R(c0167f);
        List listF = rules.f(localDateTimeR);
        if (listF.size() == 1) {
            zoneOffset = (ZoneOffset) listF.get(0);
        } else if (listF.size() == 0) {
            j$.time.zone.b bVarE = rules.e(localDateTimeR);
            c0167f = c0167f.S(c0167f.a, 0L, 0L, Duration.ofSeconds(bVarE.d.getTotalSeconds() - bVarE.c.getTotalSeconds()).a, 0L);
            zoneOffset = bVarE.d;
        } else {
            if (zoneOffset == null || !listF.contains(zoneOffset)) {
                zoneOffset = (ZoneOffset) listF.get(0);
            }
            c0167f = c0167f;
        }
        Objects.requireNonNull(zoneOffset, "offset");
        return new j(zoneId, zoneOffset, c0167f);
    }

    public static j R(k kVar, Instant instant, ZoneId zoneId) {
        ZoneOffset offset = zoneId.getRules().getOffset(instant);
        Objects.requireNonNull(offset, "offset");
        return new j(zoneId, offset, (C0167f) kVar.B(LocalDateTime.U(instant.a, instant.b, offset)));
    }

    @Override // j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            if (rVar != j$.time.temporal.a.INSTANT_SECONDS && rVar != j$.time.temporal.a.OFFSET_SECONDS) {
                return ((C0167f) o()).k(rVar);
            }
            return ((j$.time.temporal.a) rVar).b;
        }
        return rVar.j(this);
    }

    public static j n(k kVar, j$.time.temporal.m mVar) {
        j jVar = (j) mVar;
        if (kVar.equals(jVar.a())) {
            return jVar;
        }
        throw new ClassCastException("Chronology mismatch, required: " + kVar.getId() + ", actual: " + jVar.a().getId());
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            int i = AbstractC0169h.a[((j$.time.temporal.a) rVar).ordinal()];
            if (i == 1) {
                return P();
            }
            if (i == 2) {
                return g().getTotalSeconds();
            }
            return ((C0167f) o()).D(rVar);
        }
        return rVar.t(this);
    }

    public j(ZoneId zoneId, ZoneOffset zoneOffset, C0167f c0167f) {
        this.a = (C0167f) Objects.requireNonNull(c0167f, "dateTime");
        this.b = (ZoneOffset) Objects.requireNonNull(zoneOffset, "offset");
        this.c = (ZoneId) Objects.requireNonNull(zoneId, "zone");
    }

    @Override // j$.time.chrono.ChronoZonedDateTime
    public final ZoneOffset g() {
        return this.b;
    }

    @Override // j$.time.chrono.ChronoZonedDateTime
    public final InterfaceC0163b f() {
        return ((C0167f) o()).f();
    }

    @Override // j$.time.chrono.ChronoZonedDateTime
    public final j$.time.i b() {
        return ((C0167f) o()).b();
    }

    public final int hashCode() {
        return (this.a.hashCode() ^ this.b.b) ^ Integer.rotateLeft(this.c.hashCode(), 3);
    }

    @Override // j$.time.chrono.ChronoZonedDateTime
    public final ChronoLocalDateTime o() {
        return this.a;
    }

    public final String toString() {
        String str = this.a.toString() + this.b.c;
        ZoneOffset zoneOffset = this.b;
        ZoneId zoneId = this.c;
        if (zoneOffset == zoneId) {
            return str;
        }
        return str + "[" + zoneId.toString() + "]";
    }

    @Override // j$.time.chrono.ChronoZonedDateTime
    public final ZoneId C() {
        return this.c;
    }

    @Override // j$.time.chrono.ChronoZonedDateTime
    public final k a() {
        return f().a();
    }

    @Override // j$.time.chrono.ChronoZonedDateTime
    public final ChronoZonedDateTime w(ZoneId zoneId) {
        return Q(zoneId, this.b, this.a);
    }

    @Override // j$.time.temporal.n
    public final boolean e(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return true;
        }
        return rVar != null && rVar.i(this);
    }

    @Override // j$.time.temporal.m
    public final j$.time.temporal.m c(long j, j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return n(a(), rVar.y(this, j));
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
        int i = AbstractC0170i.a[aVar.ordinal()];
        if (i == 1) {
            return d(j - j$.com.android.tools.r8.a.y(this), j$.time.temporal.b.SECONDS);
        }
        if (i != 2) {
            return Q(this.c, this.b, this.a.c(j, rVar));
        }
        ZoneOffset zoneOffsetW = ZoneOffset.W(aVar.b.a(j, aVar));
        C0167f c0167f = this.a;
        c0167f.getClass();
        return R(a(), Instant.S(c0167f.T(zoneOffsetW), c0167f.b().d), this.c);
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: S, reason: merged with bridge method [inline-methods] */
    public final j d(long j, j$.time.temporal.t tVar) {
        if (tVar instanceof j$.time.temporal.b) {
            return n(a(), this.a.d(j, tVar).n(this));
        }
        return n(a(), tVar.i(this, j));
    }

    private Object writeReplace() {
        return new D((byte) 3, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ChronoZonedDateTime) && j$.com.android.tools.r8.a.g(this, (ChronoZonedDateTime) obj) == 0;
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: j */
    public final j$.time.temporal.m x(LocalDate localDate) {
        return n(a(), localDate.n(this));
    }

    @Override // j$.time.temporal.m
    public final j$.time.temporal.m y(long j, j$.time.temporal.b bVar) {
        return n(a(), j$.time.temporal.s.b(this, j, bVar));
    }

    @Override // j$.time.chrono.ChronoZonedDateTime
    public final Instant toInstant() {
        return Instant.S(P(), b().d);
    }
}
