package androidx.datastore.core;

import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public final class DataStoreInMemoryCache {
    private final MutableStateFlow cachedValue;

    public DataStoreInMemoryCache() {
        UnInitialized unInitialized = UnInitialized.INSTANCE;
        Intrinsics.checkNotNull(unInitialized, "null cannot be cast to non-null type androidx.datastore.core.State<T of androidx.datastore.core.DataStoreInMemoryCache>");
        this.cachedValue = StateFlowKt.MutableStateFlow(unInitialized);
    }

    public final State getCurrentState() {
        return (State) this.cachedValue.getValue();
    }

    public final Flow getFlow() {
        return this.cachedValue;
    }

    /* JADX WARN: Code duplicated, block: B:13:0x002c  */
    public final State tryUpdate(State newState) {
        Object value;
        State state;
        Intrinsics.checkNotNullParameter(newState, "newState");
        MutableStateFlow mutableStateFlow = this.cachedValue;
        do {
            value = mutableStateFlow.getValue();
            state = (State) value;
            if (state instanceof ReadException ? true : Intrinsics.areEqual(state, UnInitialized.INSTANCE)) {
                state = newState;
            } else if (state instanceof Data) {
                if (newState.getVersion() > state.getVersion()) {
                    state = newState;
                }
            } else if (!(state instanceof Final)) {
                throw new NoWhenBranchMatchedException();
            }
        } while (!mutableStateFlow.compareAndSet(value, state));
        return state;
    }
}
