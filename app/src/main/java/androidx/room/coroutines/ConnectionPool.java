package androidx.room.coroutines;

import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;

public interface ConnectionPool extends AutoCloseable {
    @Override // java.lang.AutoCloseable
    void close();

    Object useConnection(boolean z, Function2 function2, Continuation continuation);
}
