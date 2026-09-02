package com.radolyn.ayugram.database;

import androidx.room.migration.Migration;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import kotlin.jvm.internal.Intrinsics;

public final class AyuDatabase_AutoMigration_32_33_Impl extends Migration {
    public AyuDatabase_AutoMigration_32_33_Impl() {
        super(32, 33);
    }

    @Override // androidx.room.migration.Migration
    public void migrate(SQLiteConnection connection) throws Exception {
        Intrinsics.checkNotNullParameter(connection, "connection");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `SpyLastSeen` (`userId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `lastSeenDate` INTEGER NOT NULL)");
    }
}
