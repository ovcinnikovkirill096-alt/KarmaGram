package com.google.android.gms.internal.clearcut;

import java.util.List;

public final class zzew extends RuntimeException {
    private final List zzoy;

    public zzew(zzdo zzdoVar) {
        super("Message was missing required fields.  (Lite runtime could not determine which fields were missing).");
        this.zzoy = null;
    }
}
