package com.google.android.gms.internal.cast;

public final class zzq {
    private final String zza;
    private final long zzb;
    private final int zzc;
    private final long zzd;
    private final long zze;
    private long zzf;

    public zzq(zzp zzpVar) {
        this.zza = zzpVar.zza;
        this.zzb = zzpVar.zzb;
        this.zzc = zzpVar.zzc;
        this.zzd = zzpVar.zzd;
        this.zze = zzpVar.zze;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:71:0x0108  */
    public final zznq zza() {
        int i;
        String str = this.zza;
        zznp zznpVarZza = zznq.zza();
        switch (str) {
            case "queueInsert":
                i = 13;
                break;
            case "launch":
                i = 22;
                break;
            case "queueRemove":
                i = 15;
                break;
            case "queueFetchItems":
                i = 19;
                break;
            case "setPlaybackDevices":
                i = 23;
                break;
            case "volume-mute":
                i = 9;
                break;
            case "skipAd":
                i = 21;
                break;
            case "status":
                i = 10;
                break;
            case "queueUpdate":
                i = 14;
                break;
            case "volume":
                i = 7;
                break;
            case "setPlaybackRate":
                i = 20;
                break;
            case "load":
                i = 2;
                break;
            case "mute":
                i = 8;
                break;
            case "play":
                i = 3;
                break;
            case "seek":
                i = 6;
                break;
            case "stop":
                i = 5;
                break;
            case "pause":
                i = 4;
                break;
            case "queueFetchItemRange":
                i = 18;
                break;
            case "queueReorder":
                i = 16;
                break;
            case "trackStyle":
                i = 12;
                break;
            case "activeTracks":
                i = 11;
                break;
            case "queueFetchItemIds":
                i = 17;
                break;
            default:
                i = 1;
                break;
        }
        zznpVarZza.zze(i);
        zznpVarZza.zzb((int) this.zzb);
        zznpVarZza.zzd(this.zzc);
        zznpVarZza.zzc((int) (this.zzd - this.zzf));
        zznpVarZza.zza((int) (this.zze - this.zzf));
        return (zznq) zznpVarZza.zzq();
    }

    public final void zzb(long j) {
        this.zzf = j;
    }
}
