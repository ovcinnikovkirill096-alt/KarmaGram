package com.google.android.gms.measurement.internal;

import j$.util.Objects;

final class zzha implements Runnable {
    final /* synthetic */ boolean zza;
    final /* synthetic */ zzhb zzb;

    zzha(zzhb zzhbVar, boolean z) {
        this.zza = z;
        Objects.requireNonNull(zzhbVar);
        this.zzb = zzhbVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzb.zzc().zzas(this.zza);
    }
}
