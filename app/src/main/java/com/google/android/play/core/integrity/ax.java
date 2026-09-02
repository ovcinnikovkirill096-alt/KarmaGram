package com.google.android.play.core.integrity;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.ArrayList;

final class ax {
    final com.google.android.play.integrity.internal.ac a;
    private final com.google.android.play.integrity.internal.q b;
    private final String c;
    private final TaskCompletionSource d;

    ax(Context context, com.google.android.play.integrity.internal.q qVar) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.d = taskCompletionSource;
        this.c = context.getPackageName();
        this.b = qVar;
        com.google.android.play.integrity.internal.ac acVar = new com.google.android.play.integrity.internal.ac(context, qVar, "ExpressIntegrityService", ay.a, new com.google.android.play.integrity.internal.x() { // from class: com.google.android.play.core.integrity.ap
            @Override // com.google.android.play.integrity.internal.x
            public final Object a(IBinder iBinder) {
                return com.google.android.play.integrity.internal.h.b(iBinder);
            }
        }, null);
        this.a = acVar;
        acVar.c().post(new aq(this, taskCompletionSource, context));
    }

    static /* bridge */ /* synthetic */ Bundle a(ax axVar, String str, long j, long j2) {
        Bundle bundle = new Bundle();
        bundle.putString("package.name", axVar.c);
        bundle.putLong("cloud.prj", j);
        bundle.putString("nonce", str);
        bundle.putLong("warm.up.sid", j2);
        ArrayList arrayList = new ArrayList();
        com.google.android.play.integrity.internal.d.b(5, arrayList);
        bundle.putParcelableArrayList("event_timestamps", new ArrayList<>(com.google.android.play.integrity.internal.d.a(arrayList)));
        return bundle;
    }

    static /* bridge */ /* synthetic */ Bundle b(ax axVar, long j) {
        Bundle bundle = new Bundle();
        bundle.putString("package.name", axVar.c);
        bundle.putLong("cloud.prj", j);
        ArrayList arrayList = new ArrayList();
        com.google.android.play.integrity.internal.d.b(4, arrayList);
        bundle.putParcelableArrayList("event_timestamps", new ArrayList<>(com.google.android.play.integrity.internal.d.a(arrayList)));
        return bundle;
    }

    static /* bridge */ /* synthetic */ boolean g(ax axVar) {
        return axVar.d.getTask().isSuccessful() && !((Boolean) axVar.d.getTask().getResult()).booleanValue();
    }

    public final Task c(String str, long j, long j2) {
        this.b.c("requestExpressIntegrityToken(%s)", Long.valueOf(j2));
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.a.t(new as(this, taskCompletionSource, str, j, j2, taskCompletionSource), taskCompletionSource);
        return taskCompletionSource.getTask();
    }

    public final Task d(long j) {
        this.b.c("warmUpIntegrityToken(%s)", Long.valueOf(j));
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.a.t(new ar(this, taskCompletionSource, j, taskCompletionSource), taskCompletionSource);
        return taskCompletionSource.getTask();
    }
}
