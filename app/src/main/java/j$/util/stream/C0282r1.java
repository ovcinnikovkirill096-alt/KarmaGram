package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.IntConsumer;

/* JADX INFO: renamed from: j$.util.stream.r1, reason: case insensitive filesystem */
public final class C0282r1 extends AbstractC0297u1 implements InterfaceC0269o2 {
    public final int[] h;

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final /* bridge */ /* synthetic */ void v(Object obj) {
        l((Integer) obj);
    }

    public final /* synthetic */ IntConsumer andThen(IntConsumer intConsumer) {
        return j$.com.android.tools.r8.a.c(this, intConsumer);
    }

    @Override // j$.util.stream.InterfaceC0269o2
    public final /* synthetic */ void l(Integer num) {
        AbstractC0322z1.C(this, num);
    }

    public C0282r1(Spliterator spliterator, AbstractC0322z1 abstractC0322z1, int[] iArr) {
        super(spliterator, abstractC0322z1, iArr.length);
        this.h = iArr;
    }

    public C0282r1(C0282r1 c0282r1, Spliterator spliterator, long j, long j2) {
        super(c0282r1, spliterator, j, j2, c0282r1.h.length);
        this.h = c0282r1.h;
    }

    @Override // j$.util.stream.AbstractC0297u1
    public final AbstractC0297u1 a(Spliterator spliterator, long j, long j2) {
        return new C0282r1(this, spliterator, j, j2);
    }

    @Override // j$.util.stream.AbstractC0297u1, j$.util.stream.InterfaceC0279q2
    public final void accept(int i) {
        int i2 = this.f;
        if (i2 >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        int[] iArr = this.h;
        this.f = i2 + 1;
        iArr[i2] = i;
    }
}
