package com.radolyn.ayugram.database;

import androidx.room.migration.Migration;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import kotlin.jvm.internal.Intrinsics;

public final class AyuDatabase_AutoMigration_30_31_Impl extends Migration {
    public AyuDatabase_AutoMigration_30_31_Impl() {
        super(30, 31);
    }

    @Override // androidx.room.migration.Migration
    public void migrate(SQLiteConnection connection) throws Exception {
        Intrinsics.checkNotNullParameter(connection, "connection");
        SQLite.execSQL(connection, "ALTER TABLE `RegexFilter` ADD COLUMN `reversed` INTEGER NOT NULL DEFAULT false");
    }
}
