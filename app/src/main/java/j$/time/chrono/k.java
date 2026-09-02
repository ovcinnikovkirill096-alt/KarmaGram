package j$.time.chrono;

import j$.time.Instant;
import j$.time.LocalDateTime;
import j$.time.ZoneId;
import java.util.List;
import java.util.Map;

public interface k extends Comparable {
    InterfaceC0163b A(j$.time.temporal.n nVar);

    ChronoLocalDateTime B(LocalDateTime localDateTime);

    InterfaceC0163b I(int i, int i2, int i3);

    InterfaceC0163b K(Map map, j$.time.format.E e);

    ChronoZonedDateTime L(Instant instant, ZoneId zoneId);

    boolean O(long j);

    boolean equals(Object obj);

    String getId();

    InterfaceC0163b h(long j);

    int hashCode();

    String l();

    InterfaceC0163b m(int i, int i2);

    j$.time.temporal.v q(j$.time.temporal.a aVar);

    List s();

    String toString();

    l u(int i);

    int v(l lVar, int i);
}
