package j$.time.temporal;

import j$.time.DayOfWeek;
import j$.util.Objects;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public final class x implements Serializable {
    public static final ConcurrentHashMap g = new ConcurrentHashMap(4, 0.75f, 2);
    public static final i h;
    private static final long serialVersionUID = -1177360819670808121L;
    public final DayOfWeek a;
    public final int b;
    public final transient w c;
    public final transient w d;
    public final transient w e;
    public final transient w f;

    static {
        new x(DayOfWeek.MONDAY, 4);
        a(DayOfWeek.SUNDAY, 1);
        h = j.d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static x a(DayOfWeek dayOfWeek, int i) {
        String str = dayOfWeek.toString() + i;
        ConcurrentHashMap concurrentHashMap = g;
        x xVar = (x) concurrentHashMap.get(str);
        if (xVar != null) {
            return xVar;
        }
        concurrentHashMap.putIfAbsent(str, new x(dayOfWeek, i));
        return (x) concurrentHashMap.get(str);
    }

    public x(DayOfWeek dayOfWeek, int i) {
        b bVar = b.DAYS;
        b bVar2 = b.WEEKS;
        this.c = new w("DayOfWeek", this, bVar, bVar2, w.f);
        this.d = new w("WeekOfMonth", this, bVar2, b.MONTHS, w.g);
        i iVar = j.d;
        this.e = new w("WeekOfWeekBasedYear", this, bVar2, iVar, w.i);
        this.f = new w("WeekBasedYear", this, iVar, b.FOREVER, a.YEAR.b);
        Objects.requireNonNull(dayOfWeek, "firstDayOfWeek");
        if (i < 1 || i > 7) {
            throw new IllegalArgumentException("Minimal number of days is invalid");
        }
        this.a = dayOfWeek;
        this.b = i;
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        if (this.a == null) {
            throw new InvalidObjectException("firstDayOfWeek is null");
        }
        int i = this.b;
        if (i < 1 || i > 7) {
            throw new InvalidObjectException("Minimal number of days is invalid");
        }
    }

    private Object readResolve() throws InvalidObjectException {
        try {
            return a(this.a, this.b);
        } catch (IllegalArgumentException e) {
            throw new InvalidObjectException("Invalid serialized WeekFields: " + e.getMessage());
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof x) && hashCode() == obj.hashCode();
    }

    public final int hashCode() {
        return (this.a.ordinal() * 7) + this.b;
    }

    public final String toString() {
        return "WeekFields[" + this.a + "," + this.b + "]";
    }
}
