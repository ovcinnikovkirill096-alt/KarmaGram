package j$.time.chrono;

import j$.time.Instant;
import j$.time.LocalDate;
import j$.time.ZoneId;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class F extends AbstractC0162a implements Serializable {
    public static final F c = new F();
    private static final long serialVersionUID = 2775954514031616474L;

    static {
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        HashMap map3 = new HashMap();
        map.put("en", new String[]{"BB", "BE"});
        map.put("th", new String[]{"BB", "BE"});
        map2.put("en", new String[]{"B.B.", "B.E."});
        map2.put("th", new String[]{"พ.ศ.", "ปีก่อนคริสต์กาลที่"});
        map3.put("en", new String[]{"Before Buddhist", "Budhhist Era"});
        map3.put("th", new String[]{"พุทธศักราช", "ปีก่อนคริสต์กาลที่"});
    }

    @Override // j$.time.chrono.k
    public final l u(int i) {
        if (i == 0) {
            return I.BEFORE_BE;
        }
        if (i == 1) {
            return I.BE;
        }
        throw new j$.time.b("Invalid era: " + i);
    }

    @Override // j$.time.chrono.k
    public final String getId() {
        return "ThaiBuddhist";
    }

    @Override // j$.time.chrono.k
    public final String l() {
        return "buddhist";
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b I(int i, int i2, int i3) {
        return new H(LocalDate.of(i - 543, i2, i3));
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b m(int i, int i2) {
        return new H(LocalDate.c0(i - 543, i2));
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b h(long j) {
        return new H(LocalDate.b0(j));
    }

    @Override // j$.time.chrono.AbstractC0162a
    public final InterfaceC0163b j() {
        return new H(LocalDate.S(LocalDate.a0(j$.com.android.tools.r8.a.Z())));
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b A(j$.time.temporal.n nVar) {
        if (nVar instanceof H) {
            return (H) nVar;
        }
        return new H(LocalDate.S(nVar));
    }

    @Override // j$.time.chrono.k
    public final boolean O(long j) {
        return r.c.O(j - 543);
    }

    @Override // j$.time.chrono.k
    public final int v(l lVar, int i) {
        if (lVar instanceof I) {
            return lVar == I.BE ? i : 1 - i;
        }
        throw new ClassCastException("Era must be BuddhistEra");
    }

    private F() {
    }

    @Override // j$.time.chrono.k
    public final List s() {
        return j$.com.android.tools.r8.a.Q(I.values());
    }

    @Override // j$.time.chrono.k
    public final j$.time.temporal.v q(j$.time.temporal.a aVar) {
        int i = E.a[aVar.ordinal()];
        if (i == 1) {
            j$.time.temporal.v vVar = j$.time.temporal.a.PROLEPTIC_MONTH.b;
            return j$.time.temporal.v.f(vVar.a + 6516, vVar.d + 6516);
        }
        if (i == 2) {
            j$.time.temporal.v vVar2 = j$.time.temporal.a.YEAR.b;
            return j$.time.temporal.v.g(1L, (-(vVar2.a + 543)) + 1, vVar2.d + 543);
        }
        if (i != 3) {
            return aVar.b;
        }
        j$.time.temporal.v vVar3 = j$.time.temporal.a.YEAR.b;
        return j$.time.temporal.v.f(vVar3.a + 543, vVar3.d + 543);
    }

    @Override // j$.time.chrono.AbstractC0162a, j$.time.chrono.k
    public final InterfaceC0163b K(Map map, j$.time.format.E e) {
        return (H) super.K(map, e);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    @Override // j$.time.chrono.k
    public final ChronoZonedDateTime L(Instant instant, ZoneId zoneId) {
        return j.R(this, instant, zoneId);
    }

    public Object writeReplace() {
        return new D((byte) 1, this);
    }
}
