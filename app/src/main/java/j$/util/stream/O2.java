package j$.util.stream;

import java.util.Arrays;

public final class O2 extends C2 {
    public double[] c;
    public int d;

    @Override // j$.util.stream.AbstractC0244j2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = new double[(int) j];
    }

    @Override // j$.util.stream.AbstractC0244j2, j$.util.stream.InterfaceC0279q2
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

    @Override // j$.util.stream.InterfaceC0264n2, j$.util.stream.InterfaceC0279q2
    public final void accept(double d) {
        double[] dArr = this.c;
        int i = this.d;
        this.d = i + 1;
        dArr[i] = d;
    }
}
