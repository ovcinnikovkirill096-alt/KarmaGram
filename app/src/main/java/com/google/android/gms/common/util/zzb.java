package com.google.android.gms.common.util;

import android.os.StrictMode;

abstract class zzb {
    static StrictMode.VmPolicy.Builder zza(StrictMode.VmPolicy.Builder builder) {
        return builder.permitUnsafeIntentLaunch();
    }
}
