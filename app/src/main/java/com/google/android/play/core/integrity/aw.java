package com.google.android.play.core.integrity;

import com.google.android.gms.tasks.TaskCompletionSource;

abstract class aw extends com.google.android.play.integrity.internal.r {
    final /* synthetic */ ax f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    aw(ax axVar, TaskCompletionSource taskCompletionSource) {
        super(taskCompletionSource);
        this.f = axVar;
    }

    @Override // com.google.android.play.integrity.internal.r
    public final void a(Exception exc) {
        if (!(exc instanceof com.google.android.play.integrity.internal.ad)) {
            super.a(exc);
        } else if (ax.g(this.f)) {
            super.a(new StandardIntegrityException(-2, exc));
        } else {
            super.a(new StandardIntegrityException(-9, exc));
        }
    }
}
