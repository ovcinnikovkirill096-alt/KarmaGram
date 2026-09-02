package com.radolyn.ayugram.database;

import androidx.room.migration.Migration;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import kotlin.jvm.internal.Intrinsics;

public final class AyuDatabase_AutoMigration_29_30_Impl extends Migration {
    public AyuDatabase_AutoMigration_29_30_Impl() {
        super(29, 30);
    }

    @Override // androidx.room.migration.Migration
    public void migrate(SQLiteConnection connection) throws Exception {
        Intrinsics.checkNotNullParameter(connection, "connection");
        SQLite.execSQL(connection, "ALTER TABLE `EditedMessage` ADD COLUMN `postAuthor` TEXT DEFAULT NULL");
        SQLite.execSQL(connection, "ALTER TABLE `DeletedMessage` ADD COLUMN `postAuthor` TEXT DEFAULT NULL");
    }
}
