package com.google.android.gms.fido.fido2.api.common;

public final class zzbc extends Exception {
    public zzbc(String str) {
        super(String.format("User verification requirement %s not supported", str));
    }
}
