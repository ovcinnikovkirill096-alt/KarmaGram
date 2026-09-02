package com.radolyn.ayugram.database.dao;

import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import com.radolyn.ayugram.database.entities.EditedMessage;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;

public final class EditedMessageDao_Impl implements EditedMessageDao {
    public static final Companion Companion = new Companion(null);
    private final RoomDatabase __db;
    private final EntityInsertAdapter __insertAdapterOfEditedMessage;

    public EditedMessageDao_Impl(RoomDatabase __db) {
        Intrinsics.checkNotNullParameter(__db, "__db");
        this.__db = __db;
        this.__insertAdapterOfEditedMessage = new EntityInsertAdapter() { // from class: com.radolyn.ayugram.database.dao.EditedMessageDao_Impl.1
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR ABORT INTO `EditedMessage` (`fakeId`,`userId`,`dialogId`,`groupedId`,`peerId`,`fromId`,`topicId`,`messageId`,`date`,`flags`,`editDate`,`views`,`fwdFlags`,`fwdFromId`,`fwdName`,`fwdDate`,`fwdPostAuthor`,`postAuthor`,`replyFlags`,`replyMessageId`,`replyPeerId`,`replyTopId`,`replyForumTopic`,`replySerialized`,`replyMarkupSerialized`,`entityCreateDate`,`text`,`textEntities`,`mediaPath`,`hqThumbPath`,`documentType`,`documentSerialized`,`thumbsSerialized`,`documentAttributesSerialized`,`mimeType`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, EditedMessage entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.bindLong(1, entity.fakeId);
                statement.bindLong(2, entity.userId);
                statement.bindLong(3, entity.dialogId);
                statement.bindLong(4, entity.groupedId);
                statement.bindLong(5, entity.peerId);
                statement.bindLong(6, entity.fromId);
                statement.bindLong(7, entity.topicId);
                statement.bindLong(8, entity.messageId);
                statement.bindLong(9, entity.date);
                statement.bindLong(10, entity.flags);
                statement.bindLong(11, entity.editDate);
                statement.bindLong(12, entity.views);
                statement.bindLong(13, entity.fwdFlags);
                statement.bindLong(14, entity.fwdFromId);
                String str = entity.fwdName;
                if (str == null) {
                    statement.bindNull(15);
                } else {
                    statement.bindText(15, str);
                }
                statement.bindLong(16, entity.fwdDate);
                String str2 = entity.fwdPostAuthor;
                if (str2 == null) {
                    statement.bindNull(17);
                } else {
                    statement.bindText(17, str2);
                }
                String str3 = entity.postAuthor;
                if (str3 == null) {
                    statement.bindNull(18);
                } else {
                    statement.bindText(18, str3);
                }
                statement.bindLong(19, entity.replyFlags);
                statement.bindLong(20, entity.replyMessageId);
                statement.bindLong(21, entity.replyPeerId);
                statement.bindLong(22, entity.replyTopId);
                statement.bindLong(23, entity.replyForumTopic ? 1L : 0L);
                byte[] bArr = entity.replySerialized;
                if (bArr == null) {
                    statement.bindNull(24);
                } else {
                    statement.bindBlob(24, bArr);
                }
                byte[] bArr2 = entity.replyMarkupSerialized;
                if (bArr2 == null) {
                    statement.bindNull(25);
                } else {
                    statement.bindBlob(25, bArr2);
                }
                statement.bindLong(26, entity.entityCreateDate);
                String str4 = entity.text;
                if (str4 == null) {
                    statement.bindNull(27);
                } else {
                    statement.bindText(27, str4);
                }
                byte[] bArr3 = entity.textEntities;
                if (bArr3 == null) {
                    statement.bindNull(28);
                } else {
                    statement.bindBlob(28, bArr3);
                }
                String str5 = entity.mediaPath;
                if (str5 == null) {
                    statement.bindNull(29);
                } else {
                    statement.bindText(29, str5);
                }
                String str6 = entity.hqThumbPath;
                if (str6 == null) {
                    statement.bindNull(30);
                } else {
                    statement.bindText(30, str6);
                }
                statement.bindLong(31, entity.documentType);
                byte[] bArr4 = entity.documentSerialized;
                if (bArr4 == null) {
                    statement.bindNull(32);
                } else {
                    statement.bindBlob(32, bArr4);
                }
                byte[] bArr5 = entity.thumbsSerialized;
                if (bArr5 == null) {
                    statement.bindNull(33);
                } else {
                    statement.bindBlob(33, bArr5);
                }
                byte[] bArr6 = entity.documentAttributesSerialized;
                if (bArr6 == null) {
                    statement.bindNull(34);
                } else {
                    statement.bindBlob(34, bArr6);
                }
                String str7 = entity.mimeType;
                if (str7 == null) {
                    statement.bindNull(35);
                } else {
                    statement.bindText(35, str7);
                }
            }
        };
    }

