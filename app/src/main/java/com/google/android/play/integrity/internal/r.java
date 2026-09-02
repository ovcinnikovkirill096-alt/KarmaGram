package com.google.android.play.integrity.internal;

import com.google.android.gms.tasks.TaskCompletionSource;

public abstract class r implements Runnable {
    private final TaskCompletionSource a;

    r() {
        this.a = null;
    }

    public r(TaskCompletionSource taskCompletionSource) {
        this.a = taskCompletionSource;
    }

    public void a(Exception exc) {
        TaskCompletionSource taskCompletionSource = this.a;
        if (taskCompletionSource != null) {
            taskCompletionSource.trySetException(exc);
        }
    }

    protected abstract void b();

    final TaskCompletionSource c() {
        return this.a;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            b();
        } catch (Exception e) {
            a(e);
        }
    }
}
