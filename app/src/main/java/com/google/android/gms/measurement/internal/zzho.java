package com.google.android.gms.measurement.internal;

import j$.util.Objects;
import java.util.Map;

final class zzho implements com.google.android.gms.internal.measurement.zzo {
    final /* synthetic */ String zza;
    final /* synthetic */ zzht zzb;

    zzho(zzht zzhtVar, String str) {
        this.zza = str;
        Objects.requireNonNull(zzhtVar);
        this.zzb = zzhtVar;
    }

    @Override // com.google.android.gms.internal.measurement.zzo
    public final String zza(String str) {
        Map map = (Map) this.zzb.zzD().get(this.zza);
        if (map == null || !map.containsKey(str)) {
            return null;
        }
        return (String) map.get(str);
    }
}
