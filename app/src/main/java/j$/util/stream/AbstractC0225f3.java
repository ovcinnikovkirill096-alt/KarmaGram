package j$.util.stream;

import j$.util.Spliterator;
import java.util.Comparator;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/* JADX INFO: renamed from: j$.util.stream.f3, reason: case insensitive filesystem */
public abstract class AbstractC0225f3 implements Spliterator {
    public final boolean a;
    public final AbstractC0201b b;
    public Supplier c;
    public Spliterator d;
    public InterfaceC0279q2 e;
    public BooleanSupplier f;
    public long g;
    public AbstractC0211d h;
    public boolean i;

    public abstract void d();

    public abstract AbstractC0225f3 e(Spliterator spliterator);

    @Override // j$.util.Spliterator
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.com.android.tools.r8.a.q(this, i);
    }

    public AbstractC0225f3(AbstractC0201b abstractC0201b, Supplier supplier, boolean z) {
        this.b = abstractC0201b;
        this.c = supplier;
        this.d = null;
        this.a = z;
    }

    public AbstractC0225f3(AbstractC0201b abstractC0201b, Spliterator spliterator, boolean z) {
        this.b = abstractC0201b;
        this.c = null;
        this.d = spliterator;
        this.a = z;
    }

    public final void c() {
        if (this.d == null) {
            this.d = (Spliterator) this.c.get();
            this.c = null;
        }
    }

    public final boolean a() {
        AbstractC0211d abstractC0211d = this.h;
        if (abstractC0211d == null) {
            if (this.i) {
                return false;
            }
            c();
            d();
            this.g = 0L;
            this.e.h(this.d.getExactSizeIfKnown());
            return b();
        }
        long j = this.g + 1;
        this.g = j;
        boolean z = j < abstractC0211d.count();
        if (z) {
            return z;
        }
        this.g = 0L;
        this.h.clear();
        return b();
    }

    @Override // j$.util.Spliterator
    public Spliterator trySplit() {
        if (!this.a || this.h != null || this.i) {
            return null;
        }
        c();
        Spliterator spliteratorTrySplit = this.d.trySplit();
        if (spliteratorTrySplit == null) {
            return null;
        }
        return e(spliteratorTrySplit);
    }

    public final boolean b() {
        while (this.h.count() == 0) {
            if (this.e.m() || !this.f.getAsBoolean()) {
                if (this.i) {
                    return false;
                }
                this.e.end();
                this.i = true;
            }
        }
        return true;
    }

    @Override // j$.util.Spliterator
    public final long estimateSize() {
        c();
        return this.d.estimateSize();
    }

    @Override // j$.util.Spliterator
    public final long getExactSizeIfKnown() {
        c();
        if (EnumC0215d3.SIZED.n(this.b.m)) {
            return this.d.getExactSizeIfKnown();
        }
        return -1L;
    }

    @Override // j$.util.Spliterator
    public final int characteristics() {
        c();
        int i = this.b.m;
        int i2 = i & ((~i) >> 1) & EnumC0215d3.j & EnumC0215d3.f;
        return (i2 & 64) != 0 ? (i2 & (-16449)) | (this.d.characteristics() & 16448) : i2;
    }

    @Override // j$.util.Spliterator
    public final Comparator getComparator() {
        if (j$.com.android.tools.r8.a.q(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    public final String toString() {
        return String.format("%s[%s]", getClass().getName(), this.d);
    }
}
