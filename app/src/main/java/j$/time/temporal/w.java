package j$.time.temporal;

import j$.time.chrono.InterfaceC0163b;
import j$.time.format.D;
import j$.time.format.E;
import java.util.Map;

public final class w implements r {
    public static final v f = v.f(1, 7);
    public static final v g = v.g(0, 4, 6);
    public static final v h = v.g(0, 52, 54);
    public static final v i = v.g(1, 52, 53);
    public final String a;
    public final x b;
    public final t c;
    public final t d;
    public final v e;

    @Override // j$.time.temporal.r
    public final boolean isDateBased() {
        return true;
    }

    public final InterfaceC0163b e(j$.time.chrono.k kVar, int i2, int i3, int i4) {
        InterfaceC0163b interfaceC0163bI = kVar.I(i2, 1, 1);
        int iH = h(1, b(interfaceC0163bI));
        return interfaceC0163bI.d(((Math.min(i3, a(iH, interfaceC0163bI.M() + this.b.b) - 1) - 1) * 7) + (i4 - 1) + (-iH), (t) b.DAYS);
    }

    public w(String str, x xVar, t tVar, t tVar2, v vVar) {
        this.a = str;
        this.b = xVar;
        this.c = tVar;
        this.d = tVar2;
        this.e = vVar;
    }

    @Override // j$.time.temporal.r
    public final long t(n nVar) {
        int iC;
        b bVar = b.WEEKS;
        t tVar = this.d;
        if (tVar == bVar) {
            iC = b(nVar);
        } else if (tVar != b.MONTHS) {
            if (tVar != b.YEARS) {
                if (tVar == x.h) {
                    iC = d(nVar);
                } else if (tVar == b.FOREVER) {
                    iC = c(nVar);
                } else {
                    throw new IllegalStateException("unreachable, rangeUnit: " + tVar + ", this: " + this);
                }
            } else {
                int iB = b(nVar);
                int i2 = nVar.i(a.DAY_OF_YEAR);
                iC = a(h(i2, iB), i2);
            }
        } else {
            int iB2 = b(nVar);
            int i3 = nVar.i(a.DAY_OF_MONTH);
            iC = a(h(i3, iB2), i3);
        }
        return iC;
    }

    public final int b(n nVar) {
        return s.e(nVar.i(a.DAY_OF_WEEK) - this.b.a.getValue()) + 1;
    }

    public final int c(n nVar) {
        int iB = b(nVar);
        int i2 = nVar.i(a.YEAR);
        a aVar = a.DAY_OF_YEAR;
        int i3 = nVar.i(aVar);
        int iH = h(i3, iB);
        int iA = a(iH, i3);
        if (iA == 0) {
            return i2 - 1;
        }
        return iA >= a(iH, ((int) nVar.k(aVar).d) + this.b.b) ? i2 + 1 : i2;
    }

    public final int d(n nVar) {
        int iA;
        int iB = b(nVar);
        a aVar = a.DAY_OF_YEAR;
        int i2 = nVar.i(aVar);
        int iH = h(i2, iB);
        int iA2 = a(iH, i2);
        if (iA2 == 0) {
            return d(j$.com.android.tools.r8.a.N(nVar).A(nVar).y(i2, b.DAYS));
        }
        return (iA2 <= 50 || iA2 < (iA = a(iH, ((int) nVar.k(aVar).d) + this.b.b))) ? iA2 : (iA2 - iA) + 1;
    }

    public final int h(int i2, int i3) {
        int iE = s.e(i2 - i3);
        return iE + 1 > this.b.b ? 7 - iE : -iE;
    }

    public static int a(int i2, int i3) {
        return ((i3 - 1) + (i2 + 7)) / 7;
    }

    @Override // j$.time.temporal.r
    public final m y(m mVar, long j) {
        int iA = this.e.a(j, this);
        int i2 = mVar.i(this);
        if (iA == i2) {
            return mVar;
        }
        if (this.d != b.FOREVER) {
            return mVar.d(iA - i2, this.c);
        }
        x xVar = this.b;
        return e(j$.com.android.tools.r8.a.N(mVar), (int) j, mVar.i(xVar.e), mVar.i(xVar.c));
    }

