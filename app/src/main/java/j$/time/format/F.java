package j$.time.format;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
public final class F {
    public static final F ALWAYS;
    public static final F EXCEEDS_PAD;
    public static final F NEVER;
    public static final F NORMAL;
    public static final F NOT_NEGATIVE;
    public static final /* synthetic */ F[] a;

    public static F valueOf(String str) {
        return (F) Enum.valueOf(F.class, str);
    }

    public static F[] values() {
        return (F[]) a.clone();
    }

    static {
        F f = new F("NORMAL", 0);
        NORMAL = f;
        F f2 = new F("ALWAYS", 1);
        ALWAYS = f2;
        F f3 = new F("NEVER", 2);
        NEVER = f3;
        F f4 = new F("NOT_NEGATIVE", 3);
        NOT_NEGATIVE = f4;
        F f5 = new F("EXCEEDS_PAD", 4);
        EXCEEDS_PAD = f5;
        a = new F[]{f, f2, f3, f4, f5};
    }
}
