package com.radolyn.ayugram.database.dao;

import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomRawQuery;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.radolyn.ayugram.database.entities.SpyLastSeen;
import com.radolyn.ayugram.database.entities.SpyMessageContentsRead;
import com.radolyn.ayugram.database.entities.SpyMessageRead;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;

public final class SpyDao_Impl implements SpyDao {
    public static final Companion Companion = new Companion(null);
    private final RoomDatabase __db;
    private final EntityInsertAdapter __insertAdapterOfSpyLastSeen;
    private final EntityInsertAdapter __insertAdapterOfSpyMessageContentsRead;
    private final EntityInsertAdapter __insertAdapterOfSpyMessageRead;

    public SpyDao_Impl(RoomDatabase __db) {
        Intrinsics.checkNotNullParameter(__db, "__db");
        this.__db = __db;
        this.__insertAdapterOfSpyMessageRead = new EntityInsertAdapter() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl.1
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR ABORT INTO `SpyMessageRead` (`fakeId`,`userId`,`dialogId`,`messageId`,`entityCreateDate`) VALUES (nullif(?, 0),?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, SpyMessageRead entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.bindLong(1, entity.fakeId);
                statement.bindLong(2, entity.userId);
                statement.bindLong(3, entity.dialogId);
                statement.bindLong(4, entity.messageId);
                statement.bindLong(5, entity.entityCreateDate);
            }
        };
        this.__insertAdapterOfSpyMessageContentsRead = new EntityInsertAdapter() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl.2
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR ABORT INTO `SpyMessageContentsRead` (`fakeId`,`userId`,`dialogId`,`messageId`,`entityCreateDate`) VALUES (nullif(?, 0),?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, SpyMessageContentsRead entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.bindLong(1, entity.fakeId);
                statement.bindLong(2, entity.userId);
                statement.bindLong(3, entity.dialogId);
                statement.bindLong(4, entity.messageId);
                statement.bindLong(5, entity.entityCreateDate);
            }
        };
        this.__insertAdapterOfSpyLastSeen = new EntityInsertAdapter() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl.3
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `SpyLastSeen` (`userId`,`lastSeenDate`) VALUES (nullif(?, 0),?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, SpyLastSeen entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.bindLong(1, entity.userId);
                statement.bindLong(2, entity.lastSeenDate);
            }
        };
    }

    @Override // com.radolyn.ayugram.database.dao.SpyDao
    public void insert(final SpyMessageRead spyMessageRead) {
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SpyDao_Impl.insert$lambda$0(this.f$0, spyMessageRead, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit insert$lambda$0(SpyDao_Impl spyDao_Impl, SpyMessageRead spyMessageRead, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        spyDao_Impl.__insertAdapterOfSpyMessageRead.insert(_connection, spyMessageRead);
        return Unit.INSTANCE;
    }

    @Override // com.radolyn.ayugram.database.dao.SpyDao
    public void insert(final SpyMessageContentsRead spyMessageContentsRead) {
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SpyDao_Impl.insert$lambda$1(this.f$0, spyMessageContentsRead, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit insert$lambda$1(SpyDao_Impl spyDao_Impl, SpyMessageContentsRead spyMessageContentsRead, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        spyDao_Impl.__insertAdapterOfSpyMessageContentsRead.insert(_connection, spyMessageContentsRead);
        return Unit.INSTANCE;
    }

    @Override // com.radolyn.ayugram.database.dao.SpyDao
    public void insert(final SpyLastSeen spyLastSeen) {
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SpyDao_Impl.insert$lambda$2(this.f$0, spyLastSeen, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit insert$lambda$2(SpyDao_Impl spyDao_Impl, SpyLastSeen spyLastSeen, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        spyDao_Impl.__insertAdapterOfSpyLastSeen.insert(_connection, spyLastSeen);
        return Unit.INSTANCE;
    }

    @Override // com.radolyn.ayugram.database.dao.SpyDao
    public int getLastSeenCount() {
        final String str = "SELECT COUNT(*) FROM spylastseen";
        return ((Number) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Integer.valueOf(SpyDao_Impl.getLastSeenCount$lambda$0(str, (SQLiteConnection) obj));
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int getLastSeenCount$lambda$0(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            return sQLiteStatementPrepare.step() ? (int) sQLiteStatementPrepare.getLong(0) : 0;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.SpyDao
    public SpyMessageRead getMessageRead(final long j, final long j2, final int i) {
        final String str = "SELECT * FROM spymessageread WHERE userId = ? AND dialogId = ? AND messageId = ?";
        return (SpyMessageRead) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SpyDao_Impl.getMessageRead$lambda$0(str, j, j2, i, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final SpyMessageRead getMessageRead$lambda$0(String str, long j, long j2, int i, SQLiteConnection _connection) {
        SpyMessageRead spyMessageRead;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.bindLong(3, i);
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fakeId");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "userId");
            int columnIndexOrThrow3 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "dialogId");
            int columnIndexOrThrow4 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "messageId");
            int columnIndexOrThrow5 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "entityCreateDate");
            if (sQLiteStatementPrepare.step()) {
                spyMessageRead = new SpyMessageRead();
                spyMessageRead.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                spyMessageRead.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                spyMessageRead.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                spyMessageRead.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                spyMessageRead.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
            } else {
                spyMessageRead = null;
            }
            return spyMessageRead;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.SpyDao
    public SpyMessageContentsRead getMessageContentsRead(final long j, final long j2, final int i) {
        final String str = "SELECT * FROM spymessagecontentsread WHERE userId = ? AND dialogId = ? AND messageId = ?";
        return (SpyMessageContentsRead) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SpyDao_Impl.getMessageContentsRead$lambda$0(str, j, j2, i, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final SpyMessageContentsRead getMessageContentsRead$lambda$0(String str, long j, long j2, int i, SQLiteConnection _connection) {
        SpyMessageContentsRead spyMessageContentsRead;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.bindLong(3, i);
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fakeId");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "userId");
            int columnIndexOrThrow3 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "dialogId");
            int columnIndexOrThrow4 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "messageId");
            int columnIndexOrThrow5 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "entityCreateDate");
            if (sQLiteStatementPrepare.step()) {
                spyMessageContentsRead = new SpyMessageContentsRead();
                spyMessageContentsRead.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                spyMessageContentsRead.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                spyMessageContentsRead.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                spyMessageContentsRead.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                spyMessageContentsRead.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
            } else {
                spyMessageContentsRead = null;
            }
            return spyMessageContentsRead;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.SpyDao
    public SpyLastSeen getLastSeen(final long j) {
        final String str = "SELECT * FROM spylastseen WHERE userId = ?";
        return (SpyLastSeen) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SpyDao_Impl.getLastSeen$lambda$0(str, j, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final SpyLastSeen getLastSeen$lambda$0(String str, long j, SQLiteConnection _connection) {
        SpyLastSeen spyLastSeen;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "userId");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "lastSeenDate");
            if (sQLiteStatementPrepare.step()) {
                spyLastSeen = new SpyLastSeen();
                spyLastSeen.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                spyLastSeen.lastSeenDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
            } else {
                spyLastSeen = null;
            }
            return spyLastSeen;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.SpyDao
    public void deleteOldReads() {
        final String str = "DELETE FROM spymessageread WHERE entityCreateDate < strftime('%s', 'now') - 604800";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SpyDao_Impl.deleteOldReads$lambda$0(str, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit deleteOldReads$lambda$0(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.step();
            return Unit.INSTANCE;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.SpyDao
    public void deleteOldContentsRead() {
        final String str = "DELETE FROM spymessagecontentsread WHERE entityCreateDate < strftime('%s', 'now') - 604800";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SpyDao_Impl.deleteOldContentsRead$lambda$0(str, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit deleteOldContentsRead$lambda$0(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.step();
            return Unit.INSTANCE;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.SpyDao
    public void deleteOldLastSeen() {
        final String str = "DELETE FROM spylastseen WHERE lastSeenDate < strftime('%s', 'now') - 1209600";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SpyDao_Impl.deleteOldLastSeen$lambda$0(str, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit deleteOldLastSeen$lambda$0(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.step();
            return Unit.INSTANCE;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.SpyDao
    public int vacuum(SupportSQLiteQuery supportSQLiteQuery) {
        Intrinsics.checkNotNullParameter(supportSQLiteQuery, "supportSQLiteQuery");
        final RoomRawQuery roomRawQuery = RoomSQLiteQuery.Companion.copyFrom(supportSQLiteQuery).toRoomRawQuery();
        final String sql = roomRawQuery.getSql();
        return ((Number) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.SpyDao_Impl$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Integer.valueOf(SpyDao_Impl.vacuum$lambda$0(sql, roomRawQuery, (SQLiteConnection) obj));
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int vacuum$lambda$0(String str, RoomRawQuery roomRawQuery, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            roomRawQuery.getBindingFunction().invoke(sQLiteStatementPrepare);
            return sQLiteStatementPrepare.step() ? (int) sQLiteStatementPrepare.getLong(0) : 0;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final List<KClass> getRequiredConverters() {
            return CollectionsKt.emptyList();
        }
    }
}
