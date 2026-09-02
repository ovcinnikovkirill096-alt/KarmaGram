package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;
import j$.util.Objects;

final class zzdp extends zzeq {
    final /* synthetic */ zzfb zza;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzdp(zzfb zzfbVar) {
        super(zzfbVar, true);
        Objects.requireNonNull(zzfbVar);
        this.zza = zzfbVar;
    }

    @Override // com.google.android.gms.internal.measurement.zzeq
    final void zza() {
        ((zzcr) Preconditions.checkNotNull(this.zza.zzQ())).resetAnalyticsData(this.zzh);
    }
}
