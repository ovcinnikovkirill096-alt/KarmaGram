package j$.time;

import j$.util.Objects;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import okhttp3.internal.http2.Http2Connection;
import okhttp3.internal.url._UrlKt;
import org.mvel2.MVEL;
import org.telegram.messenger.MediaDataController;

public final class i implements j$.time.temporal.m, j$.time.temporal.o, Comparable, Serializable {
    public static final i e;
    public static final i f;
    public static final i g;
    public static final i[] h = new i[24];
    private static final long serialVersionUID = 6414437269572265201L;
    public final byte a;
    public final byte b;
    public final byte c;
    public final int d;

    static {
        int i = 0;
        while (true) {
            i[] iVarArr = h;
            if (i < iVarArr.length) {
                iVarArr[i] = new i(i, 0, 0, 0);
                i++;
            } else {
                i iVar = iVarArr[0];
                g = iVar;
                i iVar2 = iVarArr[12];
                e = iVar;
                f = new i(23, 59, 59, 999999999);
                return;
            }
        }
    }

    public static i U(int i, int i2, int i3, int i4) {
        j$.time.temporal.a.HOUR_OF_DAY.D(i);
        j$.time.temporal.a.MINUTE_OF_HOUR.D(i2);
        j$.time.temporal.a.SECOND_OF_MINUTE.D(i3);
        j$.time.temporal.a.NANO_OF_SECOND.D(i4);
        return R(i, i2, i3, i4);
    }

    public static i V(long j) {
        j$.time.temporal.a.NANO_OF_DAY.D(j);
        int i = (int) (j / 3600000000000L);
        long j2 = j - (((long) i) * 3600000000000L);
        int i2 = (int) (j2 / 60000000000L);
        long j3 = j2 - (((long) i2) * 60000000000L);
        int i3 = (int) (j3 / 1000000000);
        return R(i, i2, i3, (int) (j3 - (((long) i3) * 1000000000)));
    }

    public static i S(j$.time.temporal.n nVar) {
        Objects.requireNonNull(nVar, "temporal");
        i iVar = (i) nVar.t(j$.time.temporal.s.g);
        if (iVar != null) {
            return iVar;
        }
        throw new b("Unable to obtain LocalTime from TemporalAccessor: " + nVar + " of type " + nVar.getClass().getName());
    }

    public static i R(int i, int i2, int i3, int i4) {
        if ((i2 | i3 | i4) == 0) {
            return h[i];
        }
        return new i(i, i2, i3, i4);
    }

    public i(int i, int i2, int i3, int i4) {
        this.a = (byte) i;
        this.b = (byte) i2;
        this.c = (byte) i3;
        this.d = i4;
    }

