package j$.time.chrono;

import j$.time.Instant;
import j$.time.ZoneId;
import j$.time.ZoneOffset;
import j$.time.chrono.InterfaceC0163b;

public interface ChronoLocalDateTime<D extends InterfaceC0163b> extends j$.time.temporal.m, j$.time.temporal.o, Comparable<ChronoLocalDateTime<?>> {
    /* JADX INFO: renamed from: H */
    int compareTo(ChronoLocalDateTime chronoLocalDateTime);

    k a();

    j$.time.i b();

    InterfaceC0163b f();

    Instant toInstant(ZoneOffset zoneOffset);

    ChronoZonedDateTime z(ZoneId zoneId);
}
