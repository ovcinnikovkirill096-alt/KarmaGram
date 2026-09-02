package j$.time.temporal;

import j$.time.format.D;
import j$.time.format.E;
import java.util.Map;

public enum a implements r {
    NANO_OF_SECOND("NanoOfSecond", v.f(0, 999999999)),
    NANO_OF_DAY("NanoOfDay", v.f(0, 86399999999999L)),
    MICRO_OF_SECOND("MicroOfSecond", v.f(0, 999999)),
    MICRO_OF_DAY("MicroOfDay", v.f(0, 86399999999L)),
    MILLI_OF_SECOND("MilliOfSecond", v.f(0, 999)),
    MILLI_OF_DAY("MilliOfDay", v.f(0, 86399999)),
    SECOND_OF_MINUTE("SecondOfMinute", v.f(0, 59), 0),
    SECOND_OF_DAY("SecondOfDay", v.f(0, 86399)),
    MINUTE_OF_HOUR("MinuteOfHour", v.f(0, 59), 0),
    MINUTE_OF_DAY("MinuteOfDay", v.f(0, 1439)),
    HOUR_OF_AMPM("HourOfAmPm", v.f(0, 11)),
    CLOCK_HOUR_OF_AMPM("ClockHourOfAmPm", v.f(1, 12)),
    HOUR_OF_DAY("HourOfDay", v.f(0, 23), 0),
    CLOCK_HOUR_OF_DAY("ClockHourOfDay", v.f(1, 24)),
    AMPM_OF_DAY("AmPmOfDay", v.f(0, 1), 0),
    DAY_OF_WEEK("DayOfWeek", v.f(1, 7), 0),
    ALIGNED_DAY_OF_WEEK_IN_MONTH("AlignedDayOfWeekInMonth", v.f(1, 7)),
    ALIGNED_DAY_OF_WEEK_IN_YEAR("AlignedDayOfWeekInYear", v.f(1, 7)),
    DAY_OF_MONTH("DayOfMonth", v.g(1, 28, 31), 0),
    DAY_OF_YEAR("DayOfYear", v.g(1, 365, 366)),
    EPOCH_DAY("EpochDay", v.f(-365243219162L, 365241780471L)),
    ALIGNED_WEEK_OF_MONTH("AlignedWeekOfMonth", v.g(1, 4, 5)),
    ALIGNED_WEEK_OF_YEAR("AlignedWeekOfYear", v.f(1, 53)),
    MONTH_OF_YEAR("MonthOfYear", v.f(1, 12), 0),
    PROLEPTIC_MONTH("ProlepticMonth", v.f(-11999999988L, 11999999999L)),
    YEAR_OF_ERA("YearOfEra", v.g(1, 999999999, 1000000000)),
    YEAR("Year", v.f(-999999999, 999999999), 0),
    ERA("Era", v.f(0, 1), 0),
    INSTANT_SECONDS("InstantSeconds", v.f(Long.MIN_VALUE, Long.MAX_VALUE)),
    OFFSET_SECONDS("OffsetSeconds", v.f(-64800, 64800));

    public final String a;
    public final v b;

    @Override // j$.time.temporal.r
    public final /* synthetic */ n k(Map map, D d, E e) {
        return null;
    }

    static {
        b bVar = b.NANOS;
    }

    a(String str, v vVar) {
        this.a = str;
        this.b = vVar;
    }

    a(String str, v vVar, int i) {
        this.a = str;
        this.b = vVar;
    }

    public final void D(long j) {
        this.b.b(j, this);
    }

    @Override // j$.time.temporal.r
    public final v n() {
        return this.b;
    }

    @Override // j$.time.temporal.r
    public final boolean isDateBased() {
        return ordinal() >= DAY_OF_WEEK.ordinal() && ordinal() <= ERA.ordinal();
    }

    public final boolean Q() {
        return ordinal() < DAY_OF_WEEK.ordinal();
    }

    @Override // j$.time.temporal.r
    public final boolean i(n nVar) {
        return nVar.e(this);
    }

    @Override // j$.time.temporal.r
    public final v j(n nVar) {
        return nVar.k(this);
    }

    @Override // j$.time.temporal.r
    public final long t(n nVar) {
        return nVar.D(this);
    }

    @Override // j$.time.temporal.r
    public final m y(m mVar, long j) {
        return mVar.c(j, this);
    }

    @Override // java.lang.Enum
    public final String toString() {
        return this.a;
    }
}
