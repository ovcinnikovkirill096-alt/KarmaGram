package j$.time.chrono;

import j$.time.LocalDate;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;

public final class w extends AbstractC0165d {
    public static final LocalDate d = LocalDate.of(1873, 1, 1);
    private static final long serialVersionUID = -305327627230580483L;
    public final transient LocalDate a;
    public final transient x b;
    public final transient int c;

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final ChronoLocalDateTime F(j$.time.i iVar) {
        return new C0167f(this, iVar);
    }

    public w(LocalDate localDate) {
        if (localDate.X(d)) {
            throw new j$.time.b("JapaneseDate before Meiji 6 is not supported");
        }
        x xVarH = x.h(localDate);
        this.b = xVarH;
        this.c = (localDate.getYear() - xVarH.b.getYear()) + 1;
        this.a = localDate;
    }

    public w(x xVar, int i, LocalDate localDate) {
        if (localDate.X(d)) {
            throw new j$.time.b("JapaneseDate before Meiji 6 is not supported");
        }
        this.b = xVar;
        this.c = i;
        this.a = localDate;
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final k a() {
        return u.c;
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final int hashCode() {
        u.c.getClass();
        return this.a.hashCode() ^ (-688086063);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final l G() {
        return this.b;
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final int M() {
        int iM;
        x xVarL = this.b.l();
        if (xVarL != null && xVarL.b.getYear() == this.a.getYear()) {
            iM = xVarL.b.V() - 1;
        } else {
            iM = this.a.M();
        }
        return this.c == 1 ? iM - (this.b.b.V() - 1) : iM;
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b, j$.time.temporal.n
    public final boolean e(j$.time.temporal.r rVar) {
        if (rVar == j$.time.temporal.a.ALIGNED_DAY_OF_WEEK_IN_MONTH || rVar == j$.time.temporal.a.ALIGNED_DAY_OF_WEEK_IN_YEAR || rVar == j$.time.temporal.a.ALIGNED_WEEK_OF_MONTH || rVar == j$.time.temporal.a.ALIGNED_WEEK_OF_YEAR) {
            return false;
        }
        if (rVar instanceof j$.time.temporal.a) {
            return ((j$.time.temporal.a) rVar).isDateBased();
        }
        return rVar != null && rVar.i(this);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return rVar.j(this);
        }
        if (!e(rVar)) {
            throw new j$.time.temporal.u(j$.time.c.a("Unsupported field: ", rVar));
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
        int i = v.a[aVar.ordinal()];
        if (i == 1) {
            return j$.time.temporal.v.f(1L, this.a.Y());
        }
        if (i == 2) {
            return j$.time.temporal.v.f(1L, M());
        }
        if (i != 3) {
            return u.c.q(aVar);
        }
        int year = this.b.b.getYear();
        x xVarL = this.b.l();
        return xVarL != null ? j$.time.temporal.v.f(1L, (xVarL.b.getYear() - year) + 1) : j$.time.temporal.v.f(1L, 999999999 - year);
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return rVar.t(this);
        }
        switch (v.a[((j$.time.temporal.a) rVar).ordinal()]) {
            case 2:
                return this.c == 1 ? (this.a.V() - this.b.b.V()) + 1 : this.a.V();
            case 3:
                return this.c;
            case 4:
            case 5:
            case 6:
            case 7:
                throw new j$.time.temporal.u(j$.time.c.a("Unsupported field: ", rVar));
            case 8:
                return this.b.a;
            default:
                return this.a.D(rVar);
        }
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    /* JADX INFO: renamed from: V, reason: merged with bridge method [inline-methods] */
    public final w c(long j, j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
            if (D(aVar) == j) {
                return this;
            }
            int[] iArr = v.a;
            int i = iArr[aVar.ordinal()];
            if (i == 3 || i == 8 || i == 9) {
                u uVar = u.c;
                int iA = uVar.q(aVar).a(j, aVar);
                int i2 = iArr[aVar.ordinal()];
                if (i2 == 3) {
                    return X(this.a.k0(uVar.v(this.b, iA)));
                }
                if (i2 == 8) {
                    return X(this.a.k0(uVar.v(x.m(iA), this.c)));
                }
                if (i2 == 9) {
                    return X(this.a.k0(iA));
                }
            }
            return X(this.a.c(j, rVar));
        }
        return (w) super.c(j, rVar);
    }

    public final w W(j$.time.e eVar) {
        return (w) super.x(eVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    /* JADX INFO: renamed from: j */
    public final j$.time.temporal.m x(LocalDate localDate) {
        return (w) super.x(localDate);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final InterfaceC0163b x(j$.time.temporal.o oVar) {
        return (w) super.x(oVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final InterfaceC0163b J(j$.time.temporal.q qVar) {
        return (w) super.J(qVar);
    }

    @Override // j$.time.chrono.AbstractC0165d
    public final InterfaceC0163b T(long j) {
        return X(this.a.g0(j));
    }

    @Override // j$.time.chrono.AbstractC0165d
    public final InterfaceC0163b S(long j) {
        return X(this.a.e0(j));
    }

    @Override // j$.time.chrono.AbstractC0165d
    public final InterfaceC0163b R(long j) {
        return X(this.a.plusDays(j));
    }

    public final w U(long j, j$.time.temporal.b bVar) {
        return (w) super.d(j, (j$.time.temporal.t) bVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b, j$.time.temporal.m
    public final InterfaceC0163b d(long j, j$.time.temporal.t tVar) {
        return (w) super.d(j, tVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    public final j$.time.temporal.m d(long j, j$.time.temporal.t tVar) {
        return (w) super.d(j, tVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    /* JADX INFO: renamed from: r */
    public final InterfaceC0163b y(long j, j$.time.temporal.t tVar) {
        return (w) super.y(j, tVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    public final j$.time.temporal.m y(long j, j$.time.temporal.b bVar) {
        return (w) super.y(j, bVar);
    }

    public final w X(LocalDate localDate) {
        return localDate.equals(this.a) ? this : new w(localDate);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final long E() {
        return this.a.E();
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof w) {
            return this.a.equals(((w) obj).a);
        }
        return false;
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    private Object writeReplace() {
        return new D((byte) 4, this);
    }
}
