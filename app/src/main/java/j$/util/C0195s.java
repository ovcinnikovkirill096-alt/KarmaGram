package j$.util;

import j$.util.stream.I3;
import j$.util.stream.Stream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

/* JADX INFO: renamed from: j$.util.s, reason: case insensitive filesystem */
public final class C0195s extends C0327v {
    private static final long serialVersionUID = 7854390611657943733L;

    @Override // j$.util.C0191n, java.lang.Iterable, j$.util.Collection, j$.lang.a
    public final void forEach(Consumer consumer) {
        Objects.requireNonNull(consumer);
        Collection.EL.a(this.a, new j$.time.t(1, consumer));
    }

    @Override // j$.util.C0191n, java.util.Collection, java.lang.Iterable, j$.util.Collection
    public final Spliterator spliterator() {
        return new r(Collection.EL.c(this.a));
    }

    @Override // j$.util.C0191n, java.util.Collection, j$.util.Collection
    public final Stream stream() {
        return I3.b(spliterator(), false);
    }

    @Override // j$.util.C0191n, java.util.Collection, j$.util.Collection
    public final Stream parallelStream() {
        return I3.b(spliterator(), true);
    }

    @Override // j$.util.C0191n, java.util.Collection, java.lang.Iterable
    public final Iterator iterator() {
        return new C0190m(this);
    }

    @Override // j$.util.C0191n, java.util.Collection
    public final Object[] toArray() {
        Object[] array = this.a.toArray();
        for (int i = 0; i < array.length; i++) {
            array[i] = new C0194q((java.util.Map.Entry) array[i]);
        }
        return array;
    }

    @Override // j$.util.C0191n, java.util.Collection
    public final Object[] toArray(Object[] objArr) {
        Object[] array = this.a.toArray(objArr.length == 0 ? objArr : Arrays.copyOf(objArr, 0));
        for (int i = 0; i < array.length; i++) {
            array[i] = new C0194q((java.util.Map.Entry) array[i]);
        }
        if (array.length > objArr.length) {
            return array;
        }
        System.arraycopy(array, 0, objArr, 0, array.length);
        if (objArr.length > array.length) {
            objArr[array.length] = null;
        }
        return objArr;
    }

    @Override // j$.util.C0191n, java.util.Collection
    public final boolean contains(Object obj) {
        if (obj instanceof java.util.Map.Entry) {
            return this.a.contains(new C0194q((java.util.Map.Entry) obj));
        }
        return false;
    }

    @Override // j$.util.C0191n, java.util.Collection
    public final boolean containsAll(java.util.Collection collection) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    @Override // j$.util.C0327v, java.util.Collection, java.util.Set
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof java.util.Set)) {
            return false;
        }
        java.util.Set set = (java.util.Set) obj;
        if (set.size() != this.a.size()) {
            return false;
        }
        return containsAll(set);
    }
}
