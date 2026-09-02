package androidx.datastore.migrations;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class SharedPreferencesMigration_androidKt {
    private static final Set MIGRATE_ALL_KEYS = new LinkedHashSet();

    public static final Set getMIGRATE_ALL_KEYS() {
        return MIGRATE_ALL_KEYS;
    }
}
