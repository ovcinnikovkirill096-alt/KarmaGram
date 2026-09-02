package com.google.android.gms.internal.cast;

import com.google.android.gms.common.util.DefaultClock;

public final class zzab {
    final int zza;
    final long zzb = DefaultClock.getInstance().currentTimeMillis();
    private long zzc;

    public zzab(zzaa zzaaVar) {
        this.zza = zzaaVar.zza;
    }

    public final zzny zza() {
        zznx zznxVarZza = zzny.zza();
        zznxVarZza.zza((int) (this.zzb - this.zzc));
        int i = this.zza;
        int i2 = 2;
        if (i != 1) {
            if (i != 2) {
                i2 = i != 3 ? 1 : 4;
            } else {
                i2 = 3;
            }
        }
        zznxVarZza.zzb(i2);
        return (zzny) zznxVarZza.zzq();
    }

    public final void zzb(long j) {
        this.zzc = j;
    }
}
