package j$.util.stream;

import java.util.Arrays;

public final class I2 extends E2 {
    public Y2 c;

    @Override // j$.util.stream.AbstractC0254l2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        Y2 y2;
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        if (j <= 0) {
            y2 = new Y2();
        } else {
            y2 = new Y2((int) j);
        }
        this.c = y2;
    }

    @Override // j$.util.stream.AbstractC0254l2, j$.util.stream.InterfaceC0279q2
    public final void end() {
        long[] jArr = (long[]) this.c.b();
        Arrays.sort(jArr);
        long length = jArr.length;
        InterfaceC0279q2 interfaceC0279q2 = this.a;
        interfaceC0279q2.h(length);
        int i = 0;
        if (!this.b) {
            int length2 = jArr.length;
            while (i < length2) {
                interfaceC0279q2.accept(jArr[i]);
                i++;
            }
        } else {
            int length3 = jArr.length;
            while (i < length3) {
                long j = jArr[i];
                if (interfaceC0279q2.m()) {
                    break;
                }
                interfaceC0279q2.accept(j);
                i++;
            }
        }
        interfaceC0279q2.end();
    }

    @Override // j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final void accept(long j) {
        this.c.accept(j);
    }
}
