package j$.time.format;

import j$.time.ZoneId;
import j$.time.chrono.InterfaceC0163b;
import okhttp3.internal.url._UrlKt;

public final class x implements j$.time.temporal.n {
    public final /* synthetic */ InterfaceC0163b a;
    public final /* synthetic */ j$.time.temporal.n b;
    public final /* synthetic */ j$.time.chrono.k c;
    public final /* synthetic */ ZoneId d;

    @Override // j$.time.temporal.n
    public final /* synthetic */ int i(j$.time.temporal.r rVar) {
        return j$.time.temporal.s.a(this, rVar);
    }

    public x(InterfaceC0163b interfaceC0163b, j$.time.temporal.n nVar, j$.time.chrono.k kVar, ZoneId zoneId) {
        this.a = interfaceC0163b;
        this.b = nVar;
        this.c = kVar;
        this.d = zoneId;
    }

    @Override // j$.time.temporal.n
    public final boolean e(j$.time.temporal.r rVar) {
        InterfaceC0163b interfaceC0163b = this.a;
        if (interfaceC0163b != null && rVar.isDateBased()) {
            return interfaceC0163b.e(rVar);
        }
        return this.b.e(rVar);
    }

    @Override // j$.time.temporal.n
    public final j$.time.temporal.v k(j$.time.temporal.r rVar) {
        InterfaceC0163b interfaceC0163b = this.a;
        if (interfaceC0163b != null && rVar.isDateBased()) {
            return interfaceC0163b.k(rVar);
        }
        return this.b.k(rVar);
    }

    @Override // j$.time.temporal.n
    public final long D(j$.time.temporal.r rVar) {
        InterfaceC0163b interfaceC0163b = this.a;
        if (interfaceC0163b != null && rVar.isDateBased()) {
            return interfaceC0163b.D(rVar);
        }
        return this.b.D(rVar);
    }

    @Override // j$.time.temporal.n
    public final Object t(j$.time.e eVar) {
        if (eVar == j$.time.temporal.s.b) {
            return this.c;
        }
        if (eVar == j$.time.temporal.s.a) {
            return this.d;
        }
        if (eVar == j$.time.temporal.s.c) {
            return this.b.t(eVar);
        }
        return eVar.g(this);
    }

    public final String toString() {
        String str;
        String str2 = _UrlKt.FRAGMENT_ENCODE_SET;
        j$.time.chrono.k kVar = this.c;
        if (kVar != null) {
            str = " with chronology " + kVar;
        } else {
            str = _UrlKt.FRAGMENT_ENCODE_SET;
        }
        ZoneId zoneId = this.d;
        if (zoneId != null) {
            str2 = " with zone " + zoneId;
        }
        return this.b + str + str2;
    }
}
