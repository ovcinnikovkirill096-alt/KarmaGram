package j$.util.concurrent;

import j$.util.stream.Stream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import org.mvel2.asm.signature.SignatureVisitor;
import org.telegram.tgnet.TLObject;

public class ConcurrentHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable, t {
    public static final int g = Runtime.getRuntime().availableProcessors();
    public static final j$.sun.misc.a h;
    public static final long i;
    public static final long j;
    public static final long k;
    public static final long l;
    public static final long m;
    public static final int n;
    public static final int o;
    private static final ObjectStreamField[] serialPersistentFields;
    private static final long serialVersionUID = 7249069246763182397L;
    public volatile transient k[] a;
    public volatile transient k[] b;
    private volatile transient long baseCount;
    public volatile transient c[] c;
    private volatile transient int cellsBusy;
    public transient KeySetView d;
    public transient r e;
    public transient e f;
    private volatile transient int sizeCtl;
    private volatile transient int transferIndex;

    public static final int i(int i2) {
        return (i2 ^ (i2 >>> 16)) & Integer.MAX_VALUE;
    }

    static {
        Class cls = Integer.TYPE;
        serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("segments", m[].class), new ObjectStreamField("segmentMask", cls), new ObjectStreamField("segmentShift", cls)};
        j$.sun.misc.a aVar = j$.sun.misc.a.b;
        h = aVar;
        i = aVar.h(ConcurrentHashMap.class, "sizeCtl");
        j = aVar.h(ConcurrentHashMap.class, "transferIndex");
        k = aVar.h(ConcurrentHashMap.class, "baseCount");
        l = aVar.h(ConcurrentHashMap.class, "cellsBusy");
        m = aVar.h(c.class, "value");
        n = aVar.a(k[].class);
        int iB = aVar.b(k[].class);
        if (((iB - 1) & iB) != 0) {
            throw new ExceptionInInitializerError("array index scale not a power of two");
        }
        o = 31 - Integer.numberOfLeadingZeros(iB);
    }

    public static final int l(int i2) {
        int iNumberOfLeadingZeros = (-1) >>> Integer.numberOfLeadingZeros(i2 - 1);
        if (iNumberOfLeadingZeros < 0) {
            return 1;
        }
        return iNumberOfLeadingZeros >= 1073741824 ? TLObject.FLAG_30 : iNumberOfLeadingZeros + 1;
    }

    public static Class c(Object obj) {
        Type[] actualTypeArguments;
        if (!(obj instanceof Comparable)) {
            return null;
        }
        Class<?> cls = obj.getClass();
        if (cls != String.class) {
            Type[] genericInterfaces = cls.getGenericInterfaces();
            if (genericInterfaces == null) {
                return null;
            }
            for (Type type : genericInterfaces) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    if (parameterizedType.getRawType() != Comparable.class || (actualTypeArguments = parameterizedType.getActualTypeArguments()) == null || actualTypeArguments.length != 1 || actualTypeArguments[0] != cls) {
                    }
                }
            }
            return null;
        }
        return cls;
    }

    public static final k k(k[] kVarArr, int i2) {
        return (k) h.f(kVarArr, (((long) i2) << o) + ((long) n));
    }

    public static final boolean b(k[] kVarArr, int i2, k kVar) {
        j$.sun.misc.a aVar = h;
        return j$.com.android.tools.r8.a.S(aVar.a, kVarArr, (((long) i2) << o) + ((long) n), kVar);
    }

    public static final void h(k[] kVarArr, int i2, k kVar) {
        h.j(kVarArr, (((long) i2) << o) + ((long) n), kVar);
    }

    public ConcurrentHashMap() {
    }

    public ConcurrentHashMap(int i2) {
        this(i2, 0.75f, 1);
    }

    public ConcurrentHashMap(Map<? extends K, ? extends V> map) {
        this.sizeCtl = 16;
        putAll(map);
    }

    public ConcurrentHashMap(int i2, float f) {
        this(i2, f, 1);
    }

    public ConcurrentHashMap(int i2, float f, int i3) {
        if (f <= 0.0f || i2 < 0 || i3 <= 0) {
            throw new IllegalArgumentException();
        }
        long j2 = (long) (((double) ((i2 < i3 ? i3 : i2) / f)) + 1.0d);
        this.sizeCtl = j2 >= 1073741824 ? TLObject.FLAG_30 : l((int) j2);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        long j2 = j();
        if (j2 < 0) {
            return 0;
        }
        if (j2 > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) j2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return j() <= 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        int length;
        k kVarK;
        Object obj2;
        int i2 = i(obj.hashCode());
        k[] kVarArr = this.a;
        if (kVarArr == null || (length = kVarArr.length) <= 0 || (kVarK = k(kVarArr, (length - 1) & i2)) == null) {
            return null;
        }
        int i3 = kVarK.a;
        if (i3 == i2) {
            Object obj3 = kVarK.b;
            if (obj3 == obj || (obj3 != null && obj.equals(obj3))) {
                return (V) kVarK.c;
            }
        } else if (i3 < 0) {
            k kVarA = kVarK.a(i2, obj);
            if (kVarA != null) {
                return (V) kVarA.c;
            }
            return null;
        }
        while (true) {
            kVarK = kVarK.d;
            if (kVarK == null) {
                return null;
            }
            if (kVarK.a == i2 && ((obj2 = kVarK.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                return (V) kVarK.c;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return get(obj) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean containsValue(Object obj) {
        obj.getClass();
        k[] kVarArr = this.a;
        if (kVarArr != null) {
            o oVar = new o(kVarArr, kVarArr.length, 0, kVarArr.length);
            while (true) {
                k kVarA = oVar.a();
                if (kVarA == null) {
                    break;
                }
                Object obj2 = kVarA.c;
                if (obj2 == obj) {
                    return true;
                }
                if (obj2 != null && obj.equals(obj2)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k2, V v) {
        return (V) f(k2, v, false);
    }

    public final Object f(Object obj, Object obj2, boolean z) {
        Object obj3;
        Object obj4;
        Object obj5;
        Object obj6;
        if (obj == null || obj2 == null) {
            throw null;
        }
        int i2 = i(obj.hashCode());
        k[] kVarArrE = this.a;
        int i3 = 0;
        while (true) {
            if (kVarArrE != null) {
                int length = kVarArrE.length;
                if (length != 0) {
                    int i4 = (length - 1) & i2;
                    k kVarK = k(kVarArrE, i4);
                    if (kVarK == null) {
                        if (b(kVarArrE, i4, new k(i2, obj, obj2))) {
                            break;
                        }
                    } else {
                        int i5 = kVarK.a;
                        if (i5 == -1) {
                            kVarArrE = d(kVarArrE, kVarK);
                        } else {
                            if (z && i5 == i2 && (((obj5 = kVarK.b) == obj || (obj5 != null && obj.equals(obj5))) && (obj6 = kVarK.c) != null)) {
                                return obj6;
                            }
                            synchronized (kVarK) {
                                try {
                                    if (k(kVarArrE, i4) != kVarK) {
                                        obj3 = null;
                                    } else if (i5 >= 0) {
                                        i3 = 1;
                                        k kVar = kVarK;
                                        while (true) {
                                            if (kVar.a == i2 && ((obj4 = kVar.b) == obj || (obj4 != null && obj.equals(obj4)))) {
                                                obj3 = kVar.c;
                                                if (!z) {
                                                    kVar.c = obj2;
                                                }
                                            } else {
                                                k kVar2 = kVar.d;
                                                if (kVar2 == null) {
                                                    kVar.d = new k(i2, obj, obj2);
                                                    obj3 = null;
                                                } else {
                                                    i3++;
                                                    kVar = kVar2;
                                                }
                                            }
                                        }
                                    } else if (kVarK instanceof p) {
                                        q qVarE = ((p) kVarK).e(i2, obj, obj2);
                                        if (qVarE != null) {
                                            Object obj7 = qVarE.c;
                                            if (!z) {
                                                qVarE.c = obj2;
                                            }
                                            obj3 = obj7;
                                        } else {
                                            obj3 = null;
                                        }
                                        i3 = 2;
                                    } else {
                                        if (kVarK instanceof l) {
                                            throw new IllegalStateException("Recursive update");
                                        }
                                        obj3 = null;
                                    }
                                } catch (Throwable th) {
                                    throw th;
                                }
                            }
                            if (i3 != 0) {
                                if (i3 >= 8) {
                                    n(kVarArrE, i4);
                                }
                                if (obj3 == null) {
                                    break;
                                }
                                return obj3;
                            }
                        }
                    }
                }
            }
            kVarArrE = e();
        }
        a(1L, i3);
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        o(map.size());
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            f(entry.getKey(), entry.getValue(), false);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        return (V) g(obj, null, null);
    }

    /* JADX WARN: Code duplicated, block: B:67:0x00b0 A[PHI: r7
  0x00b0: PHI (r7v3 boolean) = 
  (r7v1 boolean)
  (r7v4 boolean)
  (r7v4 boolean)
  (r7v4 boolean)
  (r7v4 boolean)
  (r7v4 boolean)
  (r7v4 boolean)
  (r7v4 boolean)
 binds: [B:66:0x00af, B:47:0x0077, B:49:0x007d, B:53:0x0085, B:55:0x008b, B:42:0x0069, B:32:0x004b, B:34:0x0051] A[DONT_GENERATE, DONT_INLINE]] */
    public final Object g(Object obj, Object obj2, Object obj3) {
        int length;
        int i2;
        k kVarK;
        boolean z;
        Object obj4;
        q qVarB;
        Object obj5;
        int i3 = i(obj.hashCode());
        k[] kVarArrD = this.a;
        while (kVarArrD != null && (length = kVarArrD.length) != 0 && (kVarK = k(kVarArrD, (i2 = (length - 1) & i3))) != null) {
            int i4 = kVarK.a;
            if (i4 == -1) {
                kVarArrD = d(kVarArrD, kVarK);
            } else {
                synchronized (kVarK) {
                    try {
                        if (k(kVarArrD, i2) == kVarK) {
                            z = true;
                            if (i4 >= 0) {
                                k kVar = null;
                                k kVar2 = kVarK;
                                while (true) {
                                    if (kVar2.a == i3 && ((obj5 = kVar2.b) == obj || (obj5 != null && obj.equals(obj5)))) {
                                        obj4 = kVar2.c;
                                        if (obj3 == null || obj3 == obj4 || (obj4 != null && obj3.equals(obj4))) {
                                            if (obj2 != null) {
                                                kVar2.c = obj2;
                                            } else if (kVar != null) {
                                                kVar.d = kVar2.d;
                                            } else {
                                                h(kVarArrD, i2, kVar2.d);
                                            }
                                        }
                                    } else {
                                        k kVar3 = kVar2.d;
                                        if (kVar3 != null) {
                                            kVar = kVar2;
                                            kVar2 = kVar3;
                                        }
                                    }
                                    obj4 = null;
                                }
                            } else if (kVarK instanceof p) {
                                p pVar = (p) kVarK;
                                q qVar = pVar.e;
                                if (qVar == null || (qVarB = qVar.b(i3, obj, null)) == null) {
                                    obj4 = null;
                                } else {
                                    obj4 = qVarB.c;
                                    if (obj3 != null && obj3 != obj4 && (obj4 == null || !obj3.equals(obj4))) {
                                        obj4 = null;
                                    } else if (obj2 != null) {
                                        qVarB.c = obj2;
                                    } else if (pVar.f(qVarB)) {
                                        h(kVarArrD, i2, p(pVar.f));
                                    }
                                }
                            } else {
                                if (kVarK instanceof l) {
                                    throw new IllegalStateException("Recursive update");
                                }
                                z = false;
                                obj4 = null;
                            }
                        } else {
                            z = false;
                            obj4 = null;
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                if (z) {
                    if (obj4 == null) {
                        break;
                    }
                    if (obj2 == null) {
                        a(-1L, -1);
                    }
                    return obj4;
                }
            }
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        k kVarK;
        k kVar;
        k[] kVarArrD = this.a;
        long j2 = 0;
        loop0: while (true) {
            int i2 = 0;
            while (true) {
                if (kVarArrD == null || i2 >= kVarArrD.length) {
                    break loop0;
                }
                kVarK = k(kVarArrD, i2);
                if (kVarK == null) {
                    i2++;
                } else {
                    int i3 = kVarK.a;
                    if (i3 == -1) {
                        break;
                    }
                    synchronized (kVarK) {
                        try {
                            if (k(kVarArrD, i2) == kVarK) {
                                if (i3 >= 0) {
                                    kVar = kVarK;
                                } else {
                                    kVar = kVarK instanceof p ? ((p) kVarK).f : null;
                                }
                                while (kVar != null) {
                                    j2--;
                                    kVar = kVar.d;
                                }
                                h(kVarArrD, i2, null);
                                i2++;
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                }
            }
            kVarArrD = d(kVarArrD, kVarK);
        }
        if (j2 != 0) {
            a(j2, -1);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        KeySetView keySetView = this.d;
        if (keySetView != null) {
            return keySetView;
        }
        KeySetView keySetView2 = new KeySetView(this, null);
        this.d = keySetView2;
        return keySetView2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        r rVar = this.e;
        if (rVar != null) {
            return rVar;
        }
        r rVar2 = new r(this);
        this.e = rVar2;
        return rVar2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        e eVar = this.f;
        if (eVar != null) {
            return eVar;
        }
        e eVar2 = new e(this);
        this.f = eVar2;
        return eVar2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int hashCode() {
        k[] kVarArr = this.a;
        int iHashCode = 0;
        if (kVarArr != null) {
            o oVar = new o(kVarArr, kVarArr.length, 0, kVarArr.length);
            while (true) {
                k kVarA = oVar.a();
                if (kVarA == null) {
                    break;
                }
                iHashCode += kVarA.c.hashCode() ^ kVarA.b.hashCode();
            }
        }
        return iHashCode;
    }

    @Override // java.util.AbstractMap
    public final String toString() {
        k[] kVarArr = this.a;
        int length = kVarArr == null ? 0 : kVarArr.length;
        o oVar = new o(kVarArr, length, 0, length);
        StringBuilder sb = new StringBuilder("{");
        k kVarA = oVar.a();
        if (kVarA != null) {
            while (true) {
                Object obj = kVarA.b;
                Object obj2 = kVarA.c;
                if (obj == this) {
                    obj = "(this Map)";
                }
                sb.append(obj);
                sb.append(SignatureVisitor.INSTANCEOF);
                if (obj2 == this) {
                    obj2 = "(this Map)";
                }
                sb.append(obj2);
                kVarA = oVar.a();
                if (kVarA == null) {
                    break;
                }
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean equals(Object obj) {
        V value;
        V v;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map map = (Map) obj;
        k[] kVarArr = this.a;
        int length = kVarArr == null ? 0 : kVarArr.length;
        o oVar = new o(kVarArr, length, 0, length);
        while (true) {
            k kVarA = oVar.a();
            if (kVarA != null) {
                Object obj2 = kVarA.c;
                Object obj3 = map.get(kVarA.b);
                if (obj3 == null || (obj3 != obj2 && !obj3.equals(obj2))) {
                    break;
                }
            } else {
                for (Map.Entry<K, V> entry : map.entrySet()) {
                    K key = entry.getKey();
                    if (key == null || (value = entry.getValue()) == null || (v = get(key)) == null || (value != v && !value.equals(v))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int i2 = 0;
        int i3 = 1;
        while (i3 < 16) {
            i2++;
            i3 <<= 1;
        }
        int i4 = 32 - i2;
        int i5 = i3 - 1;
        m[] mVarArr = new m[16];
        for (int i6 = 0; i6 < 16; i6++) {
            mVarArr[i6] = new m();
        }
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("segments", mVarArr);
        putFieldPutFields.put("segmentShift", i4);
        putFieldPutFields.put("segmentMask", i5);
        objectOutputStream.writeFields();
        k[] kVarArr = this.a;
        if (kVarArr != null) {
            o oVar = new o(kVarArr, kVarArr.length, 0, kVarArr.length);
            while (true) {
                k kVarA = oVar.a();
                if (kVarA == null) {
                    break;
                }
                objectOutputStream.writeObject(kVarA.b);
                objectOutputStream.writeObject(kVarA.c);
            }
        }
        objectOutputStream.writeObject(null);
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        long j2;
        long j3;
        Object obj;
        this.sizeCtl = -1;
        objectInputStream.defaultReadObject();
        long j4 = 0;
        long j5 = 0;
        k kVar = null;
        while (true) {
            Object object = objectInputStream.readObject();
            Object object2 = objectInputStream.readObject();
            j2 = 1;
            if (object == null || object2 == null) {
                break;
            }
            j5++;
            kVar = new k(i(object.hashCode()), object, object2, kVar);
        }
        if (j5 == 0) {
            this.sizeCtl = 0;
            return;
        }
        long j6 = (long) (((double) (j5 / 0.75f)) + 1.0d);
        int iL = j6 >= 1073741824 ? TLObject.FLAG_30 : l((int) j6);
        k[] kVarArr = new k[iL];
        int i2 = iL - 1;
        while (kVar != null) {
            k kVar2 = kVar.d;
            int i3 = kVar.a;
            int i4 = i3 & i2;
            k kVarK = k(kVarArr, i4);
            boolean z = true;
            if (kVarK == null) {
                j3 = j2;
            } else {
                Object obj2 = kVar.b;
                if (kVarK.a < 0) {
                    if (((p) kVarK).e(i3, obj2, kVar.c) == null) {
                        j4 += j2;
                    }
                    j3 = j2;
                } else {
                    j3 = j2;
                    int i5 = 0;
                    for (k kVar3 = kVarK; kVar3 != null; kVar3 = kVar3.d) {
                        if (kVar3.a == i3 && ((obj = kVar3.b) == obj2 || (obj != null && obj2.equals(obj)))) {
                            z = false;
                            break;
                        }
                        i5++;
                    }
                    if (z && i5 >= 8) {
                        j4 += j3;
                        kVar.d = kVarK;
                        k kVar4 = kVar;
                        q qVar = null;
                        q qVar2 = null;
                        while (kVar4 != null) {
                            q qVar3 = new q(kVar4.a, kVar4.b, kVar4.c, null, null);
                            qVar3.h = qVar2;
                            if (qVar2 == null) {
                                qVar = qVar3;
                            } else {
                                qVar2.d = qVar3;
                            }
                            kVar4 = kVar4.d;
                            qVar2 = qVar3;
                        }
                        h(kVarArr, i4, new p(qVar));
                    }
                }
                z = false;
            }
            if (z) {
                j4 += j3;
                kVar.d = kVarK;
                h(kVarArr, i4, kVar);
            }
            kVar = kVar2;
            j2 = j3;
        }
        this.a = kVarArr;
        this.sizeCtl = iL - (iL >>> 2);
        this.baseCount = j4;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public V putIfAbsent(K k2, V v) {
        return (V) f(k2, v, true);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public boolean remove(Object obj, Object obj2) {
        obj.getClass();
        return (obj2 == null || g(obj, null, obj2) == null) ? false : true;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public final boolean replace(Object obj, Object obj2, Object obj3) {
        if (obj == null || obj2 == null || obj3 == null) {
            throw null;
        }
        return g(obj, obj3, obj2) != null;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public final Object replace(Object obj, Object obj2) {
        if (obj == null || obj2 == null) {
            throw null;
        }
        return g(obj, obj2, null);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public final Object getOrDefault(Object obj, Object obj2) {
        V v = get(obj);
        return v == null ? obj2 : v;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public final void forEach(BiConsumer biConsumer) {
        biConsumer.getClass();
        k[] kVarArr = this.a;
        if (kVarArr == null) {
            return;
        }
        o oVar = new o(kVarArr, kVarArr.length, 0, kVarArr.length);
        while (true) {
            k kVarA = oVar.a();
            if (kVarA == null) {
                return;
            } else {
                biConsumer.accept(kVarA.b, kVarA.c);
            }
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public final void replaceAll(BiFunction biFunction) {
        biFunction.getClass();
        k[] kVarArr = this.a;
        if (kVarArr == null) {
            return;
        }
        o oVar = new o(kVarArr, kVarArr.length, 0, kVarArr.length);
        while (true) {
            k kVarA = oVar.a();
            if (kVarA == null) {
                return;
            }
            Object obj = kVarA.c;
            Object obj2 = kVarA.b;
            do {
                Object objApply = biFunction.apply(obj2, obj);
                objApply.getClass();
                if (g(obj2, objApply, obj) != null) {
                    break;
                } else {
                    obj = get(obj2);
                }
            } while (obj != null);
        }
    }

    /* JADX WARN: Bottom block not found for handler: all -> 0x0043 */
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object computeIfAbsent(Object obj, Function function) {
        q qVarB;
        Object obj2;
        Object obj3;
        Object obj4;
        if (obj == null || function == null) {
            throw null;
        }
        int i2 = i(obj.hashCode());
        k[] kVarArrE = this.a;
        Object objApply = null;
        int i3 = 0;
        while (true) {
            if (kVarArrE != null) {
                int length = kVarArrE.length;
                if (length != 0) {
                    int i4 = (length - 1) & i2;
                    k kVarK = k(kVarArrE, i4);
                    boolean z = true;
                    if (kVarK == null) {
                        l lVar = new l();
                        synchronized (lVar) {
                            if (b(kVarArrE, i4, lVar)) {
                                try {
                                    objApply = function.apply(obj);
                                    h(kVarArrE, i4, objApply != null ? new k(i2, obj, objApply) : null);
                                    i3 = 1;
                                } catch (Throwable th) {
                                    h(kVarArrE, i4, null);
                                    throw th;
                                }
                            }
                        }
                        if (i3 != 0) {
                            break;
                        }
                    } else {
                        int i5 = kVarK.a;
                        if (i5 == -1) {
                            kVarArrE = d(kVarArrE, kVarK);
                        } else {
                            if (i5 == i2 && (((obj3 = kVarK.b) == obj || (obj3 != null && obj.equals(obj3))) && (obj4 = kVarK.c) != null)) {
                                return obj4;
                            }
                            synchronized (kVarK) {
                                try {
                                    if (k(kVarArrE, i4) != kVarK) {
                                        z = false;
                                        break;
                                    }
                                    if (i5 >= 0) {
                                        k kVar = kVarK;
                                        i3 = 1;
                                        while (true) {
                                            if (kVar.a == i2 && ((obj2 = kVar.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                                                objApply = kVar.c;
                                                z = false;
                                                break;
                                            }
                                            k kVar2 = kVar.d;
                                            if (kVar2 == null) {
                                                Object objApply2 = function.apply(obj);
                                                if (objApply2 == null) {
                                                    z = false;
                                                } else {
                                                    if (kVar.d != null) {
                                                        throw new IllegalStateException("Recursive update");
                                                    }
                                                    kVar.d = new k(i2, obj, objApply2);
                                                }
                                                objApply = objApply2;
                                                break;
                                            }
                                            i3++;
                                            kVar = kVar2;
                                        }
                                    } else if (kVarK instanceof p) {
                                        p pVar = (p) kVarK;
                                        q qVar = pVar.e;
                                        if (qVar != null && (qVarB = qVar.b(i2, obj, null)) != null) {
                                            z = false;
                                            objApply = qVarB.c;
                                        } else {
                                            objApply = function.apply(obj);
                                            if (objApply != null) {
                                                pVar.e(i2, obj, objApply);
                                            } else {
                                                z = false;
                                            }
                                        }
                                        i3 = 2;
                                    } else {
                                        if (!(kVarK instanceof l)) {
                                            z = false;
                                            break;
                                        }
                                        throw new IllegalStateException("Recursive update");
                                    }
                                } catch (Throwable th2) {
                                    throw th2;
                                }
                            }
                            if (i3 != 0) {
                                if (i3 >= 8) {
                                    n(kVarArrE, i4);
                                }
                                if (z) {
                                    break;
                                }
                                return objApply;
                            }
                        }
                    }
                }
            }
            kVarArrE = e();
        }
        if (objApply != null) {
            a(1L, i3);
        }
        return objApply;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public final Object computeIfPresent(Object obj, BiFunction biFunction) {
        q qVarB;
        Object obj2;
        if (obj == null || biFunction == null) {
            throw null;
        }
        int i2 = i(obj.hashCode());
        k[] kVarArrE = this.a;
        int i3 = 0;
        Object objApply = null;
        int i4 = 0;
        while (true) {
            if (kVarArrE != null) {
                int length = kVarArrE.length;
                if (length != 0) {
                    int i5 = (length - 1) & i2;
                    k kVarK = k(kVarArrE, i5);
                    if (kVarK == null) {
                        break;
                    }
                    int i6 = kVarK.a;
                    if (i6 == -1) {
                        kVarArrE = d(kVarArrE, kVarK);
                    } else {
                        synchronized (kVarK) {
                            try {
                                if (k(kVarArrE, i5) == kVarK) {
                                    if (i6 >= 0) {
                                        i4 = 1;
                                        k kVar = null;
                                        k kVar2 = kVarK;
                                        while (true) {
                                            if (kVar2.a == i2 && ((obj2 = kVar2.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                                                objApply = biFunction.apply(obj, kVar2.c);
                                                if (objApply != null) {
                                                    kVar2.c = objApply;
                                                    break;
                                                }
                                                k kVar3 = kVar2.d;
                                                if (kVar != null) {
                                                    kVar.d = kVar3;
                                                } else {
                                                    h(kVarArrE, i5, kVar3);
                                                }
                                                i3 = -1;
                                                break;
                                            }
                                            k kVar4 = kVar2.d;
                                            if (kVar4 == null) {
                                                break;
                                            }
                                            i4++;
                                            kVar = kVar2;
                                            kVar2 = kVar4;
                                        }
                                    } else if (kVarK instanceof p) {
                                        p pVar = (p) kVarK;
                                        q qVar = pVar.e;
                                        if (qVar != null && (qVarB = qVar.b(i2, obj, null)) != null) {
                                            objApply = biFunction.apply(obj, qVarB.c);
                                            if (objApply != null) {
                                                qVarB.c = objApply;
                                            } else {
                                                if (pVar.f(qVarB)) {
                                                    h(kVarArrE, i5, p(pVar.f));
                                                }
                                                i3 = -1;
                                            }
                                        }
                                        i4 = 2;
                                    } else if (kVarK instanceof l) {
                                        throw new IllegalStateException("Recursive update");
                                    }
                                }
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                        if (i4 != 0) {
                            break;
                        }
                    }
                }
            }
            kVarArrE = e();
        }
        if (i3 != 0) {
            a(i3, i4);
        }
        return objApply;
    }

    /* JADX WARN: Bottom block not found for handler: all -> 0x0044 */
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object compute(Object obj, BiFunction biFunction) {
        k kVar;
        Object objApply;
        Object obj2;
        if (obj == null || biFunction == null) {
            throw null;
        }
        int i2 = i(obj.hashCode());
        k[] kVarArrE = this.a;
        int i3 = 0;
        Object objApply2 = null;
        int i4 = 0;
        while (true) {
            if (kVarArrE != null) {
                int length = kVarArrE.length;
                if (length != 0) {
                    int i5 = (length - 1) & i2;
                    k kVarK = k(kVarArrE, i5);
                    if (kVarK == null) {
                        l lVar = new l();
                        synchronized (lVar) {
                            if (b(kVarArrE, i5, lVar)) {
                                try {
                                    objApply2 = biFunction.apply(obj, null);
                                    if (objApply2 != null) {
                                        kVar = new k(i2, obj, objApply2);
                                        i4 = 1;
                                    } else {
                                        kVar = null;
                                    }
                                    h(kVarArrE, i5, kVar);
                                    i3 = 1;
                                } catch (Throwable th) {
                                    h(kVarArrE, i5, null);
                                    throw th;
                                }
                            }
                        }
                        if (i3 != 0) {
                            break;
                        }
                    } else {
                        int i6 = kVarK.a;
                        if (i6 == -1) {
                            kVarArrE = d(kVarArrE, kVarK);
                        } else {
                            synchronized (kVarK) {
                                try {
                                    if (k(kVarArrE, i5) == kVarK) {
                                        if (i6 >= 0) {
                                            k kVar2 = null;
                                            k kVar3 = kVarK;
                                            i3 = 1;
                                            while (true) {
                                                if (kVar3.a == i2 && ((obj2 = kVar3.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                                                    Object objApply3 = biFunction.apply(obj, kVar3.c);
                                                    if (objApply3 != null) {
                                                        kVar3.c = objApply3;
                                                        objApply2 = objApply3;
                                                    } else {
                                                        k kVar4 = kVar3.d;
                                                        if (kVar2 != null) {
                                                            kVar2.d = kVar4;
                                                        } else {
                                                            h(kVarArrE, i5, kVar4);
                                                        }
                                                        objApply2 = objApply3;
                                                        i4 = -1;
                                                    }
                                                } else {
                                                    k kVar5 = kVar3.d;
                                                    if (kVar5 == null) {
                                                        objApply = biFunction.apply(obj, null);
                                                        if (objApply != null) {
                                                            if (kVar3.d != null) {
                                                                throw new IllegalStateException("Recursive update");
                                                            }
                                                            kVar3.d = new k(i2, obj, objApply);
                                                            i4 = 1;
                                                        }
                                                        objApply2 = objApply;
                                                    } else {
                                                        i3++;
                                                        kVar2 = kVar3;
                                                        kVar3 = kVar5;
                                                    }
                                                }
                                            }
                                        } else if (kVarK instanceof p) {
                                            p pVar = (p) kVarK;
                                            q qVar = pVar.e;
                                            q qVarB = qVar != null ? qVar.b(i2, obj, null) : null;
                                            objApply = biFunction.apply(obj, qVarB == null ? null : qVarB.c);
                                            if (objApply != null) {
                                                if (qVarB != null) {
                                                    qVarB.c = objApply;
                                                } else {
                                                    pVar.e(i2, obj, objApply);
                                                    i4 = 1;
                                                }
                                            } else if (qVarB != null) {
                                                if (pVar.f(qVarB)) {
                                                    h(kVarArrE, i5, p(pVar.f));
                                                }
                                                i4 = -1;
                                            }
                                            i3 = 1;
                                            objApply2 = objApply;
                                        } else if (kVarK instanceof l) {
                                            throw new IllegalStateException("Recursive update");
                                        }
                                    }
                                } catch (Throwable th2) {
                                    throw th2;
                                }
                            }
                            if (i3 != 0) {
                                if (i3 < 8) {
                                    break;
                                }
                                n(kVarArrE, i5);
                                break;
                            }
                        }
                    }
                }
            }
            kVarArrE = e();
        }
        if (i4 != 0) {
            a(i4, i3);
        }
        return objApply2;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public final Object merge(Object obj, Object obj2, BiFunction biFunction) {
        int i2;
        Object obj3;
        Object obj4 = obj2;
        if (obj == null || obj4 == null || biFunction == null) {
            throw null;
        }
        int i3 = i(obj.hashCode());
        k[] kVarArrE = this.a;
        int i4 = 0;
        Object obj5 = null;
        int i5 = 0;
        while (true) {
            if (kVarArrE != null) {
                int length = kVarArrE.length;
                if (length != 0) {
                    int i6 = (length - 1) & i3;
                    k kVarK = k(kVarArrE, i6);
                    i2 = 1;
                    if (kVarK == null) {
                        if (b(kVarArrE, i6, new k(i3, obj, obj4))) {
                            break;
                        }
                    } else {
                        int i7 = kVarK.a;
                        if (i7 == -1) {
                            kVarArrE = d(kVarArrE, kVarK);
                        } else {
                            synchronized (kVarK) {
                                try {
                                    if (k(kVarArrE, i6) == kVarK) {
                                        if (i7 >= 0) {
                                            k kVar = null;
                                            k kVar2 = kVarK;
                                            i4 = 1;
                                            while (true) {
                                                if (kVar2.a == i3 && ((obj3 = kVar2.b) == obj || (obj3 != null && obj.equals(obj3)))) {
                                                    Object objApply = biFunction.apply(kVar2.c, obj4);
                                                    if (objApply != null) {
                                                        kVar2.c = objApply;
                                                        obj5 = objApply;
                                                        break;
                                                    }
                                                    k kVar3 = kVar2.d;
                                                    if (kVar != null) {
                                                        kVar.d = kVar3;
                                                    } else {
                                                        h(kVarArrE, i6, kVar3);
                                                    }
                                                    obj5 = objApply;
                                                    i5 = -1;
                                                    break;
                                                }
                                                k kVar4 = kVar2.d;
                                                if (kVar4 == null) {
                                                    kVar2.d = new k(i3, obj, obj4);
                                                    obj5 = obj4;
                                                    i5 = 1;
                                                    break;
                                                }
                                                i4++;
                                                kVar = kVar2;
                                                kVar2 = kVar4;
                                            }
                                        } else if (kVarK instanceof p) {
                                            p pVar = (p) kVarK;
                                            q qVar = pVar.e;
                                            q qVarB = qVar == null ? null : qVar.b(i3, obj, null);
                                            Object objApply2 = qVarB == null ? obj4 : biFunction.apply(qVarB.c, obj4);
                                            if (objApply2 != null) {
                                                if (qVarB != null) {
                                                    qVarB.c = objApply2;
                                                } else {
                                                    pVar.e(i3, obj, objApply2);
                                                    i5 = 1;
                                                }
                                            } else if (qVarB != null) {
                                                if (pVar.f(qVarB)) {
                                                    h(kVarArrE, i6, p(pVar.f));
                                                }
                                                i5 = -1;
                                            }
                                            i4 = 2;
                                            obj5 = objApply2;
                                        } else if (kVarK instanceof l) {
                                            throw new IllegalStateException("Recursive update");
                                        }
                                    }
                                } catch (Throwable th) {
                                    throw th;
                                }
                            }
                            if (i4 != 0) {
                                if (i4 >= 8) {
                                    n(kVarArrE, i6);
                                }
                                i2 = i5;
                                obj4 = obj5;
                                break;
                            }
                        }
                    }
                }
            }
            kVarArrE = e();
        }
        if (i2 != 0) {
            a(i2, i4);
        }
        return obj4;
    }

    public static <K> KeySetView<K, Boolean> newKeySet(int i2) {
        return new KeySetView<>(new ConcurrentHashMap(i2), Boolean.TRUE);
    }

    public final k[] e() {
        while (true) {
            k[] kVarArr = this.a;
            if (kVarArr != null && kVarArr.length != 0) {
                return kVarArr;
            }
            int i2 = this.sizeCtl;
            if (i2 < 0) {
                Thread.yield();
            } else if (h.c(this, i, i2, -1)) {
                try {
                    k[] kVarArr2 = this.a;
                    if (kVarArr2 == null || kVarArr2.length == 0) {
                        int i3 = i2 > 0 ? i2 : 16;
                        k[] kVarArr3 = new k[i3];
                        this.a = kVarArr3;
                        i2 = i3 - (i3 >>> 2);
                        kVarArr2 = kVarArr3;
                    }
                    return kVarArr2;
                } finally {
                    this.sizeCtl = i2;
                }
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:124:0x019d  */
    /* JADX WARN: Code duplicated, block: B:149:0x01ab A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:150:0x014f A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:45:0x00a5  */
    /* JADX WARN: Code duplicated, block: B:48:0x00b2  */
    /* JADX WARN: Code duplicated, block: B:6:0x0019  */
    /* JADX WARN: Code duplicated, block: B:73:0x00fd  */
    /* JADX WARN: Code duplicated, block: B:98:0x0142 A[Catch: all -> 0x014d, TRY_LEAVE, TryCatch #2 {all -> 0x014d, blocks: (B:96:0x013e, B:98:0x0142), top: B:132:0x013e }] */
    public final void a(long j2, int i2) {
        boolean zD;
        u uVar;
        int i3;
        c[] cVarArr;
        j$.sun.misc.a aVar;
        long j3;
        long j4;
        boolean z;
        int length;
        boolean z2;
        int length2;
        int length3;
        c cVar;
        long j5;
        k[] kVarArr;
        int length4;
        k[] kVarArr2;
        ConcurrentHashMap<K, V> concurrentHashMap = this;
        c[] cVarArr2 = concurrentHashMap.c;
        if (cVarArr2 != null) {
            if (cVarArr2 != null) {
                zD = true;
            } else {
                zD = true;
            }
            uVar = ThreadLocalRandom.f;
            i3 = ((ThreadLocalRandom) uVar.get()).b;
            if (i3 == 0) {
                ThreadLocalRandom.d();
                i3 = ((ThreadLocalRandom) uVar.get()).b;
                zD = true;
            }
            boolean z3 = zD;
            int i4 = i3;
            while (true) {
                boolean z4 = false;
                while (true) {
                    cVarArr = concurrentHashMap.c;
                    if (cVarArr == null) {
                    }
                    if (concurrentHashMap.cellsBusy != 0) {
                        aVar = h;
                        j3 = k;
                        j4 = concurrentHashMap.baseCount;
                        if (aVar.d(concurrentHashMap, j3, j4, j4 + j2)) {
                            return;
                        }
                    } else {
                        aVar = h;
                        j3 = k;
                        j4 = concurrentHashMap.baseCount;
                        if (aVar.d(concurrentHashMap, j3, j4, j4 + j2)) {
                            return;
                        }
                    }
                    concurrentHashMap = this;
                }
                if (concurrentHashMap.c == cVarArr) {
                    concurrentHashMap.c = (c[]) Arrays.copyOf(cVarArr, length << 1);
                }
                concurrentHashMap.cellsBusy = 0;
            }
        } else {
            j$.sun.misc.a aVar2 = h;
            long j6 = k;
            long j7 = concurrentHashMap.baseCount;
            j5 = j7 + j2;
            if (!aVar2.d(concurrentHashMap, j6, j7, j5)) {
                if (cVarArr2 != null || (length3 = cVarArr2.length - 1) < 0 || (cVar = cVarArr2[length3 & ((ThreadLocalRandom) ThreadLocalRandom.f.get()).b]) == null) {
                    zD = true;
                } else {
                    j$.sun.misc.a aVar3 = h;
                    long j8 = m;
                    long j9 = cVar.value;
                    zD = aVar3.d(cVar, j8, j9, j9 + j2);
                    if (zD) {
                        if (i2 <= 1) {
                            return;
                        } else {
                            j5 = concurrentHashMap.j();
                        }
                    }
                }
                uVar = ThreadLocalRandom.f;
                i3 = ((ThreadLocalRandom) uVar.get()).b;
                if (i3 == 0) {
                    ThreadLocalRandom.d();
                    i3 = ((ThreadLocalRandom) uVar.get()).b;
                    zD = true;
                }
                boolean z5 = zD;
                int i5 = i3;
                while (true) {
                    boolean z6 = false;
                    while (true) {
                        cVarArr = concurrentHashMap.c;
                        if (cVarArr == null && (length = cVarArr.length) > 0) {
                            c cVar2 = cVarArr[(length - 1) & i5];
                            if (cVar2 != null) {
                                if (z5) {
                                    j$.sun.misc.a aVar4 = h;
                                    long j10 = m;
                                    long j11 = cVar2.value;
                                    if (!aVar4.d(cVar2, j10, j11, j11 + j2)) {
                                        if (concurrentHashMap.c == cVarArr && length < g) {
                                            if (!z6) {
                                                z6 = true;
                                            } else if (concurrentHashMap.cellsBusy == 0 && aVar4.c(concurrentHashMap, l, 0, 1)) {
                                                break;
                                            }
                                        }
                                    } else {
                                        return;
                                    }
                                } else {
                                    z5 = true;
                                }
                                int i6 = (i5 << 13) ^ i5;
                                int i7 = i6 ^ (i6 >>> 17);
                                int i8 = i7 ^ (i7 << 5);
                                ((ThreadLocalRandom) ThreadLocalRandom.f.get()).b = i8;
                                i5 = i8;
                                concurrentHashMap = this;
                            } else if (concurrentHashMap.cellsBusy == 0) {
                                c cVar3 = new c(j2);
                                if (concurrentHashMap.cellsBusy == 0 && h.c(concurrentHashMap, l, 0, 1)) {
                                    try {
                                        c[] cVarArr3 = concurrentHashMap.c;
                                        if (cVarArr3 == null || (length2 = cVarArr3.length) <= 0) {
                                            z2 = false;
                                        } else {
                                            int i9 = (length2 - 1) & i5;
                                            if (cVarArr3[i9] == null) {
                                                cVarArr3[i9] = cVar3;
                                                z2 = true;
                                            } else {
                                                z2 = false;
                                            }
                                        }
                                        concurrentHashMap.cellsBusy = 0;
                                        if (z2) {
                                            return;
                                        }
                                    } catch (Throwable th) {
                                        concurrentHashMap.cellsBusy = 0;
                                        throw th;
                                    }
                                }
                            }
                            z6 = false;
                            int i10 = (i5 << 13) ^ i5;
                            int i11 = i10 ^ (i10 >>> 17);
                            int i12 = i11 ^ (i11 << 5);
                            ((ThreadLocalRandom) ThreadLocalRandom.f.get()).b = i12;
                            i5 = i12;
                            concurrentHashMap = this;
                        } else {
                            if (concurrentHashMap.cellsBusy != 0 && concurrentHashMap.c == cVarArr && h.c(concurrentHashMap, l, 0, 1)) {
                                try {
                                    if (concurrentHashMap.c == cVarArr) {
                                        c[] cVarArr4 = new c[2];
                                        cVarArr4[i5 & 1] = new c(j2);
                                        concurrentHashMap.c = cVarArr4;
                                        z = true;
                                    } else {
                                        z = false;
                                    }
                                    concurrentHashMap.cellsBusy = 0;
                                    if (z) {
                                        return;
                                    }
                                } catch (Throwable th2) {
                                    concurrentHashMap.cellsBusy = 0;
                                    throw th2;
                                }
                            } else {
                                aVar = h;
                                j3 = k;
                                j4 = concurrentHashMap.baseCount;
                                if (aVar.d(concurrentHashMap, j3, j4, j4 + j2)) {
                                    return;
                                }
                            }
                            concurrentHashMap = this;
                        }
                    }
                    try {
                        if (concurrentHashMap.c == cVarArr) {
                            concurrentHashMap.c = (c[]) Arrays.copyOf(cVarArr, length << 1);
                        }
                        concurrentHashMap.cellsBusy = 0;
                    } catch (Throwable th3) {
                        concurrentHashMap.cellsBusy = 0;
                        throw th3;
                    }
                }
            }
        }
        if (i2 < 0) {
            return;
        }
        while (true) {
            int i13 = concurrentHashMap.sizeCtl;
            if (j5 < i13 || (kVarArr = concurrentHashMap.a) == null || (length4 = kVarArr.length) >= 1073741824) {
                return;
            }
            int iNumberOfLeadingZeros = Integer.numberOfLeadingZeros(length4) | 32768;
            if (i13 < 0) {
                if ((i13 >>> 16) != iNumberOfLeadingZeros || i13 == iNumberOfLeadingZeros + 1 || i13 == iNumberOfLeadingZeros + 65535 || (kVarArr2 = concurrentHashMap.b) == null || concurrentHashMap.transferIndex <= 0) {
                    return;
                }
                if (h.c(concurrentHashMap, i, i13, i13 + 1)) {
                    concurrentHashMap.m(kVarArr, kVarArr2);
                }
            } else if (h.c(concurrentHashMap, i, i13, (iNumberOfLeadingZeros << 16) + 2)) {
                concurrentHashMap.m(kVarArr, null);
            }
            j5 = concurrentHashMap.j();
        }
    }

    public final k[] d(k[] kVarArr, k kVar) {
        int i2;
        if (kVar instanceof g) {
            k[] kVarArr2 = ((g) kVar).e;
            int iNumberOfLeadingZeros = Integer.numberOfLeadingZeros(kVarArr.length) | 32768;
            while (kVarArr2 == this.b && this.a == kVarArr && (i2 = this.sizeCtl) < 0 && (i2 >>> 16) == iNumberOfLeadingZeros && i2 != iNumberOfLeadingZeros + 1 && i2 != 65535 + iNumberOfLeadingZeros && this.transferIndex > 0) {
                if (h.c(this, i, i2, i2 + 1)) {
                    m(kVarArr, kVarArr2);
                    return kVarArr2;
                }
            }
            return kVarArr2;
        }
        return this.a;
    }

    public final void o(int i2) {
        int length;
        int iL = i2 >= 536870912 ? 1073741824 : l(i2 + (i2 >>> 1) + 1);
        while (true) {
            int i3 = this.sizeCtl;
            if (i3 < 0) {
                break;
            }
            k[] kVarArr = this.a;
            if (kVarArr == null || (length = kVarArr.length) == 0) {
                int i4 = i3 > iL ? i3 : iL;
                if (h.c(this, i, i3, -1)) {
                    try {
                        if (this.a == kVarArr) {
                            this.a = new k[i4];
                            i3 = i4 - (i4 >>> 2);
                        }
                        this.sizeCtl = i3;
                    } catch (Throwable th) {
                        this.sizeCtl = i3;
                        throw th;
                    }
                } else {
                    continue;
                }
            } else if (iL <= i3 || length >= 1073741824) {
                break;
            } else if (kVarArr == this.a) {
                if (h.c(this, i, i3, ((Integer.numberOfLeadingZeros(length) | 32768) << 16) + 2)) {
                    m(kVarArr, null);
                }
            }
        }
    }

    public final void m(k[] kVarArr, k[] kVarArr2) {
        k[] kVarArr3;
        int i2;
        int i3;
        int i4;
        boolean z;
        char c;
        int i5;
        int i6;
        k pVar;
        k pVar2;
        k kVar;
        ConcurrentHashMap<K, V> concurrentHashMap = this;
        int length = kVarArr.length;
        int i7 = g;
        boolean z2 = true;
        int i8 = i7 > 1 ? (length >>> 3) / i7 : length;
        char c2 = 16;
        int i9 = i8 < 16 ? 16 : i8;
        if (kVarArr2 == null) {
            try {
                k[] kVarArr4 = new k[length << 1];
                concurrentHashMap.b = kVarArr4;
                concurrentHashMap.transferIndex = length;
                kVarArr3 = kVarArr4;
            } catch (Throwable unused) {
                concurrentHashMap.sizeCtl = Integer.MAX_VALUE;
                return;
            }
        } else {
            kVarArr3 = kVarArr2;
        }
        int length2 = kVarArr3.length;
        g gVar = new g(kVarArr3);
        boolean zB = true;
        int i10 = 0;
        int i11 = 0;
        boolean z3 = false;
        while (true) {
            if (zB) {
                int i12 = i10 - 1;
                if (i12 >= i11 || z3) {
                    i11 = i11;
                    i10 = i12;
                    zB = false;
                } else {
                    int i13 = concurrentHashMap.transferIndex;
                    if (i13 <= 0) {
                        i10 = -1;
                    } else {
                        j$.sun.misc.a aVar = h;
                        int i14 = i11;
                        long j2 = j;
                        if (i13 > i9) {
                            i3 = i13 - i9;
                            i2 = i12;
                        } else {
                            i2 = i12;
                            i3 = 0;
                        }
                        boolean zC = aVar.c(concurrentHashMap, j2, i13, i3);
                        i11 = i3;
                        if (zC) {
                            i10 = i13 - 1;
                        } else {
                            i11 = i14;
                            i10 = i2;
                        }
                    }
                    zB = false;
                }
            } else {
                int i15 = i11;
                q qVar = null;
                k kVar2 = null;
                if (i10 < 0 || i10 >= length || (i6 = i10 + length) >= length2) {
                    i4 = length;
                    z = z2;
                    c = c2;
                    i5 = i9;
                    if (z3) {
                        concurrentHashMap.b = null;
                        concurrentHashMap.a = kVarArr3;
                        concurrentHashMap.sizeCtl = (i4 << 1) - (i4 >>> 1);
                        return;
                    }
                    int i16 = i10;
                    j$.sun.misc.a aVar2 = h;
                    long j3 = i;
                    int i17 = concurrentHashMap.sizeCtl;
                    if (!aVar2.c(concurrentHashMap, j3, i17, i17 - 1)) {
                        i10 = i16;
                    } else {
                        if (i17 - 2 != ((Integer.numberOfLeadingZeros(i4) | 32768) << 16)) {
                            return;
                        }
                        zB = z;
                        z3 = zB;
                        i10 = i4;
                    }
                } else {
                    k kVarK = k(kVarArr, i10);
                    if (kVarK == null) {
                        zB = b(kVarArr, i10, gVar);
                        i4 = length;
                        z = z2;
                        c = c2;
                        i5 = i9;
                    } else {
                        z = z2;
                        int i18 = kVarK.a;
                        if (i18 == -1) {
                            i4 = length;
                            c = c2;
                            i5 = i9;
                            zB = z;
                        } else {
                            synchronized (kVarK) {
                                try {
                                    if (k(kVarArr, i10) == kVarK) {
                                        if (i18 >= 0) {
                                            int i19 = i18 & length;
                                            k kVar3 = kVarK.d;
                                            k kVar4 = kVarK;
                                            while (kVar3 != null) {
                                                char c3 = c2;
                                                int i20 = kVar3.a & length;
                                                if (i20 != i19) {
                                                    kVar4 = kVar3;
                                                    i19 = i20;
                                                }
                                                kVar3 = kVar3.d;
                                                c2 = c3;
                                            }
                                            c = c2;
                                            if (i19 == 0) {
                                                kVar = null;
                                                kVar2 = kVar4;
                                            } else {
                                                kVar = kVar4;
                                            }
                                            k kVar5 = kVarK;
                                            while (kVar5 != kVar4) {
                                                int i21 = kVar5.a;
                                                Object obj = kVar5.b;
                                                int i22 = length;
                                                Object obj2 = kVar5.c;
                                                if ((i21 & i22) == 0) {
                                                    kVar2 = new k(i21, obj, obj2, kVar2);
                                                } else {
                                                    kVar = new k(i21, obj, obj2, kVar);
                                                }
                                                kVar5 = kVar5.d;
                                                length = i22;
                                                i9 = i9;
                                            }
                                            i4 = length;
                                            i5 = i9;
                                            h(kVarArr3, i10, kVar2);
                                            h(kVarArr3, i6, kVar);
                                            h(kVarArr, i10, gVar);
                                        } else {
                                            i4 = length;
                                            c = c2;
                                            i5 = i9;
                                            if (kVarK instanceof p) {
                                                p pVar3 = (p) kVarK;
                                                q qVar2 = null;
                                                q qVar3 = null;
                                                k kVar6 = pVar3.f;
                                                int i23 = 0;
                                                int i24 = 0;
                                                q qVar4 = null;
                                                while (kVar6 != null) {
                                                    p pVar4 = pVar3;
                                                    int i25 = kVar6.a;
                                                    q qVar5 = new q(i25, kVar6.b, kVar6.c, null, null);
                                                    if ((i25 & i4) == 0) {
                                                        qVar5.h = qVar3;
                                                        if (qVar3 == null) {
                                                            qVar = qVar5;
                                                        } else {
                                                            qVar3.d = qVar5;
                                                        }
                                                        i23++;
                                                        qVar3 = qVar5;
                                                    } else {
                                                        qVar5.h = qVar2;
                                                        if (qVar2 == null) {
                                                            qVar4 = qVar5;
                                                        } else {
                                                            qVar2.d = qVar5;
                                                        }
                                                        i24++;
                                                        qVar2 = qVar5;
                                                    }
                                                    kVar6 = kVar6.d;
                                                    pVar3 = pVar4;
                                                }
                                                p pVar5 = pVar3;
                                                if (i23 <= 6) {
                                                    pVar = p(qVar);
                                                } else {
                                                    pVar = i24 != 0 ? new p(qVar) : pVar5;
                                                }
                                                if (i24 <= 6) {
                                                    pVar2 = p(qVar4);
                                                } else {
                                                    pVar2 = i23 != 0 ? new p(qVar4) : pVar5;
                                                }
                                                h(kVarArr3, i10, pVar);
                                                h(kVarArr3, i6, pVar2);
                                                h(kVarArr, i10, gVar);
                                            }
                                        }
                                        zB = z;
                                    } else {
                                        i4 = length;
                                        c = c2;
                                        i5 = i9;
                                    }
                                } catch (Throwable th) {
                                    throw th;
                                }
                            }
                        }
                    }
                }
                concurrentHashMap = this;
                i11 = i15;
                z2 = z;
                c2 = c;
                length = i4;
                i9 = i5;
            }
        }
    }

    public final long j() {
        c[] cVarArr = this.c;
        long j2 = this.baseCount;
        if (cVarArr != null) {
            for (c cVar : cVarArr) {
                if (cVar != null) {
                    j2 += cVar.value;
                }
            }
        }
        return j2;
    }

    public final void n(k[] kVarArr, int i2) {
        int length = kVarArr.length;
        if (length < 64) {
            o(length << 1);
            return;
        }
        k kVarK = k(kVarArr, i2);
        if (kVarK == null || kVarK.a < 0) {
            return;
        }
        synchronized (kVarK) {
            try {
                if (k(kVarArr, i2) == kVarK) {
                    q qVar = null;
                    q qVar2 = null;
                    k kVar = kVarK;
                    while (kVar != null) {
                        q qVar3 = new q(kVar.a, kVar.b, kVar.c, null, null);
                        qVar3.h = qVar2;
                        if (qVar2 == null) {
                            qVar = qVar3;
                        } else {
                            qVar2.d = qVar3;
                        }
                        kVar = kVar.d;
                        qVar2 = qVar3;
                    }
                    h(kVarArr, i2, new p(qVar));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static k p(q qVar) {
        k kVar = null;
        k kVar2 = null;
        for (k kVar3 = qVar; kVar3 != null; kVar3 = kVar3.d) {
            k kVar4 = new k(kVar3.a, kVar3.b, kVar3.c);
            if (kVar2 == null) {
                kVar = kVar4;
            } else {
                kVar2.d = kVar4;
            }
            kVar2 = kVar4;
        }
        return kVar;
    }

    public static class KeySetView<K, V> extends b implements Set<K>, Serializable, j$.util.Set<K> {
        private static final long serialVersionUID = 7249069246763182397L;
        public final Object b;

        @Override // java.util.Collection, j$.util.Collection
        public final /* synthetic */ Stream parallelStream() {
            return j$.util.Collection.CC.$default$parallelStream(this);
        }

        @Override // java.util.Collection
        public final /* synthetic */ java.util.stream.Stream parallelStream() {
            return Stream.Wrapper.convert(j$.util.Collection.CC.$default$parallelStream(this));
        }

        @Override // java.util.Collection, j$.util.Collection
        public final /* synthetic */ boolean removeIf(Predicate predicate) {
            return j$.util.Collection.CC.$default$removeIf(this, predicate);
        }

        @Override // java.util.Collection, java.lang.Iterable, java.util.Set
        public final /* synthetic */ Spliterator spliterator() {
            return j$.util.Spliterator.Wrapper.convert(spliterator());
        }

        @Override // java.util.Collection, j$.util.Collection
        public final /* synthetic */ Stream stream() {
            return j$.util.Collection.CC.$default$stream(this);
        }

        @Override // java.util.Collection
        public final /* synthetic */ java.util.stream.Stream stream() {
            return Stream.Wrapper.convert(j$.util.Collection.CC.$default$stream(this));
        }

        @Override // java.util.Collection, j$.util.Collection
        public final /* synthetic */ Object[] toArray(IntFunction intFunction) {
            return toArray((Object[]) intFunction.apply(0));
        }

        public KeySetView(ConcurrentHashMap concurrentHashMap, Object obj) {
            super(concurrentHashMap);
            this.b = obj;
        }

        @Override // j$.util.concurrent.b, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            return this.a.containsKey(obj);
        }

        @Override // j$.util.concurrent.b, java.util.Collection, java.util.Set
        public final boolean remove(Object obj) {
            return this.a.remove(obj) != null;
        }

        @Override // j$.util.concurrent.b, java.util.Collection, java.lang.Iterable, java.util.Set
        public final Iterator iterator() {
            ConcurrentHashMap concurrentHashMap = this.a;
            k[] kVarArr = concurrentHashMap.a;
            int length = kVarArr == null ? 0 : kVarArr.length;
            return new h(kVarArr, length, length, concurrentHashMap, 0);
        }

        @Override // java.util.Collection, java.util.Set
        public final boolean add(Object obj) {
            Object obj2 = this.b;
            if (obj2 != null) {
                return this.a.f(obj, obj2, true) == null;
            }
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection, java.util.Set
        public final boolean addAll(Collection collection) {
            Object obj = this.b;
            if (obj == null) {
                throw new UnsupportedOperationException();
            }
            Iterator it = collection.iterator();
            boolean z = false;
            while (it.hasNext()) {
                if (this.a.f(it.next(), obj, true) == null) {
                    z = true;
                }
            }
            return z;
        }

        @Override // java.util.Collection, java.util.Set
        public final int hashCode() {
            Object it = iterator();
            int iHashCode = 0;
            while (((a) it).hasNext()) {
                iHashCode += ((h) it).next().hashCode();
            }
            return iHashCode;
        }

        @Override // java.util.Collection, java.util.Set
        public final boolean equals(Object obj) {
            if (!(obj instanceof Set)) {
                return false;
            }
            Set set = (Set) obj;
            if (set != this) {
                return containsAll(set) && set.containsAll(this);
            }
            return true;
        }

        @Override // java.util.Collection, java.lang.Iterable, java.util.Set, j$.util.Collection
        public final j$.util.Spliterator spliterator() {
            ConcurrentHashMap concurrentHashMap = this.a;
            long j = concurrentHashMap.j();
            k[] kVarArr = concurrentHashMap.a;
            int length = kVarArr == null ? 0 : kVarArr.length;
            return new i(kVarArr, length, 0, length, j < 0 ? 0L : j, 0);
        }

        @Override // java.lang.Iterable, j$.util.Collection, j$.lang.a
        public final void forEach(Consumer consumer) {
            consumer.getClass();
            k[] kVarArr = this.a.a;
            if (kVarArr == null) {
                return;
            }
            o oVar = new o(kVarArr, kVarArr.length, 0, kVarArr.length);
            while (true) {
                k kVarA = oVar.a();
                if (kVarA == null) {
                    return;
                } else {
                    consumer.accept(kVarA.b);
                }
            }
        }
    }
}
