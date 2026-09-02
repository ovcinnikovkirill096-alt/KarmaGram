package j$.time;

import de.robv.android.xposed.callbacks.XCallback;
import j$.time.chrono.ChronoLocalDateTime;
import j$.time.chrono.InterfaceC0163b;
import j$.time.format.DateTimeFormatter;
import j$.time.format.w;
import j$.util.Objects;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.mvel2.asm.signature.SignatureVisitor;
import org.telegram.messenger.MediaDataController;

public final class LocalDate implements j$.time.temporal.m, j$.time.temporal.o, InterfaceC0163b, Serializable {
    public static final LocalDate d = of(-999999999, 1, 1);
    public static final LocalDate e = of(999999999, 12, 31);
    private static final long serialVersionUID = 2942565459149668126L;
    public final int a;
    public final short b;
    public final short c;

    static {
        of(1970, 1, 1);
    }

    public static LocalDate now() {
        return a0(j$.com.android.tools.r8.a.Z());
    }

    public static LocalDate a0(a aVar) {
        Objects.requireNonNull(aVar, "clock");
        long jCurrentTimeMillis = System.currentTimeMillis();
        Instant instant = Instant.c;
        long j = MediaDataController.MAX_STYLE_RUNS_COUNT;
        Instant instantQ = Instant.Q(j$.com.android.tools.r8.a.U(jCurrentTimeMillis, j), ((int) j$.com.android.tools.r8.a.T(jCurrentTimeMillis, j)) * 1000000);
        ZoneId zoneId = aVar.a;
        Objects.requireNonNull(instantQ, "instant");
        Objects.requireNonNull(zoneId, "zone");
        return b0(j$.com.android.tools.r8.a.U(instantQ.a + ((long) zoneId.getRules().getOffset(instantQ).getTotalSeconds()), 86400));
    }

    public static LocalDate of(int i, int i2, int i3) {
        j$.time.temporal.a.YEAR.D(i);
        j$.time.temporal.a.MONTH_OF_YEAR.D(i2);
        j$.time.temporal.a.DAY_OF_MONTH.D(i3);
        return R(i, i2, i3);
    }

    public static LocalDate c0(int i, int i2) {
        long j = i;
        j$.time.temporal.a.YEAR.D(j);
        j$.time.temporal.a.DAY_OF_YEAR.D(i2);
        boolean zO = j$.time.chrono.r.c.O(j);
        if (i2 == 366 && !zO) {
            throw new b("Invalid date 'DayOfYear 366' as '" + i + "' is not a leap year");
        }
        k kVarT = k.T(((i2 - 1) / 31) + 1);
        if (i2 > (kVarT.R(zO) + kVarT.Q(zO)) - 1) {
            kVarT = k.a[((((int) 1) + 12) + kVarT.ordinal()) % 12];
        }
        return new LocalDate(i, kVarT.getValue(), (i2 - kVarT.Q(zO)) + 1);
    }

    public static LocalDate b0(long j) {
        long j2;
        j$.time.temporal.a.EPOCH_DAY.D(j);
        long j3 = 719468 + j;
        if (j3 < 0) {
            long j4 = ((j + 719469) / 146097) - 1;
            j2 = j4 * 400;
            j3 += (-j4) * 146097;
        } else {
            j2 = 0;
        }
        long j5 = ((j3 * 400) + 591) / 146097;
        long j6 = j3 - ((j5 / 400) + (((j5 / 4) + (j5 * 365)) - (j5 / 100)));
        if (j6 < 0) {
            j5--;
            j6 = j3 - ((j5 / 400) + (((j5 / 4) + (365 * j5)) - (j5 / 100)));
        }
        int i = (int) j6;
        int i2 = ((i * 5) + 2) / 153;
        int i3 = ((i2 + 2) % 12) + 1;
        int i4 = (i - (((i2 * 306) + 5) / 10)) + 1;
        long j7 = j5 + j2 + ((long) (i2 / 10));
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR;
        return new LocalDate(aVar.b.a(j7, aVar), i3, i4);
    }

    public static LocalDate S(j$.time.temporal.n nVar) {
        Objects.requireNonNull(nVar, "temporal");
        LocalDate localDate = (LocalDate) nVar.t(j$.time.temporal.s.f);
        if (localDate != null) {
            return localDate;
        }
        throw new b("Unable to obtain LocalDate from TemporalAccessor: " + nVar + " of type " + nVar.getClass().getName());
    }

