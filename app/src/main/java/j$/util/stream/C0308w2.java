package j$.util.stream;

import j$.util.Spliterator;
import java.util.function.IntFunction;

/* JADX INFO: renamed from: j$.util.stream.w2, reason: case insensitive filesystem */
public final class C0308w2 extends AbstractC0247k0 {
    public final /* synthetic */ long s;
    public final /* synthetic */ long t;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0308w2(AbstractC0257m0 abstractC0257m0, int i, long j, long j2) {
        super(abstractC0257m0, i);
        this.s = j;
        this.t = j2;
    }

    @Override // j$.util.stream.AbstractC0201b
    public final Spliterator C0(AbstractC0201b abstractC0201b, Spliterator spliterator) {
        long jE0 = abstractC0201b.e0(spliterator);
        if (jE0 > 0 && spliterator.hasCharacteristics(16384)) {
            j$.util.Z z = (j$.util.Z) abstractC0201b.v0(spliterator);
            long j = this.s;
            return new C0304v3(z, j, B2.c(j, this.t));
        }
        if (EnumC0215d3.ORDERED.n(abstractC0201b.m)) {
            return ((J0) new A2(this, abstractC0201b, spliterator, new C0217e0(14), this.s, this.t).invoke()).spliterator();
        }
        j$.util.Z z2 = (j$.util.Z) abstractC0201b.v0(spliterator);
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
        return new B3(z2, j2, j3);
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
            return AbstractC0322z1.X(abstractC0322z1, B2.b(abstractC0201b.A0(), spliterator, this.s, this.t), true);
        }
        if (!EnumC0215d3.ORDERED.n(((AbstractC0201b) abstractC0322z1).m)) {
            j$.util.Z z = (j$.util.Z) abstractC0322z1.v0(spliterator);
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
            return AbstractC0322z1.X(this, new B3(z, j, jMin), true);
        }
        return (J0) new A2(this, abstractC0322z1, spliterator, intFunction, this.s, this.t).invoke();
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        return new C0303v2(this, interfaceC0279q2);
    }
}
