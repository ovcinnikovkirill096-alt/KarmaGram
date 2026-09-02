package androidx.camera.camera2.pipe.compat;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public interface RetryingCameraStateOpener {
    void cancelOpen();

    /* JADX INFO: renamed from: openAndAwaitCameraWithRetry-0r8Bogc, reason: not valid java name */
    AwaitOpenCameraResult mo491openAndAwaitCameraWithRetry0r8Bogc(String str, Camera2DeviceCloser camera2DeviceCloser);

    /* JADX INFO: renamed from: openCameraWithRetry-aeCOTgg, reason: not valid java name */
    Object mo492openCameraWithRetryaeCOTgg(String str, Camera2DeviceCloser camera2DeviceCloser, Function1 function1, Continuation continuation);

    /* JADX INFO: renamed from: androidx.camera.camera2.pipe.compat.RetryingCameraStateOpener$-CC, reason: invalid class name */
    public abstract /* synthetic */ class CC {
        public static boolean openCameraWithRetry_aeCOTgg$lambda$0(Unit unit) {
            Intrinsics.checkNotNullParameter(unit, "<unused var>");
            return true;
        }

        /* JADX INFO: renamed from: openCameraWithRetry-aeCOTgg$default, reason: not valid java name */
        public static /* synthetic */ Object m493openCameraWithRetryaeCOTgg$default(RetryingCameraStateOpener retryingCameraStateOpener, String str, Camera2DeviceCloser camera2DeviceCloser, Function1 function1, Continuation continuation, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: openCameraWithRetry-aeCOTgg");
            }
            if ((i & 4) != 0) {
                function1 = new Function1() { // from class: androidx.camera.camera2.pipe.compat.RetryingCameraStateOpener$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Boolean.valueOf(RetryingCameraStateOpener.CC.openCameraWithRetry_aeCOTgg$lambda$0((Unit) obj2));
                    }
                };
            }
            return retryingCameraStateOpener.mo492openCameraWithRetryaeCOTgg(str, camera2DeviceCloser, function1, continuation);
        }
    }
}
