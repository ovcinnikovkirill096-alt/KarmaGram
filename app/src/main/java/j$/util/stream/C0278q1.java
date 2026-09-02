package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.DoubleConsumer;

/* JADX INFO: renamed from: j$.util.stream.q1, reason: case insensitive filesystem */
public final class C0278q1 extends AbstractC0297u1 implements InterfaceC0264n2 {
    public final double[] h;

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final /* bridge */ /* synthetic */ void v(Object obj) {
        v((Double) obj);
    }

    public final /* synthetic */ DoubleConsumer andThen(DoubleConsumer doubleConsumer) {
        return j$.com.android.tools.r8.a.b(this, doubleConsumer);
    }

    @Override // j$.util.stream.InterfaceC0264n2
    public final /* synthetic */ void v(Double d) {
        AbstractC0322z1.A(this, d);
    }

    public C0278q1(Spliterator spliterator, AbstractC0322z1 abstractC0322z1, double[] dArr) {
        super(spliterator, abstractC0322z1, dArr.length);
        this.h = dArr;
    }

    public C0278q1(C0278q1 c0278q1, Spliterator spliterator, long j, long j2) {
        super(c0278q1, spliterator, j, j2, c0278q1.h.length);
        this.h = c0278q1.h;
    }

    @Override // j$.util.stream.AbstractC0297u1
    public final AbstractC0297u1 a(Spliterator spliterator, long j, long j2) {
        return new C0278q1(this, spliterator, j, j2);
    }

    @Override // j$.util.stream.AbstractC0297u1, j$.util.stream.InterfaceC0279q2
    public final void accept(double d) {
        int i = this.f;
        if (i >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        double[] dArr = this.h;
        this.f = i + 1;
        dArr[i] = d;
    }
}
