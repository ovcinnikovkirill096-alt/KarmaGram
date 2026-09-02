package j$.util.concurrent;

import java.util.concurrent.locks.LockSupport;

public final class p extends k {
    public static final j$.sun.misc.a h;
    public static final long i;
    public q e;
    public volatile q f;
    public volatile Thread g;
    volatile int lockState;

    static {
        j$.sun.misc.a aVar = j$.sun.misc.a.b;
        h = aVar;
        i = aVar.h(p.class, "lockState");
    }

    public static int i(Object obj, Object obj2) {
        int iCompareTo;
        if (obj == null || obj2 == null || (iCompareTo = obj.getClass().getName().compareTo(obj2.getClass().getName())) == 0) {
            return System.identityHashCode(obj) <= System.identityHashCode(obj2) ? -1 : 1;
        }
        return iCompareTo;
    }

    /* JADX WARN: Code duplicated, block: B:25:0x004b A[PHI: r7
  0x004b: PHI (r7v3 java.lang.Class<?>) = (r7v2 java.lang.Class<?>), (r7v4 java.lang.Class<?>) binds: [B:24:0x0049, B:16:0x0033] A[DONT_GENERATE, DONT_INLINE]] */
    public p(q qVar) {
        int i2;
        super(-2, null, null);
        this.f = qVar;
        q qVar2 = null;
        while (qVar != null) {
            q qVar3 = (q) qVar.d;
            qVar.g = null;
            qVar.f = null;
            if (qVar2 == null) {
                qVar.e = null;
                qVar.i = false;
            } else {
                Object obj = qVar.b;
                int i3 = qVar.a;
                q qVar4 = qVar2;
                Class<?> clsC = null;
                while (true) {
                    Object obj2 = qVar4.b;
                    int i4 = qVar4.a;
                    if (i4 > i3) {
                        i2 = -1;
                    } else if (i4 < i3) {
                        i2 = 1;
                    } else if (clsC != null || (clsC = ConcurrentHashMap.c(obj)) != null) {
                        int i5 = ConcurrentHashMap.g;
                        int iCompareTo = (obj2 == null || obj2.getClass() != clsC) ? 0 : ((Comparable) obj).compareTo(obj2);
                        if (iCompareTo == 0) {
                            i2 = i(obj, obj2);
                        } else {
                            i2 = iCompareTo;
                        }
                    } else {
                        i2 = i(obj, obj2);
                    }
                    q qVar5 = i2 <= 0 ? qVar4.f : qVar4.g;
                    if (qVar5 == null) {
                        break;
                    } else {
                        qVar4 = qVar5;
                    }
                }
                qVar.e = qVar4;
                if (i2 <= 0) {
                    qVar4.f = qVar;
                } else {
                    qVar4.g = qVar;
                }
                qVar = c(qVar2, qVar);
            }
            qVar2 = qVar;
            qVar = qVar3;
        }
        this.e = qVar2;
    }

    public final void d() {
        if (h.c(this, i, 0, 1)) {
            return;
        }
        boolean z = false;
        while (true) {
            int i2 = this.lockState;
            if ((i2 & (-3)) == 0) {
                if (h.c(this, i, i2, 1)) {
                    break;
                }
            } else if ((i2 & 2) == 0) {
                if (h.c(this, i, i2, i2 | 2)) {
                    this.g = Thread.currentThread();
                    z = true;
                }
            } else if (z) {
                LockSupport.park(this);
            }
        }
        if (z) {
            this.g = null;
        }
    }