    @Override // com.radolyn.ayugram.database.dao.EditedMessageDao
    public void insert(final EditedMessage editedMessage) {
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.EditedMessageDao_Impl$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return EditedMessageDao_Impl.insert$lambda$0(this.f$0, editedMessage, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit insert$lambda$0(EditedMessageDao_Impl editedMessageDao_Impl, EditedMessage editedMessage, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        editedMessageDao_Impl.__insertAdapterOfEditedMessage.insert(_connection, editedMessage);
        return Unit.INSTANCE;
    }

    @Override // com.radolyn.ayugram.database.dao.EditedMessageDao
    public List<EditedMessage> getAllRevisions(final long j, final long j2, final long j3, final int i, final int i2) {
        final String str = "SELECT * FROM editedmessage WHERE userId = ? AND dialogId = ? AND messageId = ? AND fakeId < ? ORDER BY entityCreateDate DESC LIMIT ?";
        return (List) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.EditedMessageDao_Impl$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return EditedMessageDao_Impl.getAllRevisions$lambda$0(str, j, j2, j3, i, i2, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getAllRevisions$lambda$0(String str, long j, long j2, long j3, int i, int i2, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.bindLong(3, j3);
            sQLiteStatementPrepare.bindLong(4, i);
            sQLiteStatementPrepare.bindLong(5, i2);
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fakeId");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "userId");
            int columnIndexOrThrow3 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "dialogId");
            int columnIndexOrThrow4 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "groupedId");
            int columnIndexOrThrow5 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "peerId");
            int columnIndexOrThrow6 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fromId");
            int columnIndexOrThrow7 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "topicId");
            int columnIndexOrThrow8 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "messageId");
            int columnIndexOrThrow9 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "date");
            int columnIndexOrThrow10 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "flags");
            int columnIndexOrThrow11 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "editDate");
            int columnIndexOrThrow12 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "views");
            int columnIndexOrThrow13 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fwdFlags");
            int columnIndexOrThrow14 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fwdFromId");
            int columnIndexOrThrow15 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fwdName");
            int columnIndexOrThrow16 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fwdDate");
            int columnIndexOrThrow17 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fwdPostAuthor");
            int columnIndexOrThrow18 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "postAuthor");
            int columnIndexOrThrow19 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replyFlags");
            int columnIndexOrThrow20 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replyMessageId");
            int columnIndexOrThrow21 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replyPeerId");
            int columnIndexOrThrow22 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replyTopId");
            int columnIndexOrThrow23 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replyForumTopic");
            int columnIndexOrThrow24 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replySerialized");
            int columnIndexOrThrow25 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replyMarkupSerialized");
            int columnIndexOrThrow26 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "entityCreateDate");
            int columnIndexOrThrow27 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "text");
            int columnIndexOrThrow28 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "textEntities");
            int columnIndexOrThrow29 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "mediaPath");
            int columnIndexOrThrow30 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "hqThumbPath");
            int columnIndexOrThrow31 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "documentType");
            int columnIndexOrThrow32 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "documentSerialized");
            int columnIndexOrThrow33 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "thumbsSerialized");
            int columnIndexOrThrow34 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "documentAttributesSerialized");
            int columnIndexOrThrow35 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "mimeType");
            ArrayList arrayList = new ArrayList();
            while (sQLiteStatementPrepare.step()) {
                ArrayList arrayList2 = arrayList;
                EditedMessage editedMessage = new EditedMessage();
                int i3 = columnIndexOrThrow14;
                editedMessage.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                editedMessage.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                editedMessage.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                editedMessage.groupedId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                editedMessage.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
                editedMessage.fromId = sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                editedMessage.topicId = sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                editedMessage.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                editedMessage.date = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                editedMessage.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow10);
                editedMessage.editDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow11);
                editedMessage.views = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow12);
                int i4 = columnIndexOrThrow;
                columnIndexOrThrow13 = columnIndexOrThrow13;
                int i5 = columnIndexOrThrow2;
                editedMessage.fwdFlags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow13);
                int i6 = columnIndexOrThrow3;
                editedMessage.fwdFromId = sQLiteStatementPrepare.getLong(i3);
                int i7 = columnIndexOrThrow15;
                if (sQLiteStatementPrepare.isNull(i7)) {
                    editedMessage.fwdName = null;
                } else {
                    editedMessage.fwdName = sQLiteStatementPrepare.getText(i7);
                }
                int i8 = columnIndexOrThrow16;
                editedMessage.fwdDate = (int) sQLiteStatementPrepare.getLong(i8);
                columnIndexOrThrow17 = columnIndexOrThrow17;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow17)) {
                    editedMessage.fwdPostAuthor = null;
                } else {
                    editedMessage.fwdPostAuthor = sQLiteStatementPrepare.getText(columnIndexOrThrow17);
                }
                int i9 = columnIndexOrThrow18;
                if (sQLiteStatementPrepare.isNull(i9)) {
                    editedMessage.postAuthor = null;
                } else {
                    editedMessage.postAuthor = sQLiteStatementPrepare.getText(i9);
                }
                int i10 = columnIndexOrThrow19;
                columnIndexOrThrow18 = i9;
                editedMessage.replyFlags = (int) sQLiteStatementPrepare.getLong(i10);
                int i11 = columnIndexOrThrow20;
                int i12 = columnIndexOrThrow4;
                editedMessage.replyMessageId = (int) sQLiteStatementPrepare.getLong(i11);
                int i13 = columnIndexOrThrow21;
                columnIndexOrThrow20 = i11;
                editedMessage.replyPeerId = sQLiteStatementPrepare.getLong(i13);
                int i14 = columnIndexOrThrow22;
                columnIndexOrThrow21 = i13;
                editedMessage.replyTopId = (int) sQLiteStatementPrepare.getLong(i14);
                int i15 = columnIndexOrThrow23;
                editedMessage.replyForumTopic = ((int) sQLiteStatementPrepare.getLong(i15)) != 0;
                int i16 = columnIndexOrThrow24;
                if (sQLiteStatementPrepare.isNull(i16)) {
                    editedMessage.replySerialized = null;
                } else {
                    editedMessage.replySerialized = sQLiteStatementPrepare.getBlob(i16);
                }
                int i17 = columnIndexOrThrow25;
                if (sQLiteStatementPrepare.isNull(i17)) {
                    editedMessage.replyMarkupSerialized = null;
                } else {
                    editedMessage.replyMarkupSerialized = sQLiteStatementPrepare.getBlob(i17);
                }
                columnIndexOrThrow23 = i15;
                columnIndexOrThrow26 = columnIndexOrThrow26;
                editedMessage.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow26);
                columnIndexOrThrow27 = columnIndexOrThrow27;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow27)) {
                    editedMessage.text = null;
                } else {
                    editedMessage.text = sQLiteStatementPrepare.getText(columnIndexOrThrow27);
                }
                int i18 = columnIndexOrThrow28;
                if (sQLiteStatementPrepare.isNull(i18)) {
                    editedMessage.textEntities = null;
                } else {
                    editedMessage.textEntities = sQLiteStatementPrepare.getBlob(i18);
                }
                columnIndexOrThrow29 = columnIndexOrThrow29;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow29)) {
                    editedMessage.mediaPath = null;
                } else {
                    editedMessage.mediaPath = sQLiteStatementPrepare.getText(columnIndexOrThrow29);
                }
                int i19 = columnIndexOrThrow30;
                if (sQLiteStatementPrepare.isNull(i19)) {
                    editedMessage.hqThumbPath = null;
                } else {
                    editedMessage.hqThumbPath = sQLiteStatementPrepare.getText(i19);
                }
                columnIndexOrThrow30 = i19;
                columnIndexOrThrow28 = i18;
                columnIndexOrThrow31 = columnIndexOrThrow31;
                editedMessage.documentType = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow31);
                columnIndexOrThrow32 = columnIndexOrThrow32;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow32)) {
                    editedMessage.documentSerialized = null;
                } else {
                    editedMessage.documentSerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow32);
                }
                int i20 = columnIndexOrThrow33;
                if (sQLiteStatementPrepare.isNull(i20)) {
                    editedMessage.thumbsSerialized = null;
                } else {
                    editedMessage.thumbsSerialized = sQLiteStatementPrepare.getBlob(i20);
                }
                columnIndexOrThrow34 = columnIndexOrThrow34;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow34)) {
                    editedMessage.documentAttributesSerialized = null;
                } else {
                    editedMessage.documentAttributesSerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow34);
                }
                int i21 = columnIndexOrThrow35;
                if (sQLiteStatementPrepare.isNull(i21)) {
                    editedMessage.mimeType = null;
                } else {
                    editedMessage.mimeType = sQLiteStatementPrepare.getText(i21);
                }
                arrayList2.add(editedMessage);
                columnIndexOrThrow4 = i12;
                columnIndexOrThrow19 = i10;
                columnIndexOrThrow22 = i14;
                columnIndexOrThrow24 = i16;
                columnIndexOrThrow25 = i17;
                arrayList = arrayList2;
                columnIndexOrThrow35 = i21;
                columnIndexOrThrow33 = i20;
                columnIndexOrThrow = i4;
                columnIndexOrThrow2 = i5;
                columnIndexOrThrow3 = i6;
                columnIndexOrThrow14 = i3;
                columnIndexOrThrow15 = i7;
                columnIndexOrThrow16 = i8;
            }
            return arrayList;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.EditedMessageDao
    public EditedMessage getLastRevision(final long j, final long j2, final long j3) {
        final String str = "SELECT * FROM editedmessage WHERE userId = ? AND dialogId = ? AND messageId = ? ORDER BY entityCreateDate DESC LIMIT 1";
        return (EditedMessage) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.EditedMessageDao_Impl$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return EditedMessageDao_Impl.getLastRevision$lambda$0(str, j, j2, j3, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final EditedMessage getLastRevision$lambda$0(String str, long j, long j2, long j3, SQLiteConnection _connection) {
        EditedMessage editedMessage;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.bindLong(3, j3);
            int columnIndexOrThrow = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fakeId");
            int columnIndexOrThrow2 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "userId");
            int columnIndexOrThrow3 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "dialogId");
            int columnIndexOrThrow4 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "groupedId");
            int columnIndexOrThrow5 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "peerId");
            int columnIndexOrThrow6 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fromId");
            int columnIndexOrThrow7 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "topicId");
            int columnIndexOrThrow8 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "messageId");
            int columnIndexOrThrow9 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "date");
            int columnIndexOrThrow10 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "flags");
            int columnIndexOrThrow11 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "editDate");
            int columnIndexOrThrow12 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "views");
            int columnIndexOrThrow13 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fwdFlags");
            int columnIndexOrThrow14 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fwdFromId");
            int columnIndexOrThrow15 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fwdName");
            int columnIndexOrThrow16 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fwdDate");
            int columnIndexOrThrow17 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "fwdPostAuthor");
            int columnIndexOrThrow18 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "postAuthor");
            int columnIndexOrThrow19 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replyFlags");
            int columnIndexOrThrow20 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replyMessageId");
            int columnIndexOrThrow21 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replyPeerId");
            int columnIndexOrThrow22 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replyTopId");
            int columnIndexOrThrow23 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replyForumTopic");
            int columnIndexOrThrow24 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replySerialized");
            int columnIndexOrThrow25 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "replyMarkupSerialized");
            int columnIndexOrThrow26 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "entityCreateDate");
            int columnIndexOrThrow27 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "text");
            int columnIndexOrThrow28 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "textEntities");
            int columnIndexOrThrow29 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "mediaPath");
            int columnIndexOrThrow30 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "hqThumbPath");
            int columnIndexOrThrow31 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "documentType");
            int columnIndexOrThrow32 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "documentSerialized");
            int columnIndexOrThrow33 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "thumbsSerialized");
            int columnIndexOrThrow34 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "documentAttributesSerialized");
            int columnIndexOrThrow35 = SQLiteStatementUtil.getColumnIndexOrThrow(sQLiteStatementPrepare, "mimeType");
            if (sQLiteStatementPrepare.step()) {
                editedMessage = new EditedMessage();
                editedMessage.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                editedMessage.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                editedMessage.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                editedMessage.groupedId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                editedMessage.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
                editedMessage.fromId = sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                editedMessage.topicId = sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                editedMessage.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                editedMessage.date = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                editedMessage.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow10);
                editedMessage.editDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow11);
                editedMessage.views = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow12);
                editedMessage.fwdFlags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow13);
                editedMessage.fwdFromId = sQLiteStatementPrepare.getLong(columnIndexOrThrow14);
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow15)) {
                    editedMessage.fwdName = null;
                } else {
                    editedMessage.fwdName = sQLiteStatementPrepare.getText(columnIndexOrThrow15);
                }
                editedMessage.fwdDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow16);
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow17)) {
                    editedMessage.fwdPostAuthor = null;
                } else {
                    editedMessage.fwdPostAuthor = sQLiteStatementPrepare.getText(columnIndexOrThrow17);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow18)) {
                    editedMessage.postAuthor = null;
                } else {
                    editedMessage.postAuthor = sQLiteStatementPrepare.getText(columnIndexOrThrow18);
                }
                editedMessage.replyFlags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow19);
                editedMessage.replyMessageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow20);
                editedMessage.replyPeerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow21);
                editedMessage.replyTopId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow22);
                editedMessage.replyForumTopic = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow23)) != 0;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow24)) {
                    editedMessage.replySerialized = null;
                } else {
                    editedMessage.replySerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow24);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow25)) {
                    editedMessage.replyMarkupSerialized = null;
                } else {
                    editedMessage.replyMarkupSerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow25);
                }
                editedMessage.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow26);
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow27)) {
                    editedMessage.text = null;
                } else {
                    editedMessage.text = sQLiteStatementPrepare.getText(columnIndexOrThrow27);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow28)) {
                    editedMessage.textEntities = null;
                } else {
                    editedMessage.textEntities = sQLiteStatementPrepare.getBlob(columnIndexOrThrow28);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow29)) {
                    editedMessage.mediaPath = null;
                } else {
                    editedMessage.mediaPath = sQLiteStatementPrepare.getText(columnIndexOrThrow29);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow30)) {
                    editedMessage.hqThumbPath = null;
                } else {
                    editedMessage.hqThumbPath = sQLiteStatementPrepare.getText(columnIndexOrThrow30);
                }
                editedMessage.documentType = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow31);
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow32)) {
                    editedMessage.documentSerialized = null;
                } else {
                    editedMessage.documentSerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow32);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow33)) {
                    editedMessage.thumbsSerialized = null;
                } else {
                    editedMessage.thumbsSerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow33);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow34)) {
                    editedMessage.documentAttributesSerialized = null;
                } else {
                    editedMessage.documentAttributesSerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow34);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow35)) {
                    editedMessage.mimeType = null;
                } else {
                    editedMessage.mimeType = sQLiteStatementPrepare.getText(columnIndexOrThrow35);
                }
            } else {
                editedMessage = null;
            }
            return editedMessage;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.EditedMessageDao
    public boolean hasAnyRevisions(final long j, final long j2, final long j3) {
        final String str = "SELECT EXISTS(SELECT * FROM editedmessage WHERE userId = ? AND dialogId = ? AND messageId = ?)";
        return ((Boolean) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.EditedMessageDao_Impl$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(EditedMessageDao_Impl.hasAnyRevisions$lambda$0(str, j, j2, j3, (SQLiteConnection) obj));
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean hasAnyRevisions$lambda$0(String str, long j, long j2, long j3, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.bindLong(3, j3);
            boolean z = false;
            if (sQLiteStatementPrepare.step()) {
                z = ((int) sQLiteStatementPrepare.getLong(0)) != 0;
            }
            return z;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.EditedMessageDao
    public void updateMediaPathForRevisionsBetweenDates(final long j, final long j2, final long j3, final String str, final String str2) {
        final String str3 = "UPDATE editedmessage SET mediaPath = ? WHERE userId = ? AND dialogId = ? AND messageId = ? AND mediaPath = ?";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.EditedMessageDao_Impl$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return EditedMessageDao_Impl.updateMediaPathForRevisionsBetweenDates$lambda$0(str3, str2, j, j2, j3, str, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit updateMediaPathForRevisionsBetweenDates$lambda$0(String str, String str2, long j, long j2, long j3, String str3, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            if (str2 == null) {
                sQLiteStatementPrepare.bindNull(1);
            } else {
                sQLiteStatementPrepare.bindText(1, str2);
            }
            sQLiteStatementPrepare.bindLong(2, j);
            sQLiteStatementPrepare.bindLong(3, j2);
            sQLiteStatementPrepare.bindLong(4, j3);
            if (str3 == null) {
                sQLiteStatementPrepare.bindNull(5);
            } else {
                sQLiteStatementPrepare.bindText(5, str3);
            }
            sQLiteStatementPrepare.step();
            sQLiteStatementPrepare.close();
            return Unit.INSTANCE;
        } catch (Throwable th) {
            sQLiteStatementPrepare.close();
            throw th;
        }
    }

    @Override // com.radolyn.ayugram.database.dao.EditedMessageDao
    public void deleteMedia(final long j) {
        final String str = "UPDATE editedmessage SET mediaPath = NULL, documentType = 0 WHERE fakeId = ?";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.EditedMessageDao_Impl$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return EditedMessageDao_Impl.deleteMedia$lambda$0(str, j, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit deleteMedia$lambda$0(String str, long j, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.step();
            return Unit.INSTANCE;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.EditedMessageDao
    public void deleteAll() {
        final String str = "DELETE FROM editedmessage";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.EditedMessageDao_Impl$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return EditedMessageDao_Impl.deleteAll$lambda$0(str, (SQLiteConnection) obj);
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
