package kotlinx.atomicfu;

import kotlin.jvm.internal.Intrinsics;

public abstract class TraceBase {
    public void append(Object event) {
        Intrinsics.checkNotNullParameter(event, "event");
    }

    public static final class None extends TraceBase {
        public static final None INSTANCE = new None();

        private None() {
        }
    }
}
