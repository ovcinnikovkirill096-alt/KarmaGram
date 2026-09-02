package androidx.work.impl.model;

import kotlin.jvm.internal.Intrinsics;

public final class WorkName {
    private final String name;
    private final String workSpecId;

    public WorkName(String name, String workSpecId) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(workSpecId, "workSpecId");
        this.name = name;
        this.workSpecId = workSpecId;
    }

    public final String getName() {
        return this.name;
    }

    public final String getWorkSpecId() {
        return this.workSpecId;
    }
}
