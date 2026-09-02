package com.google.android.gms.internal.clearcut;

public abstract class zzhb extends zzfu implements Cloneable {
    private static volatile zzhb[] zzbkd;

    public static zzhb[] zzge() {
        if (zzbkd == null) {
            synchronized (zzfy.zzrr) {
                try {
                    if (zzbkd == null) {
                        zzbkd = new zzhb[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return zzbkd;
    }
}
