package androidx.core.os;

import android.os.OutcomeReceiver;
import kotlin.coroutines.Continuation;

public abstract class OutcomeReceiverKt {
    public static final OutcomeReceiver asOutcomeReceiver(Continuation continuation) {
        return new ContinuationOutcomeReceiver(continuation);
    }
}
