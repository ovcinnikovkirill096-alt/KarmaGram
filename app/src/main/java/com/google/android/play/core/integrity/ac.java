package com.google.android.play.core.integrity;

import android.app.PendingIntent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.gms.tasks.TaskCompletionSource;

final class ac extends com.google.android.play.integrity.internal.o {
    final /* synthetic */ ad a;
    private final com.google.android.play.integrity.internal.q b = new com.google.android.play.integrity.internal.q("OnRequestIntegrityTokenCallback");
    private final TaskCompletionSource c;

    ac(ad adVar, TaskCompletionSource taskCompletionSource) {
        this.a = adVar;
        this.c = taskCompletionSource;
    }

    @Override // com.google.android.play.integrity.internal.p
    public final void b(Bundle bundle) {
        this.a.a.v(this.c);
        this.b.c("onRequestIntegrityToken", new Object[0]);
        int i = bundle.getInt("error");
        if (i != 0) {
            this.c.trySetException(new IntegrityServiceException(i, null));
            return;
        }
        String string = bundle.getString("token");
        if (string == null) {
            this.c.trySetException(new IntegrityServiceException(-100, null));
            return;
        }
        PendingIntent pendingIntent = Build.VERSION.SDK_INT >= 33 ? (PendingIntent) bundle.getParcelable("dialog.intent", PendingIntent.class) : (PendingIntent) bundle.getParcelable("dialog.intent");
        TaskCompletionSource taskCompletionSource = this.c;
        a aVar = new a();
        aVar.c(string);
        aVar.b(this.b);
        aVar.a(pendingIntent);
        taskCompletionSource.trySetResult(aVar.d());
    }
}
