package com.radolyn.ayugram.database;

import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import com.radolyn.ayugram.database.dao.DeletedDialogDao;
import com.radolyn.ayugram.database.dao.DeletedDialogDao_Impl;
import com.radolyn.ayugram.database.dao.DeletedMessageDao;
import com.radolyn.ayugram.database.dao.DeletedMessageDao_Impl;
import com.radolyn.ayugram.database.dao.EditedMessageDao;
import com.radolyn.ayugram.database.dao.EditedMessageDao_Impl;
import com.radolyn.ayugram.database.dao.RegexFilterDao;
import com.radolyn.ayugram.database.dao.RegexFilterDao_Impl;
import com.radolyn.ayugram.database.dao.SpyDao;
import com.radolyn.ayugram.database.dao.SpyDao_Impl;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import org.telegram.messenger.NotificationBadge;

public final class AyuDatabase_Impl extends AyuDatabase {
    private final Lazy _editedMessageDao = LazyKt.lazy(new Function0() { // from class: com.radolyn.ayugram.database.AyuDatabase_Impl$$ExternalSyntheticLambda0
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return AyuDatabase_Impl._editedMessageDao$lambda$0(this.f$0);
        }
    });
    private final Lazy _deletedMessageDao = LazyKt.lazy(new Function0() { // from class: com.radolyn.ayugram.database.AyuDatabase_Impl$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return AyuDatabase_Impl._deletedMessageDao$lambda$0(this.f$0);
        }
    });
    private final Lazy _deletedDialogDao = LazyKt.lazy(new Function0() { // from class: com.radolyn.ayugram.database.AyuDatabase_Impl$$ExternalSyntheticLambda2
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return AyuDatabase_Impl._deletedDialogDao$lambda$0(this.f$0);
        }
    });
    private final Lazy _regexFilterDao = LazyKt.lazy(new Function0() { // from class: com.radolyn.ayugram.database.AyuDatabase_Impl$$ExternalSyntheticLambda3
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return AyuDatabase_Impl._regexFilterDao$lambda$0(this.f$0);
        }
    });
    private final Lazy _spyDao = LazyKt.lazy(new Function0() { // from class: com.radolyn.ayugram.database.AyuDatabase_Impl$$ExternalSyntheticLambda4
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return AyuDatabase_Impl._spyDao$lambda$0(this.f$0);
        }
    });

    /* JADX INFO: Access modifiers changed from: private */
    public static final EditedMessageDao_Impl _editedMessageDao$lambda$0(AyuDatabase_Impl ayuDatabase_Impl) {
        return new EditedMessageDao_Impl(ayuDatabase_Impl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final DeletedMessageDao_Impl _deletedMessageDao$lambda$0(AyuDatabase_Impl ayuDatabase_Impl) {
        return new DeletedMessageDao_Impl(ayuDatabase_Impl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final DeletedDialogDao_Impl _deletedDialogDao$lambda$0(AyuDatabase_Impl ayuDatabase_Impl) {
        return new DeletedDialogDao_Impl(ayuDatabase_Impl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final RegexFilterDao_Impl _regexFilterDao$lambda$0(AyuDatabase_Impl ayuDatabase_Impl) {
        return new RegexFilterDao_Impl(ayuDatabase_Impl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final SpyDao_Impl _spyDao$lambda$0(AyuDatabase_Impl ayuDatabase_Impl) {
        return new SpyDao_Impl(ayuDatabase_Impl);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.room.RoomDatabase
    public RoomOpenDelegate createOpenDelegate() {
        return new RoomOpenDelegate() { // from class: com.radolyn.ayugram.database.AyuDatabase_Impl$createOpenDelegate$_openDelegate$1
            @Override // androidx.room.RoomOpenDelegate
            public void onCreate(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
            }

            @Override // androidx.room.RoomOpenDelegate
            public void onPostMigrate(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
            }

            {
                super(35, "3902602c75eac45a92a76e4acaab4efd", "aeaa914f9a33d8ccef0dcf41ed44d966");
            }

            @Override // androidx.room.RoomOpenDelegate
            public void createAllTables(SQLiteConnection connection) throws Exception {
                Intrinsics.checkNotNullParameter(connection, "connection");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `EditedMessage` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `groupedId` INTEGER NOT NULL, `peerId` INTEGER NOT NULL, `fromId` INTEGER NOT NULL, `topicId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `date` INTEGER NOT NULL, `flags` INTEGER NOT NULL, `editDate` INTEGER NOT NULL, `views` INTEGER NOT NULL, `fwdFlags` INTEGER NOT NULL, `fwdFromId` INTEGER NOT NULL, `fwdName` TEXT, `fwdDate` INTEGER NOT NULL, `fwdPostAuthor` TEXT, `postAuthor` TEXT, `replyFlags` INTEGER NOT NULL, `replyMessageId` INTEGER NOT NULL, `replyPeerId` INTEGER NOT NULL, `replyTopId` INTEGER NOT NULL, `replyForumTopic` INTEGER NOT NULL, `replySerialized` BLOB, `replyMarkupSerialized` BLOB, `entityCreateDate` INTEGER NOT NULL, `text` TEXT, `textEntities` BLOB, `mediaPath` TEXT, `hqThumbPath` TEXT, `documentType` INTEGER NOT NULL, `documentSerialized` BLOB, `thumbsSerialized` BLOB, `documentAttributesSerialized` BLOB, `mimeType` TEXT)");
                SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_EditedMessage_userId_dialogId_messageId` ON `EditedMessage` (`userId`, `dialogId`, `messageId`)");
                SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_EditedMessage_userId_dialogId_messageId_fakeId` ON `EditedMessage` (`userId`, `dialogId`, `messageId`, `fakeId`)");
                SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_EditedMessage_userId_dialogId_messageId_mediaPath` ON `EditedMessage` (`userId`, `dialogId`, `messageId`, `mediaPath`)");
                SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_EditedMessage_userId_dialogId_messageId_entityCreateDate` ON `EditedMessage` (`userId`, `dialogId`, `messageId`, `entityCreateDate`)");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `DeletedMessage` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `groupedId` INTEGER NOT NULL, `peerId` INTEGER NOT NULL, `fromId` INTEGER NOT NULL, `topicId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `date` INTEGER NOT NULL, `flags` INTEGER NOT NULL, `editDate` INTEGER NOT NULL, `views` INTEGER NOT NULL, `fwdFlags` INTEGER NOT NULL, `fwdFromId` INTEGER NOT NULL, `fwdName` TEXT, `fwdDate` INTEGER NOT NULL, `fwdPostAuthor` TEXT, `postAuthor` TEXT, `replyFlags` INTEGER NOT NULL, `replyMessageId` INTEGER NOT NULL, `replyPeerId` INTEGER NOT NULL, `replyTopId` INTEGER NOT NULL, `replyForumTopic` INTEGER NOT NULL, `replySerialized` BLOB, `replyMarkupSerialized` BLOB, `entityCreateDate` INTEGER NOT NULL, `text` TEXT, `textEntities` BLOB, `mediaPath` TEXT, `hqThumbPath` TEXT, `documentType` INTEGER NOT NULL, `documentSerialized` BLOB, `thumbsSerialized` BLOB, `documentAttributesSerialized` BLOB, `mimeType` TEXT)");
                SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_DeletedMessage_userId_dialogId_topicId_messageId` ON `DeletedMessage` (`userId`, `dialogId`, `topicId`, `messageId`)");
                SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_DeletedMessage_dialogId_groupedId` ON `DeletedMessage` (`dialogId`, `groupedId`)");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `DeletedMessageReaction` (`fakeReactionId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `deletedMessageId` INTEGER NOT NULL, `emoticon` TEXT, `documentId` INTEGER NOT NULL, `isCustom` INTEGER NOT NULL, `isPaid` INTEGER NOT NULL DEFAULT false, `count` INTEGER NOT NULL, `selfSelected` INTEGER NOT NULL)");
                SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_DeletedMessageReaction_deletedMessageId` ON `DeletedMessageReaction` (`deletedMessageId`)");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `DeletedDialog` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `peerId` INTEGER NOT NULL, `folderId` INTEGER, `topMessage` INTEGER NOT NULL, `lastMessageDate` INTEGER NOT NULL, `flags` INTEGER NOT NULL, `entityCreateDate` INTEGER NOT NULL)");
                SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_DeletedDialog_userId_dialogId` ON `DeletedDialog` (`userId`, `dialogId`)");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `RegexFilter` (`id` BLOB NOT NULL, `text` TEXT, `enabled` INTEGER NOT NULL, `reversed` INTEGER NOT NULL DEFAULT false, `caseInsensitive` INTEGER NOT NULL, `dialogId` INTEGER, PRIMARY KEY(`id`))");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `RegexFilterGlobalExclusion` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dialogId` INTEGER NOT NULL, `filterId` BLOB)");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `SpyMessageRead` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `entityCreateDate` INTEGER NOT NULL)");
                SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_SpyMessageRead_userId_dialogId_messageId` ON `SpyMessageRead` (`userId`, `dialogId`, `messageId`)");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `SpyMessageContentsRead` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `entityCreateDate` INTEGER NOT NULL)");
                SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_SpyMessageContentsRead_userId_dialogId_messageId` ON `SpyMessageContentsRead` (`userId`, `dialogId`, `messageId`)");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `SpyLastSeen` (`userId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `lastSeenDate` INTEGER NOT NULL)");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
                SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3902602c75eac45a92a76e4acaab4efd')");
            }

            @Override // androidx.room.RoomOpenDelegate
            public void dropAllTables(SQLiteConnection connection) throws Exception {
                Intrinsics.checkNotNullParameter(connection, "connection");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `EditedMessage`");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `DeletedMessage`");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `DeletedMessageReaction`");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `DeletedDialog`");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `RegexFilter`");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `RegexFilterGlobalExclusion`");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `SpyMessageRead`");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `SpyMessageContentsRead`");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `SpyLastSeen`");
            }

            @Override // androidx.room.RoomOpenDelegate
            public void onOpen(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
                this.this$0.internalInitInvalidationTracker(connection);
            }

            @Override // androidx.room.RoomOpenDelegate
            public void onPreMigrate(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
                DBUtil.dropFtsSyncTriggers(connection);
            }

            @Override // androidx.room.RoomOpenDelegate
            public RoomOpenDelegate.ValidationResult onValidateSchema(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                linkedHashMap.put("fakeId", new TableInfo.Column("fakeId", "INTEGER", true, 1, null, 1));
                linkedHashMap.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("dialogId", new TableInfo.Column("dialogId", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("groupedId", new TableInfo.Column("groupedId", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("peerId", new TableInfo.Column("peerId", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("fromId", new TableInfo.Column("fromId", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("topicId", new TableInfo.Column("topicId", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("messageId", new TableInfo.Column("messageId", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("flags", new TableInfo.Column("flags", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("editDate", new TableInfo.Column("editDate", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("views", new TableInfo.Column("views", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("fwdFlags", new TableInfo.Column("fwdFlags", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("fwdFromId", new TableInfo.Column("fwdFromId", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("fwdName", new TableInfo.Column("fwdName", "TEXT", false, 0, null, 1));
                linkedHashMap.put("fwdDate", new TableInfo.Column("fwdDate", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("fwdPostAuthor", new TableInfo.Column("fwdPostAuthor", "TEXT", false, 0, null, 1));
                linkedHashMap.put("postAuthor", new TableInfo.Column("postAuthor", "TEXT", false, 0, null, 1));
                linkedHashMap.put("replyFlags", new TableInfo.Column("replyFlags", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("replyMessageId", new TableInfo.Column("replyMessageId", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("replyPeerId", new TableInfo.Column("replyPeerId", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("replyTopId", new TableInfo.Column("replyTopId", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("replyForumTopic", new TableInfo.Column("replyForumTopic", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("replySerialized", new TableInfo.Column("replySerialized", "BLOB", false, 0, null, 1));
                linkedHashMap.put("replyMarkupSerialized", new TableInfo.Column("replyMarkupSerialized", "BLOB", false, 0, null, 1));
                linkedHashMap.put("entityCreateDate", new TableInfo.Column("entityCreateDate", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("text", new TableInfo.Column("text", "TEXT", false, 0, null, 1));
                linkedHashMap.put("textEntities", new TableInfo.Column("textEntities", "BLOB", false, 0, null, 1));
                linkedHashMap.put("mediaPath", new TableInfo.Column("mediaPath", "TEXT", false, 0, null, 1));
                linkedHashMap.put("hqThumbPath", new TableInfo.Column("hqThumbPath", "TEXT", false, 0, null, 1));
                linkedHashMap.put("documentType", new TableInfo.Column("documentType", "INTEGER", true, 0, null, 1));
                linkedHashMap.put("documentSerialized", new TableInfo.Column("documentSerialized", "BLOB", false, 0, null, 1));
                linkedHashMap.put("thumbsSerialized", new TableInfo.Column("thumbsSerialized", "BLOB", false, 0, null, 1));
                linkedHashMap.put("documentAttributesSerialized", new TableInfo.Column("documentAttributesSerialized", "BLOB", false, 0, null, 1));
                linkedHashMap.put("mimeType", new TableInfo.Column("mimeType", "TEXT", false, 0, null, 1));
                LinkedHashSet linkedHashSet = new LinkedHashSet();
                LinkedHashSet linkedHashSet2 = new LinkedHashSet();
                linkedHashSet2.add(new TableInfo.Index("index_EditedMessage_userId_dialogId_messageId", false, CollectionsKt.listOf((Object[]) new String[]{"userId", "dialogId", "messageId"}), CollectionsKt.listOf((Object[]) new String[]{"ASC", "ASC", "ASC"})));
                linkedHashSet2.add(new TableInfo.Index("index_EditedMessage_userId_dialogId_messageId_fakeId", false, CollectionsKt.listOf((Object[]) new String[]{"userId", "dialogId", "messageId", "fakeId"}), CollectionsKt.listOf((Object[]) new String[]{"ASC", "ASC", "ASC", "ASC"})));
                linkedHashSet2.add(new TableInfo.Index("index_EditedMessage_userId_dialogId_messageId_mediaPath", false, CollectionsKt.listOf((Object[]) new String[]{"userId", "dialogId", "messageId", "mediaPath"}), CollectionsKt.listOf((Object[]) new String[]{"ASC", "ASC", "ASC", "ASC"})));
                linkedHashSet2.add(new TableInfo.Index("index_EditedMessage_userId_dialogId_messageId_entityCreateDate", false, CollectionsKt.listOf((Object[]) new String[]{"userId", "dialogId", "messageId", "entityCreateDate"}), CollectionsKt.listOf((Object[]) new String[]{"ASC", "ASC", "ASC", "ASC"})));
                TableInfo tableInfo = new TableInfo("EditedMessage", linkedHashMap, linkedHashSet, linkedHashSet2);
                TableInfo.Companion companion = TableInfo.Companion;
                TableInfo tableInfo2 = companion.read(connection, "EditedMessage");
                if (!tableInfo.equals(tableInfo2)) {
                    return new RoomOpenDelegate.ValidationResult(false, "EditedMessage(com.radolyn.ayugram.database.entities.EditedMessage).\n Expected:\n" + tableInfo + "\n Found:\n" + tableInfo2);
                }
                LinkedHashMap linkedHashMap2 = new LinkedHashMap();
                linkedHashMap2.put("fakeId", new TableInfo.Column("fakeId", "INTEGER", true, 1, null, 1));
                linkedHashMap2.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("dialogId", new TableInfo.Column("dialogId", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("groupedId", new TableInfo.Column("groupedId", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("peerId", new TableInfo.Column("peerId", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("fromId", new TableInfo.Column("fromId", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("topicId", new TableInfo.Column("topicId", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("messageId", new TableInfo.Column("messageId", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("flags", new TableInfo.Column("flags", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("editDate", new TableInfo.Column("editDate", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("views", new TableInfo.Column("views", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("fwdFlags", new TableInfo.Column("fwdFlags", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("fwdFromId", new TableInfo.Column("fwdFromId", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("fwdName", new TableInfo.Column("fwdName", "TEXT", false, 0, null, 1));
                linkedHashMap2.put("fwdDate", new TableInfo.Column("fwdDate", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("fwdPostAuthor", new TableInfo.Column("fwdPostAuthor", "TEXT", false, 0, null, 1));
                linkedHashMap2.put("postAuthor", new TableInfo.Column("postAuthor", "TEXT", false, 0, null, 1));
                linkedHashMap2.put("replyFlags", new TableInfo.Column("replyFlags", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("replyMessageId", new TableInfo.Column("replyMessageId", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("replyPeerId", new TableInfo.Column("replyPeerId", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("replyTopId", new TableInfo.Column("replyTopId", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("replyForumTopic", new TableInfo.Column("replyForumTopic", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("replySerialized", new TableInfo.Column("replySerialized", "BLOB", false, 0, null, 1));
                linkedHashMap2.put("replyMarkupSerialized", new TableInfo.Column("replyMarkupSerialized", "BLOB", false, 0, null, 1));
                linkedHashMap2.put("entityCreateDate", new TableInfo.Column("entityCreateDate", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("text", new TableInfo.Column("text", "TEXT", false, 0, null, 1));
                linkedHashMap2.put("textEntities", new TableInfo.Column("textEntities", "BLOB", false, 0, null, 1));
                linkedHashMap2.put("mediaPath", new TableInfo.Column("mediaPath", "TEXT", false, 0, null, 1));
                linkedHashMap2.put("hqThumbPath", new TableInfo.Column("hqThumbPath", "TEXT", false, 0, null, 1));
                linkedHashMap2.put("documentType", new TableInfo.Column("documentType", "INTEGER", true, 0, null, 1));
                linkedHashMap2.put("documentSerialized", new TableInfo.Column("documentSerialized", "BLOB", false, 0, null, 1));
                linkedHashMap2.put("thumbsSerialized", new TableInfo.Column("thumbsSerialized", "BLOB", false, 0, null, 1));
                linkedHashMap2.put("documentAttributesSerialized", new TableInfo.Column("documentAttributesSerialized", "BLOB", false, 0, null, 1));
                linkedHashMap2.put("mimeType", new TableInfo.Column("mimeType", "TEXT", false, 0, null, 1));
                LinkedHashSet linkedHashSet3 = new LinkedHashSet();
                LinkedHashSet linkedHashSet4 = new LinkedHashSet();
                linkedHashSet4.add(new TableInfo.Index("index_DeletedMessage_userId_dialogId_topicId_messageId", false, CollectionsKt.listOf((Object[]) new String[]{"userId", "dialogId", "topicId", "messageId"}), CollectionsKt.listOf((Object[]) new String[]{"ASC", "ASC", "ASC", "ASC"})));
                linkedHashSet4.add(new TableInfo.Index("index_DeletedMessage_dialogId_groupedId", false, CollectionsKt.listOf((Object[]) new String[]{"dialogId", "groupedId"}), CollectionsKt.listOf((Object[]) new String[]{"ASC", "ASC"})));
                TableInfo tableInfo3 = new TableInfo("DeletedMessage", linkedHashMap2, linkedHashSet3, linkedHashSet4);
                TableInfo tableInfo4 = companion.read(connection, "DeletedMessage");
                if (!tableInfo3.equals(tableInfo4)) {
                    return new RoomOpenDelegate.ValidationResult(false, "DeletedMessage(com.radolyn.ayugram.database.entities.DeletedMessage).\n Expected:\n" + tableInfo3 + "\n Found:\n" + tableInfo4);
                }
                LinkedHashMap linkedHashMap3 = new LinkedHashMap();
                linkedHashMap3.put("fakeReactionId", new TableInfo.Column("fakeReactionId", "INTEGER", true, 1, null, 1));
                linkedHashMap3.put("deletedMessageId", new TableInfo.Column("deletedMessageId", "INTEGER", true, 0, null, 1));
                linkedHashMap3.put("emoticon", new TableInfo.Column("emoticon", "TEXT", false, 0, null, 1));
                linkedHashMap3.put("documentId", new TableInfo.Column("documentId", "INTEGER", true, 0, null, 1));
                linkedHashMap3.put("isCustom", new TableInfo.Column("isCustom", "INTEGER", true, 0, null, 1));
                linkedHashMap3.put("isPaid", new TableInfo.Column("isPaid", "INTEGER", true, 0, "false", 1));
                linkedHashMap3.put(NotificationBadge.NewHtcHomeBadger.COUNT, new TableInfo.Column(NotificationBadge.NewHtcHomeBadger.COUNT, "INTEGER", true, 0, null, 1));
                linkedHashMap3.put("selfSelected", new TableInfo.Column("selfSelected", "INTEGER", true, 0, null, 1));
                LinkedHashSet linkedHashSet5 = new LinkedHashSet();
                LinkedHashSet linkedHashSet6 = new LinkedHashSet();
                linkedHashSet6.add(new TableInfo.Index("index_DeletedMessageReaction_deletedMessageId", false, CollectionsKt.listOf("deletedMessageId"), CollectionsKt.listOf("ASC")));
                TableInfo tableInfo5 = new TableInfo("DeletedMessageReaction", linkedHashMap3, linkedHashSet5, linkedHashSet6);
                TableInfo tableInfo6 = companion.read(connection, "DeletedMessageReaction");
                if (!tableInfo5.equals(tableInfo6)) {
                    return new RoomOpenDelegate.ValidationResult(false, "DeletedMessageReaction(com.radolyn.ayugram.database.entities.DeletedMessageReaction).\n Expected:\n" + tableInfo5 + "\n Found:\n" + tableInfo6);
                }
                LinkedHashMap linkedHashMap4 = new LinkedHashMap();
                linkedHashMap4.put("fakeId", new TableInfo.Column("fakeId", "INTEGER", true, 1, null, 1));
                linkedHashMap4.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, 1));
                linkedHashMap4.put("dialogId", new TableInfo.Column("dialogId", "INTEGER", true, 0, null, 1));
                linkedHashMap4.put("peerId", new TableInfo.Column("peerId", "INTEGER", true, 0, null, 1));
                linkedHashMap4.put("folderId", new TableInfo.Column("folderId", "INTEGER", false, 0, null, 1));
                linkedHashMap4.put("topMessage", new TableInfo.Column("topMessage", "INTEGER", true, 0, null, 1));
                linkedHashMap4.put("lastMessageDate", new TableInfo.Column("lastMessageDate", "INTEGER", true, 0, null, 1));
                linkedHashMap4.put("flags", new TableInfo.Column("flags", "INTEGER", true, 0, null, 1));
                linkedHashMap4.put("entityCreateDate", new TableInfo.Column("entityCreateDate", "INTEGER", true, 0, null, 1));
                LinkedHashSet linkedHashSet7 = new LinkedHashSet();
                LinkedHashSet linkedHashSet8 = new LinkedHashSet();
                linkedHashSet8.add(new TableInfo.Index("index_DeletedDialog_userId_dialogId", false, CollectionsKt.listOf((Object[]) new String[]{"userId", "dialogId"}), CollectionsKt.listOf((Object[]) new String[]{"ASC", "ASC"})));
                TableInfo tableInfo7 = new TableInfo("DeletedDialog", linkedHashMap4, linkedHashSet7, linkedHashSet8);
                TableInfo tableInfo8 = companion.read(connection, "DeletedDialog");
                if (!tableInfo7.equals(tableInfo8)) {
                    return new RoomOpenDelegate.ValidationResult(false, "DeletedDialog(com.radolyn.ayugram.database.entities.DeletedDialog).\n Expected:\n" + tableInfo7 + "\n Found:\n" + tableInfo8);
                }
                LinkedHashMap linkedHashMap5 = new LinkedHashMap();
                linkedHashMap5.put("id", new TableInfo.Column("id", "BLOB", true, 1, null, 1));
                linkedHashMap5.put("text", new TableInfo.Column("text", "TEXT", false, 0, null, 1));
                linkedHashMap5.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, 1));
                linkedHashMap5.put("reversed", new TableInfo.Column("reversed", "INTEGER", true, 0, "false", 1));
                linkedHashMap5.put("caseInsensitive", new TableInfo.Column("caseInsensitive", "INTEGER", true, 0, null, 1));
                linkedHashMap5.put("dialogId", new TableInfo.Column("dialogId", "INTEGER", false, 0, null, 1));
                TableInfo tableInfo9 = new TableInfo("RegexFilter", linkedHashMap5, new LinkedHashSet(), new LinkedHashSet());
                TableInfo tableInfo10 = companion.read(connection, "RegexFilter");
                if (!tableInfo9.equals(tableInfo10)) {
                    return new RoomOpenDelegate.ValidationResult(false, "RegexFilter(com.radolyn.ayugram.database.entities.RegexFilter).\n Expected:\n" + tableInfo9 + "\n Found:\n" + tableInfo10);
                }
                LinkedHashMap linkedHashMap6 = new LinkedHashMap();
                linkedHashMap6.put("fakeId", new TableInfo.Column("fakeId", "INTEGER", true, 1, null, 1));
                linkedHashMap6.put("dialogId", new TableInfo.Column("dialogId", "INTEGER", true, 0, null, 1));
                linkedHashMap6.put("filterId", new TableInfo.Column("filterId", "BLOB", false, 0, null, 1));
                TableInfo tableInfo11 = new TableInfo("RegexFilterGlobalExclusion", linkedHashMap6, new LinkedHashSet(), new LinkedHashSet());
                TableInfo tableInfo12 = companion.read(connection, "RegexFilterGlobalExclusion");
                if (!tableInfo11.equals(tableInfo12)) {
                    return new RoomOpenDelegate.ValidationResult(false, "RegexFilterGlobalExclusion(com.radolyn.ayugram.database.entities.RegexFilterGlobalExclusion).\n Expected:\n" + tableInfo11 + "\n Found:\n" + tableInfo12);
                }
                LinkedHashMap linkedHashMap7 = new LinkedHashMap();
                linkedHashMap7.put("fakeId", new TableInfo.Column("fakeId", "INTEGER", true, 1, null, 1));
                linkedHashMap7.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, 1));
                linkedHashMap7.put("dialogId", new TableInfo.Column("dialogId", "INTEGER", true, 0, null, 1));
                linkedHashMap7.put("messageId", new TableInfo.Column("messageId", "INTEGER", true, 0, null, 1));
                linkedHashMap7.put("entityCreateDate", new TableInfo.Column("entityCreateDate", "INTEGER", true, 0, null, 1));
                LinkedHashSet linkedHashSet9 = new LinkedHashSet();
                LinkedHashSet linkedHashSet10 = new LinkedHashSet();
                linkedHashSet10.add(new TableInfo.Index("index_SpyMessageRead_userId_dialogId_messageId", false, CollectionsKt.listOf((Object[]) new String[]{"userId", "dialogId", "messageId"}), CollectionsKt.listOf((Object[]) new String[]{"ASC", "ASC", "ASC"})));
                TableInfo tableInfo13 = new TableInfo("SpyMessageRead", linkedHashMap7, linkedHashSet9, linkedHashSet10);
                TableInfo tableInfo14 = companion.read(connection, "SpyMessageRead");
                if (!tableInfo13.equals(tableInfo14)) {
                    return new RoomOpenDelegate.ValidationResult(false, "SpyMessageRead(com.radolyn.ayugram.database.entities.SpyMessageRead).\n Expected:\n" + tableInfo13 + "\n Found:\n" + tableInfo14);
                }
                LinkedHashMap linkedHashMap8 = new LinkedHashMap();
                linkedHashMap8.put("fakeId", new TableInfo.Column("fakeId", "INTEGER", true, 1, null, 1));
                linkedHashMap8.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, 1));
                linkedHashMap8.put("dialogId", new TableInfo.Column("dialogId", "INTEGER", true, 0, null, 1));
                linkedHashMap8.put("messageId", new TableInfo.Column("messageId", "INTEGER", true, 0, null, 1));
                linkedHashMap8.put("entityCreateDate", new TableInfo.Column("entityCreateDate", "INTEGER", true, 0, null, 1));
                LinkedHashSet linkedHashSet11 = new LinkedHashSet();
                LinkedHashSet linkedHashSet12 = new LinkedHashSet();
                linkedHashSet12.add(new TableInfo.Index("index_SpyMessageContentsRead_userId_dialogId_messageId", false, CollectionsKt.listOf((Object[]) new String[]{"userId", "dialogId", "messageId"}), CollectionsKt.listOf((Object[]) new String[]{"ASC", "ASC", "ASC"})));
                TableInfo tableInfo15 = new TableInfo("SpyMessageContentsRead", linkedHashMap8, linkedHashSet11, linkedHashSet12);
                TableInfo tableInfo16 = companion.read(connection, "SpyMessageContentsRead");
                if (!tableInfo15.equals(tableInfo16)) {
                    return new RoomOpenDelegate.ValidationResult(false, "SpyMessageContentsRead(com.radolyn.ayugram.database.entities.SpyMessageContentsRead).\n Expected:\n" + tableInfo15 + "\n Found:\n" + tableInfo16);
                }
                LinkedHashMap linkedHashMap9 = new LinkedHashMap();
                linkedHashMap9.put("userId", new TableInfo.Column("userId", "INTEGER", true, 1, null, 1));
                linkedHashMap9.put("lastSeenDate", new TableInfo.Column("lastSeenDate", "INTEGER", true, 0, null, 1));
                TableInfo tableInfo17 = new TableInfo("SpyLastSeen", linkedHashMap9, new LinkedHashSet(), new LinkedHashSet());
                TableInfo tableInfo18 = companion.read(connection, "SpyLastSeen");
                if (!tableInfo17.equals(tableInfo18)) {
                    return new RoomOpenDelegate.ValidationResult(false, "SpyLastSeen(com.radolyn.ayugram.database.entities.SpyLastSeen).\n Expected:\n" + tableInfo17 + "\n Found:\n" + tableInfo18);
                }
                return new RoomOpenDelegate.ValidationResult(true, null);
            }
        };
    }

    @Override // androidx.room.RoomDatabase
    protected InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new LinkedHashMap(), new LinkedHashMap(), "EditedMessage", "DeletedMessage", "DeletedMessageReaction", "DeletedDialog", "RegexFilter", "RegexFilterGlobalExclusion", "SpyMessageRead", "SpyMessageContentsRead", "SpyLastSeen");
    }

    @Override // androidx.room.RoomDatabase
    public void clearAllTables() {
        super.performClear(false, "EditedMessage", "DeletedMessage", "DeletedMessageReaction", "DeletedDialog", "RegexFilter", "RegexFilterGlobalExclusion", "SpyMessageRead", "SpyMessageContentsRead", "SpyLastSeen");
    }

    @Override // androidx.room.RoomDatabase
    protected Map<KClass, List<KClass>> getRequiredTypeConverterClasses() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(Reflection.getOrCreateKotlinClass(EditedMessageDao.class), EditedMessageDao_Impl.Companion.getRequiredConverters());
        linkedHashMap.put(Reflection.getOrCreateKotlinClass(DeletedMessageDao.class), DeletedMessageDao_Impl.Companion.getRequiredConverters());
        linkedHashMap.put(Reflection.getOrCreateKotlinClass(DeletedDialogDao.class), DeletedDialogDao_Impl.Companion.getRequiredConverters());
        linkedHashMap.put(Reflection.getOrCreateKotlinClass(RegexFilterDao.class), RegexFilterDao_Impl.Companion.getRequiredConverters());
        linkedHashMap.put(Reflection.getOrCreateKotlinClass(SpyDao.class), SpyDao_Impl.Companion.getRequiredConverters());
        return linkedHashMap;
    }

    @Override // androidx.room.RoomDatabase
    public Set<KClass> getRequiredAutoMigrationSpecClasses() {
        return new LinkedHashSet();
    }

    @Override // androidx.room.RoomDatabase
    public List<Migration> createAutoMigrations(Map<KClass, ? extends AutoMigrationSpec> autoMigrationSpecs) {
        Intrinsics.checkNotNullParameter(autoMigrationSpecs, "autoMigrationSpecs");
        ArrayList arrayList = new ArrayList();
        arrayList.add(new AyuDatabase_AutoMigration_23_24_Impl());
        arrayList.add(new AyuDatabase_AutoMigration_24_25_Impl());
        arrayList.add(new AyuDatabase_AutoMigration_26_27_Impl());
        arrayList.add(new AyuDatabase_AutoMigration_27_28_Impl());
        arrayList.add(new AyuDatabase_AutoMigration_28_29_Impl());
        arrayList.add(new AyuDatabase_AutoMigration_29_30_Impl());
        arrayList.add(new AyuDatabase_AutoMigration_30_31_Impl());
        arrayList.add(new AyuDatabase_AutoMigration_31_32_Impl());
        arrayList.add(new AyuDatabase_AutoMigration_32_33_Impl());
        arrayList.add(new AyuDatabase_AutoMigration_34_35_Impl());
        return arrayList;
    }

    @Override // com.radolyn.ayugram.database.AyuDatabase
    public EditedMessageDao editedMessageDao() {
        return (EditedMessageDao) this._editedMessageDao.getValue();
    }

    @Override // com.radolyn.ayugram.database.AyuDatabase
    public DeletedMessageDao deletedMessageDao() {
        return (DeletedMessageDao) this._deletedMessageDao.getValue();
    }

    @Override // com.radolyn.ayugram.database.AyuDatabase
    public DeletedDialogDao deletedDialogDao() {
        return (DeletedDialogDao) this._deletedDialogDao.getValue();
    }

    @Override // com.radolyn.ayugram.database.AyuDatabase
    public RegexFilterDao regexFilterDao() {
        return (RegexFilterDao) this._regexFilterDao.getValue();
    }

    @Override // com.radolyn.ayugram.database.AyuDatabase
    public SpyDao spyDao() {
        return (SpyDao) this._spyDao.getValue();
    }
}
