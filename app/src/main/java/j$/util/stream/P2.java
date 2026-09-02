package j$.util.stream;

import java.util.Arrays;

public final class P2 extends D2 {
    public int[] c;
    public int d;

    @Override // j$.util.stream.AbstractC0249k2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = new int[(int) j];
    }

    @Override // j$.util.stream.AbstractC0249k2, j$.util.stream.InterfaceC0279q2
    public final void end() {
        int i = 0;
        Arrays.sort(this.c, 0, this.d);
        long j = this.d;
        InterfaceC0279q2 interfaceC0279q2 = this.a;
        interfaceC0279q2.h(j);
        if (!this.b) {
            while (i < this.d) {
                interfaceC0279q2.accept(this.c[i]);
                i++;
            }
        } else {
            while (i < this.d && !interfaceC0279q2.m()) {
                interfaceC0279q2.accept(this.c[i]);
                i++;
            }
        }
        interfaceC0279q2.end();
        this.c = null;
    }

    @Override // j$.util.stream.InterfaceC0269o2, j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        int[] iArr = this.c;
        int i2 = this.d;
        this.d = i2 + 1;
        iArr[i2] = i;
    }
}
