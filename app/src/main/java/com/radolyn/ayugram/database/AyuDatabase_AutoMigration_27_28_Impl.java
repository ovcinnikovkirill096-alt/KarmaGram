package com.radolyn.ayugram.database;

import androidx.room.migration.Migration;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import kotlin.jvm.internal.Intrinsics;

public final class AyuDatabase_AutoMigration_27_28_Impl extends Migration {
    public AyuDatabase_AutoMigration_27_28_Impl() {
        super(27, 28);
    }

    @Override // androidx.room.migration.Migration
    public void migrate(SQLiteConnection connection) throws Exception {
        Intrinsics.checkNotNullParameter(connection, "connection");
        SQLite.execSQL(connection, "ALTER TABLE `DeletedDialog` ADD COLUMN `folderId` INTEGER DEFAULT NULL");
    }
}
