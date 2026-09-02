package com.google.android.gms.measurement.internal;

import j$.util.Objects;
import java.util.List;

final class zzhn implements com.google.android.gms.internal.measurement.zzr {
    final /* synthetic */ zzht zza;

    zzhn(zzht zzhtVar) {
        Objects.requireNonNull(zzhtVar);
        this.zza = zzhtVar;
    }

    @Override // com.google.android.gms.internal.measurement.zzr
    public final void zza(int i, String str, List list, boolean z, boolean z2) {
        zzgs zzgsVarZzj;
        int i2 = i - 1;
        if (i2 == 0) {
            zzgsVarZzj = this.zza.zzu.zzaV().zzj();
        } else if (i2 != 1) {
            if (i2 == 3) {
                zzgsVarZzj = this.zza.zzu.zzaV().zzk();
            } else if (i2 != 4) {
                zzgsVarZzj = this.zza.zzu.zzaV().zzi();
            } else if (z) {
                zzgsVarZzj = this.zza.zzu.zzaV().zzf();
            } else {
                zzgsVarZzj = !z2 ? this.zza.zzu.zzaV().zzh() : this.zza.zzu.zzaV().zze();
            }
        } else if (z) {
            zzgsVarZzj = this.zza.zzu.zzaV().zzc();
        } else {
            zzgsVarZzj = !z2 ? this.zza.zzu.zzaV().zzd() : this.zza.zzu.zzaV().zzb();
        }
        int size = list.size();
        if (size == 1) {
            zzgsVarZzj.zzb(str, list.get(0));
            return;
        }
        if (size == 2) {
            zzgsVarZzj.zzc(str, list.get(0), list.get(1));
        } else if (size != 3) {
            zzgsVarZzj.zza(str);
        } else {
            zzgsVarZzj.zzd(str, list.get(0), list.get(1), list.get(2));
        }
    }
}
