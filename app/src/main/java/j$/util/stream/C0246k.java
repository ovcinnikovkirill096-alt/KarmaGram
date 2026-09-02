package j$.util.stream;

import j$.util.C0328w;
import j$.util.C0329x;
import j$.util.C0331z;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.function.Supplier;

/* JADX INFO: renamed from: j$.util.stream.k, reason: case insensitive filesystem */
public final /* synthetic */ class C0246k implements Supplier {
    public final /* synthetic */ int a;

    @Override // java.util.function.Supplier
    public final Object get() {
        switch (this.a) {
            case 0:
                return new C0328w();
            case 1:
                return new ArrayList();
            case 2:
                return new C0329x();
            case 3:
                return new HashSet();
            case 4:
                return new C0331z();
            case 5:
                return new StringBuilder();
            case 6:
                return new LinkedHashSet();
            case 7:
                return new double[4];
            case 8:
                return new double[3];
            case 9:
                return new H();
            case 10:
                return new I();
            case 11:
                return new J();
            case 12:
                return new K();
            case 13:
                return new long[2];
            default:
                return new long[2];
        }
    }
}
