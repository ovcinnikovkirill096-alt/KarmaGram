package j$.time.format;

import j$.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class u {
    public static final j$.time.e h = new j$.time.e(1);
    public static final Map i;
    public u a;
    public final u b;
    public final List c;
    public final boolean d;
    public int e;
    public char f;
    public int g;

    static {
        HashMap map = new HashMap();
        i = map;
        map.put('G', j$.time.temporal.a.ERA);
        map.put('y', j$.time.temporal.a.YEAR_OF_ERA);
        map.put('u', j$.time.temporal.a.YEAR);
        j$.time.temporal.h hVar = j$.time.temporal.j.a;
        map.put('Q', hVar);
        map.put('q', hVar);
        j$.time.temporal.a aVar = j$.time.temporal.a.MONTH_OF_YEAR;
        map.put('M', aVar);
        map.put('L', aVar);
        map.put('D', j$.time.temporal.a.DAY_OF_YEAR);
        map.put('d', j$.time.temporal.a.DAY_OF_MONTH);
        map.put('F', j$.time.temporal.a.ALIGNED_DAY_OF_WEEK_IN_MONTH);
        j$.time.temporal.a aVar2 = j$.time.temporal.a.DAY_OF_WEEK;
        map.put('E', aVar2);
        map.put('c', aVar2);
        map.put('e', aVar2);
        map.put('a', j$.time.temporal.a.AMPM_OF_DAY);
        map.put('H', j$.time.temporal.a.HOUR_OF_DAY);
        map.put('k', j$.time.temporal.a.CLOCK_HOUR_OF_DAY);
        map.put('K', j$.time.temporal.a.HOUR_OF_AMPM);
        map.put('h', j$.time.temporal.a.CLOCK_HOUR_OF_AMPM);
        map.put('m', j$.time.temporal.a.MINUTE_OF_HOUR);
        map.put('s', j$.time.temporal.a.SECOND_OF_MINUTE);
        j$.time.temporal.a aVar3 = j$.time.temporal.a.NANO_OF_SECOND;
        map.put('S', aVar3);
        map.put('A', j$.time.temporal.a.MILLI_OF_DAY);
        map.put('n', aVar3);
        map.put('N', j$.time.temporal.a.NANO_OF_DAY);
        map.put('g', j$.time.temporal.l.a);
    }

    public u() {
        this.a = this;
        this.c = new ArrayList();
        this.g = -1;
        this.b = null;
        this.d = false;
    }

    public u(u uVar) {
        this.a = this;
        this.c = new ArrayList();
        this.g = -1;
        this.b = uVar;
        this.d = true;
    }

    public final void k(j$.time.temporal.r rVar) {
        Objects.requireNonNull(rVar, "field");
        j(new i(rVar, 1, 19, F.NORMAL));
    }

    public final void l(j$.time.temporal.r rVar, int i2) {
        Objects.requireNonNull(rVar, "field");
        if (i2 < 1 || i2 > 19) {
            throw new IllegalArgumentException("The width must be from 1 to 19 inclusive but was " + i2);
        }
        j(new i(rVar, i2, i2, F.NOT_NEGATIVE));
    }

    public final void m(j$.time.temporal.r rVar, int i2, int i3, F f) {
        if (i2 == i3 && f == F.NOT_NEGATIVE) {
            l(rVar, i3);
            return;
        }
        Objects.requireNonNull(rVar, "field");
        Objects.requireNonNull(f, "signStyle");
        if (i2 < 1 || i2 > 19) {
            throw new IllegalArgumentException("The minimum width must be from 1 to 19 inclusive but was " + i2);
        }
        if (i3 < 1 || i3 > 19) {
            throw new IllegalArgumentException("The maximum width must be from 1 to 19 inclusive but was " + i3);
        }
        if (i3 < i2) {
            throw new IllegalArgumentException("The maximum width must exceed or equal the minimum width but " + i3 + " < " + i2);
        }
        j(new i(rVar, i2, i3, f));
    }

    public final void j(i iVar) {
        i iVarD;
        u uVar = this.a;
        int i2 = uVar.g;
        if (i2 < 0) {
            uVar.g = c(iVar);
            return;
        }
        i iVar2 = (i) ((ArrayList) uVar.c).get(i2);
        int i3 = iVar.b;
        int i4 = iVar.c;
        if (i3 == i4 && iVar.d == F.NOT_NEGATIVE) {
            iVarD = iVar2.e(i4);
            c(iVar.d());
            this.a.g = i2;
        } else {
            iVarD = iVar2.d();
            this.a.g = c(iVar);
        }
        ((ArrayList) this.a.c).set(i2, iVarD);
    }

    public final void b(j$.time.temporal.a aVar, int i2, int i3, boolean z) {
        if (i2 == i3 && !z) {
            j(new C0176f(aVar, i2, i3, z));
        } else {
            c(new C0176f(aVar, i2, i3, z));
        }
    }

    public final void i(j$.time.temporal.r rVar, TextStyle textStyle) {
        Objects.requireNonNull(rVar, "field");
        Objects.requireNonNull(textStyle, "textStyle");
        c(new q(rVar, textStyle, B.c));
    }

    public final void h(j$.time.temporal.a aVar, Map map) {
        Objects.requireNonNull(aVar, "field");
        Objects.requireNonNull(map, "textLookup");
        LinkedHashMap linkedHashMap = new LinkedHashMap(map);
        TextStyle textStyle = TextStyle.FULL;
        c(new q(aVar, textStyle, new C0171a(new A(Collections.singletonMap(textStyle, linkedHashMap)))));
    }

    public final void g(String str, String str2) {
        c(new j(str, str2));
    }

    public final void f(TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "style");
        if (textStyle != TextStyle.FULL && textStyle != TextStyle.SHORT) {
            throw new IllegalArgumentException("Style must be either full or short");
        }
        c(new h(0, textStyle));
    }

    public final void d(char c) {
        c(new C0173c(c));
    }

    public final void e(String str) {
        Objects.requireNonNull(str, "literal");
        if (str.isEmpty()) {
            return;
        }
        if (str.length() == 1) {
            c(new C0173c(str.charAt(0)));
        } else {
            c(new h(1, str));
        }
    }

    public final void a(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        C0174d c0174d = dateTimeFormatter.a;
        if (c0174d.b) {
            c0174d = new C0174d(c0174d.a, false);
        }
        c(c0174d);
    }

    public final void o() {
        u uVar = this.a;
        uVar.g = -1;
        this.a = new u(uVar);
    }

    public final void n() {
        u uVar = this.a;
        if (uVar.b == null) {
            throw new IllegalStateException("Cannot call optionalEnd() as there was no previous call to optionalStart()");
        }
        if (((ArrayList) uVar.c).size() > 0) {
            u uVar2 = this.a;
            C0174d c0174d = new C0174d(uVar2.c, uVar2.d);
            this.a = this.a.b;
            c(c0174d);
            return;
        }
        this.a = this.a.b;
    }

    public final int c(InterfaceC0175e interfaceC0175e) {
        Objects.requireNonNull(interfaceC0175e, "pp");
        u uVar = this.a;
        int i2 = uVar.e;
        if (i2 > 0) {
            if (interfaceC0175e != null) {
                interfaceC0175e = new k(interfaceC0175e, i2, uVar.f);
            }
            uVar.e = 0;
            uVar.f = (char) 0;
        }
        ((ArrayList) uVar.c).add(interfaceC0175e);
        u uVar2 = this.a;
        uVar2.g = -1;
        return ((ArrayList) uVar2.c).size() - 1;
    }

    public final DateTimeFormatter p(E e, j$.time.chrono.k kVar) {
        return q(Locale.getDefault(), e, kVar);
    }

    public final DateTimeFormatter q(Locale locale, E e, j$.time.chrono.k kVar) {
        Objects.requireNonNull(locale, "locale");
        while (this.a.b != null) {
            n();
        }
        C0174d c0174d = new C0174d(this.c, false);
        C c = C.a;
        return new DateTimeFormatter(c0174d, locale, e, kVar);
    }
}
