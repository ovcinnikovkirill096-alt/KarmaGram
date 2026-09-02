package com.google.android.gms.measurement.internal;

import j$.util.Objects;

final class zzkd implements Runnable {
    final /* synthetic */ String zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ Object zzc;
    final /* synthetic */ long zzd;
    final /* synthetic */ zzlj zze;

    zzkd(zzlj zzljVar, String str, String str2, Object obj, long j) {
        this.zza = str;
        this.zzb = str2;
        this.zzc = obj;
        this.zzd = j;
        Objects.requireNonNull(zzljVar);
        this.zze = zzljVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zze.zzN(this.zza, this.zzb, this.zzc, this.zzd);
    }
}
