package com.google.android.recaptcha.internal;

public final class zzbr {
    public static final zzbr zza = new zzbr();

    private zzbr() {
    }

    public static final zzp zza(int i) {
        if (i == 403) {
            return new zzp(zzn.zzl, zzl.zzV, null);
        }
        if (i != 404) {
            return i != 503 ? new zzp(zzn.zzc, zzl.zzW, null) : new zzp(zzn.zzl, zzl.zzV, null);
        }
        return new zzp(zzn.zze, zzl.zzs, null);
    }
}
