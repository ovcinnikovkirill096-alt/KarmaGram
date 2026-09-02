package j$.time.chrono;

import j$.time.LocalDate;
import j$.util.Objects;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;

public final class B extends AbstractC0165d {
    private static final long serialVersionUID = 1300372329181994526L;
    public final transient LocalDate a;

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final ChronoLocalDateTime F(j$.time.i iVar) {
        return new C0167f(this, iVar);
    }

    public B(LocalDate localDate) {
        Objects.requireNonNull(localDate, "isoDate");
        this.a = localDate;
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final k a() {
        return z.c;
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final int hashCode() {
        z.c.getClass();
        return this.a.hashCode() ^ (-1990173233);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final l G() {
        return U() >= 1 ? C.ROC : C.BEFORE_ROC;
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
        int i = A.a[aVar.ordinal()];
        if (i == 1 || i == 2 || i == 3) {
            return this.a.k(rVar);
        }
        if (i != 4) {
            return z.c.q(aVar);
        }
        j$.time.temporal.v vVar = j$.time.temporal.a.YEAR.b;
        return j$.time.temporal.v.f(1L, U() <= 0 ? (-vVar.a) + 1912 : vVar.d - 1911);
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            int i = A.a[((j$.time.temporal.a) rVar).ordinal()];
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
        return this.a.getYear() - 1911;
    }

    /* JADX WARN: Code duplicated, block: B:16:0x0049  */
    /* JADX WARN: Code duplicated, block: B:18:0x005b A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:19:0x005d A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:22:0x006a  */
    /* JADX WARN: Code duplicated, block: B:24:0x007b  */
    /* JADX WARN: Code duplicated, block: B:26:0x0088  */
    /* JADX WARN: Code duplicated, block: B:28:0x0091  */
    /* JADX WARN: Code duplicated, block: B:29:0x0094  */
    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    /* JADX INFO: renamed from: V, reason: merged with bridge method [inline-methods] */
    public final B c(long j, j$.time.temporal.r rVar) {
        int iA;
        int i;
        int i2;
        if (rVar instanceof j$.time.temporal.a) {
            j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
            if (D(aVar) == j) {
                return this;
            }
            int[] iArr = A.a;
            int i3 = iArr[aVar.ordinal()];
            if (i3 == 4) {
                iA = z.c.q(aVar).a(j, aVar);
                i = iArr[aVar.ordinal()];
                if (i != 4) {
                    LocalDate localDate = this.a;
                    if (U() >= 1) {
                        i2 = iA + 1911;
                    } else {
                        i2 = 1912 - iA;
                    }
                    return W(localDate.k0(i2));
                }
                if (i != 6) {
                    return W(this.a.k0(iA + 1911));
                }
                if (i == 7) {
                    return W(this.a.k0(1912 - U()));
                }
            } else {
                if (i3 == 5) {
                    z.c.q(aVar).b(j, aVar);
                    long jU = ((long) U()) * 12;
                    LocalDate localDate2 = this.a;
                    return W(localDate2.e0(j - ((jU + ((long) localDate2.b)) - 1)));
                }
                if (i3 == 6 || i3 == 7) {
                    iA = z.c.q(aVar).a(j, aVar);
                    i = iArr[aVar.ordinal()];
                    if (i != 4) {
                        LocalDate localDate3 = this.a;
                        if (U() >= 1) {
                            i2 = iA + 1911;
                        } else {
                            i2 = 1912 - iA;
                        }
                        return W(localDate3.k0(i2));
                    }
                    if (i != 6) {
                        return W(this.a.k0(iA + 1911));
                    }
                    if (i == 7) {
                        return W(this.a.k0(1912 - U()));
                    }
                }
            }
            return W(this.a.c(j, rVar));
        }
        return (B) super.c(j, rVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    /* JADX INFO: renamed from: j */
    public final j$.time.temporal.m x(LocalDate localDate) {
        return (B) super.x(localDate);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final InterfaceC0163b x(j$.time.temporal.o oVar) {
        return (B) super.x(oVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final InterfaceC0163b J(j$.time.temporal.q qVar) {
        return (B) super.J(qVar);
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
        return (B) super.d(j, tVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    public final j$.time.temporal.m d(long j, j$.time.temporal.t tVar) {
        return (B) super.d(j, tVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    /* JADX INFO: renamed from: r */
    public final InterfaceC0163b y(long j, j$.time.temporal.t tVar) {
        return (B) super.y(j, tVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    public final j$.time.temporal.m y(long j, j$.time.temporal.b bVar) {
        return (B) super.y(j, bVar);
    }

    public final B W(LocalDate localDate) {
        return localDate.equals(this.a) ? this : new B(localDate);
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
        if (obj instanceof B) {
            return this.a.equals(((B) obj).a);
        }
        return false;
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    private Object writeReplace() {
        return new D((byte) 7, this);
    }
}
