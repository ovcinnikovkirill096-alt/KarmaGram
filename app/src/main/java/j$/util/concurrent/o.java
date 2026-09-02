package j$.util.concurrent;

public class o {
    public k[] a;
    public k b = null;
    public n c;
    public n d;
    public int e;
    public int f;
    public int g;
    public final int h;

    public o(k[] kVarArr, int i, int i2, int i3) {
        this.a = kVarArr;
        this.h = i;
        this.e = i2;
        this.f = i2;
        this.g = i3;
    }

    /* JADX WARN: Code duplicated, block: B:36:0x0063  */
    /* JADX WARN: Code duplicated, block: B:38:0x006c A[LOOP:1: B:34:0x005f->B:38:0x006c, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:57:0x0097 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:58:0x0084 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:59:0x008d A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:60:0x005f A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:61:0x009e A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:63:0x0006 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:65:0x0006 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:66:0x0006 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:70:0x0082 A[EDGE_INSN: B:70:0x0082->B:39:0x0082 BREAK  A[LOOP:1: B:34:0x005f->B:38:0x006c], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:71:0x0082 A[EDGE_INSN: B:71:0x0082->B:39:0x0082 BREAK  A[LOOP:1: B:34:0x005f->B:38:0x006c], SYNTHETIC] */
    public final k a() {
        k[] kVarArr;
        int length;
        int i;
        n nVar;
        int i2;
        int i3;
        int i4;
        int i5;
        k kVar = this.b;
        if (kVar != null) {
            kVar = kVar.d;
        }
        while (kVar == null) {
            if (this.f >= this.g || (kVarArr = this.a) == null || (length = kVarArr.length) <= (i = this.e) || i < 0) {
                this.b = null;
                return null;
            }
            k kVarK = ConcurrentHashMap.k(kVarArr, i);
            if (kVarK != null && kVarK.a < 0) {
                if (kVarK instanceof g) {
                    this.a = ((g) kVarK).e;
                    n nVar2 = this.d;
                    if (nVar2 == null) {
                        nVar2 = new n();
                    } else {
                        this.d = nVar2.d;
                    }
                    nVar2.c = kVarArr;
                    nVar2.a = length;
                    nVar2.b = i;
                    nVar2.d = this.c;
                    this.c = nVar2;
                    kVar = null;
                } else {
                    kVar = kVarK instanceof p ? ((p) kVarK).f : null;
                    if (this.c != null) {
                        while (true) {
                            nVar = this.c;
                            if (nVar != null) {
                                break;
                                break;
                            }
                            int i6 = this.e;
                            i3 = nVar.a;
                            i4 = i6 + i3;
                            this.e = i4;
                            if (i4 >= length) {
                                break;
                                break;
                            }
                            this.e = nVar.b;
                            this.a = nVar.c;
                            nVar.c = null;
                            n nVar3 = nVar.d;
                            nVar.d = this.d;
                            this.c = nVar3;
                            this.d = nVar;
                            length = i3;
                        }
                        if (nVar == null) {
                            i2 = this.e + this.h;
                            this.e = i2;
                            if (i2 >= length) {
                                int i7 = this.f + 1;
                                this.f = i7;
                                this.e = i7;
                            }
                        }
                    } else {
                        i5 = i + this.h;
                        this.e = i5;
                        if (i5 >= length) {
                            int i8 = this.f + 1;
                            this.f = i8;
                            this.e = i8;
                        }
                    }
                }
            } else {
                kVar = kVarK;
                if (this.c != null) {
                    while (true) {
                        nVar = this.c;
                        if (nVar != null) {
                            break;
                        }
                        int i9 = this.e;
                        i3 = nVar.a;
                        i4 = i9 + i3;
                        this.e = i4;
                        if (i4 >= length) {
                            break;
                        }
                        this.e = nVar.b;
                        this.a = nVar.c;
                        nVar.c = null;
                        n nVar4 = nVar.d;
                        nVar.d = this.d;
                        this.c = nVar4;
                        this.d = nVar;
                        length = i3;
                    }
                    if (nVar == null) {
                        i2 = this.e + this.h;
                        this.e = i2;
                        if (i2 >= length) {
                            int i10 = this.f + 1;
                            this.f = i10;
                            this.e = i10;
                        }
                    }
                } else {
                    i5 = i + this.h;
                    this.e = i5;
                    if (i5 >= length) {
                        int i11 = this.f + 1;
                        this.f = i11;
                        this.e = i11;
                    }
                }
            }
        }
        this.b = kVar;
        return kVar;
    }
}
