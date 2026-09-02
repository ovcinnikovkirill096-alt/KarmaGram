package kotlinx.coroutines;

import kotlinx.coroutines.internal.ThreadLocalElement;

public abstract class ThreadContextElementKt {
    public static final ThreadContextElement asContextElement(ThreadLocal threadLocal, Object obj) {
        return new ThreadLocalElement(obj, threadLocal);
    }
}