    @Override // j$.util.concurrent.k
    public final k a(int i2, Object obj) {
        Object obj2;
        Thread thread;
        k kVar = this.f;
        while (true) {
            q qVarB = null;
            if (kVar == null) {
                return null;
            }
            int i3 = this.lockState;
            if ((i3 & 3) != 0) {
                if (kVar.a == i2 && ((obj2 = kVar.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                    return kVar;
                }
                kVar = kVar.d;
            } else if (h.c(this, i, i3, i3 + 4)) {
                try {
                    q qVar = this.e;
                    if (qVar != null) {
                        qVarB = qVar.b(i2, obj, null);
                    }
                    return qVarB;
                } finally {
                    if (h.e(this, i) == 6 && (thread = this.g) != null) {
                        LockSupport.unpark(thread);
                    }
                }
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:45:0x0072  */
    /* JADX WARN: Code duplicated, block: B:46:0x0075  */
    /* JADX WARN: Code duplicated, block: B:50:0x0087  */
    /* JADX WARN: Code duplicated, block: B:52:0x008b  */
    /* JADX WARN: Code duplicated, block: B:53:0x008e  */
    /* JADX WARN: Code duplicated, block: B:56:0x0094  */
    /* JADX WARN: Code duplicated, block: B:58:0x0097  */
    /* JADX WARN: Code duplicated, block: B:65:0x00a9 A[LOOP:0: B:3:0x0007->B:65:0x00a9, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:74:0x0079 A[SYNTHETIC] */
    public final q e(int i2, Object obj, Object obj2) {
        int i3;
        int i4;
        q qVarB;
        q qVarB2;
        q qVar;
        q qVar2;
        q qVar3;
        q qVar4 = this.e;
        Class<?> clsC = null;
        boolean z = false;
        while (qVar4 != null) {
            int i5 = qVar4.a;
            if (i5 > i2) {
                i4 = -1;
            } else {
                if (i5 < i2) {
                    i3 = 1;
                } else {
                    Object obj3 = qVar4.b;
                    if (obj3 == obj || (obj3 != null && obj.equals(obj3))) {
                        return qVar4;
                    }
                    if (clsC != null || (clsC = ConcurrentHashMap.c(obj)) != null) {
                        int i6 = ConcurrentHashMap.g;
                        int iCompareTo = (obj3 == null || obj3.getClass() != clsC) ? 0 : ((Comparable) obj).compareTo(obj3);
                        if (iCompareTo != 0) {
                            i3 = iCompareTo;
                        }
                    }
                    if (!z) {
                        q qVar5 = qVar4.f;
                        if (qVar5 != null && (qVarB2 = qVar5.b(i2, obj, clsC)) != null) {
                            return qVarB2;
                        }
                        q qVar6 = qVar4.g;
                        if (qVar6 != null && (qVarB = qVar6.b(i2, obj, clsC)) != null) {
                            return qVarB;
                        }
                        z = true;
                    }
                    i4 = i(obj, obj3);
                }
                if (i3 <= 0) {
                    qVar = qVar4.f;
                } else {
                    qVar = qVar4.g;
                }
                if (qVar == null) {
                    qVar2 = this.f;
                    qVar3 = new q(i2, obj, obj2, qVar2, qVar4);
                    this.f = qVar3;
                    if (qVar2 != null) {
                        qVar2.h = qVar3;
                    }
                    if (i3 <= 0) {
                        qVar4.f = qVar3;
                    } else {
                        qVar4.g = qVar3;
                    }
                    if (!qVar4.i) {
                        qVar3.i = true;
                        return null;
                    }
                    d();
                    try {
                        this.e = c(this.e, qVar3);
                        return null;
                    } finally {
                        this.lockState = 0;
                    }
                }
                qVar4 = qVar;
            }
            i3 = i4;
            if (i3 <= 0) {
                qVar = qVar4.f;
            } else {
                qVar = qVar4.g;
            }
            if (qVar == null) {
                qVar2 = this.f;
                qVar3 = new q(i2, obj, obj2, qVar2, qVar4);
                this.f = qVar3;
                if (qVar2 != null) {
                    qVar2.h = qVar3;
                }
                if (i3 <= 0) {
                    qVar4.f = qVar3;
                } else {
                    qVar4.g = qVar3;
                }
                if (!qVar4.i) {
                    qVar3.i = true;
                    return null;
                }
                d();
                this.e = c(this.e, qVar3);
                return null;
            }
            qVar4 = qVar;
        }
        q qVar7 = new q(i2, obj, obj2, null, null);
        this.e = qVar7;
        this.f = qVar7;
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:57:0x008e A[PHI: r0
  0x008e: PHI (r0v4 j$.util.concurrent.q) = (r0v3 j$.util.concurrent.q), (r0v12 j$.util.concurrent.q) binds: [B:55:0x008a, B:51:0x0083] A[DONT_GENERATE, DONT_INLINE]] */
    public final boolean f(q qVar) {
        q qVar2;
        q qVar3;
        q qVar4 = (q) qVar.d;
        q qVar5 = qVar.h;
        if (qVar5 == null) {
            this.f = qVar4;
        } else {
            qVar5.d = qVar4;
        }
        if (qVar4 != null) {
            qVar4.h = qVar5;
        }
        if (this.f == null) {
            this.e = null;
            return true;
        }
        q qVarB = this.e;
        if (qVarB == null || qVarB.g == null || (qVar2 = qVarB.f) == null || qVar2.f == null) {
            return true;
        }
        d();
        try {
            q qVar6 = qVar.f;
            q qVar7 = qVar.g;
            if (qVar6 != null && qVar7 != null) {
                q qVar8 = qVar7;
                while (true) {
                    q qVar9 = qVar8.f;
                    if (qVar9 == null) {
                        break;
                    }
                    qVar8 = qVar9;
                }
                boolean z = qVar8.i;
                qVar8.i = qVar.i;
                qVar.i = z;
                q qVar10 = qVar8.g;
                q qVar11 = qVar.e;
                if (qVar8 == qVar7) {
                    qVar.e = qVar8;
                    qVar8.g = qVar;
                } else {
                    q qVar12 = qVar8.e;
                    qVar.e = qVar12;
                    if (qVar12 != null) {
                        if (qVar8 == qVar12.f) {
                            qVar12.f = qVar;
                        } else {
                            qVar12.g = qVar;
                        }
                    }
                    qVar8.g = qVar7;
                    qVar7.e = qVar8;
                }
                qVar.f = null;
                qVar.g = qVar10;
                if (qVar10 != null) {
                    qVar10.e = qVar;
                }
                qVar8.f = qVar6;
                qVar6.e = qVar8;
                qVar8.e = qVar11;
                if (qVar11 == null) {
                    qVarB = qVar8;
                } else if (qVar == qVar11.f) {
                    qVar11.f = qVar8;
                } else {
                    qVar11.g = qVar8;
                }
                if (qVar10 != null) {
                    qVar6 = qVar10;
                } else {
                    qVar6 = qVar;
                }
            } else if (qVar6 == null) {
                if (qVar7 != null) {
                    qVar6 = qVar7;
                } else {
                    qVar6 = qVar;
                }
            }
            if (qVar6 != qVar) {
                q qVar13 = qVar.e;
                qVar6.e = qVar13;
                if (qVar13 == null) {
                    qVarB = qVar6;
                } else if (qVar == qVar13.f) {
                    qVar13.f = qVar6;
                } else {
                    qVar13.g = qVar6;
                }
                qVar.e = null;
                qVar.g = null;
                qVar.f = null;
            }
            if (!qVar.i) {
                qVarB = b(qVarB, qVar6);
            }
            this.e = qVarB;
            if (qVar == qVar6 && (qVar3 = qVar.e) != null) {
                if (qVar == qVar3.f) {
                    qVar3.f = null;
                } else if (qVar == qVar3.g) {
                    qVar3.g = null;
                }
                qVar.e = null;
            }
            return false;
        } finally {
            this.lockState = 0;
        }
    }

    public static q g(q qVar, q qVar2) {
        q qVar3;
        if (qVar2 != null && (qVar3 = qVar2.g) != null) {
            q qVar4 = qVar3.f;
            qVar2.g = qVar4;
            if (qVar4 != null) {
                qVar4.e = qVar2;
            }
            q qVar5 = qVar2.e;
            qVar3.e = qVar5;
            if (qVar5 == null) {
                qVar3.i = false;
                qVar = qVar3;
            } else if (qVar5.f == qVar2) {
                qVar5.f = qVar3;
            } else {
                qVar5.g = qVar3;
            }
            qVar3.f = qVar2;
            qVar2.e = qVar3;
        }
        return qVar;
    }

    public static q h(q qVar, q qVar2) {
        q qVar3;
        if (qVar2 != null && (qVar3 = qVar2.f) != null) {
            q qVar4 = qVar3.g;
            qVar2.f = qVar4;
            if (qVar4 != null) {
                qVar4.e = qVar2;
            }
            q qVar5 = qVar2.e;
            qVar3.e = qVar5;
            if (qVar5 == null) {
                qVar3.i = false;
                qVar = qVar3;
            } else if (qVar5.g == qVar2) {
                qVar5.g = qVar3;
            } else {
                qVar5.f = qVar3;
            }
            qVar3.g = qVar2;
            qVar2.e = qVar3;
        }
        return qVar;
    }

    public static q c(q qVar, q qVar2) {
        q qVar3;
        qVar2.i = true;
        while (true) {
            q qVar4 = qVar2.e;
            if (qVar4 == null) {
                qVar2.i = false;
                return qVar2;
            }
            if (!qVar4.i || (qVar3 = qVar4.e) == null) {
                return qVar;
            }
            q qVar5 = qVar3.f;
            if (qVar4 == qVar5) {
                q qVar6 = qVar3.g;
                if (qVar6 != null && qVar6.i) {
                    qVar6.i = false;
                    qVar4.i = false;
                    qVar3.i = true;
                    qVar2 = qVar3;
                } else {
                    if (qVar2 == qVar4.g) {
                        qVar = g(qVar, qVar4);
                        q qVar7 = qVar4.e;
                        qVar3 = qVar7 == null ? null : qVar7.e;
                        qVar4 = qVar7;
                        qVar2 = qVar4;
                    }
                    if (qVar4 != null) {
                        qVar4.i = false;
                        if (qVar3 != null) {
                            qVar3.i = true;
                            qVar = h(qVar, qVar3);
                        }
                    }
                }
            } else if (qVar5 != null && qVar5.i) {
                qVar5.i = false;
                qVar4.i = false;
                qVar3.i = true;
                qVar2 = qVar3;
            } else {
                if (qVar2 == qVar4.f) {
                    qVar = h(qVar, qVar4);
                    q qVar8 = qVar4.e;
                    qVar3 = qVar8 == null ? null : qVar8.e;
                    qVar4 = qVar8;
                    qVar2 = qVar4;
                }
                if (qVar4 != null) {
                    qVar4.i = false;
                    if (qVar3 != null) {
                        qVar3.i = true;
                        qVar = g(qVar, qVar3);
                    }
                }
            }
        }
    }

    public static q b(q qVar, q qVar2) {
        while (qVar2 != null && qVar2 != qVar) {
            q qVar3 = qVar2.e;
            if (qVar3 == null) {
                qVar2.i = false;
                return qVar2;
            }
            if (qVar2.i) {
                qVar2.i = false;
                return qVar;
            }
            q qVar4 = qVar3.f;
            if (qVar4 == qVar2) {
                q qVar5 = qVar3.g;
                if (qVar5 != null && qVar5.i) {
                    qVar5.i = false;
                    qVar3.i = true;
                    qVar = g(qVar, qVar3);
                    qVar3 = qVar2.e;
                    qVar5 = qVar3 == null ? null : qVar3.g;
                }
                if (qVar5 != null) {
                    q qVar6 = qVar5.f;
                    q qVar7 = qVar5.g;
                    if ((qVar7 == null || !qVar7.i) && (qVar6 == null || !qVar6.i)) {
                        qVar5.i = true;
                    } else {
                        if (qVar7 == null || !qVar7.i) {
                            if (qVar6 != null) {
                                qVar6.i = false;
                            }
                            qVar5.i = true;
                            qVar = h(qVar, qVar5);
                            qVar3 = qVar2.e;
                            qVar5 = qVar3 != null ? qVar3.g : null;
                        }
                        if (qVar5 != null) {
                            qVar5.i = qVar3 == null ? false : qVar3.i;
                            q qVar8 = qVar5.g;
                            if (qVar8 != null) {
                                qVar8.i = false;
                            }
                        }
                        if (qVar3 != null) {
                            qVar3.i = false;
                            qVar = g(qVar, qVar3);
                        }
                        qVar2 = qVar;
                    }
                }
                qVar2 = qVar3;
            } else {
                if (qVar4 != null && qVar4.i) {
                    qVar4.i = false;
                    qVar3.i = true;
                    qVar = h(qVar, qVar3);
                    qVar3 = qVar2.e;
                    qVar4 = qVar3 == null ? null : qVar3.f;
                }
                if (qVar4 != null) {
                    q qVar9 = qVar4.f;
                    q qVar10 = qVar4.g;
                    if ((qVar9 == null || !qVar9.i) && (qVar10 == null || !qVar10.i)) {
                        qVar4.i = true;
                    } else {
                        if (qVar9 == null || !qVar9.i) {
                            if (qVar10 != null) {
                                qVar10.i = false;
                            }
                            qVar4.i = true;
                            qVar = g(qVar, qVar4);
                            qVar3 = qVar2.e;
                            qVar4 = qVar3 != null ? qVar3.f : null;
                        }
                        if (qVar4 != null) {
                            qVar4.i = qVar3 == null ? false : qVar3.i;
                            q qVar11 = qVar4.f;
                            if (qVar11 != null) {
                                qVar11.i = false;
                            }
                        }
                        if (qVar3 != null) {
                            qVar3.i = false;
                            qVar = h(qVar, qVar3);
                        }
                        qVar2 = qVar;
                    }
                }
                qVar2 = qVar3;
            }
        }
        return qVar;
    }
}
