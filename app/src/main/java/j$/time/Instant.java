package j$.time;

import j$.time.format.DateTimeFormatter;
import j$.util.Objects;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.telegram.messenger.MediaDataController;

public final class Instant implements j$.time.temporal.m, j$.time.temporal.o, Comparable<Instant>, Serializable {
    public static final Instant c = new Instant(0, 0);
    private static final long serialVersionUID = -665713676816604388L;
    public final long a;
    public final int b;

    public static Instant now() {
        a.b.getClass();
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = MediaDataController.MAX_STYLE_RUNS_COUNT;
        return Q(j$.com.android.tools.r8.a.U(jCurrentTimeMillis, j), ((int) j$.com.android.tools.r8.a.T(jCurrentTimeMillis, j)) * 1000000);
    }

    @Override // java.lang.Comparable
    public final int compareTo(Instant instant) {
        Instant instant2 = instant;
        int iCompare = Long.compare(this.a, instant2.a);
        return iCompare != 0 ? iCompare : this.b - instant2.b;
    }

    static {
        S(-31557014167219200L, 0L);
        S(31556889864403199L, 999999999L);
    }

    public static Instant S(long j, long j2) {
        return Q(j$.com.android.tools.r8.a.P(j, j$.com.android.tools.r8.a.U(j2, 1000000000L)), (int) j$.com.android.tools.r8.a.T(j2, 1000000000L));
    }

    public static Instant R(j$.time.temporal.n nVar) {
        if (nVar instanceof Instant) {
            return (Instant) nVar;
        }
        Objects.requireNonNull(nVar, "temporal");
        try {
            return S(nVar.D(j$.time.temporal.a.INSTANT_SECONDS), nVar.i(j$.time.temporal.a.NANO_OF_SECOND));
        } catch (b e) {
            throw new b("Unable to obtain Instant from TemporalAccessor: " + nVar + " of type " + nVar.getClass().getName(), e);
        }
    }

    public static Instant Q(long j, int i) {
        if ((((long) i) | j) == 0) {
            return c;
        }
        if (j < -31557014167219200L || j > 31556889864403199L) {
            throw new b("Instant exceeds minimum or maximum instant");
        }
        return new Instant(j, i);
    }

    public Instant(long j, int i) {
        this.a = j;
        this.b = i;
    }

