package androidx.camera.camera2.pipe.compat;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;

final class Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1 extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;
    final /* synthetic */ Camera2CameraAvailabilityMonitor$startMonitoring$2 this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    Camera2CameraAvailabilityMonitor$startMonitoring$2$awaitAvailableCamera$1(Camera2CameraAvailabilityMonitor$startMonitoring$2 camera2CameraAvailabilityMonitor$startMonitoring$2, Continuation continuation) {
        super(continuation);
        this.this$0 = camera2CameraAvailabilityMonitor$startMonitoring$2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.awaitAvailableCamera(0L, this);
    }
}
