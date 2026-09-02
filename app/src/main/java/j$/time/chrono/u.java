package j$.time.chrono;

import j$.time.Instant;
import j$.time.LocalDate;
import j$.time.ZoneId;
import j$.util.Objects;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import okhttp3.internal.http2.Http2Connection;

public final class u extends AbstractC0162a implements Serializable {
    public static final u c = new u();
    private static final long serialVersionUID = 459996390165777884L;

    @Override // j$.time.chrono.k
    public final String getId() {
        return "Japanese";
    }

    @Override // j$.time.chrono.k
    public final String l() {
        return "japanese";
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b I(int i, int i2, int i3) {
        return new w(LocalDate.of(i, i2, i3));
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b m(int i, int i2) {
        return new w(LocalDate.c0(i, i2));
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b h(long j) {
        return new w(LocalDate.b0(j));
    }

    @Override // j$.time.chrono.AbstractC0162a
    public final InterfaceC0163b j() {
        return new w(LocalDate.S(LocalDate.a0(j$.com.android.tools.r8.a.Z())));
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b A(j$.time.temporal.n nVar) {
        if (nVar instanceof w) {
            return (w) nVar;
        }
        return new w(LocalDate.S(nVar));
    }

    @Override // j$.time.chrono.k
    public final List s() {
        x[] xVarArr = x.e;
        return j$.com.android.tools.r8.a.Q((x[]) Arrays.copyOf(xVarArr, xVarArr.length));
    }

    @Override // j$.time.chrono.k
    public final boolean O(long j) {
        return r.c.O(j);
    }

    private u() {
    }

    @Override // j$.time.chrono.k
    public final int v(l lVar, int i) {
        if (!(lVar instanceof x)) {
            throw new ClassCastException("Era must be JapaneseEra");
        }
        x xVar = (x) lVar;
        int year = (xVar.b.getYear() + i) - 1;
        if (i != 1 && (year < -999999999 || year > 999999999 || year < xVar.b.getYear() || lVar != x.h(LocalDate.of(year, 1, 1)))) {
            throw new j$.time.b("Invalid yearOfEra value");
        }
        return year;
    }

    @Override // j$.time.chrono.k
    public final l u(int i) {
        return x.m(i);
    }

    @Override // j$.time.chrono.k
    public final j$.time.temporal.v q(j$.time.temporal.a aVar) {
        switch (t.a[aVar.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
                throw new j$.time.temporal.u("Unsupported field: " + aVar);
            case 5:
                x[] xVarArr = x.e;
                int year = xVarArr[xVarArr.length - 1].b.getYear();
                int year2 = Http2Connection.DEGRADED_PONG_TIMEOUT_NS - xVarArr[xVarArr.length - 1].b.getYear();
                int year3 = xVarArr[0].b.getYear();
                int i = 1;
                while (true) {
                    x[] xVarArr2 = x.e;
                    if (i >= xVarArr2.length) {
                        return j$.time.temporal.v.g(1L, year2, 999999999 - year);
                    }
                    x xVar = xVarArr2[i];
                    year2 = Math.min(year2, (xVar.b.getYear() - year3) + 1);
                    year3 = xVar.b.getYear();
                    i++;
                }
                break;
            case 6:
                x xVar2 = x.d;
                long j = j$.time.temporal.a.DAY_OF_YEAR.b.c;
                long jMin = j;
                for (x xVar3 : x.e) {
                    long jMin2 = Math.min(jMin, (xVar3.b.M() - xVar3.b.V()) + 1);
                    jMin = xVar3.l() != null ? Math.min(jMin2, xVar3.l().b.V() - 1) : jMin2;
                }
                return j$.time.temporal.v.g(1L, jMin, j$.time.temporal.a.DAY_OF_YEAR.b.d);
            case 7:
                return j$.time.temporal.v.f(w.d.getYear(), 999999999L);
            case 8:
                long j2 = x.d.a;
                x[] xVarArr3 = x.e;
                return j$.time.temporal.v.f(j2, xVarArr3[xVarArr3.length - 1].a);
            default:
                return aVar.b;
        }
    }

    @Override // j$.time.chrono.AbstractC0162a, j$.time.chrono.k
    public final InterfaceC0163b K(Map map, j$.time.format.E e) {
        return (w) super.K(map, e);
    }

    @Override // j$.time.chrono.AbstractC0162a
    public final InterfaceC0163b D(Map map, j$.time.format.E e) {
        w wVarW;
        j$.time.temporal.a aVar = j$.time.temporal.a.ERA;
        Long l = (Long) map.get(aVar);
        x xVarM = l != null ? x.m(q(aVar).a(l.longValue(), aVar)) : null;
        j$.time.temporal.a aVar2 = j$.time.temporal.a.YEAR_OF_ERA;
        Long l2 = (Long) map.get(aVar2);
        int iA = l2 != null ? q(aVar2).a(l2.longValue(), aVar2) : 0;
        if (xVarM == null && l2 != null && !map.containsKey(j$.time.temporal.a.YEAR) && e != j$.time.format.E.STRICT) {
            x[] xVarArr = x.e;
            xVarM = ((x[]) Arrays.copyOf(xVarArr, xVarArr.length))[((x[]) Arrays.copyOf(xVarArr, xVarArr.length)).length - 1];
        }
        if (l2 != null && xVarM != null) {
            j$.time.temporal.a aVar3 = j$.time.temporal.a.MONTH_OF_YEAR;
            if (map.containsKey(aVar3)) {
                j$.time.temporal.a aVar4 = j$.time.temporal.a.DAY_OF_MONTH;
                if (map.containsKey(aVar4)) {
                    map.remove(aVar);
                    map.remove(aVar2);
                    if (e == j$.time.format.E.LENIENT) {
                        return new w(LocalDate.of((xVarM.b.getYear() + iA) - 1, 1, 1)).U(j$.com.android.tools.r8.a.W(((Long) map.remove(aVar3)).longValue(), 1L), j$.time.temporal.b.MONTHS).U(j$.com.android.tools.r8.a.W(((Long) map.remove(aVar4)).longValue(), 1L), j$.time.temporal.b.DAYS);
                    }
                    int iA2 = q(aVar3).a(((Long) map.remove(aVar3)).longValue(), aVar3);
                    int iA3 = q(aVar4).a(((Long) map.remove(aVar4)).longValue(), aVar4);
                    if (e != j$.time.format.E.SMART) {
                        LocalDate localDate = w.d;
                        Objects.requireNonNull(xVarM, "era");
                        LocalDate localDateOf = LocalDate.of((xVarM.b.getYear() + iA) - 1, iA2, iA3);
                        if (localDateOf.X(xVarM.b) || xVarM != x.h(localDateOf)) {
                            throw new j$.time.b("year, month, and day not valid for Era");
                        }
                        return new w(xVarM, iA, localDateOf);
                    }
                    if (iA < 1) {
                        throw new j$.time.b("Invalid YearOfEra: " + iA);
                    }
                    int year = (xVarM.b.getYear() + iA) - 1;
                    try {
                        wVarW = new w(LocalDate.of(year, iA2, iA3));
                    } catch (j$.time.b unused) {
                        wVarW = new w(LocalDate.of(year, iA2, 1)).W(new j$.time.e(2));
                    }
                    if (wVarW.b == xVarM || j$.time.temporal.s.a(wVarW, j$.time.temporal.a.YEAR_OF_ERA) <= 1 || iA <= 1) {
                        return wVarW;
                    }
                    throw new j$.time.b("Invalid YearOfEra for Era: " + xVarM + " " + iA);
                }
            }
            j$.time.temporal.a aVar5 = j$.time.temporal.a.DAY_OF_YEAR;
            if (map.containsKey(aVar5)) {
                map.remove(aVar);
                map.remove(aVar2);
                if (e == j$.time.format.E.LENIENT) {
                    return new w(LocalDate.c0((xVarM.b.getYear() + iA) - 1, 1)).U(j$.com.android.tools.r8.a.W(((Long) map.remove(aVar5)).longValue(), 1L), j$.time.temporal.b.DAYS);
                }
                int iA4 = q(aVar5).a(((Long) map.remove(aVar5)).longValue(), aVar5);
                LocalDate localDate2 = w.d;
                Objects.requireNonNull(xVarM, "era");
                LocalDate localDateC0 = iA == 1 ? LocalDate.c0(xVarM.b.getYear(), (xVarM.b.V() + iA4) - 1) : LocalDate.c0((xVarM.b.getYear() + iA) - 1, iA4);
                if (localDateC0.X(xVarM.b) || xVarM != x.h(localDateC0)) {
                    throw new j$.time.b("Invalid parameters");
                }
                return new w(xVarM, iA, localDateC0);
            }
        }
        return null;
    }

    @Override // j$.time.chrono.k
    public final ChronoZonedDateTime L(Instant instant, ZoneId zoneId) {
        return j.R(this, instant, zoneId);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    public Object writeReplace() {
        return new D((byte) 1, this);
    }
}
