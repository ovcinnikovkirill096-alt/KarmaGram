package androidx.sqlite;

public interface SQLiteDriver {
    boolean hasConnectionPool();

    SQLiteConnection open(String str);
}
