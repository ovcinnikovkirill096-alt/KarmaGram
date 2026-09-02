package j$.util;

/* JADX INFO: renamed from: j$.util.g, reason: case insensitive filesystem */
public final class C0184g extends RuntimeException {
    public static void a(Object obj, String str) {
        throw new C0184g("Unsupported " + str + " :" + obj);
    }
}
