package j$.time.chrono;

import j$.time.LocalDate;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.Arrays;

public final class p extends AbstractC0165d {
    private static final long serialVersionUID = -5207853542612002020L;
    public final transient n a;
    public final transient int b;
    public final transient int c;
    public final transient int d;

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final ChronoLocalDateTime F(j$.time.i iVar) {
        return new C0167f(this, iVar);
    }

    public p(n nVar, int i, int i2, int i3) {
        nVar.T(i, i2, i3);
        this.a = nVar;
        this.b = i;
        this.c = i2;
        this.d = i3;
    }

    public p(n nVar, long j) {
        int i = (int) j;
        nVar.Q();
        if (i < nVar.e || i >= nVar.f) {
            throw new j$.time.b("Hijrah date out of range");
        }
        int iBinarySearch = Arrays.binarySearch(nVar.d, i);
        iBinarySearch = iBinarySearch < 0 ? (-iBinarySearch) - 2 : iBinarySearch;
        int[] iArr = {nVar.S(iBinarySearch), ((nVar.g + iBinarySearch) % 12) + 1, (i - nVar.d[iBinarySearch]) + 1};
        this.a = nVar;
        this.b = iArr[0];
        this.c = iArr[1];
        this.d = iArr[2];
    }

    @Override // j$.time.chrono.InterfaceC0163b
    public final k a() {
        return this.a;
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final l G() {
        return q.AH;
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final int M() {
        return this.a.W(this.b, 12);
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
        int i = o.a[aVar.ordinal()];
        if (i == 1) {
            return j$.time.temporal.v.f(1L, this.a.U(this.b, this.c));
        }
        if (i != 2) {
            return i != 3 ? this.a.q(aVar) : j$.time.temporal.v.f(1L, 5L);
        }
        return j$.time.temporal.v.f(1L, M());
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return rVar.t(this);
        }
        switch (o.a[((j$.time.temporal.a) rVar).ordinal()]) {
            case 1:
                return this.d;
            case 2:
                return U();
            case 3:
                return ((this.d - 1) / 7) + 1;
            case 4:
                return ((int) j$.com.android.tools.r8.a.T(E() + 3, 7)) + 1;
            case 5:
                return ((this.d - 1) % 7) + 1;
            case 6:
                return ((U() - 1) % 7) + 1;
            case 7:
                return E();
            case 8:
                return ((U() - 1) / 7) + 1;
            case 9:
                return this.c;
            case 10:
                return ((((long) this.b) * 12) + ((long) this.c)) - 1;
            case 11:
                return this.b;
            case 12:
                return this.b;
            case 13:
                return this.b <= 1 ? 0 : 1;
            default:
                throw new j$.time.temporal.u(j$.time.c.a("Unsupported field: ", rVar));
        }
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    /* JADX INFO: renamed from: Y, reason: merged with bridge method [inline-methods] */
    public final p c(long j, j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return (p) super.c(j, rVar);
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
        this.a.q(aVar).b(j, aVar);
        int i = (int) j;
        switch (o.a[aVar.ordinal()]) {
            case 1:
                return X(this.b, this.c, i);
            case 2:
                return R(Math.min(i, M()) - U());
            case 3:
                return R((j - D(j$.time.temporal.a.ALIGNED_WEEK_OF_MONTH)) * 7);
            case 4:
                return R(j - ((long) (((int) j$.com.android.tools.r8.a.T(E() + 3, 7)) + 1)));
            case 5:
                return R(j - D(j$.time.temporal.a.ALIGNED_DAY_OF_WEEK_IN_MONTH));
            case 6:
                return R(j - D(j$.time.temporal.a.ALIGNED_DAY_OF_WEEK_IN_YEAR));
            case 7:
                return new p(this.a, j);
            case 8:
                return R((j - D(j$.time.temporal.a.ALIGNED_WEEK_OF_YEAR)) * 7);
            case 9:
                return X(this.b, i, this.d);
            case 10:
                return S(j - (((((long) this.b) * 12) + ((long) this.c)) - 1));
            case 11:
                if (this.b < 1) {
                    i = 1 - i;
                }
                return X(i, this.c, this.d);
            case 12:
                return X(i, this.c, this.d);
            case 13:
                return X(1 - this.b, this.c, this.d);
            default:
                throw new j$.time.temporal.u(j$.time.c.a("Unsupported field: ", rVar));
        }
    }

    public final p X(int i, int i2, int i3) {
        int iU = this.a.U(i, i2);
        if (i3 > iU) {
            i3 = iU;
        }
        return new p(this.a, i, i2, i3);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    /* JADX INFO: renamed from: j */
    public final j$.time.temporal.m x(LocalDate localDate) {
        return (p) super.x(localDate);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final InterfaceC0163b x(j$.time.temporal.o oVar) {
        return (p) super.x(oVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final InterfaceC0163b J(j$.time.temporal.q qVar) {
        return (p) super.J(qVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final long E() {
        return this.a.T(this.b, this.c, this.d);
    }

    public final int U() {
        return this.a.W(this.b, this.c - 1) + this.d;
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final boolean p() {
        return this.a.O(this.b);
    }

    @Override // j$.time.chrono.AbstractC0165d
    public final InterfaceC0163b T(long j) {
        if (j == 0) {
            return this;
        }
        long j2 = ((long) this.b) + ((long) ((int) j));
        int i = (int) j2;
        if (j2 == i) {
            return X(i, this.c, this.d);
        }
        throw new ArithmeticException();
    }

    @Override // j$.time.chrono.AbstractC0165d
    /* JADX INFO: renamed from: W, reason: merged with bridge method [inline-methods] */
    public final p S(long j) {
        if (j == 0) {
            return this;
        }
        long j2 = (((long) this.b) * 12) + ((long) (this.c - 1)) + j;
        n nVar = this.a;
        long jU = j$.com.android.tools.r8.a.U(j2, 12L);
        if (jU >= nVar.S(0) && jU <= nVar.S(nVar.d.length - 1) - 1) {
            return X((int) jU, ((int) j$.com.android.tools.r8.a.T(j2, 12L)) + 1, this.d);
        }
        throw new j$.time.b("Invalid Hijrah year: " + jU);
    }

    @Override // j$.time.chrono.AbstractC0165d
    /* JADX INFO: renamed from: V, reason: merged with bridge method [inline-methods] */
    public final p R(long j) {
        return new p(this.a, E() + j);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b, j$.time.temporal.m
    public final InterfaceC0163b d(long j, j$.time.temporal.t tVar) {
        return (p) super.d(j, tVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    public final j$.time.temporal.m d(long j, j$.time.temporal.t tVar) {
        return (p) super.d(j, tVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    /* JADX INFO: renamed from: r */
    public final InterfaceC0163b y(long j, j$.time.temporal.t tVar) {
        return (p) super.y(j, tVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.temporal.m
    public final j$.time.temporal.m y(long j, j$.time.temporal.b bVar) {
        return (p) super.y(j, bVar);
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof p) {
            p pVar = (p) obj;
            if (this.b == pVar.b && this.c == pVar.c && this.d == pVar.d && this.a.equals(pVar.a)) {
                return true;
            }
        }
        return false;
    }

    @Override // j$.time.chrono.AbstractC0165d, j$.time.chrono.InterfaceC0163b
    public final int hashCode() {
        int i = this.b;
        int i2 = this.c;
        int i3 = this.d;
        this.a.getClass();
        return (((i << 11) + (i2 << 6)) + i3) ^ ((i & (-2048)) ^ 2100100019);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    private Object writeReplace() {
        return new D((byte) 6, this);
    }
}
