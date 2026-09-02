package j$.util.concurrent;

public final class g extends k {
    public final k[] e;

    public g(k[] kVarArr) {
        super(-1, null, null);
        this.e = kVarArr;
    }

    @Override // j$.util.concurrent.k
    public final k a(int i, Object obj) {
        k kVarK;
        Object obj2;
        k[] kVarArr = this.e;
        while (true) {
            int length = kVarArr.length;
            if (length == 0 || (kVarK = ConcurrentHashMap.k(kVarArr, (length - 1) & i)) == null) {
                return null;
            }
            do {
                int i2 = kVarK.a;
                if (i2 == i && ((obj2 = kVarK.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                    return kVarK;
                }
                if (i2 < 0) {
                    if (kVarK instanceof g) {
                        kVarArr = ((g) kVarK).e;
                    } else {
                        return kVarK.a(i, obj);
                    }
                } else {
                    kVarK = kVarK.d;
                }
            } while (kVarK != null);
            return null;
        }
    }
}
