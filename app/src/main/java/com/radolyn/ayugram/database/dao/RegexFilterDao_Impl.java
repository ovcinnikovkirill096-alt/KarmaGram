package com.radolyn.ayugram.database.dao;

import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.room.util.UUIDUtil;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import com.radolyn.ayugram.database.entities.RegexFilter;
import com.radolyn.ayugram.database.entities.RegexFilterGlobalExclusion;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;

public final class RegexFilterDao_Impl implements RegexFilterDao {
    public static final Companion Companion = new Companion(null);
    private final RoomDatabase __db;
    private final EntityInsertAdapter __insertAdapterOfRegexFilter;
    private final EntityInsertAdapter __insertAdapterOfRegexFilterGlobalExclusion;
    private final EntityDeleteOrUpdateAdapter __updateAdapterOfRegexFilter;

    public RegexFilterDao_Impl(RoomDatabase __db) {
        Intrinsics.checkNotNullParameter(__db, "__db");
        this.__db = __db;
        this.__insertAdapterOfRegexFilter = new EntityInsertAdapter() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl.1
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR ABORT INTO `RegexFilter` (`id`,`text`,`enabled`,`reversed`,`caseInsensitive`,`dialogId`) VALUES (?,?,?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, RegexFilter entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                UUID id = entity.id;
                Intrinsics.checkNotNullExpressionValue(id, "id");
                statement.bindBlob(1, UUIDUtil.convertUUIDToByte(id));
                String str = entity.text;
                if (str == null) {
                    statement.bindNull(2);
                } else {
                    statement.bindText(2, str);
                }
                statement.bindLong(3, entity.enabled ? 1L : 0L);
                statement.bindLong(4, entity.reversed ? 1L : 0L);
                statement.bindLong(5, entity.caseInsensitive ? 1L : 0L);
                Long l = entity.dialogId;
                if (l == null) {
                    statement.bindNull(6);
                } else {
                    statement.bindLong(6, l.longValue());
                }
            }
        };
        this.__insertAdapterOfRegexFilterGlobalExclusion = new EntityInsertAdapter() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl.2
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR ABORT INTO `RegexFilterGlobalExclusion` (`fakeId`,`dialogId`,`filterId`) VALUES (nullif(?, 0),?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, RegexFilterGlobalExclusion entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.bindLong(1, entity.fakeId);
                statement.bindLong(2, entity.dialogId);
                UUID uuid = entity.filterId;
                if (uuid == null) {
                    statement.bindNull(3);
                } else {
                    statement.bindBlob(3, UUIDUtil.convertUUIDToByte(uuid));
                }
            }
        };
        this.__updateAdapterOfRegexFilter = new EntityDeleteOrUpdateAdapter() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl.3
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            protected String createQuery() {
                return "UPDATE OR ABORT `RegexFilter` SET `id` = ?,`text` = ?,`enabled` = ?,`reversed` = ?,`caseInsensitive` = ?,`dialogId` = ? WHERE `id` = ?";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            public void bind(SQLiteStatement statement, RegexFilter entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                UUID id = entity.id;
                Intrinsics.checkNotNullExpressionValue(id, "id");
                statement.bindBlob(1, UUIDUtil.convertUUIDToByte(id));
                String str = entity.text;
                if (str == null) {
                    statement.bindNull(2);
                } else {
                    statement.bindText(2, str);
                }
                statement.bindLong(3, entity.enabled ? 1L : 0L);
                statement.bindLong(4, entity.reversed ? 1L : 0L);
                statement.bindLong(5, entity.caseInsensitive ? 1L : 0L);
                Long l = entity.dialogId;
                if (l == null) {
                    statement.bindNull(6);
                } else {
                    statement.bindLong(6, l.longValue());
                }
                UUID id2 = entity.id;
                Intrinsics.checkNotNullExpressionValue(id2, "id");
                statement.bindBlob(7, UUIDUtil.convertUUIDToByte(id2));
            }
        };
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public void insert(final RegexFilter regexFilter) {
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.insert$lambda$0(this.f$0, regexFilter, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit insert$lambda$0(RegexFilterDao_Impl regexFilterDao_Impl, RegexFilter regexFilter, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        regexFilterDao_Impl.__insertAdapterOfRegexFilter.insert(_connection, regexFilter);
        return Unit.INSTANCE;
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public void insertExclusion(final RegexFilterGlobalExclusion regexFilterGlobalExclusion) {
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.insertExclusion$lambda$0(this.f$0, regexFilterGlobalExclusion, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit insertExclusion$lambda$0(RegexFilterDao_Impl regexFilterDao_Impl, RegexFilterGlobalExclusion regexFilterGlobalExclusion, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        regexFilterDao_Impl.__insertAdapterOfRegexFilterGlobalExclusion.insert(_connection, regexFilterGlobalExclusion);
        return Unit.INSTANCE;
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public void update(final RegexFilter regexFilter) {
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.update$lambda$0(this.f$0, regexFilter, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit update$lambda$0(RegexFilterDao_Impl regexFilterDao_Impl, RegexFilter regexFilter, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        regexFilterDao_Impl.__updateAdapterOfRegexFilter.handle(_connection, regexFilter);
        return Unit.INSTANCE;
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public List<RegexFilter> getAll() {
        final String str = "SELECT * FROM regexfilter";
        return (List) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.getAll$lambda$0(str, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getAll$lambda$0(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "id");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "text");
            int columnIndexOrThrow3 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "enabled");
            int columnIndexOrThrow4 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "reversed");
            int columnIndexOrThrow5 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "caseInsensitive");
            int columnIndexOrThrow6 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "dialogId");
            ArrayList arrayList = new ArrayList();
            while (sQLiteStatementPrepare.step()) {
                RegexFilter regexFilter = new RegexFilter();
                regexFilter.id = UUIDUtil.convertByteToUUID(sQLiteStatementPrepare.getBlob(columnIndexOrThrow));
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow2)) {
                    regexFilter.text = null;
                } else {
                    regexFilter.text = sQLiteStatementPrepare.getText(columnIndexOrThrow2);
                }
                regexFilter.enabled = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow3)) != 0;
                regexFilter.reversed = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow4)) != 0;
                regexFilter.caseInsensitive = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow5)) != 0;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow6)) {
                    regexFilter.dialogId = null;
                } else {
                    regexFilter.dialogId = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow6));
                }
                arrayList.add(regexFilter);
            }
            return arrayList;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public RegexFilter getById(final UUID uuid) {
        final String str = "SELECT * FROM regexfilter WHERE id = ?";
        return (RegexFilter) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda13
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.getById$lambda$0(str, uuid, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final RegexFilter getById$lambda$0(String str, UUID uuid, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        boolean z = true;
        try {
            if (uuid == null) {
                sQLiteStatementPrepare.bindNull(1);
            } else {
                sQLiteStatementPrepare.bindBlob(1, UUIDUtil.convertUUIDToByte(uuid));
            }
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "id");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "text");
            int columnIndexOrThrow3 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "enabled");
            int columnIndexOrThrow4 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "reversed");
            int columnIndexOrThrow5 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "caseInsensitive");
            int columnIndexOrThrow6 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "dialogId");
            RegexFilter regexFilter = null;
            if (sQLiteStatementPrepare.step()) {
                RegexFilter regexFilter2 = new RegexFilter();
                regexFilter2.id = UUIDUtil.convertByteToUUID(sQLiteStatementPrepare.getBlob(columnIndexOrThrow));
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow2)) {
                    regexFilter2.text = null;
                } else {
                    regexFilter2.text = sQLiteStatementPrepare.getText(columnIndexOrThrow2);
                }
                regexFilter2.enabled = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow3)) != 0;
                regexFilter2.reversed = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow4)) != 0;
                if (((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow5)) == 0) {
                    z = false;
                }
                regexFilter2.caseInsensitive = z;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow6)) {
                    regexFilter2.dialogId = null;
                } else {
                    regexFilter2.dialogId = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow6));
                }
                regexFilter = regexFilter2;
            }
            sQLiteStatementPrepare.close();
            return regexFilter;
        } catch (Throwable th) {
            sQLiteStatementPrepare.close();
            throw th;
        }
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public List<RegexFilter> getShared() {
        final String str = "SELECT * FROM regexfilter WHERE dialogId IS null";
        return (List) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.getShared$lambda$0(str, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getShared$lambda$0(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "id");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "text");
            int columnIndexOrThrow3 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "enabled");
            int columnIndexOrThrow4 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "reversed");
            int columnIndexOrThrow5 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "caseInsensitive");
            int columnIndexOrThrow6 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "dialogId");
            ArrayList arrayList = new ArrayList();
            while (sQLiteStatementPrepare.step()) {
                RegexFilter regexFilter = new RegexFilter();
                regexFilter.id = UUIDUtil.convertByteToUUID(sQLiteStatementPrepare.getBlob(columnIndexOrThrow));
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow2)) {
                    regexFilter.text = null;
                } else {
                    regexFilter.text = sQLiteStatementPrepare.getText(columnIndexOrThrow2);
                }
                regexFilter.enabled = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow3)) != 0;
                regexFilter.reversed = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow4)) != 0;
                regexFilter.caseInsensitive = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow5)) != 0;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow6)) {
                    regexFilter.dialogId = null;
                } else {
                    regexFilter.dialogId = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow6));
                }
                arrayList.add(regexFilter);
            }
            return arrayList;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public List<RegexFilter> getByDialogId(final long j) {
        final String str = "SELECT * FROM regexfilter WHERE dialogId = ?";
        return (List) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.getByDialogId$lambda$0(str, j, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getByDialogId$lambda$0(String str, long j, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "id");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "text");
            int columnIndexOrThrow3 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "enabled");
            int columnIndexOrThrow4 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "reversed");
            int columnIndexOrThrow5 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "caseInsensitive");
            int columnIndexOrThrow6 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "dialogId");
            ArrayList arrayList = new ArrayList();
            while (sQLiteStatementPrepare.step()) {
                RegexFilter regexFilter = new RegexFilter();
                regexFilter.id = UUIDUtil.convertByteToUUID(sQLiteStatementPrepare.getBlob(columnIndexOrThrow));
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow2)) {
                    regexFilter.text = null;
                } else {
                    regexFilter.text = sQLiteStatementPrepare.getText(columnIndexOrThrow2);
                }
                regexFilter.enabled = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow3)) != 0;
                regexFilter.reversed = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow4)) != 0;
                regexFilter.caseInsensitive = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow5)) != 0;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow6)) {
                    regexFilter.dialogId = null;
                } else {
                    regexFilter.dialogId = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow6));
                }
                arrayList.add(regexFilter);
            }
            return arrayList;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public List<RegexFilterGlobalExclusion> getAllExclusions() {
        final String str = "SELECT * FROM regexfilterglobalexclusion";
        return (List) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.getAllExclusions$lambda$0(str, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getAllExclusions$lambda$0(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fakeId");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "dialogId");
            int columnIndexOrThrow3 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "filterId");
            ArrayList arrayList = new ArrayList();
            while (sQLiteStatementPrepare.step()) {
                RegexFilterGlobalExclusion regexFilterGlobalExclusion = new RegexFilterGlobalExclusion();
                regexFilterGlobalExclusion.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                regexFilterGlobalExclusion.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow3)) {
                    regexFilterGlobalExclusion.filterId = null;
                } else {
                    regexFilterGlobalExclusion.filterId = UUIDUtil.convertByteToUUID(sQLiteStatementPrepare.getBlob(columnIndexOrThrow3));
                }
                arrayList.add(regexFilterGlobalExclusion);
            }
            return arrayList;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public List<RegexFilter> getExcludedByDialogId(final long j) {
        final String str = "SELECT * FROM regexfilter WHERE id IN (SELECT filterId FROM regexfilterglobalexclusion WHERE dialogId = ?)";
        return (List) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.getExcludedByDialogId$lambda$0(str, j, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getExcludedByDialogId$lambda$0(String str, long j, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "id");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "text");
            int columnIndexOrThrow3 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "enabled");
            int columnIndexOrThrow4 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "reversed");
            int columnIndexOrThrow5 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "caseInsensitive");
            int columnIndexOrThrow6 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "dialogId");
            ArrayList arrayList = new ArrayList();
            while (sQLiteStatementPrepare.step()) {
                RegexFilter regexFilter = new RegexFilter();
                regexFilter.id = UUIDUtil.convertByteToUUID(sQLiteStatementPrepare.getBlob(columnIndexOrThrow));
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow2)) {
                    regexFilter.text = null;
                } else {
                    regexFilter.text = sQLiteStatementPrepare.getText(columnIndexOrThrow2);
                }
                regexFilter.enabled = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow3)) != 0;
                regexFilter.reversed = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow4)) != 0;
                regexFilter.caseInsensitive = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow5)) != 0;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow6)) {
                    regexFilter.dialogId = null;
                } else {
                    regexFilter.dialogId = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow6));
                }
                arrayList.add(regexFilter);
            }
            return arrayList;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public int getCount() {
        final String str = "SELECT COUNT(*) FROM regexfilter";
        return ((Number) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Integer.valueOf(RegexFilterDao_Impl.getCount$lambda$0(str, (SQLiteConnection) obj));
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int getCount$lambda$0(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            return sQLiteStatementPrepare.step() ? (int) sQLiteStatementPrepare.getLong(0) : 0;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public void delete(final UUID uuid) {
        final String str = "DELETE FROM regexfilter WHERE id = ?";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda12
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.delete$lambda$0(str, uuid, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit delete$lambda$0(String str, UUID uuid, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            if (uuid == null) {
                sQLiteStatementPrepare.bindNull(1);
            } else {
                sQLiteStatementPrepare.bindBlob(1, UUIDUtil.convertUUIDToByte(uuid));
            }
            sQLiteStatementPrepare.step();
            sQLiteStatementPrepare.close();
            return Unit.INSTANCE;
        } catch (Throwable th) {
            sQLiteStatementPrepare.close();
            throw th;
        }
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public void deleteExclusionsByFilterId(final UUID uuid) {
        final String str = "DELETE FROM regexfilterglobalexclusion WHERE filterId = ?";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.deleteExclusionsByFilterId$lambda$0(str, uuid, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit deleteExclusionsByFilterId$lambda$0(String str, UUID uuid, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            if (uuid == null) {
                sQLiteStatementPrepare.bindNull(1);
            } else {
                sQLiteStatementPrepare.bindBlob(1, UUIDUtil.convertUUIDToByte(uuid));
            }
            sQLiteStatementPrepare.step();
            sQLiteStatementPrepare.close();
            return Unit.INSTANCE;
        } catch (Throwable th) {
            sQLiteStatementPrepare.close();
            throw th;
        }
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public void deleteExclusion(final long j, final UUID uuid) {
        final String str = "DELETE FROM regexfilterglobalexclusion WHERE dialogId = ? AND filterId = ?";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.deleteExclusion$lambda$0(str, j, uuid, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit deleteExclusion$lambda$0(String str, long j, UUID uuid, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            if (uuid == null) {
                sQLiteStatementPrepare.bindNull(2);
            } else {
                sQLiteStatementPrepare.bindBlob(2, UUIDUtil.convertUUIDToByte(uuid));
            }
            sQLiteStatementPrepare.step();
            return Unit.INSTANCE;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public void deleteAllFilters() {
        final String str = "DELETE FROM regexfilter";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.deleteAllFilters$lambda$0(str, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit deleteAllFilters$lambda$0(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.step();
            return Unit.INSTANCE;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.RegexFilterDao
    public void deleteAllExclusions() {
        final String str = "DELETE FROM regexfilterglobalexclusion";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.RegexFilterDao_Impl$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RegexFilterDao_Impl.deleteAllExclusions$lambda$0(str, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit deleteAllExclusions$lambda$0(String str, SQLiteConnection _connection) {
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
