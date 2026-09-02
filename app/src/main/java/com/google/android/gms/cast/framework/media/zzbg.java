package com.google.android.gms.cast.framework.media;

import com.google.android.gms.common.api.Status;

final class zzbg implements RemoteMediaClient.MediaChannelResult {
    final /* synthetic */ Status zza;

    zzbg(zzbh zzbhVar, Status status) {
        this.zza = status;
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.zza;
    }
}
