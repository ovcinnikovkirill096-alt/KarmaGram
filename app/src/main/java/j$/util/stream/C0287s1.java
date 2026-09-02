package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.LongConsumer;

/* JADX INFO: renamed from: j$.util.stream.s1, reason: case insensitive filesystem */
public final class C0287s1 extends AbstractC0297u1 implements InterfaceC0274p2 {
    public final long[] h;

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final /* bridge */ /* synthetic */ void v(Object obj) {
        u((Long) obj);
    }

    public final /* synthetic */ LongConsumer andThen(LongConsumer longConsumer) {
        return j$.com.android.tools.r8.a.d(this, longConsumer);
    }

    @Override // j$.util.stream.InterfaceC0274p2
    public final /* synthetic */ void u(Long l) {
        AbstractC0322z1.E(this, l);
    }

    public C0287s1(Spliterator spliterator, AbstractC0322z1 abstractC0322z1, long[] jArr) {
        super(spliterator, abstractC0322z1, jArr.length);
        this.h = jArr;
    }

    public C0287s1(C0287s1 c0287s1, Spliterator spliterator, long j, long j2) {
        super(c0287s1, spliterator, j, j2, c0287s1.h.length);
        this.h = c0287s1.h;
    }

    @Override // j$.util.stream.AbstractC0297u1
    public final AbstractC0297u1 a(Spliterator spliterator, long j, long j2) {
        return new C0287s1(this, spliterator, j, j2);
    }

    @Override // j$.util.stream.AbstractC0297u1, j$.util.stream.InterfaceC0279q2, j$.util.stream.InterfaceC0274p2, java.util.function.LongConsumer
    public final void accept(long j) {
        int i = this.f;
        if (i >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        long[] jArr = this.h;
        this.f = i + 1;
        jArr[i] = j;
    }
}
