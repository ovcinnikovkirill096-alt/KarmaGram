package com.google.android.gms.internal.stats;

import java.io.Closeable;

public final class zzb implements Closeable {
    private static final zzb zza = new zzb(false, null);

    private zzb(boolean z, zzd zzdVar) {
    }

    public static zzb zza(boolean z, zzc zzcVar) {
        return zza;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
    }
}
