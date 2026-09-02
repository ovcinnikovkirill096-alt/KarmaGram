package j$.time;

import j$.util.Objects;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public final class o implements j$.time.temporal.m, j$.time.temporal.o, Comparable, Serializable {
    public static final /* synthetic */ int c = 0;
    private static final long serialVersionUID = 7264499704384272492L;
    public final i a;
    public final ZoneOffset b;

    @Override // java.lang.Comparable
    public final int compareTo(Object obj) {
        o oVar = (o) obj;
        if (this.b.equals(oVar.b)) {
            return this.a.compareTo(oVar.a);
        }
        int iCompare = Long.compare(this.a.c0() - (((long) this.b.getTotalSeconds()) * 1000000000), oVar.a.c0() - (((long) oVar.b.getTotalSeconds()) * 1000000000));
        return iCompare == 0 ? this.a.compareTo(oVar.a) : iCompare;
    }

    static {
        i iVar = i.e;
        ZoneOffset zoneOffset = ZoneOffset.g;
        iVar.getClass();
        new o(iVar, zoneOffset);
        i iVar2 = i.f;
        ZoneOffset zoneOffset2 = ZoneOffset.f;
        iVar2.getClass();
        new o(iVar2, zoneOffset2);
    }

    public o(i iVar, ZoneOffset zoneOffset) {
        this.a = (i) Objects.requireNonNull(iVar, "time");
        this.b = (ZoneOffset) Objects.requireNonNull(zoneOffset, "offset");
    }

    public final o R(i iVar, ZoneOffset zoneOffset) {
        return (this.a == iVar && this.b.equals(zoneOffset)) ? this : new o(iVar, zoneOffset);
    }

    @Override // j$.time.temporal.n
    public final boolean e(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return ((j$.time.temporal.a) rVar).Q() || rVar == j$.time.temporal.a.OFFSET_SECONDS;
        }
        return rVar != null && rVar.i(this);
    }

    @Override // j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            if (rVar != j$.time.temporal.a.OFFSET_SECONDS) {
                i iVar = this.a;
                iVar.getClass();
                return j$.time.temporal.s.d(iVar, rVar);
            }
            return ((j$.time.temporal.a) rVar).b;
        }
        return rVar.j(this);
    }

    @Override // j$.time.temporal.n
    public final int i(j$.time.temporal.r rVar) {
        return j$.time.temporal.s.a(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            if (rVar == j$.time.temporal.a.OFFSET_SECONDS) {
                return this.b.getTotalSeconds();
            }
            return this.a.D(rVar);
        }
        return rVar.t(this);
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: j */
    public final j$.time.temporal.m x(LocalDate localDate) {
        localDate.getClass();
        return (o) j$.com.android.tools.r8.a.a(localDate, this);
    }

    @Override // j$.time.temporal.m
    public final j$.time.temporal.m c(long j, j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            if (rVar == j$.time.temporal.a.OFFSET_SECONDS) {
                j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
                return R(this.a, ZoneOffset.W(aVar.b.a(j, aVar)));
            }
            return R(this.a.c(j, rVar), this.b);
        }
        return (o) rVar.y(this, j);
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: Q, reason: merged with bridge method [inline-methods] */
    public final o d(long j, j$.time.temporal.t tVar) {
        if (tVar instanceof j$.time.temporal.b) {
            return R(this.a.d(j, tVar), this.b);
        }
        return (o) tVar.i(this, j);
    }

    @Override // j$.time.temporal.m
    public final j$.time.temporal.m y(long j, j$.time.temporal.b bVar) {
        return j == Long.MIN_VALUE ? d(Long.MAX_VALUE, bVar).d(1L, bVar) : d(-j, bVar);
    }

    @Override // j$.time.temporal.n
    public final Object t(e eVar) {
        if (eVar == j$.time.temporal.s.d || eVar == j$.time.temporal.s.e) {
            return this.b;
        }
        if (((eVar == j$.time.temporal.s.a) || (eVar == j$.time.temporal.s.b)) || eVar == j$.time.temporal.s.f) {
            return null;
        }
        if (eVar == j$.time.temporal.s.g) {
            return this.a;
        }
        if (eVar == j$.time.temporal.s.c) {
            return j$.time.temporal.b.NANOS;
        }
        return eVar.g(this);
    }

    @Override // j$.time.temporal.o
    public final j$.time.temporal.m n(j$.time.temporal.m mVar) {
        return mVar.c(this.a.c0(), j$.time.temporal.a.NANO_OF_DAY).c(this.b.getTotalSeconds(), j$.time.temporal.a.OFFSET_SECONDS);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof o) {
            o oVar = (o) obj;
            if (this.a.equals(oVar.a) && this.b.equals(oVar.b)) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return this.a.hashCode() ^ this.b.b;
    }

    public final String toString() {
        return this.a.toString() + this.b.c;
    }

    private Object writeReplace() {
        return new p((byte) 9, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
