package com.google.android.gms.internal.location;

final class zzdq extends zzdo {
    private final zzds zza;

    zzdq(zzds zzdsVar, int i) {
        super(zzdsVar.size(), i);
        this.zza = zzdsVar;
    }

    @Override // com.google.android.gms.internal.location.zzdo
    protected final Object zza(int i) {
        return this.zza.get(i);
    }
}
