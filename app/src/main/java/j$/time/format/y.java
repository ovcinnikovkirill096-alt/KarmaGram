package j$.time.format;

import j$.time.ZoneId;
import j$.time.chrono.InterfaceC0163b;
import j$.util.Objects;

public final class y {
    public final j$.time.temporal.n a;
    public final DateTimeFormatter b;
    public int c;

    public y(j$.time.temporal.n nVar, DateTimeFormatter dateTimeFormatter) {
        j$.time.chrono.k kVar = dateTimeFormatter.e;
        if (kVar != null) {
            j$.time.chrono.k kVar2 = (j$.time.chrono.k) nVar.t(j$.time.temporal.s.b);
            ZoneId zoneId = (ZoneId) nVar.t(j$.time.temporal.s.a);
            InterfaceC0163b interfaceC0163bA = null;
            kVar = Objects.equals(kVar, kVar2) ? null : kVar;
            Objects.equals(null, zoneId);
            if (kVar != null) {
                j$.time.chrono.k kVar3 = kVar != null ? kVar : kVar2;
                if (kVar != null) {
                    if (nVar.e(j$.time.temporal.a.EPOCH_DAY)) {
                        interfaceC0163bA = kVar3.A(nVar);
                    } else if (kVar != j$.time.chrono.r.c || kVar2 != null) {
                        for (j$.time.temporal.a aVar : j$.time.temporal.a.values()) {
                            if (aVar.isDateBased() && nVar.e(aVar)) {
                                throw new j$.time.b("Unable to apply override chronology '" + kVar + "' because the temporal object being formatted contains date fields but does not represent a whole date: " + nVar);
                            }
                        }
                    }
                }
                nVar = new x(interfaceC0163bA, nVar, kVar3, zoneId);
            }
        }
        this.a = nVar;
        this.b = dateTimeFormatter;
    }

    public final Object b(j$.time.e eVar) {
        j$.time.temporal.n nVar = this.a;
        Object objT = nVar.t(eVar);
        if (objT != null || this.c != 0) {
            return objT;
        }
        throw new j$.time.b("Unable to extract " + eVar + " from temporal " + nVar);
    }

    public final Long a(j$.time.temporal.r rVar) {
        int i = this.c;
        j$.time.temporal.n nVar = this.a;
        if (i <= 0 || nVar.e(rVar)) {
            return Long.valueOf(nVar.D(rVar));
        }
        return null;
    }

    public final String toString() {
        return this.a.toString();
    }
}