    @Override // j$.time.temporal.n
    public final boolean e(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return ((j$.time.temporal.a) rVar).Q();
        }
        return rVar != null && rVar.i(this);
    }

    @Override // j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        return j$.time.temporal.s.d(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final int i(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            return T(rVar);
        }
        return j$.time.temporal.s.a(this, rVar);
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        if (rVar instanceof j$.time.temporal.a) {
            if (rVar == j$.time.temporal.a.NANO_OF_DAY) {
                return c0();
            }
            if (rVar == j$.time.temporal.a.MICRO_OF_DAY) {
                return c0() / 1000;
            }
            return T(rVar);
        }
        return rVar.t(this);
    }

    public final int T(j$.time.temporal.r rVar) {
        switch (h.a[((j$.time.temporal.a) rVar).ordinal()]) {
            case 1:
                return this.d;
            case 2:
                throw new j$.time.temporal.u("Invalid field 'NanoOfDay' for get() method, use getLong() instead");
            case 3:
                return this.d / MediaDataController.MAX_STYLE_RUNS_COUNT;
            case 4:
                throw new j$.time.temporal.u("Invalid field 'MicroOfDay' for get() method, use getLong() instead");
            case 5:
                return this.d / 1000000;
            case 6:
                return (int) (c0() / 1000000);
            case 7:
                return this.c;
            case 8:
                return d0();
            case 9:
                return this.b;
            case 10:
                return (this.a * 60) + this.b;
            case 11:
                return this.a % 12;
            case 12:
                int i = this.a % 12;
                if (i % 12 == 0) {
                    return 12;
                }
                return i;
            case 13:
                return this.a;
            case 14:
                byte b = this.a;
                if (b == 0) {
                    return 24;
                }
                return b;
            case 15:
                return this.a / 12;
            default:
                throw new j$.time.temporal.u(c.a("Unsupported field: ", rVar));
        }
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: j */
    public final j$.time.temporal.m x(LocalDate localDate) {
        localDate.getClass();
        return (i) j$.com.android.tools.r8.a.a(localDate, this);
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: e0, reason: merged with bridge method [inline-methods] */
    public final i c(long j, j$.time.temporal.r rVar) {
        if (!(rVar instanceof j$.time.temporal.a)) {
            return (i) rVar.y(this, j);
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) rVar;
        aVar.D(j);
        switch (h.a[aVar.ordinal()]) {
            case 1:
                return f0((int) j);
            case 2:
                return V(j);
            case 3:
                return f0(((int) j) * MediaDataController.MAX_STYLE_RUNS_COUNT);
            case 4:
                return V(j * 1000);
            case 5:
                return f0(((int) j) * 1000000);
            case 6:
                return V(j * 1000000);
            case 7:
                int i = (int) j;
                if (this.c != i) {
                    j$.time.temporal.a.SECOND_OF_MINUTE.D(i);
                    return R(this.a, this.b, i, this.d);
                }
                return this;
            case 8:
                return a0(j - ((long) d0()));
            case 9:
                int i2 = (int) j;
                if (this.b != i2) {
                    j$.time.temporal.a.MINUTE_OF_HOUR.D(i2);
                    return R(this.a, i2, this.c, this.d);
                }
                return this;
            case 10:
                return Y(j - ((long) ((this.a * 60) + this.b)));
            case 11:
                return X(j - ((long) (this.a % 12)));
            case 12:
                if (j == 12) {
                    j = 0;
                }
                return X(j - ((long) (this.a % 12)));
            case 13:
                int i3 = (int) j;
                if (this.a != i3) {
                    j$.time.temporal.a.HOUR_OF_DAY.D(i3);
                    return R(i3, this.b, this.c, this.d);
                }
                return this;
            case 14:
                if (j == 24) {
                    j = 0;
                }
                int i4 = (int) j;
                if (this.a != i4) {
                    j$.time.temporal.a.HOUR_OF_DAY.D(i4);
                    return R(i4, this.b, this.c, this.d);
                }
                return this;
            case 15:
                return X((j - ((long) (this.a / 12))) * 12);
            default:
                throw new j$.time.temporal.u(c.a("Unsupported field: ", rVar));
        }
    }

    public final i f0(int i) {
        if (this.d == i) {
            return this;
        }
        j$.time.temporal.a.NANO_OF_SECOND.D(i);
        return R(this.a, this.b, this.c, i);
    }

    @Override // j$.time.temporal.m
    /* JADX INFO: renamed from: W, reason: merged with bridge method [inline-methods] */
    public final i d(long j, j$.time.temporal.t tVar) {
        if (tVar instanceof j$.time.temporal.b) {
            switch (h.b[((j$.time.temporal.b) tVar).ordinal()]) {
                case 1:
                    return Z(j);
                case 2:
                    return Z((j % 86400000000L) * 1000);
                case 3:
                    return Z((j % 86400000) * 1000000);
                case 4:
                    return a0(j);
                case 5:
                    return Y(j);
                case 6:
                    return X(j);
                case 7:
                    return X((j % 2) * 12);
                default:
                    throw new j$.time.temporal.u("Unsupported unit: " + tVar);
            }
        }
        return (i) tVar.i(this, j);
    }

    public final i X(long j) {
        return j == 0 ? this : R(((((int) (j % 24)) + this.a) + 24) % 24, this.b, this.c, this.d);
    }

    public final i Y(long j) {
        if (j != 0) {
            int i = (this.a * 60) + this.b;
            int i2 = ((((int) (j % 1440)) + i) + 1440) % 1440;
            if (i != i2) {
                return R(i2 / 60, i2 % 60, this.c, this.d);
            }
        }
        return this;
    }

    public final i a0(long j) {
        if (j != 0) {
            int i = (this.b * 60) + (this.a * 3600) + this.c;
            int i2 = ((((int) (j % 86400)) + i) + 86400) % 86400;
            if (i != i2) {
                return R(i2 / 3600, (i2 / 60) % 60, i2 % 60, this.d);
            }
        }
        return this;
    }

    public final i Z(long j) {
        if (j != 0) {
            long jC0 = c0();
            long j2 = (((j % 86400000000000L) + jC0) + 86400000000000L) % 86400000000000L;
            if (jC0 != j2) {
                return R((int) (j2 / 3600000000000L), (int) ((j2 / 60000000000L) % 60), (int) ((j2 / 1000000000) % 60), (int) (j2 % 1000000000));
            }
        }
        return this;
    }

    @Override // j$.time.temporal.m
    public final j$.time.temporal.m y(long j, j$.time.temporal.b bVar) {
        return j == Long.MIN_VALUE ? d(Long.MAX_VALUE, bVar).d(1L, bVar) : d(-j, bVar);
    }

    @Override // j$.time.temporal.n
    public final Object t(e eVar) {
        if (eVar == j$.time.temporal.s.b || eVar == j$.time.temporal.s.a || eVar == j$.time.temporal.s.e || eVar == j$.time.temporal.s.d) {
            return null;
        }
        if (eVar == j$.time.temporal.s.g) {
            return this;
        }
        if (eVar == j$.time.temporal.s.f) {
            return null;
        }
        if (eVar == j$.time.temporal.s.c) {
            return j$.time.temporal.b.NANOS;
        }
        return eVar.g(this);
    }

    @Override // j$.time.temporal.o
    public final j$.time.temporal.m n(j$.time.temporal.m mVar) {
        return mVar.c(c0(), j$.time.temporal.a.NANO_OF_DAY);
    }

    public final int d0() {
        return (this.b * 60) + (this.a * 3600) + this.c;
    }

    public final long c0() {
        return (((long) this.c) * 1000000000) + (((long) this.b) * 60000000000L) + (((long) this.a) * 3600000000000L) + ((long) this.d);
    }

    @Override // java.lang.Comparable
    /* JADX INFO: renamed from: Q, reason: merged with bridge method [inline-methods] */
    public final int compareTo(i iVar) {
        int iCompare = Integer.compare(this.a, iVar.a);
        return (iCompare == 0 && (iCompare = Integer.compare(this.b, iVar.b)) == 0 && (iCompare = Integer.compare(this.c, iVar.c)) == 0) ? Integer.compare(this.d, iVar.d) : iCompare;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof i) {
            i iVar = (i) obj;
            if (this.a == iVar.a && this.b == iVar.b && this.c == iVar.c && this.d == iVar.d) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        long jC0 = c0();
        return (int) (jC0 ^ (jC0 >>> 32));
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(18);
        byte b = this.a;
        byte b2 = this.b;
        byte b3 = this.c;
        int i = this.d;
        sb.append(b < 10 ? MVEL.VERSION_SUB : _UrlKt.FRAGMENT_ENCODE_SET);
        sb.append((int) b);
        sb.append(b2 < 10 ? ":0" : ":");
        sb.append((int) b2);
        if (b3 > 0 || i > 0) {
            sb.append(b3 < 10 ? ":0" : ":");
            sb.append((int) b3);
            if (i > 0) {
                sb.append('.');
                if (i % 1000000 == 0) {
                    sb.append(Integer.toString((i / 1000000) + MediaDataController.MAX_STYLE_RUNS_COUNT).substring(1));
                } else if (i % MediaDataController.MAX_STYLE_RUNS_COUNT == 0) {
                    sb.append(Integer.toString((i / MediaDataController.MAX_STYLE_RUNS_COUNT) + 1000000).substring(1));
                } else {
                    sb.append(Integer.toString(i + Http2Connection.DEGRADED_PONG_TIMEOUT_NS).substring(1));
                }
            }
        }
        return sb.toString();
    }

    private Object writeReplace() {
        return new p((byte) 4, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    public final void g0(DataOutput dataOutput) throws IOException {
        if (this.d == 0) {
            if (this.c == 0) {
                if (this.b == 0) {
                    dataOutput.writeByte(~this.a);
                    return;
                } else {
                    dataOutput.writeByte(this.a);
                    dataOutput.writeByte(~this.b);
                    return;
                }
            }
            dataOutput.writeByte(this.a);
            dataOutput.writeByte(this.b);
            dataOutput.writeByte(~this.c);
            return;
        }
        dataOutput.writeByte(this.a);
        dataOutput.writeByte(this.b);
        dataOutput.writeByte(this.c);
        dataOutput.writeInt(this.d);
    }

    public static i b0(DataInput dataInput) throws IOException {
        int i;
        int i2;
        int i3 = dataInput.readByte();
        int i4 = 0;
        if (i3 < 0) {
            i3 = ~i3;
            i2 = 0;
            i = 0;
        } else {
            byte b = dataInput.readByte();
            if (b < 0) {
                int i5 = ~b;
                i = 0;
                i4 = i5;
                i2 = 0;
            } else {
                byte b2 = dataInput.readByte();
                if (b2 < 0) {
                    i2 = ~b2;
                    i = 0;
                    i4 = b;
                } else {
                    i = dataInput.readInt();
                    i4 = b;
                    i2 = b2;
                }
            }
        }
        return U(i3, i4, i2, i);
    }
}
