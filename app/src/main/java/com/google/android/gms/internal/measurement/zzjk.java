package com.google.android.gms.internal.measurement;

public final class zzjk extends Exception {
    public zzjk(String str) {
        super(str);
    }

    public zzjk(String str, Throwable th) {
        super("ContentProvider query failed", th);
    }
}
