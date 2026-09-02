package androidx.work.impl;

import java.util.concurrent.TimeUnit;

public abstract class WorkDatabaseKt {
    public static final long PRUNE_THRESHOLD_MILLIS = TimeUnit.DAYS.toMillis(1);
}
