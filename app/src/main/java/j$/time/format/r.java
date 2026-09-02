package j$.time.format;

import j$.time.DayOfWeek;
import j$.util.Objects;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.Calendar;
import java.util.Locale;

public final class r extends i {
    public final char g;
    public final int h;

    @Override // j$.time.format.i, j$.time.format.InterfaceC0175e
    public final int j(v vVar, CharSequence charSequence, int i) {
        return f(vVar.a.b).j(vVar, charSequence, i);
    }

    @Override // j$.time.format.i, j$.time.format.InterfaceC0175e
    public final boolean i(y yVar, StringBuilder sb) {
        return f(yVar.b.b).i(yVar, sb);
    }

    public r(char c, int i, int i2, int i3, int i4) {
        super(null, i2, i3, F.NOT_NEGATIVE, i4);
        this.g = c;
        this.h = i;
    }

    @Override // j$.time.format.i
    public final i d() {
        if (this.e == -1) {
            return this;
        }
        return new r(this.g, this.h, this.b, this.c, -1);
    }

    @Override // j$.time.format.i
    public final i e(int i) {
        return new r(this.g, this.h, this.b, this.c, this.e + i);
    }

    public final i f(Locale locale) {
        j$.time.temporal.w wVar;
        ConcurrentHashMap concurrentHashMap = j$.time.temporal.x.g;
        Objects.requireNonNull(locale, "locale");
        Calendar calendar = Calendar.getInstance(new Locale(locale.getLanguage(), locale.getCountry()));
        j$.time.temporal.x xVarA = j$.time.temporal.x.a(DayOfWeek.a[((((int) (((long) (calendar.getFirstDayOfWeek() - 1)) % 7)) + 7) + DayOfWeek.SUNDAY.ordinal()) % 7], calendar.getMinimalDaysInFirstWeek());
        char c = this.g;
        if (c == 'W') {
            wVar = xVarA.d;
        } else {
            if (c == 'Y') {
                j$.time.temporal.w wVar2 = xVarA.f;
                int i = this.h;
                if (i == 2) {
                    return new o(wVar2, 2, 2, o.h, this.e);
                }
                return new i(wVar2, i, 19, i < 4 ? F.NORMAL : F.EXCEEDS_PAD, this.e);
            }
            if (c == 'c' || c == 'e') {
                wVar = xVarA.c;
            } else {
                if (c != 'w') {
                    throw new IllegalStateException("unreachable");
                }
                wVar = xVarA.e;
            }
        }
        return new i(wVar, this.b, this.c, F.NOT_NEGATIVE, this.e);
    }

    @Override // j$.time.format.i
    public final String toString() {
        StringBuilder sb = new StringBuilder(30);
        sb.append("Localized(");
        int i = this.h;
        char c = this.g;
        if (c != 'Y') {
            if (c == 'W') {
                sb.append("WeekOfMonth");
            } else if (c == 'c' || c == 'e') {
                sb.append("DayOfWeek");
            } else if (c == 'w') {
                sb.append("WeekOfWeekBasedYear");
            }
            sb.append(",");
            sb.append(i);
        } else if (i == 1) {
            sb.append("WeekBasedYear");
        } else if (i == 2) {
            sb.append("ReducedValue(WeekBasedYear,2,2,2000-01-01)");
        } else {
            sb.append("WeekBasedYear,");
            sb.append(i);
            sb.append(",19,");
            sb.append(i < 4 ? F.NORMAL : F.EXCEEDS_PAD);
        }
        sb.append(")");
        return sb.toString();
    }
}
