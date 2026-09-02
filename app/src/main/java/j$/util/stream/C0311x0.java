package j$.util.stream;

import j$.util.Spliterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/* JADX INFO: renamed from: j$.util.stream.x0, reason: case insensitive filesystem */
public final class C0311x0 extends AbstractC0206c {
    public final j$.util.concurrent.s j;

    public C0311x0(j$.util.concurrent.s sVar, AbstractC0201b abstractC0201b, Spliterator spliterator) {
        super(abstractC0201b, spliterator);
        this.j = sVar;
    }

    public C0311x0(C0311x0 c0311x0, Spliterator spliterator) {
        super(c0311x0, spliterator);
        this.j = c0311x0.j;
    }

    @Override // j$.util.stream.AbstractC0216e
    public final AbstractC0216e c(Spliterator spliterator) {
        return new C0311x0(this, spliterator);
    }

    @Override // j$.util.stream.AbstractC0216e
    public final Object a() {
        AbstractC0322z1 abstractC0322z1 = this.a;
        AbstractC0301v0 abstractC0301v0 = (AbstractC0301v0) ((Supplier) this.j.c).get();
        abstractC0322z1.t0(this.b, abstractC0301v0);
        boolean z = abstractC0301v0.b;
        if (z == ((EnumC0306w0) this.j.b).b) {
            Boolean boolValueOf = Boolean.valueOf(z);
            AtomicReference atomicReference = this.h;
            while (!atomicReference.compareAndSet(null, boolValueOf) && atomicReference.get() == null) {
            }
        }
        return null;
    }

    @Override // j$.util.stream.AbstractC0206c
    public final Object h() {
        return Boolean.valueOf(!((EnumC0306w0) this.j.b).b);
    }
}
