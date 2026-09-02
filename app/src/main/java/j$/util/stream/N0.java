package j$.util.stream;

import j$.util.Spliterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public final class N0 implements J0 {
    public final Collection a;

    @Override // j$.util.stream.J0
    public final /* synthetic */ J0 e(long j, long j2, IntFunction intFunction) {
        return AbstractC0322z1.S(this, j, j2, intFunction);
    }

    @Override // j$.util.stream.J0
    public final /* synthetic */ int i() {
        return 0;
    }

    @Override // j$.util.stream.J0
    public final J0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    public N0(Collection collection) {
        this.a = collection;
    }

    @Override // j$.util.stream.J0
    public final Spliterator spliterator() {
        return j$.util.Collection.EL.stream(this.a).spliterator();
    }

    @Override // j$.util.stream.J0
    public final void f(Object[] objArr, int i) {
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            objArr[i] = it.next();
            i++;
        }
    }

    @Override // j$.util.stream.J0
    public final Object[] g(IntFunction intFunction) {
        Collection collection = this.a;
        return collection.toArray((Object[]) intFunction.apply(collection.size()));
    }

    @Override // j$.util.stream.J0
    public final long count() {
        return this.a.size();
    }

    @Override // j$.util.stream.J0
    public final void forEach(Consumer consumer) {
        j$.util.Collection.EL.a(this.a, consumer);
    }

    public final String toString() {
        return String.format("CollectionNode[%d][%s]", Integer.valueOf(this.a.size()), this.a);
    }
}
