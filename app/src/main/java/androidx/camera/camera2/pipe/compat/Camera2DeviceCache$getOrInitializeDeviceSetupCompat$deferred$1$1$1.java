package androidx.camera.camera2.pipe.compat;

import android.hardware.camera2.CameraAccessException;
import androidx.camera.camera2.pipe.CameraError;
import androidx.camera.camera2.pipe.CameraId;
import androidx.camera.camera2.pipe.core.Log;
import androidx.camera.camera2.pipe.internal.CameraErrorListener;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

final class Camera2DeviceCache$getOrInitializeDeviceSetupCompat$deferred$1$1$1 extends SuspendLambda implements Function2 {
    final /* synthetic */ String $cameraId;
    int label;
    final /* synthetic */ Camera2DeviceCache this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    Camera2DeviceCache$getOrInitializeDeviceSetupCompat$deferred$1$1$1(String str, Camera2DeviceCache camera2DeviceCache, Continuation continuation) {
        super(2, continuation);
        this.$cameraId = str;
        this.this$0 = camera2DeviceCache;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new Camera2DeviceCache$getOrInitializeDeviceSetupCompat$deferred$1$1$1(this.$cameraId, this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
        return ((Camera2DeviceCache$getOrInitializeDeviceSetupCompat$deferred$1$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) throws Exception {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label != 0) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ResultKt.throwOnFailure(obj);
        Log log = Log.INSTANCE;
        String str = this.$cameraId;
        if (log.getDEBUG_LOGGABLE()) {
            android.util.Log.d("CXCP", "Initializing CameraDeviceSetupCompat for " + ((Object) CameraId.m238toStringimpl(str)));
        }
        String str2 = this.$cameraId;
        CameraErrorListener cameraErrorListener = this.this$0.cameraErrorListener;
        try {
            return this.this$0.getCameraDeviceSetupCompatFactory().getCameraDeviceSetupCompat(this.$cameraId);
        } catch (Exception e) {
            if (e instanceof CameraAccessException) {
                if (Log.INSTANCE.getWARN_LOGGABLE()) {
                    android.util.Log.w("CXCP", "Failed to execute call: Camera encountered an error: " + e.getMessage());
                }
                cameraErrorListener.mo463onCameraError3M5Xam4(str2, CameraError.Companion.m190fromPVuDhNw$camera_camera2_pipe((CameraAccessException) e), true);
                return null;
            }
            if (!(e instanceof IllegalArgumentException) && !(e instanceof SecurityException) && !(e instanceof UnsupportedOperationException) && !(e instanceof NullPointerException)) {
                if (!(e instanceof IllegalStateException)) {
                    throw e;
                }
                if (!Log.INSTANCE.getDEBUG_LOGGABLE()) {
                    return null;
                }
                android.util.Log.d("CXCP", "Failed to execute call: Camera may be closed");
                return null;
            }
            if (Log.INSTANCE.getWARN_LOGGABLE()) {
                android.util.Log.w("CXCP", "Failed to execute call: Unexpected exception: " + e.getMessage());
            }
            cameraErrorListener.mo463onCameraError3M5Xam4(str2, CameraError.Companion.m201getERROR_GRAPH_CONFIGv7Vf74A(), false);
            return null;
        }
    }
}
