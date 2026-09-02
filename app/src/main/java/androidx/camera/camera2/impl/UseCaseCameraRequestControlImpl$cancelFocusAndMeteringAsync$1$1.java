package androidx.camera.camera2.impl;

import android.util.Log;
import androidx.camera.camera2.pipe.CameraControls3A;
import androidx.camera.camera2.pipe.CameraGraph;
import androidx.camera.core.Logger;
import java.util.concurrent.CancellationException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jdk7.AutoCloseableKt;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.Deferred;

final class UseCaseCameraRequestControlImpl$cancelFocusAndMeteringAsync$1$1 extends SuspendLambda implements Function1 {
    Object L$0;
    int label;
    final /* synthetic */ UseCaseCameraRequestControlImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    UseCaseCameraRequestControlImpl$cancelFocusAndMeteringAsync$1$1(UseCaseCameraRequestControlImpl useCaseCameraRequestControlImpl, Continuation continuation) {
        super(1, continuation);
        this.this$0 = useCaseCameraRequestControlImpl;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Continuation continuation) {
        return new UseCaseCameraRequestControlImpl$cancelFocusAndMeteringAsync$1$1(this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Object invoke(Continuation continuation) {
        return ((UseCaseCameraRequestControlImpl$cancelFocusAndMeteringAsync$1$1) create(continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code duplicated, block: B:56:0x00cf  */
    /* JADX WARN: Code duplicated, block: B:61:0x00e5 A[PHI: r13
  0x00e5: PHI (r13v5 ??) = (r13v18 ??), (r13v15 ??) binds: [B:59:0x00e2, B:14:0x002e] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00f6, code lost:
    
        if (r0 == r11) goto L64;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1 */
    /* JADX WARN: Type inference failed for: r13v10 */
    /* JADX WARN: Type inference failed for: r13v15 */
    /* JADX WARN: Type inference failed for: r13v16 */
    /* JADX WARN: Type inference failed for: r13v17 */
    /* JADX WARN: Type inference failed for: r13v18 */
    /* JADX WARN: Type inference failed for: r13v2 */
    /* JADX WARN: Type inference failed for: r13v20 */
    /* JADX WARN: Type inference failed for: r13v21 */
    /* JADX WARN: Type inference failed for: r13v23 */
    /* JADX WARN: Type inference failed for: r13v24 */
    /* JADX WARN: Type inference failed for: r13v3 */
    /* JADX WARN: Type inference failed for: r13v4, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r13v5 */
    /* JADX WARN: Type inference failed for: r13v6, types: [java.lang.Throwable] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object obj) throws Exception {
        Deferred deferred;
        ?? r13;
        ?? r14;
        Object objAcquireSession;
        ?? r15;
        Object objAcquireSession2;
        AutoCloseable autoCloseable;
        Throwable th;
        AutoCloseable autoCloseable2;
        Throwable th2;
        AutoCloseable autoCloseable3;
        CameraGraph.Session session;
        Boolean boolBoxBoolean;
        Boolean boolBoxBoolean2;
        Boolean boolBoxBoolean3;
        Object objUnlock3A$default;
        AutoCloseable autoCloseable4;
        Throwable th3;
        Throwable th4;
        Throwable th5;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        ?? r16 = 4;
        try {
            try {
                try {
                    try {
                        try {
                            if (i == 0) {
                                ResultKt.throwOnFailure(obj);
                                Camera2Logger camera2Logger = Camera2Logger.INSTANCE;
                                if (Logger.isDebugEnabled("CXCP")) {
                                    Log.d(Camera2Logger.TRUNCATED_TAG, "UseCaseCameraRequestControlImpl#cancelFocusAndMeteringAsync");
                                }
                                CameraGraph graph = this.this$0.useCaseGraphContext.getGraph();
                                this.label = 1;
                                objAcquireSession2 = graph.acquireSession(this);
                                if (objAcquireSession2 == coroutine_suspended) {
                                }
                                return coroutine_suspended;
                            }
                            if (i != 1) {
                                if (i == 2) {
                                    autoCloseable4 = (AutoCloseable) this.L$0;
                                    try {
                                        ResultKt.throwOnFailure(obj);
                                        objUnlock3A$default = obj;
                                        th5 = null;
                                        try {
                                            deferred = (Deferred) objUnlock3A$default;
                                            AutoCloseableKt.closeFinally(autoCloseable4, th5);
                                            r13 = th5;
                                            this.L$0 = r13;
                                            this.label = 3;
                                            r14 = r13;
                                            if (deferred.await(this) != coroutine_suspended) {
                                                CameraGraph graph2 = this.this$0.useCaseGraphContext.getGraph();
                                                this.label = 4;
                                                objAcquireSession = graph2.acquireSession(this);
                                                r15 = r14;
                                            }
                                            return coroutine_suspended;
                                        } catch (Throwable th6) {
                                            th4 = th6;
                                            autoCloseable3 = autoCloseable4;
                                            th3 = th5;
                                            th2 = th4;
                                            r16 = th3;
                                            try {
                                                throw th2;
                                            } catch (Throwable th7) {
                                                AutoCloseableKt.closeFinally(autoCloseable3, th2);
                                                throw th7;
                                            }
                                        }
                                    } catch (Throwable th8) {
                                        th4 = th8;
                                        autoCloseable3 = autoCloseable4;
                                        th3 = null;
                                        th2 = th4;
                                        r16 = th3;
                                        throw th2;
                                    }
                                }
                                if (i == 3) {
                                    ResultKt.throwOnFailure(obj);
                                    r14 = 0;
                                    CameraGraph graph3 = this.this$0.useCaseGraphContext.getGraph();
                                    this.label = 4;
                                    objAcquireSession = graph3.acquireSession(this);
                                    r15 = r14;
                                } else {
                                    if (i != 4) {
                                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                    }
                                    ResultKt.throwOnFailure(obj);
                                    objAcquireSession = obj;
                                    r15 = 0;
                                }
                                AutoCloseable autoCloseable5 = (AutoCloseable) objAcquireSession;
                                try {
                                    CameraGraph.Constants3A constants3A = CameraGraph.Constants3A.INSTANCE;
                                    Deferred deferredM172update3AydBZfZg$default = CameraControls3A.CC.m172update3AydBZfZg$default((CameraGraph.Session) autoCloseable5, null, null, null, ArraysKt.asList(constants3A.getMETERING_REGIONS_DEFAULT()), ArraysKt.asList(constants3A.getMETERING_REGIONS_DEFAULT()), ArraysKt.asList(constants3A.getMETERING_REGIONS_DEFAULT()), 7, null);
                                    AutoCloseableKt.closeFinally(autoCloseable5, r15);
                                    return deferredM172update3AydBZfZg$default;
                                } catch (Throwable th9) {
                                    try {
                                        throw th9;
                                    } catch (Throwable th10) {
                                        AutoCloseableKt.closeFinally(autoCloseable5, th9);
                                        throw th10;
                                    }
                                }
                            }
                            ResultKt.throwOnFailure(obj);
                            objAcquireSession2 = obj;
                            objUnlock3A$default = CameraGraph.Session.CC.unlock3A$default(session, boolBoxBoolean, boolBoxBoolean2, boolBoxBoolean3, null, 0, 0L, this, 56, null);
                            if (objUnlock3A$default != coroutine_suspended) {
                                autoCloseable4 = autoCloseable2;
                                th5 = th;
                                deferred = (Deferred) objUnlock3A$default;
                                AutoCloseableKt.closeFinally(autoCloseable4, th5);
                                r13 = th5;
                                this.L$0 = r13;
                                this.label = 3;
                                r14 = r13;
                                if (deferred.await(this) != coroutine_suspended) {
                                    CameraGraph graph4 = this.this$0.useCaseGraphContext.getGraph();
                                    this.label = 4;
                                    objAcquireSession = graph4.acquireSession(this);
                                    r15 = r14;
                                }
                            }
                            return coroutine_suspended;
                        } catch (Throwable th11) {
                            th = th11;
                            th2 = th;
                            autoCloseable3 = autoCloseable2;
                            r16 = th;
                            throw th2;
                        }
                        session = (CameraGraph.Session) autoCloseable;
                        boolBoxBoolean = Boxing.boxBoolean(true);
                        boolBoxBoolean2 = Boxing.boxBoolean(true);
                        boolBoxBoolean3 = Boxing.boxBoolean(true);
                        this.L$0 = autoCloseable;
                        this.label = 2;
                        autoCloseable2 = autoCloseable;
                        th = null;
                    } catch (Throwable th12) {
                        th = th12;
                        th = null;
                        autoCloseable2 = autoCloseable;
                    }
                    autoCloseable = (AutoCloseable) objAcquireSession2;
                } catch (CancellationException e) {
                    e = e;
                    Camera2Logger camera2Logger2 = Camera2Logger.INSTANCE;
                    if (Logger.isDebugEnabled("CXCP")) {
                        Log.d(Camera2Logger.TRUNCATED_TAG, "Cannot acquire the CameraGraph.Session", e);
                    }
                    deferred = UseCaseCameraRequestControlImpl.submitFailedResult;
                    r13 = r16;
                    this.L$0 = r13;
                    this.label = 3;
                    r14 = r13;
                    if (deferred.await(this) != coroutine_suspended) {
                        CameraGraph graph5 = this.this$0.useCaseGraphContext.getGraph();
                        this.label = 4;
                        objAcquireSession = graph5.acquireSession(this);
                        r15 = r14;
                    }
                    return coroutine_suspended;
                }
            } catch (CancellationException e2) {
                e = e2;
                r16 = 0;
                Camera2Logger camera2Logger3 = Camera2Logger.INSTANCE;
                if (Logger.isDebugEnabled("CXCP")) {
                    Log.d(Camera2Logger.TRUNCATED_TAG, "Cannot acquire the CameraGraph.Session", e);
                }
                deferred = UseCaseCameraRequestControlImpl.submitFailedResult;
                r13 = r16;
            }
        } catch (CancellationException e3) {
            Camera2Logger camera2Logger4 = Camera2Logger.INSTANCE;
            if (Logger.isDebugEnabled("CXCP")) {
                Log.d(Camera2Logger.TRUNCATED_TAG, "Cannot acquire the CameraGraph.Session", e3);
            }
            return UseCaseCameraRequestControlImpl.submitFailedResult;
        }
    }
}
