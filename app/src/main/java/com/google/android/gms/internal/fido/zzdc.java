package com.google.android.gms.internal.fido;

import java.util.Iterator;

public abstract class zzdc implements Iterator {
    protected zzdc() {
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
