package j$.time.chrono;

import j$.time.Instant;
import j$.time.LocalDate;
import j$.time.LocalDateTime;
import j$.time.ZoneId;
import j$.time.ZonedDateTime;
import j$.util.Objects;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public final class r extends AbstractC0162a implements Serializable {
    public static final r c = new r();
    private static final long serialVersionUID = -1440403870442975015L;

    @Override // j$.time.chrono.k
    public final l u(int i) {
        if (i == 0) {
            return s.BCE;
        }
        if (i == 1) {
            return s.CE;
        }
        throw new j$.time.b("Invalid era: " + i);
    }

    @Override // j$.time.chrono.k
    public final String getId() {
        return "ISO";
    }

    @Override // j$.time.chrono.k
    public final String l() {
        return "iso8601";
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b I(int i, int i2, int i3) {
        return LocalDate.of(i, i2, i3);
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b m(int i, int i2) {
        return LocalDate.c0(i, i2);
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b h(long j) {
        return LocalDate.b0(j);
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b A(j$.time.temporal.n nVar) {
        return LocalDate.S(nVar);
    }

    private r() {
    }

    @Override // j$.time.chrono.AbstractC0162a, j$.time.chrono.k
    public final ChronoLocalDateTime B(LocalDateTime localDateTime) {
        return LocalDateTime.R(localDateTime);
    }

    @Override // j$.time.chrono.k
    public final ChronoZonedDateTime L(Instant instant, ZoneId zoneId) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zoneId, "zone");
        return ZonedDateTime.n(instant.a, instant.b, zoneId);
    }

    @Override // j$.time.chrono.AbstractC0162a
    public final InterfaceC0163b j() {
        j$.time.a aVarZ = j$.com.android.tools.r8.a.Z();
        Objects.requireNonNull(aVarZ, "clock");
        return LocalDate.S(LocalDate.a0(aVarZ));
    }

    @Override // j$.time.chrono.k
    public final boolean O(long j) {
        if ((3 & j) == 0) {
            return j % 100 != 0 || j % 400 == 0;
        }
        return false;
    }

    @Override // j$.time.chrono.k
    public final int v(l lVar, int i) {
        if (lVar instanceof s) {
            return lVar == s.CE ? i : 1 - i;
        }
        throw new ClassCastException("Era must be IsoEra");
    }

    @Override // j$.time.chrono.k
    public final List s() {
        return j$.com.android.tools.r8.a.Q(s.values());
    }

    @Override // j$.time.chrono.AbstractC0162a, j$.time.chrono.k
    public final InterfaceC0163b K(Map map, j$.time.format.E e) {
        return (LocalDate) super.K(map, e);
    }

    @Override // j$.time.chrono.AbstractC0162a
    public final void t(Map map, j$.time.format.E e) {
        j$.time.temporal.a aVar = j$.time.temporal.a.PROLEPTIC_MONTH;
        Long l = (Long) map.remove(aVar);
        if (l != null) {
            if (e != j$.time.format.E.LENIENT) {
                aVar.D(l.longValue());
            }
            long j = 12;
            AbstractC0162a.i(map, j$.time.temporal.a.MONTH_OF_YEAR, ((int) j$.com.android.tools.r8.a.T(l.longValue(), j)) + 1);
            AbstractC0162a.i(map, j$.time.temporal.a.YEAR, j$.com.android.tools.r8.a.U(l.longValue(), j));
        }
    }

    @Override // j$.time.chrono.AbstractC0162a
    public final InterfaceC0163b D(Map map, j$.time.format.E e) {
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR_OF_ERA;
        Long l = (Long) map.remove(aVar);
        if (l != null) {
            if (e != j$.time.format.E.LENIENT) {
                aVar.D(l.longValue());
            }
            Long l2 = (Long) map.remove(j$.time.temporal.a.ERA);
            if (l2 != null) {
                if (l2.longValue() == 1) {
                    AbstractC0162a.i(map, j$.time.temporal.a.YEAR, l.longValue());
                    return null;
                }
                if (l2.longValue() == 0) {
                    AbstractC0162a.i(map, j$.time.temporal.a.YEAR, j$.com.android.tools.r8.a.W(1L, l.longValue()));
                    return null;
                }
                throw new j$.time.b("Invalid value for era: " + l2);
            }
            j$.time.temporal.a aVar2 = j$.time.temporal.a.YEAR;
            Long l3 = (Long) map.get(aVar2);
            if (e != j$.time.format.E.STRICT) {
                AbstractC0162a.i(map, aVar2, (l3 == null || l3.longValue() > 0) ? l.longValue() : j$.com.android.tools.r8.a.W(1L, l.longValue()));
                return null;
            }
            if (l3 != null) {
                long jLongValue = l3.longValue();
                long jLongValue2 = l.longValue();
                if (jLongValue <= 0) {
                    jLongValue2 = j$.com.android.tools.r8.a.W(1L, jLongValue2);
                }
                AbstractC0162a.i(map, aVar2, jLongValue2);
                return null;
            }
            map.put(aVar, l);
            return null;
        }
        j$.time.temporal.a aVar3 = j$.time.temporal.a.ERA;
        if (!map.containsKey(aVar3)) {
            return null;
        }
        aVar3.D(((Long) map.get(aVar3)).longValue());
        return null;
    }

    @Override // j$.time.chrono.AbstractC0162a
    public final InterfaceC0163b y(Map map, j$.time.format.E e) {
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR;
        int iA = aVar.b.a(((Long) map.remove(aVar)).longValue(), aVar);
        boolean z = true;
        if (e == j$.time.format.E.LENIENT) {
            return LocalDate.of(iA, 1, 1).e0(j$.com.android.tools.r8.a.W(((Long) map.remove(j$.time.temporal.a.MONTH_OF_YEAR)).longValue(), 1L)).plusDays(j$.com.android.tools.r8.a.W(((Long) map.remove(j$.time.temporal.a.DAY_OF_MONTH)).longValue(), 1L));
        }
        j$.time.temporal.a aVar2 = j$.time.temporal.a.MONTH_OF_YEAR;
        int iA2 = aVar2.b.a(((Long) map.remove(aVar2)).longValue(), aVar2);
        j$.time.temporal.a aVar3 = j$.time.temporal.a.DAY_OF_MONTH;
        int iA3 = aVar3.b.a(((Long) map.remove(aVar3)).longValue(), aVar3);
        if (e == j$.time.format.E.SMART) {
            if (iA2 == 4 || iA2 == 6 || iA2 == 9 || iA2 == 11) {
                iA3 = Math.min(iA3, 30);
            } else if (iA2 == 2) {
                j$.time.k kVar = j$.time.k.FEBRUARY;
                long j = iA;
                int i = j$.time.r.b;
                if ((3 & j) != 0 || (j % 100 == 0 && j % 400 != 0)) {
                    z = false;
                }
                iA3 = Math.min(iA3, kVar.R(z));
            }
        }
        return LocalDate.of(iA, iA2, iA3);
    }

    @Override // j$.time.chrono.k
    public final j$.time.temporal.v q(j$.time.temporal.a aVar) {
        return aVar.b;
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    public Object writeReplace() {
        return new D((byte) 1, this);
    }
}
