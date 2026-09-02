package androidx.camera.camera2.pipe.compat;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

final class RetryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2$cameraState$1 extends SuspendLambda implements Function2 {
    /* synthetic */ Object L$0;
    int label;

    RetryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2$cameraState$1(Continuation continuation) {
        super(2, continuation);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        RetryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2$cameraState$1 retryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2$cameraState$1 = new RetryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2$cameraState$1(continuation);
        retryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2$cameraState$1.L$0 = obj;
        return retryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2$cameraState$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CameraState cameraState, Continuation continuation) {
        return ((RetryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2$cameraState$1) create(cameraState, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label != 0) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ResultKt.throwOnFailure(obj);
        return Boxing.boxBoolean(!Intrinsics.areEqual((CameraState) this.L$0, CameraStateUnopened.INSTANCE));
    }
}
