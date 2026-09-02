package j$.time;

import j$.util.Objects;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StreamCorruptedException;

public final class p implements Externalizable {
    private static final long serialVersionUID = -7683839454370182990L;
    public byte a;
    public Object b;

    public p() {
    }

    public p(byte b, Object obj) {
        this.a = b;
        this.b = obj;
    }

    @Override // java.io.Externalizable
    public final void writeExternal(ObjectOutput objectOutput) throws IOException {
        byte b = this.a;
        Object obj = this.b;
        objectOutput.writeByte(b);
        switch (b) {
            case 1:
                Duration duration = (Duration) obj;
                objectOutput.writeLong(duration.a);
                objectOutput.writeInt(duration.b);
                return;
            case 2:
                Instant instant = (Instant) obj;
                objectOutput.writeLong(instant.a);
                objectOutput.writeInt(instant.b);
                return;
            case 3:
                LocalDate localDate = (LocalDate) obj;
                objectOutput.writeInt(localDate.a);
                objectOutput.writeByte(localDate.b);
                objectOutput.writeByte(localDate.c);
                return;
            case 4:
                ((i) obj).g0(objectOutput);
                return;
            case 5:
                LocalDateTime localDateTime = (LocalDateTime) obj;
                LocalDate localDate2 = localDateTime.a;
                objectOutput.writeInt(localDate2.a);
                objectOutput.writeByte(localDate2.b);
                objectOutput.writeByte(localDate2.c);
                localDateTime.b.g0(objectOutput);
                return;
            case 6:
                ZonedDateTime zonedDateTime = (ZonedDateTime) obj;
                LocalDateTime localDateTime2 = zonedDateTime.a;
                LocalDate localDate3 = localDateTime2.a;
                objectOutput.writeInt(localDate3.a);
                objectOutput.writeByte(localDate3.b);
                objectOutput.writeByte(localDate3.c);
                localDateTime2.b.g0(objectOutput);
                zonedDateTime.b.Z(objectOutput);
                zonedDateTime.c.T(objectOutput);
                return;
            case 7:
                objectOutput.writeUTF(((u) obj).b);
                return;
            case 8:
                ((ZoneOffset) obj).Z(objectOutput);
                return;
            case 9:
                o oVar = (o) obj;
                oVar.a.g0(objectOutput);
                oVar.b.Z(objectOutput);
                return;
            case 10:
                OffsetDateTime offsetDateTime = (OffsetDateTime) obj;
                LocalDateTime localDateTime3 = offsetDateTime.a;
                LocalDate localDate4 = localDateTime3.a;
                objectOutput.writeInt(localDate4.a);
                objectOutput.writeByte(localDate4.b);
                objectOutput.writeByte(localDate4.c);
                localDateTime3.b.g0(objectOutput);
                offsetDateTime.b.Z(objectOutput);
                return;
            case 11:
                objectOutput.writeInt(((r) obj).a);
                return;
            case 12:
                YearMonth yearMonth = (YearMonth) obj;
                objectOutput.writeInt(yearMonth.a);
                objectOutput.writeByte(yearMonth.b);
                return;
            case 13:
                m mVar = (m) obj;
                objectOutput.writeByte(mVar.a);
                objectOutput.writeByte(mVar.b);
                return;
            case 14:
                Period period = (Period) obj;
                objectOutput.writeInt(period.a);
                objectOutput.writeInt(period.b);
                objectOutput.writeInt(period.c);
                return;
            default:
                throw new InvalidClassException("Unknown serialized type");
        }
    }

    @Override // java.io.Externalizable
    public final void readExternal(ObjectInput objectInput) {
        byte b = objectInput.readByte();
        this.a = b;
        this.b = a(b, objectInput);
    }

    public static Object a(byte b, ObjectInput objectInput) throws IOException {
        switch (b) {
            case 1:
                Duration duration = Duration.c;
                long j = objectInput.readLong();
                long j2 = objectInput.readInt();
                return Duration.j(j$.com.android.tools.r8.a.P(j, j$.com.android.tools.r8.a.U(j2, 1000000000L)), (int) j$.com.android.tools.r8.a.T(j2, 1000000000L));
            case 2:
                Instant instant = Instant.c;
                return Instant.S(objectInput.readLong(), objectInput.readInt());
            case 3:
                LocalDate localDate = LocalDate.d;
                return LocalDate.of(objectInput.readInt(), objectInput.readByte(), objectInput.readByte());
            case 4:
                return i.b0(objectInput);
            case 5:
                LocalDateTime localDateTime = LocalDateTime.c;
                LocalDate localDate2 = LocalDate.d;
                return LocalDateTime.T(LocalDate.of(objectInput.readInt(), objectInput.readByte(), objectInput.readByte()), i.b0(objectInput));
            case 6:
                LocalDateTime localDateTime2 = LocalDateTime.c;
                LocalDate localDate3 = LocalDate.d;
                LocalDateTime localDateTimeT = LocalDateTime.T(LocalDate.of(objectInput.readInt(), objectInput.readByte(), objectInput.readByte()), i.b0(objectInput));
                ZoneOffset zoneOffsetY = ZoneOffset.Y(objectInput);
                ZoneId zoneId = (ZoneId) a(objectInput.readByte(), objectInput);
                Objects.requireNonNull(localDateTimeT, "localDateTime");
                Objects.requireNonNull(zoneOffsetY, "offset");
                Objects.requireNonNull(zoneId, "zone");
                if (!(zoneId instanceof ZoneOffset) || zoneOffsetY.equals(zoneId)) {
                    return new ZonedDateTime(localDateTimeT, zoneId, zoneOffsetY);
                }
                throw new IllegalArgumentException("ZoneId must match ZoneOffset");
            case 7:
                int i = u.d;
                return ZoneId.Q(objectInput.readUTF(), false);
            case 8:
                return ZoneOffset.Y(objectInput);
            case 9:
                int i2 = o.c;
                return new o(i.b0(objectInput), ZoneOffset.Y(objectInput));
            case 10:
                int i3 = OffsetDateTime.c;
                LocalDate localDate4 = LocalDate.d;
                return new OffsetDateTime(LocalDateTime.T(LocalDate.of(objectInput.readInt(), objectInput.readByte(), objectInput.readByte()), i.b0(objectInput)), ZoneOffset.Y(objectInput));
            case 11:
                int i4 = r.b;
                return r.Q(objectInput.readInt());
            case 12:
                int i5 = YearMonth.c;
                return YearMonth.of(objectInput.readInt(), objectInput.readByte());
            case 13:
                int i6 = m.c;
                byte b2 = objectInput.readByte();
                byte b3 = objectInput.readByte();
                k kVarT = k.T(b2);
                Objects.requireNonNull(kVarT, "month");
                j$.time.temporal.a.DAY_OF_MONTH.D(b3);
                if (b3 <= kVarT.S()) {
                    return new m(kVarT.getValue(), b3);
                }
                throw new b("Illegal value for DayOfMonth field, value " + ((int) b3) + " is not valid for month " + kVarT.name());
            case 14:
                Period period = Period.d;
                return Period.a(objectInput.readInt(), objectInput.readInt(), objectInput.readInt());
            default:
                throw new StreamCorruptedException("Unknown serialized type");
        }
    }

    private Object readResolve() {
        return this.b;
    }
}
