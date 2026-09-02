package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.text.TextUtils;
import j$.util.Objects;

final class zzkn implements zzpo {
    final /* synthetic */ zzlj zza;

    zzkn(zzlj zzljVar) {
        Objects.requireNonNull(zzljVar);
        this.zza = zzljVar;
    }

    @Override // com.google.android.gms.measurement.internal.zzpo
    public final void zza(String str, String str2, Bundle bundle) {
        if (TextUtils.isEmpty(str)) {
            this.zza.zzB("auto", "_err", bundle);
        } else {
            this.zza.zzI("auto", "_err", bundle, str);
        }
    }
}
