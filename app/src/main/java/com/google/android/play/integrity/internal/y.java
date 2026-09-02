package com.google.android.play.integrity.internal;

import android.os.IBinder;
import android.os.IInterface;
import java.util.Iterator;

final class y extends r {
    final /* synthetic */ IBinder a;
    final /* synthetic */ ab b;

    y(ab abVar, IBinder iBinder) {
        this.b = abVar;
        this.a = iBinder;
    }

    @Override // com.google.android.play.integrity.internal.r
    public final void b() {
        ac acVar = this.b.a;
        acVar.o = (IInterface) acVar.j.a(this.a);
        ac.r(this.b.a);
        this.b.a.h = false;
        Iterator it = this.b.a.e.iterator();
        while (it.hasNext()) {
            ((Runnable) it.next()).run();
        }
        this.b.a.e.clear();
    }
}
