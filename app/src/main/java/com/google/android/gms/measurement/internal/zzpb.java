package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.text.TextUtils;
import j$.util.Objects;

final class zzpb implements zzpo {
    final /* synthetic */ zzpg zza;

    zzpb(zzpg zzpgVar) {
        Objects.requireNonNull(zzpgVar);
        this.zza = zzpgVar;
    }

    @Override // com.google.android.gms.measurement.internal.zzpo
    public final void zza(String str, String str2, Bundle bundle) {
        if (!TextUtils.isEmpty(str)) {
            this.zza.zzaW().zzj(new zzpa(this, str, str2, bundle));
            return;
        }
        zzpg zzpgVar = this.zza;
        if (zzpgVar.zzax() != null) {
            zzpgVar.zzax().zzaV().zzb().zzb("AppId not known when logging event", str2);
        }
    }
}
