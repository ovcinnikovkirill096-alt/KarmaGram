package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

public final class zztx {
    private static zztx zza;

    private zztx() {
    }

    public static synchronized zztx zza() {
        try {
            if (zza == null) {
                zza = new zztx();
            }
        } catch (Throwable th) {
            throw th;
        }
        return zza;
    }
}
