package j$.util;

public final class B {
    public static final B c = new B();
    public final boolean a;
    public final double b;

    public B() {
        this.a = false;
        this.b = Double.NaN;
    }

    public B(double d) {
        this.a = true;
        this.b = d;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof B)) {
            return false;
        }
        B b = (B) obj;
        boolean z = b.a;
        boolean z2 = this.a;
        if (z2 && z) {
            return Double.compare(this.b, b.b) == 0;
        }
        return z2 == z;
    }

    public final int hashCode() {
        if (!this.a) {
            return 0;
        }
        long jDoubleToLongBits = Double.doubleToLongBits(this.b);
        return (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
    }

    public final String toString() {
        if (this.a) {
            return "OptionalDouble[" + this.b + "]";
        }
        return "OptionalDouble.empty";
    }
}
