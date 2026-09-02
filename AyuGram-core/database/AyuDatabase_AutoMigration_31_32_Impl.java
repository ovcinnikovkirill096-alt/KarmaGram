package com.radolyn.ayugram.database;

import androidx.room.migration.Migration;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import kotlin.jvm.internal.Intrinsics;

public final class AyuDatabase_AutoMigration_31_32_Impl extends Migration {
    public AyuDatabase_AutoMigration_31_32_Impl() {
        super(31, 32);
    }

    @Override // androidx.room.migration.Migration
    public void migrate(SQLiteConnection connection) throws Exception {
        Intrinsics.checkNotNullParameter(connection, "connection");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `_new_EditedMessage` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `groupedId` INTEGER NOT NULL, `peerId` INTEGER NOT NULL, `fromId` INTEGER NOT NULL, `topicId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `date` INTEGER NOT NULL, `flags` INTEGER NOT NULL, `editDate` INTEGER NOT NULL, `views` INTEGER NOT NULL, `fwdFlags` INTEGER NOT NULL, `fwdFromId` INTEGER NOT NULL, `fwdName` TEXT, `fwdDate` INTEGER NOT NULL, `fwdPostAuthor` TEXT, `postAuthor` TEXT, `replyFlags` INTEGER NOT NULL, `replyMessageId` INTEGER NOT NULL, `replyPeerId` INTEGER NOT NULL, `replyTopId` INTEGER NOT NULL, `replyForumTopic` INTEGER NOT NULL, `replySerialized` BLOB, `replyMarkupSerialized` BLOB, `entityCreateDate` INTEGER NOT NULL, `text` TEXT, `textEntities` BLOB, `mediaPath` TEXT, `hqThumbPath` TEXT, `documentType` INTEGER NOT NULL, `documentSerialized` BLOB, `thumbsSerialized` BLOB, `documentAttributesSerialized` BLOB, `mimeType` TEXT)");
        SQLite.execSQL(connection, "INSERT INTO `_new_EditedMessage` (`fakeId`,`userId`,`dialogId`,`groupedId`,`peerId`,`fromId`,`topicId`,`messageId`,`date`,`flags`,`editDate`,`views`,`fwdFlags`,`fwdFromId`,`fwdName`,`fwdDate`,`fwdPostAuthor`,`postAuthor`,`replyFlags`,`replyMessageId`,`replyPeerId`,`replyTopId`,`replyForumTopic`,`replySerialized`,`replyMarkupSerialized`,`entityCreateDate`,`text`,`textEntities`,`mediaPath`,`hqThumbPath`,`documentType`,`documentSerialized`,`thumbsSerialized`,`documentAttributesSerialized`,`mimeType`) SELECT `fakeId`,`userId`,`dialogId`,`groupedId`,`peerId`,`fromId`,`topicId`,`messageId`,`date`,`flags`,`editDate`,`views`,`fwdFlags`,`fwdFromId`,`fwdName`,`fwdDate`,`fwdPostAuthor`,`postAuthor`,`replyFlags`,`replyMessageId`,`replyPeerId`,`replyTopId`,`replyForumTopic`,`replySerialized`,`replyMarkupSerialized`,`entityCreateDate`,`text`,`textEntities`,`mediaPath`,`hqThumbPath`,`documentType`,`documentSerialized`,`thumbsSerialized`,`documentAttributesSerialized`,`mimeType` FROM `EditedMessage`");
        SQLite.execSQL(connection, "DROP TABLE `EditedMessage`");
        SQLite.execSQL(connection, "ALTER TABLE `_new_EditedMessage` RENAME TO `EditedMessage`");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_EditedMessage_userId_dialogId_messageId` ON `EditedMessage` (`userId`, `dialogId`, `messageId`)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_EditedMessage_userId_dialogId_messageId_fakeId` ON `EditedMessage` (`userId`, `dialogId`, `messageId`, `fakeId`)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_EditedMessage_userId_dialogId_messageId_mediaPath` ON `EditedMessage` (`userId`, `dialogId`, `messageId`, `mediaPath`)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_EditedMessage_userId_dialogId_messageId_entityCreateDate` ON `EditedMessage` (`userId`, `dialogId`, `messageId`, `entityCreateDate`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `_new_DeletedMessage` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `groupedId` INTEGER NOT NULL, `peerId` INTEGER NOT NULL, `fromId` INTEGER NOT NULL, `topicId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `date` INTEGER NOT NULL, `flags` INTEGER NOT NULL, `editDate` INTEGER NOT NULL, `views` INTEGER NOT NULL, `fwdFlags` INTEGER NOT NULL, `fwdFromId` INTEGER NOT NULL, `fwdName` TEXT, `fwdDate` INTEGER NOT NULL, `fwdPostAuthor` TEXT, `postAuthor` TEXT, `replyFlags` INTEGER NOT NULL, `replyMessageId` INTEGER NOT NULL, `replyPeerId` INTEGER NOT NULL, `replyTopId` INTEGER NOT NULL, `replyForumTopic` INTEGER NOT NULL, `replySerialized` BLOB, `replyMarkupSerialized` BLOB, `entityCreateDate` INTEGER NOT NULL, `text` TEXT, `textEntities` BLOB, `mediaPath` TEXT, `hqThumbPath` TEXT, `documentType` INTEGER NOT NULL, `documentSerialized` BLOB, `thumbsSerialized` BLOB, `documentAttributesSerialized` BLOB, `mimeType` TEXT)");
        SQLite.execSQL(connection, "INSERT INTO `_new_DeletedMessage` (`fakeId`,`userId`,`dialogId`,`groupedId`,`peerId`,`fromId`,`topicId`,`messageId`,`date`,`flags`,`editDate`,`views`,`fwdFlags`,`fwdFromId`,`fwdName`,`fwdDate`,`fwdPostAuthor`,`postAuthor`,`replyFlags`,`replyMessageId`,`replyPeerId`,`replyTopId`,`replyForumTopic`,`replySerialized`,`replyMarkupSerialized`,`entityCreateDate`,`text`,`textEntities`,`mediaPath`,`hqThumbPath`,`documentType`,`documentSerialized`,`thumbsSerialized`,`documentAttributesSerialized`,`mimeType`) SELECT `fakeId`,`userId`,`dialogId`,`groupedId`,`peerId`,`fromId`,`topicId`,`messageId`,`date`,`flags`,`editDate`,`views`,`fwdFlags`,`fwdFromId`,`fwdName`,`fwdDate`,`fwdPostAuthor`,`postAuthor`,`replyFlags`,`replyMessageId`,`replyPeerId`,`replyTopId`,`replyForumTopic`,`replySerialized`,`replyMarkupSerialized`,`entityCreateDate`,`text`,`textEntities`,`mediaPath`,`hqThumbPath`,`documentType`,`documentSerialized`,`thumbsSerialized`,`documentAttributesSerialized`,`mimeType` FROM `DeletedMessage`");
        SQLite.execSQL(connection, "DROP TABLE `DeletedMessage`");
        SQLite.execSQL(connection, "ALTER TABLE `_new_DeletedMessage` RENAME TO `DeletedMessage`");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_DeletedMessage_userId_dialogId_topicId_messageId` ON `DeletedMessage` (`userId`, `dialogId`, `topicId`, `messageId`)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_DeletedMessage_groupedId` ON `DeletedMessage` (`groupedId`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `_new_DeletedMessageReaction` (`fakeReactionId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `deletedMessageId` INTEGER NOT NULL, `emoticon` TEXT, `documentId` INTEGER NOT NULL, `isCustom` INTEGER NOT NULL, `count` INTEGER NOT NULL, `selfSelected` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "INSERT INTO `_new_DeletedMessageReaction` (`fakeReactionId`,`deletedMessageId`,`emoticon`,`documentId`,`isCustom`,`count`,`selfSelected`) SELECT `fakeReactionId`,`deletedMessageId`,`emoticon`,`documentId`,`isCustom`,`count`,`selfSelected` FROM `DeletedMessageReaction`");
        SQLite.execSQL(connection, "DROP TABLE `DeletedMessageReaction`");
        SQLite.execSQL(connection, "ALTER TABLE `_new_DeletedMessageReaction` RENAME TO `DeletedMessageReaction`");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_DeletedMessageReaction_deletedMessageId` ON `DeletedMessageReaction` (`deletedMessageId`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `_new_DeletedDialog` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `peerId` INTEGER NOT NULL, `folderId` INTEGER, `topMessage` INTEGER NOT NULL, `lastMessageDate` INTEGER NOT NULL, `flags` INTEGER NOT NULL, `entityCreateDate` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "INSERT INTO `_new_DeletedDialog` (`fakeId`,`userId`,`dialogId`,`peerId`,`folderId`,`topMessage`,`lastMessageDate`,`flags`,`entityCreateDate`) SELECT `fakeId`,`userId`,`dialogId`,`peerId`,`folderId`,`topMessage`,`lastMessageDate`,`flags`,`entityCreateDate` FROM `DeletedDialog`");
        SQLite.execSQL(connection, "DROP TABLE `DeletedDialog`");
        SQLite.execSQL(connection, "ALTER TABLE `_new_DeletedDialog` RENAME TO `DeletedDialog`");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_DeletedDialog_userId_dialogId` ON `DeletedDialog` (`userId`, `dialogId`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `_new_SpyMessageRead` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `entityCreateDate` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "INSERT INTO `_new_SpyMessageRead` (`fakeId`,`userId`,`dialogId`,`messageId`,`entityCreateDate`) SELECT `fakeId`,`userId`,`dialogId`,`messageId`,`entityCreateDate` FROM `SpyMessageRead`");
        SQLite.execSQL(connection, "DROP TABLE `SpyMessageRead`");
        SQLite.execSQL(connection, "ALTER TABLE `_new_SpyMessageRead` RENAME TO `SpyMessageRead`");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_SpyMessageRead_userId_dialogId_messageId` ON `SpyMessageRead` (`userId`, `dialogId`, `messageId`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `_new_SpyMessageContentsRead` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `entityCreateDate` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "INSERT INTO `_new_SpyMessageContentsRead` (`fakeId`,`userId`,`dialogId`,`messageId`,`entityCreateDate`) SELECT `fakeId`,`userId`,`dialogId`,`messageId`,`entityCreateDate` FROM `SpyMessageContentsRead`");
        SQLite.execSQL(connection, "DROP TABLE `SpyMessageContentsRead`");
        SQLite.execSQL(connection, "ALTER TABLE `_new_SpyMessageContentsRead` RENAME TO `SpyMessageContentsRead`");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_SpyMessageContentsRead_userId_dialogId_messageId` ON `SpyMessageContentsRead` (`userId`, `dialogId`, `messageId`)");
    }
}
