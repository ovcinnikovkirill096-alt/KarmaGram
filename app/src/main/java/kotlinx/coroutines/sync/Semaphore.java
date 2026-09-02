package kotlinx.coroutines.sync;

import kotlin.coroutines.Continuation;

public interface Semaphore {
    Object acquire(Continuation continuation);

    int getAvailablePermits();

    void release();
}
