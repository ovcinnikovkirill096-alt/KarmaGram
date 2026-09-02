package kotlinx.coroutines;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;

public abstract class CompletionStateKt {
    public static final Object toState(Object obj) {
        Throwable thM2439exceptionOrNullimpl = Result.m2439exceptionOrNullimpl(obj);
        return thM2439exceptionOrNullimpl == null ? obj : new CompletedExceptionally(thM2439exceptionOrNullimpl, false, 2, null);
    }

    public static final Object toState(Object obj, CancellableContinuation cancellableContinuation) {
        Throwable thM2439exceptionOrNullimpl = Result.m2439exceptionOrNullimpl(obj);
        return thM2439exceptionOrNullimpl == null ? obj : new CompletedExceptionally(thM2439exceptionOrNullimpl, false, 2, null);
    }

    public static final Object recoverResult(Object obj, Continuation continuation) {
        if (obj instanceof CompletedExceptionally) {
            Result.Companion companion = Result.Companion;
            return Result.m2437constructorimpl(ResultKt.createFailure(((CompletedExceptionally) obj).cause));
        }
        return Result.m2437constructorimpl(obj);
    }
}
