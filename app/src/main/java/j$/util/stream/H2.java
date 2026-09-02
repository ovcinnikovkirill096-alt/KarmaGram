package j$.util.stream;

import java.util.Arrays;

public final class H2 extends D2 {
    public W2 c;

    @Override // j$.util.stream.AbstractC0249k2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        W2 w2;
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        if (j <= 0) {
            w2 = new W2();
        } else {
            w2 = new W2((int) j);
        }
        this.c = w2;
    }

    @Override // j$.util.stream.AbstractC0249k2, j$.util.stream.InterfaceC0279q2
    public final void end() {
        int[] iArr = (int[]) this.c.b();
        Arrays.sort(iArr);
        long length = iArr.length;
        InterfaceC0279q2 interfaceC0279q2 = this.a;
        interfaceC0279q2.h(length);
        int i = 0;
        if (!this.b) {
            int length2 = iArr.length;
            while (i < length2) {
                interfaceC0279q2.accept(iArr[i]);
                i++;
            }
        } else {
            int length3 = iArr.length;
            while (i < length3) {
                int i2 = iArr[i];
                if (interfaceC0279q2.m()) {
                    break;
                }
                interfaceC0279q2.accept(i2);
                i++;
            }
        }
        interfaceC0279q2.end();
    }

    @Override // j$.util.stream.InterfaceC0269o2, j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        this.c.accept(i);
    }
}
