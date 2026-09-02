package androidx.sqlite.db;

public interface SupportSQLiteStatement extends SupportSQLiteProgram {
    void execute();

    int executeUpdateDelete();
}
