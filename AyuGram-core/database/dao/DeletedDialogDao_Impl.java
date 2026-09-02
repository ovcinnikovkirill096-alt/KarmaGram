package com.radolyn.ayugram.database.dao;

import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import com.radolyn.ayugram.database.entities.DeletedDialog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;

public final class DeletedDialogDao_Impl implements DeletedDialogDao {
    public static final Companion Companion = new Companion(null);
    private final RoomDatabase __db;
    private final EntityDeleteOrUpdateAdapter __deleteAdapterOfDeletedDialog;
    private final EntityInsertAdapter __insertAdapterOfDeletedDialog;

    public DeletedDialogDao_Impl(RoomDatabase __db) {
        Intrinsics.checkNotNullParameter(__db, "__db");
        this.__db = __db;
        this.__insertAdapterOfDeletedDialog = new EntityInsertAdapter() { // from class: com.radolyn.ayugram.database.dao.DeletedDialogDao_Impl.1
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR ABORT INTO `DeletedDialog` (`fakeId`,`userId`,`dialogId`,`peerId`,`folderId`,`topMessage`,`lastMessageDate`,`flags`,`entityCreateDate`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, DeletedDialog entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.bindLong(1, entity.fakeId);
                statement.bindLong(2, entity.userId);
                statement.bindLong(3, entity.dialogId);
                statement.bindLong(4, entity.peerId);
                Integer num = entity.folderId;
                if (num == null) {
                    statement.bindNull(5);
                } else {
                    statement.bindLong(5, num.intValue());
                }
                statement.bindLong(6, entity.topMessage);
                statement.bindLong(7, entity.lastMessageDate);
                statement.bindLong(8, entity.flags);
                statement.bindLong(9, entity.entityCreateDate);
            }
        };
        this.__deleteAdapterOfDeletedDialog = new EntityDeleteOrUpdateAdapter() { // from class: com.radolyn.ayugram.database.dao.DeletedDialogDao_Impl.2
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            protected String createQuery() {
                return "DELETE FROM `DeletedDialog` WHERE `fakeId` = ?";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            public void bind(SQLiteStatement statement, DeletedDialog entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.bindLong(1, entity.fakeId);
            }
        };
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedDialogDao
    public long insert(final DeletedDialog deletedDialog) {
        return ((Number) DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedDialogDao_Impl$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Long.valueOf(DeletedDialogDao_Impl.insert$lambda$0(this.f$0, deletedDialog, (SQLiteConnection) obj));
            }
        })).longValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final long insert$lambda$0(DeletedDialogDao_Impl deletedDialogDao_Impl, DeletedDialog deletedDialog, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        return deletedDialogDao_Impl.__insertAdapterOfDeletedDialog.insertAndReturnId(_connection, deletedDialog);
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedDialogDao
    public void delete(final DeletedDialog deletedDialog) {
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedDialogDao_Impl$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedDialogDao_Impl.delete$lambda$0(this.f$0, deletedDialog, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit delete$lambda$0(DeletedDialogDao_Impl deletedDialogDao_Impl, DeletedDialog deletedDialog, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        deletedDialogDao_Impl.__deleteAdapterOfDeletedDialog.handle(_connection, deletedDialog);
        return Unit.INSTANCE;
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedDialogDao
    public List<DeletedDialog> getAll(final long j) {
        final String str = "SELECT * FROM deleteddialog WHERE userId = ?";
        return (List) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedDialogDao_Impl$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedDialogDao_Impl.getAll$lambda$0(str, j, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getAll$lambda$0(String str, long j, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fakeId");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "userId");
            int columnIndexOrThrow3 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "dialogId");
            int columnIndexOrThrow4 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "peerId");
            int columnIndexOrThrow5 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "folderId");
            int columnIndexOrThrow6 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "topMessage");
            int columnIndexOrThrow7 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "lastMessageDate");
            int columnIndexOrThrow8 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "flags");
            int columnIndexOrThrow9 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "entityCreateDate");
            ArrayList arrayList = new ArrayList();
            while (sQLiteStatementPrepare.step()) {
                DeletedDialog deletedDialog = new DeletedDialog();
                deletedDialog.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                deletedDialog.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                deletedDialog.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                deletedDialog.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow5)) {
                    deletedDialog.folderId = null;
                } else {
                    deletedDialog.folderId = Integer.valueOf((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow5));
                }
                deletedDialog.topMessage = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                deletedDialog.lastMessageDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                deletedDialog.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                deletedDialog.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                arrayList.add(deletedDialog);
            }
            return arrayList;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedDialogDao
    public int getDeletedCount() {
        final String str = "SELECT COUNT(*) FROM deleteddialog";
        return ((Number) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedDialogDao_Impl$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Integer.valueOf(DeletedDialogDao_Impl.getDeletedCount$lambda$0(str, (SQLiteConnection) obj));
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int getDeletedCount$lambda$0(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            return sQLiteStatementPrepare.step() ? (int) sQLiteStatementPrepare.getLong(0) : 0;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedDialogDao
    public DeletedDialog get(final long j, final long j2) {
        final String str = "SELECT * FROM deleteddialog WHERE userId = ? AND dialogId = ?";
        return (DeletedDialog) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedDialogDao_Impl$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedDialogDao_Impl.get$lambda$0(str, j, j2, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final DeletedDialog get$lambda$0(String str, long j, long j2, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fakeId");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "userId");
            int columnIndexOrThrow3 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "dialogId");
            int columnIndexOrThrow4 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "peerId");
            int columnIndexOrThrow5 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "folderId");
            int columnIndexOrThrow6 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "topMessage");
            int columnIndexOrThrow7 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "lastMessageDate");
            int columnIndexOrThrow8 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "flags");
            int columnIndexOrThrow9 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "entityCreateDate");
            DeletedDialog deletedDialog = null;
            if (sQLiteStatementPrepare.step()) {
                DeletedDialog deletedDialog2 = new DeletedDialog();
                deletedDialog2.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                deletedDialog2.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                deletedDialog2.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                deletedDialog2.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow5)) {
                    deletedDialog2.folderId = null;
                } else {
                    deletedDialog2.folderId = Integer.valueOf((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow5));
                }
                deletedDialog2.topMessage = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                deletedDialog2.lastMessageDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                deletedDialog2.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                deletedDialog2.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                deletedDialog = deletedDialog2;
            }
            return deletedDialog;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedDialogDao
    public void updateDialogsFolder(final long j, final List<Long> list, final int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE deleteddialog SET folderId = ");
        sb.append("?");
        sb.append(" WHERE userId = ");
        sb.append("?");
        sb.append(" AND dialogId IN (");
        StringUtil.appendPlaceholders(sb, list == null ? 1 : list.size());
        sb.append(")");
        final String string = sb.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedDialogDao_Impl$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedDialogDao_Impl.updateDialogsFolder$lambda$0(string, i, j, list, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit updateDialogsFolder$lambda$0(String str, int i, long j, List list, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, i);
            sQLiteStatementPrepare.bindLong(2, j);
            int i2 = 3;
            if (list == null) {
                sQLiteStatementPrepare.bindNull(3);
            } else {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    Long l = (Long) it.next();
                    if (l == null) {
                        sQLiteStatementPrepare.bindNull(i2);
                    } else {
                        sQLiteStatementPrepare.bindLong(i2, l.longValue());
                    }
                    i2++;
                }
            }
            sQLiteStatementPrepare.step();
            return Unit.INSTANCE;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedDialogDao
    public void delete(final long j, final long j2) {
        final String str = "DELETE FROM deleteddialog WHERE dialogId = ? and userId = ?";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedDialogDao_Impl$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedDialogDao_Impl.delete$lambda$1(str, j2, j, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit delete$lambda$1(String str, long j, long j2, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.step();
            return Unit.INSTANCE;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedDialogDao
    public void deleteExisting(final long j, final List<Long> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM deleteddialog WHERE userId = ");
        sb.append("?");
        sb.append(" AND dialogId IN (");
        StringUtil.appendPlaceholders(sb, list == null ? 1 : list.size());
        sb.append(")");
        final String string = sb.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedDialogDao_Impl$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedDialogDao_Impl.deleteExisting$lambda$0(string, j, list, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit deleteExisting$lambda$0(String str, long j, List list, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            int i = 2;
            if (list == null) {
                sQLiteStatementPrepare.bindNull(2);
            } else {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    Long l = (Long) it.next();
                    if (l == null) {
                        sQLiteStatementPrepare.bindNull(i);
                    } else {
                        sQLiteStatementPrepare.bindLong(i, l.longValue());
                    }
                    i++;
                }
            }
            sQLiteStatementPrepare.step();
            return Unit.INSTANCE;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedDialogDao
    public void deleteAll() {
        final String str = "DELETE FROM deleteddialog";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedDialogDao_Impl$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedDialogDao_Impl.deleteAll$lambda$0(str, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit deleteAll$lambda$0(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.step();
            return Unit.INSTANCE;
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
