package j$.util.stream;

import java.util.Arrays;

public final class G2 extends C2 {
    public U2 c;

    @Override // j$.util.stream.AbstractC0244j2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        U2 u2;
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        if (j <= 0) {
            u2 = new U2();
        } else {
            u2 = new U2((int) j);
        }
        this.c = u2;
    }

    @Override // j$.util.stream.AbstractC0244j2, j$.util.stream.InterfaceC0279q2
    public final void end() {
        double[] dArr = (double[]) this.c.b();
        Arrays.sort(dArr);
        long length = dArr.length;
        InterfaceC0279q2 interfaceC0279q2 = this.a;
        interfaceC0279q2.h(length);
        int i = 0;
        if (!this.b) {
            int length2 = dArr.length;
            while (i < length2) {
                interfaceC0279q2.accept(dArr[i]);
                i++;
            }
        } else {
            int length3 = dArr.length;
            while (i < length3) {
                double d = dArr[i];
                if (interfaceC0279q2.m()) {
                    break;
                }
                interfaceC0279q2.accept(d);
                i++;
            }
        }
        interfaceC0279q2.end();
    }

    @Override // j$.util.stream.InterfaceC0264n2, j$.util.stream.InterfaceC0279q2
    public final void accept(double d) {
        this.c.accept(d);
    }
}
