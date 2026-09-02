package androidx.room;

import kotlin.coroutines.Continuation;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function2;

public interface Transactor extends PooledConnection {

    public enum SQLiteTransactionType {
        DEFERRED,
        IMMEDIATE,
        EXCLUSIVE;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());
    }

    Object inTransaction(Continuation continuation);

    Object withTransaction(SQLiteTransactionType sQLiteTransactionType, Function2 function2, Continuation continuation);
}