    @Override // j$.time.temporal.r
    public final n k(Map map, D d, E e) {
        InterfaceC0163b interfaceC0163bD;
        InterfaceC0163b interfaceC0163bD2;
        a aVar;
        InterfaceC0163b interfaceC0163bD3;
        long jLongValue = ((Long) map.get(this)).longValue();
        int iO = j$.com.android.tools.r8.a.O(jLongValue);
        b bVar = b.WEEKS;
        v vVar = this.e;
        x xVar = this.b;
        t tVar = this.d;
        if (tVar == bVar) {
            long jE = s.e((vVar.a(jLongValue, this) - 1) + (xVar.a.getValue() - 1)) + 1;
            map.remove(this);
            map.put(a.DAY_OF_WEEK, Long.valueOf(jE));
            return null;
        }
        a aVar2 = a.DAY_OF_WEEK;
        if (!map.containsKey(aVar2)) {
            return null;
        }
        int iE = s.e(aVar2.b.a(((Long) map.get(aVar2)).longValue(), aVar2) - xVar.a.getValue()) + 1;
        j$.time.chrono.k kVarN = j$.com.android.tools.r8.a.N(d);
        a aVar3 = a.YEAR;
        if (!map.containsKey(aVar3)) {
            if ((tVar != x.h && tVar != b.FOREVER) || !map.containsKey(xVar.f) || !map.containsKey(xVar.e)) {
                return null;
            }
            w wVar = xVar.f;
            int iA = wVar.e.a(((Long) map.get(wVar)).longValue(), xVar.f);
            if (e == E.LENIENT) {
                interfaceC0163bD = e(kVarN, iA, 1, iE).d(j$.com.android.tools.r8.a.W(((Long) map.get(xVar.e)).longValue(), 1L), (t) bVar);
            } else {
                w wVar2 = xVar.e;
                InterfaceC0163b interfaceC0163bE = e(kVarN, iA, wVar2.e.a(((Long) map.get(wVar2)).longValue(), xVar.e), iE);
                if (e == E.STRICT && c(interfaceC0163bE) != iA) {
                    throw new j$.time.b("Strict mode rejected resolved date as it is in a different week-based-year");
                }
                interfaceC0163bD = interfaceC0163bE;
            }
            map.remove(this);
            map.remove(xVar.f);
            map.remove(xVar.e);
            map.remove(aVar2);
            return interfaceC0163bD;
        }
        int iA2 = aVar3.b.a(((Long) map.get(aVar3)).longValue(), aVar3);
        b bVar2 = b.MONTHS;
        if (tVar == bVar2) {
            a aVar4 = a.MONTH_OF_YEAR;
            if (map.containsKey(aVar4)) {
                long jLongValue2 = ((Long) map.get(aVar4)).longValue();
                long j = iO;
                if (e == E.LENIENT) {
                    InterfaceC0163b interfaceC0163bD4 = kVarN.I(iA2, 1, 1).d(j$.com.android.tools.r8.a.W(jLongValue2, 1L), (t) bVar2);
                    int iB = b(interfaceC0163bD4);
                    int i2 = interfaceC0163bD4.i(a.DAY_OF_MONTH);
                    interfaceC0163bD3 = interfaceC0163bD4.d(j$.com.android.tools.r8.a.P(j$.com.android.tools.r8.a.V(j$.com.android.tools.r8.a.W(j, a(h(i2, iB), i2)), 7), iE - b(interfaceC0163bD4)), (t) b.DAYS);
                    aVar = aVar4;
                } else {
                    aVar = aVar4;
                    InterfaceC0163b interfaceC0163bI = kVarN.I(iA2, aVar.b.a(jLongValue2, aVar), 1);
                    long jA = vVar.a(j, this);
                    int iB2 = b(interfaceC0163bI);
                    int i3 = interfaceC0163bI.i(a.DAY_OF_MONTH);
                    InterfaceC0163b interfaceC0163bD5 = interfaceC0163bI.d((((int) (jA - ((long) a(h(i3, iB2), i3)))) * 7) + (iE - b(interfaceC0163bI)), (t) b.DAYS);
                    if (e == E.STRICT && interfaceC0163bD5.D(aVar) != jLongValue2) {
                        throw new j$.time.b("Strict mode rejected resolved date as it is in a different month");
                    }
                    interfaceC0163bD3 = interfaceC0163bD5;
                }
                map.remove(this);
                map.remove(aVar3);
                map.remove(aVar);
                map.remove(aVar2);
                return interfaceC0163bD3;
            }
        }
        if (tVar != b.YEARS) {
            return null;
        }
        long j2 = iO;
        InterfaceC0163b interfaceC0163bI2 = kVarN.I(iA2, 1, 1);
        if (e == E.LENIENT) {
            int iB3 = b(interfaceC0163bI2);
            int i4 = interfaceC0163bI2.i(a.DAY_OF_YEAR);
            interfaceC0163bD2 = interfaceC0163bI2.d(j$.com.android.tools.r8.a.P(j$.com.android.tools.r8.a.V(j$.com.android.tools.r8.a.W(j2, a(h(i4, iB3), i4)), 7), iE - b(interfaceC0163bI2)), (t) b.DAYS);
        } else {
            long jA2 = vVar.a(j2, this);
            int iB4 = b(interfaceC0163bI2);
            int i5 = interfaceC0163bI2.i(a.DAY_OF_YEAR);
            InterfaceC0163b interfaceC0163bD6 = interfaceC0163bI2.d((((int) (jA2 - ((long) a(h(i5, iB4), i5)))) * 7) + (iE - b(interfaceC0163bI2)), (t) b.DAYS);
            if (e == E.STRICT && interfaceC0163bD6.D(aVar3) != iA2) {
                throw new j$.time.b("Strict mode rejected resolved date as it is in a different year");
            }
            interfaceC0163bD2 = interfaceC0163bD6;
        }
        map.remove(this);
        map.remove(aVar3);
        map.remove(aVar2);
        return interfaceC0163bD2;
    }

