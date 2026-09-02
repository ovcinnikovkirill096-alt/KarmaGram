package com.google.android.gms.internal.mlkit_common;

public final class zzsv {
    private static zzsv zza;

    private zzsv() {
    }

    public static synchronized zzsv zza() {
        try {
            if (zza == null) {
                zza = new zzsv();
            }
        } catch (Throwable th) {
            throw th;
        }
        return zza;
    }

    public static void zzb() {
        zzsu.zza();
    }
}
