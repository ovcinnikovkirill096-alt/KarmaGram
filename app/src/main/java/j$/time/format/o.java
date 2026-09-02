package j$.time.format;

import j$.time.LocalDate;
import j$.time.chrono.InterfaceC0163b;
import j$.util.Objects;
import j$.util.function.Consumer$CC;
import java.util.ArrayList;
import java.util.function.Consumer;

public final class o extends i {
    public static final LocalDate h = LocalDate.of(2000, 1, 1);
    public final InterfaceC0163b g;

    @Override // j$.time.format.i
    public final boolean b(v vVar) {
        if (vVar.c) {
            return super.b(vVar);
        }
        return false;
    }

    public o(j$.time.temporal.r rVar, int i, int i2, InterfaceC0163b interfaceC0163b, int i3) {
        super(rVar, i, i2, F.NOT_NEGATIVE, i3);
        this.g = interfaceC0163b;
    }

    @Override // j$.time.format.i
    public final long a(y yVar, long j) {
        long jAbs = Math.abs(j);
        InterfaceC0163b interfaceC0163b = this.g;
        long jI = interfaceC0163b != null ? j$.com.android.tools.r8.a.N(yVar.a).A(interfaceC0163b).i(this.a) : 0;
        long[] jArr = i.f;
        if (j >= jI) {
            long j2 = jArr[this.b];
            if (j < jI + j2) {
                return jAbs % j2;
            }
        }
        return jAbs % jArr[this.c];
    }

    @Override // j$.time.format.i
    public final int c(v vVar, long j, int i, int i2) {
        final o oVar;
        final v vVar2;
        final long j2;
        final int i3;
        final int i4;
        int i5;
        long j3;
        InterfaceC0163b interfaceC0163b = this.g;
        if (interfaceC0163b != null) {
            j$.time.chrono.k kVar = vVar.c().c;
            if (kVar == null && (kVar = vVar.a.e) == null) {
                kVar = j$.time.chrono.r.c;
            }
            i5 = kVar.A(interfaceC0163b).i(this.a);
            oVar = this;
            vVar2 = vVar;
            j2 = j;
            i3 = i;
            i4 = i2;
            Consumer consumer = new Consumer() { // from class: j$.time.format.n
                public final /* synthetic */ Consumer andThen(Consumer consumer2) {
                    return Consumer$CC.$default$andThen(this, consumer2);
                }

                @Override // java.util.function.Consumer
                /* JADX INFO: renamed from: accept */
                public final void v(Object obj) {
                    this.a.c(vVar2, j2, i3, i4);
                }
            };
            if (vVar2.e == null) {
                vVar2.e = new ArrayList();
            }
            vVar2.e.add(consumer);
        } else {
            oVar = this;
            vVar2 = vVar;
            j2 = j;
            i3 = i;
            i4 = i2;
            i5 = 0;
        }
        int i6 = i4 - i3;
        int i7 = oVar.b;
        if (i6 != i7 || j2 < 0) {
            j3 = j2;
        } else {
            long j4 = i.f[i7];
            long j5 = i5;
            long j6 = j5 - (j5 % j4);
            long j7 = i5 > 0 ? j6 + j2 : j6 - j2;
            j3 = j7 < j5 ? j4 + j7 : j7;
        }
        return vVar2.f(oVar.a, j3, i3, i4);
    }

    @Override // j$.time.format.i
    public final i d() {
        if (this.e == -1) {
            return this;
        }
        return new o(this.a, this.b, this.c, this.g, -1);
    }

    @Override // j$.time.format.i
    public final i e(int i) {
        return new o(this.a, this.b, this.c, this.g, this.e + i);
    }

    @Override // j$.time.format.i
    public final String toString() {
        return "ReducedValue(" + this.a + "," + this.b + "," + this.c + "," + Objects.requireNonNullElse(this.g, 0) + ")";
    }
}
