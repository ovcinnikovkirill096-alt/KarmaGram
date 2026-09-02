package j$.time.temporal;

import j$.time.Duration;

public enum i implements t {
    WEEK_BASED_YEARS("WeekBasedYears"),
    QUARTER_YEARS("QuarterYears");

    public final String a;

    static {
        Duration.ofSeconds(31556952L);
        Duration.ofSeconds(7889238L);
    }

    i(String str) {
        this.a = str;
    }

    @Override // j$.time.temporal.t
    public final m i(m mVar, long j) {
        int i = c.a[ordinal()];
        if (i == 1) {
            h hVar = j.c;
            return mVar.c(j$.com.android.tools.r8.a.P(mVar.i(hVar), j), hVar);
        }
        if (i == 2) {
            return mVar.d(j / 4, b.YEARS).d((j % 4) * 3, b.MONTHS);
        }
        throw new IllegalStateException("Unreachable");
    }

    @Override // java.lang.Enum
    public final String toString() {
        return this.a;
    }
}
