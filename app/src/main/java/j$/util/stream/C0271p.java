package j$.util.stream;

import j$.util.Objects;
import j$.util.Spliterator;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntFunction;

/* JADX INFO: renamed from: j$.util.stream.p, reason: case insensitive filesystem */
public final class C0271p extends AbstractC0229g2 {
    public static N0 I0(AbstractC0322z1 abstractC0322z1, Spliterator spliterator) {
        C0246k c0246k = new C0246k(6);
        j$.time.e eVar = new j$.time.e(25);
        j$.time.e eVar2 = new j$.time.e(26);
        Objects.requireNonNull(c0246k);
        Objects.requireNonNull(eVar);
        Objects.requireNonNull(eVar2);
        return new N0((Collection) new E1(EnumC0220e3.REFERENCE, eVar2, eVar, c0246k, 3).i(abstractC0322z1, spliterator));
    }

    @Override // j$.util.stream.AbstractC0201b
    public final J0 B0(AbstractC0322z1 abstractC0322z1, Spliterator spliterator, IntFunction intFunction) {
        AbstractC0201b abstractC0201b = (AbstractC0201b) abstractC0322z1;
        if (EnumC0215d3.DISTINCT.n(abstractC0201b.m)) {
            return abstractC0322z1.d0(spliterator, false, intFunction);
        }
        if (EnumC0215d3.ORDERED.n(abstractC0201b.m)) {
            return I0(abstractC0322z1, spliterator);
        }
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        j$.util.concurrent.s sVar = new j$.util.concurrent.s(4, atomicBoolean, concurrentHashMap);
        Objects.requireNonNull(sVar);
        new S(sVar, false).a(abstractC0322z1, spliterator);
        Collection collectionKeySet = concurrentHashMap.keySet();
        if (atomicBoolean.get()) {
            HashSet hashSet = new HashSet(collectionKeySet);
            hashSet.add(null);
            collectionKeySet = hashSet;
        }
        return new N0(collectionKeySet);
    }

    @Override // j$.util.stream.AbstractC0201b
    public final Spliterator C0(AbstractC0201b abstractC0201b, Spliterator spliterator) {
        if (EnumC0215d3.DISTINCT.n(abstractC0201b.m)) {
            return abstractC0201b.v0(spliterator);
        }
        if (EnumC0215d3.ORDERED.n(abstractC0201b.m)) {
            return I0(abstractC0201b, spliterator).spliterator();
        }
        return new C0260m3(abstractC0201b.v0(spliterator), new ConcurrentHashMap());
    }

    @Override // j$.util.stream.AbstractC0201b
    public final InterfaceC0279q2 E0(int i, InterfaceC0279q2 interfaceC0279q2) {
        Objects.requireNonNull(interfaceC0279q2);
        if (EnumC0215d3.DISTINCT.n(i)) {
            return interfaceC0279q2;
        }
        if (EnumC0215d3.SORTED.n(i)) {
            return new C0261n(interfaceC0279q2);
        }
        return new C0266o(interfaceC0279q2);
    }
}
