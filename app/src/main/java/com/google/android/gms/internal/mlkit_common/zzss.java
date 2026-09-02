package com.google.android.gms.internal.mlkit_common;

import com.chaquo.python.internal.Common;

public abstract class zzss {
    private static zzsr zza;

    public static synchronized zzsh zza(zzsb zzsbVar) {
        try {
            if (zza == null) {
                zza = new zzsr(null);
            }
        } catch (Throwable th) {
            throw th;
        }
        return (zzsh) zza.get(zzsbVar);
    }

    public static synchronized zzsh zzb(String str) {
        return zza(zzsb.zzd(Common.ABI_COMMON).zzd());
    }
}
