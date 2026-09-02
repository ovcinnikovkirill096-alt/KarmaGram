package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import j$.util.Objects;

final class zzdi extends zzeq {
    final /* synthetic */ Bundle zza;
    final /* synthetic */ zzfb zzb;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzdi(zzfb zzfbVar, Bundle bundle) {
        super(zzfbVar, true);
        this.zza = bundle;
        Objects.requireNonNull(zzfbVar);
        this.zzb = zzfbVar;
    }

    @Override // com.google.android.gms.internal.measurement.zzeq
    final void zza() {
        ((zzcr) Preconditions.checkNotNull(this.zzb.zzQ())).setConditionalUserProperty(this.zza, this.zzh);
    }
}
