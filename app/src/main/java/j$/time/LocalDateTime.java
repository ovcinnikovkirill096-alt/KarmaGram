package j$.time;

import j$.time.chrono.ChronoLocalDateTime;
import j$.time.chrono.ChronoZonedDateTime;
import j$.time.chrono.InterfaceC0163b;
import j$.time.format.DateTimeFormatter;
import j$.util.Objects;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public final class LocalDateTime implements j$.time.temporal.m, j$.time.temporal.o, ChronoLocalDateTime<LocalDate>, Serializable {
    public static final LocalDateTime c = T(LocalDate.d, i.e);
    public static final LocalDateTime d = T(LocalDate.e, i.f);
    private static final long serialVersionUID = 6207766400415563566L;
    public final LocalDate a;
    public final i b;

    @Override // j$.time.chrono.ChronoLocalDateTime
    public final j$.time.chrono.k a() {
        return ((LocalDate) f()).a();
    }

    @Override // j$.time.chrono.ChronoLocalDateTime
    public final ChronoZonedDateTime z(ZoneId zoneId) {
        return ZonedDateTime.Q(this, zoneId, null);
    }

    public static LocalDateTime T(LocalDate localDate, i iVar) {
        Objects.requireNonNull(localDate, "date");
        Objects.requireNonNull(iVar, "time");
        return new LocalDateTime(localDate, iVar);
    }

    @Override // j$.time.temporal.o
    public final j$.time.temporal.m n(j$.time.temporal.m mVar) {
        return mVar.c(((LocalDate) f()).E(), j$.time.temporal.a.EPOCH_DAY).c(b().c0(), j$.time.temporal.a.NANO_OF_DAY);
    }

    public static LocalDateTime U(long j, int i, ZoneOffset zoneOffset) {
        Objects.requireNonNull(zoneOffset, "offset");
        long j2 = i;
        j$.time.temporal.a.NANO_OF_SECOND.D(j2);
        long totalSeconds = j + ((long) zoneOffset.getTotalSeconds());
        long j3 = 86400;
        return new LocalDateTime(LocalDate.b0(j$.com.android.tools.r8.a.U(totalSeconds, j3)), i.V((((long) ((int) j$.com.android.tools.r8.a.T(totalSeconds, j3))) * 1000000000) + j2));
    }

    public static LocalDateTime R(j$.time.temporal.n nVar) {
        if (nVar instanceof LocalDateTime) {
            return (LocalDateTime) nVar;
        }
        if (!(nVar instanceof ZonedDateTime)) {
            if (nVar instanceof OffsetDateTime) {
                return ((OffsetDateTime) nVar).toLocalDateTime();
            }
            try {
                return new LocalDateTime(LocalDate.S(nVar), i.S(nVar));
            } catch (b e) {
                throw new b("Unable to obtain LocalDateTime from TemporalAccessor: " + nVar + " of type " + nVar.getClass().getName(), e);
            }
        }
        return ((ZonedDateTime) nVar).a;
    }

    @Override // j$.time.chrono.ChronoLocalDateTime
    public final Instant toInstant(ZoneOffset zoneOffset) {
        return Instant.S(j$.com.android.tools.r8.a.x(this, zoneOffset), b().d);
    }

    public LocalDateTime(LocalDate localDate, i iVar) {
        this.a = localDate;
        this.b = iVar;
    }

    public final LocalDateTime Z(LocalDate localDate, i iVar) {
        return (this.a == localDate && this.b == iVar) ? this : new LocalDateTime(localDate, iVar);
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
            if (((j$.time.temporal.a) rVar).Q()) {
                i iVar = this.b;
                iVar.getClass();
                return j$.time.temporal.s.d(iVar, rVar);
            }
            return this.a.k(rVar);
        }
        return rVar.j(this);
    }

    @Override // j$.time.temporal.n
    public final int i(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return ((j$.time.temporal.a) rVar).Q() ? this.b.i(rVar) : this.a.i(rVar);
        }
        return j$.time.temporal.s.a(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return ((j$.time.temporal.a) rVar).Q() ? this.b.D(rVar) : this.a.D(rVar);
        }
        return rVar.t(this);
    }

    @Override // j$.time.chrono.ChronoLocalDateTime
    public final InterfaceC0163b f() {
        return this.a;
    }

    @Override // j$.time.chrono.ChronoLocalDateTime
    public final i b() {
        return this.b;
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: a0, reason: merged with bridge method [inline-methods] */
    public final LocalDateTime x(j$.time.temporal.o oVar) {
        if (oVar instanceof LocalDate) {
            return Z((LocalDate) oVar, this.b);
        }
        if (oVar instanceof i) {
            return Z(this.a, (i) oVar);
        }
        if (oVar instanceof LocalDateTime) {
            return (LocalDateTime) oVar;
        }
        return (LocalDateTime) oVar.n(this);
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: Y, reason: merged with bridge method [inline-methods] */
    public final LocalDateTime c(long j, j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            if (((j$.time.temporal.a) rVar).Q()) {
                return Z(this.a, this.b.c(j, rVar));
            }
            return Z(this.a.c(j, rVar), this.b);
        }
        return (LocalDateTime) rVar.y(this, j);
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: V, reason: merged with bridge method [inline-methods] */
    public final LocalDateTime d(long j, j$.time.temporal.t tVar) {
        if (!(tVar instanceof j$.time.temporal.b)) {
            return (LocalDateTime) tVar.i(this, j);
        }
        switch (g.a[((j$.time.temporal.b) tVar).ordinal()]) {
            case 1:
                return X(this.a, 0L, 0L, 0L, j);
            case 2:
                LocalDateTime localDateTimeZ = Z(this.a.plusDays(j / 86400000000L), this.b);
                return localDateTimeZ.X(localDateTimeZ.a, 0L, 0L, 0L, (j % 86400000000L) * 1000);
            case 3:
                LocalDateTime localDateTimeZ2 = Z(this.a.plusDays(j / 86400000), this.b);
                return localDateTimeZ2.X(localDateTimeZ2.a, 0L, 0L, 0L, (j % 86400000) * 1000000);
            case 4:
                return W(j);
            case 5:
                return X(this.a, 0L, j, 0L, 0L);
            case 6:
                return X(this.a, j, 0L, 0L, 0L);
            case 7:
                LocalDateTime localDateTimeZ3 = Z(this.a.plusDays(j / 256), this.b);
                return localDateTimeZ3.X(localDateTimeZ3.a, (j % 256) * 12, 0L, 0L, 0L);
            default:
                return Z(this.a.d(j, tVar), this.b);
        }
    }

    public final LocalDateTime W(long j) {
        return X(this.a, 0L, 0L, j, 0L);
    }

    @Override // j$.time.temporal.m
    public final j$.time.temporal.m y(long j, j$.time.temporal.b bVar) {
        return j == Long.MIN_VALUE ? d(Long.MAX_VALUE, bVar).d(1L, bVar) : d(-j, bVar);
    }

    public final LocalDateTime X(LocalDate localDate, long j, long j2, long j3, long j4) {
        if ((j | j2 | j3 | j4) == 0) {
            return Z(localDate, this.b);
        }
        long j5 = 1;
        long jC0 = this.b.c0();
        long j6 = ((((j % 24) * 3600000000000L) + ((j2 % 1440) * 60000000000L) + ((j3 % 86400) * 1000000000) + (j4 % 86400000000000L)) * j5) + jC0;
        long jU = j$.com.android.tools.r8.a.U(j6, 86400000000000L) + (((j / 24) + (j2 / 1440) + (j3 / 86400) + (j4 / 86400000000000L)) * j5);
        long jT = j$.com.android.tools.r8.a.T(j6, 86400000000000L);
        return Z(localDate.plusDays(jU), jT == jC0 ? this.b : i.V(jT));
    }

    @Override // j$.time.temporal.n
    public final Object t(e eVar) {
        if (eVar == j$.time.temporal.s.f) {
            return this.a;
        }
        return j$.com.android.tools.r8.a.u(this, eVar);
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.a(this);
    }

    @Override // java.lang.Comparable
    /* JADX INFO: renamed from: H, reason: merged with bridge method [inline-methods] */
    public final int compareTo(ChronoLocalDateTime chronoLocalDateTime) {
        if (chronoLocalDateTime instanceof LocalDateTime) {
            return Q((LocalDateTime) chronoLocalDateTime);
        }
        return j$.com.android.tools.r8.a.f(this, chronoLocalDateTime);
    }

    public final int Q(LocalDateTime localDateTime) {
        int iQ = this.a.Q(localDateTime.a);
        return iQ == 0 ? this.b.compareTo(localDateTime.b) : iQ;
    }

    public final boolean S(ChronoLocalDateTime chronoLocalDateTime) {
        if (chronoLocalDateTime instanceof LocalDateTime) {
            return Q((LocalDateTime) chronoLocalDateTime) < 0;
        }
        long jE = this.a.E();
        long jE2 = chronoLocalDateTime.f().E();
        if (jE >= jE2) {
            return jE == jE2 && this.b.c0() < chronoLocalDateTime.b().c0();
        }
        return true;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) obj;
            if (this.a.equals(localDateTime.a) && this.b.equals(localDateTime.b)) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return this.a.hashCode() ^ this.b.hashCode();
    }

    public final String toString() {
        return this.a.toString() + "T" + this.b.toString();
    }

    private Object writeReplace() {
        return new p((byte) 5, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
