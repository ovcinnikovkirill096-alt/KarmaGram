package j$.time.chrono;

import j$.time.DayOfWeek;
import j$.time.LocalDateTime;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: renamed from: j$.time.chrono.a, reason: case insensitive filesystem */
public abstract class AbstractC0162a implements k {
    public static final ConcurrentHashMap a = new ConcurrentHashMap();
    public static final ConcurrentHashMap b = new ConcurrentHashMap();

    public abstract /* synthetic */ InterfaceC0163b j();

    @Override // java.lang.Comparable
    public final int compareTo(Object obj) {
        return getId().compareTo(((k) obj).getId());
    }

    static {
        new Locale("ja", "JP", "JP");
    }

    public static k k(k kVar, String str) {
        String strL;
        k kVar2 = (k) a.putIfAbsent(str, kVar);
        if (kVar2 == null && (strL = kVar.l()) != null) {
            b.putIfAbsent(strL, kVar);
        }
        return kVar2;
    }

    @Override // j$.time.chrono.k
    public InterfaceC0163b K(Map map, j$.time.format.E e) {
        j$.time.temporal.a aVar = j$.time.temporal.a.EPOCH_DAY;
        if (map.containsKey(aVar)) {
            return h(((Long) map.remove(aVar)).longValue());
        }
        t(map, e);
        InterfaceC0163b interfaceC0163bD = D(map, e);
        if (interfaceC0163bD != null) {
            return interfaceC0163bD;
        }
        j$.time.temporal.a aVar2 = j$.time.temporal.a.YEAR;
        if (!map.containsKey(aVar2)) {
            return null;
        }
        j$.time.temporal.a aVar3 = j$.time.temporal.a.MONTH_OF_YEAR;
        if (map.containsKey(aVar3)) {
            if (map.containsKey(j$.time.temporal.a.DAY_OF_MONTH)) {
                return y(map, e);
            }
            j$.time.temporal.a aVar4 = j$.time.temporal.a.ALIGNED_WEEK_OF_MONTH;
            if (map.containsKey(aVar4)) {
                j$.time.temporal.a aVar5 = j$.time.temporal.a.ALIGNED_DAY_OF_WEEK_IN_MONTH;
                if (map.containsKey(aVar5)) {
                    int iA = q(aVar2).a(((Long) map.remove(aVar2)).longValue(), aVar2);
                    if (e == j$.time.format.E.LENIENT) {
                        long jW = j$.com.android.tools.r8.a.W(((Long) map.remove(aVar3)).longValue(), 1L);
                        return I(iA, 1, 1).d(jW, (j$.time.temporal.t) j$.time.temporal.b.MONTHS).d(j$.com.android.tools.r8.a.W(((Long) map.remove(aVar4)).longValue(), 1L), (j$.time.temporal.t) j$.time.temporal.b.WEEKS).d(j$.com.android.tools.r8.a.W(((Long) map.remove(aVar5)).longValue(), 1L), (j$.time.temporal.t) j$.time.temporal.b.DAYS);
                    }
                    int iA2 = q(aVar3).a(((Long) map.remove(aVar3)).longValue(), aVar3);
                    InterfaceC0163b interfaceC0163bD2 = I(iA, iA2, 1).d((q(aVar5).a(((Long) map.remove(aVar5)).longValue(), aVar5) - 1) + ((q(aVar4).a(((Long) map.remove(aVar4)).longValue(), aVar4) - 1) * 7), (j$.time.temporal.t) j$.time.temporal.b.DAYS);
                    if (e != j$.time.format.E.STRICT || interfaceC0163bD2.i(aVar3) == iA2) {
                        return interfaceC0163bD2;
                    }
                    throw new j$.time.b("Strict mode rejected resolved date as it is in a different month");
                }
                j$.time.temporal.a aVar6 = j$.time.temporal.a.DAY_OF_WEEK;
                if (map.containsKey(aVar6)) {
                    int iA3 = q(aVar2).a(((Long) map.remove(aVar2)).longValue(), aVar2);
                    if (e == j$.time.format.E.LENIENT) {
                        return n(I(iA3, 1, 1), j$.com.android.tools.r8.a.W(((Long) map.remove(aVar3)).longValue(), 1L), j$.com.android.tools.r8.a.W(((Long) map.remove(aVar4)).longValue(), 1L), j$.com.android.tools.r8.a.W(((Long) map.remove(aVar6)).longValue(), 1L));
                    }
                    int iA4 = q(aVar3).a(((Long) map.remove(aVar3)).longValue(), aVar3);
                    InterfaceC0163b interfaceC0163bX = I(iA3, iA4, 1).d((q(aVar4).a(((Long) map.remove(aVar4)).longValue(), aVar4) - 1) * 7, (j$.time.temporal.t) j$.time.temporal.b.DAYS).x(new j$.time.temporal.p(DayOfWeek.Q(q(aVar6).a(((Long) map.remove(aVar6)).longValue(), aVar6)).getValue(), 0));
                    if (e != j$.time.format.E.STRICT || interfaceC0163bX.i(aVar3) == iA4) {
                        return interfaceC0163bX;
                    }
                    throw new j$.time.b("Strict mode rejected resolved date as it is in a different month");
                }
            }
        }
        j$.time.temporal.a aVar7 = j$.time.temporal.a.DAY_OF_YEAR;
        if (map.containsKey(aVar7)) {
            int iA5 = q(aVar2).a(((Long) map.remove(aVar2)).longValue(), aVar2);
            if (e != j$.time.format.E.LENIENT) {
                return m(iA5, q(aVar7).a(((Long) map.remove(aVar7)).longValue(), aVar7));
            }
            return m(iA5, 1).d(j$.com.android.tools.r8.a.W(((Long) map.remove(aVar7)).longValue(), 1L), (j$.time.temporal.t) j$.time.temporal.b.DAYS);
        }
        j$.time.temporal.a aVar8 = j$.time.temporal.a.ALIGNED_WEEK_OF_YEAR;
        if (!map.containsKey(aVar8)) {
            return null;
        }
        j$.time.temporal.a aVar9 = j$.time.temporal.a.ALIGNED_DAY_OF_WEEK_IN_YEAR;
        if (map.containsKey(aVar9)) {
            int iA6 = q(aVar2).a(((Long) map.remove(aVar2)).longValue(), aVar2);
            if (e == j$.time.format.E.LENIENT) {
                return m(iA6, 1).d(j$.com.android.tools.r8.a.W(((Long) map.remove(aVar8)).longValue(), 1L), (j$.time.temporal.t) j$.time.temporal.b.WEEKS).d(j$.com.android.tools.r8.a.W(((Long) map.remove(aVar9)).longValue(), 1L), (j$.time.temporal.t) j$.time.temporal.b.DAYS);
            }
            InterfaceC0163b interfaceC0163bD3 = m(iA6, 1).d((q(aVar9).a(((Long) map.remove(aVar9)).longValue(), aVar9) - 1) + ((q(aVar8).a(((Long) map.remove(aVar8)).longValue(), aVar8) - 1) * 7), (j$.time.temporal.t) j$.time.temporal.b.DAYS);
            if (e != j$.time.format.E.STRICT || interfaceC0163bD3.i(aVar2) == iA6) {
                return interfaceC0163bD3;
            }
            throw new j$.time.b("Strict mode rejected resolved date as it is in a different year");
        }
        j$.time.temporal.a aVar10 = j$.time.temporal.a.DAY_OF_WEEK;
        if (!map.containsKey(aVar10)) {
            return null;
        }
        int iA7 = q(aVar2).a(((Long) map.remove(aVar2)).longValue(), aVar2);
        if (e == j$.time.format.E.LENIENT) {
            return n(m(iA7, 1), 0L, j$.com.android.tools.r8.a.W(((Long) map.remove(aVar8)).longValue(), 1L), j$.com.android.tools.r8.a.W(((Long) map.remove(aVar10)).longValue(), 1L));
        }
        InterfaceC0163b interfaceC0163bX2 = m(iA7, 1).d((q(aVar8).a(((Long) map.remove(aVar8)).longValue(), aVar8) - 1) * 7, (j$.time.temporal.t) j$.time.temporal.b.DAYS).x(new j$.time.temporal.p(DayOfWeek.Q(q(aVar10).a(((Long) map.remove(aVar10)).longValue(), aVar10)).getValue(), 0));
        if (e != j$.time.format.E.STRICT || interfaceC0163bX2.i(aVar2) == iA7) {
            return interfaceC0163bX2;
        }
        throw new j$.time.b("Strict mode rejected resolved date as it is in a different year");
    }

