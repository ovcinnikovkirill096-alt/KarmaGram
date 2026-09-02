package com.radolyn.ayugram.database;

import androidx.room.migration.Migration;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import kotlin.jvm.internal.Intrinsics;

public final class AyuDatabase_AutoMigration_34_35_Impl extends Migration {
    public AyuDatabase_AutoMigration_34_35_Impl() {
        super(34, 35);
    }

    @Override // androidx.room.migration.Migration
    public void migrate(SQLiteConnection connection) throws Exception {
        Intrinsics.checkNotNullParameter(connection, "connection");
        SQLite.execSQL(connection, "ALTER TABLE `DeletedMessageReaction` ADD COLUMN `isPaid` INTEGER NOT NULL DEFAULT false");
    }
}
