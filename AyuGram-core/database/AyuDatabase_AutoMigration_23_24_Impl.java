package com.radolyn.ayugram.database;

import androidx.room.migration.Migration;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import kotlin.jvm.internal.Intrinsics;

public final class AyuDatabase_AutoMigration_23_24_Impl extends Migration {
    public AyuDatabase_AutoMigration_23_24_Impl() {
        super(23, 24);
    }

    @Override // androidx.room.migration.Migration
    public void migrate(SQLiteConnection connection) throws Exception {
        Intrinsics.checkNotNullParameter(connection, "connection");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `RegexFilter` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `text` TEXT, `enabled` INTEGER NOT NULL, `caseInsensitive` INTEGER NOT NULL, `dialogId` INTEGER)");
    }
}
