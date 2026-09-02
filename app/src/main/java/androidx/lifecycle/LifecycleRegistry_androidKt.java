package androidx.lifecycle;

import androidx.arch.core.executor.ArchTaskExecutor;

public abstract class LifecycleRegistry_androidKt {
    public static final boolean isMainThread() {
        return ArchTaskExecutor.getInstance().isMainThread();
    }
}
