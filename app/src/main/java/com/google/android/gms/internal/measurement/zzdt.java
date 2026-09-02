package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;
import j$.util.Objects;

final class zzdt extends zzeq {
    final /* synthetic */ String zza;
    final /* synthetic */ zzfb zzb;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzdt(zzfb zzfbVar, String str) {
        super(zzfbVar, true);
        this.zza = str;
        Objects.requireNonNull(zzfbVar);
        this.zzb = zzfbVar;
    }

    @Override // com.google.android.gms.internal.measurement.zzeq
    final void zza() {
        ((zzcr) Preconditions.checkNotNull(this.zzb.zzQ())).endAdUnitExposure(this.zza, this.zzi);
    }
}
