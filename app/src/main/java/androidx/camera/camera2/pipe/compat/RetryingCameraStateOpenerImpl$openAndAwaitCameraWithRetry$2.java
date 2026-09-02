package androidx.camera.camera2.pipe.compat;

import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.camera2.pipe.core.Log;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.StateFlow;

final class RetryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2 extends SuspendLambda implements Function2 {
    final /* synthetic */ Camera2DeviceCloser $camera2DeviceCloser;
    final /* synthetic */ String $cameraId;
    Object L$0;
    int label;
    final /* synthetic */ RetryingCameraStateOpenerImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    RetryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2(RetryingCameraStateOpenerImpl retryingCameraStateOpenerImpl, String str, Camera2DeviceCloser camera2DeviceCloser, Continuation continuation) {
        super(2, continuation);
        this.this$0 = retryingCameraStateOpenerImpl;
        this.$cameraId = str;
        this.$camera2DeviceCloser = camera2DeviceCloser;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new RetryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2(this.this$0, this.$cameraId, this.$camera2DeviceCloser, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
        return ((RetryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0085, code lost:
    
        if (r1 == r7) goto L22;
     */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object obj) {
        Object objM493openCameraWithRetryaeCOTgg$default;
        AndroidCameraState cameraState;
        Object objFirst;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            RetryingCameraStateOpenerImpl retryingCameraStateOpenerImpl = this.this$0;
            String str = this.$cameraId;
            Camera2DeviceCloser camera2DeviceCloser = this.$camera2DeviceCloser;
            this.label = 1;
            objM493openCameraWithRetryaeCOTgg$default = RetryingCameraStateOpener.CC.m493openCameraWithRetryaeCOTgg$default(retryingCameraStateOpenerImpl, str, camera2DeviceCloser, null, this, 4, null);
            if (objM493openCameraWithRetryaeCOTgg$default != coroutine_suspended) {
            }
            return coroutine_suspended;
        }
        if (i == 1) {
            ResultKt.throwOnFailure(obj);
            objM493openCameraWithRetryaeCOTgg$default = obj;
        } else {
            if (i != 2) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            cameraState = (AndroidCameraState) this.L$0;
            ResultKt.throwOnFailure(obj);
            objFirst = obj;
        }
        CameraState cameraState2 = (CameraState) objFirst;
        if (cameraState2 instanceof CameraStateOpen) {
            Log log = Log.INSTANCE;
            String str2 = this.$cameraId;
            if (log.getINFO_LOGGABLE()) {
                android.util.Log.i("CXCP", ((Object) CameraId.m238toStringimpl(str2)) + " opened successfully.");
            }
            return new AwaitOpenCameraResult(((CameraStateOpen) cameraState2).getCameraDevice(), cameraState);
        }
        Log log2 = Log.INSTANCE;
        String str3 = this.$cameraId;
        if (log2.getERROR_LOGGABLE()) {
            android.util.Log.e("CXCP", "Failed to open " + ((Object) CameraId.m238toStringimpl(str3)) + '!');
        }
        return new AwaitOpenCameraResult(null, null);
        cameraState = ((OpenCameraResult) objM493openCameraWithRetryaeCOTgg$default).getCameraState();
        if (cameraState == null) {
            Log log3 = Log.INSTANCE;
            String str4 = this.$cameraId;
            if (log3.getERROR_LOGGABLE()) {
                android.util.Log.e("CXCP", "Failed to open " + ((Object) CameraId.m238toStringimpl(str4)) + '!');
            }
            return new AwaitOpenCameraResult(null, null);
        }
        StateFlow state = cameraState.getState();
        RetryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2$cameraState$1 retryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2$cameraState$1 = new RetryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2$cameraState$1(null);
        this.L$0 = cameraState;
        this.label = 2;
        objFirst = FlowKt.first(state, retryingCameraStateOpenerImpl$openAndAwaitCameraWithRetry$2$cameraState$1, this);
    }
}
