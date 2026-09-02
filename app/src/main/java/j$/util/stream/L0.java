package j$.util.stream;

public abstract class L0 implements J0 {
    public final J0 a;
    public final J0 b;
    public final long c;

    @Override // j$.util.stream.J0
    public final int i() {
        return 2;
    }

    public L0(J0 j0, J0 j1) {
        this.a = j0;
        this.b = j1;
        this.c = j1.count() + j0.count();
    }

    @Override // j$.util.stream.J0
    public final J0 a(int i) {
        if (i == 0) {
            return this.a;
        }
        if (i == 1) {
            return this.b;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.J0
    public final long count() {
        return this.c;
    }

    @Override // j$.util.stream.J0
    public /* bridge */ /* synthetic */ I0 a(int i) {
        return (I0) a(i);
    }
}
