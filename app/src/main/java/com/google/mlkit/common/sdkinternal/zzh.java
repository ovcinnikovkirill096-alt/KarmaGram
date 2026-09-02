package com.google.mlkit.common.sdkinternal;

import java.util.concurrent.Executor;

enum zzh implements Executor {
    INSTANCE;

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        MLTaskExecutor.getInstance().zzc.post(runnable);
    }
}
