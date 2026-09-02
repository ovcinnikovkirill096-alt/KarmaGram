package j$.time.chrono;

import j$.util.Objects;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/* JADX INFO: renamed from: j$.time.chrono.g, reason: case insensitive filesystem */
public final class C0168g implements j$.time.temporal.q, Serializable {
    public static final /* synthetic */ int e = 0;
    private static final long serialVersionUID = 57387258289L;
    public final k a;
    public final int b;
    public final int c;
    public final int d;

    static {
        j$.com.android.tools.r8.a.Q(new Object[]{j$.time.temporal.b.YEARS, j$.time.temporal.b.MONTHS, j$.time.temporal.b.DAYS});
    }

    public C0168g(k kVar, int i, int i2, int i3) {
        Objects.requireNonNull(kVar, "chrono");
        this.a = kVar;
        this.b = i;
        this.c = i2;
        this.d = i3;
    }

    public final String toString() {
        if (this.b == 0 && this.c == 0 && this.d == 0) {
            return this.a.toString() + " P0D";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.a.toString());
        sb.append(" P");
        int i = this.b;
        if (i != 0) {
            sb.append(i);
            sb.append('Y');
        }
        int i2 = this.c;
        if (i2 != 0) {
            sb.append(i2);
            sb.append('M');
        }
        int i3 = this.d;
        if (i3 != 0) {
            sb.append(i3);
            sb.append('D');
        }
        return sb.toString();
    }

    @Override // j$.time.temporal.q
    public final j$.time.temporal.m i(j$.time.temporal.m mVar) {
        Objects.requireNonNull(mVar, "temporal");
        k kVar = (k) mVar.t(j$.time.temporal.s.b);
        if (kVar == null || this.a.equals(kVar)) {
            if (this.c != 0) {
                j$.time.temporal.v vVarQ = this.a.q(j$.time.temporal.a.MONTH_OF_YEAR);
                long j = (vVarQ.a == vVarQ.b && vVarQ.c == vVarQ.d && vVarQ.d()) ? (vVarQ.d - vVarQ.a) + 1 : -1L;
                if (j > 0) {
                    mVar = mVar.d((((long) this.b) * j) + ((long) this.c), j$.time.temporal.b.MONTHS);
                } else {
                    int i = this.b;
                    if (i != 0) {
                        mVar = mVar.d(i, j$.time.temporal.b.YEARS);
                    }
                    mVar = mVar.d(this.c, j$.time.temporal.b.MONTHS);
                }
            } else {
                int i2 = this.b;
                if (i2 != 0) {
                    mVar = mVar.d(i2, j$.time.temporal.b.YEARS);
                }
            }
            int i3 = this.d;
            return i3 != 0 ? mVar.d(i3, j$.time.temporal.b.DAYS) : mVar;
        }
        throw new j$.time.b("Chronology mismatch, expected: " + this.a.getId() + ", actual: " + kVar.getId());
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof C0168g) {
            C0168g c0168g = (C0168g) obj;
            if (this.b == c0168g.b && this.c == c0168g.c && this.d == c0168g.d && this.a.equals(c0168g.a)) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return (Integer.rotateLeft(this.d, 16) + (Integer.rotateLeft(this.c, 8) + this.b)) ^ this.a.hashCode();
    }

    public Object writeReplace() {
        return new D((byte) 9, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
