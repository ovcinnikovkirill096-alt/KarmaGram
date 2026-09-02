package androidx.room;

import androidx.sqlite.SQLiteStatement;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

final class BindOnlySQLiteStatement implements SQLiteStatement {
    public static final Companion Companion = new Companion(null);
    private final /* synthetic */ SQLiteStatement $$delegate_0;

    @Override // androidx.sqlite.SQLiteStatement
    public void bindBlob(int i, byte[] value) {
        Intrinsics.checkNotNullParameter(value, "value");
        this.$$delegate_0.bindBlob(i, value);
    }

    @Override // androidx.sqlite.SQLiteStatement
    public void bindDouble(int i, double d) {
        this.$$delegate_0.bindDouble(i, d);
    }

    @Override // androidx.sqlite.SQLiteStatement
    public void bindLong(int i, long j) {
        this.$$delegate_0.bindLong(i, j);
    }

    @Override // androidx.sqlite.SQLiteStatement
    public void bindNull(int i) {
        this.$$delegate_0.bindNull(i);
    }

    @Override // androidx.sqlite.SQLiteStatement
    public void bindText(int i, String value) {
        Intrinsics.checkNotNullParameter(value, "value");
        this.$$delegate_0.bindText(i, value);
    }

    @Override // androidx.sqlite.SQLiteStatement
    public void clearBindings() {
        this.$$delegate_0.clearBindings();
    }

    @Override // androidx.sqlite.SQLiteStatement
    public boolean getBoolean(int i) {
        return this.$$delegate_0.getBoolean(i);
    }

    public BindOnlySQLiteStatement(SQLiteStatement delegate) {
        Intrinsics.checkNotNullParameter(delegate, "delegate");
        this.$$delegate_0 = delegate;
    }

    @Override // androidx.sqlite.SQLiteStatement
    public byte[] getBlob(int i) {
        throw new IllegalStateException("Only bind*() calls are allowed on the RoomRawQuery received statement.");
    }

    @Override // androidx.sqlite.SQLiteStatement
    public long getLong(int i) {
        throw new IllegalStateException("Only bind*() calls are allowed on the RoomRawQuery received statement.");
    }

    @Override // androidx.sqlite.SQLiteStatement
    public String getText(int i) {
        throw new IllegalStateException("Only bind*() calls are allowed on the RoomRawQuery received statement.");
    }

    @Override // androidx.sqlite.SQLiteStatement
    public boolean isNull(int i) {
        throw new IllegalStateException("Only bind*() calls are allowed on the RoomRawQuery received statement.");
    }

    @Override // androidx.sqlite.SQLiteStatement
    public int getColumnCount() {
        throw new IllegalStateException("Only bind*() calls are allowed on the RoomRawQuery received statement.");
    }

    @Override // androidx.sqlite.SQLiteStatement
    public String getColumnName(int i) {
        throw new IllegalStateException("Only bind*() calls are allowed on the RoomRawQuery received statement.");
    }

    @Override // androidx.sqlite.SQLiteStatement
    public boolean step() {
        throw new IllegalStateException("Only bind*() calls are allowed on the RoomRawQuery received statement.");
    }

    @Override // androidx.sqlite.SQLiteStatement
    public void reset() {
        throw new IllegalStateException("Only bind*() calls are allowed on the RoomRawQuery received statement.");
    }

    @Override // androidx.sqlite.SQLiteStatement, java.lang.AutoCloseable
    public void close() {
        throw new IllegalStateException("Only bind*() calls are allowed on the RoomRawQuery received statement.");
    }

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
