package j$.util.stream;

import j$.util.Collection;
import j$.util.List;
import j$.util.Objects;
import java.util.ArrayList;

public final class N2 extends F2 {
    public ArrayList d;

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public final void h(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.d = j >= 0 ? new ArrayList((int) j) : new ArrayList();
    }

    @Override // j$.util.stream.AbstractC0259m2, j$.util.stream.InterfaceC0279q2
    public final void end() {
        List.EL.sort(this.d, this.b);
        long size = this.d.size();
        InterfaceC0279q2 interfaceC0279q2 = this.a;
        interfaceC0279q2.h(size);
        if (!this.c) {
            ArrayList arrayList = this.d;
            Objects.requireNonNull(interfaceC0279q2);
            Collection.EL.a(arrayList, new j$.time.t(8, interfaceC0279q2));
        } else {
            ArrayList arrayList2 = this.d;
            int size2 = arrayList2.size();
            int i = 0;
            while (i < size2) {
                Object obj = arrayList2.get(i);
                i++;
                if (interfaceC0279q2.m()) {
                    break;
                } else {
                    interfaceC0279q2.v(obj);
                }
            }
        }
        interfaceC0279q2.end();
        this.d = null;
    }

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final void v(Object obj) {
        this.d.add(obj);
    }
}
