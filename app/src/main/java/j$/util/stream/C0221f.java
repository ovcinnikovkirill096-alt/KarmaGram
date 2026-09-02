package j$.util.stream;

import j$.util.Spliterator;
import java.util.Iterator;
import java.util.stream.DoubleStream;

/* JADX INFO: renamed from: j$.util.stream.f, reason: case insensitive filesystem */
public final /* synthetic */ class C0221f implements BaseStream {
    public final /* synthetic */ java.util.stream.BaseStream a;

    public /* synthetic */ C0221f(java.util.stream.BaseStream baseStream) {
        this.a = baseStream;
    }

    public static /* synthetic */ BaseStream f(java.util.stream.BaseStream baseStream) {
        if (baseStream == null) {
            return null;
        }
        if (baseStream instanceof C0226g) {
            return ((C0226g) baseStream).a;
        }
        if (baseStream instanceof DoubleStream) {
            return D.f((DoubleStream) baseStream);
        }
        if (baseStream instanceof java.util.stream.IntStream) {
            return IntStream.VivifiedWrapper.convert((java.util.stream.IntStream) baseStream);
        }
        if (baseStream instanceof java.util.stream.LongStream) {
            return C0262n0.f((java.util.stream.LongStream) baseStream);
        }
        return baseStream instanceof java.util.stream.Stream ? Stream.VivifiedWrapper.convert((java.util.stream.Stream) baseStream) : new C0221f(baseStream);
    }

    @Override // j$.util.stream.BaseStream, java.lang.AutoCloseable
    public final /* synthetic */ void close() {
        this.a.close();
    }

    public final /* synthetic */ boolean equals(Object obj) {
        java.util.stream.BaseStream baseStream = this.a;
        if (obj instanceof C0221f) {
            obj = ((C0221f) obj).a;
        }
        return baseStream.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream onClose(Runnable runnable) {
        return f(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ BaseStream parallel() {
        return f(this.a.parallel());
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ BaseStream sequential() {
        return f(this.a.sequential());
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ Spliterator spliterator() {
        return j$.util.d0.a(this.a.spliterator());
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream unordered() {
        return f(this.a.unordered());
    }
}
