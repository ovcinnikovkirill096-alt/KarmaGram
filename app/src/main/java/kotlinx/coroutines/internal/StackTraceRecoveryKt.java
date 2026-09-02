package kotlinx.coroutines.internal;

import _COROUTINE.ArtificialStackFrames;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;

public abstract class StackTraceRecoveryKt {
    private static final StackTraceElement ARTIFICIAL_FRAME = new ArtificialStackFrames().coroutineBoundary();
    private static final String baseContinuationImplClassName;
    private static final String stackTraceRecoveryClassName;

    public static final Throwable recoverStackTrace(Throwable th) {
        return th;
    }

    static {
        Object objM2437constructorimpl;
        Object objM2437constructorimpl2;
        try {
            Result.Companion companion = Result.Companion;
            objM2437constructorimpl = Result.m2437constructorimpl(BaseContinuationImpl.class.getCanonicalName());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            objM2437constructorimpl = Result.m2437constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m2439exceptionOrNullimpl(objM2437constructorimpl) != null) {
            objM2437constructorimpl = "kotlin.coroutines.jvm.internal.BaseContinuationImpl";
        }
        baseContinuationImplClassName = (String) objM2437constructorimpl;
        try {
            objM2437constructorimpl2 = Result.m2437constructorimpl(StackTraceRecoveryKt.class.getCanonicalName());
        } catch (Throwable th2) {
            Result.Companion companion3 = Result.Companion;
            objM2437constructorimpl2 = Result.m2437constructorimpl(ResultKt.createFailure(th2));
        }
        if (Result.m2439exceptionOrNullimpl(objM2437constructorimpl2) != null) {
            objM2437constructorimpl2 = "kotlinx.coroutines.internal.StackTraceRecoveryKt";
        }
        stackTraceRecoveryClassName = (String) objM2437constructorimpl2;
    }
}
