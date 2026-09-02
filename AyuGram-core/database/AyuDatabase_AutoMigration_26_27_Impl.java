package com.radolyn.ayugram.database;

import androidx.room.migration.Migration;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import kotlin.jvm.internal.Intrinsics;

public final class AyuDatabase_AutoMigration_26_27_Impl extends Migration {
    public AyuDatabase_AutoMigration_26_27_Impl() {
        super(26, 27);
    }

    @Override // androidx.room.migration.Migration
    public void migrate(SQLiteConnection connection) throws Exception {
        Intrinsics.checkNotNullParameter(connection, "connection");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `SpyMessageRead` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `entityCreateDate` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `SpyMessageContentsRead` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `entityCreateDate` INTEGER NOT NULL)");
    }
}
