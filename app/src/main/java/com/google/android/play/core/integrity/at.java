package com.google.android.play.core.integrity;

import android.os.Bundle;
import com.google.android.gms.tasks.TaskCompletionSource;

class at extends com.google.android.play.integrity.internal.j {
    final TaskCompletionSource a;
    final /* synthetic */ ax b;

    at(ax axVar, TaskCompletionSource taskCompletionSource) {
        this.b = axVar;
        this.a = taskCompletionSource;
    }

    @Override // com.google.android.play.integrity.internal.k
    public final void b(Bundle bundle) {
        this.b.a.v(this.a);
    }

    @Override // com.google.android.play.integrity.internal.k
    public void c(Bundle bundle) {
        this.b.a.v(this.a);
    }

    @Override // com.google.android.play.integrity.internal.k
    public final void d(Bundle bundle) {
        this.b.a.v(this.a);
    }

    @Override // com.google.android.play.integrity.internal.k
    public void e(Bundle bundle) {
        this.b.a.v(this.a);
    }
}
