package com.google.android.play.integrity.internal;

import com.google.android.gms.tasks.TaskCompletionSource;

final class u extends r {
    final /* synthetic */ TaskCompletionSource a;
    final /* synthetic */ r b;
    final /* synthetic */ ac c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    u(ac acVar, TaskCompletionSource taskCompletionSource, TaskCompletionSource taskCompletionSource2, r rVar) {
        super(taskCompletionSource);
        this.c = acVar;
        this.a = taskCompletionSource2;
        this.b = rVar;
    }

    @Override // com.google.android.play.integrity.internal.r
    public final void b() {
        synchronized (this.c.g) {
            try {
                ac.o(this.c, this.a);
                if (this.c.m.getAndIncrement() > 0) {
                    this.c.c.c("Already connected to the service.", new Object[0]);
                }
                ac.q(this.c, this.b);
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
