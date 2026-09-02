package com.google.android.play.integrity.internal;

import android.os.IBinder;
import android.os.IInterface;

public abstract class m extends b implements n {
    public static n b(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.play.core.integrity.protocol.IIntegrityService");
        return iInterfaceQueryLocalInterface instanceof n ? (n) iInterfaceQueryLocalInterface : new l(iBinder);
    }
}
