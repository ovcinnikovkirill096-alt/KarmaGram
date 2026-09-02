package androidx.sqlite;

public interface SQLiteConnection extends AutoCloseable {
    @Override // java.lang.AutoCloseable
    void close();

    boolean inTransaction();

    SQLiteStatement prepare(String str);
}
