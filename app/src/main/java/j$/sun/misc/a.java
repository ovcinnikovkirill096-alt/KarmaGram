package j$.sun.misc;

import j$.util.concurrent.k;
import j$.util.concurrent.p;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import sun.misc.Unsafe;

public final class a {
    public static final a b;
    public final Unsafe a;

    static {
        Field fieldG = g();
        fieldG.setAccessible(true);
        try {
            b = new a((Unsafe) fieldG.get(null));
        } catch (IllegalAccessException e) {
            throw new AssertionError("Couldn't get the Unsafe", e);
        }
    }

    public a(Unsafe unsafe) {
        this.a = unsafe;
    }

    public static Field g() {
        try {
            return Unsafe.class.getDeclaredField("theUnsafe");
        } catch (NoSuchFieldException e) {
            for (Field field : Unsafe.class.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Unsafe.class.isAssignableFrom(field.getType())) {
                    return field;
                }
            }
            throw new AssertionError("Couldn't find the Unsafe", e);
        }
    }

    public final int e(p pVar, long j) {
        while (true) {
            int intVolatile = this.a.getIntVolatile(pVar, j);
            p pVar2 = pVar;
            long j2 = j;
            if (this.a.compareAndSwapInt(pVar2, j2, intVolatile, intVolatile - 4)) {
                return intVolatile;
            }
            pVar = pVar2;
            j = j2;
        }
    }

    public final long i(Field field) {
        return this.a.objectFieldOffset(field);
    }

    public final long h(Class cls, String str) {
        try {
            return i(cls.getDeclaredField(str));
        } catch (NoSuchFieldException e) {
            throw new AssertionError("Cannot find field:", e);
        }
    }

    public final int a(Class cls) {
        return this.a.arrayBaseOffset(cls);
    }

    public final int b(Class cls) {
        return this.a.arrayIndexScale(cls);
    }

    public final Object f(Object obj, long j) {
        return this.a.getObjectVolatile(obj, j);
    }

    public final void j(Object obj, long j, k kVar) {
        this.a.putObjectVolatile(obj, j, kVar);
    }

    public final boolean c(Object obj, long j, int i, int i2) {
        return this.a.compareAndSwapInt(obj, j, i, i2);
    }

    public final boolean d(Object obj, long j, long j2, long j3) {
        return this.a.compareAndSwapLong(obj, j, j2, j3);
    }
}
