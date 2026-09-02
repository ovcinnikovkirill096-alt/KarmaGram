package com.radolyn.ayugram.database;

import androidx.room.migration.Migration;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import kotlin.jvm.internal.Intrinsics;

public final class AyuDatabase_AutoMigration_24_25_Impl extends Migration {
    public AyuDatabase_AutoMigration_24_25_Impl() {
        super(24, 25);
    }

    @Override // androidx.room.migration.Migration
    public void migrate(SQLiteConnection connection) throws Exception {
        Intrinsics.checkNotNullParameter(connection, "connection");
        SQLite.execSQL(connection, "ALTER TABLE `EditedMessage` ADD COLUMN `replySerialized` BLOB DEFAULT NULL");
        SQLite.execSQL(connection, "ALTER TABLE `DeletedMessage` ADD COLUMN `replySerialized` BLOB DEFAULT NULL");
    }
}
