package j$.time.format;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: renamed from: j$.time.format.a, reason: case insensitive filesystem */
public final class C0171a extends B {
    public final /* synthetic */ A d;

    public C0171a(A a) {
        this.d = a;
    }

    @Override // j$.time.format.B
    public final String b(j$.time.chrono.k kVar, j$.time.temporal.r rVar, long j, TextStyle textStyle, Locale locale) {
        return this.d.a(j, textStyle);
    }

    @Override // j$.time.format.B
    public final String c(j$.time.temporal.r rVar, long j, TextStyle textStyle, Locale locale) {
        return this.d.a(j, textStyle);
    }

    @Override // j$.time.format.B
    public final Iterator d(j$.time.chrono.k kVar, j$.time.temporal.r rVar, TextStyle textStyle, Locale locale) {
        List list = (List) ((HashMap) this.d.b).get(textStyle);
        if (list != null) {
            return list.iterator();
        }
        return null;
    }

    @Override // j$.time.format.B
    public final Iterator e(j$.time.temporal.r rVar, TextStyle textStyle, Locale locale) {
        List list = (List) ((HashMap) this.d.b).get(textStyle);
        if (list != null) {
            return list.iterator();
        }
        return null;
    }
}
