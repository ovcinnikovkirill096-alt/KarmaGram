package j$.util.stream;

/* JADX INFO: renamed from: j$.util.stream.h2, reason: case insensitive filesystem */
public abstract class AbstractC0234h2 extends AbstractC0239i2 {
    @Override // j$.util.stream.AbstractC0201b
    public final boolean D0() {
        return false;
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !EnumC0215d3.ORDERED.n(this.m) ? this : new C0214d2(this, EnumC0215d3.r);
    }
}
