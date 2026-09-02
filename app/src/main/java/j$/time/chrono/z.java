package j$.time.chrono;

import j$.time.Instant;
import j$.time.LocalDate;
import j$.time.ZoneId;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public final class z extends AbstractC0162a implements Serializable {
    public static final z c = new z();
    private static final long serialVersionUID = 1039765215346859963L;

    @Override // j$.time.chrono.k
    public final String getId() {
        return "Minguo";
    }

    @Override // j$.time.chrono.k
    public final l u(int i) {
        if (i == 0) {
            return C.BEFORE_ROC;
        }
        if (i == 1) {
            return C.ROC;
        }
        throw new j$.time.b("Invalid era: " + i);
    }

    @Override // j$.time.chrono.k
    public final String l() {
        return "roc";
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b I(int i, int i2, int i3) {
        return new B(LocalDate.of(i + 1911, i2, i3));
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b m(int i, int i2) {
        return new B(LocalDate.c0(i + 1911, i2));
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b h(long j) {
        return new B(LocalDate.b0(j));
    }

    @Override // j$.time.chrono.AbstractC0162a
    public final InterfaceC0163b j() {
        return new B(LocalDate.S(LocalDate.a0(j$.com.android.tools.r8.a.Z())));
    }

    @Override // j$.time.chrono.k
    public final InterfaceC0163b A(j$.time.temporal.n nVar) {
        if (nVar instanceof B) {
            return (B) nVar;
        }
        return new B(LocalDate.S(nVar));
    }

    @Override // j$.time.chrono.k
    public final boolean O(long j) {
        return r.c.O(j + 1911);
    }

    @Override // j$.time.chrono.k
    public final int v(l lVar, int i) {
        if (lVar instanceof C) {
            return lVar == C.ROC ? i : 1 - i;
        }
        throw new ClassCastException("Era must be MinguoEra");
    }

    @Override // j$.time.chrono.k
    public final List s() {
        return j$.com.android.tools.r8.a.Q(C.values());
    }

    @Override // j$.time.chrono.k
    public final j$.time.temporal.v q(j$.time.temporal.a aVar) {
        int i = y.a[aVar.ordinal()];
        if (i == 1) {
            j$.time.temporal.v vVar = j$.time.temporal.a.PROLEPTIC_MONTH.b;
            return j$.time.temporal.v.f(vVar.a - 22932, vVar.d - 22932);
        }
        if (i == 2) {
            j$.time.temporal.v vVar2 = j$.time.temporal.a.YEAR.b;
            return j$.time.temporal.v.g(1L, vVar2.d - 1911, (-vVar2.a) + 1912);
        }
        if (i != 3) {
            return aVar.b;
        }
        j$.time.temporal.v vVar3 = j$.time.temporal.a.YEAR.b;
        return j$.time.temporal.v.f(vVar3.a - 1911, vVar3.d - 1911);
    }

    @Override // j$.time.chrono.AbstractC0162a, j$.time.chrono.k
    public final InterfaceC0163b K(Map map, j$.time.format.E e) {
        return (B) super.K(map, e);
    }

    private z() {
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
