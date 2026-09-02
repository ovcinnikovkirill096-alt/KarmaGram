package j$.util.stream;

import j$.util.Spliterator;

public final class G1 extends AbstractC0322z1 {
    public final /* synthetic */ int h;

    public /* synthetic */ G1(int i) {
        this.h = i;
    }

    @Override // j$.util.stream.AbstractC0322z1
    public final U1 s0() {
        switch (this.h) {
            case 0:
                return new Y1();
            case 1:
                return new W1();
            case 2:
                return new Z1();
            default:
                return new X1();
        }
    }

    @Override // j$.util.stream.AbstractC0322z1, j$.util.stream.M3
    public final Object f(AbstractC0201b abstractC0201b, Spliterator spliterator) {
        switch (this.h) {
            case 0:
                return EnumC0215d3.SIZED.n(abstractC0201b.m) ? Long.valueOf(spliterator.getExactSizeIfKnown()) : (Long) super.f(abstractC0201b, spliterator);
            case 1:
                return EnumC0215d3.SIZED.n(abstractC0201b.m) ? Long.valueOf(spliterator.getExactSizeIfKnown()) : (Long) super.f(abstractC0201b, spliterator);
            case 2:
                return EnumC0215d3.SIZED.n(abstractC0201b.m) ? Long.valueOf(spliterator.getExactSizeIfKnown()) : (Long) super.f(abstractC0201b, spliterator);
            default:
                return EnumC0215d3.SIZED.n(abstractC0201b.m) ? Long.valueOf(spliterator.getExactSizeIfKnown()) : (Long) super.f(abstractC0201b, spliterator);
        }
    }

    @Override // j$.util.stream.AbstractC0322z1, j$.util.stream.M3
    public final Object i(AbstractC0322z1 abstractC0322z1, Spliterator spliterator) {
        switch (this.h) {
            case 0:
                return EnumC0215d3.SIZED.n(((AbstractC0201b) abstractC0322z1).m) ? Long.valueOf(spliterator.getExactSizeIfKnown()) : (Long) super.i(abstractC0322z1, spliterator);
            case 1:
                return EnumC0215d3.SIZED.n(((AbstractC0201b) abstractC0322z1).m) ? Long.valueOf(spliterator.getExactSizeIfKnown()) : (Long) super.i(abstractC0322z1, spliterator);
            case 2:
                return EnumC0215d3.SIZED.n(((AbstractC0201b) abstractC0322z1).m) ? Long.valueOf(spliterator.getExactSizeIfKnown()) : (Long) super.i(abstractC0322z1, spliterator);
            default:
                return EnumC0215d3.SIZED.n(((AbstractC0201b) abstractC0322z1).m) ? Long.valueOf(spliterator.getExactSizeIfKnown()) : (Long) super.i(abstractC0322z1, spliterator);
        }
    }

    @Override // j$.util.stream.AbstractC0322z1, j$.util.stream.M3
    public final int s() {
        switch (this.h) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
        return EnumC0215d3.r;
    }
}