    @Override // j$.time.temporal.n
    public final boolean e(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return rVar == j$.time.temporal.a.INSTANT_SECONDS || rVar == j$.time.temporal.a.NANO_OF_SECOND || rVar == j$.time.temporal.a.MICRO_OF_SECOND || rVar == j$.time.temporal.a.MILLI_OF_SECOND;
        }
        return rVar != null && rVar.i(this);
    }

    @Override // j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        return j$.time.temporal.s.d(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final int i(j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return j$.time.temporal.s.d(this, rVar).a(rVar.t(this), rVar);
        }
        int i = d.a[((j$.time.temporal.a) rVar).ordinal()];
        if (i == 1) {
            return this.b;
        }
        if (i == 2) {
            return this.b / MediaDataController.MAX_STYLE_RUNS_COUNT;
        }
        if (i == 3) {
            return this.b / 1000000;
        }
        if (i == 4) {
            j$.time.temporal.a aVar = j$.time.temporal.a.INSTANT_SECONDS;
            aVar.b.a(this.a, aVar);
        }
        throw new j$.time.temporal.u(c.a("Unsupported field: ", rVar));
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        int i;
        if (!(rVar instanceof j$.time.temporal.a)) {
            return rVar.t(this);
        }
        int i2 = d.a[((j$.time.temporal.a) rVar).ordinal()];
        if (i2 == 1) {
            i = this.b;
        } else if (i2 == 2) {
            i = this.b / MediaDataController.MAX_STYLE_RUNS_COUNT;
        } else {
            if (i2 != 3) {
                if (i2 == 4) {
                    return this.a;
                }
                throw new j$.time.temporal.u(c.a("Unsupported field: ", rVar));
            }
            i = this.b / 1000000;
        }
        return i;
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: j */
    public final j$.time.temporal.m x(LocalDate localDate) {
        localDate.getClass();
        return (Instant) j$.com.android.tools.r8.a.a(localDate, this);
    }

    @Override // j$.time.temporal.m
    public final j$.time.temporal.m c(long j, j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return (Instant) rVar.y(this, j);
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
        aVar.D(j);
        int i = d.a[aVar.ordinal()];
        if (i != 1) {
            if (i == 2) {
                int i2 = ((int) j) * MediaDataController.MAX_STYLE_RUNS_COUNT;
                if (i2 != this.b) {
                    return Q(this.a, i2);
                }
            } else if (i == 3) {
                int i3 = ((int) j) * 1000000;
                if (i3 != this.b) {
                    return Q(this.a, i3);
                }
            } else {
                if (i != 4) {
                    throw new j$.time.temporal.u(c.a("Unsupported field: ", rVar));
                }
                if (j != this.a) {
                    return Q(j, this.b);
                }
            }
        } else if (j != this.b) {
            return Q(this.a, (int) j);
        }
        return this;
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: U, reason: merged with bridge method [inline-methods] */
    public final Instant d(long j, j$.time.temporal.t tVar) {
        if (!(tVar instanceof j$.time.temporal.b)) {
            return (Instant) tVar.i(this, j);
        }
        switch (d.b[((j$.time.temporal.b) tVar).ordinal()]) {
            case 1:
                return T(0L, j);
            case 2:
                return T(j / 1000000, (j % 1000000) * 1000);
            case 3:
                return T(j / 1000, (j % 1000) * 1000000);
            case 4:
                return plusSeconds(j);
            case 5:
                return plusSeconds(j$.com.android.tools.r8.a.V(j, 60));
            case 6:
                return plusSeconds(j$.com.android.tools.r8.a.V(j, 3600));
            case 7:
                return plusSeconds(j$.com.android.tools.r8.a.V(j, 43200));
            case 8:
                return plusSeconds(j$.com.android.tools.r8.a.V(j, 86400));
            default:
                throw new j$.time.temporal.u("Unsupported unit: " + tVar);
        }
    }

    public Instant plusSeconds(long j) {
        return T(j, 0L);
    }

    public final Instant T(long j, long j2) {
        if ((j | j2) == 0) {
            return this;
        }
        return S(j$.com.android.tools.r8.a.P(j$.com.android.tools.r8.a.P(this.a, j), j2 / 1000000000), ((long) this.b) + (j2 % 1000000000));
    }

    @Override // j$.time.temporal.m
    public final j$.time.temporal.m y(long j, j$.time.temporal.b bVar) {
        return j == Long.MIN_VALUE ? d(Long.MAX_VALUE, bVar).d(1L, bVar) : d(-j, bVar);
    }

    @Override // j$.time.temporal.n
    public final Object t(e eVar) {
        if (eVar == j$.time.temporal.s.c) {
            return j$.time.temporal.b.NANOS;
        }
        if (eVar == j$.time.temporal.s.b || eVar == j$.time.temporal.s.a || eVar == j$.time.temporal.s.e || eVar == j$.time.temporal.s.d || eVar == j$.time.temporal.s.f || eVar == j$.time.temporal.s.g) {
            return null;
        }
        return eVar.g(this);
    }

    @Override // j$.time.temporal.o
    public final j$.time.temporal.m n(j$.time.temporal.m mVar) {
        return mVar.c(this.a, j$.time.temporal.a.INSTANT_SECONDS).c(this.b, j$.time.temporal.a.NANO_OF_SECOND);
    }

    public OffsetDateTime atOffset(ZoneOffset zoneOffset) {
        return OffsetDateTime.Q(this, zoneOffset);
    }

    public long toEpochMilli() {
        long j = this.a;
        return (j >= 0 || this.b <= 0) ? j$.com.android.tools.r8.a.P(j$.com.android.tools.r8.a.V(j, MediaDataController.MAX_STYLE_RUNS_COUNT), this.b / 1000000) : j$.com.android.tools.r8.a.P(j$.com.android.tools.r8.a.V(j + 1, MediaDataController.MAX_STYLE_RUNS_COUNT), (this.b / 1000000) - MediaDataController.MAX_STYLE_RUNS_COUNT);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Instant) {
            Instant instant = (Instant) obj;
            if (this.a == instant.a && this.b == instant.b) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        long j = this.a;
        return (this.b * 51) + ((int) (j ^ (j >>> 32)));
    }

    public String toString() {
        return DateTimeFormatter.f.a(this);
    }

    private Object writeReplace() {
        return new p((byte) 2, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
