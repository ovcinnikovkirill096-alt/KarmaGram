package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import j$.util.Objects;

final class zzmz implements Runnable {
    final /* synthetic */ ComponentName zza;
    final /* synthetic */ zznf zzb;

    zzmz(zznf zznfVar, ComponentName componentName) {
        this.zza = componentName;
        Objects.requireNonNull(zznfVar);
        this.zzb = zznfVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzb.zza.zzW(this.zza);
    }
}
