package j$.util;

public final class C {
    public static final C c = new C();
    public final boolean a;
    public final long b;

    public C() {
        this.a = false;
        this.b = 0L;
    }

    public C(long j) {
        this.a = true;
        this.b = j;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof C)) {
            return false;
        }
        C c2 = (C) obj;
        boolean z = c2.a;
        boolean z2 = this.a;
        if (z2 && z) {
            return this.b == c2.b;
        }
        return z2 == z;
    }

    public final int hashCode() {
        if (!this.a) {
            return 0;
        }
        long j = this.b;
        return (int) (j ^ (j >>> 32));
    }

    public final String toString() {
        if (this.a) {
            return "OptionalLong[" + this.b + "]";
        }
        return "OptionalLong.empty";
    }
}
