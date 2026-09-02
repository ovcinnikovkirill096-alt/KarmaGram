package j$.time.temporal;

import j$.time.DayOfWeek;
import j$.time.LocalDate;
import j$.time.format.D;
import j$.time.format.E;
import java.util.Map;

/* JADX WARN: Enum visitor error
java.lang.NullPointerException: Cannot invoke "java.util.List.iterator()" because the return value of "jadx.core.dex.nodes.MethodNode.getBasicBlocks()" is null
	at jadx.core.dex.visitors.EnumVisitor.searchEnumSuperCtrInsn(EnumVisitor.java:495)
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:473)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByRegister(EnumVisitor.java:422)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:351)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:284)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:153)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:102)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
public abstract class h implements r {
    public static final h DAY_OF_QUARTER;
    public static final h QUARTER_OF_YEAR;
    public static final h WEEK_BASED_YEAR;
    public static final h WEEK_OF_WEEK_BASED_YEAR;
    public static final int[] a;
    public static final /* synthetic */ h[] b;

    @Override // j$.time.temporal.r
    public final boolean isDateBased() {
        return true;
    }

    public /* synthetic */ n k(Map map, D d, E e) {
        return null;
    }

    public static h valueOf(String str) {
        return (h) Enum.valueOf(h.class, str);
    }

    public static h[] values() {
        return (h[]) b.clone();
    }

    static {
        h hVar = new h() { // from class: j$.time.temporal.d
            @Override // j$.time.temporal.r
            public final v n() {
                return v.g(1L, 90L, 92L);
            }

            @Override // j$.time.temporal.r
            public final boolean i(n nVar) {
                if (!nVar.e(a.DAY_OF_YEAR) || !nVar.e(a.MONTH_OF_YEAR) || !nVar.e(a.YEAR)) {
                    return false;
                }
                h hVar2 = j.a;
                return j$.com.android.tools.r8.a.N(nVar).equals(j$.time.chrono.r.c);
            }

            @Override // j$.time.temporal.r
            public final v j(n nVar) {
                if (!i(nVar)) {
                    throw new u("Unsupported field: DayOfQuarter");
                }
                long jD = nVar.D(h.QUARTER_OF_YEAR);
                if (jD == 1) {
                    return j$.time.chrono.r.c.O(nVar.D(a.YEAR)) ? v.f(1L, 91L) : v.f(1L, 90L);
                }
                if (jD == 2) {
                    return v.f(1L, 91L);
                }
                if (jD == 3 || jD == 4) {
                    return v.f(1L, 92L);
                }
                return n();
            }

            @Override // j$.time.temporal.r
            public final long t(n nVar) {
                if (!i(nVar)) {
                    throw new u("Unsupported field: DayOfQuarter");
                }
                return nVar.i(a.DAY_OF_YEAR) - h.a[((nVar.i(a.MONTH_OF_YEAR) - 1) / 3) + (j$.time.chrono.r.c.O(nVar.D(a.YEAR)) ? 4 : 0)];
            }

            @Override // j$.time.temporal.r
            public final m y(m mVar, long j) {
                long jT = t(mVar);
                n().b(j, this);
                a aVar = a.DAY_OF_YEAR;
                return mVar.c((j - jT) + mVar.D(aVar), aVar);
            }

            @Override // j$.time.temporal.h, j$.time.temporal.r
            public final n k(Map map, D d, E e) {
                LocalDate localDateE0;
                long jW;
                a aVar = a.YEAR;
                Long l = (Long) map.get(aVar);
                r rVar = h.QUARTER_OF_YEAR;
                Long l2 = (Long) map.get(rVar);
                if (l == null || l2 == null) {
                    return null;
                }
                int iA = aVar.b.a(l.longValue(), aVar);
                long jLongValue = ((Long) map.get(h.DAY_OF_QUARTER)).longValue();
                h hVar2 = j.a;
                if (!j$.com.android.tools.r8.a.N(d).equals(j$.time.chrono.r.c)) {
                    throw new j$.time.b("Resolve requires IsoChronology");
                }
                if (e == E.LENIENT) {
                    localDateE0 = LocalDate.of(iA, 1, 1).e0(j$.com.android.tools.r8.a.V(j$.com.android.tools.r8.a.W(l2.longValue(), 1L), 3));
                    jW = j$.com.android.tools.r8.a.W(jLongValue, 1L);
                } else {
                    LocalDate localDateOf = LocalDate.of(iA, ((rVar.n().a(l2.longValue(), rVar) - 1) * 3) + 1, 1);
                    if (jLongValue < 1 || jLongValue > 90) {
                        if (e == E.STRICT) {
                            j(localDateOf).b(jLongValue, this);
                        } else {
                            n().b(jLongValue, this);
                        }
                    }
                    localDateE0 = localDateOf;
                    jW = jLongValue - 1;
                }
                map.remove(this);
                map.remove(aVar);
                map.remove(rVar);
                return localDateE0.plusDays(jW);
            }

            @Override // java.lang.Enum
            public final String toString() {
                return "DayOfQuarter";
            }
        };
        DAY_OF_QUARTER = hVar;
        h hVar2 = new h() { // from class: j$.time.temporal.e
            @Override // j$.time.temporal.r
            public final v n() {
                return v.f(1L, 4L);
            }

            @Override // j$.time.temporal.r
            public final boolean i(n nVar) {
                if (!nVar.e(a.MONTH_OF_YEAR)) {
                    return false;
                }
                h hVar3 = j.a;
                return j$.com.android.tools.r8.a.N(nVar).equals(j$.time.chrono.r.c);
            }

            @Override // j$.time.temporal.r
            public final long t(n nVar) {
                if (!i(nVar)) {
                    throw new u("Unsupported field: QuarterOfYear");
                }
                return (nVar.D(a.MONTH_OF_YEAR) + 2) / 3;
            }

            @Override // j$.time.temporal.r
            public final v j(n nVar) {
                if (!i(nVar)) {
                    throw new u("Unsupported field: QuarterOfYear");
                }
                return n();
            }

            @Override // j$.time.temporal.r
            public final m y(m mVar, long j) {
                long jT = t(mVar);
                n().b(j, this);
                a aVar = a.MONTH_OF_YEAR;
                return mVar.c(((j - jT) * 3) + mVar.D(aVar), aVar);
            }

            @Override // java.lang.Enum
            public final String toString() {
                return "QuarterOfYear";
            }
        };
        QUARTER_OF_YEAR = hVar2;
        h hVar3 = new h() { // from class: j$.time.temporal.f
            @Override // j$.time.temporal.r
            public final v n() {
                return v.g(1L, 52L, 53L);
            }

            @Override // j$.time.temporal.r
            public final boolean i(n nVar) {
                if (!nVar.e(a.EPOCH_DAY)) {
                    return false;
                }
                h hVar4 = j.a;
                return j$.com.android.tools.r8.a.N(nVar).equals(j$.time.chrono.r.c);
            }

            @Override // j$.time.temporal.r
            public final v j(n nVar) {
                if (i(nVar)) {
                    return h.S(LocalDate.S(nVar));
                }
                throw new u("Unsupported field: WeekOfWeekBasedYear");
            }

            @Override // j$.time.temporal.r
            public final long t(n nVar) {
                if (!i(nVar)) {
                    throw new u("Unsupported field: WeekOfWeekBasedYear");
                }
                return h.D(LocalDate.S(nVar));
            }

            @Override // j$.time.temporal.r
            public final m y(m mVar, long j) {
                n().b(j, this);
                return mVar.d(j$.com.android.tools.r8.a.W(j, t(mVar)), b.WEEKS);
            }

            @Override // j$.time.temporal.h, j$.time.temporal.r
            public final n k(Map map, D d, E e) {
                LocalDate localDateC;
                long j;
                long j2;
                r rVar = h.WEEK_BASED_YEAR;
                Long l = (Long) map.get(rVar);
                a aVar = a.DAY_OF_WEEK;
                Long l2 = (Long) map.get(aVar);
                if (l == null || l2 == null) {
                    return null;
                }
                int iA = rVar.n().a(l.longValue(), rVar);
                long jLongValue = ((Long) map.get(h.WEEK_OF_WEEK_BASED_YEAR)).longValue();
                h hVar4 = j.a;
                if (!j$.com.android.tools.r8.a.N(d).equals(j$.time.chrono.r.c)) {
                    throw new j$.time.b("Resolve requires IsoChronology");
                }
                LocalDate localDateOf = LocalDate.of(iA, 1, 4);
                if (e == E.LENIENT) {
                    long jLongValue2 = l2.longValue();
                    if (jLongValue2 > 7) {
                        long j3 = jLongValue2 - 1;
                        j = 1;
                        localDateOf = localDateOf.f0(j3 / 7);
                        j2 = j3 % 7;
                    } else {
                        j = 1;
                        if (jLongValue2 < 1) {
                            localDateOf = localDateOf.f0(j$.com.android.tools.r8.a.W(jLongValue2, 7L) / 7);
                            j2 = (jLongValue2 + 6) % 7;
                        }
                        localDateC = localDateOf.f0(j$.com.android.tools.r8.a.W(jLongValue, j)).c(jLongValue2, aVar);
                    }
                    jLongValue2 = j2 + j;
                    localDateC = localDateOf.f0(j$.com.android.tools.r8.a.W(jLongValue, j)).c(jLongValue2, aVar);
                } else {
                    int iA2 = aVar.b.a(l2.longValue(), aVar);
                    if (jLongValue < 1 || jLongValue > 52) {
                        if (e == E.STRICT) {
                            h.S(localDateOf).b(jLongValue, this);
                        } else {
                            n().b(jLongValue, this);
                        }
                    }
                    localDateC = localDateOf.f0(jLongValue - 1).c(iA2, aVar);
                }
                map.remove(this);
                map.remove(rVar);
                map.remove(aVar);
                return localDateC;
            }

            @Override // java.lang.Enum
            public final String toString() {
                return "WeekOfWeekBasedYear";
            }
        };
        WEEK_OF_WEEK_BASED_YEAR = hVar3;
        h hVar4 = new h() { // from class: j$.time.temporal.g
            @Override // j$.time.temporal.r
            public final v n() {
                return a.YEAR.b;
            }

            @Override // j$.time.temporal.r
            public final boolean i(n nVar) {
                if (!nVar.e(a.EPOCH_DAY)) {
                    return false;
                }
                h hVar5 = j.a;
                return j$.com.android.tools.r8.a.N(nVar).equals(j$.time.chrono.r.c);
            }

            @Override // j$.time.temporal.r
            public final long t(n nVar) {
                if (i(nVar)) {
                    return h.Q(LocalDate.S(nVar));
                }
                throw new u("Unsupported field: WeekBasedYear");
            }

            @Override // j$.time.temporal.r
            public final v j(n nVar) {
                if (!i(nVar)) {
                    throw new u("Unsupported field: WeekBasedYear");
                }
                return n();
            }

            @Override // j$.time.temporal.r
            public final m y(m mVar, long j) {
                if (!i(mVar)) {
                    throw new u("Unsupported field: WeekBasedYear");
                }
                int iA = a.YEAR.b.a(j, h.WEEK_BASED_YEAR);
                LocalDate localDateS = LocalDate.S(mVar);
                a aVar = a.DAY_OF_WEEK;
                int i = localDateS.i(aVar);
                int iD = h.D(localDateS);
                if (iD == 53 && h.R(iA) == 52) {
                    iD = 52;
                }
                LocalDate localDateOf = LocalDate.of(iA, 1, 4);
                return mVar.x(localDateOf.plusDays(((iD - 1) * 7) + (i - localDateOf.i(aVar))));
            }

            @Override // java.lang.Enum
            public final String toString() {
                return "WeekBasedYear";
            }
        };
        WEEK_BASED_YEAR = hVar4;
        b = new h[]{hVar, hVar2, hVar3, hVar4};
        a = new int[]{0, 90, 181, 273, 0, 91, 182, 274};
    }

    public static v S(LocalDate localDate) {
        return v.f(1L, R(Q(localDate)));
    }

    public static int R(int i) {
        LocalDate localDateOf = LocalDate.of(i, 1, 1);
        if (localDateOf.U() != DayOfWeek.THURSDAY) {
            return (localDateOf.U() == DayOfWeek.WEDNESDAY && localDateOf.p()) ? 53 : 52;
        }
        return 53;
    }

    public static int D(LocalDate localDate) {
        int iOrdinal = localDate.U().ordinal();
        int iV = localDate.V() - 1;
        int i = (3 - iOrdinal) + iV;
        int i2 = i - ((i / 7) * 7);
        int i3 = i2 - 3;
        if (i3 < -3) {
            i3 = i2 + 4;
        }
        if (iV >= i3) {
            int i4 = ((iV - i3) / 7) + 1;
            if (i4 != 53 || i3 == -3 || (i3 == -2 && localDate.p())) {
                return i4;
            }
            return 1;
        }
        if (localDate.V() != 180) {
            localDate = LocalDate.c0(localDate.a, 180);
        }
        return (int) S(localDate.g0(-1L)).d;
    }

    public static int Q(LocalDate localDate) {
        int year = localDate.getYear();
        int iV = localDate.V();
        if (iV <= 3) {
            return iV - localDate.U().ordinal() < -2 ? year - 1 : year;
        }
        if (iV >= 363) {
            return ((iV - 363) - (localDate.p() ? 1 : 0)) - localDate.U().ordinal() >= 0 ? year + 1 : year;
        }
        return year;
    }
}
