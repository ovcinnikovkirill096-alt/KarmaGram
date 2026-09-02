package j$.time.chrono;

import j$.time.LocalDate;
import j$.time.ZoneId;
import j$.time.ZoneOffset;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StreamCorruptedException;

public final class D implements Externalizable {
    private static final long serialVersionUID = -6103370247208168577L;
    public byte a;
    public Object b;

    public D() {
    }

    public D(byte b, Object obj) {
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
                objectOutput.writeUTF(((AbstractC0162a) obj).getId());
                return;
            case 2:
                C0167f c0167f = (C0167f) obj;
                objectOutput.writeObject(c0167f.a);
                objectOutput.writeObject(c0167f.b);
                return;
            case 3:
                j jVar = (j) obj;
                objectOutput.writeObject(jVar.a);
                objectOutput.writeObject(jVar.b);
                objectOutput.writeObject(jVar.c);
                return;
            case 4:
                w wVar = (w) obj;
                wVar.getClass();
                objectOutput.writeInt(j$.time.temporal.s.a(wVar, j$.time.temporal.a.YEAR));
                objectOutput.writeByte(j$.time.temporal.s.a(wVar, j$.time.temporal.a.MONTH_OF_YEAR));
                objectOutput.writeByte(j$.time.temporal.s.a(wVar, j$.time.temporal.a.DAY_OF_MONTH));
                return;
            case 5:
                objectOutput.writeByte(((x) obj).a);
                return;
            case 6:
                p pVar = (p) obj;
                objectOutput.writeObject(pVar.a);
                objectOutput.writeInt(j$.time.temporal.s.a(pVar, j$.time.temporal.a.YEAR));
                objectOutput.writeByte(j$.time.temporal.s.a(pVar, j$.time.temporal.a.MONTH_OF_YEAR));
                objectOutput.writeByte(j$.time.temporal.s.a(pVar, j$.time.temporal.a.DAY_OF_MONTH));
                return;
            case 7:
                B b2 = (B) obj;
                b2.getClass();
                objectOutput.writeInt(j$.time.temporal.s.a(b2, j$.time.temporal.a.YEAR));
                objectOutput.writeByte(j$.time.temporal.s.a(b2, j$.time.temporal.a.MONTH_OF_YEAR));
                objectOutput.writeByte(j$.time.temporal.s.a(b2, j$.time.temporal.a.DAY_OF_MONTH));
                return;
            case 8:
                H h = (H) obj;
                h.getClass();
                objectOutput.writeInt(j$.time.temporal.s.a(h, j$.time.temporal.a.YEAR));
                objectOutput.writeByte(j$.time.temporal.s.a(h, j$.time.temporal.a.MONTH_OF_YEAR));
                objectOutput.writeByte(j$.time.temporal.s.a(h, j$.time.temporal.a.DAY_OF_MONTH));
                return;
            case 9:
                C0168g c0168g = (C0168g) obj;
                objectOutput.writeUTF(c0168g.a.getId());
                objectOutput.writeInt(c0168g.b);
                objectOutput.writeInt(c0168g.c);
                objectOutput.writeInt(c0168g.d);
                return;
            default:
                throw new InvalidClassException("Unknown serialized type");
        }
    }

    @Override // java.io.Externalizable
    public final void readExternal(ObjectInput objectInput) throws IOException {
        Object objY;
        byte b = objectInput.readByte();
        this.a = b;
        switch (b) {
            case 1:
                ConcurrentHashMap concurrentHashMap = AbstractC0162a.a;
                objY = j$.com.android.tools.r8.a.Y(objectInput.readUTF());
                break;
            case 2:
                objY = ((InterfaceC0163b) objectInput.readObject()).F((j$.time.i) objectInput.readObject());
                break;
            case 3:
                objY = ((ChronoLocalDateTime) objectInput.readObject()).z((ZoneOffset) objectInput.readObject()).w((ZoneId) objectInput.readObject());
                break;
            case 4:
                LocalDate localDate = w.d;
                int i = objectInput.readInt();
                byte b2 = objectInput.readByte();
                byte b3 = objectInput.readByte();
                u.c.getClass();
                objY = new w(LocalDate.of(i, b2, b3));
                break;
            case 5:
                x xVar = x.d;
                objY = x.m(objectInput.readByte());
                break;
            case 6:
                n nVar = (n) objectInput.readObject();
                int i2 = objectInput.readInt();
                byte b4 = objectInput.readByte();
                byte b5 = objectInput.readByte();
                nVar.getClass();
                objY = new p(nVar, i2, b4, b5);
                break;
            case 7:
                int i3 = objectInput.readInt();
                byte b6 = objectInput.readByte();
                byte b7 = objectInput.readByte();
                z.c.getClass();
                objY = new B(LocalDate.of(i3 + 1911, b6, b7));
                break;
            case 8:
                int i4 = objectInput.readInt();
                byte b8 = objectInput.readByte();
                byte b9 = objectInput.readByte();
                F.c.getClass();
                objY = new H(LocalDate.of(i4 - 543, b8, b9));
                break;
            case 9:
                int i5 = C0168g.e;
                objY = new C0168g(j$.com.android.tools.r8.a.Y(objectInput.readUTF()), objectInput.readInt(), objectInput.readInt(), objectInput.readInt());
                break;
            default:
                throw new StreamCorruptedException("Unknown serialized type");
        }
        this.b = objY;
    }

    private Object readResolve() {
        return this.b;
    }
}
