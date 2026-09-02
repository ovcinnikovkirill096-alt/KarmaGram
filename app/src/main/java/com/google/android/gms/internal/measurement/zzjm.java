package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.os.UserManager;
import android.util.Log;

public abstract class zzjm {
    private static UserManager zza;
    private static volatile boolean zzb = !zza();

    public static boolean zza() {
        return Build.VERSION.SDK_INT >= 24;
    }

    public static boolean zzb(Context context) {
        return zza() && !zzd(context);
    }

    public static boolean zzc(Context context) {
        return !zza() || zzd(context);
    }

    /* JADX WARN: Code duplicated, block: B:32:0x0050 A[Catch: all -> 0x000f, TryCatch #1 {all -> 0x000f, blocks: (B:7:0x0009, B:9:0x000d, B:16:0x0017, B:18:0x001b, B:19:0x0025, B:32:0x0050, B:33:0x0052, B:22:0x002b, B:24:0x0031, B:28:0x003e, B:30:0x004c), top: B:39:0x0009, inners: #0 }] */
    private static boolean zzd(Context context) {
        if (zzb) {
            return true;
        }
        synchronized (zzjm.class) {
            try {
                if (zzb) {
                    return true;
                }
                int i = 1;
                while (true) {
                    boolean z = false;
                    if (i <= 2) {
                        if (zza == null) {
                            zza = (UserManager) context.getSystemService(UserManager.class);
                        }
                        UserManager userManager = zza;
                        if (userManager == null) {
                            z = true;
                        } else {
                            try {
                                if (userManager.isUserUnlocked() || !userManager.isUserRunning(Process.myUserHandle())) {
                                    z = true;
                                }
                            } catch (NullPointerException e) {
                                Log.w("DirectBootUtils", "Failed to check if user is unlocked.", e);
                                zza = null;
                                i++;
                            }
                        }
                        if (z) {
                            zzb = true;
                        }
                        return z;
                    }
                    if (z) {
                        zza = null;
                    }
                    if (z) {
                        zzb = true;
                    }
                    return z;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
