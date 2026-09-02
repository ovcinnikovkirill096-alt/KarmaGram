package com.google.android.gms.measurement.api;

import android.os.Bundle;
import com.google.android.gms.internal.measurement.zzfb;
import com.google.android.gms.measurement.internal.zzjq;
import java.util.Map;

public class AppMeasurementSdk {
    private final zzfb zza;

    public interface OnEventListener extends zzjq {
    }

    public AppMeasurementSdk(zzfb zzfbVar) {
        this.zza = zzfbVar;
    }

    public Map getUserProperties(String str, String str2, boolean z) {
        return this.zza.zzC(str, str2, z);
    }

    public void logEvent(String str, String str2, Bundle bundle) {
        this.zza.zzi(str, str2, bundle);
    }

    public void registerOnMeasurementEventListener(OnEventListener onEventListener) {
        this.zza.zzf(onEventListener);
    }

    public void setUserProperty(String str, String str2, Object obj) {
        this.zza.zzk(str, str2, obj, true);
    }
}
