package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import j$.util.Objects;

final class zzmj implements Runnable {
    final /* synthetic */ zzr zza;
    final /* synthetic */ com.google.android.gms.internal.measurement.zzcu zzb;
    final /* synthetic */ zznl zzc;

    zzmj(zznl zznlVar, zzr zzrVar, com.google.android.gms.internal.measurement.zzcu zzcuVar) {
        this.zza = zzrVar;
        this.zzb = zzcuVar;
        Objects.requireNonNull(zznlVar);
        this.zzc = zznlVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        com.google.android.gms.internal.measurement.zzcu zzcuVar;
        zzpp zzppVarZzk;
        String strZzm = null;
        try {
            try {
                zznl zznlVar = this.zzc;
                zzic zzicVar = zznlVar.zzu;
                if (zzicVar.zzd().zzl().zzo(zzjk.ANALYTICS_STORAGE)) {
                    zzgb zzgbVarZzZ = zznlVar.zzZ();
                    if (zzgbVarZzZ != null) {
                        zzr zzrVar = this.zza;
                        Preconditions.checkNotNull(zzrVar);
                        strZzm = zzgbVarZzZ.zzm(zzrVar);
                        if (strZzm != null) {
                            zznlVar.zzu.zzj().zzR(strZzm);
                            zzicVar.zzd().zze.zzb(strZzm);
                        }
                        zznlVar.zzV();
                        zznl zznlVar2 = this.zzc;
                        zzcuVar = this.zzb;
                        zzppVarZzk = zznlVar2.zzu.zzk();
                        zzppVarZzk.zzal(zzcuVar, strZzm);
                    }
                    zzicVar.zzaV().zzb().zza("Failed to get app instance id");
                } else {
                    zzicVar.zzaV().zzh().zza("Analytics storage consent denied; will not get app instance id");
                    zznlVar.zzu.zzj().zzR(null);
                    zzicVar.zzd().zze.zzb(null);
                }
                zzppVarZzk = zzicVar.zzk();
                zzcuVar = this.zzb;
            } catch (RemoteException e) {
                this.zzc.zzu.zzaV().zzb().zzb("Failed to get app instance id", e);
            }
            zzppVarZzk.zzal(zzcuVar, strZzm);
        } catch (Throwable th) {
            zznl zznlVar3 = this.zzc;
            zznlVar3.zzu.zzk().zzal(this.zzb, null);
            throw th;
        }
    }
}
