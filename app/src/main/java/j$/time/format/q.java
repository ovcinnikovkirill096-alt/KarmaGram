package j$.time.format;

import java.util.Iterator;
import java.util.Map;

public final class q implements InterfaceC0175e {
    public final j$.time.temporal.r a;
    public final TextStyle b;
    public final B c;
    public volatile i d;

    public q(j$.time.temporal.r rVar, TextStyle textStyle, B b) {
        this.a = rVar;
        this.b = textStyle;
        this.c = b;
    }

    @Override // j$.time.format.InterfaceC0175e
    public final boolean i(y yVar, StringBuilder sb) {
        String strC;
        Long lA = yVar.a(this.a);
        DateTimeFormatter dateTimeFormatter = yVar.b;
        if (lA == null) {
            return false;
        }
        j$.time.chrono.k kVar = (j$.time.chrono.k) yVar.a.t(j$.time.temporal.s.b);
        if (kVar == null || kVar == j$.time.chrono.r.c) {
            strC = this.c.c(this.a, lA.longValue(), this.b, dateTimeFormatter.b);
        } else {
            strC = this.c.b(kVar, this.a, lA.longValue(), this.b, dateTimeFormatter.b);
        }
        if (strC != null) {
            sb.append(strC);
            return true;
        }
        if (this.d == null) {
            this.d = new i(this.a, 1, 19, F.NORMAL);
        }
        return this.d.i(yVar, sb);
    }

    @Override // j$.time.format.InterfaceC0175e
    public final int j(v vVar, CharSequence charSequence, int i) {
        Iterator itE;
        B b = this.c;
        j$.time.temporal.r rVar = this.a;
        int length = charSequence.length();
        if (i >= 0 && i <= length) {
            boolean z = vVar.c;
            DateTimeFormatter dateTimeFormatter = vVar.a;
            TextStyle textStyle = z ? this.b : null;
            j$.time.chrono.k kVar = vVar.c().c;
            if (kVar == null && (kVar = vVar.a.e) == null) {
                kVar = j$.time.chrono.r.c;
            }
            j$.time.chrono.k kVar2 = kVar;
            if (kVar2 == null || kVar2 == j$.time.chrono.r.c) {
                itE = b.e(rVar, textStyle, dateTimeFormatter.b);
            } else {
                itE = b.d(kVar2, rVar, textStyle, dateTimeFormatter.b);
            }
            Iterator it = itE;
            if (it != null) {
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String str = (String) entry.getKey();
                    if (vVar.g(str, 0, charSequence, i, str.length())) {
                        return vVar.f(this.a, ((Long) entry.getValue()).longValue(), i, str.length() + i);
                    }
                }
                if (rVar == j$.time.temporal.a.ERA && !vVar.c) {
                    for (j$.time.chrono.l lVar : kVar2.s()) {
                        String string = lVar.toString();
                        if (vVar.g(string, 0, charSequence, i, string.length())) {
                            return vVar.f(this.a, lVar.getValue(), i, string.length() + i);
                        }
                    }
                }
                if (vVar.c) {
                    return ~i;
                }
            }
            if (this.d == null) {
                this.d = new i(this.a, 1, 19, F.NORMAL);
            }
            return this.d.j(vVar, charSequence, i);
        }
        throw new IndexOutOfBoundsException();
    }

    public final String toString() {
        TextStyle textStyle = TextStyle.FULL;
        j$.time.temporal.r rVar = this.a;
        TextStyle textStyle2 = this.b;
        if (textStyle2 == textStyle) {
            return "Text(" + rVar + ")";
        }
        return "Text(" + rVar + "," + textStyle2 + ")";
    }
}