    @Override // j$.time.chrono.k
    public ChronoLocalDateTime B(LocalDateTime localDateTime) {
        try {
            return A(localDateTime).F(j$.time.i.S(localDateTime));
        } catch (j$.time.b e) {
            throw new j$.time.b("Unable to obtain ChronoLocalDateTime from TemporalAccessor: " + LocalDateTime.class, e);
        }
    }

    public void t(Map map, j$.time.format.E e) {
        j$.time.temporal.a aVar = j$.time.temporal.a.PROLEPTIC_MONTH;
        Long l = (Long) map.remove(aVar);
        if (l != null) {
            if (e != j$.time.format.E.LENIENT) {
                aVar.D(l.longValue());
            }
            InterfaceC0163b interfaceC0163bC = j().c(1L, (j$.time.temporal.r) j$.time.temporal.a.DAY_OF_MONTH).c(l.longValue(), (j$.time.temporal.r) aVar);
            j$.time.temporal.a aVar2 = j$.time.temporal.a.MONTH_OF_YEAR;
            i(map, aVar2, interfaceC0163bC.i(aVar2));
            j$.time.temporal.a aVar3 = j$.time.temporal.a.YEAR;
            i(map, aVar3, interfaceC0163bC.i(aVar3));
        }
    }

    public InterfaceC0163b D(Map map, j$.time.format.E e) {
        int iO;
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR_OF_ERA;
        Long l = (Long) map.remove(aVar);
        if (l != null) {
            j$.time.temporal.a aVar2 = j$.time.temporal.a.ERA;
            Long l2 = (Long) map.remove(aVar2);
            if (e != j$.time.format.E.LENIENT) {
                iO = q(aVar).a(l.longValue(), aVar);
            } else {
                iO = j$.com.android.tools.r8.a.O(l.longValue());
            }
            if (l2 != null) {
                i(map, j$.time.temporal.a.YEAR, v(u(q(aVar2).a(l2.longValue(), aVar2)), iO));
                return null;
            }
            j$.time.temporal.a aVar3 = j$.time.temporal.a.YEAR;
            if (map.containsKey(aVar3)) {
                i(map, aVar3, v(m(q(aVar3).a(((Long) map.get(aVar3)).longValue(), aVar3), 1).G(), iO));
                return null;
            }
            if (e == j$.time.format.E.STRICT) {
                map.put(aVar, l);
                return null;
            }
            List listS = s();
            if (listS.isEmpty()) {
                i(map, aVar3, iO);
                return null;
            }
            i(map, aVar3, v((l) listS.get(listS.size() - 1), iO));
            return null;
        }
        j$.time.temporal.a aVar4 = j$.time.temporal.a.ERA;
        if (!map.containsKey(aVar4)) {
            return null;
        }
        q(aVar4).b(((Long) map.get(aVar4)).longValue(), aVar4);
        return null;
    }

