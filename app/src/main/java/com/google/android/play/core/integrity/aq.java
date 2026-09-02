package com.google.android.play.core.integrity;

import android.content.Context;
import com.google.android.gms.tasks.TaskCompletionSource;

final class aq extends com.google.android.play.integrity.internal.r {
    final /* synthetic */ Context a;
    final /* synthetic */ ax b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    aq(ax axVar, TaskCompletionSource taskCompletionSource, Context context) {
        super(taskCompletionSource);
        this.b = axVar;
        this.a = context;
    }

    @Override // com.google.android.play.integrity.internal.r
    protected final void b() {
        this.b.d.trySetResult(Boolean.valueOf(com.google.android.play.integrity.internal.ag.a(this.a)));
    }
}
