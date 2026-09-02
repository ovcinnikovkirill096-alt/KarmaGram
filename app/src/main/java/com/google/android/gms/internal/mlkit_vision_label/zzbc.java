package com.google.android.gms.internal.mlkit_vision_label;

final class zzbc extends zzu {
    private final zzbe zza;

    zzbc(zzbe zzbeVar, int i) {
        super(zzbeVar.size(), i);
        this.zza = zzbeVar;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzu
    protected final Object zza(int i) {
        return this.zza.get(i);
    }
}