    public InterfaceC0163b y(Map map, j$.time.format.E e) {
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR;
        int iA = q(aVar).a(((Long) map.remove(aVar)).longValue(), aVar);
        if (e == j$.time.format.E.LENIENT) {
            long jW = j$.com.android.tools.r8.a.W(((Long) map.remove(j$.time.temporal.a.MONTH_OF_YEAR)).longValue(), 1L);
            return I(iA, 1, 1).d(jW, (j$.time.temporal.t) j$.time.temporal.b.MONTHS).d(j$.com.android.tools.r8.a.W(((Long) map.remove(j$.time.temporal.a.DAY_OF_MONTH)).longValue(), 1L), (j$.time.temporal.t) j$.time.temporal.b.DAYS);
        }
        j$.time.temporal.a aVar2 = j$.time.temporal.a.MONTH_OF_YEAR;
        int iA2 = q(aVar2).a(((Long) map.remove(aVar2)).longValue(), aVar2);
        j$.time.temporal.a aVar3 = j$.time.temporal.a.DAY_OF_MONTH;
        int iA3 = q(aVar3).a(((Long) map.remove(aVar3)).longValue(), aVar3);
        if (e != j$.time.format.E.SMART) {
            return I(iA, iA2, iA3);
        }
        try {
            return I(iA, iA2, iA3);
        } catch (j$.time.b unused) {
            return I(iA, iA2, 1).x(new j$.time.e(2));
        }
    }

    public static InterfaceC0163b n(InterfaceC0163b interfaceC0163b, long j, long j2, long j3) {
        long j4;
        InterfaceC0163b interfaceC0163bD = interfaceC0163b.d(j, (j$.time.temporal.t) j$.time.temporal.b.MONTHS);
        j$.time.temporal.b bVar = j$.time.temporal.b.WEEKS;
        InterfaceC0163b interfaceC0163bD2 = interfaceC0163bD.d(j2, (j$.time.temporal.t) bVar);
        if (j3 > 7) {
            long j5 = j3 - 1;
            interfaceC0163bD2 = interfaceC0163bD2.d(j5 / 7, (j$.time.temporal.t) bVar);
            j4 = j5 % 7;
        } else {
            if (j3 < 1) {
                interfaceC0163bD2 = interfaceC0163bD2.d(j$.com.android.tools.r8.a.W(j3, 7L) / 7, (j$.time.temporal.t) bVar);
                j4 = (j3 + 6) % 7;
            }
            return interfaceC0163bD2.x(new j$.time.temporal.p(DayOfWeek.Q((int) j3).getValue(), 0));
        }
        j3 = j4 + 1;
        return interfaceC0163bD2.x(new j$.time.temporal.p(DayOfWeek.Q((int) j3).getValue(), 0));
    }

    public static void i(Map map, j$.time.temporal.a aVar, long j) {
        Long l = (Long) map.get(aVar);
        if (l != null && l.longValue() != j) {
            throw new j$.time.b("Conflict found: " + aVar + " " + l + " differs from " + aVar + " " + j);
        }
        map.put(aVar, Long.valueOf(j));
    }

    @Override // j$.time.chrono.k
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof AbstractC0162a) && getId().compareTo(((AbstractC0162a) obj).getId()) == 0;
    }

    @Override // j$.time.chrono.k
    public final int hashCode() {
        return getClass().hashCode() ^ getId().hashCode();
    }

    @Override // j$.time.chrono.k
    public final String toString() {
        return getId();
    }
}
