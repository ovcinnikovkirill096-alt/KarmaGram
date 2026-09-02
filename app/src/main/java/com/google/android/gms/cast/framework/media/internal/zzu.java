package com.google.android.gms.cast.framework.media.internal;

import com.google.android.gms.cast.framework.media.RemoteMediaClient;

final class zzu extends RemoteMediaClient.Callback {
    final /* synthetic */ zzv zza;

    /* synthetic */ zzu(zzv zzvVar, zzt zztVar) {
        this.zza = zzvVar;
    }

    @Override // com.google.android.gms.cast.framework.media.RemoteMediaClient.Callback
    public final void onAdBreakStatusUpdated() throws CloneNotSupportedException {
        this.zza.zzl(false);
    }

    @Override // com.google.android.gms.cast.framework.media.RemoteMediaClient.Callback
    public final void onMetadataUpdated() throws CloneNotSupportedException {
        this.zza.zzl(false);
    }

    @Override // com.google.android.gms.cast.framework.media.RemoteMediaClient.Callback
    public final void onPreloadStatusUpdated() throws CloneNotSupportedException {
        this.zza.zzl(false);
    }

    @Override // com.google.android.gms.cast.framework.media.RemoteMediaClient.Callback
    public final void onQueueStatusUpdated() throws CloneNotSupportedException {
        this.zza.zzl(false);
    }

    @Override // com.google.android.gms.cast.framework.media.RemoteMediaClient.Callback
    public final void onStatusUpdated() throws CloneNotSupportedException {
        this.zza.zzl(false);
    }
}
