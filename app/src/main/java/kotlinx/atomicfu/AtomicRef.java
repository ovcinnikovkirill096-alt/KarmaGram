package kotlinx.atomicfu;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class AtomicRef {
    private static final Companion Companion = new Companion(null);
    private static final AtomicReferenceFieldUpdater FU = AtomicReferenceFieldUpdater.newUpdater(AtomicRef.class, Object.class, "value");
    private final TraceBase trace;
    private volatile Object value;

    public AtomicRef(Object obj, TraceBase trace) {
        Intrinsics.checkNotNullParameter(trace, "trace");
        this.trace = trace;
        this.value = obj;
    }

    public final Object getValue() {
        return this.value;
    }

    public final void setValue(Object obj) {
        this.value = obj;
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("set(" + obj + ')');
        }
    }

    public final boolean compareAndSet(Object obj, Object obj2) {
        TraceBase traceBase;
        boolean zM = AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(FU, this, obj, obj2);
        if (zM && (traceBase = this.trace) != TraceBase.None.INSTANCE) {
            traceBase.append("CAS(" + obj + ", " + obj2 + ')');
        }
        return zM;
    }

    public final Object getAndSet(Object obj) {
        Object andSet = FU.getAndSet(this, obj);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("getAndSet(" + obj + "):" + andSet);
        }
        return andSet;
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
