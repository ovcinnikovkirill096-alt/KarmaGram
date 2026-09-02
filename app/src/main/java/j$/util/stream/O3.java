package j$.util.stream;

import java.security.AccessController;

public abstract class O3 {
    public static final boolean a = ((Boolean) AccessController.doPrivileged(new j$.util.s0(2))).booleanValue();

    public static void a(Class cls, String str) {
        throw new UnsupportedOperationException(cls + " tripwire tripped but logging not supported: " + str);
    }
}
