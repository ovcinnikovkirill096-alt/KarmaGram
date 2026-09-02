package j$.time.chrono;

import j$.time.Instant;
import j$.time.ZoneId;
import j$.time.ZoneOffset;
import j$.time.chrono.InterfaceC0163b;

public interface ChronoZonedDateTime<D extends InterfaceC0163b> extends j$.time.temporal.m, Comparable<ChronoZonedDateTime<?>> {
    ZoneId C();

    long P();

    k a();

    j$.time.i b();

    InterfaceC0163b f();

    ZoneOffset g();

    ChronoLocalDateTime o();

    Instant toInstant();

    ChronoZonedDateTime w(ZoneId zoneId);
}
