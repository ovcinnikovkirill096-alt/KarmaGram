package j$.util.stream;

import java.util.function.IntFunction;

public abstract class T0 extends L0 implements I0 {
    @Override // j$.util.stream.J0
    public final /* synthetic */ Object[] g(IntFunction intFunction) {
        return AbstractC0322z1.I(this, intFunction);
    }

    @Override // j$.util.stream.I0
    public final void d(Object obj) {
        ((I0) this.a).d(obj);
        ((I0) this.b).d(obj);
    }

    @Override // j$.util.stream.I0
    public final void c(int i, Object obj) {
        J0 j0 = this.a;
        ((I0) j0).c(i, obj);
        ((I0) this.b).c(i + ((int) ((I0) j0).count()), obj);
    }

    @Override // j$.util.stream.I0
    public final Object b() {
        long j = this.c;
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        Object objNewArray = newArray((int) j);
        c(0, objNewArray);
        return objNewArray;
    }

    public final String toString() {
        long j = this.c;
        return j < 32 ? String.format("%s[%s.%s]", getClass().getName(), this.a, this.b) : String.format("%s[size=%d]", getClass().getName(), Long.valueOf(j));
    }
}
