package androidx.camera.camera2.pipe.compat;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CoroutineScope;

final class Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$success$1 extends SuspendLambda implements Function2 {
    final /* synthetic */ CompletableDeferred $listener;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$success$1(CompletableDeferred completableDeferred, Continuation continuation) {
        super(2, continuation);
        this.$listener = completableDeferred;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$success$1(this.$listener, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
        return ((Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$success$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            CompletableDeferred completableDeferred = this.$listener;
            this.label = 1;
            if (completableDeferred.await(this) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            if (i != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
        }
        return Unit.INSTANCE;
    }
}
