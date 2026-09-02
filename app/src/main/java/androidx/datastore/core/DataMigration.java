package androidx.datastore.core;

import kotlin.coroutines.Continuation;

public interface DataMigration {
    Object cleanUp(Continuation continuation);

    Object migrate(Object obj, Continuation continuation);

    Object shouldMigrate(Object obj, Continuation continuation);
}
