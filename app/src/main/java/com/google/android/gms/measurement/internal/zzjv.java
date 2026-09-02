package com.google.android.gms.measurement.internal;

import j$.util.Objects;
import java.util.concurrent.Executor;

final class zzjv implements Executor {
    final /* synthetic */ zzlj zza;

    zzjv(zzlj zzljVar) {
        Objects.requireNonNull(zzljVar);
        this.zza = zzljVar;
    }

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        this.zza.zzu.zzaW().zzj(runnable);
    }
}
