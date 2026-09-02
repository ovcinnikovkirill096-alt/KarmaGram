package j$.util.stream;

import java.util.Arrays;

public final class R2 extends F2 {
    public Object[] d;
    public int e;

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.d = new Object[(int) j];
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public final void end() {
        int i = 0;
        Arrays.sort(this.d, 0, this.e, this.b);
        long j = this.e;
        InterfaceC0279q2 interfaceC0279q2 = this.a;
        interfaceC0279q2.h(j);
        if (!this.c) {
            while (i < this.e) {
                interfaceC0279q2.accept(this.d[i]);
                i++;
            }
        } else {
            while (i < this.e && !interfaceC0279q2.m()) {
                interfaceC0279q2.accept(this.d[i]);
                i++;
            }
        }
        interfaceC0279q2.end();
        this.d = null;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        Object[] objArr = this.d;
        int i = this.e;
        this.e = i + 1;
        objArr[i] = obj;
    }
}
