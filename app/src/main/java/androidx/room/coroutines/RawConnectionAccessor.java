package androidx.room.coroutines;

import androidx.sqlite.SQLiteConnection;

public interface RawConnectionAccessor {
    SQLiteConnection getRawConnection();
}
