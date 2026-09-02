package j$.time.chrono;

import j$.time.LocalDate;
import j$.util.Objects;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;

public final class H extends AbstractC0165d {
    private static final long serialVersionUID = -8722293800195731463L;
    public final transient LocalDate a;

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final ChronoLocalDateTime F(j$.time.i iVar) {
        return new C0167f(this, iVar);
    }

    public H(LocalDate localDate) {
        Objects.requireNonNull(localDate, "isoDate");
        this.a = localDate;
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final k a() {
        return F.c;
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final int hashCode() {
        F.c.getClass();
        return this.a.hashCode() ^ 146118545;
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final l G() {
        return U() >= 1 ? I.BE : I.BEFORE_BE;
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return rVar.j(this);
        }
        if (!j$.com.android.tools.r8.a.r(this, rVar)) {
            throw new j$.time.temporal.u(j$.time.c.a("Unsupported field: ", rVar));
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
        int i = G.a[aVar.ordinal()];
        if (i == 1 || i == 2 || i == 3) {
            return this.a.k(rVar);
        }
        if (i != 4) {
            return F.c.q(aVar);
        }
        j$.time.temporal.v vVar = j$.time.temporal.a.YEAR.b;
        return j$.time.temporal.v.f(1L, U() <= 0 ? (-(vVar.a + 543)) + 1 : 543 + vVar.d);
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            int i = G.a[((j$.time.temporal.a) rVar).ordinal()];
            if (i == 4) {
                int iU = U();
                if (iU < 1) {
                    iU = 1 - iU;
                }
                return iU;
            }
            if (i == 5) {
                return ((((long) U()) * 12) + ((long) this.a.b)) - 1;
            }
            if (i == 6) {
                return U();
            }
            if (i != 7) {
                return this.a.D(rVar);
            }
            return U() < 1 ? 0 : 1;
        }
        return rVar.t(this);
    }

    public final int U() {
        return this.a.getYear() + 543;
    }

    /* JADX WARN: Code duplicated, block: B:16:0x0049  */
    /* JADX WARN: Code duplicated, block: B:18:0x005b A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:19:0x005d A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:22:0x006a  */
    /* JADX WARN: Code duplicated, block: B:24:0x007b  */
    /* JADX WARN: Code duplicated, block: B:26:0x0088  */
    /* JADX WARN: Code duplicated, block: B:29:0x0092  */
    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    /* JADX INFO: renamed from: V, reason: merged with bridge method [inline-methods] */
    public final H c(long j, j$.time.temporal.r rVar) {
        int iA;
        int i;
        if (rVar instanceof j$.time.temporal.a) {
            j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
            if (D(aVar) == j) {
                return this;
            }
            int[] iArr = G.a;
            int i2 = iArr[aVar.ordinal()];
            if (i2 == 4) {
                iA = F.c.q(aVar).a(j, aVar);
                i = iArr[aVar.ordinal()];
                if (i != 4) {
                    LocalDate localDate = this.a;
                    if (U() < 1) {
                        iA = 1 - iA;
                    }
                    return W(localDate.k0(iA - 543));
                }
                if (i != 6) {
                    return W(this.a.k0(iA - 543));
                }
                if (i == 7) {
                    return W(this.a.k0((-542) - U()));
                }
            } else {
                if (i2 == 5) {
                    F.c.q(aVar).b(j, aVar);
                    long jU = ((long) U()) * 12;
                    LocalDate localDate2 = this.a;
                    return W(localDate2.e0(j - ((jU + ((long) localDate2.b)) - 1)));
                }
                if (i2 == 6 || i2 == 7) {
                    iA = F.c.q(aVar).a(j, aVar);
                    i = iArr[aVar.ordinal()];
                    if (i != 4) {
                        LocalDate localDate3 = this.a;
                        if (U() < 1) {
                            iA = 1 - iA;
                        }
                        return W(localDate3.k0(iA - 543));
                    }
                    if (i != 6) {
                        return W(this.a.k0(iA - 543));
                    }
                    if (i == 7) {
                        return W(this.a.k0((-542) - U()));
                    }
                }
            }
            return W(this.a.c(j, rVar));
        }
        return (H) super.c(j, rVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    /* JADX INFO: renamed from: j */
    public final j$.time.temporal.m x(LocalDate localDate) {
        return (H) super.x(localDate);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final InterfaceC0163b x(j$.time.temporal.o oVar) {
        return (H) super.x(oVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final InterfaceC0163b J(j$.time.temporal.q qVar) {
        return (H) super.J(qVar);
    }

    @Override // j$.time.chrono.AbstractC0165d
    public final InterfaceC0163b T(long j) {
        return W(this.a.g0(j));
    }

    @Override // j$.time.chrono.AbstractC0165d
    public final InterfaceC0163b S(long j) {
        return W(this.a.e0(j));
    }

    @Override // j$.time.chrono.AbstractC0165d
    public final InterfaceC0163b R(long j) {
        return W(this.a.plusDays(j));
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b, j$.time.temporal.m
    public final InterfaceC0163b d(long j, j$.time.temporal.t tVar) {
        return (H) super.d(j, tVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    public final j$.time.temporal.m d(long j, j$.time.temporal.t tVar) {
        return (H) super.d(j, tVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    /* JADX INFO: renamed from: r */
    public final InterfaceC0163b y(long j, j$.time.temporal.t tVar) {
        return (H) super.y(j, tVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    public final j$.time.temporal.m y(long j, j$.time.temporal.b bVar) {
        return (H) super.y(j, bVar);
    }

    public final H W(LocalDate localDate) {
        return localDate.equals(this.a) ? this : new H(localDate);
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
        if (obj instanceof H) {
            return this.a.equals(((H) obj).a);
        }
        return false;
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    private Object writeReplace() {
        return new D((byte) 8, this);
    }
}
