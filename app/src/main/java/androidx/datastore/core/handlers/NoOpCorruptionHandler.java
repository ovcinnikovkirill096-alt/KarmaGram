package androidx.datastore.core.handlers;

import androidx.datastore.core.CorruptionException;
import androidx.datastore.core.CorruptionHandler;
import kotlin.coroutines.Continuation;

public final class NoOpCorruptionHandler implements CorruptionHandler {
    @Override // androidx.datastore.core.CorruptionHandler
    public Object handleCorruption(CorruptionException corruptionException, Continuation continuation) throws CorruptionException {
        throw corruptionException;
    }
}
