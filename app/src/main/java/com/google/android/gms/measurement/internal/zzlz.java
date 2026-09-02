package com.google.android.gms.measurement.internal;

import j$.util.Objects;

final class zzlz implements Runnable {
    final /* synthetic */ zzlu zza;
    final /* synthetic */ long zzb;
    final /* synthetic */ zzmb zzc;

    zzlz(zzmb zzmbVar, zzlu zzluVar, long j) {
        this.zza = zzluVar;
        this.zzb = j;
        Objects.requireNonNull(zzmbVar);
        this.zzc = zzmbVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzmb zzmbVar = this.zzc;
        zzmbVar.zzv(this.zza, false, this.zzb);
        zzmbVar.zza = null;
        zzmbVar.zzu.zzt().zzG(null);
    }
}
