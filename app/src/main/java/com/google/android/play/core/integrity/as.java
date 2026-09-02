package com.google.android.play.core.integrity;

import android.os.RemoteException;
import com.google.android.gms.tasks.TaskCompletionSource;

final class as extends aw {
    final /* synthetic */ String a;
    final /* synthetic */ long b;
    final /* synthetic */ long c;
    final /* synthetic */ TaskCompletionSource d;
    final /* synthetic */ ax e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    as(ax axVar, TaskCompletionSource taskCompletionSource, String str, long j, long j2, TaskCompletionSource taskCompletionSource2) {
        super(axVar, taskCompletionSource);
        this.e = axVar;
        this.a = str;
        this.b = j;
        this.c = j2;
        this.d = taskCompletionSource2;
    }

    @Override // com.google.android.play.integrity.internal.r
    protected final void b() {
        if (ax.g(this.e)) {
            super.a(new StandardIntegrityException(-2, null));
            return;
        }
        try {
            ax axVar = this.e;
            ((com.google.android.play.integrity.internal.i) axVar.a.e()).c(ax.a(axVar, this.a, this.b, this.c), new au(this.e, this.d));
        } catch (RemoteException e) {
            this.e.b.b(e, "requestExpressIntegrityToken(%s, %s)", this.a, Long.valueOf(this.b));
            this.d.trySetException(new StandardIntegrityException(-100, e));
        }
    }
}
