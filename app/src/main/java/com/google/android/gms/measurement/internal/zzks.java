package com.google.android.gms.measurement.internal;

import j$.util.Objects;

final class zzks implements Runnable {
    final /* synthetic */ Boolean zza;
    final /* synthetic */ zzlj zzb;

    zzks(zzlj zzljVar, Boolean bool) {
        this.zza = bool;
        Objects.requireNonNull(zzljVar);
        this.zzb = zzljVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzb.zzaj(this.zza, true);
    }
}