    @Override // j$.time.temporal.r
    public final v n() {
        return this.e;
    }

    @Override // j$.time.temporal.r
    public final boolean i(n nVar) {
        if (!nVar.e(a.DAY_OF_WEEK)) {
            return false;
        }
        b bVar = b.WEEKS;
        t tVar = this.d;
        if (tVar == bVar) {
            return true;
        }
        if (tVar == b.MONTHS) {
            return nVar.e(a.DAY_OF_MONTH);
        }
        if (tVar == b.YEARS) {
            return nVar.e(a.DAY_OF_YEAR);
        }
        if (tVar == x.h) {
            return nVar.e(a.DAY_OF_YEAR);
        }
        if (tVar == b.FOREVER) {
            return nVar.e(a.YEAR);
        }
        return false;
    }

    @Override // j$.time.temporal.r
    public final v j(n nVar) {
        b bVar = b.WEEKS;
        t tVar = this.d;
        if (tVar == bVar) {
            return this.e;
        }
        if (tVar == b.MONTHS) {
            return f(nVar, a.DAY_OF_MONTH);
        }
        if (tVar == b.YEARS) {
            return f(nVar, a.DAY_OF_YEAR);
        }
        if (tVar == x.h) {
            return g(nVar);
        }
        if (tVar == b.FOREVER) {
            return a.YEAR.b;
        }
        throw new IllegalStateException("unreachable, rangeUnit: " + tVar + ", this: " + this);
    }

    public final v f(n nVar, a aVar) {
        int iH = h(nVar.i(aVar), b(nVar));
        v vVarK = nVar.k(aVar);
        return v.f(a(iH, (int) vVarK.a), a(iH, (int) vVarK.d));
    }

    public final v g(n nVar) {
        a aVar = a.DAY_OF_YEAR;
        if (!nVar.e(aVar)) {
            return h;
        }
        int iB = b(nVar);
        int i2 = nVar.i(aVar);
        int iH = h(i2, iB);
        int iA = a(iH, i2);
        if (iA != 0) {
            int i3 = (int) nVar.k(aVar).d;
            int iA2 = a(iH, this.b.b + i3);
            if (iA >= iA2) {
                return g(j$.com.android.tools.r8.a.N(nVar).A(nVar).d((i3 - i2) + 8, (t) b.DAYS));
            }
            return v.f(1L, iA2 - 1);
        }
        return g(j$.com.android.tools.r8.a.N(nVar).A(nVar).y(i2 + 7, b.DAYS));
    }

    public final String toString() {
        return this.a + "[" + this.b.toString() + "]";
    }
}