    public static LocalDate parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        String string;
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        e eVar = new e(0);
        dateTimeFormatter.getClass();
        Objects.requireNonNull(charSequence, "text");
        Objects.requireNonNull(eVar, "query");
        try {
            return (LocalDate) dateTimeFormatter.b(charSequence).t(eVar);
        } catch (w e2) {
            throw e2;
        } catch (RuntimeException e3) {
            if (charSequence.length() > 64) {
                string = charSequence.subSequence(0, 64).toString() + "...";
            } else {
                string = charSequence.toString();
            }
            w wVar = new w("Text '" + string + "' could not be parsed: " + e3.getMessage(), e3);
            charSequence.toString();
            throw wVar;
        }
    }

    public static LocalDate R(int i, int i2, int i3) {
        int i4 = 28;
        if (i3 > 28) {
            if (i2 != 2) {
                i4 = (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) ? 30 : 31;
            } else if (j$.time.chrono.r.c.O(i)) {
                i4 = 29;
            }
            if (i3 > i4) {
                if (i3 == 29) {
                    throw new b("Invalid date 'February 29' as '" + i + "' is not a leap year");
                }
                throw new b("Invalid date '" + k.T(i2).name() + " " + i3 + "'");
            }
        }
        return new LocalDate(i, i2, i3);
    }

    public static LocalDate h0(int i, int i2, int i3) {
        if (i2 == 2) {
            i3 = Math.min(i3, j$.time.chrono.r.c.O((long) i) ? 29 : 28);
        } else if (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) {
            i3 = Math.min(i3, 30);
        }
        return new LocalDate(i, i2, i3);
    }

    public LocalDate(int i, int i2, int i3) {
        this.a = i;
        this.b = (short) i2;
        this.c = (short) i3;
    }

    @Override // j$.time.temporal.n
    public final boolean e(j$.time.temporal.r rVar) {
        return j$.com.android.tools.r8.a.r(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return rVar.j(this);
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
        if (!aVar.isDateBased()) {
            throw new j$.time.temporal.u(c.a("Unsupported field: ", rVar));
        }
        int i = f.a[aVar.ordinal()];
        if (i == 1) {
            return j$.time.temporal.v.f(1L, Y());
        }
        if (i == 2) {
            return j$.time.temporal.v.f(1L, M());
        }
        if (i == 3) {
            return j$.time.temporal.v.f(1L, (k.T(this.b) != k.FEBRUARY || p()) ? 5L : 4L);
        }
        if (i != 4) {
            return aVar.b;
        }
        return getYear() <= 0 ? j$.time.temporal.v.f(1L, 1000000000L) : j$.time.temporal.v.f(1L, 999999999L);
    }

    @Override // j$.time.temporal.n
    public final int i(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return T(rVar);
        }
        return j$.time.temporal.s.a(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            if (rVar == j$.time.temporal.a.EPOCH_DAY) {
                return E();
            }
            if (rVar == j$.time.temporal.a.PROLEPTIC_MONTH) {
                return W();
            }
            return T(rVar);
        }
        return rVar.t(this);
    }

    public final int T(j$.time.temporal.r rVar) {
        switch (f.a[((j$.time.temporal.a) rVar).ordinal()]) {
            case 1:
                return this.c;
            case 2:
                return V();
            case 3:
                return ((this.c - 1) / 7) + 1;
            case 4:
                int i = this.a;
                return i >= 1 ? i : 1 - i;
            case 5:
                return U().getValue();
            case 6:
                return ((this.c - 1) % 7) + 1;
            case 7:
                return ((V() - 1) % 7) + 1;
            case 8:
                throw new j$.time.temporal.u("Invalid field 'EpochDay' for get() method, use getLong() instead");
            case 9:
                return ((V() - 1) / 7) + 1;
            case 10:
                return this.b;
            case 11:
                throw new j$.time.temporal.u("Invalid field 'ProlepticMonth' for get() method, use getLong() instead");
            case 12:
                return this.a;
            case 13:
                return this.a >= 1 ? 1 : 0;
            default:
                throw new j$.time.temporal.u(c.a("Unsupported field: ", rVar));
        }
    }

    public final long W() {
        return ((((long) this.a) * 12) + ((long) this.b)) - 1;
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final j$.time.chrono.k a() {
        return j$.time.chrono.r.c;
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final j$.time.chrono.l G() {
        return getYear() >= 1 ? j$.time.chrono.s.CE : j$.time.chrono.s.BCE;
    }

    public int getYear() {
        return this.a;
    }

    public final int V() {
        return (k.T(this.b).Q(p()) + this.c) - 1;
    }

    public final DayOfWeek U() {
        return DayOfWeek.Q(((int) j$.com.android.tools.r8.a.T(E() + 3, 7)) + 1);
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final boolean p() {
        return j$.time.chrono.r.c.O(this.a);
    }

    public final int Y() {
        short s = this.b;
        if (s != 2) {
            return (s == 4 || s == 6 || s == 9 || s == 11) ? 30 : 31;
        }
        return p() ? 29 : 28;
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final int M() {
        return p() ? 366 : 365;
    }

    @Override // j$.time.chrono.InterfaceC0163b
    /* JADX INFO: renamed from: j0, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public final LocalDate x(j$.time.temporal.o oVar) {
        if (oVar instanceof LocalDate) {
            return (LocalDate) oVar;
        }
        return (LocalDate) oVar.n(this);
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: i0, reason: merged with bridge method [inline-methods] */
    public final LocalDate c(long j, j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return (LocalDate) rVar.y(this, j);
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
        aVar.D(j);
        switch (f.a[aVar.ordinal()]) {
            case 1:
                int i = (int) j;
                if (this.c != i) {
                    return of(this.a, this.b, i);
                }
                return this;
            case 2:
                int i2 = (int) j;
                if (V() != i2) {
                    return c0(this.a, i2);
                }
                return this;
            case 3:
                return f0(j - D(j$.time.temporal.a.ALIGNED_WEEK_OF_MONTH));
            case 4:
                if (this.a < 1) {
                    j = 1 - j;
                }
                return k0((int) j);
            case 5:
                return plusDays(j - ((long) U().getValue()));
            case 6:
                return plusDays(j - D(j$.time.temporal.a.ALIGNED_DAY_OF_WEEK_IN_MONTH));
            case 7:
                return plusDays(j - D(j$.time.temporal.a.ALIGNED_DAY_OF_WEEK_IN_YEAR));
            case 8:
                return b0(j);
            case 9:
                return f0(j - D(j$.time.temporal.a.ALIGNED_WEEK_OF_YEAR));
            case 10:
                int i3 = (int) j;
                if (this.b != i3) {
                    j$.time.temporal.a.MONTH_OF_YEAR.D(i3);
                    return h0(this.a, i3, this.c);
                }
                return this;
            case 11:
                return e0(j - W());
            case 12:
                return k0((int) j);
            case 13:
                if (D(j$.time.temporal.a.ERA) != j) {
                    return k0(1 - this.a);
                }
                return this;
            default:
                throw new j$.time.temporal.u(c.a("Unsupported field: ", rVar));
        }
    }

    public final LocalDate k0(int i) {
        if (this.a == i) {
            return this;
        }
        j$.time.temporal.a.YEAR.D(i);
        return h0(i, this.b, this.c);
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final InterfaceC0163b J(j$.time.temporal.q qVar) {
        if (c.b(qVar)) {
            Period period = (Period) qVar;
            return e0((((long) period.a) * 12) + ((long) period.b)).plusDays(period.c);
        }
        Objects.requireNonNull(qVar, "amountToAdd");
        return (LocalDate) ((Period) qVar).i(this);
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: d0, reason: merged with bridge method [inline-methods] */
    public final LocalDate d(long j, j$.time.temporal.t tVar) {
        if (!(tVar instanceof j$.time.temporal.b)) {
            return (LocalDate) tVar.i(this, j);
        }
        switch (f.b[((j$.time.temporal.b) tVar).ordinal()]) {
            case 1:
                return plusDays(j);
            case 2:
                return f0(j);
            case 3:
                return e0(j);
            case 4:
                return g0(j);
            case 5:
                return g0(j$.com.android.tools.r8.a.V(j, 10));
            case 6:
                return g0(j$.com.android.tools.r8.a.V(j, 100));
            case 7:
                return g0(j$.com.android.tools.r8.a.V(j, MediaDataController.MAX_STYLE_RUNS_COUNT));
            case 8:
                j$.time.temporal.a aVar = j$.time.temporal.a.ERA;
                return c(j$.com.android.tools.r8.a.P(D(aVar), j), aVar);
            default:
                throw new j$.time.temporal.u("Unsupported unit: " + tVar);
        }
    }

    public final LocalDate g0(long j) {
        if (j == 0) {
            return this;
        }
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR;
        return h0(aVar.b.a(((long) this.a) + j, aVar), this.b, this.c);
    }

    public final LocalDate e0(long j) {
        if (j == 0) {
            return this;
        }
        long j2 = (((long) this.a) * 12) + ((long) (this.b - 1)) + j;
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR;
        long j3 = 12;
        return h0(aVar.b.a(j$.com.android.tools.r8.a.U(j2, j3), aVar), ((int) j$.com.android.tools.r8.a.T(j2, j3)) + 1, this.c);
    }

    public final LocalDate f0(long j) {
        return plusDays(j$.com.android.tools.r8.a.V(j, 7));
    }

    public LocalDate plusDays(long j) {
        if (j == 0) {
            return this;
        }
        long j2 = ((long) this.c) + j;
        if (j2 > 0) {
            if (j2 <= 28) {
                return new LocalDate(this.a, this.b, (int) j2);
            }
            if (j2 <= 59) {
                long jY = Y();
                if (j2 <= jY) {
                    return new LocalDate(this.a, this.b, (int) j2);
                }
                short s = this.b;
                if (s < 12) {
                    return new LocalDate(this.a, s + 1, (int) (j2 - jY));
                }
                j$.time.temporal.a.YEAR.D(this.a + 1);
                return new LocalDate(this.a + 1, 1, (int) (j2 - jY));
            }
        }
        return b0(j$.com.android.tools.r8.a.P(E(), j));
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: Z, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public final LocalDate y(long j, j$.time.temporal.t tVar) {
        return j == Long.MIN_VALUE ? d(Long.MAX_VALUE, tVar).d(1L, tVar) : d(-j, tVar);
    }

    @Override // j$.time.temporal.n
    public final Object t(e eVar) {
        return eVar == j$.time.temporal.s.f ? this : j$.com.android.tools.r8.a.t(this, eVar);
    }

    @Override // j$.time.temporal.o
    public final j$.time.temporal.m n(j$.time.temporal.m mVar) {
        return j$.com.android.tools.r8.a.a(this, mVar);
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.a(this);
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final ChronoLocalDateTime F(i iVar) {
        return LocalDateTime.T(this, iVar);
    }

    public LocalDateTime atStartOfDay() {
        return LocalDateTime.T(this, i.g);
    }

    public ZonedDateTime atStartOfDay(ZoneId zoneId) {
        j$.time.zone.b bVarE;
        Objects.requireNonNull(zoneId, "zone");
        LocalDateTime localDateTimeT = LocalDateTime.T(this, i.g);
        if (!(zoneId instanceof ZoneOffset) && (bVarE = zoneId.getRules().e(localDateTimeT)) != null && bVarE.i()) {
            localDateTimeT = bVarE.b.W(bVarE.d.getTotalSeconds() - bVarE.c.getTotalSeconds());
        }
        return ZonedDateTime.Q(localDateTimeT, zoneId, null);
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final long E() {
        long j;
        long j2 = this.a;
        long j3 = this.b;
        long j4 = 365 * j2;
        if (j2 >= 0) {
            j = ((j2 + 399) / 400) + (((3 + j2) / 4) - ((99 + j2) / 100)) + j4;
        } else {
            j = j4 - ((j2 / (-400)) + ((j2 / (-4)) - (j2 / (-100))));
        }
        long j5 = (((367 * j3) - 362) / 12) + j + ((long) (this.c - 1));
        if (j3 > 2) {
            j5 = !p() ? j5 - 2 : j5 - 1;
        }
        return j5 - 719528;
    }

    @Override // java.lang.Comparable
    /* JADX INFO: renamed from: N, reason: merged with bridge method [inline-methods] */
    public final int compareTo(InterfaceC0163b interfaceC0163b) {
        if (interfaceC0163b instanceof LocalDate) {
            return Q((LocalDate) interfaceC0163b);
        }
        return j$.com.android.tools.r8.a.e(this, interfaceC0163b);
    }

    public final int Q(LocalDate localDate) {
        int i = this.a - localDate.a;
        if (i != 0) {
            return i;
        }
        int i2 = this.b - localDate.b;
        return i2 == 0 ? this.c - localDate.c : i2;
    }

    public final boolean X(InterfaceC0163b interfaceC0163b) {
        if (interfaceC0163b instanceof LocalDate) {
            return Q((LocalDate) interfaceC0163b) < 0;
        }
        return E() < interfaceC0163b.E();
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof LocalDate) && Q((LocalDate) obj) == 0;
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final int hashCode() {
        int i = this.a;
        return (((i << 11) + (this.b << 6)) + this.c) ^ (i & (-2048));
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final String toString() {
        int i = this.a;
        short s = this.b;
        short s2 = this.c;
        int iAbs = Math.abs(i);
        StringBuilder sb = new StringBuilder(10);
        if (iAbs >= 1000) {
            if (i > 9999) {
                sb.append(SignatureVisitor.EXTENDS);
            }
            sb.append(i);
        } else if (i < 0) {
            sb.append(i + XCallback.PRIORITY_LOWEST);
            sb.deleteCharAt(1);
        } else {
            sb.append(i + XCallback.PRIORITY_HIGHEST);
            sb.deleteCharAt(0);
        }
        sb.append(s < 10 ? "-0" : "-");
        sb.append((int) s);
        sb.append(s2 < 10 ? "-0" : "-");
        sb.append((int) s2);
        return sb.toString();
    }

    private Object writeReplace() {
        return new p((byte) 3, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
