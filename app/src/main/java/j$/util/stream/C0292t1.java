package j$.util.stream;

import j$.util.Spliterator;

/* JADX INFO: renamed from: j$.util.stream.t1, reason: case insensitive filesystem */
public final class C0292t1 extends AbstractC0297u1 {
    public final Object[] h;

    public C0292t1(Spliterator spliterator, AbstractC0322z1 abstractC0322z1, Object[] objArr) {
        super(spliterator, abstractC0322z1, objArr.length);
        this.h = objArr;
    }

    public C0292t1(C0292t1 c0292t1, Spliterator spliterator, long j, long j2) {
        super(c0292t1, spliterator, j, j2, c0292t1.h.length);
        this.h = c0292t1.h;
    }

    @Override // j$.util.stream.AbstractC0297u1
    public final AbstractC0297u1 a(Spliterator spliterator, long j, long j2) {
        return new C0292t1(this, spliterator, j, j2);
    }

    @Override // java.util.function.Consumer
    /* JADX INFO: renamed from: accept */
    public final void v(Object obj) {
        int i = this.f;
        if (i >= this.g) {
            throw new IndexOutOfBoundsException(Integer.toString(this.f));
        }
        Object[] objArr = this.h;
        this.f = i + 1;
        objArr[i] = obj;
    }
}
