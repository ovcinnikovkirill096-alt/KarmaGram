package j$.time.format;

import j$.time.Instant;
import j$.time.LocalDate;
import j$.time.Period;
import j$.time.ZoneId;
import j$.time.ZoneOffset;
import j$.time.chrono.ChronoZonedDateTime;
import j$.time.chrono.InterfaceC0163b;
import j$.util.Objects;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class D implements j$.time.temporal.n {
    public ZoneId b;
    public j$.time.chrono.k c;
    public boolean d;
    public E e;
    public InterfaceC0163b f;
    public j$.time.i g;
    public final Map a = new HashMap();
    public Period h = Period.d;

    @Override // j$.time.temporal.n
    public final /* synthetic */ int i(j$.time.temporal.r rVar) {
        return j$.time.temporal.s.a(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final /* synthetic */ j$.time.temporal.v k(j$.time.temporal.r rVar) {
        return j$.time.temporal.s.d(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final boolean e(j$.time.temporal.r rVar) {
        if (((HashMap) this.a).containsKey(rVar)) {
            return true;
        }
        InterfaceC0163b interfaceC0163b = this.f;
        if (interfaceC0163b != null && interfaceC0163b.e(rVar)) {
            return true;
        }
        j$.time.i iVar = this.g;
        if (iVar == null || !iVar.e(rVar)) {
            return (rVar == null || (rVar instanceof j$.time.temporal.a) || !rVar.i(this)) ? false : true;
        }
        return true;
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        Objects.requireNonNull(rVar, "field");
        Long l = (Long) ((HashMap) this.a).get(rVar);
        if (l != null) {
            return l.longValue();
        }
        InterfaceC0163b interfaceC0163b = this.f;
        if (interfaceC0163b != null && interfaceC0163b.e(rVar)) {
            return this.f.D(rVar);
        }
        j$.time.i iVar = this.g;
        if (iVar != null && iVar.e(rVar)) {
            return this.g.D(rVar);
        }
        if (rVar instanceof j$.time.temporal.a) {
            throw new j$.time.temporal.u(j$.time.c.a("Unsupported field: ", rVar));
        }
        return rVar.t(this);
    }

    @Override // j$.time.temporal.n
    public final Object t(j$.time.e eVar) {
        if (eVar == j$.time.temporal.s.a) {
            return this.b;
        }
        if (eVar == j$.time.temporal.s.b) {
            return this.c;
        }
        if (eVar == j$.time.temporal.s.f) {
            InterfaceC0163b interfaceC0163b = this.f;
            if (interfaceC0163b != null) {
                return LocalDate.S(interfaceC0163b);
            }
            return null;
        }
        if (eVar == j$.time.temporal.s.g) {
            return this.g;
        }
        if (eVar == j$.time.temporal.s.d) {
            Long l = (Long) ((HashMap) this.a).get(j$.time.temporal.a.OFFSET_SECONDS);
            if (l != null) {
                return ZoneOffset.W(l.intValue());
            }
            ZoneId zoneId = this.b;
            return zoneId instanceof ZoneOffset ? zoneId : eVar.g(this);
        }
        if (eVar == j$.time.temporal.s.e) {
            return eVar.g(this);
        }
        if (eVar == j$.time.temporal.s.c) {
            return null;
        }
        return eVar.g(this);
    }

    public final void v(j$.time.temporal.r rVar, j$.time.temporal.a aVar, Long l) {
        Long l2 = (Long) ((HashMap) this.a).put(aVar, l);
        if (l2 == null || l2.longValue() == l.longValue()) {
            return;
        }
        throw new j$.time.b("Conflict found: " + aVar + " " + l2 + " differs from " + aVar + " " + l + " while resolving  " + rVar);
    }

    public final void l() {
        if (((HashMap) this.a).containsKey(j$.time.temporal.a.INSTANT_SECONDS)) {
            ZoneId zoneId = this.b;
            if (zoneId != null) {
                m(zoneId);
                return;
            }
            Long l = (Long) ((HashMap) this.a).get(j$.time.temporal.a.OFFSET_SECONDS);
            if (l != null) {
                m(ZoneOffset.W(l.intValue()));
            }
        }
    }

    public final void m(ZoneId zoneId) {
        Map map = this.a;
        j$.time.temporal.a aVar = j$.time.temporal.a.INSTANT_SECONDS;
        ChronoZonedDateTime chronoZonedDateTimeL = this.c.L(Instant.Q(((Long) ((HashMap) map).remove(aVar)).longValue(), 0), zoneId);
        u(chronoZonedDateTimeL.f());
        v(aVar, j$.time.temporal.a.SECOND_OF_DAY, Long.valueOf(chronoZonedDateTimeL.b().d0()));
    }

    public final void u(InterfaceC0163b interfaceC0163b) {
        InterfaceC0163b interfaceC0163b2 = this.f;
        if (interfaceC0163b2 != null) {
            if (interfaceC0163b == null || interfaceC0163b2.equals(interfaceC0163b)) {
                return;
            }
            throw new j$.time.b("Conflict found: Fields resolved to two different dates: " + this.f + " " + interfaceC0163b);
        }
        if (interfaceC0163b != null) {
            if (!this.c.equals(interfaceC0163b.a())) {
                throw new j$.time.b("ChronoLocalDate must use the effective parsed chronology: " + this.c);
            }
            this.f = interfaceC0163b;
        }
    }

    public final void q() {
        Map map = this.a;
        j$.time.temporal.a aVar = j$.time.temporal.a.CLOCK_HOUR_OF_DAY;
        if (((HashMap) map).containsKey(aVar)) {
            long jLongValue = ((Long) ((HashMap) this.a).remove(aVar)).longValue();
            E e = this.e;
            if (e == E.STRICT || (e == E.SMART && jLongValue != 0)) {
                aVar.D(jLongValue);
            }
            j$.time.temporal.a aVar2 = j$.time.temporal.a.HOUR_OF_DAY;
            if (jLongValue == 24) {
                jLongValue = 0;
            }
            v(aVar, aVar2, Long.valueOf(jLongValue));
        }
        Map map2 = this.a;
        j$.time.temporal.a aVar3 = j$.time.temporal.a.CLOCK_HOUR_OF_AMPM;
        if (((HashMap) map2).containsKey(aVar3)) {
            long jLongValue2 = ((Long) ((HashMap) this.a).remove(aVar3)).longValue();
            E e2 = this.e;
            if (e2 == E.STRICT || (e2 == E.SMART && jLongValue2 != 0)) {
                aVar3.D(jLongValue2);
            }
            v(aVar3, j$.time.temporal.a.HOUR_OF_AMPM, Long.valueOf(jLongValue2 != 12 ? jLongValue2 : 0L));
        }
        Map map3 = this.a;
        j$.time.temporal.a aVar4 = j$.time.temporal.a.AMPM_OF_DAY;
        if (((HashMap) map3).containsKey(aVar4)) {
            Map map4 = this.a;
            j$.time.temporal.a aVar5 = j$.time.temporal.a.HOUR_OF_AMPM;
            if (((HashMap) map4).containsKey(aVar5)) {
                long jLongValue3 = ((Long) ((HashMap) this.a).remove(aVar4)).longValue();
                long jLongValue4 = ((Long) ((HashMap) this.a).remove(aVar5)).longValue();
                if (this.e == E.LENIENT) {
                    v(aVar4, j$.time.temporal.a.HOUR_OF_DAY, Long.valueOf(j$.com.android.tools.r8.a.P(j$.com.android.tools.r8.a.V(jLongValue3, 12), jLongValue4)));
                } else {
                    aVar4.D(jLongValue3);
                    aVar5.D(jLongValue3);
                    v(aVar4, j$.time.temporal.a.HOUR_OF_DAY, Long.valueOf((jLongValue3 * 12) + jLongValue4));
                }
            }
        }
        Map map5 = this.a;
        j$.time.temporal.a aVar6 = j$.time.temporal.a.NANO_OF_DAY;
        if (((HashMap) map5).containsKey(aVar6)) {
            long jLongValue5 = ((Long) ((HashMap) this.a).remove(aVar6)).longValue();
            if (this.e != E.LENIENT) {
                aVar6.D(jLongValue5);
            }
            v(aVar6, j$.time.temporal.a.HOUR_OF_DAY, Long.valueOf(jLongValue5 / 3600000000000L));
            v(aVar6, j$.time.temporal.a.MINUTE_OF_HOUR, Long.valueOf((jLongValue5 / 60000000000L) % 60));
            v(aVar6, j$.time.temporal.a.SECOND_OF_MINUTE, Long.valueOf((jLongValue5 / 1000000000) % 60));
            v(aVar6, j$.time.temporal.a.NANO_OF_SECOND, Long.valueOf(jLongValue5 % 1000000000));
        }
        Map map6 = this.a;
        j$.time.temporal.a aVar7 = j$.time.temporal.a.MICRO_OF_DAY;
        if (((HashMap) map6).containsKey(aVar7)) {
            long jLongValue6 = ((Long) ((HashMap) this.a).remove(aVar7)).longValue();
            if (this.e != E.LENIENT) {
                aVar7.D(jLongValue6);
            }
            v(aVar7, j$.time.temporal.a.SECOND_OF_DAY, Long.valueOf(jLongValue6 / 1000000));
            v(aVar7, j$.time.temporal.a.MICRO_OF_SECOND, Long.valueOf(jLongValue6 % 1000000));
        }
        Map map7 = this.a;
        j$.time.temporal.a aVar8 = j$.time.temporal.a.MILLI_OF_DAY;
        if (((HashMap) map7).containsKey(aVar8)) {
            long jLongValue7 = ((Long) ((HashMap) this.a).remove(aVar8)).longValue();
            if (this.e != E.LENIENT) {
                aVar8.D(jLongValue7);
            }
            v(aVar8, j$.time.temporal.a.SECOND_OF_DAY, Long.valueOf(jLongValue7 / 1000));
            v(aVar8, j$.time.temporal.a.MILLI_OF_SECOND, Long.valueOf(jLongValue7 % 1000));
        }
        Map map8 = this.a;
        j$.time.temporal.a aVar9 = j$.time.temporal.a.SECOND_OF_DAY;
        if (((HashMap) map8).containsKey(aVar9)) {
            long jLongValue8 = ((Long) ((HashMap) this.a).remove(aVar9)).longValue();
            if (this.e != E.LENIENT) {
                aVar9.D(jLongValue8);
            }
            v(aVar9, j$.time.temporal.a.HOUR_OF_DAY, Long.valueOf(jLongValue8 / 3600));
            v(aVar9, j$.time.temporal.a.MINUTE_OF_HOUR, Long.valueOf((jLongValue8 / 60) % 60));
            v(aVar9, j$.time.temporal.a.SECOND_OF_MINUTE, Long.valueOf(jLongValue8 % 60));
        }
        Map map9 = this.a;
        j$.time.temporal.a aVar10 = j$.time.temporal.a.MINUTE_OF_DAY;
        if (((HashMap) map9).containsKey(aVar10)) {
            long jLongValue9 = ((Long) ((HashMap) this.a).remove(aVar10)).longValue();
            if (this.e != E.LENIENT) {
                aVar10.D(jLongValue9);
            }
            v(aVar10, j$.time.temporal.a.HOUR_OF_DAY, Long.valueOf(jLongValue9 / 60));
            v(aVar10, j$.time.temporal.a.MINUTE_OF_HOUR, Long.valueOf(jLongValue9 % 60));
        }
        Map map10 = this.a;
        j$.time.temporal.a aVar11 = j$.time.temporal.a.NANO_OF_SECOND;
        if (((HashMap) map10).containsKey(aVar11)) {
            long jLongValue10 = ((Long) ((HashMap) this.a).get(aVar11)).longValue();
            E e3 = this.e;
            E e4 = E.LENIENT;
            if (e3 != e4) {
                aVar11.D(jLongValue10);
            }
            Map map11 = this.a;
            j$.time.temporal.a aVar12 = j$.time.temporal.a.MICRO_OF_SECOND;
            if (((HashMap) map11).containsKey(aVar12)) {
                long jLongValue11 = ((Long) ((HashMap) this.a).remove(aVar12)).longValue();
                if (this.e != e4) {
                    aVar12.D(jLongValue11);
                }
                jLongValue10 = (jLongValue10 % 1000) + (jLongValue11 * 1000);
                v(aVar12, aVar11, Long.valueOf(jLongValue10));
            }
            Map map12 = this.a;
            j$.time.temporal.a aVar13 = j$.time.temporal.a.MILLI_OF_SECOND;
            if (((HashMap) map12).containsKey(aVar13)) {
                long jLongValue12 = ((Long) ((HashMap) this.a).remove(aVar13)).longValue();
                if (this.e != e4) {
                    aVar13.D(jLongValue12);
                }
                v(aVar13, aVar11, Long.valueOf((jLongValue10 % 1000000) + (jLongValue12 * 1000000)));
            }
        }
        Map map13 = this.a;
        j$.time.temporal.a aVar14 = j$.time.temporal.a.HOUR_OF_DAY;
        if (((HashMap) map13).containsKey(aVar14)) {
            Map map14 = this.a;
            j$.time.temporal.a aVar15 = j$.time.temporal.a.MINUTE_OF_HOUR;
            if (((HashMap) map14).containsKey(aVar15)) {
                Map map15 = this.a;
                j$.time.temporal.a aVar16 = j$.time.temporal.a.SECOND_OF_MINUTE;
                if (((HashMap) map15).containsKey(aVar16) && ((HashMap) this.a).containsKey(aVar11)) {
                    n(((Long) ((HashMap) this.a).remove(aVar14)).longValue(), ((Long) ((HashMap) this.a).remove(aVar15)).longValue(), ((Long) ((HashMap) this.a).remove(aVar16)).longValue(), ((Long) ((HashMap) this.a).remove(aVar11)).longValue());
                }
            }
        }
    }

    public final void n(long j, long j2, long j3, long j4) {
        if (this.e == E.LENIENT) {
            long jP = j$.com.android.tools.r8.a.P(j$.com.android.tools.r8.a.P(j$.com.android.tools.r8.a.P(j$.com.android.tools.r8.a.V(j, 3600000000000L), j$.com.android.tools.r8.a.V(j2, 60000000000L)), j$.com.android.tools.r8.a.V(j3, 1000000000L)), j4);
            s(j$.time.i.V(j$.com.android.tools.r8.a.T(jP, 86400000000000L)), Period.a(0, 0, (int) j$.com.android.tools.r8.a.U(jP, 86400000000000L)));
            return;
        }
        j$.time.temporal.a aVar = j$.time.temporal.a.MINUTE_OF_HOUR;
        int iA = aVar.b.a(j2, aVar);
        j$.time.temporal.a aVar2 = j$.time.temporal.a.NANO_OF_SECOND;
        int iA2 = aVar2.b.a(j4, aVar2);
        if (this.e == E.SMART && j == 24 && iA == 0 && j3 == 0 && iA2 == 0) {
            s(j$.time.i.g, Period.a(0, 0, 1));
            return;
        }
        j$.time.temporal.a aVar3 = j$.time.temporal.a.HOUR_OF_DAY;
        int iA3 = aVar3.b.a(j, aVar3);
        j$.time.temporal.a aVar4 = j$.time.temporal.a.SECOND_OF_MINUTE;
        s(j$.time.i.U(iA3, iA, aVar4.b.a(j3, aVar4), iA2), Period.d);
    }

    public final void s(j$.time.i iVar, Period period) {
        j$.time.i iVar2 = this.g;
        if (iVar2 != null) {
            if (!iVar2.equals(iVar)) {
                throw new j$.time.b("Conflict found: Fields resolved to different times: " + this.g + " " + iVar);
            }
            Period period2 = this.h;
            period2.getClass();
            Period period3 = Period.d;
            if (period2 != period3 && period != period3 && !this.h.equals(period)) {
                throw new j$.time.b("Conflict found: Fields resolved to different excess periods: " + this.h + " " + period);
            }
            this.h = period;
            return;
        }
        this.g = iVar;
        this.h = period;
    }

    public final void h(j$.time.temporal.n nVar) {
        Iterator it = ((HashMap) this.a).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            j$.time.temporal.r rVar = (j$.time.temporal.r) entry.getKey();
            if (nVar.e(rVar)) {
                try {
                    long jD = nVar.D(rVar);
                    long jLongValue = ((Long) entry.getValue()).longValue();
                    if (jD != jLongValue) {
                        throw new j$.time.b("Conflict found: Field " + rVar + " " + jD + " differs from " + rVar + " " + jLongValue + " derived from " + nVar);
                    }
                    it.remove();
                } catch (RuntimeException unused) {
                    continue;
                }
            }
        }
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append(this.a);
        sb.append(',');
        sb.append(this.c);
        if (this.b != null) {
            sb.append(',');
            sb.append(this.b);
        }
        if (this.f != null || this.g != null) {
            sb.append(" resolved to ");
            InterfaceC0163b interfaceC0163b = this.f;
            if (interfaceC0163b != null) {
                sb.append(interfaceC0163b);
                if (this.g != null) {
                    sb.append('T');
                    sb.append(this.g);
                }
            } else {
                sb.append(this.g);
            }
        }
        return sb.toString();
    }
}
