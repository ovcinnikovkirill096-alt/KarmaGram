package com.google.android.gms.maps;

import com.google.android.gms.maps.internal.zzao;

final class zzj extends zzao {
    final /* synthetic */ GoogleMap.OnMapLoadedCallback zza;

    zzj(GoogleMap googleMap, GoogleMap.OnMapLoadedCallback onMapLoadedCallback) {
        this.zza = onMapLoadedCallback;
    }

    @Override // com.google.android.gms.maps.internal.zzap
    public final void zzb() {
        this.zza.onMapLoaded();
    }
}
