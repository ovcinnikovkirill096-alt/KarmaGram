package com.google.android.play.integrity.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;

public final class l extends a implements n {
    l(IBinder iBinder) {
        super(iBinder, "com.google.android.play.core.integrity.protocol.IIntegrityService");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.play.integrity.internal.n
    public final void c(Bundle bundle, p pVar) {
        Parcel parcelA = a();
        c.c(parcelA, bundle);
        parcelA.writeStrongBinder(pVar);
        b(2, parcelA);
    }
}
