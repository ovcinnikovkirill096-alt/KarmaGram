package kotlinx.coroutines.internal;

import java.util.concurrent.CancellationException;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CompletionStateKt;
import kotlinx.coroutines.CoroutineContextKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.DispatchException;
import kotlinx.coroutines.EventLoop;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.ThreadLocalEventLoop;
import kotlinx.coroutines.UndispatchedCoroutine;

public abstract class DispatchedContinuationKt {
    private static final Symbol UNDEFINED = new Symbol("UNDEFINED");
    public static final Symbol REUSABLE_CLAIMED = new Symbol("REUSABLE_CLAIMED");

    public static final void safeDispatch(CoroutineDispatcher coroutineDispatcher, CoroutineContext coroutineContext, Runnable runnable) {
        try {
            coroutineDispatcher.dispatch(coroutineContext, runnable);
        } catch (Throwable th) {
            throw new DispatchException(th, coroutineDispatcher, coroutineContext);
        }
    }

    public static final boolean safeIsDispatchNeeded(CoroutineDispatcher coroutineDispatcher, CoroutineContext coroutineContext) throws DispatchException {
        try {
            return coroutineDispatcher.isDispatchNeeded(coroutineContext);
        } catch (Throwable th) {
            throw new DispatchException(th, coroutineDispatcher, coroutineContext);
        }
    }

    public static final void resumeCancellableWith(Continuation continuation, Object obj) {
        if (continuation instanceof DispatchedContinuation) {
            DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
            Object state = CompletionStateKt.toState(obj);
            if (safeIsDispatchNeeded(dispatchedContinuation.dispatcher, dispatchedContinuation.getContext())) {
                dispatchedContinuation._state = state;
                dispatchedContinuation.resumeMode = 1;
                safeDispatch(dispatchedContinuation.dispatcher, dispatchedContinuation.getContext(), dispatchedContinuation);
                return;
            }
            EventLoop eventLoop$kotlinx_coroutines_core = ThreadLocalEventLoop.INSTANCE.getEventLoop$kotlinx_coroutines_core();
            if (!eventLoop$kotlinx_coroutines_core.isUnconfinedLoopActive()) {
                eventLoop$kotlinx_coroutines_core.incrementUseCount(true);
                try {
                    Job job = (Job) dispatchedContinuation.getContext().get(Job.Key);
                    if (job != null && !job.isActive()) {
                        CancellationException cancellationException = job.getCancellationException();
                        dispatchedContinuation.cancelCompletedResult$kotlinx_coroutines_core(state, cancellationException);
                        Result.Companion companion = Result.Companion;
                        dispatchedContinuation.resumeWith(Result.m2437constructorimpl(ResultKt.createFailure(cancellationException)));
                    } else {
                        Continuation continuation2 = dispatchedContinuation.continuation;
                        Object obj2 = dispatchedContinuation.countOrElement;
                        CoroutineContext context = continuation2.getContext();
                        Object objUpdateThreadContext = ThreadContextKt.updateThreadContext(context, obj2);
                        UndispatchedCoroutine undispatchedCoroutineUpdateUndispatchedCompletion = objUpdateThreadContext != ThreadContextKt.NO_THREAD_ELEMENTS ? CoroutineContextKt.updateUndispatchedCompletion(continuation2, context, objUpdateThreadContext) : null;
                        try {
                            dispatchedContinuation.continuation.resumeWith(obj);
                            Unit unit = Unit.INSTANCE;
                            if (undispatchedCoroutineUpdateUndispatchedCompletion == null || undispatchedCoroutineUpdateUndispatchedCompletion.clearThreadContext()) {
                                ThreadContextKt.restoreThreadContext(context, objUpdateThreadContext);
                            }
                        } catch (Throwable th) {
                            if (undispatchedCoroutineUpdateUndispatchedCompletion == null || undispatchedCoroutineUpdateUndispatchedCompletion.clearThreadContext()) {
                                ThreadContextKt.restoreThreadContext(context, objUpdateThreadContext);
                            }
                            throw th;
                        }
                    }
                    while (eventLoop$kotlinx_coroutines_core.processUnconfinedEvent()) {
                    }
                } catch (Throwable th2) {
                    try {
                        dispatchedContinuation.handleFatalException$kotlinx_coroutines_core(th2);
                    } finally {
                        eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
                    }
                }
                return;
            }
            dispatchedContinuation._state = state;
            dispatchedContinuation.resumeMode = 1;
            eventLoop$kotlinx_coroutines_core.dispatchUnconfined(dispatchedContinuation);
            return;
        }
        continuation.resumeWith(obj);
    }

    public static final boolean yieldUndispatched(DispatchedContinuation dispatchedContinuation) {
        Unit unit = Unit.INSTANCE;
        EventLoop eventLoop$kotlinx_coroutines_core = ThreadLocalEventLoop.INSTANCE.getEventLoop$kotlinx_coroutines_core();
        if (eventLoop$kotlinx_coroutines_core.isUnconfinedQueueEmpty()) {
            return false;
        }
        if (!eventLoop$kotlinx_coroutines_core.isUnconfinedLoopActive()) {
            eventLoop$kotlinx_coroutines_core.incrementUseCount(true);
            try {
                dispatchedContinuation.run();
                do {
                } while (eventLoop$kotlinx_coroutines_core.processUnconfinedEvent());
            } catch (Throwable th) {
                try {
                    dispatchedContinuation.handleFatalException$kotlinx_coroutines_core(th);
                } finally {
                    eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
                }
            }
            return false;
        }
        dispatchedContinuation._state = unit;
        dispatchedContinuation.resumeMode = 1;
        eventLoop$kotlinx_coroutines_core.dispatchUnconfined(dispatchedContinuation);
        return true;
    }
}
