package androidx.camera.core;

import com.google.common.util.concurrent.ListenableFuture;

public interface CameraControl {
    ListenableFuture enableTorch(boolean z);

    ListenableFuture setZoomRatio(float f);

    ListenableFuture startFocusAndMetering(FocusMeteringAction focusMeteringAction);

    public static final class OperationCanceledException extends Exception {
        public OperationCanceledException(String str) {
            super(str);
        }
    }
}
