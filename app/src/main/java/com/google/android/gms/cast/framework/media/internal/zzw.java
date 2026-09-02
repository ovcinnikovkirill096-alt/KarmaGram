package com.google.android.gms.cast.framework.media.internal;

import android.os.RemoteException;
import com.google.android.gms.cast.framework.media.NotificationOptions;
import com.google.android.gms.cast.internal.Logger;
import java.util.List;

public abstract class zzw {
    private static final Logger zza = new Logger("MediaSessionUtils");

    public static int zza(NotificationOptions notificationOptions, long j) {
        int forwardDrawableResId = notificationOptions.getForwardDrawableResId();
        if (j == 10000) {
            return notificationOptions.getForward10DrawableResId();
        }
        return j != 30000 ? forwardDrawableResId : notificationOptions.getForward30DrawableResId();
    }

    public static int zzb(NotificationOptions notificationOptions, long j) {
        int iZzd = notificationOptions.zzd();
        if (j == 10000) {
            return notificationOptions.zzb();
        }
        return j != 30000 ? iZzd : notificationOptions.zzc();
    }

    public static int zzc(NotificationOptions notificationOptions, long j) {
        int rewindDrawableResId = notificationOptions.getRewindDrawableResId();
        if (j == 10000) {
            return notificationOptions.getRewind10DrawableResId();
        }
        return j != 30000 ? rewindDrawableResId : notificationOptions.getRewind30DrawableResId();
    }

    public static int zzd(NotificationOptions notificationOptions, long j) {
        int iZzj = notificationOptions.zzj();
        if (j == 10000) {
            return notificationOptions.zzh();
        }
        return j != 30000 ? iZzj : notificationOptions.zzi();
    }

    public static List zzf(com.google.android.gms.cast.framework.media.zzg zzgVar) {
        try {
            return zzgVar.zzf();
        } catch (RemoteException e) {
            zza.e(e, "Unable to call %s on %s.", "getNotificationActions", com.google.android.gms.cast.framework.media.zzg.class.getSimpleName());
            return null;
        }
    }

    public static int[] zzg(com.google.android.gms.cast.framework.media.zzg zzgVar) {
        try {
            return zzgVar.zzg();
        } catch (RemoteException e) {
            zza.e(e, "Unable to call %s on %s.", "getCompactViewActionIndices", com.google.android.gms.cast.framework.media.zzg.class.getSimpleName());
            return null;
        }
    }
}
