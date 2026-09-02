package com.android.billingclient.api;

import com.google.android.gms.internal.play_billing.zze;
import com.google.android.gms.internal.play_billing.zzjx;
import com.google.android.gms.internal.play_billing.zzjz;
import com.google.android.gms.internal.play_billing.zzke;
import com.google.android.gms.internal.play_billing.zzki;

public abstract /* synthetic */ class zzcg {
    static {
        int i = zzch.$r8$clinit;
    }

    public static zzjz zzb(int i, int i2, BillingResult billingResult) {
        try {
            zzjx zzjxVarZzc = zzjz.zzc();
            zzke zzkeVarZzc = zzki.zzc();
            zzkeVarZzc.zzn(billingResult.getResponseCode());
            zzkeVarZzc.zzm(billingResult.getDebugMessage());
            zzkeVarZzc.zzo(i);
            zzjxVarZzc.zza(zzkeVarZzc);
            zzjxVarZzc.zzn(i2);
            return (zzjz) zzjxVarZzc.zzf();
        } catch (Exception e) {
            zze.zzm("BillingLogger", "Unable to create logging payload", e);
            return null;
        }
    }
}
