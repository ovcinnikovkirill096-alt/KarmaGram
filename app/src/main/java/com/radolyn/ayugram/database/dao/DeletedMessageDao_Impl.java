package com.radolyn.ayugram.database.dao;

import androidx.collection.LongSparseArray;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import com.radolyn.ayugram.database.entities.DeletedMessage;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.database.entities.DeletedMessageReaction;
import com.radolyn.ayugram.database.other.CleanUpUnion;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;

public final class DeletedMessageDao_Impl implements DeletedMessageDao {
    public static final Companion Companion = new Companion(null);
    private final RoomDatabase __db;
    private final EntityInsertAdapter __insertAdapterOfDeletedMessage;
    private final EntityInsertAdapter __insertAdapterOfDeletedMessageReaction;

    public DeletedMessageDao_Impl(RoomDatabase __db) {
        Intrinsics.checkNotNullParameter(__db, "__db");
        this.__db = __db;
        this.__insertAdapterOfDeletedMessage = new EntityInsertAdapter() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl.1
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR ABORT INTO `DeletedMessage` (`fakeId`,`userId`,`dialogId`,`groupedId`,`peerId`,`fromId`,`topicId`,`messageId`,`date`,`flags`,`editDate`,`views`,`fwdFlags`,`fwdFromId`,`fwdName`,`fwdDate`,`fwdPostAuthor`,`postAuthor`,`replyFlags`,`replyMessageId`,`replyPeerId`,`replyTopId`,`replyForumTopic`,`replySerialized`,`replyMarkupSerialized`,`entityCreateDate`,`text`,`textEntities`,`mediaPath`,`hqThumbPath`,`documentType`,`documentSerialized`,`thumbsSerialized`,`documentAttributesSerialized`,`mimeType`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, DeletedMessage entity) {
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
        this.__insertAdapterOfDeletedMessageReaction = new EntityInsertAdapter() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl.2
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR ABORT INTO `DeletedMessageReaction` (`fakeReactionId`,`deletedMessageId`,`emoticon`,`documentId`,`isCustom`,`isPaid`,`count`,`selfSelected`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, DeletedMessageReaction entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.bindLong(1, entity.fakeReactionId);
                statement.bindLong(2, entity.deletedMessageId);
                String str = entity.emoticon;
                if (str == null) {
                    statement.bindNull(3);
                } else {
                    statement.bindText(3, str);
                }
                statement.bindLong(4, entity.documentId);
                statement.bindLong(5, entity.isCustom ? 1L : 0L);
                statement.bindLong(6, entity.isPaid ? 1L : 0L);
                statement.bindLong(7, entity.count);
                statement.bindLong(8, entity.selfSelected ? 1L : 0L);
            }
        };
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public long insert(final DeletedMessage deletedMessage) {
        return ((Number) DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda13
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Long.valueOf(DeletedMessageDao_Impl.insert$lambda$0(this.f$0, deletedMessage, (SQLiteConnection) obj));
            }
        })).longValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final long insert$lambda$0(DeletedMessageDao_Impl deletedMessageDao_Impl, DeletedMessage deletedMessage, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        return deletedMessageDao_Impl.__insertAdapterOfDeletedMessage.insertAndReturnId(_connection, deletedMessage);
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public void insertReaction(final DeletedMessageReaction deletedMessageReaction) {
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedMessageDao_Impl.insertReaction$lambda$0(this.f$0, deletedMessageReaction, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit insertReaction$lambda$0(DeletedMessageDao_Impl deletedMessageDao_Impl, DeletedMessageReaction deletedMessageReaction, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        deletedMessageDao_Impl.__insertAdapterOfDeletedMessageReaction.insert(_connection, deletedMessageReaction);
        return Unit.INSTANCE;
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public DeletedMessageFull getMessage(final long j, final long j2, final int i) {
        final String str = "SELECT * FROM deletedmessage WHERE userId = ? AND dialogId = ? AND messageId = ?";
        return (DeletedMessageFull) DBUtil.performBlocking(this.__db, true, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedMessageDao_Impl.getMessage$lambda$0(str, j, j2, i, this, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:186:0x04d0  */
    /* JADX WARN: Code duplicated, block: B:187:0x04d2 A[Catch: all -> 0x0172, TryCatch #0 {all -> 0x0172, blocks: (B:3:0x0010, B:4:0x013f, B:6:0x0145, B:11:0x0158, B:13:0x0162, B:9:0x014e, B:17:0x0178, B:19:0x0186, B:21:0x018c, B:23:0x0192, B:25:0x0198, B:27:0x019e, B:29:0x01a4, B:31:0x01aa, B:33:0x01b0, B:35:0x01b6, B:37:0x01bc, B:39:0x01c2, B:41:0x01ca, B:43:0x01d2, B:45:0x01da, B:47:0x01e2, B:49:0x01ec, B:51:0x01f6, B:53:0x0200, B:55:0x020a, B:57:0x0214, B:59:0x021e, B:61:0x0228, B:63:0x0232, B:65:0x023c, B:67:0x0246, B:69:0x0250, B:71:0x025a, B:73:0x0264, B:75:0x026e, B:77:0x0278, B:79:0x0282, B:81:0x028c, B:83:0x0296, B:85:0x02a0, B:87:0x02aa, B:116:0x0320, B:118:0x038f, B:121:0x039c, B:123:0x03ab, B:126:0x03b8, B:128:0x03be, B:131:0x03cb, B:135:0x03f8, B:137:0x0402, B:140:0x040f, B:142:0x0415, B:145:0x0422, B:147:0x0431, B:150:0x043e, B:152:0x0444, B:155:0x0451, B:157:0x0457, B:160:0x0464, B:162:0x046a, B:165:0x0477, B:167:0x0486, B:170:0x0493, B:172:0x0499, B:175:0x04a6, B:177:0x04ac, B:180:0x04b9, B:182:0x04bf, B:184:0x04ca, B:189:0x04dc, B:191:0x04e8, B:195:0x04f8, B:192:0x04eb, B:193:0x04f2, B:194:0x04f3, B:187:0x04d2, B:183:0x04c3, B:179:0x04b2, B:174:0x049f, B:169:0x048c, B:164:0x0470, B:159:0x045d, B:154:0x044a, B:149:0x0437, B:144:0x041b, B:139:0x0408, B:130:0x03c4, B:125:0x03b1, B:120:0x0395), top: B:202:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:189:0x04dc A[Catch: all -> 0x0172, TryCatch #0 {all -> 0x0172, blocks: (B:3:0x0010, B:4:0x013f, B:6:0x0145, B:11:0x0158, B:13:0x0162, B:9:0x014e, B:17:0x0178, B:19:0x0186, B:21:0x018c, B:23:0x0192, B:25:0x0198, B:27:0x019e, B:29:0x01a4, B:31:0x01aa, B:33:0x01b0, B:35:0x01b6, B:37:0x01bc, B:39:0x01c2, B:41:0x01ca, B:43:0x01d2, B:45:0x01da, B:47:0x01e2, B:49:0x01ec, B:51:0x01f6, B:53:0x0200, B:55:0x020a, B:57:0x0214, B:59:0x021e, B:61:0x0228, B:63:0x0232, B:65:0x023c, B:67:0x0246, B:69:0x0250, B:71:0x025a, B:73:0x0264, B:75:0x026e, B:77:0x0278, B:79:0x0282, B:81:0x028c, B:83:0x0296, B:85:0x02a0, B:87:0x02aa, B:116:0x0320, B:118:0x038f, B:121:0x039c, B:123:0x03ab, B:126:0x03b8, B:128:0x03be, B:131:0x03cb, B:135:0x03f8, B:137:0x0402, B:140:0x040f, B:142:0x0415, B:145:0x0422, B:147:0x0431, B:150:0x043e, B:152:0x0444, B:155:0x0451, B:157:0x0457, B:160:0x0464, B:162:0x046a, B:165:0x0477, B:167:0x0486, B:170:0x0493, B:172:0x0499, B:175:0x04a6, B:177:0x04ac, B:180:0x04b9, B:182:0x04bf, B:184:0x04ca, B:189:0x04dc, B:191:0x04e8, B:195:0x04f8, B:192:0x04eb, B:193:0x04f2, B:194:0x04f3, B:187:0x04d2, B:183:0x04c3, B:179:0x04b2, B:174:0x049f, B:169:0x048c, B:164:0x0470, B:159:0x045d, B:154:0x044a, B:149:0x0437, B:144:0x041b, B:139:0x0408, B:130:0x03c4, B:125:0x03b1, B:120:0x0395), top: B:202:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:191:0x04e8 A[Catch: all -> 0x0172, TryCatch #0 {all -> 0x0172, blocks: (B:3:0x0010, B:4:0x013f, B:6:0x0145, B:11:0x0158, B:13:0x0162, B:9:0x014e, B:17:0x0178, B:19:0x0186, B:21:0x018c, B:23:0x0192, B:25:0x0198, B:27:0x019e, B:29:0x01a4, B:31:0x01aa, B:33:0x01b0, B:35:0x01b6, B:37:0x01bc, B:39:0x01c2, B:41:0x01ca, B:43:0x01d2, B:45:0x01da, B:47:0x01e2, B:49:0x01ec, B:51:0x01f6, B:53:0x0200, B:55:0x020a, B:57:0x0214, B:59:0x021e, B:61:0x0228, B:63:0x0232, B:65:0x023c, B:67:0x0246, B:69:0x0250, B:71:0x025a, B:73:0x0264, B:75:0x026e, B:77:0x0278, B:79:0x0282, B:81:0x028c, B:83:0x0296, B:85:0x02a0, B:87:0x02aa, B:116:0x0320, B:118:0x038f, B:121:0x039c, B:123:0x03ab, B:126:0x03b8, B:128:0x03be, B:131:0x03cb, B:135:0x03f8, B:137:0x0402, B:140:0x040f, B:142:0x0415, B:145:0x0422, B:147:0x0431, B:150:0x043e, B:152:0x0444, B:155:0x0451, B:157:0x0457, B:160:0x0464, B:162:0x046a, B:165:0x0477, B:167:0x0486, B:170:0x0493, B:172:0x0499, B:175:0x04a6, B:177:0x04ac, B:180:0x04b9, B:182:0x04bf, B:184:0x04ca, B:189:0x04dc, B:191:0x04e8, B:195:0x04f8, B:192:0x04eb, B:193:0x04f2, B:194:0x04f3, B:187:0x04d2, B:183:0x04c3, B:179:0x04b2, B:174:0x049f, B:169:0x048c, B:164:0x0470, B:159:0x045d, B:154:0x044a, B:149:0x0437, B:144:0x041b, B:139:0x0408, B:130:0x03c4, B:125:0x03b1, B:120:0x0395), top: B:202:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:192:0x04eb A[Catch: all -> 0x0172, TryCatch #0 {all -> 0x0172, blocks: (B:3:0x0010, B:4:0x013f, B:6:0x0145, B:11:0x0158, B:13:0x0162, B:9:0x014e, B:17:0x0178, B:19:0x0186, B:21:0x018c, B:23:0x0192, B:25:0x0198, B:27:0x019e, B:29:0x01a4, B:31:0x01aa, B:33:0x01b0, B:35:0x01b6, B:37:0x01bc, B:39:0x01c2, B:41:0x01ca, B:43:0x01d2, B:45:0x01da, B:47:0x01e2, B:49:0x01ec, B:51:0x01f6, B:53:0x0200, B:55:0x020a, B:57:0x0214, B:59:0x021e, B:61:0x0228, B:63:0x0232, B:65:0x023c, B:67:0x0246, B:69:0x0250, B:71:0x025a, B:73:0x0264, B:75:0x026e, B:77:0x0278, B:79:0x0282, B:81:0x028c, B:83:0x0296, B:85:0x02a0, B:87:0x02aa, B:116:0x0320, B:118:0x038f, B:121:0x039c, B:123:0x03ab, B:126:0x03b8, B:128:0x03be, B:131:0x03cb, B:135:0x03f8, B:137:0x0402, B:140:0x040f, B:142:0x0415, B:145:0x0422, B:147:0x0431, B:150:0x043e, B:152:0x0444, B:155:0x0451, B:157:0x0457, B:160:0x0464, B:162:0x046a, B:165:0x0477, B:167:0x0486, B:170:0x0493, B:172:0x0499, B:175:0x04a6, B:177:0x04ac, B:180:0x04b9, B:182:0x04bf, B:184:0x04ca, B:189:0x04dc, B:191:0x04e8, B:195:0x04f8, B:192:0x04eb, B:193:0x04f2, B:194:0x04f3, B:187:0x04d2, B:183:0x04c3, B:179:0x04b2, B:174:0x049f, B:169:0x048c, B:164:0x0470, B:159:0x045d, B:154:0x044a, B:149:0x0437, B:144:0x041b, B:139:0x0408, B:130:0x03c4, B:125:0x03b1, B:120:0x0395), top: B:202:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:194:0x04f3 A[Catch: all -> 0x0172, TryCatch #0 {all -> 0x0172, blocks: (B:3:0x0010, B:4:0x013f, B:6:0x0145, B:11:0x0158, B:13:0x0162, B:9:0x014e, B:17:0x0178, B:19:0x0186, B:21:0x018c, B:23:0x0192, B:25:0x0198, B:27:0x019e, B:29:0x01a4, B:31:0x01aa, B:33:0x01b0, B:35:0x01b6, B:37:0x01bc, B:39:0x01c2, B:41:0x01ca, B:43:0x01d2, B:45:0x01da, B:47:0x01e2, B:49:0x01ec, B:51:0x01f6, B:53:0x0200, B:55:0x020a, B:57:0x0214, B:59:0x021e, B:61:0x0228, B:63:0x0232, B:65:0x023c, B:67:0x0246, B:69:0x0250, B:71:0x025a, B:73:0x0264, B:75:0x026e, B:77:0x0278, B:79:0x0282, B:81:0x028c, B:83:0x0296, B:85:0x02a0, B:87:0x02aa, B:116:0x0320, B:118:0x038f, B:121:0x039c, B:123:0x03ab, B:126:0x03b8, B:128:0x03be, B:131:0x03cb, B:135:0x03f8, B:137:0x0402, B:140:0x040f, B:142:0x0415, B:145:0x0422, B:147:0x0431, B:150:0x043e, B:152:0x0444, B:155:0x0451, B:157:0x0457, B:160:0x0464, B:162:0x046a, B:165:0x0477, B:167:0x0486, B:170:0x0493, B:172:0x0499, B:175:0x04a6, B:177:0x04ac, B:180:0x04b9, B:182:0x04bf, B:184:0x04ca, B:189:0x04dc, B:191:0x04e8, B:195:0x04f8, B:192:0x04eb, B:193:0x04f2, B:194:0x04f3, B:187:0x04d2, B:183:0x04c3, B:179:0x04b2, B:174:0x049f, B:169:0x048c, B:164:0x0470, B:159:0x045d, B:154:0x044a, B:149:0x0437, B:144:0x041b, B:139:0x0408, B:130:0x03c4, B:125:0x03b1, B:120:0x0395), top: B:202:0x0010 }] */
    public static final DeletedMessageFull getMessage$lambda$0(String str, long j, long j2, int i, DeletedMessageDao_Impl deletedMessageDao_Impl, SQLiteConnection _connection) {
        DeletedMessageFull deletedMessageFull;
        int i2;
        LongSparseArray longSparseArray;
        int i3;
        int i4;
        int i5;
        Long l;
        DeletedMessage deletedMessage;
        Long lValueOf;
        List<DeletedMessageReaction> arrayList;
        Object obj;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.bindLong(3, i);
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
            Long l2 = null;
            LongSparseArray longSparseArray2 = new LongSparseArray(0, 1, null);
            while (sQLiteStatementPrepare.step()) {
                Long lValueOf2 = sQLiteStatementPrepare.isNull(columnIndexOrThrow) ? l2 : Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                if (lValueOf2 != null) {
                    if (longSparseArray2.containsKey(lValueOf2.longValue())) {
                        l2 = null;
                    } else {
                        longSparseArray2.put(lValueOf2.longValue(), new ArrayList());
                        l2 = null;
                    }
                }
            }
            sQLiteStatementPrepare.reset();
            deletedMessageDao_Impl.__fetchRelationshipDeletedMessageReactionAscomRadolynAyugramDatabaseEntitiesDeletedMessageReaction(_connection, longSparseArray2);
            if (sQLiteStatementPrepare.step()) {
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow) && sQLiteStatementPrepare.isNull(columnIndexOrThrow2) && sQLiteStatementPrepare.isNull(columnIndexOrThrow3) && sQLiteStatementPrepare.isNull(columnIndexOrThrow4) && sQLiteStatementPrepare.isNull(columnIndexOrThrow5) && sQLiteStatementPrepare.isNull(columnIndexOrThrow6) && sQLiteStatementPrepare.isNull(columnIndexOrThrow7) && sQLiteStatementPrepare.isNull(columnIndexOrThrow8) && sQLiteStatementPrepare.isNull(columnIndexOrThrow9) && sQLiteStatementPrepare.isNull(columnIndexOrThrow10)) {
                    i5 = columnIndexOrThrow11;
                    if (sQLiteStatementPrepare.isNull(i5)) {
                        i4 = columnIndexOrThrow12;
                        if (sQLiteStatementPrepare.isNull(i4)) {
                            i3 = columnIndexOrThrow13;
                            if (sQLiteStatementPrepare.isNull(i3)) {
                                i2 = columnIndexOrThrow14;
                                if (sQLiteStatementPrepare.isNull(i2)) {
                                    longSparseArray = longSparseArray2;
                                    if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow15)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow16)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow17)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow18)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow19)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow20)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow21)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow22)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow23)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                        columnIndexOrThrow23 = columnIndexOrThrow23;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow24)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                        columnIndexOrThrow23 = columnIndexOrThrow23;
                                        columnIndexOrThrow24 = columnIndexOrThrow24;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow25)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                        columnIndexOrThrow23 = columnIndexOrThrow23;
                                        columnIndexOrThrow24 = columnIndexOrThrow24;
                                        columnIndexOrThrow25 = columnIndexOrThrow25;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow26)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                        columnIndexOrThrow23 = columnIndexOrThrow23;
                                        columnIndexOrThrow24 = columnIndexOrThrow24;
                                        columnIndexOrThrow25 = columnIndexOrThrow25;
                                        columnIndexOrThrow26 = columnIndexOrThrow26;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow27)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                        columnIndexOrThrow23 = columnIndexOrThrow23;
                                        columnIndexOrThrow24 = columnIndexOrThrow24;
                                        columnIndexOrThrow25 = columnIndexOrThrow25;
                                        columnIndexOrThrow26 = columnIndexOrThrow26;
                                        columnIndexOrThrow27 = columnIndexOrThrow27;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow28)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                        columnIndexOrThrow23 = columnIndexOrThrow23;
                                        columnIndexOrThrow24 = columnIndexOrThrow24;
                                        columnIndexOrThrow25 = columnIndexOrThrow25;
                                        columnIndexOrThrow26 = columnIndexOrThrow26;
                                        columnIndexOrThrow27 = columnIndexOrThrow27;
                                        columnIndexOrThrow28 = columnIndexOrThrow28;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow29)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                        columnIndexOrThrow23 = columnIndexOrThrow23;
                                        columnIndexOrThrow24 = columnIndexOrThrow24;
                                        columnIndexOrThrow25 = columnIndexOrThrow25;
                                        columnIndexOrThrow26 = columnIndexOrThrow26;
                                        columnIndexOrThrow27 = columnIndexOrThrow27;
                                        columnIndexOrThrow28 = columnIndexOrThrow28;
                                        columnIndexOrThrow29 = columnIndexOrThrow29;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow30)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                        columnIndexOrThrow23 = columnIndexOrThrow23;
                                        columnIndexOrThrow24 = columnIndexOrThrow24;
                                        columnIndexOrThrow25 = columnIndexOrThrow25;
                                        columnIndexOrThrow26 = columnIndexOrThrow26;
                                        columnIndexOrThrow27 = columnIndexOrThrow27;
                                        columnIndexOrThrow28 = columnIndexOrThrow28;
                                        columnIndexOrThrow29 = columnIndexOrThrow29;
                                        columnIndexOrThrow30 = columnIndexOrThrow30;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow31)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                        columnIndexOrThrow23 = columnIndexOrThrow23;
                                        columnIndexOrThrow24 = columnIndexOrThrow24;
                                        columnIndexOrThrow25 = columnIndexOrThrow25;
                                        columnIndexOrThrow26 = columnIndexOrThrow26;
                                        columnIndexOrThrow27 = columnIndexOrThrow27;
                                        columnIndexOrThrow28 = columnIndexOrThrow28;
                                        columnIndexOrThrow29 = columnIndexOrThrow29;
                                        columnIndexOrThrow30 = columnIndexOrThrow30;
                                        columnIndexOrThrow31 = columnIndexOrThrow31;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow32)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                        columnIndexOrThrow23 = columnIndexOrThrow23;
                                        columnIndexOrThrow24 = columnIndexOrThrow24;
                                        columnIndexOrThrow25 = columnIndexOrThrow25;
                                        columnIndexOrThrow26 = columnIndexOrThrow26;
                                        columnIndexOrThrow27 = columnIndexOrThrow27;
                                        columnIndexOrThrow28 = columnIndexOrThrow28;
                                        columnIndexOrThrow29 = columnIndexOrThrow29;
                                        columnIndexOrThrow30 = columnIndexOrThrow30;
                                        columnIndexOrThrow31 = columnIndexOrThrow31;
                                        columnIndexOrThrow32 = columnIndexOrThrow32;
                                    } else if (!sQLiteStatementPrepare.isNull(columnIndexOrThrow33)) {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                        columnIndexOrThrow23 = columnIndexOrThrow23;
                                        columnIndexOrThrow24 = columnIndexOrThrow24;
                                        columnIndexOrThrow25 = columnIndexOrThrow25;
                                        columnIndexOrThrow26 = columnIndexOrThrow26;
                                        columnIndexOrThrow27 = columnIndexOrThrow27;
                                        columnIndexOrThrow28 = columnIndexOrThrow28;
                                        columnIndexOrThrow29 = columnIndexOrThrow29;
                                        columnIndexOrThrow30 = columnIndexOrThrow30;
                                        columnIndexOrThrow31 = columnIndexOrThrow31;
                                        columnIndexOrThrow32 = columnIndexOrThrow32;
                                        columnIndexOrThrow33 = columnIndexOrThrow33;
                                    } else if (sQLiteStatementPrepare.isNull(columnIndexOrThrow34)) {
                                        if (sQLiteStatementPrepare.isNull(columnIndexOrThrow35)) {
                                            columnIndexOrThrow15 = columnIndexOrThrow15;
                                            columnIndexOrThrow16 = columnIndexOrThrow16;
                                            columnIndexOrThrow17 = columnIndexOrThrow17;
                                            columnIndexOrThrow18 = columnIndexOrThrow18;
                                            columnIndexOrThrow19 = columnIndexOrThrow19;
                                            columnIndexOrThrow20 = columnIndexOrThrow20;
                                            columnIndexOrThrow21 = columnIndexOrThrow21;
                                            columnIndexOrThrow22 = columnIndexOrThrow22;
                                            columnIndexOrThrow23 = columnIndexOrThrow23;
                                            columnIndexOrThrow24 = columnIndexOrThrow24;
                                            columnIndexOrThrow25 = columnIndexOrThrow25;
                                            columnIndexOrThrow26 = columnIndexOrThrow26;
                                            columnIndexOrThrow27 = columnIndexOrThrow27;
                                            columnIndexOrThrow28 = columnIndexOrThrow28;
                                            columnIndexOrThrow29 = columnIndexOrThrow29;
                                            columnIndexOrThrow30 = columnIndexOrThrow30;
                                            columnIndexOrThrow31 = columnIndexOrThrow31;
                                            columnIndexOrThrow32 = columnIndexOrThrow32;
                                            columnIndexOrThrow33 = columnIndexOrThrow33;
                                            columnIndexOrThrow34 = columnIndexOrThrow34;
                                            deletedMessage = null;
                                            l = null;
                                        } else {
                                            columnIndexOrThrow15 = columnIndexOrThrow15;
                                            columnIndexOrThrow16 = columnIndexOrThrow16;
                                            columnIndexOrThrow17 = columnIndexOrThrow17;
                                            columnIndexOrThrow18 = columnIndexOrThrow18;
                                            columnIndexOrThrow19 = columnIndexOrThrow19;
                                            columnIndexOrThrow20 = columnIndexOrThrow20;
                                            columnIndexOrThrow21 = columnIndexOrThrow21;
                                            columnIndexOrThrow22 = columnIndexOrThrow22;
                                            columnIndexOrThrow23 = columnIndexOrThrow23;
                                            columnIndexOrThrow24 = columnIndexOrThrow24;
                                            columnIndexOrThrow25 = columnIndexOrThrow25;
                                            columnIndexOrThrow26 = columnIndexOrThrow26;
                                            columnIndexOrThrow27 = columnIndexOrThrow27;
                                            columnIndexOrThrow28 = columnIndexOrThrow28;
                                            columnIndexOrThrow29 = columnIndexOrThrow29;
                                            columnIndexOrThrow30 = columnIndexOrThrow30;
                                            columnIndexOrThrow31 = columnIndexOrThrow31;
                                            columnIndexOrThrow32 = columnIndexOrThrow32;
                                            columnIndexOrThrow33 = columnIndexOrThrow33;
                                            columnIndexOrThrow34 = columnIndexOrThrow34;
                                            columnIndexOrThrow35 = columnIndexOrThrow35;
                                        }
                                        if (sQLiteStatementPrepare.isNull(columnIndexOrThrow)) {
                                            lValueOf = l;
                                        } else {
                                            lValueOf = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                                        }
                                        if (lValueOf != null) {
                                            obj = longSparseArray.get(lValueOf.longValue());
                                            if (obj != null) {
                                                throw new IllegalStateException("Required value was null.");
                                            }
                                            arrayList = (List) obj;
                                        } else {
                                            arrayList = new ArrayList<>();
                                        }
                                        deletedMessageFull = new DeletedMessageFull();
                                        deletedMessageFull.message = deletedMessage;
                                        deletedMessageFull.reactions = arrayList;
                                    } else {
                                        columnIndexOrThrow15 = columnIndexOrThrow15;
                                        columnIndexOrThrow16 = columnIndexOrThrow16;
                                        columnIndexOrThrow17 = columnIndexOrThrow17;
                                        columnIndexOrThrow18 = columnIndexOrThrow18;
                                        columnIndexOrThrow19 = columnIndexOrThrow19;
                                        columnIndexOrThrow20 = columnIndexOrThrow20;
                                        columnIndexOrThrow21 = columnIndexOrThrow21;
                                        columnIndexOrThrow22 = columnIndexOrThrow22;
                                        columnIndexOrThrow23 = columnIndexOrThrow23;
                                        columnIndexOrThrow24 = columnIndexOrThrow24;
                                        columnIndexOrThrow25 = columnIndexOrThrow25;
                                        columnIndexOrThrow26 = columnIndexOrThrow26;
                                        columnIndexOrThrow27 = columnIndexOrThrow27;
                                        columnIndexOrThrow28 = columnIndexOrThrow28;
                                        columnIndexOrThrow29 = columnIndexOrThrow29;
                                        columnIndexOrThrow30 = columnIndexOrThrow30;
                                        columnIndexOrThrow31 = columnIndexOrThrow31;
                                        columnIndexOrThrow32 = columnIndexOrThrow32;
                                        columnIndexOrThrow33 = columnIndexOrThrow33;
                                        columnIndexOrThrow34 = columnIndexOrThrow34;
                                    }
                                }
                            } else {
                                i2 = columnIndexOrThrow14;
                            }
                            longSparseArray = longSparseArray2;
                        } else {
                            i2 = columnIndexOrThrow14;
                            longSparseArray = longSparseArray2;
                            i3 = columnIndexOrThrow13;
                        }
                    } else {
                        i2 = columnIndexOrThrow14;
                        longSparseArray = longSparseArray2;
                        i3 = columnIndexOrThrow13;
                        i4 = columnIndexOrThrow12;
                    }
                } else {
                    i2 = columnIndexOrThrow14;
                    longSparseArray = longSparseArray2;
                    i3 = columnIndexOrThrow13;
                    i4 = columnIndexOrThrow12;
                    i5 = columnIndexOrThrow11;
                }
                deletedMessage = new DeletedMessage();
                deletedMessage.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                deletedMessage.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                deletedMessage.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                deletedMessage.groupedId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                deletedMessage.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
                deletedMessage.fromId = sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                deletedMessage.topicId = sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                deletedMessage.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                deletedMessage.date = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                deletedMessage.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow10);
                deletedMessage.editDate = (int) sQLiteStatementPrepare.getLong(i5);
                deletedMessage.views = (int) sQLiteStatementPrepare.getLong(i4);
                deletedMessage.fwdFlags = (int) sQLiteStatementPrepare.getLong(i3);
                deletedMessage.fwdFromId = sQLiteStatementPrepare.getLong(i2);
                int i6 = columnIndexOrThrow15;
                if (sQLiteStatementPrepare.isNull(i6)) {
                    deletedMessage.fwdName = null;
                } else {
                    deletedMessage.fwdName = sQLiteStatementPrepare.getText(i6);
                }
                deletedMessage.fwdDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow16);
                int i7 = columnIndexOrThrow17;
                if (sQLiteStatementPrepare.isNull(i7)) {
                    deletedMessage.fwdPostAuthor = null;
                } else {
                    deletedMessage.fwdPostAuthor = sQLiteStatementPrepare.getText(i7);
                }
                int i8 = columnIndexOrThrow18;
                if (sQLiteStatementPrepare.isNull(i8)) {
                    deletedMessage.postAuthor = null;
                } else {
                    deletedMessage.postAuthor = sQLiteStatementPrepare.getText(i8);
                }
                deletedMessage.replyFlags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow19);
                deletedMessage.replyMessageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow20);
                deletedMessage.replyPeerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow21);
                deletedMessage.replyTopId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow22);
                deletedMessage.replyForumTopic = ((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow23)) != 0;
                int i9 = columnIndexOrThrow24;
                if (sQLiteStatementPrepare.isNull(i9)) {
                    deletedMessage.replySerialized = null;
                } else {
                    deletedMessage.replySerialized = sQLiteStatementPrepare.getBlob(i9);
                }
                int i10 = columnIndexOrThrow25;
                if (sQLiteStatementPrepare.isNull(i10)) {
                    deletedMessage.replyMarkupSerialized = null;
                } else {
                    deletedMessage.replyMarkupSerialized = sQLiteStatementPrepare.getBlob(i10);
                }
                deletedMessage.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow26);
                int i11 = columnIndexOrThrow27;
                if (sQLiteStatementPrepare.isNull(i11)) {
                    deletedMessage.text = null;
                } else {
                    deletedMessage.text = sQLiteStatementPrepare.getText(i11);
                }
                int i12 = columnIndexOrThrow28;
                if (sQLiteStatementPrepare.isNull(i12)) {
                    deletedMessage.textEntities = null;
                } else {
                    deletedMessage.textEntities = sQLiteStatementPrepare.getBlob(i12);
                }
                int i13 = columnIndexOrThrow29;
                if (sQLiteStatementPrepare.isNull(i13)) {
                    deletedMessage.mediaPath = null;
                } else {
                    deletedMessage.mediaPath = sQLiteStatementPrepare.getText(i13);
                }
                int i14 = columnIndexOrThrow30;
                if (sQLiteStatementPrepare.isNull(i14)) {
                    deletedMessage.hqThumbPath = null;
                } else {
                    deletedMessage.hqThumbPath = sQLiteStatementPrepare.getText(i14);
                }
                deletedMessage.documentType = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow31);
                int i15 = columnIndexOrThrow32;
                if (sQLiteStatementPrepare.isNull(i15)) {
                    deletedMessage.documentSerialized = null;
                } else {
                    deletedMessage.documentSerialized = sQLiteStatementPrepare.getBlob(i15);
                }
                int i16 = columnIndexOrThrow33;
                if (sQLiteStatementPrepare.isNull(i16)) {
                    deletedMessage.thumbsSerialized = null;
                } else {
                    deletedMessage.thumbsSerialized = sQLiteStatementPrepare.getBlob(i16);
                }
                int i17 = columnIndexOrThrow34;
                if (sQLiteStatementPrepare.isNull(i17)) {
                    deletedMessage.documentAttributesSerialized = null;
                } else {
                    deletedMessage.documentAttributesSerialized = sQLiteStatementPrepare.getBlob(i17);
                }
                int i18 = columnIndexOrThrow35;
                if (sQLiteStatementPrepare.isNull(i18)) {
                    l = null;
                    deletedMessage.mimeType = null;
                } else {
                    l = null;
                    deletedMessage.mimeType = sQLiteStatementPrepare.getText(i18);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow)) {
                    lValueOf = l;
                } else {
                    lValueOf = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                }
                if (lValueOf != null) {
                    obj = longSparseArray.get(lValueOf.longValue());
                    if (obj != null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    arrayList = (List) obj;
                } else {
                    arrayList = new ArrayList<>();
                }
                deletedMessageFull = new DeletedMessageFull();
                deletedMessageFull.message = deletedMessage;
                deletedMessageFull.reactions = arrayList;
            } else {
                deletedMessageFull = null;
            }
            sQLiteStatementPrepare.close();
            return deletedMessageFull;
        } catch (Throwable th) {
            sQLiteStatementPrepare.close();
            throw th;
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public List<DeletedMessageFull> getMessagesTopicless(final long j, final long j2, final int i, final int i2) {
        final String str = "SELECT dm.*\nFROM deletedmessage dm\nWHERE dm.userId = ?\n    AND dm.dialogId = ?\n    AND dm.messageId BETWEEN ? AND ?\n   OR (\n    dm.dialogId = ?\n        AND dm.groupedId <> 0\n        AND dm.groupedId IN (SELECT DISTINCT groupedId\n                             FROM deletedmessage\n                             WHERE userId = ?\n                               AND dialogId = ?\n                               AND messageId BETWEEN ? AND ?\n                               AND groupedId <> 0)\n    )\nLIMIT 120\n";
        return (List) DBUtil.performBlocking(this.__db, true, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedMessageDao_Impl.getMessagesTopicless$lambda$0(str, j, j2, i, i2, this, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:120:0x03c3 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:122:0x03cd A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:125:0x03e3 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:127:0x03e9 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:130:0x03f6 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:132:0x0402 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:135:0x043e  */
    /* JADX WARN: Code duplicated, block: B:136:0x0440  */
    /* JADX WARN: Code duplicated, block: B:139:0x044b A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:141:0x0451 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:144:0x045e A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:146:0x046a A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:149:0x0482 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:151:0x0488 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:154:0x0495 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:156:0x049d A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:159:0x04ac A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:161:0x04b4 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:164:0x04c3 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:166:0x04cf A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:169:0x04e7 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:171:0x04ed A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:174:0x04fa A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:176:0x0502 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:179:0x0511 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:181:0x0519 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:184:0x0528 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:185:0x052e A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:188:0x053c  */
    /* JADX WARN: Code duplicated, block: B:189:0x053e A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:191:0x0548 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:193:0x0558 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:196:0x0563 A[Catch: all -> 0x018f, TryCatch #0 {all -> 0x018f, blocks: (B:3:0x0012, B:4:0x015c, B:6:0x0162, B:11:0x0175, B:13:0x017f, B:9:0x016b, B:17:0x0195, B:18:0x01a2, B:20:0x01a8, B:22:0x01ae, B:24:0x01b4, B:26:0x01ba, B:28:0x01c0, B:30:0x01c6, B:32:0x01cc, B:34:0x01d2, B:36:0x01d8, B:38:0x01de, B:40:0x01e4, B:42:0x01ec, B:44:0x01f4, B:46:0x01fc, B:48:0x0206, B:50:0x0210, B:52:0x021a, B:54:0x0224, B:56:0x022e, B:58:0x0238, B:60:0x0242, B:62:0x024c, B:64:0x0256, B:66:0x0260, B:68:0x026a, B:70:0x0274, B:72:0x027e, B:74:0x0288, B:76:0x0292, B:78:0x029c, B:80:0x02a6, B:82:0x02b0, B:84:0x02ba, B:86:0x02c4, B:88:0x02ce, B:118:0x034f, B:120:0x03c3, B:123:0x03d4, B:125:0x03e3, B:128:0x03f0, B:130:0x03f6, B:133:0x040b, B:137:0x0441, B:139:0x044b, B:142:0x0458, B:144:0x045e, B:147:0x0473, B:149:0x0482, B:152:0x048f, B:154:0x0495, B:157:0x04a6, B:159:0x04ac, B:162:0x04bd, B:164:0x04c3, B:167:0x04d8, B:169:0x04e7, B:172:0x04f4, B:174:0x04fa, B:177:0x050b, B:179:0x0511, B:182:0x0522, B:184:0x0528, B:186:0x0536, B:191:0x0548, B:193:0x0558, B:197:0x056e, B:194:0x055b, B:195:0x0562, B:196:0x0563, B:189:0x053e, B:185:0x052e, B:181:0x0519, B:176:0x0502, B:171:0x04ed, B:166:0x04cf, B:161:0x04b4, B:156:0x049d, B:151:0x0488, B:146:0x046a, B:141:0x0451, B:132:0x0402, B:127:0x03e9, B:122:0x03cd), top: B:203:0x0012 }] */
    /* JADX WARN: Code duplicated, block: B:214:0x055b A[SYNTHETIC] */
    public static final List getMessagesTopicless$lambda$0(String str, long j, long j2, int i, int i2, DeletedMessageDao_Impl deletedMessageDao_Impl, SQLiteConnection _connection) {
        int i3;
        int i4;
        int i5;
        int i6;
        LongSparseArray longSparseArray;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        DeletedMessage deletedMessage;
        int i13;
        int i14;
        Long lValueOf;
        LongSparseArray longSparseArray2;
        List<DeletedMessageReaction> arrayList;
        Object obj;
        int i15;
        int i16;
        int i17;
        boolean z;
        int i18;
        int i19;
        int i20;
        int i21;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            long j3 = i;
            sQLiteStatementPrepare.bindLong(3, j3);
            long j4 = i2;
            sQLiteStatementPrepare.bindLong(4, j4);
            sQLiteStatementPrepare.bindLong(5, j2);
            sQLiteStatementPrepare.bindLong(6, j);
            sQLiteStatementPrepare.bindLong(7, j2);
            sQLiteStatementPrepare.bindLong(8, j3);
            sQLiteStatementPrepare.bindLong(9, j4);
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
            int i22 = columnIndexOrThrow13;
            int i23 = columnIndexOrThrow12;
            Long l = null;
            int i24 = columnIndexOrThrow11;
            LongSparseArray longSparseArray3 = new LongSparseArray(0, 1, null);
            while (sQLiteStatementPrepare.step()) {
                Long lValueOf2 = sQLiteStatementPrepare.isNull(columnIndexOrThrow) ? l : Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                if (lValueOf2 != null) {
                    if (longSparseArray3.containsKey(lValueOf2.longValue())) {
                        l = null;
                    } else {
                        longSparseArray3.put(lValueOf2.longValue(), new ArrayList());
                        l = null;
                    }
                }
            }
            sQLiteStatementPrepare.reset();
            deletedMessageDao_Impl.__fetchRelationshipDeletedMessageReactionAscomRadolynAyugramDatabaseEntitiesDeletedMessageReaction(_connection, longSparseArray3);
            ArrayList arrayList2 = new ArrayList();
            while (sQLiteStatementPrepare.step()) {
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow) && sQLiteStatementPrepare.isNull(columnIndexOrThrow2) && sQLiteStatementPrepare.isNull(columnIndexOrThrow3) && sQLiteStatementPrepare.isNull(columnIndexOrThrow4) && sQLiteStatementPrepare.isNull(columnIndexOrThrow5) && sQLiteStatementPrepare.isNull(columnIndexOrThrow6) && sQLiteStatementPrepare.isNull(columnIndexOrThrow7) && sQLiteStatementPrepare.isNull(columnIndexOrThrow8) && sQLiteStatementPrepare.isNull(columnIndexOrThrow9) && sQLiteStatementPrepare.isNull(columnIndexOrThrow10)) {
                    i5 = i24;
                    if (sQLiteStatementPrepare.isNull(i5)) {
                        i4 = i23;
                        if (sQLiteStatementPrepare.isNull(i4)) {
                            i3 = i22;
                            if (sQLiteStatementPrepare.isNull(i3)) {
                                arrayList2 = arrayList2;
                                i6 = columnIndexOrThrow14;
                                if (sQLiteStatementPrepare.isNull(i6)) {
                                    longSparseArray = longSparseArray3;
                                    int i25 = columnIndexOrThrow15;
                                    if (sQLiteStatementPrepare.isNull(i25)) {
                                        columnIndexOrThrow15 = i25;
                                        int i26 = columnIndexOrThrow16;
                                        if (sQLiteStatementPrepare.isNull(i26)) {
                                            columnIndexOrThrow16 = i26;
                                            int i27 = columnIndexOrThrow17;
                                            if (sQLiteStatementPrepare.isNull(i27)) {
                                                columnIndexOrThrow17 = i27;
                                                int i28 = columnIndexOrThrow18;
                                                if (sQLiteStatementPrepare.isNull(i28)) {
                                                    columnIndexOrThrow18 = i28;
                                                    int i29 = columnIndexOrThrow19;
                                                    if (sQLiteStatementPrepare.isNull(i29)) {
                                                        columnIndexOrThrow19 = i29;
                                                        int i30 = columnIndexOrThrow20;
                                                        if (sQLiteStatementPrepare.isNull(i30)) {
                                                            columnIndexOrThrow20 = i30;
                                                            int i31 = columnIndexOrThrow21;
                                                            if (sQLiteStatementPrepare.isNull(i31)) {
                                                                columnIndexOrThrow21 = i31;
                                                                int i32 = columnIndexOrThrow22;
                                                                if (sQLiteStatementPrepare.isNull(i32)) {
                                                                    columnIndexOrThrow22 = i32;
                                                                    int i33 = columnIndexOrThrow23;
                                                                    if (sQLiteStatementPrepare.isNull(i33)) {
                                                                        columnIndexOrThrow23 = i33;
                                                                        int i34 = columnIndexOrThrow24;
                                                                        if (sQLiteStatementPrepare.isNull(i34)) {
                                                                            columnIndexOrThrow24 = i34;
                                                                            int i35 = columnIndexOrThrow25;
                                                                            if (sQLiteStatementPrepare.isNull(i35)) {
                                                                                columnIndexOrThrow25 = i35;
                                                                                int i36 = columnIndexOrThrow26;
                                                                                if (sQLiteStatementPrepare.isNull(i36)) {
                                                                                    columnIndexOrThrow26 = i36;
                                                                                    int i37 = columnIndexOrThrow27;
                                                                                    if (sQLiteStatementPrepare.isNull(i37)) {
                                                                                        columnIndexOrThrow27 = i37;
                                                                                        int i38 = columnIndexOrThrow28;
                                                                                        if (sQLiteStatementPrepare.isNull(i38)) {
                                                                                            columnIndexOrThrow28 = i38;
                                                                                            int i39 = columnIndexOrThrow29;
                                                                                            if (sQLiteStatementPrepare.isNull(i39)) {
                                                                                                columnIndexOrThrow29 = i39;
                                                                                                int i40 = columnIndexOrThrow30;
                                                                                                if (sQLiteStatementPrepare.isNull(i40)) {
                                                                                                    columnIndexOrThrow30 = i40;
                                                                                                    int i41 = columnIndexOrThrow31;
                                                                                                    if (sQLiteStatementPrepare.isNull(i41)) {
                                                                                                        columnIndexOrThrow31 = i41;
                                                                                                        int i42 = columnIndexOrThrow32;
                                                                                                        if (sQLiteStatementPrepare.isNull(i42)) {
                                                                                                            columnIndexOrThrow32 = i42;
                                                                                                            int i43 = columnIndexOrThrow33;
                                                                                                            if (sQLiteStatementPrepare.isNull(i43)) {
                                                                                                                columnIndexOrThrow33 = i43;
                                                                                                                int i44 = columnIndexOrThrow34;
                                                                                                                if (sQLiteStatementPrepare.isNull(i44)) {
                                                                                                                    columnIndexOrThrow34 = i44;
                                                                                                                    int i45 = columnIndexOrThrow35;
                                                                                                                    if (sQLiteStatementPrepare.isNull(i45)) {
                                                                                                                        i9 = columnIndexOrThrow3;
                                                                                                                        i10 = columnIndexOrThrow4;
                                                                                                                        columnIndexOrThrow35 = i45;
                                                                                                                        i8 = i3;
                                                                                                                        i7 = columnIndexOrThrow20;
                                                                                                                        i14 = columnIndexOrThrow33;
                                                                                                                        deletedMessage = null;
                                                                                                                        i12 = columnIndexOrThrow2;
                                                                                                                        i11 = i6;
                                                                                                                        i13 = columnIndexOrThrow25;
                                                                                                                    } else {
                                                                                                                        columnIndexOrThrow35 = i45;
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    columnIndexOrThrow34 = i44;
                                                                                                                }
                                                                                                            } else {
                                                                                                                columnIndexOrThrow33 = i43;
                                                                                                            }
                                                                                                        } else {
                                                                                                            columnIndexOrThrow32 = i42;
                                                                                                        }
                                                                                                    } else {
                                                                                                        columnIndexOrThrow31 = i41;
                                                                                                    }
                                                                                                } else {
                                                                                                    columnIndexOrThrow30 = i40;
                                                                                                }
                                                                                            } else {
                                                                                                columnIndexOrThrow29 = i39;
                                                                                            }
                                                                                        } else {
                                                                                            columnIndexOrThrow28 = i38;
                                                                                        }
                                                                                    } else {
                                                                                        columnIndexOrThrow27 = i37;
                                                                                    }
                                                                                } else {
                                                                                    columnIndexOrThrow26 = i36;
                                                                                }
                                                                            } else {
                                                                                columnIndexOrThrow25 = i35;
                                                                            }
                                                                        } else {
                                                                            columnIndexOrThrow24 = i34;
                                                                        }
                                                                    } else {
                                                                        columnIndexOrThrow23 = i33;
                                                                    }
                                                                } else {
                                                                    columnIndexOrThrow22 = i32;
                                                                }
                                                            } else {
                                                                columnIndexOrThrow21 = i31;
                                                            }
                                                        } else {
                                                            columnIndexOrThrow20 = i30;
                                                        }
                                                    } else {
                                                        columnIndexOrThrow19 = i29;
                                                    }
                                                } else {
                                                    columnIndexOrThrow18 = i28;
                                                }
                                            } else {
                                                columnIndexOrThrow17 = i27;
                                            }
                                        } else {
                                            columnIndexOrThrow16 = i26;
                                        }
                                    } else {
                                        columnIndexOrThrow15 = i25;
                                    }
                                } else {
                                    longSparseArray = longSparseArray3;
                                }
                                deletedMessage = new DeletedMessage();
                                int i46 = i3;
                                deletedMessage.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                                deletedMessage.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                                deletedMessage.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                                deletedMessage.groupedId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                                deletedMessage.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
                                deletedMessage.fromId = sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                                deletedMessage.topicId = sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                                deletedMessage.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                                deletedMessage.date = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                                deletedMessage.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow10);
                                deletedMessage.editDate = (int) sQLiteStatementPrepare.getLong(i5);
                                i12 = columnIndexOrThrow2;
                                i4 = i4;
                                i9 = columnIndexOrThrow3;
                                deletedMessage.views = (int) sQLiteStatementPrepare.getLong(i4);
                                i10 = columnIndexOrThrow4;
                                deletedMessage.fwdFlags = (int) sQLiteStatementPrepare.getLong(i46);
                                deletedMessage.fwdFromId = sQLiteStatementPrepare.getLong(i6);
                                i15 = columnIndexOrThrow15;
                                if (sQLiteStatementPrepare.isNull(i15)) {
                                    deletedMessage.fwdName = null;
                                } else {
                                    deletedMessage.fwdName = sQLiteStatementPrepare.getText(i15);
                                }
                                int i47 = columnIndexOrThrow16;
                                i8 = i46;
                                columnIndexOrThrow15 = i15;
                                deletedMessage.fwdDate = (int) sQLiteStatementPrepare.getLong(i47);
                                columnIndexOrThrow17 = columnIndexOrThrow17;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow17)) {
                                    deletedMessage.fwdPostAuthor = null;
                                } else {
                                    deletedMessage.fwdPostAuthor = sQLiteStatementPrepare.getText(columnIndexOrThrow17);
                                }
                                i16 = columnIndexOrThrow18;
                                if (sQLiteStatementPrepare.isNull(i16)) {
                                    deletedMessage.postAuthor = null;
                                } else {
                                    deletedMessage.postAuthor = sQLiteStatementPrepare.getText(i16);
                                }
                                int i48 = columnIndexOrThrow19;
                                columnIndexOrThrow18 = i16;
                                columnIndexOrThrow16 = i47;
                                deletedMessage.replyFlags = (int) sQLiteStatementPrepare.getLong(i48);
                                int i49 = columnIndexOrThrow20;
                                i11 = i6;
                                deletedMessage.replyMessageId = (int) sQLiteStatementPrepare.getLong(i49);
                                columnIndexOrThrow19 = i48;
                                int i50 = columnIndexOrThrow21;
                                deletedMessage.replyPeerId = sQLiteStatementPrepare.getLong(i50);
                                columnIndexOrThrow21 = i50;
                                columnIndexOrThrow22 = columnIndexOrThrow22;
                                deletedMessage.replyTopId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow22);
                                i7 = i49;
                                i17 = columnIndexOrThrow23;
                                if (((int) sQLiteStatementPrepare.getLong(i17)) != 0) {
                                    z = true;
                                } else {
                                    z = false;
                                }
                                deletedMessage.replyForumTopic = z;
                                i18 = columnIndexOrThrow24;
                                if (sQLiteStatementPrepare.isNull(i18)) {
                                    deletedMessage.replySerialized = null;
                                } else {
                                    deletedMessage.replySerialized = sQLiteStatementPrepare.getBlob(i18);
                                }
                                i13 = columnIndexOrThrow25;
                                if (sQLiteStatementPrepare.isNull(i13)) {
                                    deletedMessage.replyMarkupSerialized = null;
                                } else {
                                    deletedMessage.replyMarkupSerialized = sQLiteStatementPrepare.getBlob(i13);
                                }
                                columnIndexOrThrow23 = i17;
                                columnIndexOrThrow24 = i18;
                                columnIndexOrThrow26 = columnIndexOrThrow26;
                                deletedMessage.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow26);
                                columnIndexOrThrow27 = columnIndexOrThrow27;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow27)) {
                                    deletedMessage.text = null;
                                } else {
                                    deletedMessage.text = sQLiteStatementPrepare.getText(columnIndexOrThrow27);
                                }
                                i19 = columnIndexOrThrow28;
                                if (sQLiteStatementPrepare.isNull(i19)) {
                                    deletedMessage.textEntities = null;
                                } else {
                                    deletedMessage.textEntities = sQLiteStatementPrepare.getBlob(i19);
                                }
                                columnIndexOrThrow29 = columnIndexOrThrow29;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow29)) {
                                    deletedMessage.mediaPath = null;
                                } else {
                                    deletedMessage.mediaPath = sQLiteStatementPrepare.getText(columnIndexOrThrow29);
                                }
                                i20 = columnIndexOrThrow30;
                                if (sQLiteStatementPrepare.isNull(i20)) {
                                    deletedMessage.hqThumbPath = null;
                                } else {
                                    deletedMessage.hqThumbPath = sQLiteStatementPrepare.getText(i20);
                                }
                                columnIndexOrThrow30 = i20;
                                columnIndexOrThrow28 = i19;
                                columnIndexOrThrow31 = columnIndexOrThrow31;
                                deletedMessage.documentType = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow31);
                                columnIndexOrThrow32 = columnIndexOrThrow32;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow32)) {
                                    deletedMessage.documentSerialized = null;
                                } else {
                                    deletedMessage.documentSerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow32);
                                }
                                i14 = columnIndexOrThrow33;
                                if (sQLiteStatementPrepare.isNull(i14)) {
                                    deletedMessage.thumbsSerialized = null;
                                } else {
                                    deletedMessage.thumbsSerialized = sQLiteStatementPrepare.getBlob(i14);
                                }
                                i21 = columnIndexOrThrow34;
                                if (sQLiteStatementPrepare.isNull(i21)) {
                                    deletedMessage.documentAttributesSerialized = null;
                                } else {
                                    deletedMessage.documentAttributesSerialized = sQLiteStatementPrepare.getBlob(i21);
                                }
                                columnIndexOrThrow35 = columnIndexOrThrow35;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow35)) {
                                    columnIndexOrThrow34 = i21;
                                    deletedMessage.mimeType = null;
                                } else {
                                    columnIndexOrThrow34 = i21;
                                    deletedMessage.mimeType = sQLiteStatementPrepare.getText(columnIndexOrThrow35);
                                }
                            }
                            if (sQLiteStatementPrepare.isNull(columnIndexOrThrow)) {
                                lValueOf = null;
                            } else {
                                lValueOf = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                            }
                            if (lValueOf != null) {
                                longSparseArray2 = longSparseArray;
                                obj = longSparseArray2.get(lValueOf.longValue());
                                if (obj != null) {
                                    throw new IllegalStateException("Required value was null.");
                                }
                                arrayList = (List) obj;
                            } else {
                                longSparseArray2 = longSparseArray;
                                arrayList = new ArrayList<>();
                            }
                            DeletedMessageFull deletedMessageFull = new DeletedMessageFull();
                            deletedMessageFull.message = deletedMessage;
                            deletedMessageFull.reactions = arrayList;
                            ArrayList arrayList3 = arrayList2;
                            arrayList3.add(deletedMessageFull);
                            int i51 = i13;
                            arrayList2 = arrayList3;
                            columnIndexOrThrow = columnIndexOrThrow;
                            columnIndexOrThrow25 = i51;
                            longSparseArray3 = longSparseArray2;
                            columnIndexOrThrow33 = i14;
                            columnIndexOrThrow2 = i12;
                            columnIndexOrThrow14 = i11;
                            columnIndexOrThrow4 = i10;
                            columnIndexOrThrow3 = i9;
                            i22 = i8;
                            columnIndexOrThrow20 = i7;
                            i24 = i5;
                            i23 = i4;
                        } else {
                            i3 = i22;
                        }
                    } else {
                        i3 = i22;
                        i4 = i23;
                    }
                } else {
                    i3 = i22;
                    i4 = i23;
                    i5 = i24;
                }
                i6 = columnIndexOrThrow14;
                longSparseArray = longSparseArray3;
                deletedMessage = new DeletedMessage();
                int i410 = i3;
                deletedMessage.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                deletedMessage.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                deletedMessage.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                deletedMessage.groupedId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                deletedMessage.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
                deletedMessage.fromId = sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                deletedMessage.topicId = sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                deletedMessage.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                deletedMessage.date = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                deletedMessage.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow10);
                deletedMessage.editDate = (int) sQLiteStatementPrepare.getLong(i5);
                i12 = columnIndexOrThrow2;
                i4 = i4;
                i9 = columnIndexOrThrow3;
                deletedMessage.views = (int) sQLiteStatementPrepare.getLong(i4);
                i10 = columnIndexOrThrow4;
                deletedMessage.fwdFlags = (int) sQLiteStatementPrepare.getLong(i410);
                deletedMessage.fwdFromId = sQLiteStatementPrepare.getLong(i6);
                i15 = columnIndexOrThrow15;
                if (sQLiteStatementPrepare.isNull(i15)) {
                    deletedMessage.fwdName = null;
                } else {
                    deletedMessage.fwdName = sQLiteStatementPrepare.getText(i15);
                }
                int i411 = columnIndexOrThrow16;
                i8 = i410;
                columnIndexOrThrow15 = i15;
                deletedMessage.fwdDate = (int) sQLiteStatementPrepare.getLong(i411);
                columnIndexOrThrow17 = columnIndexOrThrow17;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow17)) {
                    deletedMessage.fwdPostAuthor = null;
                } else {
                    deletedMessage.fwdPostAuthor = sQLiteStatementPrepare.getText(columnIndexOrThrow17);
                }
                i16 = columnIndexOrThrow18;
                if (sQLiteStatementPrepare.isNull(i16)) {
                    deletedMessage.postAuthor = null;
                } else {
                    deletedMessage.postAuthor = sQLiteStatementPrepare.getText(i16);
                }
                int i412 = columnIndexOrThrow19;
                columnIndexOrThrow18 = i16;
                columnIndexOrThrow16 = i411;
                deletedMessage.replyFlags = (int) sQLiteStatementPrepare.getLong(i412);
                int i413 = columnIndexOrThrow20;
                i11 = i6;
                deletedMessage.replyMessageId = (int) sQLiteStatementPrepare.getLong(i413);
                columnIndexOrThrow19 = i412;
                int i52 = columnIndexOrThrow21;
                deletedMessage.replyPeerId = sQLiteStatementPrepare.getLong(i52);
                columnIndexOrThrow21 = i52;
                columnIndexOrThrow22 = columnIndexOrThrow22;
                deletedMessage.replyTopId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow22);
                i7 = i413;
                i17 = columnIndexOrThrow23;
                if (((int) sQLiteStatementPrepare.getLong(i17)) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                deletedMessage.replyForumTopic = z;
                i18 = columnIndexOrThrow24;
                if (sQLiteStatementPrepare.isNull(i18)) {
                    deletedMessage.replySerialized = null;
                } else {
                    deletedMessage.replySerialized = sQLiteStatementPrepare.getBlob(i18);
                }
                i13 = columnIndexOrThrow25;
                if (sQLiteStatementPrepare.isNull(i13)) {
                    deletedMessage.replyMarkupSerialized = null;
                } else {
                    deletedMessage.replyMarkupSerialized = sQLiteStatementPrepare.getBlob(i13);
                }
                columnIndexOrThrow23 = i17;
                columnIndexOrThrow24 = i18;
                columnIndexOrThrow26 = columnIndexOrThrow26;
                deletedMessage.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow26);
                columnIndexOrThrow27 = columnIndexOrThrow27;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow27)) {
                    deletedMessage.text = null;
                } else {
                    deletedMessage.text = sQLiteStatementPrepare.getText(columnIndexOrThrow27);
                }
                i19 = columnIndexOrThrow28;
                if (sQLiteStatementPrepare.isNull(i19)) {
                    deletedMessage.textEntities = null;
                } else {
                    deletedMessage.textEntities = sQLiteStatementPrepare.getBlob(i19);
                }
                columnIndexOrThrow29 = columnIndexOrThrow29;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow29)) {
                    deletedMessage.mediaPath = null;
                } else {
                    deletedMessage.mediaPath = sQLiteStatementPrepare.getText(columnIndexOrThrow29);
                }
                i20 = columnIndexOrThrow30;
                if (sQLiteStatementPrepare.isNull(i20)) {
                    deletedMessage.hqThumbPath = null;
                } else {
                    deletedMessage.hqThumbPath = sQLiteStatementPrepare.getText(i20);
                }
                columnIndexOrThrow30 = i20;
                columnIndexOrThrow28 = i19;
                columnIndexOrThrow31 = columnIndexOrThrow31;
                deletedMessage.documentType = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow31);
                columnIndexOrThrow32 = columnIndexOrThrow32;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow32)) {
                    deletedMessage.documentSerialized = null;
                } else {
                    deletedMessage.documentSerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow32);
                }
                i14 = columnIndexOrThrow33;
                if (sQLiteStatementPrepare.isNull(i14)) {
                    deletedMessage.thumbsSerialized = null;
                } else {
                    deletedMessage.thumbsSerialized = sQLiteStatementPrepare.getBlob(i14);
                }
                i21 = columnIndexOrThrow34;
                if (sQLiteStatementPrepare.isNull(i21)) {
                    deletedMessage.documentAttributesSerialized = null;
                } else {
                    deletedMessage.documentAttributesSerialized = sQLiteStatementPrepare.getBlob(i21);
                }
                columnIndexOrThrow35 = columnIndexOrThrow35;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow35)) {
                    columnIndexOrThrow34 = i21;
                    deletedMessage.mimeType = null;
                } else {
                    columnIndexOrThrow34 = i21;
                    deletedMessage.mimeType = sQLiteStatementPrepare.getText(columnIndexOrThrow35);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow)) {
                    lValueOf = null;
                } else {
                    lValueOf = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                }
                if (lValueOf != null) {
                    longSparseArray2 = longSparseArray;
                    obj = longSparseArray2.get(lValueOf.longValue());
                    if (obj != null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    arrayList = (List) obj;
                } else {
                    longSparseArray2 = longSparseArray;
                    arrayList = new ArrayList<>();
                }
                DeletedMessageFull deletedMessageFull2 = new DeletedMessageFull();
                deletedMessageFull2.message = deletedMessage;
                deletedMessageFull2.reactions = arrayList;
                ArrayList arrayList4 = arrayList2;
                arrayList4.add(deletedMessageFull2);
                int i53 = i13;
                arrayList2 = arrayList4;
                columnIndexOrThrow = columnIndexOrThrow;
                columnIndexOrThrow25 = i53;
                longSparseArray3 = longSparseArray2;
                columnIndexOrThrow33 = i14;
                columnIndexOrThrow2 = i12;
                columnIndexOrThrow14 = i11;
                columnIndexOrThrow4 = i10;
                columnIndexOrThrow3 = i9;
                i22 = i8;
                columnIndexOrThrow20 = i7;
                i24 = i5;
                i23 = i4;
            }
            ArrayList arrayList5 = arrayList2;
            sQLiteStatementPrepare.close();
            return arrayList5;
        } catch (Throwable th) {
            sQLiteStatementPrepare.close();
            throw th;
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public List<DeletedMessageFull> getMessagesForTopic(final long j, final long j2, final long j3, final int i, final int i2) {
        final String str = "SELECT dm.*\nFROM deletedmessage dm\nWHERE dm.userId = ?\n    AND dm.dialogId = ?\n    AND dm.topicId = ?\n    AND dm.messageId BETWEEN ? AND ?\n   OR (\n    dm.dialogId = ?\n        AND dm.groupedId <> 0\n        AND dm.groupedId IN (SELECT DISTINCT groupedId\n                             FROM deletedmessage\n                             WHERE userId = ?\n                               AND dialogId = ?\n                               AND topicId = ?\n                               AND messageId BETWEEN ? AND ?\n                               AND groupedId <> 0)\n    )\nLIMIT 120\n";
        return (List) DBUtil.performBlocking(this.__db, true, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedMessageDao_Impl.getMessagesForTopic$lambda$0(str, j, j2, j3, i, i2, this, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:120:0x03cf A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:122:0x03d9 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:125:0x03ef A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:127:0x03f5 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:130:0x0402 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:132:0x040e A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:135:0x044a  */
    /* JADX WARN: Code duplicated, block: B:136:0x044c  */
    /* JADX WARN: Code duplicated, block: B:139:0x0457 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:141:0x045d A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:144:0x046a A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:146:0x0476 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:149:0x048e A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:151:0x0494 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:154:0x04a1 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:156:0x04a9 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:159:0x04b8 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:161:0x04c0 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:164:0x04cf A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:166:0x04db A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:169:0x04f3 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:171:0x04f9 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:174:0x0506 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:176:0x050e A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:179:0x051d A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:181:0x0525 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:184:0x0534 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:185:0x053a A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:188:0x0548  */
    /* JADX WARN: Code duplicated, block: B:189:0x054a A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:191:0x0554 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:193:0x0564 A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:196:0x056f A[Catch: all -> 0x019b, TryCatch #0 {all -> 0x019b, blocks: (B:3:0x0014, B:4:0x0168, B:6:0x016e, B:11:0x0181, B:13:0x018b, B:9:0x0177, B:17:0x01a1, B:18:0x01ae, B:20:0x01b4, B:22:0x01ba, B:24:0x01c0, B:26:0x01c6, B:28:0x01cc, B:30:0x01d2, B:32:0x01d8, B:34:0x01de, B:36:0x01e4, B:38:0x01ea, B:40:0x01f0, B:42:0x01f8, B:44:0x0200, B:46:0x0208, B:48:0x0212, B:50:0x021c, B:52:0x0226, B:54:0x0230, B:56:0x023a, B:58:0x0244, B:60:0x024e, B:62:0x0258, B:64:0x0262, B:66:0x026c, B:68:0x0276, B:70:0x0280, B:72:0x028a, B:74:0x0294, B:76:0x029e, B:78:0x02a8, B:80:0x02b2, B:82:0x02bc, B:84:0x02c6, B:86:0x02d0, B:88:0x02da, B:118:0x035b, B:120:0x03cf, B:123:0x03e0, B:125:0x03ef, B:128:0x03fc, B:130:0x0402, B:133:0x0417, B:137:0x044d, B:139:0x0457, B:142:0x0464, B:144:0x046a, B:147:0x047f, B:149:0x048e, B:152:0x049b, B:154:0x04a1, B:157:0x04b2, B:159:0x04b8, B:162:0x04c9, B:164:0x04cf, B:167:0x04e4, B:169:0x04f3, B:172:0x0500, B:174:0x0506, B:177:0x0517, B:179:0x051d, B:182:0x052e, B:184:0x0534, B:186:0x0542, B:191:0x0554, B:193:0x0564, B:197:0x057a, B:194:0x0567, B:195:0x056e, B:196:0x056f, B:189:0x054a, B:185:0x053a, B:181:0x0525, B:176:0x050e, B:171:0x04f9, B:166:0x04db, B:161:0x04c0, B:156:0x04a9, B:151:0x0494, B:146:0x0476, B:141:0x045d, B:132:0x040e, B:127:0x03f5, B:122:0x03d9), top: B:203:0x0014 }] */
    /* JADX WARN: Code duplicated, block: B:214:0x0567 A[SYNTHETIC] */
    public static final List getMessagesForTopic$lambda$0(String str, long j, long j2, long j3, int i, int i2, DeletedMessageDao_Impl deletedMessageDao_Impl, SQLiteConnection _connection) {
        int i3;
        int i4;
        int i5;
        int i6;
        LongSparseArray longSparseArray;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        DeletedMessage deletedMessage;
        int i13;
        int i14;
        Long lValueOf;
        LongSparseArray longSparseArray2;
        List<DeletedMessageReaction> arrayList;
        Object obj;
        int i15;
        int i16;
        int i17;
        boolean z;
        int i18;
        int i19;
        int i20;
        int i21;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.bindLong(3, j3);
            long j4 = i;
            sQLiteStatementPrepare.bindLong(4, j4);
            long j5 = i2;
            sQLiteStatementPrepare.bindLong(5, j5);
            sQLiteStatementPrepare.bindLong(6, j2);
            sQLiteStatementPrepare.bindLong(7, j);
            sQLiteStatementPrepare.bindLong(8, j2);
            sQLiteStatementPrepare.bindLong(9, j3);
            sQLiteStatementPrepare.bindLong(10, j4);
            sQLiteStatementPrepare.bindLong(11, j5);
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
            int i22 = columnIndexOrThrow13;
            int i23 = columnIndexOrThrow12;
            Long l = null;
            int i24 = columnIndexOrThrow11;
            LongSparseArray longSparseArray3 = new LongSparseArray(0, 1, null);
            while (sQLiteStatementPrepare.step()) {
                Long lValueOf2 = sQLiteStatementPrepare.isNull(columnIndexOrThrow) ? l : Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                if (lValueOf2 != null) {
                    if (longSparseArray3.containsKey(lValueOf2.longValue())) {
                        l = null;
                    } else {
                        longSparseArray3.put(lValueOf2.longValue(), new ArrayList());
                        l = null;
                    }
                }
            }
            sQLiteStatementPrepare.reset();
            deletedMessageDao_Impl.__fetchRelationshipDeletedMessageReactionAscomRadolynAyugramDatabaseEntitiesDeletedMessageReaction(_connection, longSparseArray3);
            ArrayList arrayList2 = new ArrayList();
            while (sQLiteStatementPrepare.step()) {
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow) && sQLiteStatementPrepare.isNull(columnIndexOrThrow2) && sQLiteStatementPrepare.isNull(columnIndexOrThrow3) && sQLiteStatementPrepare.isNull(columnIndexOrThrow4) && sQLiteStatementPrepare.isNull(columnIndexOrThrow5) && sQLiteStatementPrepare.isNull(columnIndexOrThrow6) && sQLiteStatementPrepare.isNull(columnIndexOrThrow7) && sQLiteStatementPrepare.isNull(columnIndexOrThrow8) && sQLiteStatementPrepare.isNull(columnIndexOrThrow9) && sQLiteStatementPrepare.isNull(columnIndexOrThrow10)) {
                    i5 = i24;
                    if (sQLiteStatementPrepare.isNull(i5)) {
                        i4 = i23;
                        if (sQLiteStatementPrepare.isNull(i4)) {
                            i3 = i22;
                            if (sQLiteStatementPrepare.isNull(i3)) {
                                arrayList2 = arrayList2;
                                i6 = columnIndexOrThrow14;
                                if (sQLiteStatementPrepare.isNull(i6)) {
                                    longSparseArray = longSparseArray3;
                                    int i25 = columnIndexOrThrow15;
                                    if (sQLiteStatementPrepare.isNull(i25)) {
                                        columnIndexOrThrow15 = i25;
                                        int i26 = columnIndexOrThrow16;
                                        if (sQLiteStatementPrepare.isNull(i26)) {
                                            columnIndexOrThrow16 = i26;
                                            int i27 = columnIndexOrThrow17;
                                            if (sQLiteStatementPrepare.isNull(i27)) {
                                                columnIndexOrThrow17 = i27;
                                                int i28 = columnIndexOrThrow18;
                                                if (sQLiteStatementPrepare.isNull(i28)) {
                                                    columnIndexOrThrow18 = i28;
                                                    int i29 = columnIndexOrThrow19;
                                                    if (sQLiteStatementPrepare.isNull(i29)) {
                                                        columnIndexOrThrow19 = i29;
                                                        int i30 = columnIndexOrThrow20;
                                                        if (sQLiteStatementPrepare.isNull(i30)) {
                                                            columnIndexOrThrow20 = i30;
                                                            int i31 = columnIndexOrThrow21;
                                                            if (sQLiteStatementPrepare.isNull(i31)) {
                                                                columnIndexOrThrow21 = i31;
                                                                int i32 = columnIndexOrThrow22;
                                                                if (sQLiteStatementPrepare.isNull(i32)) {
                                                                    columnIndexOrThrow22 = i32;
                                                                    int i33 = columnIndexOrThrow23;
                                                                    if (sQLiteStatementPrepare.isNull(i33)) {
                                                                        columnIndexOrThrow23 = i33;
                                                                        int i34 = columnIndexOrThrow24;
                                                                        if (sQLiteStatementPrepare.isNull(i34)) {
                                                                            columnIndexOrThrow24 = i34;
                                                                            int i35 = columnIndexOrThrow25;
                                                                            if (sQLiteStatementPrepare.isNull(i35)) {
                                                                                columnIndexOrThrow25 = i35;
                                                                                int i36 = columnIndexOrThrow26;
                                                                                if (sQLiteStatementPrepare.isNull(i36)) {
                                                                                    columnIndexOrThrow26 = i36;
                                                                                    int i37 = columnIndexOrThrow27;
                                                                                    if (sQLiteStatementPrepare.isNull(i37)) {
                                                                                        columnIndexOrThrow27 = i37;
                                                                                        int i38 = columnIndexOrThrow28;
                                                                                        if (sQLiteStatementPrepare.isNull(i38)) {
                                                                                            columnIndexOrThrow28 = i38;
                                                                                            int i39 = columnIndexOrThrow29;
                                                                                            if (sQLiteStatementPrepare.isNull(i39)) {
                                                                                                columnIndexOrThrow29 = i39;
                                                                                                int i40 = columnIndexOrThrow30;
                                                                                                if (sQLiteStatementPrepare.isNull(i40)) {
                                                                                                    columnIndexOrThrow30 = i40;
                                                                                                    int i41 = columnIndexOrThrow31;
                                                                                                    if (sQLiteStatementPrepare.isNull(i41)) {
                                                                                                        columnIndexOrThrow31 = i41;
                                                                                                        int i42 = columnIndexOrThrow32;
                                                                                                        if (sQLiteStatementPrepare.isNull(i42)) {
                                                                                                            columnIndexOrThrow32 = i42;
                                                                                                            int i43 = columnIndexOrThrow33;
                                                                                                            if (sQLiteStatementPrepare.isNull(i43)) {
                                                                                                                columnIndexOrThrow33 = i43;
                                                                                                                int i44 = columnIndexOrThrow34;
                                                                                                                if (sQLiteStatementPrepare.isNull(i44)) {
                                                                                                                    columnIndexOrThrow34 = i44;
                                                                                                                    int i45 = columnIndexOrThrow35;
                                                                                                                    if (sQLiteStatementPrepare.isNull(i45)) {
                                                                                                                        i7 = columnIndexOrThrow19;
                                                                                                                        i10 = columnIndexOrThrow3;
                                                                                                                        i11 = columnIndexOrThrow4;
                                                                                                                        i8 = columnIndexOrThrow5;
                                                                                                                        columnIndexOrThrow35 = i45;
                                                                                                                        i9 = i3;
                                                                                                                        i13 = columnIndexOrThrow25;
                                                                                                                        i14 = columnIndexOrThrow33;
                                                                                                                        deletedMessage = null;
                                                                                                                        i12 = columnIndexOrThrow2;
                                                                                                                    } else {
                                                                                                                        columnIndexOrThrow35 = i45;
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    columnIndexOrThrow34 = i44;
                                                                                                                }
                                                                                                            } else {
                                                                                                                columnIndexOrThrow33 = i43;
                                                                                                            }
                                                                                                        } else {
                                                                                                            columnIndexOrThrow32 = i42;
                                                                                                        }
                                                                                                    } else {
                                                                                                        columnIndexOrThrow31 = i41;
                                                                                                    }
                                                                                                } else {
                                                                                                    columnIndexOrThrow30 = i40;
                                                                                                }
                                                                                            } else {
                                                                                                columnIndexOrThrow29 = i39;
                                                                                            }
                                                                                        } else {
                                                                                            columnIndexOrThrow28 = i38;
                                                                                        }
                                                                                    } else {
                                                                                        columnIndexOrThrow27 = i37;
                                                                                    }
                                                                                } else {
                                                                                    columnIndexOrThrow26 = i36;
                                                                                }
                                                                            } else {
                                                                                columnIndexOrThrow25 = i35;
                                                                            }
                                                                        } else {
                                                                            columnIndexOrThrow24 = i34;
                                                                        }
                                                                    } else {
                                                                        columnIndexOrThrow23 = i33;
                                                                    }
                                                                } else {
                                                                    columnIndexOrThrow22 = i32;
                                                                }
                                                            } else {
                                                                columnIndexOrThrow21 = i31;
                                                            }
                                                        } else {
                                                            columnIndexOrThrow20 = i30;
                                                        }
                                                    } else {
                                                        columnIndexOrThrow19 = i29;
                                                    }
                                                } else {
                                                    columnIndexOrThrow18 = i28;
                                                }
                                            } else {
                                                columnIndexOrThrow17 = i27;
                                            }
                                        } else {
                                            columnIndexOrThrow16 = i26;
                                        }
                                    } else {
                                        columnIndexOrThrow15 = i25;
                                    }
                                } else {
                                    longSparseArray = longSparseArray3;
                                }
                                deletedMessage = new DeletedMessage();
                                int i46 = i3;
                                deletedMessage.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                                deletedMessage.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                                deletedMessage.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                                deletedMessage.groupedId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                                deletedMessage.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
                                deletedMessage.fromId = sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                                deletedMessage.topicId = sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                                deletedMessage.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                                deletedMessage.date = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                                deletedMessage.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow10);
                                deletedMessage.editDate = (int) sQLiteStatementPrepare.getLong(i5);
                                i12 = columnIndexOrThrow2;
                                i4 = i4;
                                i10 = columnIndexOrThrow3;
                                deletedMessage.views = (int) sQLiteStatementPrepare.getLong(i4);
                                i11 = columnIndexOrThrow4;
                                deletedMessage.fwdFlags = (int) sQLiteStatementPrepare.getLong(i46);
                                deletedMessage.fwdFromId = sQLiteStatementPrepare.getLong(i6);
                                i15 = columnIndexOrThrow15;
                                if (sQLiteStatementPrepare.isNull(i15)) {
                                    deletedMessage.fwdName = null;
                                } else {
                                    deletedMessage.fwdName = sQLiteStatementPrepare.getText(i15);
                                }
                                int i47 = columnIndexOrThrow16;
                                i9 = i46;
                                columnIndexOrThrow15 = i15;
                                deletedMessage.fwdDate = (int) sQLiteStatementPrepare.getLong(i47);
                                columnIndexOrThrow17 = columnIndexOrThrow17;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow17)) {
                                    deletedMessage.fwdPostAuthor = null;
                                } else {
                                    deletedMessage.fwdPostAuthor = sQLiteStatementPrepare.getText(columnIndexOrThrow17);
                                }
                                i16 = columnIndexOrThrow18;
                                if (sQLiteStatementPrepare.isNull(i16)) {
                                    deletedMessage.postAuthor = null;
                                } else {
                                    deletedMessage.postAuthor = sQLiteStatementPrepare.getText(i16);
                                }
                                int i48 = columnIndexOrThrow19;
                                columnIndexOrThrow18 = i16;
                                columnIndexOrThrow16 = i47;
                                deletedMessage.replyFlags = (int) sQLiteStatementPrepare.getLong(i48);
                                int i49 = columnIndexOrThrow20;
                                i8 = columnIndexOrThrow5;
                                deletedMessage.replyMessageId = (int) sQLiteStatementPrepare.getLong(i49);
                                int i50 = columnIndexOrThrow21;
                                columnIndexOrThrow20 = i49;
                                deletedMessage.replyPeerId = sQLiteStatementPrepare.getLong(i50);
                                columnIndexOrThrow21 = i50;
                                columnIndexOrThrow22 = columnIndexOrThrow22;
                                deletedMessage.replyTopId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow22);
                                i7 = i48;
                                i17 = columnIndexOrThrow23;
                                if (((int) sQLiteStatementPrepare.getLong(i17)) != 0) {
                                    z = true;
                                } else {
                                    z = false;
                                }
                                deletedMessage.replyForumTopic = z;
                                i18 = columnIndexOrThrow24;
                                if (sQLiteStatementPrepare.isNull(i18)) {
                                    deletedMessage.replySerialized = null;
                                } else {
                                    deletedMessage.replySerialized = sQLiteStatementPrepare.getBlob(i18);
                                }
                                i13 = columnIndexOrThrow25;
                                if (sQLiteStatementPrepare.isNull(i13)) {
                                    deletedMessage.replyMarkupSerialized = null;
                                } else {
                                    deletedMessage.replyMarkupSerialized = sQLiteStatementPrepare.getBlob(i13);
                                }
                                columnIndexOrThrow23 = i17;
                                columnIndexOrThrow24 = i18;
                                columnIndexOrThrow26 = columnIndexOrThrow26;
                                deletedMessage.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow26);
                                columnIndexOrThrow27 = columnIndexOrThrow27;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow27)) {
                                    deletedMessage.text = null;
                                } else {
                                    deletedMessage.text = sQLiteStatementPrepare.getText(columnIndexOrThrow27);
                                }
                                i19 = columnIndexOrThrow28;
                                if (sQLiteStatementPrepare.isNull(i19)) {
                                    deletedMessage.textEntities = null;
                                } else {
                                    deletedMessage.textEntities = sQLiteStatementPrepare.getBlob(i19);
                                }
                                columnIndexOrThrow29 = columnIndexOrThrow29;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow29)) {
                                    deletedMessage.mediaPath = null;
                                } else {
                                    deletedMessage.mediaPath = sQLiteStatementPrepare.getText(columnIndexOrThrow29);
                                }
                                i20 = columnIndexOrThrow30;
                                if (sQLiteStatementPrepare.isNull(i20)) {
                                    deletedMessage.hqThumbPath = null;
                                } else {
                                    deletedMessage.hqThumbPath = sQLiteStatementPrepare.getText(i20);
                                }
                                columnIndexOrThrow30 = i20;
                                columnIndexOrThrow28 = i19;
                                columnIndexOrThrow31 = columnIndexOrThrow31;
                                deletedMessage.documentType = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow31);
                                columnIndexOrThrow32 = columnIndexOrThrow32;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow32)) {
                                    deletedMessage.documentSerialized = null;
                                } else {
                                    deletedMessage.documentSerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow32);
                                }
                                i14 = columnIndexOrThrow33;
                                if (sQLiteStatementPrepare.isNull(i14)) {
                                    deletedMessage.thumbsSerialized = null;
                                } else {
                                    deletedMessage.thumbsSerialized = sQLiteStatementPrepare.getBlob(i14);
                                }
                                i21 = columnIndexOrThrow34;
                                if (sQLiteStatementPrepare.isNull(i21)) {
                                    deletedMessage.documentAttributesSerialized = null;
                                } else {
                                    deletedMessage.documentAttributesSerialized = sQLiteStatementPrepare.getBlob(i21);
                                }
                                columnIndexOrThrow35 = columnIndexOrThrow35;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow35)) {
                                    columnIndexOrThrow34 = i21;
                                    deletedMessage.mimeType = null;
                                } else {
                                    columnIndexOrThrow34 = i21;
                                    deletedMessage.mimeType = sQLiteStatementPrepare.getText(columnIndexOrThrow35);
                                }
                            }
                            if (sQLiteStatementPrepare.isNull(columnIndexOrThrow)) {
                                lValueOf = null;
                            } else {
                                lValueOf = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                            }
                            if (lValueOf != null) {
                                longSparseArray2 = longSparseArray;
                                obj = longSparseArray2.get(lValueOf.longValue());
                                if (obj != null) {
                                    throw new IllegalStateException("Required value was null.");
                                }
                                arrayList = (List) obj;
                            } else {
                                longSparseArray2 = longSparseArray;
                                arrayList = new ArrayList<>();
                            }
                            DeletedMessageFull deletedMessageFull = new DeletedMessageFull();
                            deletedMessageFull.message = deletedMessage;
                            deletedMessageFull.reactions = arrayList;
                            ArrayList arrayList3 = arrayList2;
                            arrayList3.add(deletedMessageFull);
                            longSparseArray3 = longSparseArray2;
                            columnIndexOrThrow33 = i14;
                            columnIndexOrThrow14 = i6;
                            columnIndexOrThrow2 = i12;
                            columnIndexOrThrow4 = i11;
                            columnIndexOrThrow3 = i10;
                            i22 = i9;
                            arrayList2 = arrayList3;
                            i24 = i5;
                            i23 = i4;
                            columnIndexOrThrow = columnIndexOrThrow;
                            columnIndexOrThrow25 = i13;
                            columnIndexOrThrow5 = i8;
                            columnIndexOrThrow19 = i7;
                        } else {
                            i3 = i22;
                        }
                    } else {
                        i3 = i22;
                        i4 = i23;
                    }
                } else {
                    i3 = i22;
                    i4 = i23;
                    i5 = i24;
                }
                i6 = columnIndexOrThrow14;
                longSparseArray = longSparseArray3;
                deletedMessage = new DeletedMessage();
                int i410 = i3;
                deletedMessage.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                deletedMessage.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                deletedMessage.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                deletedMessage.groupedId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                deletedMessage.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
                deletedMessage.fromId = sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                deletedMessage.topicId = sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                deletedMessage.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                deletedMessage.date = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                deletedMessage.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow10);
                deletedMessage.editDate = (int) sQLiteStatementPrepare.getLong(i5);
                i12 = columnIndexOrThrow2;
                i4 = i4;
                i10 = columnIndexOrThrow3;
                deletedMessage.views = (int) sQLiteStatementPrepare.getLong(i4);
                i11 = columnIndexOrThrow4;
                deletedMessage.fwdFlags = (int) sQLiteStatementPrepare.getLong(i410);
                deletedMessage.fwdFromId = sQLiteStatementPrepare.getLong(i6);
                i15 = columnIndexOrThrow15;
                if (sQLiteStatementPrepare.isNull(i15)) {
                    deletedMessage.fwdName = null;
                } else {
                    deletedMessage.fwdName = sQLiteStatementPrepare.getText(i15);
                }
                int i411 = columnIndexOrThrow16;
                i9 = i410;
                columnIndexOrThrow15 = i15;
                deletedMessage.fwdDate = (int) sQLiteStatementPrepare.getLong(i411);
                columnIndexOrThrow17 = columnIndexOrThrow17;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow17)) {
                    deletedMessage.fwdPostAuthor = null;
                } else {
                    deletedMessage.fwdPostAuthor = sQLiteStatementPrepare.getText(columnIndexOrThrow17);
                }
                i16 = columnIndexOrThrow18;
                if (sQLiteStatementPrepare.isNull(i16)) {
                    deletedMessage.postAuthor = null;
                } else {
                    deletedMessage.postAuthor = sQLiteStatementPrepare.getText(i16);
                }
                int i412 = columnIndexOrThrow19;
                columnIndexOrThrow18 = i16;
                columnIndexOrThrow16 = i411;
                deletedMessage.replyFlags = (int) sQLiteStatementPrepare.getLong(i412);
                int i413 = columnIndexOrThrow20;
                i8 = columnIndexOrThrow5;
                deletedMessage.replyMessageId = (int) sQLiteStatementPrepare.getLong(i413);
                int i51 = columnIndexOrThrow21;
                columnIndexOrThrow20 = i413;
                deletedMessage.replyPeerId = sQLiteStatementPrepare.getLong(i51);
                columnIndexOrThrow21 = i51;
                columnIndexOrThrow22 = columnIndexOrThrow22;
                deletedMessage.replyTopId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow22);
                i7 = i412;
                i17 = columnIndexOrThrow23;
                if (((int) sQLiteStatementPrepare.getLong(i17)) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                deletedMessage.replyForumTopic = z;
                i18 = columnIndexOrThrow24;
                if (sQLiteStatementPrepare.isNull(i18)) {
                    deletedMessage.replySerialized = null;
                } else {
                    deletedMessage.replySerialized = sQLiteStatementPrepare.getBlob(i18);
                }
                i13 = columnIndexOrThrow25;
                if (sQLiteStatementPrepare.isNull(i13)) {
                    deletedMessage.replyMarkupSerialized = null;
                } else {
                    deletedMessage.replyMarkupSerialized = sQLiteStatementPrepare.getBlob(i13);
                }
                columnIndexOrThrow23 = i17;
                columnIndexOrThrow24 = i18;
                columnIndexOrThrow26 = columnIndexOrThrow26;
                deletedMessage.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow26);
                columnIndexOrThrow27 = columnIndexOrThrow27;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow27)) {
                    deletedMessage.text = null;
                } else {
                    deletedMessage.text = sQLiteStatementPrepare.getText(columnIndexOrThrow27);
                }
                i19 = columnIndexOrThrow28;
                if (sQLiteStatementPrepare.isNull(i19)) {
                    deletedMessage.textEntities = null;
                } else {
                    deletedMessage.textEntities = sQLiteStatementPrepare.getBlob(i19);
                }
                columnIndexOrThrow29 = columnIndexOrThrow29;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow29)) {
                    deletedMessage.mediaPath = null;
                } else {
                    deletedMessage.mediaPath = sQLiteStatementPrepare.getText(columnIndexOrThrow29);
                }
                i20 = columnIndexOrThrow30;
                if (sQLiteStatementPrepare.isNull(i20)) {
                    deletedMessage.hqThumbPath = null;
                } else {
                    deletedMessage.hqThumbPath = sQLiteStatementPrepare.getText(i20);
                }
                columnIndexOrThrow30 = i20;
                columnIndexOrThrow28 = i19;
                columnIndexOrThrow31 = columnIndexOrThrow31;
                deletedMessage.documentType = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow31);
                columnIndexOrThrow32 = columnIndexOrThrow32;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow32)) {
                    deletedMessage.documentSerialized = null;
                } else {
                    deletedMessage.documentSerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow32);
                }
                i14 = columnIndexOrThrow33;
                if (sQLiteStatementPrepare.isNull(i14)) {
                    deletedMessage.thumbsSerialized = null;
                } else {
                    deletedMessage.thumbsSerialized = sQLiteStatementPrepare.getBlob(i14);
                }
                i21 = columnIndexOrThrow34;
                if (sQLiteStatementPrepare.isNull(i21)) {
                    deletedMessage.documentAttributesSerialized = null;
                } else {
                    deletedMessage.documentAttributesSerialized = sQLiteStatementPrepare.getBlob(i21);
                }
                columnIndexOrThrow35 = columnIndexOrThrow35;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow35)) {
                    columnIndexOrThrow34 = i21;
                    deletedMessage.mimeType = null;
                } else {
                    columnIndexOrThrow34 = i21;
                    deletedMessage.mimeType = sQLiteStatementPrepare.getText(columnIndexOrThrow35);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow)) {
                    lValueOf = null;
                } else {
                    lValueOf = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                }
                if (lValueOf != null) {
                    longSparseArray2 = longSparseArray;
                    obj = longSparseArray2.get(lValueOf.longValue());
                    if (obj != null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    arrayList = (List) obj;
                } else {
                    longSparseArray2 = longSparseArray;
                    arrayList = new ArrayList<>();
                }
                DeletedMessageFull deletedMessageFull2 = new DeletedMessageFull();
                deletedMessageFull2.message = deletedMessage;
                deletedMessageFull2.reactions = arrayList;
                ArrayList arrayList4 = arrayList2;
                arrayList4.add(deletedMessageFull2);
                longSparseArray3 = longSparseArray2;
                columnIndexOrThrow33 = i14;
                columnIndexOrThrow14 = i6;
                columnIndexOrThrow2 = i12;
                columnIndexOrThrow4 = i11;
                columnIndexOrThrow3 = i10;
                i22 = i9;
                arrayList2 = arrayList4;
                i24 = i5;
                i23 = i4;
                columnIndexOrThrow = columnIndexOrThrow;
                columnIndexOrThrow25 = i13;
                columnIndexOrThrow5 = i8;
                columnIndexOrThrow19 = i7;
            }
            ArrayList arrayList5 = arrayList2;
            sQLiteStatementPrepare.close();
            return arrayList5;
        } catch (Throwable th) {
            sQLiteStatementPrepare.close();
            throw th;
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public List<DeletedMessageFull> getMessagesForScroll(final long j, final long j2, final long j3, final String str, final int i, final int i2) {
        final String str2 = "WITH messages AS (\n  SELECT * FROM deletedmessage\n  WHERE userId = ? AND dialogId = ? AND (topicId = ? OR ? = 0)\n  AND messageId < ? AND (? = '' OR (text LIKE '%' || ? || '%'))\n  ORDER BY messageId DESC\n  LIMIT ?\n),\ngrouped AS (\n  SELECT * FROM deletedmessage\n  WHERE groupedId IN (SELECT groupedId FROM messages WHERE groupedId <> 0) AND dialogId = ?\n)\nSELECT * FROM messages\nUNION\nSELECT * FROM grouped\n";
        return (List) DBUtil.performBlocking(this.__db, true, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedMessageDao_Impl.getMessagesForScroll$lambda$0(str2, j, j2, j3, i, str, i2, this, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:128:0x03db A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:130:0x03e5 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:133:0x03fb A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:135:0x0401 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:138:0x040e A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:140:0x041a A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:143:0x0456  */
    /* JADX WARN: Code duplicated, block: B:144:0x0458  */
    /* JADX WARN: Code duplicated, block: B:147:0x0463 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:149:0x0469 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:152:0x0476 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:154:0x0482 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:157:0x049a A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:159:0x04a0 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:162:0x04ad A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:164:0x04b5 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:167:0x04c4 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:169:0x04cc A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:172:0x04db A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:174:0x04e7 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:177:0x04ff A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:179:0x0505 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:182:0x0512 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:184:0x051a A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:187:0x0529 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:189:0x0531 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:192:0x0540 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:193:0x0546 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:196:0x0554  */
    /* JADX WARN: Code duplicated, block: B:197:0x0556 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:199:0x0560 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:201:0x0570 A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:204:0x057b A[Catch: all -> 0x0033, TryCatch #0 {all -> 0x0033, blocks: (B:3:0x0016, B:5:0x002f, B:11:0x003c, B:13:0x0043, B:14:0x016f, B:16:0x0175, B:21:0x0188, B:23:0x0192, B:19:0x017e, B:25:0x01a5, B:26:0x01b2, B:28:0x01b8, B:30:0x01be, B:32:0x01c4, B:34:0x01ca, B:36:0x01d0, B:38:0x01d6, B:40:0x01dc, B:42:0x01e2, B:44:0x01e8, B:46:0x01ee, B:48:0x01f4, B:50:0x01fc, B:52:0x0204, B:54:0x020c, B:56:0x0216, B:58:0x0220, B:60:0x022a, B:62:0x0234, B:64:0x023e, B:66:0x0248, B:68:0x0252, B:70:0x025c, B:72:0x0266, B:74:0x0270, B:76:0x027a, B:78:0x0284, B:80:0x028e, B:82:0x0298, B:84:0x02a2, B:86:0x02ac, B:88:0x02b6, B:90:0x02c0, B:92:0x02ca, B:94:0x02d4, B:96:0x02de, B:126:0x0367, B:128:0x03db, B:131:0x03ec, B:133:0x03fb, B:136:0x0408, B:138:0x040e, B:141:0x0423, B:145:0x0459, B:147:0x0463, B:150:0x0470, B:152:0x0476, B:155:0x048b, B:157:0x049a, B:160:0x04a7, B:162:0x04ad, B:165:0x04be, B:167:0x04c4, B:170:0x04d5, B:172:0x04db, B:175:0x04f0, B:177:0x04ff, B:180:0x050c, B:182:0x0512, B:185:0x0523, B:187:0x0529, B:190:0x053a, B:192:0x0540, B:194:0x054e, B:199:0x0560, B:201:0x0570, B:205:0x0586, B:202:0x0573, B:203:0x057a, B:204:0x057b, B:197:0x0556, B:193:0x0546, B:189:0x0531, B:184:0x051a, B:179:0x0505, B:174:0x04e7, B:169:0x04cc, B:164:0x04b5, B:159:0x04a0, B:154:0x0482, B:149:0x0469, B:140:0x041a, B:135:0x0401, B:130:0x03e5, B:12:0x0040, B:8:0x0036), top: B:211:0x0016 }] */
    /* JADX WARN: Code duplicated, block: B:222:0x0573 A[SYNTHETIC] */
    public static final List getMessagesForScroll$lambda$0(String str, long j, long j2, long j3, int i, String str2, int i2, DeletedMessageDao_Impl deletedMessageDao_Impl, SQLiteConnection _connection) {
        int i3;
        int i4;
        int i5;
        int i6;
        LongSparseArray longSparseArray;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        DeletedMessage deletedMessage;
        int i16;
        int i17;
        Long lValueOf;
        LongSparseArray longSparseArray2;
        List<DeletedMessageReaction> arrayList;
        Object obj;
        int i18;
        int i19;
        int i20;
        boolean z;
        int i21;
        int i22;
        int i23;
        int i24;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.bindLong(3, j3);
            sQLiteStatementPrepare.bindLong(4, j3);
            sQLiteStatementPrepare.bindLong(5, i);
            if (str2 == null) {
                sQLiteStatementPrepare.bindNull(6);
            } else {
                sQLiteStatementPrepare.bindText(6, str2);
            }
            if (str2 == null) {
                sQLiteStatementPrepare.bindNull(7);
            } else {
                sQLiteStatementPrepare.bindText(7, str2);
            }
            sQLiteStatementPrepare.bindLong(8, i2);
            sQLiteStatementPrepare.bindLong(9, j2);
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
            int i25 = columnIndexOrThrow13;
            int i26 = columnIndexOrThrow12;
            Long l = null;
            int i27 = columnIndexOrThrow11;
            LongSparseArray longSparseArray3 = new LongSparseArray(0, 1, null);
            while (sQLiteStatementPrepare.step()) {
                Long lValueOf2 = sQLiteStatementPrepare.isNull(columnIndexOrThrow) ? l : Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                if (lValueOf2 != null) {
                    if (longSparseArray3.containsKey(lValueOf2.longValue())) {
                        l = null;
                    } else {
                        longSparseArray3.put(lValueOf2.longValue(), new ArrayList());
                        l = null;
                    }
                }
            }
            sQLiteStatementPrepare.reset();
            deletedMessageDao_Impl.__fetchRelationshipDeletedMessageReactionAscomRadolynAyugramDatabaseEntitiesDeletedMessageReaction(_connection, longSparseArray3);
            ArrayList arrayList2 = new ArrayList();
            while (sQLiteStatementPrepare.step()) {
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow) && sQLiteStatementPrepare.isNull(columnIndexOrThrow2) && sQLiteStatementPrepare.isNull(columnIndexOrThrow3) && sQLiteStatementPrepare.isNull(columnIndexOrThrow4) && sQLiteStatementPrepare.isNull(columnIndexOrThrow5) && sQLiteStatementPrepare.isNull(columnIndexOrThrow6) && sQLiteStatementPrepare.isNull(columnIndexOrThrow7) && sQLiteStatementPrepare.isNull(columnIndexOrThrow8) && sQLiteStatementPrepare.isNull(columnIndexOrThrow9) && sQLiteStatementPrepare.isNull(columnIndexOrThrow10)) {
                    i5 = i27;
                    if (sQLiteStatementPrepare.isNull(i5)) {
                        i4 = i26;
                        if (sQLiteStatementPrepare.isNull(i4)) {
                            i3 = i25;
                            if (sQLiteStatementPrepare.isNull(i3)) {
                                arrayList2 = arrayList2;
                                i6 = columnIndexOrThrow14;
                                if (sQLiteStatementPrepare.isNull(i6)) {
                                    longSparseArray = longSparseArray3;
                                    int i28 = columnIndexOrThrow15;
                                    if (sQLiteStatementPrepare.isNull(i28)) {
                                        columnIndexOrThrow15 = i28;
                                        int i29 = columnIndexOrThrow16;
                                        if (sQLiteStatementPrepare.isNull(i29)) {
                                            columnIndexOrThrow16 = i29;
                                            int i30 = columnIndexOrThrow17;
                                            if (sQLiteStatementPrepare.isNull(i30)) {
                                                columnIndexOrThrow17 = i30;
                                                int i31 = columnIndexOrThrow18;
                                                if (sQLiteStatementPrepare.isNull(i31)) {
                                                    columnIndexOrThrow18 = i31;
                                                    int i32 = columnIndexOrThrow19;
                                                    if (sQLiteStatementPrepare.isNull(i32)) {
                                                        columnIndexOrThrow19 = i32;
                                                        int i33 = columnIndexOrThrow20;
                                                        if (sQLiteStatementPrepare.isNull(i33)) {
                                                            columnIndexOrThrow20 = i33;
                                                            int i34 = columnIndexOrThrow21;
                                                            if (sQLiteStatementPrepare.isNull(i34)) {
                                                                columnIndexOrThrow21 = i34;
                                                                int i35 = columnIndexOrThrow22;
                                                                if (sQLiteStatementPrepare.isNull(i35)) {
                                                                    columnIndexOrThrow22 = i35;
                                                                    int i36 = columnIndexOrThrow23;
                                                                    if (sQLiteStatementPrepare.isNull(i36)) {
                                                                        columnIndexOrThrow23 = i36;
                                                                        int i37 = columnIndexOrThrow24;
                                                                        if (sQLiteStatementPrepare.isNull(i37)) {
                                                                            columnIndexOrThrow24 = i37;
                                                                            int i38 = columnIndexOrThrow25;
                                                                            if (sQLiteStatementPrepare.isNull(i38)) {
                                                                                columnIndexOrThrow25 = i38;
                                                                                int i39 = columnIndexOrThrow26;
                                                                                if (sQLiteStatementPrepare.isNull(i39)) {
                                                                                    columnIndexOrThrow26 = i39;
                                                                                    int i40 = columnIndexOrThrow27;
                                                                                    if (sQLiteStatementPrepare.isNull(i40)) {
                                                                                        columnIndexOrThrow27 = i40;
                                                                                        int i41 = columnIndexOrThrow28;
                                                                                        if (sQLiteStatementPrepare.isNull(i41)) {
                                                                                            columnIndexOrThrow28 = i41;
                                                                                            int i42 = columnIndexOrThrow29;
                                                                                            if (sQLiteStatementPrepare.isNull(i42)) {
                                                                                                columnIndexOrThrow29 = i42;
                                                                                                int i43 = columnIndexOrThrow30;
                                                                                                if (sQLiteStatementPrepare.isNull(i43)) {
                                                                                                    columnIndexOrThrow30 = i43;
                                                                                                    int i44 = columnIndexOrThrow31;
                                                                                                    if (sQLiteStatementPrepare.isNull(i44)) {
                                                                                                        columnIndexOrThrow31 = i44;
                                                                                                        int i45 = columnIndexOrThrow32;
                                                                                                        if (sQLiteStatementPrepare.isNull(i45)) {
                                                                                                            columnIndexOrThrow32 = i45;
                                                                                                            int i46 = columnIndexOrThrow33;
                                                                                                            if (sQLiteStatementPrepare.isNull(i46)) {
                                                                                                                columnIndexOrThrow33 = i46;
                                                                                                                int i47 = columnIndexOrThrow34;
                                                                                                                if (sQLiteStatementPrepare.isNull(i47)) {
                                                                                                                    columnIndexOrThrow34 = i47;
                                                                                                                    int i48 = columnIndexOrThrow35;
                                                                                                                    if (sQLiteStatementPrepare.isNull(i48)) {
                                                                                                                        i7 = columnIndexOrThrow19;
                                                                                                                        i8 = columnIndexOrThrow5;
                                                                                                                        i16 = columnIndexOrThrow25;
                                                                                                                        i12 = columnIndexOrThrow24;
                                                                                                                        i13 = columnIndexOrThrow23;
                                                                                                                        i14 = columnIndexOrThrow22;
                                                                                                                        i10 = columnIndexOrThrow3;
                                                                                                                        i11 = columnIndexOrThrow4;
                                                                                                                        columnIndexOrThrow35 = i48;
                                                                                                                        i9 = i3;
                                                                                                                        i17 = columnIndexOrThrow33;
                                                                                                                        deletedMessage = null;
                                                                                                                        i15 = columnIndexOrThrow2;
                                                                                                                    } else {
                                                                                                                        columnIndexOrThrow35 = i48;
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    columnIndexOrThrow34 = i47;
                                                                                                                }
                                                                                                            } else {
                                                                                                                columnIndexOrThrow33 = i46;
                                                                                                            }
                                                                                                        } else {
                                                                                                            columnIndexOrThrow32 = i45;
                                                                                                        }
                                                                                                    } else {
                                                                                                        columnIndexOrThrow31 = i44;
                                                                                                    }
                                                                                                } else {
                                                                                                    columnIndexOrThrow30 = i43;
                                                                                                }
                                                                                            } else {
                                                                                                columnIndexOrThrow29 = i42;
                                                                                            }
                                                                                        } else {
                                                                                            columnIndexOrThrow28 = i41;
                                                                                        }
                                                                                    } else {
                                                                                        columnIndexOrThrow27 = i40;
                                                                                    }
                                                                                } else {
                                                                                    columnIndexOrThrow26 = i39;
                                                                                }
                                                                            } else {
                                                                                columnIndexOrThrow25 = i38;
                                                                            }
                                                                        } else {
                                                                            columnIndexOrThrow24 = i37;
                                                                        }
                                                                    } else {
                                                                        columnIndexOrThrow23 = i36;
                                                                    }
                                                                } else {
                                                                    columnIndexOrThrow22 = i35;
                                                                }
                                                            } else {
                                                                columnIndexOrThrow21 = i34;
                                                            }
                                                        } else {
                                                            columnIndexOrThrow20 = i33;
                                                        }
                                                    } else {
                                                        columnIndexOrThrow19 = i32;
                                                    }
                                                } else {
                                                    columnIndexOrThrow18 = i31;
                                                }
                                            } else {
                                                columnIndexOrThrow17 = i30;
                                            }
                                        } else {
                                            columnIndexOrThrow16 = i29;
                                        }
                                    } else {
                                        columnIndexOrThrow15 = i28;
                                    }
                                } else {
                                    longSparseArray = longSparseArray3;
                                }
                                deletedMessage = new DeletedMessage();
                                int i49 = i3;
                                deletedMessage.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                                deletedMessage.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                                deletedMessage.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                                deletedMessage.groupedId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                                deletedMessage.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
                                deletedMessage.fromId = sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                                deletedMessage.topicId = sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                                deletedMessage.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                                deletedMessage.date = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                                deletedMessage.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow10);
                                deletedMessage.editDate = (int) sQLiteStatementPrepare.getLong(i5);
                                i15 = columnIndexOrThrow2;
                                i4 = i4;
                                i10 = columnIndexOrThrow3;
                                deletedMessage.views = (int) sQLiteStatementPrepare.getLong(i4);
                                i11 = columnIndexOrThrow4;
                                deletedMessage.fwdFlags = (int) sQLiteStatementPrepare.getLong(i49);
                                deletedMessage.fwdFromId = sQLiteStatementPrepare.getLong(i6);
                                i18 = columnIndexOrThrow15;
                                if (sQLiteStatementPrepare.isNull(i18)) {
                                    deletedMessage.fwdName = null;
                                } else {
                                    deletedMessage.fwdName = sQLiteStatementPrepare.getText(i18);
                                }
                                int i50 = columnIndexOrThrow16;
                                i9 = i49;
                                columnIndexOrThrow15 = i18;
                                deletedMessage.fwdDate = (int) sQLiteStatementPrepare.getLong(i50);
                                columnIndexOrThrow17 = columnIndexOrThrow17;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow17)) {
                                    deletedMessage.fwdPostAuthor = null;
                                } else {
                                    deletedMessage.fwdPostAuthor = sQLiteStatementPrepare.getText(columnIndexOrThrow17);
                                }
                                i19 = columnIndexOrThrow18;
                                if (sQLiteStatementPrepare.isNull(i19)) {
                                    deletedMessage.postAuthor = null;
                                } else {
                                    deletedMessage.postAuthor = sQLiteStatementPrepare.getText(i19);
                                }
                                int i51 = columnIndexOrThrow19;
                                columnIndexOrThrow18 = i19;
                                columnIndexOrThrow16 = i50;
                                deletedMessage.replyFlags = (int) sQLiteStatementPrepare.getLong(i51);
                                int i52 = columnIndexOrThrow20;
                                i8 = columnIndexOrThrow5;
                                deletedMessage.replyMessageId = (int) sQLiteStatementPrepare.getLong(i52);
                                int i53 = columnIndexOrThrow21;
                                columnIndexOrThrow20 = i52;
                                deletedMessage.replyPeerId = sQLiteStatementPrepare.getLong(i53);
                                i14 = columnIndexOrThrow22;
                                columnIndexOrThrow21 = i53;
                                deletedMessage.replyTopId = (int) sQLiteStatementPrepare.getLong(i14);
                                i7 = i51;
                                i20 = columnIndexOrThrow23;
                                if (((int) sQLiteStatementPrepare.getLong(i20)) != 0) {
                                    z = true;
                                } else {
                                    z = false;
                                }
                                deletedMessage.replyForumTopic = z;
                                i21 = columnIndexOrThrow24;
                                if (sQLiteStatementPrepare.isNull(i21)) {
                                    deletedMessage.replySerialized = null;
                                } else {
                                    deletedMessage.replySerialized = sQLiteStatementPrepare.getBlob(i21);
                                }
                                i16 = columnIndexOrThrow25;
                                if (sQLiteStatementPrepare.isNull(i16)) {
                                    deletedMessage.replyMarkupSerialized = null;
                                } else {
                                    deletedMessage.replyMarkupSerialized = sQLiteStatementPrepare.getBlob(i16);
                                }
                                i13 = i20;
                                i12 = i21;
                                columnIndexOrThrow26 = columnIndexOrThrow26;
                                deletedMessage.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow26);
                                columnIndexOrThrow27 = columnIndexOrThrow27;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow27)) {
                                    deletedMessage.text = null;
                                } else {
                                    deletedMessage.text = sQLiteStatementPrepare.getText(columnIndexOrThrow27);
                                }
                                i22 = columnIndexOrThrow28;
                                if (sQLiteStatementPrepare.isNull(i22)) {
                                    deletedMessage.textEntities = null;
                                } else {
                                    deletedMessage.textEntities = sQLiteStatementPrepare.getBlob(i22);
                                }
                                columnIndexOrThrow29 = columnIndexOrThrow29;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow29)) {
                                    deletedMessage.mediaPath = null;
                                } else {
                                    deletedMessage.mediaPath = sQLiteStatementPrepare.getText(columnIndexOrThrow29);
                                }
                                i23 = columnIndexOrThrow30;
                                if (sQLiteStatementPrepare.isNull(i23)) {
                                    deletedMessage.hqThumbPath = null;
                                } else {
                                    deletedMessage.hqThumbPath = sQLiteStatementPrepare.getText(i23);
                                }
                                columnIndexOrThrow30 = i23;
                                columnIndexOrThrow28 = i22;
                                columnIndexOrThrow31 = columnIndexOrThrow31;
                                deletedMessage.documentType = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow31);
                                columnIndexOrThrow32 = columnIndexOrThrow32;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow32)) {
                                    deletedMessage.documentSerialized = null;
                                } else {
                                    deletedMessage.documentSerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow32);
                                }
                                i17 = columnIndexOrThrow33;
                                if (sQLiteStatementPrepare.isNull(i17)) {
                                    deletedMessage.thumbsSerialized = null;
                                } else {
                                    deletedMessage.thumbsSerialized = sQLiteStatementPrepare.getBlob(i17);
                                }
                                i24 = columnIndexOrThrow34;
                                if (sQLiteStatementPrepare.isNull(i24)) {
                                    deletedMessage.documentAttributesSerialized = null;
                                } else {
                                    deletedMessage.documentAttributesSerialized = sQLiteStatementPrepare.getBlob(i24);
                                }
                                columnIndexOrThrow35 = columnIndexOrThrow35;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow35)) {
                                    columnIndexOrThrow34 = i24;
                                    deletedMessage.mimeType = null;
                                } else {
                                    columnIndexOrThrow34 = i24;
                                    deletedMessage.mimeType = sQLiteStatementPrepare.getText(columnIndexOrThrow35);
                                }
                            }
                            if (sQLiteStatementPrepare.isNull(columnIndexOrThrow)) {
                                lValueOf = null;
                            } else {
                                lValueOf = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                            }
                            if (lValueOf != null) {
                                longSparseArray2 = longSparseArray;
                                obj = longSparseArray2.get(lValueOf.longValue());
                                if (obj != null) {
                                    throw new IllegalStateException("Required value was null.");
                                }
                                arrayList = (List) obj;
                            } else {
                                longSparseArray2 = longSparseArray;
                                arrayList = new ArrayList<>();
                            }
                            DeletedMessageFull deletedMessageFull = new DeletedMessageFull();
                            deletedMessageFull.message = deletedMessage;
                            deletedMessageFull.reactions = arrayList;
                            ArrayList arrayList3 = arrayList2;
                            arrayList3.add(deletedMessageFull);
                            int i54 = i16;
                            columnIndexOrThrow5 = i8;
                            columnIndexOrThrow19 = i7;
                            columnIndexOrThrow22 = i14;
                            columnIndexOrThrow23 = i13;
                            columnIndexOrThrow24 = i12;
                            columnIndexOrThrow25 = i54;
                            longSparseArray3 = longSparseArray2;
                            columnIndexOrThrow14 = i6;
                            columnIndexOrThrow2 = i15;
                            columnIndexOrThrow3 = i10;
                            arrayList2 = arrayList3;
                            i26 = i4;
                            columnIndexOrThrow = columnIndexOrThrow;
                            columnIndexOrThrow33 = i17;
                            columnIndexOrThrow4 = i11;
                            i25 = i9;
                            i27 = i5;
                        } else {
                            i3 = i25;
                        }
                    } else {
                        i3 = i25;
                        i4 = i26;
                    }
                } else {
                    i3 = i25;
                    i4 = i26;
                    i5 = i27;
                }
                i6 = columnIndexOrThrow14;
                longSparseArray = longSparseArray3;
                deletedMessage = new DeletedMessage();
                int i410 = i3;
                deletedMessage.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                deletedMessage.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                deletedMessage.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                deletedMessage.groupedId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                deletedMessage.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
                deletedMessage.fromId = sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                deletedMessage.topicId = sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                deletedMessage.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                deletedMessage.date = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                deletedMessage.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow10);
                deletedMessage.editDate = (int) sQLiteStatementPrepare.getLong(i5);
                i15 = columnIndexOrThrow2;
                i4 = i4;
                i10 = columnIndexOrThrow3;
                deletedMessage.views = (int) sQLiteStatementPrepare.getLong(i4);
                i11 = columnIndexOrThrow4;
                deletedMessage.fwdFlags = (int) sQLiteStatementPrepare.getLong(i410);
                deletedMessage.fwdFromId = sQLiteStatementPrepare.getLong(i6);
                i18 = columnIndexOrThrow15;
                if (sQLiteStatementPrepare.isNull(i18)) {
                    deletedMessage.fwdName = null;
                } else {
                    deletedMessage.fwdName = sQLiteStatementPrepare.getText(i18);
                }
                int i55 = columnIndexOrThrow16;
                i9 = i410;
                columnIndexOrThrow15 = i18;
                deletedMessage.fwdDate = (int) sQLiteStatementPrepare.getLong(i55);
                columnIndexOrThrow17 = columnIndexOrThrow17;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow17)) {
                    deletedMessage.fwdPostAuthor = null;
                } else {
                    deletedMessage.fwdPostAuthor = sQLiteStatementPrepare.getText(columnIndexOrThrow17);
                }
                i19 = columnIndexOrThrow18;
                if (sQLiteStatementPrepare.isNull(i19)) {
                    deletedMessage.postAuthor = null;
                } else {
                    deletedMessage.postAuthor = sQLiteStatementPrepare.getText(i19);
                }
                int i56 = columnIndexOrThrow19;
                columnIndexOrThrow18 = i19;
                columnIndexOrThrow16 = i55;
                deletedMessage.replyFlags = (int) sQLiteStatementPrepare.getLong(i56);
                int i57 = columnIndexOrThrow20;
                i8 = columnIndexOrThrow5;
                deletedMessage.replyMessageId = (int) sQLiteStatementPrepare.getLong(i57);
                int i58 = columnIndexOrThrow21;
                columnIndexOrThrow20 = i57;
                deletedMessage.replyPeerId = sQLiteStatementPrepare.getLong(i58);
                i14 = columnIndexOrThrow22;
                columnIndexOrThrow21 = i58;
                deletedMessage.replyTopId = (int) sQLiteStatementPrepare.getLong(i14);
                i7 = i56;
                i20 = columnIndexOrThrow23;
                if (((int) sQLiteStatementPrepare.getLong(i20)) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                deletedMessage.replyForumTopic = z;
                i21 = columnIndexOrThrow24;
                if (sQLiteStatementPrepare.isNull(i21)) {
                    deletedMessage.replySerialized = null;
                } else {
                    deletedMessage.replySerialized = sQLiteStatementPrepare.getBlob(i21);
                }
                i16 = columnIndexOrThrow25;
                if (sQLiteStatementPrepare.isNull(i16)) {
                    deletedMessage.replyMarkupSerialized = null;
                } else {
                    deletedMessage.replyMarkupSerialized = sQLiteStatementPrepare.getBlob(i16);
                }
                i13 = i20;
                i12 = i21;
                columnIndexOrThrow26 = columnIndexOrThrow26;
                deletedMessage.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow26);
                columnIndexOrThrow27 = columnIndexOrThrow27;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow27)) {
                    deletedMessage.text = null;
                } else {
                    deletedMessage.text = sQLiteStatementPrepare.getText(columnIndexOrThrow27);
                }
                i22 = columnIndexOrThrow28;
                if (sQLiteStatementPrepare.isNull(i22)) {
                    deletedMessage.textEntities = null;
                } else {
                    deletedMessage.textEntities = sQLiteStatementPrepare.getBlob(i22);
                }
                columnIndexOrThrow29 = columnIndexOrThrow29;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow29)) {
                    deletedMessage.mediaPath = null;
                } else {
                    deletedMessage.mediaPath = sQLiteStatementPrepare.getText(columnIndexOrThrow29);
                }
                i23 = columnIndexOrThrow30;
                if (sQLiteStatementPrepare.isNull(i23)) {
                    deletedMessage.hqThumbPath = null;
                } else {
                    deletedMessage.hqThumbPath = sQLiteStatementPrepare.getText(i23);
                }
                columnIndexOrThrow30 = i23;
                columnIndexOrThrow28 = i22;
                columnIndexOrThrow31 = columnIndexOrThrow31;
                deletedMessage.documentType = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow31);
                columnIndexOrThrow32 = columnIndexOrThrow32;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow32)) {
                    deletedMessage.documentSerialized = null;
                } else {
                    deletedMessage.documentSerialized = sQLiteStatementPrepare.getBlob(columnIndexOrThrow32);
                }
                i17 = columnIndexOrThrow33;
                if (sQLiteStatementPrepare.isNull(i17)) {
                    deletedMessage.thumbsSerialized = null;
                } else {
                    deletedMessage.thumbsSerialized = sQLiteStatementPrepare.getBlob(i17);
                }
                i24 = columnIndexOrThrow34;
                if (sQLiteStatementPrepare.isNull(i24)) {
                    deletedMessage.documentAttributesSerialized = null;
                } else {
                    deletedMessage.documentAttributesSerialized = sQLiteStatementPrepare.getBlob(i24);
                }
                columnIndexOrThrow35 = columnIndexOrThrow35;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow35)) {
                    columnIndexOrThrow34 = i24;
                    deletedMessage.mimeType = null;
                } else {
                    columnIndexOrThrow34 = i24;
                    deletedMessage.mimeType = sQLiteStatementPrepare.getText(columnIndexOrThrow35);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow)) {
                    lValueOf = null;
                } else {
                    lValueOf = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                }
                if (lValueOf != null) {
                    longSparseArray2 = longSparseArray;
                    obj = longSparseArray2.get(lValueOf.longValue());
                    if (obj != null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    arrayList = (List) obj;
                } else {
                    longSparseArray2 = longSparseArray;
                    arrayList = new ArrayList<>();
                }
                DeletedMessageFull deletedMessageFull2 = new DeletedMessageFull();
                deletedMessageFull2.message = deletedMessage;
                deletedMessageFull2.reactions = arrayList;
                ArrayList arrayList4 = arrayList2;
                arrayList4.add(deletedMessageFull2);
                int i59 = i16;
                columnIndexOrThrow5 = i8;
                columnIndexOrThrow19 = i7;
                columnIndexOrThrow22 = i14;
                columnIndexOrThrow23 = i13;
                columnIndexOrThrow24 = i12;
                columnIndexOrThrow25 = i59;
                longSparseArray3 = longSparseArray2;
                columnIndexOrThrow14 = i6;
                columnIndexOrThrow2 = i15;
                columnIndexOrThrow3 = i10;
                arrayList2 = arrayList4;
                i26 = i4;
                columnIndexOrThrow = columnIndexOrThrow;
                columnIndexOrThrow33 = i17;
                columnIndexOrThrow4 = i11;
                i25 = i9;
                i27 = i5;
            }
            ArrayList arrayList5 = arrayList2;
            sQLiteStatementPrepare.close();
            return arrayList5;
        } catch (Throwable th) {
            sQLiteStatementPrepare.close();
            throw th;
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public List<CleanUpUnion> getMessagesForCleanUp() {
        final String str = "SELECT fakeId,\n       mediaPath,\n       entityCreateDate,\n       'deleted' AS msgType\nFROM deletedmessage\nWHERE mediaPath IS NOT NULL\n\nUNION ALL\n\nSELECT fakeId,\n       mediaPath,\n       entityCreateDate,\n       'edited' AS msgType\nFROM editedmessage\nWHERE mediaPath IS NOT NULL\n\nORDER BY entityCreateDate\nLIMIT 20;\n";
        return (List) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedMessageDao_Impl.getMessagesForCleanUp$lambda$0(str, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getMessagesForCleanUp$lambda$0(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            ArrayList arrayList = new ArrayList();
            while (sQLiteStatementPrepare.step()) {
                CleanUpUnion cleanUpUnion = new CleanUpUnion();
                cleanUpUnion.fakeId = sQLiteStatementPrepare.getLong(0);
                if (sQLiteStatementPrepare.isNull(1)) {
                    cleanUpUnion.mediaPath = null;
                } else {
                    cleanUpUnion.mediaPath = sQLiteStatementPrepare.getText(1);
                }
                cleanUpUnion.entityCreateDate = sQLiteStatementPrepare.getLong(2);
                if (sQLiteStatementPrepare.isNull(3)) {
                    cleanUpUnion.msgType = null;
                } else {
                    cleanUpUnion.msgType = sQLiteStatementPrepare.getText(3);
                }
                arrayList.add(cleanUpUnion);
            }
            return arrayList;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public int getDeletedCount(final long j, final long j2, final long j3, final String str) {
        final String str2 = "SELECT COUNT(*) FROM deletedmessage WHERE userId = ? AND dialogId = ? AND (topicId = ? OR ? = 0) AND (? = '' OR (text LIKE '%' || ? || '%')) AND ((text IS NOT NULL AND text != '') OR mediaPath IS NOT NULL OR documentSerialized IS NOT NULL)";
        return ((Number) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Integer.valueOf(DeletedMessageDao_Impl.getDeletedCount$lambda$0(str2, j, j2, j3, str, (SQLiteConnection) obj));
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int getDeletedCount$lambda$0(String str, long j, long j2, long j3, String str2, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.bindLong(3, j3);
            sQLiteStatementPrepare.bindLong(4, j3);
            if (str2 == null) {
                sQLiteStatementPrepare.bindNull(5);
            } else {
                sQLiteStatementPrepare.bindText(5, str2);
            }
            if (str2 == null) {
                sQLiteStatementPrepare.bindNull(6);
            } else {
                sQLiteStatementPrepare.bindText(6, str2);
            }
            return sQLiteStatementPrepare.step() ? (int) sQLiteStatementPrepare.getLong(0) : 0;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public int getDeletedCount() {
        final String str = "SELECT COUNT(*) FROM deletedmessage";
        return ((Number) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Integer.valueOf(DeletedMessageDao_Impl.getDeletedCount$lambda$1(str, (SQLiteConnection) obj));
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int getDeletedCount$lambda$1(String str, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            return sQLiteStatementPrepare.step() ? (int) sQLiteStatementPrepare.getLong(0) : 0;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public List<DeletedMessageFull> getLastMessages(final long j) {
        final String str = "SELECT t.*\nFROM deletedmessage t\nJOIN (\n  SELECT dialogId, MAX(messageId) AS max_messageId\n  FROM deletedmessage\n  WHERE userId = ?\n  GROUP BY dialogId\n) m\nON t.dialogId = m.dialogId AND t.messageId = m.max_messageId AND t.userId = ?;\n";
        return (List) DBUtil.performBlocking(this.__db, true, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedMessageDao_Impl.getLastMessages$lambda$0(str, j, this, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:120:0x03ad A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:122:0x03b7 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:125:0x03cd A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:127:0x03d3 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:130:0x03e0 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:132:0x03ec A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:135:0x042b  */
    /* JADX WARN: Code duplicated, block: B:136:0x042d  */
    /* JADX WARN: Code duplicated, block: B:139:0x0438 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:141:0x043e A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:144:0x044b A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:146:0x0457 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:149:0x046f A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:151:0x0475 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:154:0x0482 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:156:0x048a A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:159:0x0499 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:161:0x04a1 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:164:0x04b0 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:166:0x04bc A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:169:0x04d4 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:171:0x04da A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:174:0x04e7 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:176:0x04ef A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:179:0x04fe A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:181:0x0506 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:184:0x0515 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:185:0x051b A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:188:0x0529  */
    /* JADX WARN: Code duplicated, block: B:189:0x052b A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:191:0x0535 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:193:0x0545 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:196:0x0550 A[Catch: all -> 0x0169, TryCatch #0 {all -> 0x0169, blocks: (B:3:0x0010, B:4:0x0136, B:6:0x013c, B:11:0x014f, B:13:0x0159, B:9:0x0145, B:17:0x016f, B:18:0x017c, B:20:0x0182, B:22:0x0188, B:24:0x018e, B:26:0x0194, B:28:0x019a, B:30:0x01a0, B:32:0x01a6, B:34:0x01ac, B:36:0x01b2, B:38:0x01b8, B:40:0x01be, B:42:0x01c6, B:44:0x01ce, B:46:0x01d6, B:48:0x01e0, B:50:0x01ea, B:52:0x01f4, B:54:0x01fe, B:56:0x0208, B:58:0x0212, B:60:0x021c, B:62:0x0226, B:64:0x0230, B:66:0x023a, B:68:0x0244, B:70:0x024e, B:72:0x0258, B:74:0x0262, B:76:0x026c, B:78:0x0276, B:80:0x0280, B:82:0x028a, B:84:0x0294, B:86:0x029e, B:88:0x02a8, B:118:0x0339, B:120:0x03ad, B:123:0x03be, B:125:0x03cd, B:128:0x03da, B:130:0x03e0, B:133:0x03f5, B:137:0x042e, B:139:0x0438, B:142:0x0445, B:144:0x044b, B:147:0x0460, B:149:0x046f, B:152:0x047c, B:154:0x0482, B:157:0x0493, B:159:0x0499, B:162:0x04aa, B:164:0x04b0, B:167:0x04c5, B:169:0x04d4, B:172:0x04e1, B:174:0x04e7, B:177:0x04f8, B:179:0x04fe, B:182:0x050f, B:184:0x0515, B:186:0x0523, B:191:0x0535, B:193:0x0545, B:197:0x055b, B:194:0x0548, B:195:0x054f, B:196:0x0550, B:189:0x052b, B:185:0x051b, B:181:0x0506, B:176:0x04ef, B:171:0x04da, B:166:0x04bc, B:161:0x04a1, B:156:0x048a, B:151:0x0475, B:146:0x0457, B:141:0x043e, B:132:0x03ec, B:127:0x03d3, B:122:0x03b7), top: B:203:0x0010 }] */
    /* JADX WARN: Code duplicated, block: B:214:0x0548 A[SYNTHETIC] */
    public static final List getLastMessages$lambda$0(String str, long j, DeletedMessageDao_Impl deletedMessageDao_Impl, SQLiteConnection _connection) {
        int i;
        int i2;
        int i3;
        int i4;
        LongSparseArray longSparseArray;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        DeletedMessage deletedMessage;
        Long lValueOf;
        LongSparseArray longSparseArray2;
        List<DeletedMessageReaction> arrayList;
        Object obj;
        int i21;
        int i22;
        boolean z;
        int i23;
        int i24;
        int i25;
        int i26;
        int i27;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j);
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
            int i28 = columnIndexOrThrow13;
            int i29 = columnIndexOrThrow12;
            Long l = null;
            int i30 = columnIndexOrThrow11;
            LongSparseArray longSparseArray3 = new LongSparseArray(0, 1, null);
            while (sQLiteStatementPrepare.step()) {
                Long lValueOf2 = sQLiteStatementPrepare.isNull(columnIndexOrThrow) ? l : Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                if (lValueOf2 != null) {
                    if (longSparseArray3.containsKey(lValueOf2.longValue())) {
                        l = null;
                    } else {
                        longSparseArray3.put(lValueOf2.longValue(), new ArrayList());
                        l = null;
                    }
                }
            }
            sQLiteStatementPrepare.reset();
            deletedMessageDao_Impl.__fetchRelationshipDeletedMessageReactionAscomRadolynAyugramDatabaseEntitiesDeletedMessageReaction(_connection, longSparseArray3);
            ArrayList arrayList2 = new ArrayList();
            while (sQLiteStatementPrepare.step()) {
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow) && sQLiteStatementPrepare.isNull(columnIndexOrThrow2) && sQLiteStatementPrepare.isNull(columnIndexOrThrow3) && sQLiteStatementPrepare.isNull(columnIndexOrThrow4) && sQLiteStatementPrepare.isNull(columnIndexOrThrow5) && sQLiteStatementPrepare.isNull(columnIndexOrThrow6) && sQLiteStatementPrepare.isNull(columnIndexOrThrow7) && sQLiteStatementPrepare.isNull(columnIndexOrThrow8) && sQLiteStatementPrepare.isNull(columnIndexOrThrow9) && sQLiteStatementPrepare.isNull(columnIndexOrThrow10)) {
                    i3 = i30;
                    if (sQLiteStatementPrepare.isNull(i3)) {
                        i2 = i29;
                        if (sQLiteStatementPrepare.isNull(i2)) {
                            i = i28;
                            if (sQLiteStatementPrepare.isNull(i)) {
                                arrayList2 = arrayList2;
                                i4 = columnIndexOrThrow14;
                                if (sQLiteStatementPrepare.isNull(i4)) {
                                    longSparseArray = longSparseArray3;
                                    int i31 = columnIndexOrThrow15;
                                    if (sQLiteStatementPrepare.isNull(i31)) {
                                        columnIndexOrThrow15 = i31;
                                        int i32 = columnIndexOrThrow16;
                                        if (sQLiteStatementPrepare.isNull(i32)) {
                                            columnIndexOrThrow16 = i32;
                                            int i33 = columnIndexOrThrow17;
                                            if (sQLiteStatementPrepare.isNull(i33)) {
                                                columnIndexOrThrow17 = i33;
                                                int i34 = columnIndexOrThrow18;
                                                if (sQLiteStatementPrepare.isNull(i34)) {
                                                    columnIndexOrThrow18 = i34;
                                                    int i35 = columnIndexOrThrow19;
                                                    if (sQLiteStatementPrepare.isNull(i35)) {
                                                        columnIndexOrThrow19 = i35;
                                                        int i36 = columnIndexOrThrow20;
                                                        if (sQLiteStatementPrepare.isNull(i36)) {
                                                            columnIndexOrThrow20 = i36;
                                                            int i37 = columnIndexOrThrow21;
                                                            if (sQLiteStatementPrepare.isNull(i37)) {
                                                                columnIndexOrThrow21 = i37;
                                                                int i38 = columnIndexOrThrow22;
                                                                if (sQLiteStatementPrepare.isNull(i38)) {
                                                                    columnIndexOrThrow22 = i38;
                                                                    int i39 = columnIndexOrThrow23;
                                                                    if (sQLiteStatementPrepare.isNull(i39)) {
                                                                        columnIndexOrThrow23 = i39;
                                                                        int i40 = columnIndexOrThrow24;
                                                                        if (sQLiteStatementPrepare.isNull(i40)) {
                                                                            columnIndexOrThrow24 = i40;
                                                                            int i41 = columnIndexOrThrow25;
                                                                            if (sQLiteStatementPrepare.isNull(i41)) {
                                                                                columnIndexOrThrow25 = i41;
                                                                                int i42 = columnIndexOrThrow26;
                                                                                if (sQLiteStatementPrepare.isNull(i42)) {
                                                                                    columnIndexOrThrow26 = i42;
                                                                                    int i43 = columnIndexOrThrow27;
                                                                                    if (sQLiteStatementPrepare.isNull(i43)) {
                                                                                        columnIndexOrThrow27 = i43;
                                                                                        int i44 = columnIndexOrThrow28;
                                                                                        if (sQLiteStatementPrepare.isNull(i44)) {
                                                                                            columnIndexOrThrow28 = i44;
                                                                                            int i45 = columnIndexOrThrow29;
                                                                                            if (sQLiteStatementPrepare.isNull(i45)) {
                                                                                                columnIndexOrThrow29 = i45;
                                                                                                int i46 = columnIndexOrThrow30;
                                                                                                if (sQLiteStatementPrepare.isNull(i46)) {
                                                                                                    columnIndexOrThrow30 = i46;
                                                                                                    int i47 = columnIndexOrThrow31;
                                                                                                    if (sQLiteStatementPrepare.isNull(i47)) {
                                                                                                        columnIndexOrThrow31 = i47;
                                                                                                        int i48 = columnIndexOrThrow32;
                                                                                                        if (sQLiteStatementPrepare.isNull(i48)) {
                                                                                                            columnIndexOrThrow32 = i48;
                                                                                                            int i49 = columnIndexOrThrow33;
                                                                                                            if (sQLiteStatementPrepare.isNull(i49)) {
                                                                                                                columnIndexOrThrow33 = i49;
                                                                                                                int i50 = columnIndexOrThrow34;
                                                                                                                if (sQLiteStatementPrepare.isNull(i50)) {
                                                                                                                    columnIndexOrThrow34 = i50;
                                                                                                                    int i51 = columnIndexOrThrow35;
                                                                                                                    if (sQLiteStatementPrepare.isNull(i51)) {
                                                                                                                        i7 = columnIndexOrThrow4;
                                                                                                                        i8 = columnIndexOrThrow5;
                                                                                                                        i18 = columnIndexOrThrow16;
                                                                                                                        i5 = columnIndexOrThrow18;
                                                                                                                        i19 = columnIndexOrThrow33;
                                                                                                                        i16 = i4;
                                                                                                                        columnIndexOrThrow35 = i51;
                                                                                                                        i15 = i;
                                                                                                                        deletedMessage = null;
                                                                                                                        i17 = columnIndexOrThrow3;
                                                                                                                        i20 = columnIndexOrThrow32;
                                                                                                                        i9 = columnIndexOrThrow30;
                                                                                                                        i10 = columnIndexOrThrow28;
                                                                                                                        i11 = columnIndexOrThrow27;
                                                                                                                        i12 = columnIndexOrThrow20;
                                                                                                                        i13 = columnIndexOrThrow19;
                                                                                                                        i14 = columnIndexOrThrow15;
                                                                                                                        i6 = columnIndexOrThrow2;
                                                                                                                    } else {
                                                                                                                        columnIndexOrThrow35 = i51;
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    columnIndexOrThrow34 = i50;
                                                                                                                }
                                                                                                            } else {
                                                                                                                columnIndexOrThrow33 = i49;
                                                                                                            }
                                                                                                        } else {
                                                                                                            columnIndexOrThrow32 = i48;
                                                                                                        }
                                                                                                    } else {
                                                                                                        columnIndexOrThrow31 = i47;
                                                                                                    }
                                                                                                } else {
                                                                                                    columnIndexOrThrow30 = i46;
                                                                                                }
                                                                                            } else {
                                                                                                columnIndexOrThrow29 = i45;
                                                                                            }
                                                                                        } else {
                                                                                            columnIndexOrThrow28 = i44;
                                                                                        }
                                                                                    } else {
                                                                                        columnIndexOrThrow27 = i43;
                                                                                    }
                                                                                } else {
                                                                                    columnIndexOrThrow26 = i42;
                                                                                }
                                                                            } else {
                                                                                columnIndexOrThrow25 = i41;
                                                                            }
                                                                        } else {
                                                                            columnIndexOrThrow24 = i40;
                                                                        }
                                                                    } else {
                                                                        columnIndexOrThrow23 = i39;
                                                                    }
                                                                } else {
                                                                    columnIndexOrThrow22 = i38;
                                                                }
                                                            } else {
                                                                columnIndexOrThrow21 = i37;
                                                            }
                                                        } else {
                                                            columnIndexOrThrow20 = i36;
                                                        }
                                                    } else {
                                                        columnIndexOrThrow19 = i35;
                                                    }
                                                } else {
                                                    columnIndexOrThrow18 = i34;
                                                }
                                            } else {
                                                columnIndexOrThrow17 = i33;
                                            }
                                        } else {
                                            columnIndexOrThrow16 = i32;
                                        }
                                    } else {
                                        columnIndexOrThrow15 = i31;
                                    }
                                } else {
                                    longSparseArray = longSparseArray3;
                                }
                                deletedMessage = new DeletedMessage();
                                int i52 = i;
                                deletedMessage.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                                deletedMessage.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                                deletedMessage.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                                deletedMessage.groupedId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                                deletedMessage.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
                                deletedMessage.fromId = sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                                deletedMessage.topicId = sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                                deletedMessage.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                                deletedMessage.date = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                                deletedMessage.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow10);
                                deletedMessage.editDate = (int) sQLiteStatementPrepare.getLong(i3);
                                i17 = columnIndexOrThrow3;
                                i2 = i2;
                                i7 = columnIndexOrThrow4;
                                deletedMessage.views = (int) sQLiteStatementPrepare.getLong(i2);
                                i8 = columnIndexOrThrow5;
                                deletedMessage.fwdFlags = (int) sQLiteStatementPrepare.getLong(i52);
                                deletedMessage.fwdFromId = sQLiteStatementPrepare.getLong(i4);
                                i21 = columnIndexOrThrow15;
                                if (sQLiteStatementPrepare.isNull(i21)) {
                                    deletedMessage.fwdName = null;
                                } else {
                                    deletedMessage.fwdName = sQLiteStatementPrepare.getText(i21);
                                }
                                i6 = columnIndexOrThrow2;
                                i18 = columnIndexOrThrow16;
                                i16 = i4;
                                deletedMessage.fwdDate = (int) sQLiteStatementPrepare.getLong(i18);
                                columnIndexOrThrow17 = columnIndexOrThrow17;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow17)) {
                                    deletedMessage.fwdPostAuthor = null;
                                } else {
                                    deletedMessage.fwdPostAuthor = sQLiteStatementPrepare.getText(columnIndexOrThrow17);
                                }
                                i22 = columnIndexOrThrow18;
                                if (sQLiteStatementPrepare.isNull(i22)) {
                                    deletedMessage.postAuthor = null;
                                } else {
                                    deletedMessage.postAuthor = sQLiteStatementPrepare.getText(i22);
                                }
                                i15 = i52;
                                int i53 = columnIndexOrThrow19;
                                i14 = i21;
                                deletedMessage.replyFlags = (int) sQLiteStatementPrepare.getLong(i53);
                                int i54 = columnIndexOrThrow20;
                                i13 = i53;
                                deletedMessage.replyMessageId = (int) sQLiteStatementPrepare.getLong(i54);
                                i5 = i22;
                                int i55 = columnIndexOrThrow21;
                                deletedMessage.replyPeerId = sQLiteStatementPrepare.getLong(i55);
                                columnIndexOrThrow21 = i55;
                                int i56 = columnIndexOrThrow22;
                                deletedMessage.replyTopId = (int) sQLiteStatementPrepare.getLong(i56);
                                columnIndexOrThrow22 = i56;
                                i12 = i54;
                                columnIndexOrThrow23 = columnIndexOrThrow23;
                                if (((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow23)) != 0) {
                                    z = true;
                                } else {
                                    z = false;
                                }
                                deletedMessage.replyForumTopic = z;
                                i23 = columnIndexOrThrow24;
                                if (sQLiteStatementPrepare.isNull(i23)) {
                                    deletedMessage.replySerialized = null;
                                } else {
                                    deletedMessage.replySerialized = sQLiteStatementPrepare.getBlob(i23);
                                }
                                i24 = columnIndexOrThrow25;
                                if (sQLiteStatementPrepare.isNull(i24)) {
                                    deletedMessage.replyMarkupSerialized = null;
                                } else {
                                    deletedMessage.replyMarkupSerialized = sQLiteStatementPrepare.getBlob(i24);
                                }
                                columnIndexOrThrow24 = i23;
                                columnIndexOrThrow25 = i24;
                                columnIndexOrThrow26 = columnIndexOrThrow26;
                                deletedMessage.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow26);
                                i25 = columnIndexOrThrow27;
                                if (sQLiteStatementPrepare.isNull(i25)) {
                                    deletedMessage.text = null;
                                } else {
                                    deletedMessage.text = sQLiteStatementPrepare.getText(i25);
                                }
                                i26 = columnIndexOrThrow28;
                                if (sQLiteStatementPrepare.isNull(i26)) {
                                    deletedMessage.textEntities = null;
                                } else {
                                    deletedMessage.textEntities = sQLiteStatementPrepare.getBlob(i26);
                                }
                                columnIndexOrThrow29 = columnIndexOrThrow29;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow29)) {
                                    deletedMessage.mediaPath = null;
                                } else {
                                    deletedMessage.mediaPath = sQLiteStatementPrepare.getText(columnIndexOrThrow29);
                                }
                                i9 = columnIndexOrThrow30;
                                if (sQLiteStatementPrepare.isNull(i9)) {
                                    deletedMessage.hqThumbPath = null;
                                } else {
                                    deletedMessage.hqThumbPath = sQLiteStatementPrepare.getText(i9);
                                }
                                i11 = i25;
                                i10 = i26;
                                columnIndexOrThrow31 = columnIndexOrThrow31;
                                deletedMessage.documentType = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow31);
                                i20 = columnIndexOrThrow32;
                                if (sQLiteStatementPrepare.isNull(i20)) {
                                    deletedMessage.documentSerialized = null;
                                } else {
                                    deletedMessage.documentSerialized = sQLiteStatementPrepare.getBlob(i20);
                                }
                                i19 = columnIndexOrThrow33;
                                if (sQLiteStatementPrepare.isNull(i19)) {
                                    deletedMessage.thumbsSerialized = null;
                                } else {
                                    deletedMessage.thumbsSerialized = sQLiteStatementPrepare.getBlob(i19);
                                }
                                i27 = columnIndexOrThrow34;
                                if (sQLiteStatementPrepare.isNull(i27)) {
                                    deletedMessage.documentAttributesSerialized = null;
                                } else {
                                    deletedMessage.documentAttributesSerialized = sQLiteStatementPrepare.getBlob(i27);
                                }
                                columnIndexOrThrow35 = columnIndexOrThrow35;
                                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow35)) {
                                    columnIndexOrThrow34 = i27;
                                    deletedMessage.mimeType = null;
                                } else {
                                    columnIndexOrThrow34 = i27;
                                    deletedMessage.mimeType = sQLiteStatementPrepare.getText(columnIndexOrThrow35);
                                }
                            }
                            if (sQLiteStatementPrepare.isNull(columnIndexOrThrow)) {
                                lValueOf = null;
                            } else {
                                lValueOf = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                            }
                            if (lValueOf != null) {
                                longSparseArray2 = longSparseArray;
                                obj = longSparseArray2.get(lValueOf.longValue());
                                if (obj != null) {
                                    throw new IllegalStateException("Required value was null.");
                                }
                                arrayList = (List) obj;
                            } else {
                                longSparseArray2 = longSparseArray;
                                arrayList = new ArrayList<>();
                            }
                            DeletedMessageFull deletedMessageFull = new DeletedMessageFull();
                            deletedMessageFull.message = deletedMessage;
                            deletedMessageFull.reactions = arrayList;
                            ArrayList arrayList3 = arrayList2;
                            arrayList3.add(deletedMessageFull);
                            columnIndexOrThrow2 = i6;
                            longSparseArray3 = longSparseArray2;
                            i30 = i3;
                            columnIndexOrThrow14 = i16;
                            columnIndexOrThrow15 = i14;
                            columnIndexOrThrow19 = i13;
                            columnIndexOrThrow20 = i12;
                            columnIndexOrThrow27 = i11;
                            columnIndexOrThrow28 = i10;
                            columnIndexOrThrow30 = i9;
                            arrayList2 = arrayList3;
                            columnIndexOrThrow32 = i20;
                            columnIndexOrThrow16 = i18;
                            columnIndexOrThrow3 = i17;
                            columnIndexOrThrow = columnIndexOrThrow;
                            columnIndexOrThrow5 = i8;
                            columnIndexOrThrow33 = i19;
                            i28 = i15;
                            columnIndexOrThrow4 = i7;
                            columnIndexOrThrow18 = i5;
                            i29 = i2;
                        } else {
                            i = i28;
                        }
                    } else {
                        i = i28;
                        i2 = i29;
                    }
                } else {
                    i = i28;
                    i2 = i29;
                    i3 = i30;
                }
                i4 = columnIndexOrThrow14;
                longSparseArray = longSparseArray3;
                deletedMessage = new DeletedMessage();
                int i57 = i;
                deletedMessage.fakeId = sQLiteStatementPrepare.getLong(columnIndexOrThrow);
                deletedMessage.userId = sQLiteStatementPrepare.getLong(columnIndexOrThrow2);
                deletedMessage.dialogId = sQLiteStatementPrepare.getLong(columnIndexOrThrow3);
                deletedMessage.groupedId = sQLiteStatementPrepare.getLong(columnIndexOrThrow4);
                deletedMessage.peerId = sQLiteStatementPrepare.getLong(columnIndexOrThrow5);
                deletedMessage.fromId = sQLiteStatementPrepare.getLong(columnIndexOrThrow6);
                deletedMessage.topicId = sQLiteStatementPrepare.getLong(columnIndexOrThrow7);
                deletedMessage.messageId = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow8);
                deletedMessage.date = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow9);
                deletedMessage.flags = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow10);
                deletedMessage.editDate = (int) sQLiteStatementPrepare.getLong(i3);
                i17 = columnIndexOrThrow3;
                i2 = i2;
                i7 = columnIndexOrThrow4;
                deletedMessage.views = (int) sQLiteStatementPrepare.getLong(i2);
                i8 = columnIndexOrThrow5;
                deletedMessage.fwdFlags = (int) sQLiteStatementPrepare.getLong(i57);
                deletedMessage.fwdFromId = sQLiteStatementPrepare.getLong(i4);
                i21 = columnIndexOrThrow15;
                if (sQLiteStatementPrepare.isNull(i21)) {
                    deletedMessage.fwdName = null;
                } else {
                    deletedMessage.fwdName = sQLiteStatementPrepare.getText(i21);
                }
                i6 = columnIndexOrThrow2;
                i18 = columnIndexOrThrow16;
                i16 = i4;
                deletedMessage.fwdDate = (int) sQLiteStatementPrepare.getLong(i18);
                columnIndexOrThrow17 = columnIndexOrThrow17;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow17)) {
                    deletedMessage.fwdPostAuthor = null;
                } else {
                    deletedMessage.fwdPostAuthor = sQLiteStatementPrepare.getText(columnIndexOrThrow17);
                }
                i22 = columnIndexOrThrow18;
                if (sQLiteStatementPrepare.isNull(i22)) {
                    deletedMessage.postAuthor = null;
                } else {
                    deletedMessage.postAuthor = sQLiteStatementPrepare.getText(i22);
                }
                i15 = i57;
                int i58 = columnIndexOrThrow19;
                i14 = i21;
                deletedMessage.replyFlags = (int) sQLiteStatementPrepare.getLong(i58);
                int i59 = columnIndexOrThrow20;
                i13 = i58;
                deletedMessage.replyMessageId = (int) sQLiteStatementPrepare.getLong(i59);
                i5 = i22;
                int i510 = columnIndexOrThrow21;
                deletedMessage.replyPeerId = sQLiteStatementPrepare.getLong(i510);
                columnIndexOrThrow21 = i510;
                int i511 = columnIndexOrThrow22;
                deletedMessage.replyTopId = (int) sQLiteStatementPrepare.getLong(i511);
                columnIndexOrThrow22 = i511;
                i12 = i59;
                columnIndexOrThrow23 = columnIndexOrThrow23;
                if (((int) sQLiteStatementPrepare.getLong(columnIndexOrThrow23)) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                deletedMessage.replyForumTopic = z;
                i23 = columnIndexOrThrow24;
                if (sQLiteStatementPrepare.isNull(i23)) {
                    deletedMessage.replySerialized = null;
                } else {
                    deletedMessage.replySerialized = sQLiteStatementPrepare.getBlob(i23);
                }
                i24 = columnIndexOrThrow25;
                if (sQLiteStatementPrepare.isNull(i24)) {
                    deletedMessage.replyMarkupSerialized = null;
                } else {
                    deletedMessage.replyMarkupSerialized = sQLiteStatementPrepare.getBlob(i24);
                }
                columnIndexOrThrow24 = i23;
                columnIndexOrThrow25 = i24;
                columnIndexOrThrow26 = columnIndexOrThrow26;
                deletedMessage.entityCreateDate = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow26);
                i25 = columnIndexOrThrow27;
                if (sQLiteStatementPrepare.isNull(i25)) {
                    deletedMessage.text = null;
                } else {
                    deletedMessage.text = sQLiteStatementPrepare.getText(i25);
                }
                i26 = columnIndexOrThrow28;
                if (sQLiteStatementPrepare.isNull(i26)) {
                    deletedMessage.textEntities = null;
                } else {
                    deletedMessage.textEntities = sQLiteStatementPrepare.getBlob(i26);
                }
                columnIndexOrThrow29 = columnIndexOrThrow29;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow29)) {
                    deletedMessage.mediaPath = null;
                } else {
                    deletedMessage.mediaPath = sQLiteStatementPrepare.getText(columnIndexOrThrow29);
                }
                i9 = columnIndexOrThrow30;
                if (sQLiteStatementPrepare.isNull(i9)) {
                    deletedMessage.hqThumbPath = null;
                } else {
                    deletedMessage.hqThumbPath = sQLiteStatementPrepare.getText(i9);
                }
                i11 = i25;
                i10 = i26;
                columnIndexOrThrow31 = columnIndexOrThrow31;
                deletedMessage.documentType = (int) sQLiteStatementPrepare.getLong(columnIndexOrThrow31);
                i20 = columnIndexOrThrow32;
                if (sQLiteStatementPrepare.isNull(i20)) {
                    deletedMessage.documentSerialized = null;
                } else {
                    deletedMessage.documentSerialized = sQLiteStatementPrepare.getBlob(i20);
                }
                i19 = columnIndexOrThrow33;
                if (sQLiteStatementPrepare.isNull(i19)) {
                    deletedMessage.thumbsSerialized = null;
                } else {
                    deletedMessage.thumbsSerialized = sQLiteStatementPrepare.getBlob(i19);
                }
                i27 = columnIndexOrThrow34;
                if (sQLiteStatementPrepare.isNull(i27)) {
                    deletedMessage.documentAttributesSerialized = null;
                } else {
                    deletedMessage.documentAttributesSerialized = sQLiteStatementPrepare.getBlob(i27);
                }
                columnIndexOrThrow35 = columnIndexOrThrow35;
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow35)) {
                    columnIndexOrThrow34 = i27;
                    deletedMessage.mimeType = null;
                } else {
                    columnIndexOrThrow34 = i27;
                    deletedMessage.mimeType = sQLiteStatementPrepare.getText(columnIndexOrThrow35);
                }
                if (sQLiteStatementPrepare.isNull(columnIndexOrThrow)) {
                    lValueOf = null;
                } else {
                    lValueOf = Long.valueOf(sQLiteStatementPrepare.getLong(columnIndexOrThrow));
                }
                if (lValueOf != null) {
                    longSparseArray2 = longSparseArray;
                    obj = longSparseArray2.get(lValueOf.longValue());
                    if (obj != null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    arrayList = (List) obj;
                } else {
                    longSparseArray2 = longSparseArray;
                    arrayList = new ArrayList<>();
                }
                DeletedMessageFull deletedMessageFull2 = new DeletedMessageFull();
                deletedMessageFull2.message = deletedMessage;
                deletedMessageFull2.reactions = arrayList;
                ArrayList arrayList4 = arrayList2;
                arrayList4.add(deletedMessageFull2);
                columnIndexOrThrow2 = i6;
                longSparseArray3 = longSparseArray2;
                i30 = i3;
                columnIndexOrThrow14 = i16;
                columnIndexOrThrow15 = i14;
                columnIndexOrThrow19 = i13;
                columnIndexOrThrow20 = i12;
                columnIndexOrThrow27 = i11;
                columnIndexOrThrow28 = i10;
                columnIndexOrThrow30 = i9;
                arrayList2 = arrayList4;
                columnIndexOrThrow32 = i20;
                columnIndexOrThrow16 = i18;
                columnIndexOrThrow3 = i17;
                columnIndexOrThrow = columnIndexOrThrow;
                columnIndexOrThrow5 = i8;
                columnIndexOrThrow33 = i19;
                i28 = i15;
                columnIndexOrThrow4 = i7;
                columnIndexOrThrow18 = i5;
                i29 = i2;
            }
            ArrayList arrayList5 = arrayList2;
            sQLiteStatementPrepare.close();
            return arrayList5;
        } catch (Throwable th) {
            sQLiteStatementPrepare.close();
            throw th;
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public boolean exists(final long j, final long j2, final long j3, final int i) {
        final String str = "SELECT EXISTS(SELECT * FROM deletedmessage WHERE userId = ? AND dialogId = ? AND topicId = ? AND messageId = ?)";
        return ((Boolean) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda12
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(DeletedMessageDao_Impl.exists$lambda$0(str, j, j2, j3, i, (SQLiteConnection) obj));
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean exists$lambda$0(String str, long j, long j2, long j3, int i, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.bindLong(3, j3);
            sQLiteStatementPrepare.bindLong(4, i);
            boolean z = false;
            if (sQLiteStatementPrepare.step()) {
                z = ((int) sQLiteStatementPrepare.getLong(0)) != 0;
            }
            return z;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public boolean existsWithoutMedia(final long j, final long j2, final int i) {
        final String str = "SELECT EXISTS(SELECT * FROM deletedmessage WHERE userId = ? AND dialogId = ? AND messageId = ? AND (mediaPath IS NULL OR mediaPath = '' OR mediaPath = '/'))";
        return ((Boolean) DBUtil.performBlocking(this.__db, true, false, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(DeletedMessageDao_Impl.existsWithoutMedia$lambda$0(str, j, j2, i, (SQLiteConnection) obj));
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean existsWithoutMedia$lambda$0(String str, long j, long j2, int i, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.bindLong(3, i);
            boolean z = false;
            if (sQLiteStatementPrepare.step()) {
                z = ((int) sQLiteStatementPrepare.getLong(0)) != 0;
            }
            return z;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public void delete(final long j, final long j2, final int i) {
        final String str = "DELETE FROM deletedmessage WHERE userId = ? AND dialogId = ? AND messageId = ?";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedMessageDao_Impl.delete$lambda$0(str, j, j2, i, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit delete$lambda$0(String str, long j, long j2, int i, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            sQLiteStatementPrepare.bindLong(3, i);
            sQLiteStatementPrepare.step();
            return Unit.INSTANCE;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public void deleteMedia(final long j) {
        final String str = "UPDATE deletedmessage SET mediaPath = NULL, documentType = 0 WHERE fakeId = ?";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda16
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedMessageDao_Impl.deleteMedia$lambda$0(str, j, (SQLiteConnection) obj);
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

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public void updateMediaPath(final long j, final long j2, final long j3, final String str) {
        final String str2 = "UPDATE deletedmessage SET mediaPath = ? WHERE userId = ? AND dialogId = ? AND messageId = ?";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedMessageDao_Impl.updateMediaPath$lambda$0(str2, str, j, j2, j3, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit updateMediaPath$lambda$0(String str, String str2, long j, long j2, long j3, SQLiteConnection _connection) {
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
            sQLiteStatementPrepare.step();
            sQLiteStatementPrepare.close();
            return Unit.INSTANCE;
        } catch (Throwable th) {
            sQLiteStatementPrepare.close();
            throw th;
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public void clearForDialog(final long j, final long j2, final Long l) {
        final String str = "DELETE FROM deletedmessage WHERE userId = ? AND dialogId = ? AND (? IS NULL OR topicId = ?)";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedMessageDao_Impl.clearForDialog$lambda$0(str, j, j2, l, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit clearForDialog$lambda$0(String str, long j, long j2, Long l, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement sQLiteStatementPrepare = _connection.prepare(str);
        try {
            sQLiteStatementPrepare.bindLong(1, j);
            sQLiteStatementPrepare.bindLong(2, j2);
            if (l == null) {
                sQLiteStatementPrepare.bindNull(3);
            } else {
                sQLiteStatementPrepare.bindLong(3, l.longValue());
            }
            if (l == null) {
                sQLiteStatementPrepare.bindNull(4);
            } else {
                sQLiteStatementPrepare.bindLong(4, l.longValue());
            }
            sQLiteStatementPrepare.step();
            return Unit.INSTANCE;
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    @Override // com.radolyn.ayugram.database.dao.DeletedMessageDao
    public void deleteAll() {
        final String str = "DELETE FROM deletedmessage";
        DBUtil.performBlocking(this.__db, false, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DeletedMessageDao_Impl.deleteAll$lambda$0(str, (SQLiteConnection) obj);
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

    private final void __fetchRelationshipDeletedMessageReactionAscomRadolynAyugramDatabaseEntitiesDeletedMessageReaction(final SQLiteConnection sQLiteConnection, LongSparseArray longSparseArray) {
        if (longSparseArray.isEmpty()) {
            return;
        }
        if (longSparseArray.size() > 999) {
            RelationUtil.recursiveFetchLongSparseArray(longSparseArray, true, new Function1() { // from class: com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl$$ExternalSyntheticLambda7
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return DeletedMessageDao_Impl.__fetchRelationshipDeletedMessageReactionAscomRadolynAyugramDatabaseEntitiesDeletedMessageReaction$lambda$0(this.f$0, sQLiteConnection, (LongSparseArray) obj);
                }
            });
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT `fakeReactionId`,`deletedMessageId`,`emoticon`,`documentId`,`isCustom`,`isPaid`,`count`,`selfSelected` FROM `DeletedMessageReaction` WHERE `deletedMessageId` IN (");
        StringUtil.appendPlaceholders(sb, longSparseArray.size());
        sb.append(")");
        String string = sb.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        SQLiteStatement sQLiteStatementPrepare = sQLiteConnection.prepare(string);
        int size = longSparseArray.size();
        int i = 1;
        for (int i2 = 0; i2 < size; i2++) {
            sQLiteStatementPrepare.bindLong(i, longSparseArray.keyAt(i2));
            i++;
        }
        try {
            int columnIndex = SQLiteStatementUtil.getColumnIndex(sQLiteStatementPrepare, "deletedMessageId");
            if (columnIndex == -1) {
                return;
            }
            while (sQLiteStatementPrepare.step()) {
                List list = (List) longSparseArray.get(sQLiteStatementPrepare.getLong(columnIndex));
                if (list != null) {
                    DeletedMessageReaction deletedMessageReaction = new DeletedMessageReaction();
                    deletedMessageReaction.fakeReactionId = sQLiteStatementPrepare.getLong(0);
                    deletedMessageReaction.deletedMessageId = sQLiteStatementPrepare.getLong(1);
                    if (sQLiteStatementPrepare.isNull(2)) {
                        deletedMessageReaction.emoticon = null;
                    } else {
                        deletedMessageReaction.emoticon = sQLiteStatementPrepare.getText(2);
                    }
                    deletedMessageReaction.documentId = sQLiteStatementPrepare.getLong(3);
                    deletedMessageReaction.isCustom = ((int) sQLiteStatementPrepare.getLong(4)) != 0;
                    deletedMessageReaction.isPaid = ((int) sQLiteStatementPrepare.getLong(5)) != 0;
                    deletedMessageReaction.count = (int) sQLiteStatementPrepare.getLong(6);
                    deletedMessageReaction.selfSelected = ((int) sQLiteStatementPrepare.getLong(7)) != 0;
                    list.add(deletedMessageReaction);
                }
            }
        } finally {
            sQLiteStatementPrepare.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit __fetchRelationshipDeletedMessageReactionAscomRadolynAyugramDatabaseEntitiesDeletedMessageReaction$lambda$0(DeletedMessageDao_Impl deletedMessageDao_Impl, SQLiteConnection sQLiteConnection, LongSparseArray _tmpMap) {
        Intrinsics.checkNotNullParameter(_tmpMap, "_tmpMap");
        deletedMessageDao_Impl.__fetchRelationshipDeletedMessageReactionAscomRadolynAyugramDatabaseEntitiesDeletedMessageReaction(sQLiteConnection, _tmpMap);
        return Unit.INSTANCE;
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
