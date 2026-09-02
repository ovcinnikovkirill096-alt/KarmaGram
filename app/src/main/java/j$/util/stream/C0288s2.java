package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.IntFunction;

/* JADX INFO: renamed from: j$.util.stream.s2, reason: case insensitive filesystem */
public final class C0288s2 extends AbstractC0229g2 {
    public final /* synthetic */ long s;
    public final /* synthetic */ long t;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0288s2(AbstractC0239i2 abstractC0239i2, int i, long j, long j2) {
        super(abstractC0239i2, i);
        this.s = j;
        this.t = j2;
    }

    @Override // j$.util.stream.AbstractC0201b
    public final Spliterator C0(AbstractC0201b abstractC0201b, Spliterator spliterator) {
        long jE0 = abstractC0201b.e0(spliterator);
        if (jE0 > 0 && spliterator.hasCharacteristics(16384)) {
            Spliterator spliteratorV0 = abstractC0201b.v0(spliterator);
            long j = this.s;
            return new C0314x3(spliteratorV0, j, B2.c(j, this.t));
        }
        if (EnumC0215d3.ORDERED.n(abstractC0201b.m)) {
            return ((J0) new A2(this, abstractC0201b, spliterator, new C0217e0(4), this.s, this.t).invoke()).spliterator();
        }
        Spliterator spliteratorV1 = abstractC0201b.v0(spliterator);
        long j2 = this.s;
        long j3 = this.t;
        if (j2 <= jE0) {
            long jMin = jE0 - j2;
            if (j3 >= 0) {
                jMin = Math.min(j3, jMin);
            }
            j3 = jMin;
            j2 = 0;
        }
        return new D3(spliteratorV1, j2, j3);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final J0 B0(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, IntFunction intFunction) {
        long jMin;
        long j;
        long jE0 = abstractC0322z1.e0(spliterator);
        if (jE0 > 0 && spliterator.hasCharacteristics(16384)) {
            AbstractC0201b abstractC0201b = (AbstractC0201b) abstractC0322z1;
            while (abstractC0201b.l > 0) {
                abstractC0201b = abstractC0201b.i;
            }
            return AbstractC0322z1.U(abstractC0322z1, B2.b(abstractC0201b.A0(), spliterator, this.s, this.t), true, intFunction);
        }
        if (!EnumC0215d3.ORDERED.n(((AbstractC0201b) abstractC0322z1).m)) {
            Spliterator spliteratorV0 = abstractC0322z1.v0(spliterator);
            long j2 = this.s;
            long j3 = this.t;
            if (j2 <= jE0) {
                long j4 = jE0 - j2;
                jMin = j3 >= 0 ? Math.min(j3, j4) : j4;
                j = 0;
            } else {
                jMin = j3;
                j = j2;
            }
            return AbstractC0322z1.U(this, new D3(spliteratorV0, j, jMin), true, intFunction);
        }
        return (J0) new A2(this, abstractC0322z1, spliterator, intFunction, this.s, this.t).invoke();
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        return new C0283r2(this, interfaceC0279q2);
    }
}
