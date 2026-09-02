package com.google.android.gms.internal.measurement;

import android.database.ContentObserver;
import android.os.Handler;
import j$.util.Objects;

final class zzjw extends ContentObserver {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzjw(zzjy zzjyVar, Handler handler) {
        super(null);
        Objects.requireNonNull(zzjyVar);
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z) {
        zzkm.zzc();
    }
}
