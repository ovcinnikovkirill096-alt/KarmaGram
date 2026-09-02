package androidx.room;

import androidx.sqlite.SQLiteStatement;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public final class RoomRawQuery {
    private final Function1 bindingFunction;
    private final String sql;

    public RoomRawQuery(String sql, final Function1 onBindStatement) {
        Intrinsics.checkNotNullParameter(sql, "sql");
        Intrinsics.checkNotNullParameter(onBindStatement, "onBindStatement");
        this.sql = sql;
        this.bindingFunction = new Function1() { // from class: androidx.room.RoomRawQuery$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RoomRawQuery.bindingFunction$lambda$1(onBindStatement, (SQLiteStatement) obj);
            }
        };
    }

    public final String getSql() {
        return this.sql;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit bindingFunction$lambda$1(Function1 function1, SQLiteStatement it) {
        Intrinsics.checkNotNullParameter(it, "it");
        function1.invoke(new BindOnlySQLiteStatement(it));
        return Unit.INSTANCE;
    }

    public final Function1 getBindingFunction() {
        return this.bindingFunction;
    }
}
