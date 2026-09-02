package com.google.android.play.integrity.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

final class ab implements ServiceConnection {
    final /* synthetic */ ac a;

    /* synthetic */ ab(ac acVar, aa aaVar) {
        this.a = acVar;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.a.c.c("ServiceConnectionImpl.onServiceConnected(%s)", componentName);
        this.a.c().post(new y(this, iBinder));
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        this.a.c.c("ServiceConnectionImpl.onServiceDisconnected(%s)", componentName);
        this.a.c().post(new z(this));
    }
}
